package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttachement;

public interface TblRequirementFeatureAttachementMapper {
	 int deleteByPrimaryKey(Long id);

	    int insert(TblRequirementFeatureAttachement record);

	    int insertSelective(TblRequirementFeatureAttachement record);

	    TblRequirementFeatureAttachement selectByPrimaryKey(Long id);

	    int updateByPrimaryKeySelective(TblRequirementFeatureAttachement record);

	    int updateByPrimaryKey(TblRequirementFeatureAttachement record);

		void insertAtt(TblRequirementFeatureAttachement tblRequirementFeatureAttachement);

		List<TblRequirementFeatureAttachement> findAttByRFId(Long id);

		void updateNo(Long id);

		void updateYes(Long id);

		TblRequirementFeatureAttachement getReqFeatureAttByUrl(String filePath);

		/**
		* @author author
		* @Description 批量逻辑删除STATUS = 2
		* @Date 2020/9/24
		* @param diffIds
		* @return void
		**/
		void deleteByIds(List<Long> diffIds);

		/**
		* @author author
		* @Description 根据任务表id查询附件
		* @Date 2020/9/24
		* @param ids
		* @return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeatureAttachement>
		**/
		List<TblRequirementFeatureAttachement> findAttByRFIds(@Param("ids")Long [] ids);

		void updateFeatureId(TblRequirementFeatureAttachement atta);
}