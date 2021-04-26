package cn.pioneeruniverse.project.dao.mybatis;

import java.util.HashMap;
import java.util.List;

import cn.pioneeruniverse.project.entity.TblProjectInfo;
/**
 *
 * @ClassName: ProjectInfoMapper
 * @Description: 项目mapper接口
 * @author author
 *
 */
public interface ProjectInfoMapper {
//	, String currentPage, String pageSize

	List<TblProjectInfo> selectProjects(HashMap<String, Object> map);

	void insertProject(TblProjectInfo tblProjectInfo);

	void deleteProjectById(Long id);

	/**
	 * 查询
	 * @param id
	 * @return
	 */
	TblProjectInfo selectProjectById(Long id);

	void updateProject(TblProjectInfo tblProjectInfo);

	Integer selectCount();


    List<TblProjectInfo> getAllProjectByCurrentUserId(Long currentUserId);

	List<TblProjectInfo> getAllProject();
}
