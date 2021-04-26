package cn.pioneeruniverse.report.dao.mybatis.report;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.report.vo.TblReportInfo;

public interface UreportMapper {

	/**
	 * 
	* @Title: saveFile
	* @Description: 保存页面编辑的文件信息
	* @author author
	* @param file
	* @throws
	 */
	public void saveFile(TblReportInfo file);
	
	/**
	 * 
	* @Title: selectFileByName
	* @Description: 通过文件名获取到报表文件信息
	* @author author
	* @param filename
	* @return
	* @throws
	 */
	public TblReportInfo selectFileByName(@Param("filename")String filename);
	
	/**
	 * 
	* @Title: selectFileList
	* @Description: 获取所有保存的报表文件
	* @author author
	* @return
	* @throws
	 */
	public List<TblReportInfo> selectFileList();
	
	/**
	 * 
	* @Title: updateFileByName
	* @Description: 通过文件名更新文件
	* @author author
	* @param filename
	* @throws
	 */
	public void updateFileByName(@Param("filename")String filename);
	
	/**
	 * 
	* @Title: delFileByName
	* @Description: 通过文件名删除文件
	* @author author
	* @param filename
	* @throws
	 */
	public void delFileByName(@Param("filename")String filename);
}
