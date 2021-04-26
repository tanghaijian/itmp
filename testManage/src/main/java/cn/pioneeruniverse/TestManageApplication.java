package cn.pioneeruniverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author author
 * @Description test后台启动类
 * @Date 2020/9/3
 * @return
 **/
@SpringCloudApplication
@EnableAutoConfiguration
@Configuration
@EnableFeignClients
@ComponentScan
public class TestManageApplication {
	

	public static void main(String args[]){
		SpringApplication.run(TestManageApplication.class, args);
		
	}
}
