package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.TblRequirementFeatureRemark;

public interface TblRequirementFeatureRemarkMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblRequirementFeatureRemark record);

    int insertSelective(TblRequirementFeatureRemark record);

    TblRequirementFeatureRemark selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblRequirementFeatureRemark record);

    int updateByPrimaryKey(TblRequirementFeatureRemark record);

	void addRemark(TblRequirementFeatureRemark remark);
    
	/**
	*@author author
	*@Description 根据开发任务查询备注
	*@Date 2020/8/17
	 * @param id
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeatureRemark>
	**/
	List<TblRequirementFeatureRemark> findRemarkByReqFeatureId(Long id);


}