package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttachement;
import org.apache.ibatis.annotations.Param;

public interface TblRequirementFeatureAttachementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblRequirementFeatureAttachement record);

    int insertSelective(TblRequirementFeatureAttachement record);

    TblRequirementFeatureAttachement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblRequirementFeatureAttachement record);

    int updateByPrimaryKey(TblRequirementFeatureAttachement record);

    /**
    *@author liushan
    *@Description 新增附件
    *@Date 2020/8/14
     * @param tblRequirementFeatureAttachement
    *@return void
    **/
	void insertAtt(TblRequirementFeatureAttachement tblRequirementFeatureAttachement);

	/**
	*@author liushan
	*@Description 根据开发任务查询附件
	*@Date 2020/8/14
	 * @param id
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttachement>
	**/
	List<TblRequirementFeatureAttachement> findAttByRFId(Long id);

	/**
	*@author liushan
	*@Description 根据开发任务id 修改所属附件状态
	*@Date 2020/8/14
	 * @param id
	*@return void
	**/
	void updateNo(Long id);

	/**
	*@author liushan
	*@Description 根据附件id修改附件状态为1
	*@Date 2020/8/14
	 * @param id
	*@return void
	**/
	void updateYes(Long id);

	/**
	*@author liushan
	*@Description 根据附件地址 查询附件
	*@Date 2020/8/14
	 * @param filePath
	*@return cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttachement
	**/
	TblRequirementFeatureAttachement getReqFeatureAttByUrl(String filePath);

	/**
	*@author liushan
	*@Description 批量移除附件
	*@Date 2020/8/14
	 * @param diffIds
	*@return void
	**/
	void deleteByIds(List<Long> diffIds);

	/**
	*@author liushan
	*@Description 根据开发任务id 和 旧名称 查询附件
	*@Date 2020/8/14
	 * @param featureId 开发任务id
 * @param fileNameOld 旧名称
	*@return cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttachement
	**/
    TblRequirementFeatureAttachement getReqFeatureAttByfileNameOld(@Param("featureId") Long featureId,
                                                                   @Param("fileNameOld") String fileNameOld);
}