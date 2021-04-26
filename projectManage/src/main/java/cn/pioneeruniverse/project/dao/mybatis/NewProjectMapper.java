package cn.pioneeruniverse.project.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.project.entity.TblProjectInfo;
/**
 *
 * @ClassName: NewProjectMapper
 * @Description: 新项目mapper
 * @author author
 *
 */
public interface NewProjectMapper {

	/**
	 * 查询所有新项目
	 * @param map
	 * @return List<TblProjectInfo>
	 */
	List<TblProjectInfo> getAllNewProject(Map<String, Object> map);

	List<TblProjectInfo> getNewProjectByUid(Map<String, Object> map);

	TblProjectInfo getNewProjectById(Long id);

	Long getCountNewProject();

	void insertNewProject(TblProjectInfo projectInfo);

	List<TblProjectInfo> getNewProjectByPage(HashMap<String, Object> map);

	/**
	 * 更新
	 * @param projectInfo
	 */

	void updateNewProject(TblProjectInfo projectInfo);

	/**
	 * 获取项目编码
	 * @param id
	 * @return String
	 */

	String getProjectCodeById(Long id);

}
