package cn.pioneeruniverse.dev.service.worktask.impl;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.bean.PropertyInfo;
import cn.pioneeruniverse.common.bean.ReflectFieledType;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.constants.DicConstants;
import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.CommonUtils;
import cn.pioneeruniverse.common.utils.JiraUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.PageWithBootStrap;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.common.utils.UploadFileUtil;
import cn.pioneeruniverse.dev.dao.mybatis.AllDevRequirementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskAttentionMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskLogAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskLogMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblProjectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureLogMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.entity.DevDetailVo;
import cn.pioneeruniverse.dev.entity.ExtendedField;
import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblDefectRemark;
import cn.pioneeruniverse.dev.entity.TblDevTask;
import cn.pioneeruniverse.dev.entity.TblDevTaskAttachement;
import cn.pioneeruniverse.dev.entity.TblDevTaskAttention;
import cn.pioneeruniverse.dev.entity.TblDevTaskLog;
import cn.pioneeruniverse.dev.entity.TblDevTaskLogAttachement;
import cn.pioneeruniverse.dev.entity.TblProjectInfo;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureLog;
import cn.pioneeruniverse.dev.entity.TblRequirementInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import cn.pioneeruniverse.dev.feignInterface.DevManageToTestManageInterface;
import cn.pioneeruniverse.dev.service.defect.test.DefectService;
import cn.pioneeruniverse.dev.service.devtask.DevTaskService;
import cn.pioneeruniverse.dev.service.worktask.WorkTaskService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;

@Service("workTaskService")
public class WorkTaskServiceImpl implements WorkTaskService {
    public final static Logger logger = LoggerFactory.getLogger(WorkTaskServiceImpl.class);
    
    //????????????
    public static final String SOURCE = "TBL_REQUIREMENT_INFO_REQUIREMENT_SOURCE";
    //????????????
    public static final String TYPE = "TBL_REQUIREMENT_INFO_REQUIREMENT_TYPE";
    //???????????????
    public static final String PRIORITY = "TBL_REQUIREMENT_INFO_REQUIREMENT_PRIORITY";
    //????????????
    public static final String PLAN = "TBL_REQUIREMENT_INFO_REQUIREMENT_PLAN";
    //????????????
    public static final String STATUS = "TBL_REQUIREMENT_INFO_REQUIREMENT_STATUS";
    //????????????????????????
    public static final String DEVSTAUS = "TBL_DEV_TASK_DEV_TASK_STATUS";
    //????????????
    public static final String RETYPE = "TBL_REQUIREMENT_INFO_REQUIREMENT_TYPE";
    //???????????????????????????????????????
    private final List<String> listType = ReflectFieledType.listType;
    @Autowired
    private TblDevTaskMapper devTaskMapper;
    @Autowired
    private TblRequirementFeatureMapper tblRequirementFeatureMapper;
    @Autowired
    private TblRequirementInfoMapper requirementInfoMapper;
    @Autowired
    private TblSystemInfoMapper systemInfoMapper;
    @Autowired
    private AllDevRequirementMapper allDevRequirementMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TblDevTaskLogMapper tblDevTaskLogMapper;
    @Autowired
    private TblDevTaskLogAttachementMapper tblDevTaskLogAttachementMapper;
    @Autowired
    private TblDevTaskAttachementMapper tblDevTaskAttachementMapper;
    @Autowired
    private TblDefectInfoMapper tblDefectInfoMapper;
    @Autowired
    private TblProjectInfoMapper projectInfoMapper;
    @Autowired
    private DevTaskService devTaskService;
    @Autowired
    private DefectService defectService;
    @Autowired
    private TblDefectInfoMapper defectInfoMapper;
    @Autowired
    private TblRequirementFeatureLogMapper requirementFeatureLogMapper;
    @Autowired
    private DevManageToSystemInterface devManageToSystemInterface;
    @Autowired
    private DevManageToTestManageInterface devManageToTestManageInterface;
    @Autowired
    private TblDevTaskAttentionMapper tblDevTaskAttentionMapper;
    @Autowired
    private S3Util s3Util;
    @Autowired
    private JiraUtil jiraUtil;

    /**
     * 
    * @Title: getDevTask
    * @Description: ????????????????????????????????????
    * @author author
    * @param tblDevTask ?????????????????????
    * @param projectIds ???????????????
    * @param page ?????????
    * @param rows ????????????
    * @param request
    * @return map key-rows:???????????????????????????
    *                 records:?????????
    *                 total: ?????????
    *                 page:?????????
    *                 rwos????????????????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public Map getDevTask(TblDevTask tblDevTask, Long[] projectIds, Integer page, Integer rows,HttpServletRequest request) {
        List<TblDevTask> list = new ArrayList();
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> parm = new HashMap<String, Object>();
        List<Integer> Array = new ArrayList();
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
           /* if (tblDevTask.getDevTaskStatus() != null) {
                String devStatus = tblDevTask.getDevTaskStatus().toString();
                if (devStatus.length() == 1) {
                    Array.add(Integer.parseInt(devStatus));
                    tblDevTask.setDevStatus(Array);
                } else {
                    for (int i = 0; i < devStatus.length(); i++) {
                        String status = devStatus.substring(i, i + 1);
                        Array.add(Integer.parseInt(status));
                    }
                    tblDevTask.setDevStatus(Array);
                }
            }*/
            parm.put("task", tblDevTask);
            // parm.put("devUserID", tblDevTask.getDevUserId().toString());
            parm.put("projectIds", projectIds);
    		Boolean flag = new CommonUtils().currentUserWithAdmin(request);
            if (flag) {//?????????
                list = devTaskMapper.getDevTaskAll(parm);
            } else {//????????????
                list = devTaskMapper.getDevTask(parm);
            }
            PageInfo<TblDevTask> pageInfo = new PageInfo<TblDevTask>(list);
            result.put("rows", list);
            result.put("records", pageInfo.getTotal());
            result.put("total", pageInfo.getPages());
            result.put("page", page < pageInfo.getPages() ? page : pageInfo.getPages());
            return result;
        } else {
            result.put("rwos", devTaskMapper.getDevTask(parm));
        }

        return result;
    }


    /**
     * 
    * @Title: getExcelAllWork
    * @Description: excel????????????????????????????????????
    * @author author
    * @param tblDevTask
    * @param projectIds
    * @param request
    * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblDevTask> getExcelAllWork(TblDevTask tblDevTask, Long[] projectIds,HttpServletRequest request) {
        List<TblDevTask> list = new ArrayList();
        try{
        Map<String, Object> parm = new HashMap<String, Object>();
        parm.put("task", tblDevTask);
        parm.put("projectIds", projectIds);
        Boolean flag = new CommonUtils().currentUserWithAdmin(request);
        if (flag) {//?????????  ????????????
            list = devTaskMapper.getDevTaskAll(parm);
        } else {//????????????
            list = devTaskMapper.getDevTask(parm);
        }


        List<TblDataDic> dataDics = getDataFromRedis("CODE_REVIEW_STATU");


        Object object = redisUtils.get(DEVSTAUS);
        Map<String, Object> mapsource = new HashMap<String, Object>();
        if (object != null && !"".equals(object)) {//redis????????????redis??????
            mapsource = JSON.parseObject(object.toString());
        }
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getFieldJson()!=null) {
                    JSONObject jsonObject = JSONObject.parseObject((list.get(i).getFieldJson()));
                    String listTxt = jsonObject.getString("field");
                    list.get(i).setExtendedFields(JSONArray.parseArray(listTxt, ExtendedField.class));
                }
                for (String key : mapsource.keySet()) {
                    if (key.equals(list.get(i).getDevTaskStatus().toString())) {
                        list.get(i).setWorkTaskStatus(mapsource.get(key).toString());
                    }
                }
                if(list.get(i).getNeedCodeReview()!=null) {
                    for (TblDataDic dataDic : dataDics) {
                        if (StringUtils.isNotBlank(dataDic.getValueCode())  && dataDic.getValueCode().equals(list.get(i).getNeedCodeReview().toString())) {
                            list.get(i).setCodeReviewStatusName(dataDic.getValueName());
                        }

                    }
                }


            }
        }

        }catch (Exception e){
            e.printStackTrace();

        }
        return list;
    }
    
    /**
     * 
    * @Title: getDataFromRedis
    * @Description: ????????????????????????????????????
    * @author author
    * @param termCode
    * @return List<TblDataDic>
     */
    public List<TblDataDic> getDataFromRedis(String termCode) {
        String result = redisUtils.get(termCode).toString();
        List<TblDataDic> dics = new ArrayList<>();
        if (!StringUtils.isBlank(result)) {
            Map<String, Object> maps = (Map<String, Object>) JSON.parse(result);
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                TblDataDic dic = new TblDataDic();
                dic.setValueCode(entry.getKey());
                dic.setValueName(entry.getValue().toString());
                dics.add(dic);
            }
        }
        return dics;
    }

    /**
     * 
    * @Title: getAllDevUser
    * @Description: ??????????????????????????????
    * @author author
    * @param id ??????ID
    * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblDevTask> getAllDevUser(Long id) {
        List<TblDevTask> list = devTaskMapper.getAllDevUser(id);
        return list;
    }

    /**
     * 
    * @Title: getAllRequirt
    * @Description: ??????????????????
    * @author author
    * @param RequirementInfo ?????????????????????
    * @param page
    * @param rows
    * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Map getAllRequirt(TblRequirementInfo RequirementInfo, Integer page, Integer rows) {
        List<TblRequirementInfo> list = new ArrayList();
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> Array = new ArrayList();
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
            if (RequirementInfo.getRequirementType() != null) {
                String RequirementType = RequirementInfo.getRequirementType();

                Array = Arrays.asList(RequirementType.split(","));
                RequirementInfo.setRequirementTypeList(Array);
            }
            list = requirementInfoMapper.getRequirement(RequirementInfo);

            Object object = redisUtils.get(RETYPE);
            Map<String, Object> mapsource = new HashMap<String, Object>();
            if (object != null && !"".equals(object)) {//redis????????????redis??????
                mapsource = JSON.parseObject(object.toString());
            }
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    for (String key : mapsource.keySet()) {
                        if (key.equals(list.get(i).getRequirementType())) {
                            list.get(i).setRequirementType(mapsource.get(key).toString());
                        }
                    }
                }
            }


            PageInfo<TblRequirementInfo> pageInfo = new PageInfo<TblRequirementInfo>(list);
            result.put("rows", list);
            result.put("records", pageInfo.getTotal());
            result.put("total", pageInfo.getPages());
            result.put("page", page < pageInfo.getPages() ? page : pageInfo.getPages());
            return result;
        } else {
            result.put("rows", requirementInfoMapper.getRequirement(RequirementInfo));
        }
        return result;
    }

    /**
     * ????????????????????????????????????
     *
     * @param request
     * @return
     */
    @Override
    public List<TblProjectInfo> findProjectByUser(HttpServletRequest request) {
        return projectInfoMapper.findProjectByUser(CommonUtil.getCurrentUserId(request));
    }

    @Override
    @Transactional(readOnly = true)
    public TblDevTask getDevTaskByCode(String code) {
        return devTaskMapper.getDevTaskByCode(code);
    }

    @Override
    @Transactional(readOnly = false)
    public void addworkTaskAttachement(TblAttachementInfoDTO attachementInfoDTO) throws Exception {
        tblDevTaskAttachementMapper.addworkTaskAttachement(attachementInfoDTO);
    }

    /**
     * shan-??????id????????????????????????
     *
     * @param id
     * @return
     */
    @Override
    public TblDevTask getDevTaskById(Long id) {
        return devTaskMapper.getDevTaskById(id);
    }

    /**
     * 
    * @Title: getAllsystem
    * @Description: ????????????????????????
    * @author author
    * @param systemInfo ?????????????????????
    * @param page ?????????
    * @param rows ????????????
    * @return
    * @throws
     */
    @Override
    @Transactional(readOnly = true)
    public Map getAllsystem(TblSystemInfo systemInfo, Integer page, Integer rows) {
        List<TblSystemInfo> list = new ArrayList<>();
        Map<String, Object> result = new HashMap<String, Object>();
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);

            list = systemInfoMapper.getAllsystem(systemInfo);
            PageInfo<TblSystemInfo> pageInfo = new PageInfo<TblSystemInfo>(list);
            result.put("rows", list);
            result.put("records", pageInfo.getTotal());
            result.put("total", pageInfo.getPages());
            result.put("page", page < pageInfo.getPages() ? page : pageInfo.getPages());
            Object object = redisUtils.get("TBL_SYSTEM_INFO_SYSTEM_TYPE");
            Map<String, Object> mapsource = new HashMap<String, Object>();
            if (object != null && !"".equals(object)) {//redis????????????redis??????
                mapsource = JSON.parseObject(object.toString());
            }
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    for (String key : mapsource.keySet()) {
                        if (key.equals(list.get(i).getSystemType())) {
                            list.get(i).setSystemType(mapsource.get(key).toString());
                        }
                    }
                }
            }
            return result;
        } else {
            result.put("rows", systemInfoMapper.getAllsystem(systemInfo));
        }


        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TblDevTask> getUserName() {
        List<TblDevTask> list = devTaskMapper.getUseName();
        return list;
    }

    /**
     * 
    * @Title: getEditDevTask
    * @Description:??????????????????????????????
    * @author author
    * @param id
    * @return Map<String, Object>
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getEditDevTask(String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        Long devid = Long.parseLong(id);
        List<TblDevTask> list = devTaskMapper.getEditDevTask(devid);
        TblDefectInfo tblDefectInfo = new TblDefectInfo();
        Integer systemCodeReviewStatus=null;
        for (int i = 0; i < list.size(); i++) {
            tblDefectInfo = defectInfoMapper.selectByPrimaryKey(list.get(i).getDefectID());
            if(list.get(i).getSystemId()!=null) {
            	systemCodeReviewStatus= devTaskMapper.selectSystemCodeReview(list.get(i).getSystemId());
            }
            
        }
        result.put("systemCodeReviewStatus", systemCodeReviewStatus);
        result.put("defect", tblDefectInfo);
        result.put("dev", list);
        return result;
    }

    /**
     * 
    * @Title: updateDevTask
    * @Description:????????????????????????
    * @author author
    * @param obj ????????????
    * @param attachFiles ??????
    * @param deleteAttaches ?????????????????????
    * @param Userid
    * @param request
     */
    @Transactional(readOnly = false)
    @Override
    public void updateDevTask(String obj, String attachFiles, String deleteAttaches, Long Userid, HttpServletRequest request) {
       //??????????????????
    	TblDevTask devTask = JSON.parseObject(obj, TblDevTask.class);

        devTask.setLastUpdateBy(Userid);
        Date date = new Date();
        try {
            TblDevTask OlddevTask = devTaskMapper.getDevOld(devTask.getId());//?????????
            
            //????????????????????????????????????????????????
            if (devTask.getDefectID() != null && devTask.getDefectID() > 0) {
                TblDefectInfo tblDefectInfo = new TblDefectInfo();
                tblDefectInfo.setId(devTask.getDefectID());
                tblDefectInfo.setAssignUserId(devTask.getDevUserId());
                defectService.updateDefect(tblDefectInfo, request);
            }
            Long Devid = devTask.getId();//??????id
            Long DevUser = devTask.getDevUserId();//?????????ID
            TblDevTask DevTask = devTaskMapper.getDevUser(Devid);
            if (DevTask != null) {
                Long oldUser = DevTask.getDevUserId();
                if (oldUser != DevUser) {
                    String userAccount = OlddevTask.getUserAccount();
                }

            }
            if(devTask.getDevTaskStatus()==1 || devTask.getDevTaskStatus()==2) { //??????????????????????????????????????? ????????????????????????????????????
            	/*Integer Status = devTask.getRequirementFeatureStatus();
                if (Status != null && (Status.longValue() == 1 || Status.longValue() == 3)) {*/
                	Long featureId = devTask.getRequirementFeatureId();
                	TblRequirementFeature requirementFeature = tblRequirementFeatureMapper.selectByPrimaryKey(featureId);
                	
                	if(!"02".equals( requirementFeature.getRequirementFeatureStatus())) {//??????????????????????????????????????????????????????
                		//devTaskMapper.editFeature(featureId);
                    	devTaskService.updateStatus(featureId, request);
                        
                      //????????????
                		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
                		log.setRequirementFeatureId(featureId);
                		log.setLogType("??????????????????");
                		
                		String beforeName = "";
                		String afterName = "";
                		
                		beforeName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", requirementFeature.getRequirementFeatureStatus(),"");
                		afterName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", "02","");
                				
                		log.setLogDetail("???????????????"+"&nbsp;&nbsp;???<b>"+beforeName+"</b>???&nbsp;&nbsp;"+"?????????"+"&nbsp;&nbsp;???<b>"+afterName+"</b>???&nbsp;&nbsp;") ;
                		log.setCreateBy(CommonUtil.getCurrentUserId(request));
                		log.setCreateDate(new Timestamp(new Date().getTime()));
                		log.setUserId(CommonUtil.getCurrentUserId(request));
                		log.setUserAccount(CommonUtil.getCurrentUserAccount(request));
                		log.setUserName(CommonUtil.getCurrentUserName(request));
                		requirementFeatureLogMapper.insert(log);
                	}
                //}
            }
            

            Timestamp time = new Timestamp(date.getTime());
            devTask.setLastUpdateDate(time);
            //devTaskMapper.updateDevTask(devTask);
            devTaskMapper.updateDevTaskNew(devTask);
            logAlls(devTask.getId().toString(), attachFiles, deleteAttaches, OlddevTask, "??????????????????", new HashMap<>(), request);
          
            //??????????????? ?????????????????????????????????????????????????????????????????????????????????????????? --ztt
			sendDevTaskMassage(OlddevTask);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????????????? ?????????????????????????????????????????????????????????????????????????????????????????? 
     * --ztt
     * */
	public void sendDevTaskMassage(TblDevTask devTask) {
		String userIds2 = tblDevTaskAttentionMapper.getUserIdsByDevTaskId(devTask.getId());
		if(StringUtils.isNotBlank(userIds2)) {
			Map<String,Object> emWeMap = new HashMap<String, Object>();
			emWeMap.put("messageTitle", "???IT???????????????????????????- ???????????????????????????");
			emWeMap.put("messageContent","???????????????"+ devTask.getDevTaskCode()+" | "+devTask.getDevTaskName()+"????????????????????????????????????");
			emWeMap.put("messageReceiver",userIds2 );//????????? ?????????????????????
			emWeMap.put("sendMethod", 3);//???????????? 3 ???????????????
			devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
		}
	}
	
	/**
	 * ???????????????????????????????????????
	 */
	public void sendWorkTaskMassage(TblDevTask devTask) {
		try {
			if(devTask!=null&&devTask.getDevUserId()!=null&&devTask.getDevUserId()!=0) {
				Map<String,Object> emWeMap = new HashMap<String, Object>();
				emWeMap.put("messageTitle", "???IT???????????????????????????- ??????????????????????????????????????????");
				emWeMap.put("messageContent","???????????????????????????????????????????????????"+ devTask.getDevTaskCode()+" | "+ devTask.getDevTaskName()+"????????????????????????");
				emWeMap.put("messageReceiver",devTask.getDevUserId());//????????????????????? ???????????????
				emWeMap.put("sendMethod", 3);//???????????? 3 ???????????????
				devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
			}
		} catch (Exception e) {
			logger.error("???????????????????????????????????????"+e.getMessage(),e);
			e.printStackTrace();
		}
	}

    /**
     * 
    * @Title: addDevTask
    * @Description: ????????????????????????
    * @author author
    * @param obj ????????????
    * @param attachFiles ??????
    * @param Userid  ?????????
    * @param request
    * @param UserAccount ???????????????
    * @throws Exception
    * @throws
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void addDevTask(String obj, String attachFiles, Long Userid, HttpServletRequest request, String UserAccount) throws Exception {
        TblDevTask devTask = JSON.parseObject(obj, TblDevTask.class);
        devTask.setCreateBy(Userid);
        devTask.setDevTaskCode(getDevCode());
        Date date = new Date();
        Timestamp time2 = new Timestamp(date.getTime());
        devTask.setCreateDate(time2);
        Integer Status = devTask.getRequirementFeatureStatus();
        Long featureId = devTask.getRequirementFeatureId();
        //????????????????????????????????????????????????????????????????????????????????????
        if (Status != null && Status.longValue() == 1 || Status.longValue() == 3) {
        	//devTaskMapper.editFeature(featureId);
    		TblRequirementFeature requirementFeature = tblRequirementFeatureMapper.selectByPrimaryKey(featureId);

        	devTaskService.updateStatus(featureId, request);
        	//????????????
    		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
    		log.setRequirementFeatureId(featureId);
    		log.setLogType("??????????????????");
    		
    		String beforeName = "";
    		String afterName = "";
    		
    		beforeName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", requirementFeature.getRequirementFeatureStatus(),"");
    		afterName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", "02","");
    				
    		log.setLogDetail("???????????????"+"&nbsp;&nbsp;???<b>"+beforeName+"</b>???&nbsp;&nbsp;"+"?????????"+"&nbsp;&nbsp;???<b>"+afterName+"</b>???&nbsp;&nbsp;") ;
    		log.setCreateBy(CommonUtil.getCurrentUserId(request));
    		log.setCreateDate(new Timestamp(new Date().getTime()));
    		log.setUserId(CommonUtil.getCurrentUserId(request));
    		log.setUserAccount(CommonUtil.getCurrentUserAccount(request));
    		log.setUserName(CommonUtil.getCurrentUserName(request));
    		requirementFeatureLogMapper.insert(log);
            
        }
        devTaskMapper.addDevTask(devTask);
        
        //?????????????????????????????????
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("requirementFeatureId", featureId);
        jsonMap.put("devTaskCreateTime", new Timestamp(new Date().getTime()));
        String json = JsonUtil.toJson(jsonMap);
        devTaskService.updateReqFeatureTimeTrace(json);

        //????????????
        Long testId = devTask.getId();
        TblDevTaskLog tblDevTaskLog = new TblDevTaskLog();
        tblDevTaskLog.setDevTaskId(testId);
        tblDevTaskLog.setLogType("??????????????????");
        Long logId = this.insertDevTaskLog(tblDevTaskLog, request);
        
        //??????????????????
        if (!attachFiles.equals("") && !attachFiles.equals(null)) {
            List<TblDevTaskLogAttachement> files = JsonUtil.fromJson(attachFiles, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskLogAttachement.class));
            for (int i = 0; i < files.size(); i++) {
                files.get(i).setDevTaskLogId(logId);
                files.get(i).setCreateBy(Userid);
            }
            tblDevTaskLogAttachementMapper.addLogAttachement(files);
        }
        
        //????????????????????????
        if (!attachFiles.equals("") && !attachFiles.equals(null) && !attachFiles.equals("[]")) {
            List<TblDevTaskAttachement> fileTask = JsonUtil.fromJson(attachFiles, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskAttachement.class));
            for (int i = 0; i < fileTask.size(); i++) {
                fileTask.get(i).setDevTaskId(testId);
                fileTask.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
            }
            tblDevTaskAttachementMapper.addAttachement(fileTask);
        }
        
        //????????????????????????????????????????????????????????????????????????????????????
        if (devTask.getDefectID() != null) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("devTaskId", devTask.getId());
            result.put("reqFid", devTask.getRequirementFeatureId());
            result.put("defectId", devTask.getDefectID());
            tblDefectInfoMapper.updateDevDefect(result);

            // ????????????
            TblDefectInfo defectInfo = new TblDefectInfo();
            defectInfo.setId(devTask.getDefectID());
            defectInfo.setDefectStatus(4);
            defectService.updateDefectwithTBC(defectInfo, new TblDefectRemark(), null, request);
        }

        sendAddMessage(obj,devTask.getDevTaskCode(),featureId);
        
        sendWorkTaskMassage(devTask);// ????????????

    }

    //???????????????????????????
    private void sendAddMessage(String objStr,String devCode,Long featureId){
        //???featureId???????????????systemId
        TblDevTask tblDevTask = tblDefectInfoMapper.selectRequirementFeatureById(featureId);
        TblDevTask devTask = JSON.parseObject(objStr, TblDevTask.class);
        Map<String,Object> map=new HashMap<>();
        map.put("messageTitle","??????????????????????????????????????????");
        map.put("messageContent",devCode+"|"+devTask.getDevTaskName());
        map.put("messageReceiverScope",2);//????????????????????????
        //???????????? 2-- ??????????????????
        map.put("messageSource",2);
        if (tblDevTask != null){
            map.put("projectId",tblDevTask.getProjectId());
        }
        map.put("messageReceiver",devTask.getDevUserId());
        devManageToSystemInterface.insertMessage(JSON.toJSONString(map));

    }


    /**
     * 
    * @Title: getSeeDetail
    * @Description: ??????????????????
    * @author author
    * @param id
    * @return map key-devTaskType:????????????????????????
    *                 dev ?????????????????????
    * @throws
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSeeDetail(String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        DevDetailVo dev = new DevDetailVo();
        Long devID = Long.parseLong(id);
        //?????????????????????
        dev = allDevRequirementMapper.AlldevReuirement(devID);
        dev.setDevuserName(allDevRequirementMapper.getdevName(dev.getDevuserID()));
        dev.setCreateName(allDevRequirementMapper.getdevName(dev.getCreateBy()));
        dev.setManageUserName(allDevRequirementMapper.getdevName(dev.getManageUserId()));
        dev.setSystemName(allDevRequirementMapper.getSystemName(dev.getSystemId()));
        dev.setExecuteUserName(allDevRequirementMapper.getdevName(dev.getExecuteUserId()));
        dev.setApplyUserName(allDevRequirementMapper.getdevName(dev.getApplyUserId()));
        dev.setApplyDeptName(allDevRequirementMapper.getdeptName(dev.getApplyDeptId()));
        
        //??????????????????????????????
        Object source = redisUtils.get(SOURCE);//????????????
        Object type = redisUtils.get(TYPE);
        Object priority = redisUtils.get(PRIORITY);
        Object plan = redisUtils.get(PLAN);
        Object status = redisUtils.get(STATUS);//??????
        Object devStatus = redisUtils.get(DEVSTAUS);
        Object requiretStatus = redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS");
        Map<String, Object> mapsource = JSON.parseObject(source.toString());
        Map<String, Object> mapdevStatus = JSON.parseObject(devStatus.toString());
        Map<String, Object> maptype = JSON.parseObject(type.toString());
        Map<String, Object> mappriority = JSON.parseObject(priority.toString());
        Map<String, Object> mapplan = JSON.parseObject(plan.toString());
        Map<String, Object> mapstatus = JSON.parseObject(status.toString());
        Map<String, Object> mapRequiretStatus = JSON.parseObject(requiretStatus.toString());
        for (String key : mapsource.keySet()) {
            String reqiureSorce = dev.getRequirementSource();
            if (reqiureSorce != null) {
                if (key.equals(reqiureSorce.toUpperCase(Locale.ENGLISH))) {
                    dev.setRequirementSource(mapsource.get(key).toString());
                }
            }
        }
        for (String key : mapRequiretStatus.keySet()) {
            if (dev.getRequirementFeatureStatus() != null) {
                if (key.equals(dev.getRequirementFeatureStatus().toUpperCase(Locale.ENGLISH))) {
                    dev.setRequirementFeatureStatus(mapRequiretStatus.get(key).toString());
                }
            }

        }
        for (String key : mapdevStatus.keySet()) {
            if (key.equals(dev.getDevTaskStatus().toString().toUpperCase(Locale.ENGLISH))) {
                dev.setDevStatus(mapdevStatus.get(key).toString());
            }
        }
        for (String key : maptype.keySet()) {
            if (dev.getRequirementType() != null) {
                if (key.equals(dev.getRequirementType().toUpperCase(Locale.ENGLISH))) {
                    dev.setRequirementType(maptype.get(key).toString());
                }
            }

        }
        for (String key : mappriority.keySet()) {
            if (dev.getRequirementPriority() != null) {
                if (key.equals(dev.getRequirementPriority().toUpperCase(Locale.ENGLISH))) {
                    dev.setRequirementPriority(mappriority.get(key).toString());
                }
            }

        }
        for (String key : mapplan.keySet()) {
            if (dev.getRequirementPanl() != null) {
                if (key.equals(dev.getRequirementPanl().toUpperCase(Locale.ENGLISH))) {
                    dev.setRequirementPanl(mapplan.get(key).toString());
                }
            }

        }
        for (String key : mapstatus.keySet()) {
            if (dev.getRequirementStatus() != null) {
                if (key.equals(dev.getRequirementStatus().toUpperCase(Locale.ENGLISH))) {
                    dev.setRequirementStatus(mapstatus.get(key).toString());
                }
            }

        }
        try {
           result.put("devTaskType",CommonUtil.getDic(DicConstants.DEV_TASK_DEV_TASK_TYPE).get(dev.getDevTaskType().toString()));
        }catch (Exception e){
            result.put("devTaskType","");
        }
        result.put("dev", dev);
        return result;

    }
    
    //????????????
    @Override
	public List<TblDevTaskAttention> getAttentionList(TblDevTaskAttention attention) {
		List<TblDevTaskAttention> attentionList = tblDevTaskAttentionMapper.selectList(new EntityWrapper<TblDevTaskAttention>(attention));
		return attentionList;
	}
	
	//????????????????????????
	@Override
	@Transactional(readOnly = false)
	public void changeAttention(Long id, Integer attentionStatus, HttpServletRequest request) {
		Long userId = CommonUtil.getCurrentUserId(request);
		Timestamp stamp = new Timestamp(new Date().getTime());
		TblDevTaskAttention attention = new TblDevTaskAttention();
		attention.setDevTaskId(id);
		attention.setUserId(userId);
		TblDevTaskAttention attentionOld = tblDevTaskAttentionMapper.selectOne(attention);
		if (attentionStatus == 1) { //??????
			if (attentionOld == null) {
				CommonUtil.setBaseValue(attention, request);
				tblDevTaskAttentionMapper.insert(attention);
			} else if (attentionOld.getStatus() == 2) {
				attentionOld.setStatus(1);
				attentionOld.setLastUpdateBy(userId);
				attentionOld.setLastUpdateDate(stamp);
				tblDevTaskAttentionMapper.updateByPrimaryKey(attentionOld);
			}
		} else {
			if (attentionOld != null) {
				attentionOld.setStatus(2);
				attentionOld.setLastUpdateBy(userId);
				attentionOld.setLastUpdateDate(stamp);
				tblDevTaskAttentionMapper.updateByPrimaryKey(attentionOld);
			}
		}
	}

    //??????
    @Override
    @Transactional(readOnly = false)
    public void Handle(TblDevTask devTask, String HattachFiles, String deleteAttaches, HttpServletRequest request) {
        TblDevTask oldDevTask = devTaskMapper.getDevOld(devTask.getId());
        devTaskMapper.Handle(devTask);
        logAlls(devTask.getId().toString(), HattachFiles, deleteAttaches, oldDevTask, "??????????????????", new HashMap<>(), request);
        sendDevTaskMassage(oldDevTask);
    }

    /**
     * 
    * @Title: CodeHandle
    * @Description: ??????????????????
    * @author author
    * @param handle
    * @param HattachFiles
    * @param deleteAttaches
    * @param request
    * @throws
     */
    @Override
    @Transactional(readOnly = false)
    public void CodeHandle(String handle, String HattachFiles, String deleteAttaches, HttpServletRequest request) {
        TblDevTask devTask = new TblDevTask();
        devTask = JSON.parseObject(handle, TblDevTask.class);
        TblDevTask oldDevTask = devTaskMapper.getDevOld(devTask.getId());
        devTaskMapper.Handle(devTask);
        //?????????????????????????????????????????????
        devTaskMapper.CodeHandle(devTask);
        logAlls(devTask.getId().toString(), HattachFiles, deleteAttaches, oldDevTask, "??????????????????", new HashMap<>(), request);
        sendDevTaskMassage(oldDevTask);
    }

    /**
     * 
    * @Title: assigDev
    * @Description: ??????????????????
    * @author author
    * @param assig ?????????
    * @param Remark ????????????
    * @param request
    * @throws
     */
    @Override
    @Transactional(readOnly = false)
    public void assigDev(String assig, String Remark, HttpServletRequest request) {
        TblDevTask devTask = new TblDevTask();
        devTask = JSON.parseObject(assig, TblDevTask.class);
        Long Devid = devTask.getId();//??????id
        Long DevUser = devTask.getDevUserId();//?????????ID
        TblDevTask oldDevTask = devTaskMapper.getDevOld(Devid);
        Map<String, Object> remarkMap = new HashMap<>();

        if (devTask.getDefectID() != null && devTask.getDefectID() > 0) {
            TblDefectInfo tblDefectInfo = new TblDefectInfo();
            tblDefectInfo.setId(devTask.getDefectID());
            tblDefectInfo.setAssignUserId(devTask.getDevUserId());
            try {
                defectService.updateDefect(tblDefectInfo, request);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (oldDevTask != null) {
            Long OleUser = oldDevTask.getDevUserId();
            if (OleUser == null) {

                if (!Remark.equals("") && Remark != null) {
                    remarkMap.put("remark", Remark);
                }
                devTaskMapper.assigDev(devTask);
                logAll(Devid.toString(), "", oldDevTask, "??????????????????", remarkMap, request);
                sendDevTaskMassage(oldDevTask);

            } else if (OleUser != DevUser) {
                devTaskMapper.assigDev(devTask);
                if (!Remark.equals("") && Remark != null) {
                    remarkMap.put("remark", Remark);
                }
                logAll(Devid.toString(), "", oldDevTask, "??????????????????", remarkMap, request);
                String UserAccount = oldDevTask.getUserAccount();
                sendDevTaskMassage(oldDevTask);
            }

        }
    }


    /**
     * 
    * @Title: getMyTaskListForTaskPlugin
    * @Description: ????????????????????????
    * @author author
    * @param userId
    * @return
    * @throws
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMyTaskListForTaskPlugin(Long userId) {
        return devTaskMapper.getTaskListByDevUserId(userId);
    }

    /**
     * 
    * @Title: getTaskDetailForTaskPlugin
    * @Description: ??????????????????????????????
    * @author author
    * @param taskId
    * @return
    * @throws
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTaskDetailForTaskPlugin(Long taskId) {
        return devTaskMapper.getTaskDetailById(taskId);
    }

    /**
     * 
    * @Title: finishTaskForTaskPlugin
    * @Description: ??????????????????
    * @author author
    * @param tblDevTask
    * @return
    * @throws
     */
    @Override
    @Transactional(readOnly = false)
    public int finishTaskForTaskPlugin(TblDevTask tblDevTask) {
        tblDevTask.setDevTaskStatus(3);
        return devTaskMapper.finishTask(tblDevTask);
    }


    /**
     * 
    * @Title: DHandle
    * @Description: ?????????---->?????????
    * @author author
    * @param handle
    * @param DHattachFiles
    * @param deleteAttaches
    * @param request
    * @throws
     */
    @Override
    @Transactional(readOnly = false)
    public void DHandle(String handle, String DHattachFiles, String deleteAttaches, HttpServletRequest request) {
        TblDevTask devTask = new TblDevTask();
        devTask = JSON.parseObject(handle, TblDevTask.class);
        TblDevTask oldDevTask = devTaskMapper.getDevOld(devTask.getId());
        devTaskMapper.DHandle(devTask);
        logAlls(devTask.getId().toString(), DHattachFiles, deleteAttaches, oldDevTask, "??????????????????", new HashMap<>(), request);
        sendDevTaskMassage(oldDevTask);
    }

    //???????????????????????????
    @Override
    @Transactional(readOnly = false)
    public void reviewAdopt(String id, String adoptFiles, String deleteAttaches, HttpServletRequest request) {
        TblDevTask oldDevTask = devTaskMapper.getDevOld(Long.parseLong(id));

        devTaskMapper.reviewAdopt(id);
        logAlls(id, adoptFiles, deleteAttaches, oldDevTask, "??????????????????", new HashMap<>(), request);
        sendDevTaskMassage(oldDevTask);
    }

    //???????????????
    @Override
    @Transactional(readOnly = false)
    public void reviewNAdopt(String id, String adoptFiles, String deleteAttaches, HttpServletRequest request) {

        TblDevTask oldDevTask = devTaskMapper.getDevOld(Long.parseLong(id));
        devTaskMapper.reviewNAdopt(id);
        logAlls(id, adoptFiles, deleteAttaches, oldDevTask, "??????????????????", new HashMap<>(), request);
        sendDevTaskMassage(oldDevTask);
    }

    @Override
    public String DevfindMaxCode(int length) {
        return devTaskMapper.DevfindMaxCode(length);
    }

    /**
     * 
    * @Title: getDevCode
    * @Description: ??????????????????????????????
    * @author author
    * @return
    * @throws
     */
    public String getDevCode() {
        String featureCode = "";
        int codeInt = 0;
        Object object = redisUtils.get("TBL_DEV_TASK_DEV_TASK_CODE");
        if (object != null && !"".equals(object)) {//redis????????????redis??????
            String code = object.toString();
            if (!StringUtils.isBlank(code)) {
                codeInt = Integer.parseInt(code) + 1;
            }
        } else {//redis????????????????????????????????????????????????
            int length = Constants.ITMP_WORK_TASK_CODE.length() + 1;
            String cod = DevfindMaxCode(length);
            if (!StringUtils.isBlank(cod)) {
                codeInt = Integer.parseInt(cod) + 1;
            } else {
                codeInt = 1;
            }

        }
        DecimalFormat mat = new DecimalFormat("00000000");
        String codeString = mat.format(codeInt);

        featureCode = Constants.ITMP_WORK_TASK_CODE + codeString;
        redisUtils.set("TBL_DEV_TASK_DEV_TASK_CODE", codeString);
        return featureCode;
    }

    //??????????????????
    @Override
    @Transactional(readOnly = true)
    public Map devStatus() {
        Object object = redisUtils.get(DEVSTAUS);
        Map<String, Object> mapsource = new HashMap<String, Object>();
        if (object != null && !"".equals(object)) {//redis????????????redis??????
            mapsource = JSON.parseObject(object.toString());
        }
        return mapsource;
    }

    //????????????
    @Override
    @Transactional(readOnly = true)
    public Map ReqStatus() {
        Object object = redisUtils.get(RETYPE);
        Map<String, Object> mapsource = new HashMap<String, Object>();
        if (object != null && !"".equals(object)) {//redis????????????redis??????
            mapsource = JSON.parseObject(object.toString());
        }
        return mapsource;
    }

   //????????????
    @Override
    @Transactional(readOnly = true)
    public Map ReqSystem() {
        Object object = redisUtils.get("TBL_SYSTEM_INFO_SYSTEM_TYPE");
        Map<String, Object> mapsource = new HashMap<String, Object>();
        if (object != null && !"".equals(object)) {//redis????????????redis??????
            mapsource = JSON.parseObject(object.toString());
        }
        return mapsource;
    }

    //??????????????????
    @Override
    @Transactional(readOnly = true)
    public Map FeatureStatus() {
        Object object = redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS");
        Map<String, Object> mapsource = new HashMap<String, Object>();
        if (object != null && !"".equals(object)) {//redis????????????redis??????
            mapsource = JSON.parseObject(object.toString());
        }
        return mapsource;
    }
    
    //??????????????????
    @Override
    public Long insertDevTaskLog(TblDevTaskLog devTaskLog, HttpServletRequest request) {
    	devTaskLog = this.setDevTaskLog(devTaskLog, request);
    	devTaskLog.setCreateBy(CommonUtil.getCurrentUserId(request));
    	devTaskLog.setCreateDate(new Timestamp(new Date().getTime()));
        tblDevTaskLogMapper.insertDefectLog(devTaskLog);
        return devTaskLog.getId();
    }

    /**
     * 
    * @Title: findFieldByDevId
    * @Description: ??????????????????????????????
    * @author author
    * @param id
    * @return
    * @throws
     */
    @Override
    public List<ExtendedField> findFieldByDevId(Long id) {

            Map<String,Object> map=devManageToSystemInterface.findFieldByTableName("tbl_dev_task_itmpdb");
            String listTxt = JSONArray.toJSONString(map.get("field"));
            List<ExtendedField> extendedFields = JSONArray.parseArray(listTxt, ExtendedField.class);
            if(id!=null) {
                if (extendedFields != null) {
                    Iterator<ExtendedField> it = extendedFields.iterator();
                    while (it.hasNext()) {
                        ExtendedField extendedField = it.next();
                        if (extendedField.getStatus().equals("2")) {
                            it.remove();
                        } else {
                            String fieldName=extendedField.getFieldName();
                            String valueName = devTaskMapper.getDevTaskFieldTemplateById(id, fieldName);
                            extendedField.setValueName(valueName == null ? "" : valueName);
                        }
                    }
                }
            }

            if(extendedFields==null){
                extendedFields=new ArrayList<>();
            }
            return extendedFields;
        }

    /**
     *  ??????id????????????????????????
     * @param id
     * @return
     */
    @Override
    public TblRequirementFeature selectRequirementFeatureById(Long id) {
        return devTaskMapper.selectRequirementFeatureById(id);
    }

   //????????????????????????
    private TblDevTaskLog setDevTaskLog(TblDevTaskLog defectLog, HttpServletRequest request) {
        defectLog.setUserId(CommonUtil.getCurrentUserId(request));
        defectLog.setUserName(CommonUtil.getCurrentUserName(request));
        defectLog.setUserAccount(CommonUtil.getCurrentUserAccount(request));
        return defectLog;
    }

    /**
     * ??????????????????
     *
     * @param id
     * @param adoptFiles
     * @param oldDevTask
     * @param request
     */
    @Override
    public void logAll(String id, String adoptFiles, Object oldDevTask, String LogType, Map<String, Object> remarkMap, HttpServletRequest request) {
        TblDevTaskLog tblDevTaskLog = new TblDevTaskLog();
        tblDevTaskLog.setDevTaskId(Long.parseLong(id));
        tblDevTaskLog.setLogType(LogType);
        Long logId = this.insertDevTaskLog(tblDevTaskLog, request);
        TblDevTask newDevTask = devTaskMapper.getDevOld(Long.parseLong(id));
        if (!adoptFiles.equals("") && !adoptFiles.equals(null) && !adoptFiles.equals("[]")) {
            List<TblDevTaskLogAttachement> files = JsonUtil.fromJson(adoptFiles, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskLogAttachement.class));
            for (int i = 0; i < files.size(); i++) {
                files.get(i).setDevTaskLogId(logId);
                files.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
                files.get(i).setStatus(1);
            }
            tblDevTaskLogAttachementMapper.addLogAttachement(files);
        }

        if (!adoptFiles.equals("") && !adoptFiles.equals(null) && !adoptFiles.equals("[]")) {
            List<TblDevTaskAttachement> fileTask = JsonUtil.fromJson(adoptFiles, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskAttachement.class));
            for (int i = 0; i < fileTask.size(); i++) {
                fileTask.get(i).setDevTaskId(Long.parseLong(id));
                fileTask.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
            }
            tblDevTaskAttachementMapper.addAttachement(fileTask);
        }
        try {
            updateFieledsReflect(oldDevTask, newDevTask, logId, remarkMap);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    //???????????????????????????????????????????????????
    public void logAlls(String id, String adoptFiles, String deleteAttaches, Object oldDevTask, String LogType, Map<String, Object> remarkMap, HttpServletRequest request) {
        TblDevTaskLog tblDevTaskLog = new TblDevTaskLog();
        tblDevTaskLog.setDevTaskId(Long.parseLong(id));
        tblDevTaskLog.setLogType(LogType);
        Long logId = this.insertDevTaskLog(tblDevTaskLog, request);
        TblDevTask newDevTask = devTaskMapper.getDevOld(Long.parseLong(id));
        if (!adoptFiles.equals("") && !adoptFiles.equals(null) && !adoptFiles.equals("[]")) {
            List<TblDevTaskLogAttachement> files = JsonUtil.fromJson(adoptFiles, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskLogAttachement.class));
            for (int i = 0; i < files.size(); i++) {
                files.get(i).setDevTaskLogId(logId);
                files.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
                files.get(i).setStatus(1);
            }
            tblDevTaskLogAttachementMapper.addLogAttachement(files);
        }

        if (!adoptFiles.equals("") && !adoptFiles.equals(null) && !adoptFiles.equals("[]")) {
            List<TblDevTaskAttachement> fileTask = JsonUtil.fromJson(adoptFiles, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskAttachement.class));
            for (int i = 0; i < fileTask.size(); i++) {
                fileTask.get(i).setDevTaskId(Long.parseLong(id));
                fileTask.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
            }
            tblDevTaskAttachementMapper.addAttachement(fileTask);
        }

        // ????????????????????????
        if (!deleteAttaches.equals("") && !deleteAttaches.equals(null) && !deleteAttaches.equals("[]")) {
            List<TblDevTaskLogAttachement> deleteFileTask = JsonUtil.fromJson(deleteAttaches, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskLogAttachement.class));
            for (int i = 0; i < deleteFileTask.size(); i++) {
                deleteFileTask.get(i).setDevTaskLogId(logId);
                deleteFileTask.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
                deleteFileTask.get(i).setStatus(2);//????????????
            }
            tblDevTaskLogAttachementMapper.addLogAttachement(deleteFileTask);
        }
        try {
            updateFieledsReflect(oldDevTask, newDevTask, logId, remarkMap);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    /**
     * ?????????????????? ????????????
     * ????????????
     *
     * @param oldObject
     * @param newObject
     * @param logId
     */
    public void updateFieledsReflect(Object oldObject, Object newObject, Long logId, Map<String, Object> remarkMap) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
       /* Class<?> oldDemo = null;
        Class<?> newDemo = null;*/
        List<Map<String, Object>> list = new ArrayList<>();
        if (remarkMap!=null && !remarkMap.isEmpty()) {
            list.add(remarkMap);
        }
        Class<?> oldDemo = Class.forName(oldObject.getClass().getName());
        Class<?> newDemo = Class.forName(newObject.getClass().getName());

        Field[] oldFields = oldDemo.getDeclaredFields();
        // if(!oldObject.equals("Infinity")) {
        for (Field oldField : oldFields) {

            oldField.setAccessible(true);
            String oldDclareName = oldField.getName();
            Object oldDeclareValue = oldField.get(oldObject);
            PropertyInfo propertyInfo = oldField.getAnnotation(PropertyInfo.class);

            Field newField = newDemo.getDeclaredField(oldDclareName);
            newField.setAccessible(true);
            Object newDeclareValue = newField.get(newObject);

            // ??????????????????
            if (oldDclareName.equals("requirementFeatureId") && oldDeclareValue != null) {
                oldDeclareValue = devTaskMapper.getFeatureNameById((Long) oldDeclareValue);
            }
            if (oldDclareName.equals("requirementFeatureId") && newDeclareValue != null) {
                newDeclareValue = devTaskMapper.getFeatureNameById((Long) newDeclareValue);
            }
            /*if (oldDclareName.equals("devTaskPriority") && oldDeclareValue != null) {
            	
            }*/
            if (oldDclareName.equals("devTaskPriority") ) {
                if(newDeclareValue==null&&oldDeclareValue!=null) {
                	newDeclareValue="";
                }
            }
            Object object = redisUtils.get(DEVSTAUS);
            Map<String, Object> mapsource = new HashMap<>();
            if (object != null && !"".equals(object)) {//redis????????????redis??????
                mapsource = JSON.parseObject(object.toString());
            }
            // ????????????
            if (oldDclareName.equals("devTaskStatus") && oldDeclareValue != null) {
                for (String key : mapsource.keySet()) {
                    if (key.equals(oldDeclareValue.toString())) {
                        oldDeclareValue = mapsource.get(key).toString();
                    }
                }
            }
            if (oldDclareName.equals("devTaskStatus") && newDeclareValue != null) {

                for (String key : mapsource.keySet()) {
                    if (key.equals(newDeclareValue.toString())) {
                        newDeclareValue = mapsource.get(key).toString();
                    }
                }

            }


            if (oldDclareName.equals("commissioningWindowId") && newDeclareValue != null) {
                continue;
            }
            if (oldDclareName.equals("devUserId") && oldDeclareValue != null) {
                //oldDeclareValue=systemInfoMapper.getUserName(oldDeclareValue.toString());
                continue;

            }
            /*if (oldDclareName.equals("devUserId") && newDeclareValue != null) {
                newDeclareValue=systemInfoMapper.getUserName(newDeclareValue.toString());
            }*/
            if (oldDclareName.equals("featureName") && newDeclareValue != null) {
                continue;
            }
            if (oldDclareName.equals("requirementFeatureStatus") && newDeclareValue != null){
            	continue;
            }
            if (oldDclareName.equals("createName") && newDeclareValue != null) {
                continue;
            }
            if (oldDclareName.equals("defectID") && newDeclareValue != null) {
                continue;
            }
            if (oldDclareName.equals("userAccount") && newDeclareValue != null) {
                continue;
            }

            String[] fieldTypeName = newField.getType().getName().split("\\.");
            if (!listType.contains(fieldTypeName[fieldTypeName.length - 1])) {
                updateFieledsReflect(oldDeclareValue, newDeclareValue, logId, remarkMap);
                continue;
            }

            if (oldDeclareValue == null || newDeclareValue == null) {
                if (oldDeclareValue == null && newDeclareValue == null) {
                    continue;
                } else {
                    if (oldDeclareValue == null) {
                        if (oldField.getType().equals(java.util.Date.class)) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            newDeclareValue = simpleDateFormat.format(newDeclareValue);
                        }
                        // ??????????????????
                        Map<String, Object> newMap = new HashMap<>();
                        newMap.put("fieldName", propertyInfo.name());
                        newMap.put("oldValue", "");
                        newMap.put("newValue", newDeclareValue.toString());
                        list.add(newMap);
                    } else {
                        // ??????????????????
                        if (oldField.getType().equals(java.sql.Timestamp.class)) {
                            continue;
                        } else {
                            if (oldField.getType().equals(java.util.Date.class)) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                oldDeclareValue = simpleDateFormat.format(oldDeclareValue);

                            }
                               /*String name= propertyInfo.name();
                                Map<String,Object> delMap = new HashMap<>();
	                            delMap.put("fieldName",name);
	                            delMap.put("oldValue",oldDeclareValue.toString());
	                            delMap.put("newValue","?????????");
	                            list.add(delMap);*/
                        }

                    }
                    continue;
                }
            }
            // ??????????????????
            if (!oldDeclareValue.toString().equals(newDeclareValue.toString())) {
                if (oldField.getType().equals(java.sql.Timestamp.class)) {
                    continue;
                } else {
                    if (oldField.getType().equals(java.util.Date.class)) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        oldDeclareValue = simpleDateFormat.format(oldDeclareValue);
                        newDeclareValue = simpleDateFormat.format(newDeclareValue);
                    }
                    String name = propertyInfo.name();
                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("fieldName", name);
                    updateMap.put("oldValue", oldDeclareValue.toString());
                    updateMap.put("newValue", newDeclareValue.toString());
                    list.add(updateMap);
                }
            }

            // }
        }
        Gson gson = new Gson();
        String logDetail = gson.toJson(list);
        TblDevTaskLog log = new TblDevTaskLog();
        log.setLogDetail(logDetail);
        log.setId(logId);
        // ??????????????????
        tblDevTaskLogMapper.updateLogById(log);
    }

    @Override
    public void addDefectDevTask(String obj, String attachFiles, Long Userid, HttpServletRequest request,
                                 String UserAccount) {

    }

    
    /**
     * 
    * @Title: getAllFeature
    * @Description: ????????????????????????
    * @author author
    * @param tblDevTask ??????????????????
    * @param userdId ????????????
    * @param roleCodes ????????????
    * @param pageNumber ?????????
    * @param pageSize ????????????
    * @return
    * @throws
     */
    @Override
    public List<Map<String, Object>> getAllFeature(TblDevTask tblDevTask, Long userdId, List<String> roleCodes,Integer pageNumber, Integer pageSize) {
        if(tblDevTask.getRequirementCode()!=null && !"".equals(tblDevTask.getRequirementCode())){
            TblRequirementInfo req = requirementInfoMapper.findRequirementByCode(tblDevTask.getRequirementCode());
            if(req != null){
                tblDevTask.setRequirementId(req.getId());
            }
        }
        Map<String, Object> map = new HashMap<>();
        map = PageWithBootStrap.setPageNumberSize(map, pageNumber, pageSize);
        map.put("tblDevTask", tblDevTask);
        map.put("userId", userdId);
        if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//?????????????????????????????????????????????
        	return tblRequirementFeatureMapper.getAllRequirementFeature(map);
        }else {
        	return tblRequirementFeatureMapper.getAllRequirementFeatureCondition(map);
        }
        
    }


    @Override
    public List<Map<String, Object>> countWorkloadBysystemId(Long systemId) {
        return devTaskMapper.countWorkloadBysystemId(systemId);
    }

    /**
     * 
    * @Title: addDefectDevTask1
    * @Description: 
    * @author author
    * @param defect
    * @param workTask
    * @param attachFiles
    * @param request
    * @throws Exception
    * @throws
     */
    @Override
    @DataSource(name = "itmpDataSource")
    @Transactional(readOnly=false,rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
    public void addDefectDevTask1(String defect,String workTask, String attachFiles, HttpServletRequest request) throws Exception {
        //??????
        TblDefectInfo defectInfo = JSON.parseObject(defect,TblDefectInfo.class);
        List<TblDevTask> tblDevTasks = JSON.parseArray(workTask,TblDevTask.class);

        //????????????????????????
        TblRequirementFeature tblRequirementFeature = new TblRequirementFeature();
        tblRequirementFeature.setFeatureCode(getFeatureCode());
        tblRequirementFeature.setFeatureName(defectInfo.getDefectSummary());
        tblRequirementFeature.setFeatureOverview(defectInfo.getDefectOverview());
        tblRequirementFeature.setSystemId(defectInfo.getSystemId());
        TblProjectInfo projectInfo = projectInfoMapper.getProjectBySystemId(defectInfo.getSystemId());
        if(projectInfo!=null){
            tblRequirementFeature.setProjectId(projectInfo.getId());
        }
        tblRequirementFeature.setQuestionNumber(defectInfo.getDefectCode());
        tblRequirementFeature.setCreateType(1);
        tblRequirementFeature.setRequirementFeatureSource(2);
        tblRequirementFeature.setRequirementFeatureStatus("01");
        tblRequirementFeature.setCheckStatus(defectInfo.getCheckStatus());
        CommonUtil.setBaseValue(tblRequirementFeature,request);
        tblRequirementFeatureMapper.insertReqFeature(tblRequirementFeature);

        //????????????????????????
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("requirementFeatureId", tblRequirementFeature.getId());
        jsonMap.put("devTaskCreateTime", new Timestamp(new Date().getTime()));
        String json = JsonUtil.toJson(jsonMap);
        devTaskService.updateReqFeatureTimeTrace(json);
        Long userId = CommonUtil.getCurrentUserId(request);

        //????????????????????????
        for(TblDevTask tblDevTask : tblDevTasks){
            CommonUtil.setBaseValue(tblDevTask,request);
            tblDevTask.setRequirementFeatureId(tblRequirementFeature.getId());
            tblDevTask.setDevTaskCode(getDevCode());
            devTaskMapper.addDevTask(tblDevTask);

            Long testId = tblDevTask.getId();
            TblDevTaskLog tblDevTaskLog = new TblDevTaskLog();
            tblDevTaskLog.setDevTaskId(testId);
            tblDevTaskLog.setLogType("??????????????????");
            Long logId = this.insertDevTaskLog(tblDevTaskLog, request);

            if (!attachFiles.equals("") && !attachFiles.equals(null)) {
                List<TblDevTaskLogAttachement> files = JsonUtil.fromJson(attachFiles, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskLogAttachement.class));
                for (int i = 0; i < files.size(); i++) {
                    files.get(i).setDevTaskLogId(logId);
                    files.get(i).setCreateBy(userId);
                }
                tblDevTaskLogAttachementMapper.addLogAttachement(files);
            }
            if (!attachFiles.equals("") && !attachFiles.equals(null) && !attachFiles.equals("[]")) {
                List<TblDevTaskAttachement> fileTask = JsonUtil.fromJson(attachFiles, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskAttachement.class));
                for (int i = 0; i < fileTask.size(); i++) {
                    fileTask.get(i).setDevTaskId(testId);
                    fileTask.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
                }
                tblDevTaskAttachementMapper.addAttachement(fileTask);
            }

            if (tblDevTask.getDefectID() != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("devTaskId", tblDevTask.getId());
                result.put("reqFid", tblDevTask.getRequirementFeatureId());
                result.put("defectId", tblDevTask.getDefectID());
                tblDefectInfoMapper.updateDevDefect(result);

                // ????????????
                TblDefectInfo defectInfo1 = new TblDefectInfo();
                defectInfo1.setId(tblDevTask.getDefectID());
                defectInfo1.setDefectStatus(4);
                defectService.updateDefectwithTBC(defectInfo1, new TblDefectRemark(), null, request);
            }
            sendAddMessage(JSON.toJSONString(tblDevTask),tblDevTask.getDevTaskCode(),tblRequirementFeature.getId());
        }
        
        //???????????????????????????
        SpringContextHolder.getBean(WorkTaskService.class).addTestTask(tblRequirementFeature);
    }

    @Override
    @DataSource(name = "tmpDataSource")
    @Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
    public void addTestTask(TblRequirementFeature tblRequirementFeature) {
        tblRequirementFeature.setCheckStatus(null);
        tblRequirementFeature.setRequirementFeatureSource(null);
        String featureCode = devManageToTestManageInterface.getNewFeatureCode();
        tblRequirementFeature.setFeatureCode(featureCode);
        tblRequirementFeature.setCreateType(-1);//aviyy 20201102 ?????????????????????????????????????????????
        tblRequirementFeatureMapper.insertTestReqFeature(tblRequirementFeature);
    }

    /**
    *@author liushan
    *@Description ??????jira??????
    *@Date 2020/5/11
    *@Param [workCodes, request]
    *@return void
    **/
    @Override
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public Set<String> jiraFileByCode(String workCodeJiraIds, HttpServletRequest request) {
        Set<String> set = new HashSet<>();
        String[] ids = workCodeJiraIds.split(",");
        try {
            for(String id:ids){
                try {
                    Map<String,Map[]> maps = jiraUtil.jiraFileDemo(id);
                    String code = maps.get("customUrl")[0].get("key").toString();
                    TblDevTask devTask = devTaskMapper.getDevTaskByCode(code);
                    if(devTask == null){
                        set.add("id:"+id + ",code:"+ code+"??????????????????");
                        continue;
                    }
                    try {
                        set.add("id:"+id + ",code:"+code+",fileLength:"+maps.get("files").length);
                        for(Map<String,Object> map:maps.get("files")){
                            String fileName = map.get("fileName").toString();
                            TblDevTaskAttachement attachement = new TblDevTaskAttachement(request);
                            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);// ?????????

                            HttpURLConnection httpURLConnection = UploadFileUtil.downloadFile(map.get("accessory").toString());
                            // ????????????
                            Long fileLengthLong = httpURLConnection.getContentLengthLong();
                            // ???????????????????????????
                            logger.info("??????????????????????????????:" + fileLengthLong / (1024) + "KB");
                            // ????????????????????????????????????
                            InputStream is = httpURLConnection.getInputStream();
                            String keyname = s3Util.putObject1(s3Util.getDevTaskBucket(), fileName, is,fileLengthLong);
                            attachement.setDevTaskId(devTask.getId());
                            attachement.setFileS3Bucket(s3Util.getDevTaskBucket());
                            attachement.setFileS3Key(keyname);
                            attachement.setFileNameOld(fileName);
                            attachement.setFileType(extension);
                            TblDevTaskAttachement devTaskAttachement = tblDevTaskAttachementMapper.getAttachementByfileNameOld(attachement.getDevTaskId(),fileName);
                            if (devTaskAttachement == null) {
                                tblDevTaskAttachementMapper.insertSelective(attachement);
                            } else{
                                attachement.setId(devTaskAttachement.getId());
                                tblDevTaskAttachementMapper.updateByPrimaryKey(attachement);
                            }
                        }
                    } catch (Exception e){
                        set.add("id:"+id + ",code:"+code+",?????????????????????");
                        continue;
                    }
                }catch (Exception e){
                    set.add("id:"+id + ",jira??????????????????");
                }
            }
        } catch (Exception e) {
            logger.info("?????????????????????");
        }
        return set;
    }

    /**
     * 
    * @Title: getFeatureCode
    * @Description: ??????????????????????????????
    * @author author
    * @return
    * @throws
     */
    private String getFeatureCode() {
        String featureCode = "";
        int codeInt = 0;
        Object object = redisUtils.get("TBL_REQUIREMENT_FEATURE_FEATURE_CODE");
        if (object != null && !"".equals(object)) {// redis????????????redis??????
            String code = object.toString();
            // codeInt=Integer.parseInt(code.substring(Constants.ITMP_DEV_TASK_CODE.length()+1))+1;
            codeInt = Integer.parseInt(code) + 1;
        } else {// redis????????????????????????????????????????????????
            int length = Constants.ITMP_DEV_TASK_CODE.length() + 1;
            String cod = devTaskService.findMaxCode(length);
            if (!StringUtils.isBlank(cod)) {
                codeInt = Integer.parseInt(cod) + 1;
            } else {
                codeInt = 0;
            }
        }
        DecimalFormat df = new DecimalFormat("00000000");
        String codeString = df.format(codeInt);
        featureCode = Constants.ITMP_DEV_TASK_CODE + codeString;
        redisUtils.set("TBL_REQUIREMENT_FEATURE_FEATURE_CODE", codeString);
        return featureCode;
    }
}
