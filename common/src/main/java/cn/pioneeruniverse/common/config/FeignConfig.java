package cn.pioneeruniverse.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.Request;
import feign.Retryer;

@Configuration
public class FeignConfig {

	@Bean
    public Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
	
	@Bean
    Request.Options feignOptions() {
        return new Request.Options(1 * 1000, 1 * 60000);
    }
	
	@Bean
    Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }
}
