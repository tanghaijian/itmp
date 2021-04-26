package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementInfo;
import cn.pioneeruniverse.dev.vo.RequirementVo;

public interface TblRequirementInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblRequirementInfo record);

    int insertSelective(TblRequirementInfo record);

    TblRequirementInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblRequirementInfo record);

    int updateByPrimaryKey(TblRequirementInfo record);

	int getCountRequirement(TblRequirementInfo record);

	List<TblRequirementInfo> getAllRequirement(TblRequirementInfo requirement);

	List<TblRequirementInfo> getAllReq();
	List<TblRequirementInfo> getRequirement(TblRequirementInfo record);

	List<Map<String, Object>> getAllReq2(Map<String, Object> map);

	String getReqCode(Long requirementId);

	int getAllRequirementCount(TblRequirementInfo requirement);
	
	List<Long> selectReqIdByReqCodeSql(@Param("requirementCodeSql") String requirementCodeSql);

}