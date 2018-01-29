package io.github.okooo.service;

import io.github.okooo.domain.Okooo;
import io.github.okooo.mapper.OkoooMapper;
import io.github.okooo.util.SimpleHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OkoooService {

    private static final String S = "->";
    @Autowired
    private OkoooMapper okoooMapper;

    //每20分钟抓一次
    @Scheduled(cron = "1 */20 * * * ?")
    public void fetch() {
        fetchZhishu();
        fetchChayi();
    }

    public void fetchZhishu() {
        String url = "http://www.okooo.com/jingcai/shuju/zhishu/";
        String html = SimpleHttpClient.getCurrent().get(url).getResponseText();
        Document doc = Jsoup.parse(html);

        Elements trs = doc.select("table tr");
        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");

            String serial = tds.get(0).text();
            String matchType = tds.get(1).text();
            String matchTime = tds.get(2).text();
            String hostTeam = tds.get(3).text();
            String guestTeam = tds.get(5).text();
            String zhishu = tds.get(12).text().replaceAll("\\s", "");
            String matchResult = tds.get(13).text();

            Okooo existed = okoooMapper.findOneBySerialAndMatchTime(serial, matchTime);
            if (existed == null) {
                Okooo okooo = new Okooo();
                okooo.setSerial(serial);
                okooo.setMatchType(matchType);
                okooo.setMatchTime(matchTime);
                okooo.setHostTeam(hostTeam);
                okooo.setGuestTeam(guestTeam);
                okooo.setZhishu(zhishu);
                okooo.setMatchResult(matchResult);
                okoooMapper.insertSelective(okooo);
            } else {
                existed.setZhishu(diff(zhishu, existed.getZhishu()));
                okoooMapper.updateByPrimaryKeySelective(existed);
            }
        }
    }

    private String diff(String newStr, String oldStr) {
        String rs = oldStr;
        //先判断有没有-》
        if (StringUtils.contains(oldStr, S)) {
            int backIdx = StringUtils.indexOf(oldStr, S);
            String backStr = StringUtils.substring(oldStr, backIdx + 2);
            //新值旧值比较
            if (!backStr.equals(newStr)) {
                String frontStr = StringUtils.substring(oldStr, 0, backIdx);
                rs = frontStr + "->" + newStr;
            }
        } else {
            if (!oldStr.equals(newStr)) {
                rs = oldStr + "->" + newStr;
            }
        }
        return rs;
    }

    public void fetchChayi() {
        Date currentDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String url = "http://www.okooo.com/jingcai/shuju/chayi/" + df.format(currentDate);

        String html = SimpleHttpClient.getCurrent().get(url).getResponseText();
        Document doc = Jsoup.parse(html);

        Elements divs = doc.select("div.pankoudata");
        for (int i = 0; i < divs.size(); i++) {
            Element one = divs.get(i);

            Elements left = one.select("div.magazineDateTit p.float_l b");
            String serial = left.get(0).text();
            String matchTime = left.get(2).text();

            Elements trs = one.select("table.tab1 tr");
            String w = trs.get(2).select("td").get(4).text().replaceAll("\\s", "");
            String d = trs.get(3).select("td").get(4).text().replaceAll("\\s", "");
            String l = trs.get(4).select("td").get(4).text().replaceAll("\\s", "");

            String ow = trs.get(2).select("td").get(2).text().replaceAll("\\s", "");
            String od = trs.get(3).select("td").get(2).text().replaceAll("\\s", "");
            String ol = trs.get(4).select("td").get(2).text().replaceAll("\\s", "");

            Okooo existed = okoooMapper.findOneBySerialAndMatchTime(serial, matchTime);

            String[] chayis = StringUtils.split(existed.getChayi(), "|");
            String _w = chayis[0];
            String _d = chayis[1];
            String _l = chayis[2];
            String chayi = diff(w, _w) + "|" + diff(d, _d) + "|" + diff(l, _l);
            existed.setChayi(chayi);

            String[] oddss = StringUtils.split(existed.getOdds(), "|");
            String _ow = oddss[0];
            String _od = oddss[1];
            String _ol = oddss[2];
            String odds = diff(ow, _ow) + "|" + diff(od, _od) + "|" + diff(ol, _ol);
            existed.setOdds(odds);

            okoooMapper.updateByPrimaryKeySelective(existed);
        }
    }
}
