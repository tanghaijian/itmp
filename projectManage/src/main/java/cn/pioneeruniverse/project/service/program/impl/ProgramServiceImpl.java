package cn.pioneeruniverse.project.service.program.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.project.dao.mybatis.TblProgramInfoMapper;
import cn.pioneeruniverse.project.entity.TblProgramInfo;
import cn.pioneeruniverse.project.entity.TblProgramProject;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
import cn.pioneeruniverse.project.service.program.ProgramService;

@Service
public class ProgramServiceImpl implements ProgramService {
	
	@Autowired
	private TblProgramInfoMapper tblProgramMapper;
	
	@Autowired
	private RedisUtils redisUtils;

	/**
	 * 
	* @Title: getAllPrograms
	* @Description: 获取所有项目群列表
	* @author author
	* @param programInfo：封装的查询条件
	* @param uid 当前用户ID
	* @param roleCodes
	* @param page 第几页
	* @param rows 每页数据量
	* @return List<TblProgramInfo>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TblProgramInfo> getAllPrograms(TblProgramInfo programInfo, Long uid, List<String> roleCodes,
			Integer page, Integer rows) {
		Integer start = (page-1)*rows;
		Map<String,Object> map = new HashMap<>();
		map.put("programInfo", programInfo);
		map.put("uid", uid);
		map.put("start", start);
		map.put("rows", rows);
		List<TblProgramInfo> list = new ArrayList<>();
		//如果当前登录用户角色是系统管理员
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) { 
			list = tblProgramMapper.getAllPrograms(map);
		}else {
			//其他用户查询
			list = tblProgramMapper.getProgramsByUid(map);
		}
		return list;
	}

	/**
	 * 
	* @Title: getProgramById
	* @Description: 根据ID获取项目群详情
	* @author author
	* @param id 项目群ID
	* @return TblProgramInfo
	 */
	@Override
	@Transactional(readOnly=true)
	public TblProgramInfo getProgramById(Long id) {
		// TODO Auto-generated method stub
		TblProgramInfo programInfo = tblProgramMapper.getProgramById(id);
		List<TblProjectInfo> list = tblProgramMapper.getProjectsByProgramId(id);
		for (TblProjectInfo tblProjectInfo : list) {
			Integer projectStatus = tblProjectInfo.getProjectStatus();
			String redisStr = redisUtils.get("TBL_PROJECT_INFO_PROJECT_STATUS").toString();
			JSONObject jsonObj = JSON.parseObject(redisStr);
			String statusName = jsonObj.get(projectStatus).toString();
			tblProjectInfo.setProjectStatusName(statusName);
		}
		programInfo.setProjectList(list);
		return programInfo;
	}

	/**
	 * 
	* @Title: updateProgram
	* @Description:更新项目群
	* @author author
	* @param tblProgramInfo 项目群信息
	* @param request
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateProgram(TblProgramInfo tblProgramInfo, HttpServletRequest request) {
		tblProgramInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblProgramInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		//更新项目群
		tblProgramMapper.updateProgram(tblProgramInfo);
		//逻辑删除项目群和项目关联关系
		tblProgramMapper.updateProgramProject(tblProgramInfo.getId());
		String pIds = tblProgramInfo.getProjectIds();
		//重新关联项目群和项目关联关系
		if(!pIds.equals("")) {
			String[] projectIds = pIds.split(",");
			for (String projectId : projectIds) {
				TblProgramProject programProject = new TblProgramProject();
				programProject.setProgramId(tblProgramInfo.getId());
				programProject.setProjectId(Long.valueOf(projectId));
				programProject.setStatus(1);
				programProject.setCreateBy(CommonUtil.getCurrentUserId(request));
				programProject.setCreateDate(new Timestamp(new Date().getTime()));
				programProject.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
				programProject.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tblProgramMapper.insertProgramProject(programProject);
			}
		}
	}

}
