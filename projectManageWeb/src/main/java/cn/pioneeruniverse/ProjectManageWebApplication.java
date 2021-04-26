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
* @Description 启动类
* @Date 2020/9/15
* @return
**/
@SpringCloudApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@Configuration
@EnableFeignClients
@ComponentScan
@SpringBootApplication
public class ProjectManageWebApplication {
	

	public static void main(String args[]){
		SpringApplication.run(ProjectManageWebApplication.class, args);
		
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}
}
