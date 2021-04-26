package cn.pioneeruniverse.project.dao.mybatis;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.project.entity.TblRequirementFeatureAttention;


public interface TblRequirementFeatureAttentionMapper extends BaseMapper<TblRequirementFeatureAttention> {
	int deleteByPrimaryKey(Long id);

	Integer insert(TblRequirementFeatureAttention record);

	int insertSelective(TblRequirementFeatureAttention record);

	TblRequirementFeatureAttention selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(TblRequirementFeatureAttention record);

	/**
	* @author author
	* @Description 根据id修改
	* @Date 2020/9/24
	* @param record
	* @return int
	**/
	int updateByPrimaryKey(TblRequirementFeatureAttention record);

	/**
	* @author author
	* @Description 更新开发任务关注信息
	* @Date 2020/9/24
	* @param id
	* @return java.lang.String
	**/
	String getUserIdsByReqFeatureId(@Param("requirementFeatureId")Long id);
}