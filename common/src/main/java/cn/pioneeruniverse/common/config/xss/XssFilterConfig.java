package cn.pioneeruniverse.common.config.xss;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 15:03 2019/5/16
 * @Modified By:
 */
@Configuration
@ConditionalOnProperty(prefix = "itmp.xss-filter-config", name = "open", havingValue = "true")
public class XssFilterConfig {


    @Value("${xss.filter.exclude.urls}")
    private String xssFilterExcludeUrls;

    @Bean
    @Order(0)
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = Maps.newHashMap();
        initParameters.put("excludes", xssFilterExcludeUrls);
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }

}
