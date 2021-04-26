package cn.pioneeruniverse.project.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.project.entity.TblCommissioningWindow;

public interface CommissioningWindowMapper extends BaseMapper<TblCommissioningWindow>{

	/**
	* @author author
	* @Description 查询投产窗口
	* @Date 2020/9/3
	* @param map
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblCommissioningWindow>
	**/
	List<TblCommissioningWindow> selectCommissioningWindows(HashMap<Object, Object> map);

	/**
	* @author author
	* @Description 查询最大的版本号
	* @Date 2020/9/3
	* @param 
	* @return java.lang.String
	**/
	String selectMaxVersion();

	/**
	* @author author
	* @Description 新增
	* @Date 2020/9/3
	* @param commissioningWindow
	* @return void
	**/
	void insertCommissioningWindow(TblCommissioningWindow commissioningWindow);

	/**
	* @author author
	* @Description 删除
	* @Date 2020/9/3
	* @param map
	* @return void
	**/
	void delectCommissioningWindow(HashMap<String, Object> map);

	/**
	* @author author
	* @Description 根据id查询投产窗口
	* @Date 2020/9/3
	* @param id
	* @return cn.pioneeruniverse.project.entity.TblCommissioningWindow
	**/
	TblCommissioningWindow selectCommissioningWindowById(Long id);

	/**
	* @author author
	* @Description 编辑
	* @Date 2020/9/3
	* @param commissioningWindow
	* @return void
	**/
	void editCommissioningWindow(TblCommissioningWindow commissioningWindow);
	
	/**
	* @author author
	* @Description 根据日期查询投产窗口
	* @Date 2020/9/3
	* @param commissioningWindow
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblCommissioningWindow>
	**/
	List<TblCommissioningWindow> findCommissioningByWindowDate(TblCommissioningWindow commissioningWindow);

	/**
	* @author author
	* @Description 查询所有的投产窗口
	* @Date 2020/9/3
	* @param map
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblCommissioningWindow>
	**/
	List<TblCommissioningWindow> selectAllWindows(Map<String, Object> map);

	/**
	* @author author
	* @Description 根据窗口id获取开发任务
	* @Date 2020/9/3
	* @param requirementId
	* @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	**/
	List<Map<String, Object>> getReqFeatureGroupbyWindow(Long requirementId);

}
