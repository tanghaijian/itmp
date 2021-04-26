package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.dev.entity.TblTestSetCaseExecuteAttachement;

public interface TblTestSetCaseExecuteAttachementMapper {
	void addAttachement(List<TblTestSetCaseExecuteAttachement> list);
	
	List<TblTestSetCaseExecuteAttachement> selectAttchement(Long testSetCaseExecuteId);
	void deleteFile(Map<String, Object> map);

	void addAttachementMap(Map<String,Object> param);
}
