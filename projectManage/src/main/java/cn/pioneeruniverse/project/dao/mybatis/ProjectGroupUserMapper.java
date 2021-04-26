package cn.pioneeruniverse.project.dao.mybatis;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.project.entity.TblProjectGroupUser;

public interface ProjectGroupUserMapper {

	List<Long> findProjectGroupIdsByUserId(Long userId);

	List<Long> findProjectGroupIdsByUserIdAndUserPost(HashMap<String, Object> map);

	List<Long> findUserId(HashMap<String, Object> map);

	List<TblProjectGroupUser> selectProjectGroupUserByProjectGroupId(Long projectGroupId);

	/**
	*@author liushan
	*@Description 查询测试组长、测试管理岗人员
	*@Date 2020/5/22
	*@Param [projectId]
	*@return java.util.List<cn.pioneeruniverse.project.entity.TblProjectGroupUser>
	**/
	String selectGroupUserByProjectIdAndUserPost(Long projectId);

	void updateProjectGroupUser(HashMap<String, Object> map3);

	List<Long> selectIdByProjectGroupId(Long projectGroupId);

	void insertProjectGroupUser(TblProjectGroupUser tblProjectGroupUser);

	void delProjectGroupUser(List<Long> ls);

	void deleteProjectGroupUser(HashMap<String, Object> map);

	List<String> getProjectUserPost(@Param("projectId")Long projectId, @Param("userPostIds")String userPostIds);


    List<Long> findProjectGroupIdsByUserIdAndUserPostNew(HashMap<String, Object> map);
}
