package io.github.okooo.service

import io.github.okooo.domain.Zhishu
import io.github.okooo.mapper.ZhishuMapper
import io.github.okooo.util.SimpleHttpClient
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ZhishuService {

    @Autowired
    ZhishuMapper zhishuMapper

    @Scheduled(initialDelay = 10000L, fixedRate = 3600000L)
    void fetch() {
        def url = "http://www.okooo.com/jingcai/shuju/zhishu/"
        String html = SimpleHttpClient.current.get(url).responseText
        Document doc = Jsoup.parse(html)

        Elements trs = doc.select("table tr")
        for (int i in 1..<trs.size()) {
            Elements tds = trs.get(i).select("td")

            Zhishu zhishu = new Zhishu()
            zhishu.serial = tds.get(0).text()
            zhishu.matchType = tds.get(1).text()
            zhishu.matchTime = tds.get(2).text()
            zhishu.hostTeam = tds.get(3).text()
            zhishu.guestTeam = tds.get(5).text()
            zhishu.remind = tds.get(12).text().replaceAll("\\s", "")
            zhishu.matchResult = tds.get(13).text()

            Zhishu existed = zhishuMapper.findBySerialAndMatchTime(zhishu.serial, zhishu.matchTime)
            if (existed == null) {
                zhishuMapper.insertSelective(zhishu)
            } else {
                def last = existed.remind
                if (existed.remind.indexOf("->") > -1) {
                    last = existed.remind.substring(existed.remind.lastIndexOf("->") + 2)
                }
                if (last != zhishu.remind) {
                    existed.remind = existed.remind + "->" + zhishu.remind
                }
                existed.matchResult = zhishu.matchResult
                zhishuMapper.updateByPrimaryKeySelective(existed)
            }
        }
    }
}
