package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblArchivedCase;
import cn.pioneeruniverse.dev.entity.TblCaseInfo;

public interface TblArchivedCaseMapper {

	void archivingCase(TblCaseInfo tblCaseInfo);

	List<String> getArchivedCaseIds();

	void deleteArchivedCase(HashMap<String, Object> map);

	void updateArchivedCase(TblCaseInfo caseInfo);

	List<TblArchivedCase> getCaseInfos(HashMap<String, Object> map);

	//非系统管理员
	List<TblArchivedCase> getCaseInfoCondition(HashMap<String, Object> map);

	TblArchivedCase getArchivedCaseById(Long id);

	void deleteArchivedCase(List<Long> ids);
	
	List<String> getCaseNumberByIds(@Param("list")List<Long> list);

}
