package cn.pioneeruniverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
* @author author
* @Description test前台启动类
* @Date 2020/9/3
* @return
**/
@SpringCloudApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@Configuration
@EnableFeignClients
@ComponentScan
@SpringBootApplication
public class TestManageWebApplication {
	

	public static void main(String args[]){
		SpringApplication.run(TestManageWebApplication.class, args);
		
	}
}
