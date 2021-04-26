/**  
* @Title:  DatasourceConfig.java
* @Package cn.pioneeruniverse.report.config
* @Description: TODO(用一句话描述该文件做什么)
* @author author
* @date  2020年10月14日 上午9:18:13
* @version V1.0  
* Update Logs:
* ****************************************************
* Name:
* Date:
* Description:
******************************************************
*/
package cn.pioneeruniverse.report.config;

import java.sql.SQLException;

/**
* @ClassName: DatasourceConfig
* @Description: TODO(这里用一句话描述这个类的作用)
* @author author
* @date 2020年10月14日 上午9:18:13
*
*/

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;

import cn.pioneeruniverse.common.bean.DruidProperties;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 10:12 2019/1/22
 * @Modified By:
 */
@Configuration
public class DatasourceConfig {

    private Logger logger = LoggerFactory.getLogger(DatasourceConfig.class);

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



    @Primary
    @Bean(name="itmpDatasource")
    public DataSource itmpDataSource(@Qualifier("itmpDruid") DruidProperties itmpDruidProperties) {
        //组装第一个数据源
        DruidDataSource dataSource1 = new DruidDataSource();
        itmpDruidProperties.config(dataSource1);
        try {
            dataSource1.init();
        } catch (SQLException sql) {
            logger.error("datasource init error", sql);
        }
       
        return dataSource1;
    }

    @Bean(name="tmpDatasource")
    public DataSource tmpDataSource(@Qualifier("tmpDruid") DruidProperties tmpDruidProperties) {
        //组装第一个数据源
        DruidDataSource dataSource1 = new DruidDataSource();
        tmpDruidProperties.config(dataSource1);
        try {
            dataSource1.init();
        } catch (SQLException sql) {
            logger.error("datasource init error", sql);
        }
       
        return dataSource1;
    }

}

