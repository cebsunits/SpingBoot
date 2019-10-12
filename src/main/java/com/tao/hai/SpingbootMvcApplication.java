package com.tao.hai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpingbootMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpingbootMvcApplication.class, args);
    }

}
