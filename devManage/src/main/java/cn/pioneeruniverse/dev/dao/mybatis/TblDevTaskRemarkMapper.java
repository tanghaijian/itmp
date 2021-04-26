package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblDevTaskRemark;

public interface TblDevTaskRemarkMapper extends BaseMapper<TblDevTaskRemark>{
	/**
	*@author author
	*@Description 添加备注
	*@Date 2020/8/25
	 * @param tblDevTaskRemark
	*@return java.lang.Long
	**/
	Long addTaskRemark(TblDevTaskRemark tblDevTaskRemark);
	
	/**
	*@author author
	*@Description 查询备注
	*@Date 2020/8/25
	 * @param DevTaskId
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskRemark>
	**/
	List<TblDevTaskRemark> selectRemark(Long DevTaskId);
}
