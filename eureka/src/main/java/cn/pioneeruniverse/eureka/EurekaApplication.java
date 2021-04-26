package cn.pioneeruniverse.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
* @author author
* @Description Eureka启动类
* @Date 2020/9/9
* @return
**/
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {

	public static void main(String args[]){
		SpringApplication.run(EurekaApplication.class, args);
	}
}
