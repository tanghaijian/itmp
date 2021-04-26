package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.dev.entity.TblTestSetCase;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblCaseInfo;

public interface TblCaseInfoMapper extends BaseMapper<TblCaseInfo> {

	List<TblCaseInfo> getCaseInfos(HashMap<String, Object> map);

	//非系統管理员查询列表数据
	List<TblCaseInfo> getCaseInfoCondition(HashMap<String, Object> map);

	void insertCaseInfo(TblCaseInfo tblCaseInfo);

	void deleteCaseInfo(HashMap<String, Object> map);

	String selectMaxCaseNumber();

	TblCaseInfo findCaseInfoById(Long id);
	
	List<TblCaseInfo> getCaseByIds(List<String> ids);

	List<Map<String, Object>> getCaseInfo(HashMap<String, Object> paramMap);

	void updateCaseInfo(TblCaseInfo caseInfo);

	Long getIdByCaseNumber(String caseNumber);

	void updateArchiveStatus(HashMap<String, Object> map);

	void updateCaseArchivedStatus(HashMap<String, Object> map);

	void updateCase(Map<String, Object> result);

	String getCaseFieldTemplateById(@Param("id")Long id, @Param("fieldName")String fieldName);
	
	/**
	*@author liushan
	*@Description 根据测试集案例id添加测试案例
	*@Date 2020/3/23
	*@Param [setCase]
	*@return void
	**/
	void insertByTestSetCaseId(TblTestSetCase setCase);

}