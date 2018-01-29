package io.github.okooo.service;

import io.github.okooo.util.SimpleHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OkoooServiceTest {

    @Autowired
    private OkoooService service;

    @Test
    public void fetch() {
        service.fetchChayi();
    }

    @Test
    public void test() {
//        for (int i = 17001; i <= 17195; i++) {
//            String url = "http://kaijiang.500.com/shtml/sfc/" + i + ".shtml";
//            String html = SimpleHttpClient.getCurrent().get(url).getResponseText();
//
//            Document doc = Jsoup.parse(html);
//            Elements td = doc.select("table.kj_tablelist02").get(1).select("tr").get(4).select("td");
//            System.out.println(i + "期" + td.get(1).text() + "注" + td.get(2).text() + "元");
//        }
    }

}