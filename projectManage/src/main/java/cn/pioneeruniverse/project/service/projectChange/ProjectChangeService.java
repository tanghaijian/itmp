package cn.pioneeruniverse.project.service.projectChange;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.project.entity.TblProjectChangeInfo;
// import cn.pioneeruniverse.project.entity.TblQuestionLog;
import cn.pioneeruniverse.project.entity.TblProjectChangeLog;

public interface ProjectChangeService {

	List<TblProjectChangeInfo> getChanges(Long projectId, HttpServletRequest request);

	void deleteProjectChange(Long id, HttpServletRequest request);

	void insertProjectChange(TblProjectChangeInfo tblProjectChangeInfo, HttpServletRequest request);

	TblProjectChangeInfo getProjectChangeById(Long id);

	List<TblProjectChangeLog> getChangeLog(Long id);

	void updateProjectChange(TblProjectChangeInfo tblProjectChangeInfo, HttpServletRequest request);

	List<TblProjectChangeInfo> getChangesByProgram(Long programId, HttpServletRequest request);

}
