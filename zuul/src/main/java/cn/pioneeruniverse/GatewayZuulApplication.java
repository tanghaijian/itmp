package cn.pioneeruniverse;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import cn.pioneeruniverse.zuul.filter.AccessFilter;
import cn.pioneeruniverse.zuul.filter.ErrorFilter;
import cn.pioneeruniverse.zuul.filter.PostFilter;
import cn.pioneeruniverse.zuul.filter.RouteFilter;
import cn.pioneeruniverse.zuul.filter.XssFilter;

@ComponentScan
@EnableZuulProxy
@SpringCloudApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
public class GatewayZuulApplication {


    public static void main(String args[]) {
        SpringApplication.run(GatewayZuulApplication.class, args);
    }
    
	/*@Bean
	public XssFilter xssFilter() {
		return new XssFilter();
	}*/

    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }

    @Bean
    public RouteFilter routeFilter() {
        return new RouteFilter();
    }

    @Bean
    public PostFilter postFilter() {
        return new PostFilter();
    }

    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }


}
