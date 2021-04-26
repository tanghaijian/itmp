package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblTestTaskRemarkAttachement;

public interface TblTestTaskRemarkAttachementMapper extends BaseMapper<TblTestTaskRemarkAttachement>{
	/**
	* @author author
	* @Description 查询测试开发任务备注附件信息
	* @Date 2020/9/8
	* @param ids
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblTestTaskRemarkAttachement>
	**/
	List<TblTestTaskRemarkAttachement> findTaskRemarkAttachement(List<Long> ids);
	
	/**
	* @author author
	* @Description 批量添加附件
	* @Date 2020/9/8
	* @param list
	* @return void
	**/
	void addRemarkAttachement(List<TblTestTaskRemarkAttachement> list);
}
