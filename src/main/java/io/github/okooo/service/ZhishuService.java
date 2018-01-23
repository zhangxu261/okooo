package io.github.okooo.service;

import io.github.okooo.domain.Zhishu;
import io.github.okooo.mapper.ZhishuMapper;
import io.github.okooo.util.SimpleHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ZhishuService {

    @Autowired
    private ZhishuMapper zhishuMapper;

    @Scheduled(initialDelay = 10000, fixedRate = 3600000)
    public void fetch() {
        String url = "http://www.okooo.com/jingcai/shuju/zhishu/";
        String html = SimpleHttpClient.getCurrent().get(url).getResponseText();
        Document doc = Jsoup.parse(html);

        Elements trs = doc.select("table tr");
        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");

            Zhishu zhishu = new Zhishu();
            zhishu.setSerial(tds.get(0).text());
            zhishu.setMatchType(tds.get(1).text());
            zhishu.setMatchTime(tds.get(2).text());
            zhishu.setHostTeam(tds.get(3).text());
            zhishu.setGuestTeam(tds.get(5).text());
            zhishu.setRemind(tds.get(12).text().replaceAll("\\s", ""));
            zhishu.setMatchResult(tds.get(13).text());

            Zhishu existed = zhishuMapper.findBySerialAndMatchTime(zhishu.getSerial(), zhishu.getMatchTime());
            if (existed == null) {
                zhishuMapper.insertSelective(zhishu);
            } else {
                String last = existed.getRemind();
                if (existed.getRemind().contains("->")) {
                    last = existed.getRemind().substring(existed.getRemind().lastIndexOf("->") + 2);
                }
                if (!last.equals(existed.getRemind())) {
                    existed.setRemind(existed.getRemind() + "->" + zhishu.getRemind());
                }
                existed.setMatchResult(zhishu.getMatchResult());
                zhishuMapper.updateByPrimaryKeySelective(existed);
            }
        }
    }
}
