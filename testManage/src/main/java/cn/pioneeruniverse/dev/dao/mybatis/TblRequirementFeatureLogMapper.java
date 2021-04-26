package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.TblRequirementFeatureLog;

public interface TblRequirementFeatureLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblRequirementFeatureLog record);

    int insertSelective(TblRequirementFeatureLog record);

    TblRequirementFeatureLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblRequirementFeatureLog record);

    int updateByPrimaryKey(TblRequirementFeatureLog record);

	List<TblRequirementFeatureLog> findByReqFeatureId(Long id);

}