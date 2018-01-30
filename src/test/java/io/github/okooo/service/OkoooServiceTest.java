package io.github.okooo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OkoooServiceTest {

    @Autowired
    private OkoooService service;

    @Test
    public void fetch() {
        service.fetchIdx();
        service.fetchDiff();
        service.fetchKelly(1,2,0);
        service.fetchKelly(2,2,0);
    }

}