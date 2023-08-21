package com.supercoding.commerce03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication(exclude = SecurityAutoConfiguration.class) //spring security를 임시로 꺼둘 때 사용합니다.
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) //dataSorce를 직접 설정할 때 사용합니다.
@SpringBootApplication
public class Commerce03Application {

	public static void main(String[] args) {
		SpringApplication.run(Commerce03Application.class, args);
	}

}
