package io.github.okooo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import tk.mybatis.spring.annotation.MapperScan

@MapperScan("io.github.okooo.mapper")
@EnableScheduling
@SpringBootApplication
class OkoooApplication {

    static void main(String[] args) {
        SpringApplication.run(OkoooApplication.class, args)
    }

}