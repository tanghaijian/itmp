package cn.pioneeruniverse.report.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.ureport.definition.datasource.BuildinDatasource;

/**
 * 
* @ClassName: BuildInDruidDatasource
* @Description: 定义报表工具的内置数据源信息：开发库
* @author author
* @date 2020年8月3日 下午10:19:33
*
 */
@Component
public class BuildInDruidDatasource implements BuildinDatasource{

	@Autowired
	@Qualifier("itmpDatasource")
    private  DataSource dataSource;

	/**
	 * 
	* @Title: name
	* @Description: 内置数据库显示在页面的名字
	* @author author
	* @return
	* @throws
	 */
	@Override
	public String name() {
		return "devDb";
	}

	/**
	 * 
	* @Title: getConnection
	* @Description: 返回开发库连接
	* @author author
	* @return
	* @throws
	 */
	@Override
	public Connection getConnection() {
		try {
           return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
