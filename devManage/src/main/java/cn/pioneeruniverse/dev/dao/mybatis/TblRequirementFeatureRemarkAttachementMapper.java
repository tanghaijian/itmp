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

	/**
	*@author author
	*@Description 根据开发任务查询备注附件
	*@Date 2020/8/17
	 * @param id
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeatureRemarkAttachement>
	**/
	List<TblRequirementFeatureRemarkAttachement> findByRemarkId(Long id);
}