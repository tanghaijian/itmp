package cn.pioneeruniverse.common.druid;

import javax.sql.DataSource;

import cn.pioneeruniverse.common.bean.DruidProperties;
import cn.pioneeruniverse.common.bean.DynamicDataSource;
import cn.pioneeruniverse.common.bean.MultiSourceExAop;
import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 10:12 2019/1/22
 * @Modified By:
 */
@Configuration
@ConditionalOnProperty(prefix = "multiple-datasource", name = "open", havingValue = "true")
//由于引入多数据源，所以让spring事务的aop要在多数据源切换aop的后面
@EnableTransactionManagement(order = 2)
public class MultipleDruidDBConfig {

    private Logger logger = LoggerFactory.getLogger(MultipleDruidDBConfig.class);

    @Bean(name = "itmpDruid")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidProperties itmpDruidProperties() {
        return new DruidProperties();
    }

    @Bean(name = "tmpDruid")
    @ConfigurationProperties(prefix = "spring.tmp.datasource")
    public DruidProperties tmpDruidProperties() {
        return new DruidProperties();
    }

    @Bean
    public MultiSourceExAop multiSourceExAop() {
        return new MultiSourceExAop();
    }


    @Bean
    public DataSource dataSource(@Qualifier("itmpDruid") DruidProperties itmpDruidProperties, @Qualifier("tmpDruid") DruidProperties tmpDruidProperties) {
        //组装第一个数据源
        DruidDataSource dataSource1 = new DruidDataSource();
        itmpDruidProperties.config(dataSource1);
        //组装第二个数据源
        DruidDataSource dataSource2 = new DruidDataSource();
        tmpDruidProperties.config(dataSource2);
        try {
            dataSource1.init();
            dataSource2.init();
        } catch (SQLException sql) {
            logger.error("datasource init error", sql);
        }
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(itmpDruidProperties.getDataSourceName(), dataSource1);
        hashMap.put(tmpDruidProperties.getDataSourceName(), dataSource2);
        dynamicDataSource.setTargetDataSources(hashMap);
        //设置默认数据源为itmp
        dynamicDataSource.setDefaultTargetDataSource(dataSource1);
        return dynamicDataSource;
    }


}
