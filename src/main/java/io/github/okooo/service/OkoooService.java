package io.github.okooo.service;

import io.github.okooo.domain.Chayi;
import io.github.okooo.domain.Zhishu;
import io.github.okooo.mapper.ChayiMapper;
import io.github.okooo.mapper.ZhishuMapper;
import io.github.okooo.util.SimpleHttpClient;
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

    @Autowired
    private ZhishuMapper zhishuMapper;
    @Autowired
    private ChayiMapper chayiMapper;

    @Scheduled(cron = "1 */20 * * * ?")
    public void fetchZhishu() {
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
                if (!last.equals(zhishu.getRemind())) {
                    existed.setRemind(existed.getRemind() + "->" + zhishu.getRemind());
                }
                existed.setMatchResult(zhishu.getMatchResult());
                zhishuMapper.updateByPrimaryKeySelective(existed);
            }
        }
    }

    @Scheduled(cron = "2 */20 * * * ?")
    public void fetchChayi() {
        Date currentDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String url = "http://www.okooo.com/jingcai/shuju/chayi/" + df.format(currentDate);

        String html = SimpleHttpClient.getCurrent().get(url).getResponseText();
        Document doc = Jsoup.parse(html);

        Elements divs = doc.select("div.pankoudata");
        for (int i = 0; i < divs.size(); i++) {
            Element one = divs.get(i);

            Chayi chayi = new Chayi();
            Elements left = one.select("div.magazineDateTit p.float_l b");
            chayi.setSerial(left.get(0).text());
            chayi.setMatchType(left.get(1).text());
            chayi.setMatchTime(left.get(2).text());

            Element ti = one.select("div.magazineDateTit div.titnamebox").get(0);
            chayi.setHostTeam(ti.select("span").text());
            chayi.setGuestTeam(ti.select("b").text());

            Elements tab1 = one.select("table.tab1");
            String tzb = tab1.select("tr").get(3).select("td").get(5).text().replaceAll("\\s", "");
            chayi.setTzbRemind(tzb);
            Elements tab2 = one.select("table.tab2");
            String jql = tab2.select("tr").get(3).select("td").get(3).text().replaceAll("\\s", "");
            chayi.setJqlRemind(jql);
            String r = tab2.select("tr").get(3).select("td").get(4).text().replaceAll("\\s", "");
            chayi.setMatchResult(r);


            Chayi existed = chayiMapper.findBySerialAndMatchTime(chayi.getSerial(), chayi.getMatchTime());
            if (existed == null) {
                chayiMapper.insertSelective(chayi);
            } else {
                String lastTzb = existed.getTzbRemind();
                if (existed.getTzbRemind().contains("->")) {
                    lastTzb = existed.getTzbRemind().substring(existed.getTzbRemind().lastIndexOf("->") + 2);
                }
                if (!lastTzb.equals(chayi.getTzbRemind())) {
                    existed.setTzbRemind(existed.getTzbRemind() + "->" + chayi.getTzbRemind());
                }
                String lastJql = existed.getJqlRemind();
                if (existed.getJqlRemind().contains("->")) {
                    lastJql = existed.getJqlRemind().substring(existed.getJqlRemind().lastIndexOf("->") + 2);
                }
                if (!lastJql.equals(chayi.getJqlRemind())) {
                    existed.setJqlRemind(existed.getJqlRemind() + "->" + chayi.getJqlRemind());
                }

                existed.setMatchResult(chayi.getMatchResult());
                chayiMapper.updateByPrimaryKeySelective(existed);
            }
        }
    }
}
