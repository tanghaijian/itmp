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
* @ClassName: BuildInTmpDatasource
* @Description: 定义报表工具内置数据源：测试库
* @author author
* @date 2020年8月3日 下午10:20:35
*
 */
@Component
public class BuildInTmpDatasource implements BuildinDatasource{
	@Autowired
	@Qualifier("tmpDatasource")
    private  DataSource dataSource;
	
	/**
	 * 
	* @Title: name
	* @Description: 数据源显示在工具页面的名字
	* @author author
	* @return
	* @throws
	 */
	@Override
	public String name() {
		return "testDb";
	}

	/**
	 * 
	* @Title: getConnection
	* @Description: 返回测试库数据连接
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
