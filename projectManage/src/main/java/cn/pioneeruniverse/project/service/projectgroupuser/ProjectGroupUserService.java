package cn.pioneeruniverse.project.service.projectgroupuser;

import java.util.List;

public interface ProjectGroupUserService {
	/**
	 *
	 * @Title: findProjectGroupIdsByUserId
	 * @Description: 获取某人的项目小组id
	 * @author author
	 * @param userId 人员id
	 * @return List<Long> 项目小组id列表
	 */
	List<Long> findProjectGroupIdsByUserId(Long userId);



}
