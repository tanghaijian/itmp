package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;

public interface RequirementMapper extends BaseMapper<TblRequirementInfo>{
	
	List<TblRequirementInfo> getAllRequirement(Map<String, Object> map);
	List<TblRequirementInfo> getAllRequirement1(Map<String, Object> map);
	//导出需求
	List<TblRequirementInfo> excelRequirement(Map<String, Object> map);
	List<TblRequirementInfo> excelRequirement1(Map<String, Object> map);
	//总记录数
	int getCountRequirement(Map<String, Object> map);
	int getCountRequirement1(Map<String, Object> map);

	//查询需求详情
	TblRequirementInfo findRequirementById(Long id);
	//查询有无重复需求名字
	List<TblRequirementInfo> findRequirementByName(TblRequirementInfo requirementInfo);
	//查询父需求
	TblRequirementInfo findFatherReq(Long parentId);
	//查询子需求
	List<TblRequirementInfo> findSonReq(Long id);
	//根据需求编号查询需求是否存在
	TblRequirementInfo getRequirementInByCode(String requirementCode);
	//新增需求	
	int insertRequirement(TblRequirementInfo record);	
	TblRequirementInfo findRequirementById1(Long id);
	//同步需求信息
	int updateRequirementData(TblRequirementInfo record);
	//修改需求信息
	int updateRequirement(TblRequirementInfo record);
	//删除需求		
    int deleteRequirement(Long id);
    //需求弹窗总记录数
    int getAllRequirementCount(TblRequirementInfo requirement);
    //需求弹窗（bootstrap）
	List<Map<String, Object>> getAllReq(Map<String, Object> map);
	//根据全流程平台id查询需求
	TblRequirementInfo getRequirementByItcd(Long itcdId);

	//获取最大需求编号
	String selectMaxRqeuirementCode();

	List<String> getRelationrequirementNames(@Param("requirement_ids")String requirement_ids);
	String getDevTaskFieldTemplateById(@Param("id")Long id,@Param("fieldName") String fieldName);
	List<Long> getUserIdsBySystemIds(@Param("systemIds")String systemIds);
	String getCodeById(Long id);


	List<Map<String, Object>> getReqidBysystemId(Map<String, Object> systemMap);
	String getProManageUserIds(@Param("requirementId")Long id);

    List<Map<String, Object>> getSystemByReqId(long parseLong);
	List<TblRequirementInfo> getRequirementsByIds(List<Long> ids);
}