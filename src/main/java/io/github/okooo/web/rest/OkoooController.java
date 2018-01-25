package io.github.okooo.web.rest;

import io.github.okooo.service.OkoooService;
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
public class OkoooController {

    @Autowired
    private OkoooService okoooService;

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello world");
    }

    @GetMapping("/zhishu")
    public ResponseEntity<?> zhishu() {
        okoooService.fetchZhishu();
        return ResponseEntity.ok("fetch ok");
    }

    @GetMapping("/chayi")
    public ResponseEntity<?> chayi() {
        okoooService.fetchChayi();
        return ResponseEntity.ok("fetch ok");
    }

}
