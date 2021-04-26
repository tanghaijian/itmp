package cn.pioneeruniverse.project.service.projectgroupuser.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.pioneeruniverse.project.dao.mybatis.ProjectGroupUserMapper;
import cn.pioneeruniverse.project.service.projectgroupuser.ProjectGroupUserService;

@Service
public class ProjectGroupUserServiceImpl implements ProjectGroupUserService {

	@Autowired
	private ProjectGroupUserMapper projectGroupUserMapper;
	
	/**
	 * 
	* @Title: findProjectGroupIdsByUserId
	* @Description: 获取某人的项目小组id
	* @author author
	* @param userId 人员id
	* @return List<Long> 项目小组id列表
	 */
	@Override
	public List<Long> findProjectGroupIdsByUserId(Long userId) {
		return projectGroupUserMapper.findProjectGroupIdsByUserId(userId);
		
	}



}
