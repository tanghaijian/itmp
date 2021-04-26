package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblTestTaskRemark;

public interface TblTestTaskRemarkMapper extends BaseMapper<TblTestTaskRemark>{
	/*添加备注*/
	Long addTaskRemark(TblTestTaskRemark tblTestTaskRemark);
	
	List<TblTestTaskRemark> selectRemark(Long testTaskId);
}
