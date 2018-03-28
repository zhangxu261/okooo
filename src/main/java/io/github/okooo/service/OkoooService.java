package io.github.okooo.service;

import io.github.okooo.domain.Game;
import io.github.okooo.domain.Idx;
import io.github.okooo.mapper.GameMapper;
import io.github.okooo.mapper.IdxMapper;
import io.github.okooo.util.SimpleHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OkoooService {
    private final GameMapper gameMapper;
    private final IdxMapper idxMapper;

    public OkoooService(GameMapper gameMapper,
                        IdxMapper idxMapper) {
        this.gameMapper = gameMapper;
        this.idxMapper = idxMapper;
    }

    @Scheduled(cron = "1 */1 * * * ?")
    public void fetchIdx() {
        String url = "http://www.okooo.com/jingcai/shuju/zhishu/";
        String html = SimpleHttpClient.getCurrent().get(url).getResponseText();
        Document doc = Jsoup.parse(html);

        Elements trs = doc.select("table tr");
        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");

            String serial = tds.get(0).text();
            String matchType = tds.get(1).text();
            String matchTime = tds.get(2).text();
            String host = tds.get(3).text();
            String guest = tds.get(5).text();
            String w = tds.get(9).text().replaceAll("\\s", "");
            String d = tds.get(10).text().replaceAll("\\s", "");
            String l = tds.get(11).text().replaceAll("\\s", "");
            String r = tds.get(12).text().replaceAll("\\s", "");
            String result = tds.get(13).text();

            if (StringUtils.isEmpty(result)) {
                Idx last = idxMapper.findLastOne(serial, matchTime);
                boolean same = last != null && last.getWin().equals(w) && last.getDraw().equals(d) && last.getLose().equals(l);
                if (!same) {
                    Idx idx = new Idx();
                    idx.setSerial(serial);
                    idx.setMatchTime(matchTime);
                    idx.setWin(w);
                    idx.setDraw(d);
                    idx.setLose(l);
                    idx.setRemind(r);
                    idxMapper.insert(idx);
                }

            }

            Game existed = gameMapper.findOneBySerialAndMatchTime(serial, matchTime);
            if (existed == null) {
                Game okooo = new Game();
                okooo.setSerial(serial);
                okooo.setMatchType(matchType);
                okooo.setMatchTime(matchTime);
                okooo.setHost(host);
                okooo.setGuest(guest);
                okooo.setResult(result);
                gameMapper.insert(okooo);
            } else {
                existed.setResult(result);
                gameMapper.updateById(existed);
            }
        }
    }

}
