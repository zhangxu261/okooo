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
import java.util.HashMap;
import java.util.Map;

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
        fetchKaili(1);
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
                if (StringUtils.isNotEmpty(zhishu)) {
                    okooo.setZhishu(zhishu);
                }
                okooo.setMatchResult(matchResult);
                okoooMapper.insertSelective(okooo);
            } else {
                existed.setZhishu(diff(zhishu, existed.getZhishu()));
                okoooMapper.updateByPrimaryKeySelective(existed);
            }
        }
    }

    public void fetchKaili(int page) {
        Date currentDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat("u");

        Map<String, String> params = new HashMap<>();
        params.put("LeagueID", "136,8,23,37,44,19,131,38,34,36,35,352,372,155,238,182,347,463,17");
        params.put("HandicapNumber", "-1,-2,-3,1,2");
        params.put("BetDate", df.format(currentDate));
        params.put("MakerType", "AuthoriteBooks");
        params.put("PageID", String.valueOf(page));
        params.put("HasEnd", "0");
        String html = SimpleHttpClient.getCurrent().post("http://www.okooo.com/jingcai/shuju/peilv/", params).getResponseText();

        Document doc = Jsoup.parse(html);

        Elements divs = doc.select("div.pankoudata");
        for (int i = 0; i < divs.size(); i++) {
            Element one = divs.get(i);

            Elements left = one.select("div.magazineDateTit p.float_l b");
            String serial = left.get(0).text();
            String matchTime = left.get(2).text();

            Element td = one.select("table tr.titlebg td:contains(所选公司凯利离散度)").get(0);
            String w = td.parent().select("td").get(1).text();
            String d = td.parent().select("td").get(2).text();
            String l = td.parent().select("td").get(2).text();

            Okooo existed = okoooMapper.findOneBySerialAndMatchTime(serial, matchTime);

            if (StringUtils.isEmpty(existed.getKaili())) {
                if (StringUtils.isNotEmpty(w) && StringUtils.isNotEmpty(d) && StringUtils.isNotEmpty(l)) {
                    existed.setKaili(w + "|" + d + "|" + l);
                }
            } else {
                String[] kailis = StringUtils.split(existed.getKaili(), "|");
                String _w = kailis[0];
                String _d = kailis[1];
                String _l = kailis[2];
                String kaili = diff(w, _w) + "|" + diff(d, _d) + "|" + diff(l, _l);
                existed.setKaili(kaili);
            }

            okoooMapper.updateByPrimaryKeySelective(existed);
        }
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

            if (StringUtils.isEmpty(existed.getChayi())) {
                existed.setChayi(w + "|" + d + "|" + l);
            } else {
                String[] chayis = StringUtils.split(existed.getChayi(), "|");
                String _w = chayis[0];
                String _d = chayis[1];
                String _l = chayis[2];
                String chayi = diff(w, _w) + "|" + diff(d, _d) + "|" + diff(l, _l);
                existed.setChayi(chayi);
            }

            if (StringUtils.isEmpty(existed.getOdds())) {
                existed.setOdds(ow + "|" + od + "|" + ol);
            } else {
                String[] oddss = StringUtils.split(existed.getOdds(), "|");
                String _ow = oddss[0];
                String _od = oddss[1];
                String _ol = oddss[2];
                String odds = diff(ow, _ow) + "|" + diff(od, _od) + "|" + diff(ol, _ol);
                existed.setOdds(odds);
            }

            if ("||".equals(existed.getChayi())) {
                existed.setChayi(null);
            }

            okoooMapper.updateByPrimaryKeySelective(existed);
        }
    }


    private String diff(String newStr, String oldStr) {
        oldStr = StringUtils.isEmpty(oldStr) ? "" : oldStr;
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
}
