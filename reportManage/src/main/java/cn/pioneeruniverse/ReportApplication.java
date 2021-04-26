package cn.pioneeruniverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bstek.ureport.console.UReportServlet;

/**
* @author author
* @Description 启动类
* @Date 2020/9/15
* @return
**/
@ImportResource(locations= {"classpath:report-context.xml"})
@SpringCloudApplication
@Configuration
@ComponentScan
@EnableFeignClients
@EnableTransactionManagement
public class ReportApplication {
	
	public static void main(String args[]) {
		SpringApplication.run(ReportApplication.class, args);
	}

	@Bean
    public ServletRegistrationBean buildUReportServlet() {
        return new ServletRegistrationBean(new UReportServlet(), "/ureport/*");

    }
	
}
