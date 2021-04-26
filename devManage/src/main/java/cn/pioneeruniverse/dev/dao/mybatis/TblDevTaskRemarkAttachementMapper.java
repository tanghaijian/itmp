package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblDevTaskRemarkAttachement;

public interface TblDevTaskRemarkAttachementMapper extends BaseMapper<TblDevTaskRemarkAttachement>{
	
	/**
	*@author author
	*@Description 批量查询备注附件
	*@Date 2020/8/25
	 * @param ids
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskRemarkAttachement>
	**/
	List<TblDevTaskRemarkAttachement> findTaskRemarkAttachement(List<Long> ids);
	
	/**
	*@author author
	*@Description 批量添加备注附件
	*@Date 2020/8/25
	 * @param list
	*@return void
	**/
	void addRemarkAttachement(List<TblDevTaskRemarkAttachement> list);
}
