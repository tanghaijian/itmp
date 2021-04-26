package cn.pioneeruniverse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.mongodb.MongoClient;

/**
 * @author author
 * @Description Project后台启动类
 * @Date 2020/9/3
 * @return
 **/
@SpringCloudApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@Configuration
@ComponentScan
@SpringBootApplication
@EnableFeignClients
public class ProjectManageApplication {

	public static void main(String args[]){
		
		SpringApplication.run(ProjectManageApplication.class, args);
	}


	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}

	@Bean
	public RestTemplate restTemplate(){
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		//单位为ms
		factory.setReadTimeout(60000);
		//单位为ms
		factory.setConnectTimeout(60000);
		RestTemplate restTemplate=new RestTemplate(factory);
		return restTemplate;
	}

	//由于IT全流程方采用的主从形式的mongodb，而本系统版本原因，直接将主从mongodb拆分成两个独立的mongodb
	@Value("${mongodb.olddb.database}")
	private String olddb;
	@Value("${mongodb.olddb.host}")
	private String olddbHost;
	@Value("${mongodb.olddb.port}")
	private int olddbPort;

	@Value("${mongodb.newdb.database}")
	private String newdb;
	@Value("${mongodb.newdb.host}")
	private String newdbHost;
	@Value("${mongodb.newdb.port}")
	private int newdbPort;

	@Autowired
	private MongoMappingContext mongoMappingContext;

	@Bean(name="oldGridFsTemplate")
	public GridFsTemplate readGridFsTemplate() throws Exception {
		return new GridFsTemplate(olddbFactory(), mongoConverter(olddbFactory()));
	}

	@Bean(name="newGridFsTemplate")
	public GridFsTemplate writeGridFsTemplate() throws Exception {
		return new GridFsTemplate(newdbFactory(), mongoConverter(newdbFactory()));
	}

	@Bean(name="newdbMongoTemplate")
	public MongoTemplate newdbMongoTemplate() throws Exception {
		return new MongoTemplate(newdbFactory());
	}

	@Bean(name="olddbMongoTemplate")
	public MongoTemplate olddbMongoTemplate() throws Exception {
		return new MongoTemplate(olddbFactory());
	}

	@Bean
	@Primary
	public MongoDbFactory newdbFactory() throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(this.newdbHost, this.newdbPort),
				this.newdb);
	}

	@Bean
	public MongoDbFactory olddbFactory() throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(this.olddbHost,this.olddbPort),
				this.newdb);
	}

	@Bean
	public MappingMongoConverter mongoConverter(MongoDbFactory factory) throws Exception {
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
		MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
		mongoConverter.setMapKeyDotReplacement("_");
		mongoConverter.afterPropertiesSet();
		return mongoConverter;
	}
}
