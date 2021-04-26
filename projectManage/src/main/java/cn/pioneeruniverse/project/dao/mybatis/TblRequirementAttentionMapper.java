package cn.pioneeruniverse.project.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.project.entity.TblRequirementAttention;

public interface TblRequirementAttentionMapper extends BaseMapper<TblRequirementAttention> {
	int deleteByPrimaryKey(Long id);

	Integer insert(TblRequirementAttention record);

	int insertSelective(TblRequirementAttention record);

	TblRequirementAttention selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(TblRequirementAttention record);

	int updateByPrimaryKey(TblRequirementAttention record);
}