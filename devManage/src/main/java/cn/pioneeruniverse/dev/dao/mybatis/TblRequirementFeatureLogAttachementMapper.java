package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.TblRequirementFeatureLogAttachement;

public interface TblRequirementFeatureLogAttachementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblRequirementFeatureLogAttachement record);

    int insertSelective(TblRequirementFeatureLogAttachement record);

    TblRequirementFeatureLogAttachement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblRequirementFeatureLogAttachement record);

    int updateByPrimaryKey(TblRequirementFeatureLogAttachement record);

	List<TblRequirementFeatureLogAttachement> findByLogId(Long id);
}