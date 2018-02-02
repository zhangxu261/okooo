package io.github.okooo.service;

import io.github.okooo.domain.Okooo;
import io.github.okooo.mapper.OkoooMapper;
import io.github.okooo.util.SimpleHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OkoooService {
    private static final String S = "->";

    private final OkoooMapper okoooMapper;

    public OkoooService(OkoooMapper okoooMapper) {
        this.okoooMapper = okoooMapper;
    }

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
            String result = tds.get(13).text();

            Okooo existed = okoooMapper.findOneBySerialAndMatchTime(serial, matchTime);
            if (existed == null) {
                Okooo okooo = new Okooo();
                okooo.setSerial(serial);
                okooo.setMatchType(matchType);
                okooo.setMatchTime(matchTime);
                okooo.setHost(host);
                okooo.setGuest(guest);
                if (StringUtils.isNotEmpty(w) && StringUtils.isNotEmpty(d) && StringUtils.isNotEmpty(l)) {
                    okooo.setIdx(w + "|" + d + "|" + l);
                }
                okooo.setResult(result);
                okoooMapper.insertSelective(okooo);
            } else {
                if (StringUtils.isNotEmpty(w) && StringUtils.isNotEmpty(d) && StringUtils.isNotEmpty(l)) {
                    existed.setIdx(w + "|" + d + "|" + l);
                }
                existed.setResult(result);
                okoooMapper.updateByPrimaryKeySelective(existed);
            }
        }
    }

    public void fetchKelly(int page, int date) {
        Map<String, String> params = new HashMap<>();
        params.put("LeagueID", "8,17,18,19,23,24,34,35,36,37,38,44,131,136,155,182,238,244,326,328,329,330,333,347,352,372,384,463");
        params.put("HandicapNumber", "-1,-2,-3,1,2");
        params.put("BetDate", String.valueOf(date));
        params.put("MakerType", "AuthoriteBooks");
        params.put("PageID", String.valueOf(page));
        params.put("HasEnd", "1");
        String html = SimpleHttpClient.getCurrent().post("http://www.okooo.com/jingcai/shuju/peilv/", params).getResponseText();

        Document doc = Jsoup.parse(html);

        Elements divs = doc.select("div.pankoudata");
        for (int i = 0; i < divs.size(); i++) {
            Element one = divs.get(i);

            Elements left = one.select("div.magazineDateTit p.float_l b");
            String serial = left.get(0).text();
            String matchTime = left.get(2).text();

            Element td = one.select("table tr.titlebg td:contains(所选公司凯利离散度)").get(0);
            String w = td.parent().select("td").get(1).text().replaceAll("\\s", "");
            String d = td.parent().select("td").get(2).text().replaceAll("\\s", "");
            String l = td.parent().select("td").get(3).text().replaceAll("\\s", "");

            Okooo existed = okoooMapper.findOneBySerialAndMatchTime(serial, matchTime);

            if (StringUtils.isNotEmpty(w) && StringUtils.isNotEmpty(d) && StringUtils.isNotEmpty(l)) {
                existed.setKelly(w + "|" + d + "|" + l);
            }

            okoooMapper.updateByPrimaryKeySelective(existed);
        }
    }

    public void fetchDiff(int page, int date) {
        Map<String, String> params = new HashMap<>();
        params.put("LeagueID", "8,17,18,19,23,24,34,35,36,37,38,44,131,136,155,182,238,244,326,328,329,330,333,347,352,372,384,463");
        params.put("HandicapNumber", "0");
        params.put("BetDate", String.valueOf(date));
        params.put("MakerType", "");
        params.put("PageID", String.valueOf(page));
        params.put("HasEnd", "1");
        String html = SimpleHttpClient.getCurrent().post("http://www.okooo.com/jingcai/shuju/chayi/", params).getResponseText();

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

            if (StringUtils.isNotEmpty(w) && StringUtils.isNotEmpty(d) && StringUtils.isNotEmpty(l)) {
                existed.setBiff(w + "|" + d + "|" + l);
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

            trs = one.select("table.tab2 tr");
            String h = trs.get(3).select("td").get(0).text().replaceAll("\\s", "");
            String g = trs.get(3).select("td").get(1).text().replaceAll("\\s", "");
            if (StringUtils.isNotEmpty(h) && StringUtils.isNotEmpty(g)) {
                existed.setGiff(h + "|" + g);
            }


            okoooMapper.updateByPrimaryKeySelective(existed);
        }
    }


    private String diff(String newStr, String oldStr) {
        if (StringUtils.isEmpty(newStr) && StringUtils.isEmpty(oldStr)) {
            return null;
        } else if (StringUtils.isNotEmpty(newStr) && StringUtils.isEmpty(oldStr)) {
            return newStr;
        } else if (StringUtils.isEmpty(newStr) && StringUtils.isNotEmpty(oldStr)) {
            return oldStr;
        } else {
            if (StringUtils.contains(oldStr, S)) {
                int backIdx = StringUtils.indexOf(oldStr, S);
                String backStr = StringUtils.substring(oldStr, backIdx + 2);
                if (!backStr.equals(newStr)) {
                    String frontStr = StringUtils.substring(oldStr, 0, backIdx);
                    return frontStr + "->" + newStr;
                } else {
                    return oldStr;
                }
            } else {
                if (!oldStr.equals(newStr)) {
                    return oldStr + "->" + newStr;
                } else {
                    return oldStr;
                }
            }
        }
    }
}
