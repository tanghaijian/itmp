package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblProjectChangeInfo;
/**
 *
 * @ClassName:TblProjectChangeInfoMapper
 * @Description
 * @author author
 * @date 2020年8月27日
 *
 */
public interface TblProjectChangeInfoMapper {
	/**
	 * 查询
	 * @param projectId
	 * @return List<TblProjectChangeInfo>
	 */

	List<TblProjectChangeInfo> getChanges(Long projectId);

	/**
	 * 删除
	 * @param tblProjectChangeInfo
	 */

	void deleteProjectChange(TblProjectChangeInfo tblProjectChangeInfo);

	/**
	 * 新增
	 * @param tblProjectChangeInfo
	 */

	void insertProjectChange(TblProjectChangeInfo tblProjectChangeInfo);

	/**
	 * 查询
	 * @param id
	 * @return TblProjectChangeInfo
	 */

	TblProjectChangeInfo getProjectChangeById(Long id);

	void updateProjectChange(TblProjectChangeInfo tblProjectChangeInfo);

	List<TblProjectChangeInfo> getChangesByProgram(Long programId);

}
