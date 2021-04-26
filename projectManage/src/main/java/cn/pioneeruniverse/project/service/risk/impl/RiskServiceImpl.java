package cn.pioneeruniverse.project.service.risk.impl;

import java.sql.Timestamp;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.common.utils.*;
import cn.pioneeruniverse.project.dao.mybatis.*;
import cn.pioneeruniverse.project.entity.*;
import cn.pioneeruniverse.project.feignInterface.TestTaskInterface;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.project.service.risk.RiskService;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RiskServiceImpl implements RiskService {
	
	@Autowired
	private TblRiskInfoMapper tblRiskInfoMapper;

	@Autowired
	private RequirementFeatureMapper featureMapper;

	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private TblRiskLogMapper tblRiskLogMapper;
	
	@Autowired
	private TblRiskAttachementMapper riskAttachementMapper;
	@Autowired
	private ProjectGroupUserMapper groupUserMapper;

	@Autowired
	private TestTaskInterface testTaskInterface;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private S3Util s3Util;

	@Value("${s3.riskBucket}")
	private String riskBucket;

	/**
	*@author liushan
	*@Description
	*@Date 2020/7/29
	*@Param [riskInfo 风险实体类接收参数, projectType 项目类型标识, page, pageSzie, request]
	*@return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@Override
	@Transactional(readOnly=true)
	public Map<String,Object> getRiskInfo(TblRiskInfo riskInfo, Integer projectType, Integer page, Integer pageSzie, HttpServletRequest request) {
		Map<String,Object> result = new HashMap<>();
		List<TblRiskInfo> list = new ArrayList<>();
		if(projectType.intValue() == 1){
			// 运维类项目进入

			list = tblRiskInfoMapper.getAllRisk(riskInfo,(page-1)*pageSzie,pageSzie);
			int total = tblRiskInfoMapper.getAllRiskCount(riskInfo);
			result.put("total", total);
			for (TblRiskInfo tblRiskInfo : list) {
				String groupUser = groupUserMapper.selectGroupUserByProjectIdAndUserPost(tblRiskInfo.getProjectId());
				tblRiskInfo.setGroupUser(groupUser);
				if(tblRiskInfo.getRequirementFeatureId() != null){
					try {
						Map<String,Object>  map = testTaskInterface.getRequirementFeatureNameById(tblRiskInfo.getRequirementFeatureId());
						tblRiskInfo.setRequirementFeatureName(map.get("featureName").toString());
					}catch (Exception e){
						tblRiskInfo.setRequirementFeatureName(null);
					}
				}
			}
		} else if(projectType.intValue() == 2){
			// 新建类项目进入
			list = tblRiskInfoMapper.getRiskInfo(riskInfo.getProjectId());
			for (TblRiskInfo tblRiskInfo : list) {
				String riskPriorityName = getValue(tblRiskInfo.getRiskPriority(),"TBL_RISK_INFO_RISK_PRIORITY");  //优先级
				String statusName = getValue(tblRiskInfo.getRiskStatus(),"TBL_RISK_INFO_RISK_STATUS");   //状态
				tblRiskInfo.setRiskPriorityName(riskPriorityName);
				tblRiskInfo.setStatusName(statusName);
			}
		}

		result.put("data", list);
		return result;
	}

	/**
	*@author liushan
	*@Description 数据字典
	*@Date 2020/7/31
	 * @param key
 * @param string
	*@return java.lang.String
	**/
	private String getValue(Integer key, String string) {
		try {
			String redisStr = redisUtils.get(string).toString();
			JSONObject jsonObj = JSON.parseObject(redisStr);
			return jsonObj.get(key).toString();
		}catch (Exception e){
			return "";
		}
	}

	/**
	*@author liushan
	*@Description 新增风险
	*@Date 2020/7/31
	 * @param tblRiskInfo 风险数据
 * @param files 附件
 * @param request
	*@return void
	**/
	@Override
	@Transactional(readOnly=false)
	public void insertRiskInfo(TblRiskInfo tblRiskInfo, MultipartFile[] files, HttpServletRequest request) throws Exception{
		tblRiskInfo.setStatus(1);
		tblRiskInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
		tblRiskInfo.setCreateDate(new Timestamp(System.currentTimeMillis()));
		tblRiskInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblRiskInfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		tblRiskInfoMapper.insertRiskInfo(tblRiskInfo);

		// 添加附件
		updateFiles(tblRiskInfo.getId(),files,request);

		//新增风险日志
		TblRiskLog log = new TblRiskLog();
		log.setRiskId(tblRiskInfo.getId());
		log.setLogType("新增风险");
		log.setUserId(CommonUtil.getCurrentUserId(request));
		log.setUserName(CommonUtil.getCurrentUserName(request));
		log.setUserAccount(CommonUtil.getCurrentUserAccount(request));
		log.setStatus(1);
		log.setCreateBy(CommonUtil.getCurrentUserId(request));
		log.setCreateDate(new Timestamp(System.currentTimeMillis()));
		log.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		log.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		tblRiskLogMapper.insertLog(log);
	}

	/**
	*@author liushan
	*@Description 删除风险
	*@Date 2020/7/31
	 * @param id
 * @param request
	*@return void
	**/
	@Override
	@Transactional(readOnly=false)
	public void deleteRiskInfo(Long id, HttpServletRequest request) {
		TblRiskInfo riskInfo = new TblRiskInfo();
		riskInfo.setId(id);
		riskInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		riskInfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		tblRiskInfoMapper.deleteRiskInfo(riskInfo);
	}

	/**
	*@author liushan
	*@Description 获取详情
	*@Date 2020/7/31
	 * @param id
	*@return cn.pioneeruniverse.project.entity.TblRiskInfo
	**/
	@Override
	@Transactional(readOnly=true)
	public TblRiskInfo getRiskInfoById(Long id) {
		TblRiskInfo tblRiskInfo = tblRiskInfoMapper.getRiskInfoById(id);
		tblRiskInfo.setRiskPriorityName(getValue(tblRiskInfo.getRiskPriority(),"TBL_RISK_INFO_RISK_PRIORITY"));
		tblRiskInfo.setStatusName(getValue(tblRiskInfo.getRiskStatus(),"TBL_RISK_INFO_RISK_STATUS"));
		tblRiskInfo.setRiskTypeName(getValue(tblRiskInfo.getRiskType(),"TBL_RISK_INFO_RISK_TYPE"));
		tblRiskInfo.setRiskFactorName(getValue(tblRiskInfo.getRiskFactor(),"TBL_RISK_INFO_RISK_FACTOR"));
		tblRiskInfo.setRiskProbabilityName(getValue(tblRiskInfo.getRiskProbability(),"TBL_RISK_INFO_RISK_PROBABILITY"));
		return tblRiskInfo;
	}

	/**
	*@author liushan
	*@Description 获取风险数据
	*@Date 2020/7/31
	 * @param id
	*@return cn.pioneeruniverse.project.entity.TblRiskInfo
	**/
    @Override
    @Transactional(readOnly=true)
    public TblRiskInfo getRiskById(Long id) {
        TblRiskInfo tblRiskInfo = tblRiskInfoMapper.getRiskById(id);
		if(tblRiskInfo.getRequirementFeatureId() != null){
			try {
				tblRiskInfo.setRequirementFeatureName(testTaskInterface.getRequirementFeatureNameById(tblRiskInfo.getRequirementFeatureId()).get("featureName").toString());
			}catch (Exception e){
				tblRiskInfo.setRequirementFeatureName(null);
			}
		}
        return tblRiskInfo;
    }

    /**
    *@author liushan
    *@Description 获取风险日志
    *@Date 2020/7/31
     * @param id
    *@return java.util.List<cn.pioneeruniverse.project.entity.TblRiskLog>
    **/
	@Override
	@Transactional(readOnly=true)
	public List<TblRiskLog> getRiskLog(Long id) {
		return tblRiskLogMapper.getRiskLog(id);
		
	}

	/**
	*@author liushan
	*@Description 获取风险附件
	*@Date 2020/4/28
	*@Param [id]
	*@return java.util.List<cn.pioneeruniverse.project.entity.TblRiskAttachement>
	**/
	@Override
	@Transactional(readOnly=true)
	public List<TblRiskAttachement> getRiskAttachement(Long id) {
		return riskAttachementMapper.getRiskAttachement(id);
	}

	/**
	*@author liushan
	*@Description 编辑风险
	*@Date 2020/7/31
	 * @param tblRiskInfo 
 * @param projectType
 * @param files
 * @param removeFileIds
 * @param request
	*@return void
	**/
	@Override
	@Transactional(readOnly=false)
	public void updateRisk(TblRiskInfo tblRiskInfo, Integer projectType, MultipartFile[] files, String removeFileIds, HttpServletRequest request) throws Exception{
		//获取修改之前的旧数据
		TblRiskInfo oldTblRiskInfo = new TblRiskInfo();

		if(projectType.intValue() == 1){
			// 运维类项目进入
			oldTblRiskInfo = getRiskById(tblRiskInfo.getId());
		} else if(projectType.intValue() == 2){
			// 新建类项目进入
			oldTblRiskInfo = tblRiskInfoMapper.getRiskInfoById(tblRiskInfo.getId());
		}

		tblRiskInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblRiskInfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		tblRiskInfoMapper.updateRisk(tblRiskInfo);

		if(projectType.intValue() == 1){
			// 运维类项目进入
			// 添加附件
			updateFiles(tblRiskInfo.getId(),files,request);

			// 删除附件
			if(StringUtils.isNotEmpty(removeFileIds)){
				riskAttachementMapper.removeFile(removeFileIds,CommonUtil.getCurrentUserId(request));
			}

			// 日志
			// log = new TblRiskLog(tblRiskInfo.getId(),"修改风险",
					//CommonUtils.updateFieledsReflect(oldTblRiskInfo,getRiskById(tblRiskInfo.getId()),null),request);

		} else if(projectType.intValue() == 2){
			// 新建类项目进入
			String logDetail = "";
			if(tblRiskInfo.getResponsibleUserId()!=oldTblRiskInfo.getResponsibleUserId()) {
				String beforeName = userMapper.getUserNameById(oldTblRiskInfo.getResponsibleUserId());
				String afterName = userMapper.getUserNameById(tblRiskInfo.getResponsibleUserId());
				logDetail += "责任人：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(!tblRiskInfo.getRiskDescription().equals(oldTblRiskInfo.getRiskDescription())) {
				logDetail += "风险描述：&nbsp;&nbsp;“&nbsp;<b>"+oldTblRiskInfo.getRiskDescription()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblRiskInfo.getRiskDescription()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(tblRiskInfo.getRiskType()!=oldTblRiskInfo.getRiskType()) {
				String beforeName = getValue(oldTblRiskInfo.getRiskType(),"TBL_RISK_INFO_RISK_TYPE");
				String afterName = getValue(tblRiskInfo.getRiskType(),"TBL_RISK_INFO_RISK_TYPE");
				logDetail += "风险分类：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(tblRiskInfo.getRiskFactor()!=oldTblRiskInfo.getRiskFactor()) {
				String beforeName = getValue(oldTblRiskInfo.getRiskFactor(),"TBL_RISK_INFO_RISK_FACTOR");
				String afterName = getValue(tblRiskInfo.getRiskFactor(),"TBL_RISK_INFO_RISK_FACTOR");
				logDetail += "危险度：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(tblRiskInfo.getRiskProbability()!=oldTblRiskInfo.getRiskProbability()) {
				String beforeName = getValue(oldTblRiskInfo.getRiskProbability(),"TBL_RISK_INFO_RISK_PROBABILITY");
				String afterName = getValue(tblRiskInfo.getRiskProbability(),"TBL_RISK_INFO_RISK_PROBABILITY");
				logDetail += "发生概率：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(tblRiskInfo.getRiskPriority()!=oldTblRiskInfo.getRiskPriority()) {
				String beforeName = getValue(oldTblRiskInfo.getRiskPriority(),"TBL_RISK_INFO_RISK_PRIORITY");
				String afterName = getValue(tblRiskInfo.getRiskPriority(),"TBL_RISK_INFO_RISK_PRIORITY");
				logDetail += "风险优先级：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(!tblRiskInfo.getRiskInfluence().equals(oldTblRiskInfo.getRiskInfluence())) {
				logDetail += "风险影响：&nbsp;&nbsp;“&nbsp;<b>"+oldTblRiskInfo.getRiskInfluence()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblRiskInfo.getRiskInfluence()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(!tblRiskInfo.getRiskTriggers().equals(oldTblRiskInfo.getRiskTriggers())) {
				logDetail += "触发条件：&nbsp;&nbsp;“&nbsp;<b>"+oldTblRiskInfo.getRiskTriggers()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblRiskInfo.getRiskTriggers()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(tblRiskInfo.getRiskStatus()!=oldTblRiskInfo.getRiskStatus()) {
				String beforeName = getValue(oldTblRiskInfo.getRiskStatus(),"TBL_RISK_INFO_RISK_STATUS");
				String afterName = getValue(tblRiskInfo.getRiskStatus(),"TBL_RISK_INFO_RISK_STATUS");
				logDetail += "风险状态：&nbsp;&nbsp;“&nbsp;<b>"+beforeName+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+afterName+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(!tblRiskInfo.getRemark().equals(oldTblRiskInfo.getRemark())) {
				logDetail += "备注：&nbsp;&nbsp;“&nbsp;<b>"+oldTblRiskInfo.getRemark()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblRiskInfo.getRemark()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(!tblRiskInfo.getCopingStrategy().equals(oldTblRiskInfo.getCopingStrategy())) {
				logDetail += "缓解措施：&nbsp;&nbsp;“&nbsp;<b>"+oldTblRiskInfo.getCopingStrategy()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblRiskInfo.getCopingStrategy()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(!tblRiskInfo.getCopingStrategyRecord().equals(oldTblRiskInfo.getCopingStrategyRecord())) {
				logDetail += "缓解措施执行记录：&nbsp;&nbsp;“&nbsp;<b>"+oldTblRiskInfo.getCopingStrategyRecord()+"</b>&nbsp;”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“&nbsp;<b>"+tblRiskInfo.getCopingStrategyRecord()+"</b>&nbsp;”&nbsp;&nbsp;"+"；<br/>";
			}
			if(logDetail.equals("")) {
				logDetail +="未作任何修改";
			}
			//新增日志
			TblRiskLog log = new TblRiskLog(tblRiskInfo.getId(),"修改风险",logDetail,request);
			tblRiskLogMapper.insertLog(log);
		}
	}

	
	/**
	*@author liushan
	*@Description 项目群管理风险列表
	*@Date 2020/7/31
	 * @param programId 运维类项目
 * @param request
	*@return java.util.List<cn.pioneeruniverse.project.entity.TblRiskInfo>
	**/
	@Override
	@Transactional(readOnly=true)
	public List<TblRiskInfo> getRiskInfoByProgram(Long programId, HttpServletRequest request) {
		List<TblRiskInfo> list = tblRiskInfoMapper.getRiskInfoByProgram(programId);
		Long number = Long.valueOf(1);
		for (TblRiskInfo tblRiskInfo : list) {
			String riskPriorityName = getValue(tblRiskInfo.getRiskPriority(),"TBL_RISK_INFO_RISK_PRIORITY");  //优先级
			String statusName = getValue(tblRiskInfo.getRiskStatus(),"TBL_RISK_INFO_RISK_STATUS");   //状态
			tblRiskInfo.setRiskPriorityName(riskPriorityName);
			tblRiskInfo.setStatusName(statusName);
			tblRiskInfo.setNumber(number);
			number ++;
		}
		return list;
	}


	/**
	*@author liushan
	*@Description 获取开发任务名称
	*@Date 2020/7/31
	 * @param requirementFeatureId
	*@return java.lang.String
	**/
	@Override
	@DataSource(name="tmpDataSource")
	@Transactional(readOnly=true,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public String getRequirementFeatureName(Long requirementFeatureId){
		TblRequirementFeature feature = featureMapper.getReqFeatureById(requirementFeatureId);
		return feature != null ? feature.getFeatureName() :"";
	}

	/**
	*@author liushan
	*@Description 上传多个文件
	*@Date 2020/4/28
	*@Param []
	*@return void
	**/
	private void updateFiles(Long riskId,MultipartFile[] files,HttpServletRequest request) throws Exception{
		if (files != null && files.length > 0) {
			for (MultipartFile file : files) {
				if (!Objects.isNull(file) || !file.isEmpty()) {
					TblAttachementInfoDTO attachementInfoDTO = UploadFileUtil.updateMultipartFile(file,s3Util,riskBucket,request);
					attachementInfoDTO.setAssociatedId(riskId);
					riskAttachementMapper.insertAttachement(attachementInfoDTO);
				}
			}
		}
	}

}
