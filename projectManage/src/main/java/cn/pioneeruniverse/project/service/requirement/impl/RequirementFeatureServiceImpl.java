package cn.pioneeruniverse.project.service.requirement.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.databus.DataBusRequestHead;
import cn.pioneeruniverse.common.databus.DataBusUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.ReflectUtils;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.project.common.SynRequirementSystemUtil;
import cn.pioneeruniverse.project.dao.mybatis.RequirementFeatureHistoryMapper;
import cn.pioneeruniverse.project.dao.mybatis.RequirementFeatureMapper;
import cn.pioneeruniverse.project.dao.mybatis.RequirementFeatureTimeTraceMapper;
import cn.pioneeruniverse.project.dao.mybatis.RequirementMapper;
import cn.pioneeruniverse.project.dao.mybatis.SprintBurnoutMapper;
import cn.pioneeruniverse.project.dao.mybatis.SystemInfoMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblRequirementFeatureAttentionMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblRequirementFeatureLogMapper;
import cn.pioneeruniverse.project.entity.TblRequirementFeature;
import cn.pioneeruniverse.project.entity.TblRequirementFeatureHistory;
import cn.pioneeruniverse.project.entity.TblRequirementFeatureLog;
import cn.pioneeruniverse.project.entity.TblRequirementFeatureTimeTrace;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.entity.TblRequirementSystem;
import cn.pioneeruniverse.project.entity.TblSprintBurnout;
import cn.pioneeruniverse.project.entity.TblSystemInfo;
import cn.pioneeruniverse.project.feignInterface.DevTaskInterface;
import cn.pioneeruniverse.project.feignInterface.ProjectToSystemInterface;
import cn.pioneeruniverse.project.feignInterface.TestTaskInterface;
import cn.pioneeruniverse.project.service.requirement.RequirementFeatureService;
import cn.pioneeruniverse.project.vo.SynRequirementSystem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.StringUtil;

@Transactional(readOnly = true)
@Service("requirementFeatureService")
public class RequirementFeatureServiceImpl implements RequirementFeatureService {

    @Autowired
    private DevTaskInterface taskInterface;
    @Autowired
    private TestTaskInterface testInterface;
    @Autowired
    private RequirementMapper tblRequirementInfoMapper;
    @Autowired
    private RequirementFeatureMapper requirementFeatureMapper;
    @Autowired
    private RequirementFeatureHistoryMapper requirementFeatureHistoryMapper;
    @Autowired
    private RequirementFeatureTimeTraceMapper requirementFeatureTimeTraceMapper;
    @Autowired
    private SprintBurnoutMapper sprintBurnoutMapper;
    @Autowired
    private ProjectToSystemInterface projectToSystemInterface;
    @Autowired
    private TblRequirementFeatureAttentionMapper tblRequirementFeatureAttentionMapper;
    @Autowired
    private SystemInfoMapper systemInfoMapper;
	@Autowired
	private TblRequirementFeatureLogMapper requirementFeatureLogMapper;
	
	public final static Logger logger = LoggerFactory.getLogger(RequirementFeatureServiceImpl.class);

    private static String databusccName = "itcd_manager_syn";

    @Override
    public List<TblRequirementFeature> findFeatureByRequirementId(Long id) {
        return requirementFeatureMapper.findFeatureByRequirementId(id);
    }

    @Override
    public List<TblRequirementFeature> getAllFeature(String featureName) {
        return requirementFeatureMapper.getAllFeature(featureName);
    }

    /**
     * 
    * @Title: updateTaskDataItmp
    * @Description: ??????????????????????????????
    * @author author
    * @param feature
    * @param reqId ??????id
    * @throws
     */
    @Override
    @Transactional(readOnly = false)
    public void updateTaskDataItmp(TblRequirementFeature feature,String reqId) {
        feature.setRequirementId(Long.valueOf(reqId));
        //??????????????????
        TblRequirementInfo tblRequirementInfo = tblRequirementInfoMapper.findRequirementById1(Long.valueOf(reqId));
        //??????????????????????????????????????????????????????
        TblRequirementFeature feature1 = requirementFeatureMapper.getFeatureByCode(feature.getFeatureCode());
        //???????????????????????????????????????
        List<TblRequirementFeature> featureList = requirementFeatureMapper.
                selectFeatureBySystemIdAndRequirementId(feature.getSystemId(),feature.getRequirementId());
        //????????????????????????????????????????????????
        if (feature1==null&&(featureList==null||featureList.size()==0)) {
            if(tblRequirementInfo.getRequirementType()!=null) {
                feature.setDevelopmentDeptId(tblRequirementInfo.getDevelopmentDeptId());
                feature.setRequirementFeatureSource(Long.valueOf(1));
            }
            //???????????????
            if(!feature.getRequirementFeatureStatus().equals("00")) {
            	
                TblRequirementFeatureTimeTrace featureTimeTrace = new TblRequirementFeatureTimeTrace();
                //??????????????????
                requirementFeatureMapper.insertFeature(feature);
               //?????????????????????????????????
                featureTimeTrace.setRequirementFeatureId(feature.getId());
                featureTimeTrace.setRequirementFeatureCreateTime(feature.getCreateDate());
                featureTimeTrace.setStatus(1);
                featureTimeTrace.setCreateDate(new Timestamp(new Date().getTime()));
                featureTimeTrace.setLastUpdateDate(new Timestamp(new Date().getTime()));
                requirementFeatureTimeTraceMapper.insertFeatureTimeTrace(featureTimeTrace);
                // ??????????????????==>????????????????????????IT??????????????????
                TblRequirementFeatureLog log = new TblRequirementFeatureLog();
        		log.setRequirementFeatureId(feature.getId());
        		log.setLogType("????????????????????????IT??????????????????");
        		insertLog(log);
                //???????????????????????????????????????
                sendAddMessage(feature);
                //???????????????????????????????????????????????????????????????????????? --ztt
                if (feature.getManageUserId()!=null || feature.getExecuteUserId()!=null) {
                	Map<String,Object> emWeMap = new HashMap<String, Object>();
            		emWeMap.put("messageTitle", "???IT???????????????????????????- ????????????????????????????????????");
            		emWeMap.put("messageContent","?????????????????????????????????????????????"+ feature.getFeatureCode()+" | "+feature.getFeatureName()+"????????????????????????");
            		emWeMap.put("messageReceiver",feature.getManageUserId()+","+feature.getExecuteUserId()+"," );//????????? ??????????????????????????? ???????????????
            		emWeMap.put("sendMethod", 3);//???????????? 3 ???????????????
            		projectToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
				}
            }
        }else if(feature1==null&&featureList.size()>0) {//???CODE?????????????????????????????????????????????????????????????????????
        	//????????????????????????
            if(feature.getRequirementFeatureStatus().equals("00")) {
                updateStatusItmp(feature);
            }else{
            	// ??????????????????????????????,????????????????????????
            	TblRequirementFeature requirementFeature2 = new TblRequirementFeature();
            	requirementFeature2.setFeatureCode(featureList.get(0).getFeatureCode());
            	requirementFeature2.setTaskId(featureList.get(0).getTaskId());
            	
            	//???????????????????????????????????????????????????????????????????????????
                featureList.get(0).setFeatureCode(feature.getFeatureCode());
                featureList.get(0).setTaskId(feature.getTaskId());
                requirementFeatureMapper.updateTaskId(featureList.get(0));
                
                // ??????????????????==>????????????????????????IT??????????????????
        		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
        		log.setRequirementFeatureId(featureList.get(0).getId());
        		log.setLogType("????????????????????????IT??????????????????");
        		Map<String, String> map = new HashMap<>();
        		map.put("taskId", "taskId");
        		map.put("featureCode", "featureCode");
        		String detail = ReflectUtils.packageModifyContent(featureList.get(0), requirementFeature2,map);
        		log.setLogDetail(detail);
        		insertLog(log);
        		
                //??????
                if(isSyn(featureList.get(0).getRequirementFeatureStatus())) {
                    Map<String, Object> dataResult = pushDevData(featureList.get(0));
                    String taskId = "";
                    if(feature.getFeatureCode()!=null) {
                        taskId = feature.getFeatureCode();
                    }
                    // ??????databus
                    DataBusUtil.send(databusccName,taskId,JsonUtil.toJson(dataResult));
                }
            }
        }else if(feature1!=null){
            if(feature.getRequirementFeatureStatus().equals("00")) {
                updateStatusItmp(feature);
            }
        }

        /*else {
            if(feature.getRequirementFeatureStatus().equals("00")) {
                feature.setId(feature1.getId());
                requirementFeatureMapper.updateStatusById(feature);//?????????????????????????????????
                taskInterface.cancelStatusReqFeature(feature.getId());//?????????????????????????????????
            }else{
                feature.setId(feature1.getId());
                feature.setCreateBy(null);
                feature.setCreateDate(null);
                feature.setRequirementFeatureStatus(null);
                requirementFeatureMapper.updateFeatureById(feature);
                //????????????
                sendAddMessage(feature);
            }
            //??????????????? ???????????????????????????????????????????????????????????????????????????????????? --ztt
    		String userIds = tblRequirementFeatureAttentionMapper.getUserIdsByReqFeatureId(feature.getId());
    		if(StringUtils.isNotBlank(userIds)) {
    			Map<String,Object> emWeMap = new HashMap<String, Object>();
    			emWeMap.put("messageTitle", "???IT???????????????????????????- ???????????????????????????");
    			emWeMap.put("messageContent","???????????????"+ feature.getFeatureCode()+" | "+feature.getFeatureName()+"????????????????????????????????????");
    			emWeMap.put("messageReceiver",userIds );//????????? ?????????????????????
    			emWeMap.put("sendMethod", 3);//???????????? 3 ???????????????
    			projectToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
    		}
        }*/
    }


   /**
    * 
   * @Title: sendAddMessage
   * @Description: ????????????
   * @author author
   * @param tblRequirementFeature void
    */
    private void sendAddMessage( TblRequirementFeature tblRequirementFeature){

        Map<String,Object> map=new HashMap<>();
        map.put("messageTitle","????????????????????????????????????");
        map.put("messageContent",tblRequirementFeature.getFeatureCode()+"|"+tblRequirementFeature.getFeatureName());
        map.put("messageReceiverScope",2);
        String userId="";
        if(tblRequirementFeature.getExecuteUserId()!=null){
            userId=userId+tblRequirementFeature.getExecuteUserId()+",";
        }
        if(tblRequirementFeature.getManageUserId()!=null){
            userId=userId+tblRequirementFeature.getManageUserId()+",";
        }
        if(!userId.equals("")) {
            userId = userId.substring(0, userId.length() - 1);
            map.put("messageReceiver", userId);
            projectToSystemInterface.insertMessage(JSON.toJSONString(map));
        }




    }



    //?????????????????????tmp_db
    @Override
    @DataSource(name="tmpDataSource")
    @Transactional(readOnly = false)
    public void updateTaskDataTmp(TblRequirementFeature feature,String reqId) {
        feature.setRequirementId(Long.valueOf(reqId));
        //??????????????????
        TblRequirementInfo tblRequirementInfo = tblRequirementInfoMapper.findRequirementById1(Long.valueOf(reqId));
        if(tblRequirementInfo.getRequirementType()!=null) {
            feature.setRequirementFeatureSource(putFeatureSource(tblRequirementInfo.getRequirementType()));
            feature.setDevelopmentDeptId(tblRequirementInfo.getDevelopmentDeptId());
        }
        //??????????????????????????????????????????????????????
        TblRequirementFeature feature1 = requirementFeatureMapper.getFeatureByCode(feature.getFeatureCode());
        //???????????????????????????????????????
        List<TblRequirementFeature> featureList = requirementFeatureMapper.
                selectFeatureBySystemIdAndRequirementId(feature.getSystemId(),feature.getRequirementId());
        //TODO aviyy ???????????????????????????
        featureList = featureList.stream().filter(x -> 0 == x.getCreateType()).collect(Collectors.toList());
        if (feature1==null&&(featureList==null||featureList.size()==0)) {
            if(!feature.getRequirementFeatureStatus().equals("00")) {
//                tbl_project_system prosys ON prosys.SYSTEM_ID =tsystem.ID AND prosys.STATUS=1 AND prosys.RELATION_TYPE = 1
                if(feature.getRequirementChangeNumber() == null){
                    feature.setRequirementChangeNumber(0);
                }
                requirementFeatureMapper.insertFeature(feature);
                TblRequirementFeatureLog log = new TblRequirementFeatureLog();
                log.setRequirementFeatureId(feature.getId());
                log.setLogType("????????????????????????IT??????????????????");
                insertLog(log);
                sendAddMessageTmp(feature);
                //???????????????????????????????????????????????????????????????????????? --ztt
                if(feature.getSystemId()!=null) {
                	TblSystemInfo systemInfo = systemInfoMapper.selectById(feature.getSystemId());
                    if (systemInfo.getTaskMessageStatus() == 1 && (feature.getManageUserId()!=null || feature.getExecuteUserId()!=null)) {
                    	Map<String,Object> emWeMap = new HashMap<String, Object>();
                		emWeMap.put("messageTitle", "???IT???????????????????????????- ????????????????????????????????????");
                		emWeMap.put("messageContent","?????????????????????????????????????????????"+ feature.getFeatureCode()+" | "+feature.getFeatureName()+"????????????????????????");
                		emWeMap.put("messageReceiver",feature.getManageUserId()+","+feature.getExecuteUserId()+"," );//????????? ??????????????????????????? ???????????????
                		emWeMap.put("sendMethod", 3);//???????????? 3 ???????????????
                		projectToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
    				}
                }
            }
        }else if(feature1==null&&featureList.size()>0) {//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if(feature.getRequirementFeatureStatus().equals("00")) {//???????????????????????????
                updateStatusByTmp(feature);
            }else{
            	//???????????????????????????????????????ID
<<<<<<< .mine
                featureList.get(0).setFeatureCode(feature.getFeatureCode());//??????????????????
                //??????????????????????????????
||||||| .r5245
                featureList.get(0).setFeatureCode(feature.getFeatureCode());//??????????????????
                //??????????????????????????????
=======
                featureList.get(0).setFeatureCode(feature.getFeatureCode());//??????????????????
                // ??????????????????????????????,????????????????????????
                TblRequirementFeature requirementFeature2 = new TblRequirementFeature();
                requirementFeature2.setFeatureCode(featureList.get(0).getFeatureCode());
                requirementFeature2.setTaskId(featureList.get(0).getTaskId());
                //??????????????????????????????
>>>>>>> .r5303
                featureList.get(0).setTaskId(feature.getTaskId());
                featureList.get(0).setRequirementFeatureSource(feature.getRequirementFeatureSource());
                featureList.get(0).setCreateType(2l);//TODO aviyy ????????????????????????????????????
                if(featureList.get(0).getRequirementChangeNumber() == null){
                    featureList.get(0).setRequirementChangeNumber(0);
                }
                requirementFeatureMapper.updateTmpTaskId(featureList.get(0));
                // ??????????????????==>????????????????????????IT??????????????????
                TblRequirementFeatureLog log = new TblRequirementFeatureLog();
                log.setRequirementFeatureId(featureList.get(0).getId());
                log.setLogType("????????????????????????IT??????????????????");
                Map<String, String> map = new HashMap<>();
                map.put("taskId", "taskId");
                map.put("featureCode", "featureCode");
                String detail = ReflectUtils.packageModifyContent(featureList.get(0), requirementFeature2,map);
                log.setLogDetail(detail);
                insertLog(log);
                //??????
                if(isSyn(featureList.get(0).getRequirementFeatureStatus())) {
                    Map<String, Object> dataResult = pushTestData(featureList.get(0));
                    String taskId = "";
                    if(feature.getFeatureCode()!=null) {
                        taskId = feature.getFeatureCode();
                    }
                    // ??????databus
                    DataBusUtil.send(databusccName,taskId,JsonUtil.toJson(dataResult));
                }
            }
        }else if(feature1!=null){
            if(feature.getRequirementFeatureStatus().equals("00")) {
                updateStatusByTmp(feature);
            }
        }


        /*else {
            if(feature.getRequirementFeatureStatus().equals("00")) {
                feature.setId(feature1.getId());
                requirementFeatureMapper.updateStatusById(feature);
                testInterface.cancelStatusReqFeature(feature.getId());
            }else{
                feature.setId(feature1.getId());
                feature.setCreateBy(null);
                feature.setCreateDate(null);
                feature.setRequirementFeatureStatus(null);
                requirementFeatureMapper.updateFeatureById(feature);
            }
        }*/
    }
    /**
     *
     * @Title: sendAddMessage
     * @Description: ????????????
     * @author author
     * @param tblRequirementFeature void
     */
    private void sendAddMessageTmp( TblRequirementFeature tblRequirementFeature){

        Map<String,Object> map=new HashMap<>();
        map.put("messageTitle","????????????????????????????????????");
        map.put("messageContent",tblRequirementFeature.getFeatureCode()+"|"+tblRequirementFeature.getFeatureName());
        map.put("messageReceiverScope",2);
        String userId="";
        if(tblRequirementFeature.getExecuteUserId()!=null){
            userId=userId+tblRequirementFeature.getExecuteUserId()+",";
        }
        if(tblRequirementFeature.getManageUserId()!=null){
            userId=userId+tblRequirementFeature.getManageUserId()+",";
        }
        if(!userId.equals("")) {
            userId = userId.substring(0, userId.length() - 1);
            map.put("messageReceiver", userId);
            projectToSystemInterface.insertMessage(JSON.toJSONString(map));
        }




    }
    @Override
    @Transactional(readOnly = false)
    public void updateStatusItmp(TblRequirementFeature feature) {
        TblRequirementFeature feature1 = requirementFeatureMapper.getFeatureByCode(feature.getFeatureCode());
        if(feature1!=null){
            feature.setId(feature1.getId());
            requirementFeatureMapper.updateStatusById(feature);
            //??????????????? ?????????????????????????????????????????????????????????????????????????????? --ztt
    		String userIds = tblRequirementFeatureAttentionMapper.getUserIdsByReqFeatureId(feature1.getId());
    		if(StringUtils.isNotBlank(userIds)) {
    			Map<String,Object> emWeMap = new HashMap<String, Object>();
    			emWeMap.put("messageTitle", "???IT???????????????????????????- ???????????????????????????");
    			emWeMap.put("messageContent","???????????????"+ feature1.getFeatureCode()+" | "+feature1.getFeatureName()+"????????????????????????????????????");
    			emWeMap.put("messageReceiver",userIds );//????????? ?????????????????????
    			emWeMap.put("sendMethod", 3);//???????????? 3 ???????????????
    			projectToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
    		}
    		
    		 taskInterface.cancelStatusReqFeature(feature1.getId());
        }
    }

    /**
     * 
    * @Title: updateStatusByTmp
    * @Description: ???????????????
    * @author author
    * @param feature ??????
     */
    @Override
    @DataSource(name="tmpDataSource")
    @Transactional(readOnly = false)
    public void updateStatusByTmp(TblRequirementFeature feature) {
        TblRequirementFeature feature1 = requirementFeatureMapper.getFeatureByCode(feature.getFeatureCode());
        if(feature1!=null){
            feature.setId(feature1.getId());
            requirementFeatureMapper.updateStatusById(feature);
            TblRequirementFeatureLog log = new TblRequirementFeatureLog();
            log.setRequirementFeatureId(feature.getId());
            log.setLogType("????????????????????????IT??????????????????");
            insertLog(log);
            testInterface.cancelStatusReqFeature(feature1.getId());
        }
    }

    private Long putFeatureSource(String reqType){
        int testSource=0;
        if(reqType.toUpperCase(Locale.ENGLISH).equals("POLICYREGULATION")){
            testSource=1;
        }else if(reqType.toUpperCase(Locale.ENGLISH).equals("SUPPORTSALES")){
            testSource=4;
        }else if(reqType.toUpperCase(Locale.ENGLISH).equals("RISKCONTROL")){
            testSource=3;
        }else if(reqType.toUpperCase(Locale.ENGLISH).equals("MANAGEMENTOPTIMIZATION")){
            testSource=5;
        }else if(reqType.toUpperCase(Locale.ENGLISH).equals("OTHER")){
            testSource=7;
        }
        return Long.valueOf(testSource);
    }

    /**
     * 
    * @Title: executeFeatureToHistoryJob
    * @Description: ??????????????????
    * @author author
    * @param parameterJson ???jobmanage????????????job??????
     */
	@Override
    @Transactional(readOnly = false)
	public void executeFeatureToHistoryJob(String parameterJson) {
		Date date = new Date();
		String dateStr = DateUtil.formatDate(date);
		String selectDate = "";
		String startDate = "";
		String endDate = "";
		if (StringUtil.isNotEmpty(parameterJson)) {
			Map<String, String> parameterMap = JSON.parseObject(parameterJson, Map.class);
			selectDate = parameterMap.get("selectDate");
			startDate = parameterMap.get("startDate");
			endDate = parameterMap.get("endDate");
		}
		
		List<String> dateList = new ArrayList<String>();
		if (StringUtil.isNotEmpty(startDate) && StringUtil.isNotEmpty(endDate)) {
			Date start = DateUtil.parseDate(startDate);
			Date end = DateUtil.parseDate(endDate);
			while (start.compareTo(end) <= 0) {
				dateList.add(startDate);
				startDate = DateUtil.getNextDate(startDate);
				start = DateUtil.parseDate(startDate);
			}
		} else if (StringUtil.isNotEmpty(selectDate)) {
			dateList.add(selectDate);
		} else {
			dateList.add(dateStr);
		}
		
		for (String currentDate : dateList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("currentDate", currentDate);
			//?????????tbl_requirement_feature????????? where??????????????????????????????????????????????????????status=1 ??????????????????tbl_sprint_info?????????????????????
			List<TblRequirementFeature> featureList = requirementFeatureMapper.getFeatureToHistoryList(map);

           // ??????????????????????????????tbl_sprint_info ??????????????????????????????id ???????????????????????????

            String sprintIds = requirementFeatureMapper.getSprintIdsByDate(map);
            Map<String, Double> testWork = new HashMap<String, Double>();
            for(String sId:sprintIds.split(",")){
                String reqFeatureIds=  requirementFeatureMapper.getFeaBySprintId(sId);
                if(reqFeatureIds==null){
                    testWork.put(sId,new Double(0));
                }else{
                    Map<String,Object> result=testInterface.getWorkLoadByFeIds(reqFeatureIds);
                    Double estimateRemainWorkload=new Double(0);
                    if (result != null && result.get("estimateRemainWorkload") != null) {
                        estimateRemainWorkload = (Double) result.get("estimateRemainWorkload");
                    }
                    testWork.put(sId,estimateRemainWorkload);
                }



            }
            //String featureIds=requirementFeatureMapper.getFeatureIdsBysprintId(map);
			
			List<TblRequirementFeatureHistory> newFeatureHistoryList = new ArrayList<TblRequirementFeatureHistory>();
			for (TblRequirementFeature feature : featureList) {
				TblRequirementFeatureHistory newHistory = new TblRequirementFeatureHistory();
				newHistory.setRequirementFeatureId(feature.getId());
				newHistory.setAssetSystemTreeId(feature.getAssetSystemTreeId());
				newHistory.setEstimateRemainWorkload(feature.getEstimateRemainWorkload());
				newHistory.setCommissioningWindowId(feature.getCommissioningWindowId());
				newHistory.setSprintId(feature.getSprintId());
				newHistory.setProjectPlanId(feature.getProjectPlanId());
				newHistory.setRequirementFeatureStatus(feature.getRequirementFeatureStatus());
				newHistory.setStatus(1);
				newHistory.setCreateDate(date);
				newFeatureHistoryList.add(newHistory);
			}
			
			//???????????????
			EntityWrapper<TblRequirementFeatureHistory> featureHistoryWrapper = new EntityWrapper<TblRequirementFeatureHistory>();
			featureHistoryWrapper.eq("CREATE_DATE", currentDate);
			requirementFeatureHistoryMapper.delete(featureHistoryWrapper);
			for (TblRequirementFeatureHistory newHistory : newFeatureHistoryList) {
				requirementFeatureHistoryMapper.insert(newHistory);
			}


			//??????????????????????????????
			List<TblRequirementFeatureHistory> workloadList = requirementFeatureHistoryMapper.getFeatureHistoryWorkloadList(map);
			List<TblSprintBurnout> newSprintBurnoutList = new ArrayList<TblSprintBurnout>();
			for (TblRequirementFeatureHistory workload : workloadList) {
				TblSprintBurnout newSprintBurnout = new TblSprintBurnout();
				newSprintBurnout.setSprintId(workload.getSprintId());
                Double testWork1=  testWork.get(workload.getSprintId().toString());
        if (testWork1 == null) {
          newSprintBurnout.setEstimateRemainWorkload(
              new BigDecimal(Double.toString(new Double(0)))
                  .add(new BigDecimal(workload.getEstimateRemainWorkload()))
                  .doubleValue());
          newSprintBurnout.setTestEstimateRemainWorkload(new Double(0));
        } else {
          // newSprintBurnout.setEstimateRemainWorkload(workload.getEstimateRemainWorkload());
          newSprintBurnout.setEstimateRemainWorkload(
              new BigDecimal(Double.toString(testWork1))
                  .add(new BigDecimal(workload.getEstimateRemainWorkload()))
                  .doubleValue());
          newSprintBurnout.setTestEstimateRemainWorkload(testWork1);
        }

        newSprintBurnout.setDevEstimateRemainWorkload(workload.getEstimateRemainWorkload());
        newSprintBurnout.setCreateDate(date);
        newSprintBurnoutList.add(newSprintBurnout);
      }

			//?????????????????????????????????????????????
			EntityWrapper<TblSprintBurnout> sprintBurnoutWrapper = new EntityWrapper<TblSprintBurnout>();
			sprintBurnoutWrapper.eq("CREATE_DATE", currentDate);
			sprintBurnoutMapper.delete(sprintBurnoutWrapper);
			for (TblSprintBurnout newSprintBurnout : newSprintBurnoutList) {
				sprintBurnoutMapper.insert(newSprintBurnout);
			}
		}


	}

	/**
	 * 
	* @Title: addItmpFeatureInfo
	* @Description: ??????????????????
	* @author author
	* @param reqSystemList ?????????????????????
	* @param reqId ??????id
	 */
    @Override
    @DataSource(name = "itmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addItmpFeatureInfo(String reqSystemList,String reqId) {
        List<SynRequirementSystem> resultList = JSONObject.parseArray(reqSystemList, SynRequirementSystem.class);

        for (SynRequirementSystem synReqSystem : resultList) {
            TblRequirementSystem trs = SynRequirementSystemUtil.SynTblRequirementSystem(synReqSystem);
            List<TblRequirementFeature> featureList = requirementFeatureMapper.
                    selectFeatureBySystemIdAndRequirementId(trs.getSystemId(),Long.valueOf(reqId));
            TblRequirementInfo tblRequirementInfo = tblRequirementInfoMapper.findRequirementById1(Long.valueOf(reqId));
            if(featureList==null||featureList.size()==0&&trs.getSystemId()!=null){
                TblRequirementFeature feature = new TblRequirementFeature();
                if(tblRequirementInfo.getRequirementType()!=null) {
                    feature.setDevelopmentDeptId(tblRequirementInfo.getDevelopmentDeptId());
                    feature.setRequirementFeatureSource(Long.valueOf(1));
                }
                feature.setFeatureCode(taskInterface.getNewFeatureCode());
                feature.setFeatureName(tblRequirementInfo.getRequirementName());
                feature.setFeatureOverview(tblRequirementInfo.getRequirementOverview());
                feature.setRequirementFeatureStatus("01");
                feature.setSystemId(trs.getSystemId());
                feature.setRequirementId(Long.valueOf(reqId));
                feature.setCreateType(Long.valueOf(1));
                feature.setManageUserId(trs.getDevManageUserId());
                feature.setStatus(1);
                feature.setCreateBy(Long.valueOf(1));
                feature.setLastUpdateBy(Long.valueOf(1));
                feature.setCreateDate(new Timestamp(new Date().getTime()));
                feature.setLastUpdateDate(new Timestamp(new Date().getTime()));
                requirementFeatureMapper.insertFeature1(feature);
                
                // ??????????????????==>??????????????????(??????????????????????????????)
                TblRequirementFeatureLog log = new TblRequirementFeatureLog();
        		log.setRequirementFeatureId(feature.getId());
        		log.setLogType("??????????????????(??????????????????????????????)");
        		insertLog(log);
            }
        }
        SpringContextHolder.getBean(RequirementFeatureService.class).addTmpFeatureInfo(reqSystemList,reqId);
    }

    /**
	 * 
	* @Title: addItmpFeatureInfo
	* @Description: ???????????????????????????
	* @author author
	* @param reqSystemList ?????????????????????
	* @param reqId ??????id
	 */
    @Override
    @DataSource(name = "tmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void addTmpFeatureInfo(String reqSystemList,String reqId) {
        List<SynRequirementSystem> resultList = JSONObject.parseArray(reqSystemList, SynRequirementSystem.class);
        for (SynRequirementSystem synReqSystem : resultList) {
            TblRequirementSystem trs = SynRequirementSystemUtil.SynTblRequirementSystem(synReqSystem);
            List<TblRequirementFeature> featureList = requirementFeatureMapper.
                    selectFeatureBySystemIdAndRequirementId(trs.getSystemId(),Long.valueOf(reqId));
            TblRequirementInfo tblRequirementInfo = tblRequirementInfoMapper.findRequirementById1(Long.valueOf(reqId));
            if(featureList==null||featureList.size()==0&&trs.getSystemId()!=null){
                TblRequirementFeature feature = new TblRequirementFeature();
                if(tblRequirementInfo.getRequirementType()!=null) {
                    feature.setDevelopmentDeptId(tblRequirementInfo.getDevelopmentDeptId());
                }
                feature.setFeatureCode(testInterface.getNewFeatureCode());
                feature.setFeatureName(tblRequirementInfo.getRequirementName());
                feature.setFeatureOverview(tblRequirementInfo.getRequirementOverview());
                feature.setRequirementFeatureStatus("01");
                feature.setSystemId(trs.getSystemId());
                feature.setRequirementId(Long.valueOf(reqId));
                feature.setCreateType(Long.valueOf(0));// aviyy 20201103 ????????????????????????
                feature.setManageUserId(trs.getTestManageUserId());
                feature.setRequirementChangeNumber(0);
                feature.setStatus(1);
                feature.setCreateBy(Long.valueOf(1));
                feature.setLastUpdateBy(Long.valueOf(1));
                feature.setCreateDate(new Timestamp(new Date().getTime()));
                feature.setLastUpdateDate(new Timestamp(new Date().getTime()));
                requirementFeatureMapper.insertFeature1(feature);
            }
        }
    }

    private static Boolean isSyn(String status){
        if((Integer.valueOf(status)>4 && Integer.valueOf(status)<9 )|| "03".equals(status)) {
            return true;
        }else{
            return false;
        }
    }

    //??????????????????????????????IT?????????
    private Map<String, Object> pushDevData(TblRequirementFeature requirementFeature) {
        Map<String, Object> mapAll = new LinkedHashMap<>();
        Map<String, Object> mapBody = new HashMap<>();
        mapBody.put("tbltaskId", requirementFeature.getFeatureCode());
        mapBody.put("taskResult","????????????");
        mapBody.put("taskWorkload",requirementFeature.getActualWorkload());
        mapAll.put("requestHead",DataBusRequestHead.getRequestHead());
        mapAll.put("requestBody",mapBody);

        return mapAll;
    }

    //????????????????????????IT?????????
    private Map<String, Object> pushTestData(TblRequirementFeature requirementFeature) {
        Map<String, Object> mapAll = new LinkedHashMap<>();
        Map<String, Object> mapBody = new HashMap<>();
        mapBody.put("tbltaskId", requirementFeature.getFeatureCode());
        mapBody.put("taskResult", "????????????");

        Double actualSitWorkload = requirementFeatureMapper.getWorkload(requirementFeature.getId());
        Double actualPptWorkload = requirementFeatureMapper.getWorkload2(requirementFeature.getId());
        if (actualSitWorkload != null && actualSitWorkload > 0) {
            mapBody.put("taskWorkload", actualSitWorkload);
        } else if (actualPptWorkload != null && actualPptWorkload > 0) {
            mapBody.put("taskWorkload", actualPptWorkload);
        } else {
            mapBody.put("taskWorkload", 0.0);
        }
        mapAll.put("requestHead", DataBusRequestHead.getRequestHead());
        mapAll.put("requestBody", mapBody);
        return mapAll;
    }
    
    /**
     * 
     * @Title: insertLog
     * @Description: ??????????????????????????????????????????IT???????????????????????????????????????????????????????????????
     * @param log
     * @author wangwei
     * @date 2020???8???21???
     */
    @Transactional(readOnly = false)
	public void insertLog(TblRequirementFeatureLog log) {
    	try {
			log.setCreateDate(new Timestamp(new Date().getTime()));
			log.setUserId(Long.valueOf(1));
			log.setUserAccount("");
			log.setUserName("??????????????????");
			requirementFeatureLogMapper.insert(log);
		} catch (Exception e) {
			logger.error("????????????????????????:" + e.getMessage(), e);
			e.printStackTrace();
		}
	}
    
}


