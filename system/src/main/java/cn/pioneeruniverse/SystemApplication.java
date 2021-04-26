package cn.pioneeruniverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * @author author
 * @Description system后台启动类
 * @Date 2020/9/3
 * @return
 **/
@SpringCloudApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@RefreshScope
@Configuration
@ComponentScan
@EnableFeignClients
@SpringBootApplication
public class SystemApplication {

	public static void main(String args[]){
		
		SpringApplication.run(SystemApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}
}
