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
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@SpringCloudApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@Configuration
@ComponentScan
@EnableFeignClients
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class DevManageApplication {

	public static void main(String args[]) {

		SpringApplication.run(DevManageApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}

	/**
	 * Set the ThreadPoolExecutor's core pool size.
	 */
	private int corePoolSize = 30;
	/**
	 * Set the ThreadPoolExecutor's maximum pool size.
	 */
	private int maxPoolSize = 200;
	/**
	 * Set the capacity for the ThreadPoolExecutor's BlockingQueue.
	 */
	private int queueCapacity = 10;

	@Bean
	public Executor initChangedFilesForSvnPreCommitHook() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("InitChanedFilesForSvnPreCommitHook-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

	@Bean
	public Executor initChangedSonFilesForSvnPreCommitHook() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("InitChangedSonFilesForSvnPreCommitHook-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

	@Bean
	public Executor getSonFilesForAddCommitOnlyDictRecord(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(30);
		executor.setQueueCapacity(5);
		executor.setThreadNamePrefix("getSonFilesForAddCommitOnlyDictRecord-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

	@Bean
	public Executor batchInsertOrUpdateDevTaskScmFileForPostCommitHook() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("BatchInsertOrUpdateDevTaskScmFileForPostCommitHook-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

    @Bean
	public Executor batchInsertOrUpdateDevTaskScmGitFileForPostReceiveHook(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("BatchInsertOrUpdateDevTaskScmGitFileForPostReceiveHook-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

	@Bean
	public RestTemplate restTemplate(){
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout(60000);//单位为ms
		factory.setConnectTimeout(60000);//单位为ms
		RestTemplate restTemplate=new RestTemplate(factory);
		return restTemplate;
	}
}
