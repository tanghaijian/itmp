package cn.pioneeruniverse.project.service.systeminfo.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.pioneeruniverse.project.dao.mybatis.SystemInfoMapper;
import cn.pioneeruniverse.project.entity.TblSystemInfo;
import cn.pioneeruniverse.project.service.systeminfo.SystemInfoService;

@Service
public class SystemInfoServiceImpl implements SystemInfoService {
	
	@Autowired
	private SystemInfoMapper systemInfoMapper;

	/**
	 * 
	* @Title: selectSystemInfo
	* @Description: 获取系统信息
	* @author author
	* @param tblSystemInfo 封装的查询条件
	* @param pageNumber 第几页
	* @param pageSize 每页数量
	* @return List<TblSystemInfo>
	 */
	@Override
	public List<TblSystemInfo> selectSystemInfo(TblSystemInfo tblSystemInfo, Integer pageNumber, Integer pageSize) {
		HashMap<String, Object> map = new HashMap<>();
		Integer start = (pageNumber-1)*pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("tblSystemInfo", tblSystemInfo);
		return systemInfoMapper.selectSystemInfo(map);
	}


	/**
	 * 
	* @Title: updateSystemInfo
	* @Description: 更新系统信息
	* @author author
	* @param projectId 项目id
	* @param systemNames 系统名
	 */
	@Override
	public void updateSystemInfo(Long projectId, List<String> systemNames) {
		HashMap<String,Object> map = new HashMap<>();
		map.put("projectId", projectId);
		map.put("systemNames", systemNames);
		systemInfoMapper.updateSystemInfo(map);
		
	}


	/**
	 * 
	* @Title: selectSystemName
	* @Description: 获取系统名
	* @author author
	* @param id 系统id
	* @return
	 */
	@Override
	public List<String> selectSystemName(Long id) {
		return systemInfoMapper.selectSystemName(id);
	}


	/**
	 * 
	* @Title: updateSystemInfoBySystemName
	* @Description: 根据系统名取消系统和项目的关联
	* @author author
	* @param systemName 系统名
	 */
	@Override
	public void updateSystemInfoBySystemName(String systemName) {
		systemInfoMapper.updateSystemInfoBySystemName(systemName);
	}

}
