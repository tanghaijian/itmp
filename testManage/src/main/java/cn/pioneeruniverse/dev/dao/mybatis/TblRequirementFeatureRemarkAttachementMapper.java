package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.TblRequirementFeatureRemarkAttachement;

public interface TblRequirementFeatureRemarkAttachementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblRequirementFeatureRemarkAttachement record);

    int insertSelective(TblRequirementFeatureRemarkAttachement record);

    TblRequirementFeatureRemarkAttachement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblRequirementFeatureRemarkAttachement record);

    int updateByPrimaryKey(TblRequirementFeatureRemarkAttachement record);

	void insertAtt(TblRequirementFeatureRemarkAttachement tblRequirementFeatureRemarkAttachement);

	List<TblRequirementFeatureRemarkAttachement> findByRemarkId(Long id);
}