package io.github.okooo.web.rest;

import io.github.okooo.service.ZhishuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("io.github.okooo.mapper")
@EnableScheduling
@SpringBootApplication
@RestController
public class ZhishuController {

    @Autowired
    private ZhishuService zhishuService;

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello world");
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> fetch() {
        zhishuService.fetch();
        return ResponseEntity.ok("fetch ok");
    }

}
