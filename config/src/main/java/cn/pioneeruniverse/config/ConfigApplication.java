package cn.pioneeruniverse.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author author
 * @Description config启动类
 * @Date 2020/9/3
 * @return void
 **/
@EnableConfigServer
@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
public class ConfigApplication {

	public static void main(String args[]){
		SpringApplication.run(ConfigApplication.class, args);
	}
}
