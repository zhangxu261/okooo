package io.github.okooo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    @org.junit.Test
    public void test() {
        Date currentDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat("u");
        String d = df.format(currentDate);
        System.out.println(d);
    }
}
