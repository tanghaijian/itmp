package cn.pioneeruniverse.project.service.question.impl;

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
import cn.pioneeruniverse.project.dao.mybatis.TblQuestionInfoMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblQuestionLogMapper;
import cn.pioneeruniverse.project.dao.mybatis.UserMapper;
import cn.pioneeruniverse.project.entity.TblQuestionInfo;
import cn.pioneeruniverse.project.entity.TblQuestionLog;
import cn.pioneeruniverse.project.service.question.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {
	
	@Autowired
	private TblQuestionInfoMapper tblQuestionInfoMapper;
	
	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private TblQuestionLogMapper tblQuestionLogMapper; 
	
	@Autowired
	private UserMapper userMapper; 

	//问题列表
	@Override
	@Transactional(readOnly=true)
	public List<TblQuestionInfo> getQuestions(Long projectId, HttpServletRequest request) {
		List<TblQuestionInfo> list = tblQuestionInfoMapper.getQuestions(projectId);
		Long number = Long.valueOf(1);
		for (TblQuestionInfo tblQuestionInfo : list) {
			tblQuestionInfo.setQuestionPriorityName(getValue(tblQuestionInfo.getQuestionPriority(),"TBL_QUESTION_INFO_QUESTION_PRIORITY"));
			tblQuestionInfo.setNumber(number);
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

	
	//删除问题
	@Override
	@Transactional(readOnly=false)
	public void deleteQuestion(Long id, HttpServletRequest request) {
		TblQuestionInfo questionInfo = new TblQuestionInfo();
		questionInfo.setId(id);
		questionInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		questionInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tblQuestionInfoMapper.deleteQuestion(questionInfo);
	}

	
	//新增问题
	@Override
	@Transactional(readOnly=false)
	public void insertQuestion(TblQuestionInfo tblQuestionInfo, HttpServletRequest request) {
		tblQuestionInfo.setStatus(1);
		tblQuestionInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
		tblQuestionInfo.setCreateDate(new Timestamp(new Date().getTime()));
		tblQuestionInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblQuestionInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tblQuestionInfoMapper.insertQuestion(tblQuestionInfo);
		//新增问题日志
		TblQuestionLog tblQuestionLog = new TblQuestionLog();
		tblQuestionLog.setQuestionId(tblQuestionInfo.getId());
		tblQuestionLog.setLogType("新增问题");
		tblQuestionLog.setUserId(CommonUtil.getCurrentUserId(request));
		tblQuestionLog.setUserName(CommonUtil.getCurrentUserName(request));
		tblQuestionLog.setUserAccount(CommonUtil.getCurrentUserAccount(request));
		tblQuestionLog.setStatus(1);
		tblQuestionLog.setCreateBy(CommonUtil.getCurrentUserId(request));
		tblQuestionLog.setCreateDate(new Timestamp(new Date().getTime()));
		tblQuestionLog.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblQuestionLog.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tblQuestionLogMapper.insertLog(tblQuestionLog);
	}

	
	//问题详情
	@Override
	@Transactional(readOnly=true)
	public TblQuestionInfo getQuestionById(Long id) {
		TblQuestionInfo tblQuestionInfo = tblQuestionInfoMapper.getQuestionById(id);
		//问题详情中涉及的数据字典编码变为具体的名字
		tblQuestionInfo.setQuestionReasonTypeName(getValue(tblQuestionInfo.getQuestionReasonType(),"TBL_QUESTION_INFO_QUESTION_REASON_TYPE"));
		tblQuestionInfo.setQuestionImportanceName(getValue(tblQuestionInfo.getQuestionImportance(),"TBL_QUESTION_INFO_QUESTION_IMPORTANCE"));
		tblQuestionInfo.setQuestionEmergencyLevelName(getValue(tblQuestionInfo.getQuestionEmergencyLevel(),"TBL_QUESTION_INFO_QUESTION_EMERGENCY_LEVEL"));
		tblQuestionInfo.setQuestionPriorityName(getValue(tblQuestionInfo.getQuestionPriority(),"TBL_QUESTION_INFO_QUESTION_PRIORITY"));
		tblQuestionInfo.setHappenStageName(getValue(tblQuestionInfo.getHappenStage(),"TBL_QUESTION_INFO_HAPPEN_STAGE"));
		return tblQuestionInfo;
	}

	//问题日志
	@Override
	@Transactional(readOnly=true)
	public List<TblQuestionLog> getQuestionLog(Long id) {
		return tblQuestionLogMapper.getQuestionLog(id);
	}

	
	//编辑问题
	@Override
	@Transactional(readOnly=false)
	public void updateQuestion(TblQuestionInfo tblQuestionInfo, HttpServletRequest request) {
		//获取编辑前的旧数据
		TblQuestionInfo oldQuestionInfo = tblQuestionInfoMapper.getQuestionById(tblQuestionInfo.getId());
		tblQuestionInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblQuestionInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tblQuestionInfoMapper.updateQuestion(tblQuestionInfo);
		//新增问题日志
		TblQuestionLog log = new TblQuestionLog();
		log.setQuestionId(tblQuestionInfo.getId());
		log.setLogType("修改问题");
		log.setUserId(CommonUtil.getCurrentUserId(request));
		log.setUserName(CommonUtil.getCurrentUserName(request));
		log.setUserAccount(CommonUtil.getCurrentUserAccount(request));
		log.setStatus(1);
		log.setCreateBy(CommonUtil.getCurrentUserId(request));
		log.setCreateDate(new Timestamp(new Date().getTime()));
		log.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		log.setLastUpdateDate(new Timestamp(new Date().getTime()));
		//日志详情
		String logDetail = "";
		if(tblQuestionInfo.getResponsibleUserId() != oldQuestionInfo.getResponsibleUserId()) {
			String beforeName = userMapper.getUserNameById(oldQuestionInfo.getResponsibleUserId());
			String afterName = userMapper.getUserNameById(tblQuestionInfo.getResponsibleUserId());
			logDetail += "责任人：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(!tblQuestionInfo.getQuestionDescription().equals(oldQuestionInfo.getQuestionDescription())) {
			logDetail += "问题描述：&nbsp;&nbsp;“&nbsp;<b>"+oldQuestionInfo.getQuestionDescription()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblQuestionInfo.getQuestionDescription()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(tblQuestionInfo.getQuestionReasonType() != oldQuestionInfo.getQuestionReasonType()) {
			String beforeName = getValue(oldQuestionInfo.getQuestionReasonType(),"TBL_QUESTION_INFO_QUESTION_REASON_TYPE");
			String afterName = getValue(tblQuestionInfo.getQuestionReasonType(),"TBL_QUESTION_INFO_QUESTION_REASON_TYPE");
			logDetail += "原因分类：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(tblQuestionInfo.getQuestionImportance() != oldQuestionInfo.getQuestionImportance()) {
			String beforeName = getValue(oldQuestionInfo.getQuestionImportance(),"TBL_QUESTION_INFO_QUESTION_IMPORTANCE");
			String afterName = getValue(tblQuestionInfo.getQuestionImportance(),"TBL_QUESTION_INFO_QUESTION_IMPORTANCE");
			logDetail += "重要性：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(tblQuestionInfo.getQuestionEmergencyLevel() != oldQuestionInfo.getQuestionEmergencyLevel()) {
			String beforeName = getValue(oldQuestionInfo.getQuestionEmergencyLevel(),"TBL_QUESTION_INFO_QUESTION_EMERGENCY_LEVEL");
			String afterName = getValue(tblQuestionInfo.getQuestionEmergencyLevel(),"TBL_QUESTION_INFO_QUESTION_EMERGENCY_LEVEL");
			logDetail += "紧急程度：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(tblQuestionInfo.getQuestionPriority() != oldQuestionInfo.getQuestionPriority()) {
			String beforeName = getValue(oldQuestionInfo.getQuestionPriority(),"TBL_QUESTION_INFO_QUESTION_PRIORITY");
			String afterName = getValue(tblQuestionInfo.getQuestionPriority(),"TBL_QUESTION_INFO_QUESTION_PRIORITY");
			logDetail += "优先级：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(tblQuestionInfo.getHappenDate().getTime() != oldQuestionInfo.getHappenDate().getTime()) {
			logDetail += "发生日期：&nbsp;&nbsp;“&nbsp;<b>"+DateFormat.getDateInstance().format(oldQuestionInfo.getHappenDate())+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+DateFormat.getDateInstance().format(tblQuestionInfo.getHappenDate())+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(tblQuestionInfo.getEndDate().getTime() != oldQuestionInfo.getEndDate().getTime()) {
			logDetail += "结束日期：&nbsp;&nbsp;“&nbsp;<b>"+DateFormat.getDateInstance().format(oldQuestionInfo.getEndDate())+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+DateFormat.getDateInstance().format(tblQuestionInfo.getEndDate())+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(tblQuestionInfo.getHappenStage() != oldQuestionInfo.getHappenStage()) {
			String beforeName = getValue(oldQuestionInfo.getHappenStage(),"TBL_QUESTION_INFO_HAPPEN_STAGE");
			String afterName = getValue(tblQuestionInfo.getHappenStage(),"TBL_QUESTION_INFO_HAPPEN_STAGE");
			logDetail += "发生阶段：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(!tblQuestionInfo.getReasonAnalysis().equals(oldQuestionInfo.getReasonAnalysis())) {
			logDetail += "问题原因分析：&nbsp;&nbsp;“&nbsp;<b>"+oldQuestionInfo.getReasonAnalysis()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblQuestionInfo.getReasonAnalysis()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(!tblQuestionInfo.getCopingStrategyRecord().equals(oldQuestionInfo.getCopingStrategyRecord())) {
			logDetail += "解决措施执行情况：&nbsp;&nbsp;“&nbsp;<b>"+oldQuestionInfo.getCopingStrategyRecord()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblQuestionInfo.getCopingStrategyRecord()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(!tblQuestionInfo.getRemark().equals(oldQuestionInfo.getRemark())) {
			logDetail += "备注：&nbsp;&nbsp;“&nbsp;<b>"+oldQuestionInfo.getRemark()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblQuestionInfo.getRemark()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
		}
		if(logDetail.equals("")) {
			logDetail += "未作任何修改";
		}
		log.setLogDetail(logDetail);
		tblQuestionLogMapper.insertLog(log);
	}

	
	
	//项目群管理问题列表
	@Override
	@Transactional(readOnly=true)
	public List<TblQuestionInfo> getQuestionByProgram(Long programId, HttpServletRequest request) {
		List<TblQuestionInfo> list = tblQuestionInfoMapper.getQuestionByProgram(programId);
		Long number = Long.valueOf(1);
		for (TblQuestionInfo tblQuestionInfo : list) {
			tblQuestionInfo.setQuestionPriorityName(getValue(tblQuestionInfo.getQuestionPriority(),"TBL_QUESTION_INFO_QUESTION_PRIORITY"));
			tblQuestionInfo.setNumber(number);
			number ++;
		}
		return list;
	}

}
