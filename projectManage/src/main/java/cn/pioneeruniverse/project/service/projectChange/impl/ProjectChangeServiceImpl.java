package cn.pioneeruniverse.project.service.projectChange.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.project.dao.mybatis.TblProjectChangeInfoMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblProjectChangeLogMapper;
// import cn.pioneeruniverse.project.dao.mybatis.TblQuestionLogMapper;
import cn.pioneeruniverse.project.dao.mybatis.UserMapper;
import cn.pioneeruniverse.project.entity.TblProjectChangeInfo;
import cn.pioneeruniverse.project.entity.TblProjectChangeLog;
// import cn.pioneeruniverse.project.entity.TblQuestionLog;
import cn.pioneeruniverse.project.service.projectChange.ProjectChangeService;

@Service
public class ProjectChangeServiceImpl implements ProjectChangeService {
	
	@Autowired
	private TblProjectChangeInfoMapper tblProjectChangeInfoMapper;
	
	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private UserMapper userMapper; 
	
	@Autowired
	private TblProjectChangeLogMapper tblProjectChangeLogMapper;

	/**
	 * 
	* @Title: getChanges
	* @Description: 获取项目变更列表
	* @author author
	* @param projectId 项目id
	* @param request
	* @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TblProjectChangeInfo> getChanges(Long projectId, HttpServletRequest request) {
		List<TblProjectChangeInfo> list = tblProjectChangeInfoMapper.getChanges(projectId);
		Long number = Long.valueOf(1);
		for (TblProjectChangeInfo tblProjectChangeInfo : list) {
			tblProjectChangeInfo.setChangeStatusName(getValue(tblProjectChangeInfo.getChangeStatus(),"TBL_PROJECT_CHANGE_INFO_CHANGE_STATUS"));
			tblProjectChangeInfo.setNumber(number);
			number ++;
		}
		return list;
	}
	
	//数据字典
	private String getValue(Integer key, String string) {
		String redisStr = redisUtils.get(string).toString();
		JSONObject jsonObj = JSON.parseObject(redisStr);
		String name = jsonObj.get(key).toString();
		return name;
	}

	
	/**
	 * 
	* @Title: deleteProjectChange
	* @Description: 删除项目变更
	* @author author
	* @param id 项目id
	* @param request
	 */
	@Override
	@Transactional(readOnly=false)
	public void deleteProjectChange(Long id, HttpServletRequest request) {
		TblProjectChangeInfo tblProjectChangeInfo = new TblProjectChangeInfo();
		tblProjectChangeInfo.setId(id);
		tblProjectChangeInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblProjectChangeInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tblProjectChangeInfoMapper.deleteProjectChange(tblProjectChangeInfo);
	}

	
	/**
	 * 
	* @Title: insertProjectChange
	* @Description: 新增变更
	* @author author
	* @param tblProjectChangeInfo 变更信息
	* @param request
	 */
	@Override
	@Transactional(readOnly=false)
	public void insertProjectChange(TblProjectChangeInfo tblProjectChangeInfo, HttpServletRequest request) {
		tblProjectChangeInfo.setStatus(1);
		tblProjectChangeInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
		tblProjectChangeInfo.setCreateDate(new Timestamp(new Date().getTime()));
		tblProjectChangeInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblProjectChangeInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tblProjectChangeInfoMapper.insertProjectChange(tblProjectChangeInfo);
		//新增变更日志
		TblProjectChangeLog log = new TblProjectChangeLog();
		log.setProjectChangeId(tblProjectChangeInfo.getId());
		log.setLogType("新增变更");
		log.setUserId(CommonUtil.getCurrentUserId(request));
		log.setUserName(CommonUtil.getCurrentUserName(request));
		log.setUserAccount(CommonUtil.getCurrentUserAccount(request));
		log.setStatus(1);
		log.setCreateBy(CommonUtil.getCurrentUserId(request));
		log.setCreateDate(new Timestamp(new Date().getTime()));
		log.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		log.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tblProjectChangeLogMapper.insertLog(log);
	}

	
	//获取变更详情
	@Override
	@Transactional(readOnly=true)
	public TblProjectChangeInfo getProjectChangeById(Long id) {
		TblProjectChangeInfo tblProjectChangeInfo = tblProjectChangeInfoMapper.getProjectChangeById(id);
		tblProjectChangeInfo.setChangeStatusName(getValue(tblProjectChangeInfo.getChangeStatus(),"TBL_PROJECT_CHANGE_INFO_CHANGE_STATUS"));
		return tblProjectChangeInfo;
	}

	
	//获取变更日志
	@Override
	@Transactional(readOnly=true)
	public List<TblProjectChangeLog> getChangeLog(Long id) {
		return tblProjectChangeLogMapper.getChangeLog(id);
	}

	
	//编辑变更记录
	@Override
	@Transactional(readOnly=false)
	public void updateProjectChange(TblProjectChangeInfo tblProjectChangeInfo, HttpServletRequest request) {
		//获取编辑前的旧数据
		TblProjectChangeInfo oldProjectChangeInfo = tblProjectChangeInfoMapper.getProjectChangeById(tblProjectChangeInfo.getId());
		tblProjectChangeInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblProjectChangeInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tblProjectChangeInfoMapper.updateProjectChange(tblProjectChangeInfo);
		//新增变更日志
		TblProjectChangeLog log = new TblProjectChangeLog();
		log.setProjectChangeId(tblProjectChangeInfo.getId());
		log.setLogType("修改变更");
		log.setUserId(CommonUtil.getCurrentUserId(request));
		log.setUserName(CommonUtil.getCurrentUserName(request));
		log.setUserAccount(CommonUtil.getCurrentUserAccount(request));
		log.setStatus(1);
		log.setCreateBy(CommonUtil.getCurrentUserId(request));
		log.setCreateDate(new Timestamp(new Date().getTime()));
		log.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		log.setLastUpdateDate(new Timestamp(new Date().getTime()));
		//日志详细内容
		String logDetail = "";
		if(tblProjectChangeInfo.getResponsibleUserId() != oldProjectChangeInfo.getResponsibleUserId()) {
			String beforeName = userMapper.getUserNameById(oldProjectChangeInfo.getResponsibleUserId());
			String afterName = userMapper.getUserNameById(tblProjectChangeInfo.getResponsibleUserId());
			logDetail += "责任人：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(!tblProjectChangeInfo.getChangeItem().equals(oldProjectChangeInfo.getChangeItem())) {
			logDetail += "变更事项：&nbsp;&nbsp;“&nbsp;<b>"+oldProjectChangeInfo.getChangeItem()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblProjectChangeInfo.getChangeItem()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(tblProjectChangeInfo.getSubmitDate().getTime() != oldProjectChangeInfo.getSubmitDate().getTime()) {
			logDetail += "提出日期：&nbsp;&nbsp;“&nbsp;<b>"+DateFormat.getDateInstance().format(oldProjectChangeInfo.getSubmitDate())+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+DateFormat.getDateInstance().format(tblProjectChangeInfo.getSubmitDate())+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(tblProjectChangeInfo.getApproveDate() != null && oldProjectChangeInfo.getApproveDate() != null) {
			if(tblProjectChangeInfo.getApproveDate().getTime() != oldProjectChangeInfo.getApproveDate().getTime()) {
				logDetail += "批准日期：&nbsp;&nbsp;“&nbsp;<b>"+DateFormat.getDateInstance().format(oldProjectChangeInfo.getApproveDate())+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+DateFormat.getDateInstance().format(tblProjectChangeInfo.getApproveDate())+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
		}
		if(tblProjectChangeInfo.getChangeStatus() != oldProjectChangeInfo.getChangeStatus()) {
			String beforeName = getValue(oldProjectChangeInfo.getChangeStatus(),"TBL_PROJECT_CHANGE_INFO_CHANGE_STATUS");
			String afterName = getValue(tblProjectChangeInfo.getChangeStatus(),"TBL_PROJECT_CHANGE_INFO_CHANGE_STATUS");
			logDetail += "状态：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(!tblProjectChangeInfo.getAnalyseSummary().equals(oldProjectChangeInfo.getAnalyseSummary())) {
			logDetail += "分析汇总：&nbsp;&nbsp;“&nbsp;<b>"+oldProjectChangeInfo.getAnalyseSummary()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblProjectChangeInfo.getAnalyseSummary()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(!tblProjectChangeInfo.getResultExplain().equals(oldProjectChangeInfo.getResultExplain())) {
			logDetail += "执行结果说明：&nbsp;&nbsp;“&nbsp;<b>"+oldProjectChangeInfo.getResultExplain()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblProjectChangeInfo.getResultExplain()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(!tblProjectChangeInfo.getRemark().equals(oldProjectChangeInfo.getRemark())) {
			logDetail += "执行结果说明：&nbsp;&nbsp;“&nbsp;<b>"+oldProjectChangeInfo.getRemark()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblProjectChangeInfo.getRemark()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(logDetail.equals("")) {
			logDetail += "未作任何修改";
		}
		log.setLogDetail(logDetail);
		tblProjectChangeLogMapper.insertLog(log);
	}

	
	//项目群管理变更列表
	@Override
	public List<TblProjectChangeInfo> getChangesByProgram(Long programId, HttpServletRequest request) {
		List<TblProjectChangeInfo> list = tblProjectChangeInfoMapper.getChangesByProgram(programId);
		Long number = Long.valueOf(1);
		for (TblProjectChangeInfo tblProjectChangeInfo : list) {
			tblProjectChangeInfo.setChangeStatusName(getValue(tblProjectChangeInfo.getChangeStatus(),"TBL_PROJECT_CHANGE_INFO_CHANGE_STATUS"));
			tblProjectChangeInfo.setNumber(number);
			number ++;
		}
		return list;
	}

	
}
