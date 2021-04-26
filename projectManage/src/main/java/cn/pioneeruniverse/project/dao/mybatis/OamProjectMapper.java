package cn.pioneeruniverse.project.dao.mybatis;

import java.util.HashMap;
import java.util.List;

import cn.pioneeruniverse.project.entity.TblProjectInfo;

public interface OamProjectMapper {

	List<TblProjectInfo> selectOamProject(HashMap<String, Object> map);

	void insertOamProject(TblProjectInfo tblProjectInfo);

	TblProjectInfo selectProject(Long id);

	void updateProject(TblProjectInfo tblProjectInfo);

	String selectMaxProjectCode();

	void endProject(HashMap<String, Object> map);

	void insertTmpOamProject(TblProjectInfo tblProjectInfo);

	List<TblProjectInfo> selectOamProjectCondition(HashMap<String, Object> map);

}
