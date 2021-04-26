package cn.pioneeruniverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


/**
* @author author
* @Description 启动类
* @Date 2020/9/14
* @return
**/
@SpringCloudApplication
@EnableAutoConfiguration
@ComponentScan
@EnableFeignClients
public class JobApplication {

	public static void main(String args[]) {
		
	 SpringApplication.run(JobApplication.class, args);
	}
	
}
