package cn.pioneeruniverse.dev.dao.mybatis;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblDevTaskAttention;

public interface TblDevTaskAttentionMapper extends BaseMapper<TblDevTaskAttention> {
	int deleteByPrimaryKey(Long id);

	Integer insert(TblDevTaskAttention record);

	int insertSelective(TblDevTaskAttention record);

	TblDevTaskAttention selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(TblDevTaskAttention record);

	int updateByPrimaryKey(TblDevTaskAttention record);
	
	/**
	* @author author
	* @Description 获取某个开发工作任务的关联人员
	* @Date 2020/9/21
	* @param id
	* @return java.lang.String
	**/
	String getUserIdsByDevTaskId(@Param("devTaskId")Long id);
}