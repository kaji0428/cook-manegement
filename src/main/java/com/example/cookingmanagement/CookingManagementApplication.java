package com.example.cookingmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.cookingmanagement.mapper")  // ← 追加！！！
public class CookingManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookingManagementApplication.class, args);
    }

}
