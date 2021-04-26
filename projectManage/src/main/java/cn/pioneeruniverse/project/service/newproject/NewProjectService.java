package cn.pioneeruniverse.project.service.newproject;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.project.entity.TblProgramInfo;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
/**
 *
 * @ClassName: NewProjectService
 * @Description: 新项目serice
 * @author author
 *
 */
public interface NewProjectService {
	/**
	 * 查询
	 * @param projectInfo
	 * @param uid
	 * @param roleCodes
	 * @param page
	 * @param rows
	 * @return List<TblProjectInfo>
	 */

	List<TblProjectInfo> getAllNewProject(TblProjectInfo projectInfo, Long uid, List<String> roleCodes, Integer page,
			Integer rows);

	/**
	 * 查询
	 * @param id
	 * @return TblProjectInfo
	 */
	TblProjectInfo getNewProjectById(Long id);

	/**
	 * 插入新项目
	 * @param projectInfo
	 * @param request
	 * @return Long
	 * @throws Exception
	 */

	Long insertNewProject(TblProjectInfo projectInfo, HttpServletRequest request) throws Exception;

	void insertProgram(TblProgramInfo programInfo, HttpServletRequest request);

	List<TblProjectInfo> getNewProjectByPage(TblProjectInfo projectSearchInfo, Integer pageNumber, Integer pageSize);

	/**
	 * 更新
	 * @param projectInfo
	 * @param request
	 */

	void updateNewProject(TblProjectInfo projectInfo, HttpServletRequest request);

	void updateSystemDirectory(Long projectId, HttpServletRequest request, Integer projectType) throws Exception;

	void insertTmpNewProject(TblProjectInfo projectInfo, HttpServletRequest request);

	void updateTmpNewProject(TblProjectInfo projectInfo, HttpServletRequest request);

}
