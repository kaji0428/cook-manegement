package com.example.cookmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootTest
class CookManagementApplicationTests {

	@Test
	void contextLoads() {
	}
	@SpringBootApplication
	@MapperScan("com.example.cookingmanagement.mapper") // ★追加：Mapperを自動スキャンする！
	public class CookingManagementApplication {

		public static void main(String[] args) {
			SpringApplication.run(CookingManagementApplication.class, args);
		}
	}

}
