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
    
    //需求来源
    public static final String SOURCE = "TBL_REQUIREMENT_INFO_REQUIREMENT_SOURCE";
    //需求类型
    public static final String TYPE = "TBL_REQUIREMENT_INFO_REQUIREMENT_TYPE";
    //需求优先级
    public static final String PRIORITY = "TBL_REQUIREMENT_INFO_REQUIREMENT_PRIORITY";
    //需求计划
    public static final String PLAN = "TBL_REQUIREMENT_INFO_REQUIREMENT_PLAN";
    //需求状态
    public static final String STATUS = "TBL_REQUIREMENT_INFO_REQUIREMENT_STATUS";
    //开发工作任务状态
    public static final String DEVSTAUS = "TBL_DEV_TASK_DEV_TASK_STATUS";
    //需求类型
    public static final String RETYPE = "TBL_REQUIREMENT_INFO_REQUIREMENT_TYPE";
    //存放需要添加日志的字段类型
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
    * @Description: 分页获取所有开发工作任务
    * @author author
    * @param tblDevTask 封装的查询条件
    * @param projectIds 选择的项目
    * @param page 第几页
    * @param rows 每页数目
    * @param request
    * @return map key-rows:获取的开发工作任务
    *                 records:总数目
    *                 total: 总页数
    *                 page:第几页
    *                 rwos：不分页情况下返回的数据
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
            if (flag) {//管理员
                list = devTaskMapper.getDevTaskAll(parm);
            } else {//非管理员
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
    * @Description: excel导出时，查询出的工作任务
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
        if (flag) {//管理员  导出数据
            list = devTaskMapper.getDevTaskAll(parm);
        } else {//非管理员
            list = devTaskMapper.getDevTask(parm);
        }


        List<TblDataDic> dataDics = getDataFromRedis("CODE_REVIEW_STATU");


        Object object = redisUtils.get(DEVSTAUS);
        Map<String, Object> mapsource = new HashMap<String, Object>();
        if (object != null && !"".equals(object)) {//redis有直接从redis中取
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
    * @Description: 从缓存中获取数据字典信息
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
    * @Description: 获取同一项目组的人员
    * @author author
    * @param id 人员ID
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
    * @Description: 获取需求列表
    * @author author
    * @param RequirementInfo 封装的查询条件
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
            if (object != null && !"".equals(object)) {//redis有直接从redis中取
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
     * 查询当前登陆人所属的项目
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
     * shan-根据id获取当前工作任务
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
    * @Description: 获取所有系统信息
    * @author author
    * @param systemInfo 封装的查询条件
    * @param page 第几页
    * @param rows 每页条数
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
            if (object != null && !"".equals(object)) {//redis有直接从redis中取
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
    * @Description:获取编辑开发工作任务
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
    * @Description:更新开发工作任务
    * @author author
    * @param obj 工作任务
    * @param attachFiles 附件
    * @param deleteAttaches 需要删除的附件
    * @param Userid
    * @param request
     */
    @Transactional(readOnly = false)
    @Override
    public void updateDevTask(String obj, String attachFiles, String deleteAttaches, Long Userid, HttpServletRequest request) {
       //开发工作任务
    	TblDevTask devTask = JSON.parseObject(obj, TblDevTask.class);

        devTask.setLastUpdateBy(Userid);
        Date date = new Date();
        try {
            TblDevTask OlddevTask = devTaskMapper.getDevOld(devTask.getId());//旧数据
            
            //如果有关联缺陷，更新缺陷关联关系
            if (devTask.getDefectID() != null && devTask.getDefectID() > 0) {
                TblDefectInfo tblDefectInfo = new TblDefectInfo();
                tblDefectInfo.setId(devTask.getDefectID());
                tblDefectInfo.setAssignUserId(devTask.getDevUserId());
                defectService.updateDefect(tblDefectInfo, request);
            }
            Long Devid = devTask.getId();//任务id
            Long DevUser = devTask.getDevUserId();//新用户ID
            TblDevTask DevTask = devTaskMapper.getDevUser(Devid);
            if (DevTask != null) {
                Long oldUser = DevTask.getDevUserId();
                if (oldUser != DevUser) {
                    String userAccount = OlddevTask.getUserAccount();
                }

            }
            if(devTask.getDevTaskStatus()==1 || devTask.getDevTaskStatus()==2) { //工作任务是待开发和开发中， 都要把开发任务改成实施中
            	/*Integer Status = devTask.getRequirementFeatureStatus();
                if (Status != null && (Status.longValue() == 1 || Status.longValue() == 3)) {*/
                	Long featureId = devTask.getRequirementFeatureId();
                	TblRequirementFeature requirementFeature = tblRequirementFeatureMapper.selectByPrimaryKey(featureId);
                	
                	if(!"02".equals( requirementFeature.getRequirementFeatureStatus())) {//开发任务如果不是实施中再修改为实施中
                		//devTaskMapper.editFeature(featureId);
                    	devTaskService.updateStatus(featureId, request);
                        
                      //插入日志
                		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
                		log.setRequirementFeatureId(featureId);
                		log.setLogType("修改开发任务");
                		
                		String beforeName = "";
                		String afterName = "";
                		
                		beforeName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", requirementFeature.getRequirementFeatureStatus(),"");
                		afterName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", "02","");
                				
                		log.setLogDetail("任务状态："+"&nbsp;&nbsp;“<b>"+beforeName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;") ;
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
            logAlls(devTask.getId().toString(), attachFiles, deleteAttaches, OlddevTask, "编辑工作任务", new HashMap<>(), request);
          
            //关注提醒： 开发工作任务状态或者内容变更时给关注该任务的人发送邮件和微信 --ztt
			sendDevTaskMassage(OlddevTask);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关注提醒： 开发工作任务状态或者内容变更时给关注该任务的人发送邮件和微信 
     * --ztt
     * */
	public void sendDevTaskMassage(TblDevTask devTask) {
		String userIds2 = tblDevTaskAttentionMapper.getUserIdsByDevTaskId(devTask.getId());
		if(StringUtils.isNotBlank(userIds2)) {
			Map<String,Object> emWeMap = new HashMap<String, Object>();
			emWeMap.put("messageTitle", "【IT开发测试管理系统】- 你关注的任务有更新");
			emWeMap.put("messageContent","您关注的“"+ devTask.getDevTaskCode()+" | "+devTask.getDevTaskName()+"，内容有更新，请注意。”");
			emWeMap.put("messageReceiver",userIds2 );//接收人 关注该任务的人
			emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
			devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
		}
	}
	
	/**
	 * 开发工作任务发送邮件和微信
	 */
	public void sendWorkTaskMassage(TblDevTask devTask) {
		try {
			if(devTask!=null&&devTask.getDevUserId()!=null&&devTask.getDevUserId()!=0) {
				Map<String,Object> emWeMap = new HashMap<String, Object>();
				emWeMap.put("messageTitle", "【IT开发测试管理系统】- 收到一个新分配的开发工作任务");
				emWeMap.put("messageContent","您收到一个新分配的开发工作任务：“"+ devTask.getDevTaskCode()+" | "+ devTask.getDevTaskName()+"”，请及时处理。");
				emWeMap.put("messageReceiver",devTask.getDevUserId());//接收人开发人员 用户表主键
				emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
				devManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
			}
		} catch (Exception e) {
			logger.error("开发工作任务发送消息异常："+e.getMessage(),e);
			e.printStackTrace();
		}
	}

    /**
     * 
    * @Title: addDevTask
    * @Description: 新增开发工作任务
    * @author author
    * @param obj 工作任务
    * @param attachFiles 附件
    * @param Userid  创建人
    * @param request
    * @param UserAccount 创建人账号
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
        //开发任务状态为待实施或实施完成时，开发任务状态边为实施中
        if (Status != null && Status.longValue() == 1 || Status.longValue() == 3) {
        	//devTaskMapper.editFeature(featureId);
    		TblRequirementFeature requirementFeature = tblRequirementFeatureMapper.selectByPrimaryKey(featureId);

        	devTaskService.updateStatus(featureId, request);
        	//插入日志
    		TblRequirementFeatureLog log = new TblRequirementFeatureLog();
    		log.setRequirementFeatureId(featureId);
    		log.setLogType("修改开发任务");
    		
    		String beforeName = "";
    		String afterName = "";
    		
    		beforeName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", requirementFeature.getRequirementFeatureStatus(),"");
    		afterName = CommonUtil.getDictValueName("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS", "02","");
    				
    		log.setLogDetail("任务状态："+"&nbsp;&nbsp;“<b>"+beforeName+"</b>”&nbsp;&nbsp;"+"修改为"+"&nbsp;&nbsp;“<b>"+afterName+"</b>”&nbsp;&nbsp;") ;
    		log.setCreateBy(CommonUtil.getCurrentUserId(request));
    		log.setCreateDate(new Timestamp(new Date().getTime()));
    		log.setUserId(CommonUtil.getCurrentUserId(request));
    		log.setUserAccount(CommonUtil.getCurrentUserAccount(request));
    		log.setUserName(CommonUtil.getCurrentUserName(request));
    		requirementFeatureLogMapper.insert(log);
            
        }
        devTaskMapper.addDevTask(devTask);
        
        //添加开发任务时间追踪点
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("requirementFeatureId", featureId);
        jsonMap.put("devTaskCreateTime", new Timestamp(new Date().getTime()));
        String json = JsonUtil.toJson(jsonMap);
        devTaskService.updateReqFeatureTimeTrace(json);

        //添加日志
        Long testId = devTask.getId();
        TblDevTaskLog tblDevTaskLog = new TblDevTaskLog();
        tblDevTaskLog.setDevTaskId(testId);
        tblDevTaskLog.setLogType("新增工作任务");
        Long logId = this.insertDevTaskLog(tblDevTaskLog, request);
        
        //添加日志附件
        if (!attachFiles.equals("") && !attachFiles.equals(null)) {
            List<TblDevTaskLogAttachement> files = JsonUtil.fromJson(attachFiles, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskLogAttachement.class));
            for (int i = 0; i < files.size(); i++) {
                files.get(i).setDevTaskLogId(logId);
                files.get(i).setCreateBy(Userid);
            }
            tblDevTaskLogAttachementMapper.addLogAttachement(files);
        }
        
        //添加工作任务附件
        if (!attachFiles.equals("") && !attachFiles.equals(null) && !attachFiles.equals("[]")) {
            List<TblDevTaskAttachement> fileTask = JsonUtil.fromJson(attachFiles, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskAttachement.class));
            for (int i = 0; i < fileTask.size(); i++) {
                fileTask.get(i).setDevTaskId(testId);
                fileTask.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
            }
            tblDevTaskAttachementMapper.addAttachement(fileTask);
        }
        
        //如果通过开发缺陷确认并新建开发工作任务，则再处理开发缺陷
        if (devTask.getDefectID() != null) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("devTaskId", devTask.getId());
            result.put("reqFid", devTask.getRequirementFeatureId());
            result.put("defectId", devTask.getDefectID());
            tblDefectInfoMapper.updateDevDefect(result);

            // 确认缺陷
            TblDefectInfo defectInfo = new TblDefectInfo();
            defectInfo.setId(devTask.getDefectID());
            defectInfo.setDefectStatus(4);
            defectService.updateDefectwithTBC(defectInfo, new TblDefectRemark(), null, request);
        }

        sendAddMessage(obj,devTask.getDevTaskCode(),featureId);
        
        sendWorkTaskMassage(devTask);// 发送消息

    }

    //给开发人员发送消息
    private void sendAddMessage(String objStr,String devCode,Long featureId){
        //用featureId查询该任务systemId
        TblDevTask tblDevTask = tblDefectInfoMapper.selectRequirementFeatureById(featureId);
        TblDevTask devTask = JSON.parseObject(objStr, TblDevTask.class);
        Map<String,Object> map=new HashMap<>();
        map.put("messageTitle","收到一个新分配的开发工作任务");
        map.put("messageContent",devCode+"|"+devTask.getDevTaskName());
        map.put("messageReceiverScope",2);//发送范围：指定人
        //消息来源 2-- 新建工作任务
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
    * @Description: 工作任务详情
    * @author author
    * @param id
    * @return map key-devTaskType:开发工作任务类型
    *                 dev ：工作任务数据
    * @throws
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSeeDetail(String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        DevDetailVo dev = new DevDetailVo();
        Long devID = Long.parseLong(id);
        //关联的开发任务
        dev = allDevRequirementMapper.AlldevReuirement(devID);
        dev.setDevuserName(allDevRequirementMapper.getdevName(dev.getDevuserID()));
        dev.setCreateName(allDevRequirementMapper.getdevName(dev.getCreateBy()));
        dev.setManageUserName(allDevRequirementMapper.getdevName(dev.getManageUserId()));
        dev.setSystemName(allDevRequirementMapper.getSystemName(dev.getSystemId()));
        dev.setExecuteUserName(allDevRequirementMapper.getdevName(dev.getExecuteUserId()));
        dev.setApplyUserName(allDevRequirementMapper.getdevName(dev.getApplyUserId()));
        dev.setApplyDeptName(allDevRequirementMapper.getdeptName(dev.getApplyDeptId()));
        
        //处理数据字典对应的值
        Object source = redisUtils.get(SOURCE);//需求来源
        Object type = redisUtils.get(TYPE);
        Object priority = redisUtils.get(PRIORITY);
        Object plan = redisUtils.get(PLAN);
        Object status = redisUtils.get(STATUS);//需求
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
    
    //关注列表
    @Override
	public List<TblDevTaskAttention> getAttentionList(TblDevTaskAttention attention) {
		List<TblDevTaskAttention> attentionList = tblDevTaskAttentionMapper.selectList(new EntityWrapper<TblDevTaskAttention>(attention));
		return attentionList;
	}
	
	//更新需求关注功能
	@Override
	@Transactional(readOnly = false)
	public void changeAttention(Long id, Integer attentionStatus, HttpServletRequest request) {
		Long userId = CommonUtil.getCurrentUserId(request);
		Timestamp stamp = new Timestamp(new Date().getTime());
		TblDevTaskAttention attention = new TblDevTaskAttention();
		attention.setDevTaskId(id);
		attention.setUserId(userId);
		TblDevTaskAttention attentionOld = tblDevTaskAttentionMapper.selectOne(attention);
		if (attentionStatus == 1) { //关注
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

    //处理
    @Override
    @Transactional(readOnly = false)
    public void Handle(TblDevTask devTask, String HattachFiles, String deleteAttaches, HttpServletRequest request) {
        TblDevTask oldDevTask = devTaskMapper.getDevOld(devTask.getId());
        devTaskMapper.Handle(devTask);
        logAlls(devTask.getId().toString(), HattachFiles, deleteAttaches, oldDevTask, "处理工作任务", new HashMap<>(), request);
        sendDevTaskMassage(oldDevTask);
    }

    /**
     * 
    * @Title: CodeHandle
    * @Description: 代码评审状态
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
        //更新数据，状态变为代码评审状态
        devTaskMapper.CodeHandle(devTask);
        logAlls(devTask.getId().toString(), HattachFiles, deleteAttaches, oldDevTask, "处理工作任务", new HashMap<>(), request);
        sendDevTaskMassage(oldDevTask);
    }

    /**
     * 
    * @Title: assigDev
    * @Description: 工作任务转派
    * @author author
    * @param assig 转派人
    * @param Remark 工作备注
    * @param request
    * @throws
     */
    @Override
    @Transactional(readOnly = false)
    public void assigDev(String assig, String Remark, HttpServletRequest request) {
        TblDevTask devTask = new TblDevTask();
        devTask = JSON.parseObject(assig, TblDevTask.class);
        Long Devid = devTask.getId();//任务id
        Long DevUser = devTask.getDevUserId();//新用户ID
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
                logAll(Devid.toString(), "", oldDevTask, "工作任务分派", remarkMap, request);
                sendDevTaskMassage(oldDevTask);

            } else if (OleUser != DevUser) {
                devTaskMapper.assigDev(devTask);
                if (!Remark.equals("") && Remark != null) {
                    remarkMap.put("remark", Remark);
                }
                logAll(Devid.toString(), "", oldDevTask, "工作任务转派", remarkMap, request);
                String UserAccount = oldDevTask.getUserAccount();
                sendDevTaskMassage(oldDevTask);
            }

        }
    }


    /**
     * 
    * @Title: getMyTaskListForTaskPlugin
    * @Description: 获取个人工作任务
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
    * @Description: 获取个人工作任务详情
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
    * @Description: 完成工作任务
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
    * @Description: 待开发---->开发中
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
        logAlls(devTask.getId().toString(), DHattachFiles, deleteAttaches, oldDevTask, "处理工作任务", new HashMap<>(), request);
        sendDevTaskMassage(oldDevTask);
    }

    //评审通过，开发完成
    @Override
    @Transactional(readOnly = false)
    public void reviewAdopt(String id, String adoptFiles, String deleteAttaches, HttpServletRequest request) {
        TblDevTask oldDevTask = devTaskMapper.getDevOld(Long.parseLong(id));

        devTaskMapper.reviewAdopt(id);
        logAlls(id, adoptFiles, deleteAttaches, oldDevTask, "处理工作任务", new HashMap<>(), request);
        sendDevTaskMassage(oldDevTask);
    }

    //评审未通过
    @Override
    @Transactional(readOnly = false)
    public void reviewNAdopt(String id, String adoptFiles, String deleteAttaches, HttpServletRequest request) {

        TblDevTask oldDevTask = devTaskMapper.getDevOld(Long.parseLong(id));
        devTaskMapper.reviewNAdopt(id);
        logAlls(id, adoptFiles, deleteAttaches, oldDevTask, "处理工作任务", new HashMap<>(), request);
        sendDevTaskMassage(oldDevTask);
    }

    @Override
    public String DevfindMaxCode(int length) {
        return devTaskMapper.DevfindMaxCode(length);
    }

    /**
     * 
    * @Title: getDevCode
    * @Description: 工作任务编号生成规则
    * @author author
    * @return
    * @throws
     */
    public String getDevCode() {
        String featureCode = "";
        int codeInt = 0;
        Object object = redisUtils.get("TBL_DEV_TASK_DEV_TASK_CODE");
        if (object != null && !"".equals(object)) {//redis有直接从redis中取
            String code = object.toString();
            if (!StringUtils.isBlank(code)) {
                codeInt = Integer.parseInt(code) + 1;
            }
        } else {//redis中没有查询数据库中最大的任务编号
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

    //开发任务状态
    @Override
    @Transactional(readOnly = true)
    public Map devStatus() {
        Object object = redisUtils.get(DEVSTAUS);
        Map<String, Object> mapsource = new HashMap<String, Object>();
        if (object != null && !"".equals(object)) {//redis有直接从redis中取
            mapsource = JSON.parseObject(object.toString());
        }
        return mapsource;
    }

    //需求类型
    @Override
    @Transactional(readOnly = true)
    public Map ReqStatus() {
        Object object = redisUtils.get(RETYPE);
        Map<String, Object> mapsource = new HashMap<String, Object>();
        if (object != null && !"".equals(object)) {//redis有直接从redis中取
            mapsource = JSON.parseObject(object.toString());
        }
        return mapsource;
    }

   //系统类型
    @Override
    @Transactional(readOnly = true)
    public Map ReqSystem() {
        Object object = redisUtils.get("TBL_SYSTEM_INFO_SYSTEM_TYPE");
        Map<String, Object> mapsource = new HashMap<String, Object>();
        if (object != null && !"".equals(object)) {//redis有直接从redis中取
            mapsource = JSON.parseObject(object.toString());
        }
        return mapsource;
    }

    //开发任务状态
    @Override
    @Transactional(readOnly = true)
    public Map FeatureStatus() {
        Object object = redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS");
        Map<String, Object> mapsource = new HashMap<String, Object>();
        if (object != null && !"".equals(object)) {//redis有直接从redis中取
            mapsource = JSON.parseObject(object.toString());
        }
        return mapsource;
    }
    
    //工作任务日志
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
    * @Description: 获取工作任务扩展字段
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
     *  根据id获取开发任务信息
     * @param id
     * @return
     */
    @Override
    public TblRequirementFeature selectRequirementFeatureById(Long id) {
        return devTaskMapper.selectRequirementFeatureById(id);
    }

   //日志公共字段赋值
    private TblDevTaskLog setDevTaskLog(TblDevTaskLog defectLog, HttpServletRequest request) {
        defectLog.setUserId(CommonUtil.getCurrentUserId(request));
        defectLog.setUserName(CommonUtil.getCurrentUserName(request));
        defectLog.setUserAccount(CommonUtil.getCurrentUserAccount(request));
        return defectLog;
    }

    /**
     * 添加日志附件
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

    //记录工作任务日志附件和工作任务附件
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

        // 添加删除附件日志
        if (!deleteAttaches.equals("") && !deleteAttaches.equals(null) && !deleteAttaches.equals("[]")) {
            List<TblDevTaskLogAttachement> deleteFileTask = JsonUtil.fromJson(deleteAttaches, JsonUtil.createCollectionType(ArrayList.class, TblDevTaskLogAttachement.class));
            for (int i = 0; i < deleteFileTask.size(); i++) {
                deleteFileTask.get(i).setDevTaskLogId(logId);
                deleteFileTask.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
                deleteFileTask.get(i).setStatus(2);//逻辑删除
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
     * 添加日志详情 一个字段
     * 更新字段
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

            // 工作任务名称
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
            if (object != null && !"".equals(object)) {//redis有直接从redis中取
                mapsource = JSON.parseObject(object.toString());
            }
            // 工作任务
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
                        // 新增日志操作
                        Map<String, Object> newMap = new HashMap<>();
                        newMap.put("fieldName", propertyInfo.name());
                        newMap.put("oldValue", "");
                        newMap.put("newValue", newDeclareValue.toString());
                        list.add(newMap);
                    } else {
                        // 删除日志操作
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
	                            delMap.put("newValue","已删除");
	                            list.add(delMap);*/
                        }

                    }
                    continue;
                }
            }
            // 修改日志操作
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
        // 保存到数据库
        tblDevTaskLogMapper.updateLogById(log);
    }

    @Override
    public void addDefectDevTask(String obj, String attachFiles, Long Userid, HttpServletRequest request,
                                 String UserAccount) {

    }

    
    /**
     * 
    * @Title: getAllFeature
    * @Description: 获取所有开发任务
    * @author author
    * @param tblDevTask 封装查询条件
    * @param userdId 当前用户
    * @param roleCodes 角色信息
    * @param pageNumber 第几页
    * @param pageSize 每页条数
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
        if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户有角色是系统管理员
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
        //转换
        TblDefectInfo defectInfo = JSON.parseObject(defect,TblDefectInfo.class);
        List<TblDevTask> tblDevTasks = JSON.parseArray(workTask,TblDevTask.class);

        //添加开发任务信息
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

        //添加时间点追踪表
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("requirementFeatureId", tblRequirementFeature.getId());
        jsonMap.put("devTaskCreateTime", new Timestamp(new Date().getTime()));
        String json = JsonUtil.toJson(jsonMap);
        devTaskService.updateReqFeatureTimeTrace(json);
        Long userId = CommonUtil.getCurrentUserId(request);

        //添加工作任务信息
        for(TblDevTask tblDevTask : tblDevTasks){
            CommonUtil.setBaseValue(tblDevTask,request);
            tblDevTask.setRequirementFeatureId(tblRequirementFeature.getId());
            tblDevTask.setDevTaskCode(getDevCode());
            devTaskMapper.addDevTask(tblDevTask);

            Long testId = tblDevTask.getId();
            TblDevTaskLog tblDevTaskLog = new TblDevTaskLog();
            tblDevTaskLog.setDevTaskId(testId);
            tblDevTaskLog.setLogType("新增工作任务");
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

                // 确认缺陷
                TblDefectInfo defectInfo1 = new TblDefectInfo();
                defectInfo1.setId(tblDevTask.getDefectID());
                defectInfo1.setDefectStatus(4);
                defectService.updateDefectwithTBC(defectInfo1, new TblDefectRemark(), null, request);
            }
            sendAddMessage(JSON.toJSONString(tblDevTask),tblDevTask.getDevTaskCode(),tblRequirementFeature.getId());
        }
        
        //同步测试库测试任务
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
        tblRequirementFeature.setCreateType(-1);//aviyy 20201102 生产问题确认生成的测试任务类型
        tblRequirementFeatureMapper.insertTestReqFeature(tblRequirementFeature);
    }

    /**
    *@author liushan
    *@Description 同步jira附件
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
                        set.add("id:"+id + ",code:"+ code+"编号不存在！");
                        continue;
                    }
                    try {
                        set.add("id:"+id + ",code:"+code+",fileLength:"+maps.get("files").length);
                        for(Map<String,Object> map:maps.get("files")){
                            String fileName = map.get("fileName").toString();
                            TblDevTaskAttachement attachement = new TblDevTaskAttachement(request);
                            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);// 后缀名

                            HttpURLConnection httpURLConnection = UploadFileUtil.downloadFile(map.get("accessory").toString());
                            // 文件大小
                            Long fileLengthLong = httpURLConnection.getContentLengthLong();
                            // 控制台打印文件大小
                            logger.info("您要下载的文件大小为:" + fileLengthLong / (1024) + "KB");
                            // 建立链接从请求中获取数据
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
                        set.add("id:"+id + ",code:"+code+",文件导入失败！");
                        continue;
                    }
                }catch (Exception e){
                    set.add("id:"+id + ",jira数据查不到！");
                }
            }
        } catch (Exception e) {
            logger.info("文件下载失败！");
        }
        return set;
    }

    /**
     * 
    * @Title: getFeatureCode
    * @Description: 开发任务编号生成规则
    * @author author
    * @return
    * @throws
     */
    private String getFeatureCode() {
        String featureCode = "";
        int codeInt = 0;
        Object object = redisUtils.get("TBL_REQUIREMENT_FEATURE_FEATURE_CODE");
        if (object != null && !"".equals(object)) {// redis有直接从redis中取
            String code = object.toString();
            // codeInt=Integer.parseInt(code.substring(Constants.ITMP_DEV_TASK_CODE.length()+1))+1;
            codeInt = Integer.parseInt(code) + 1;
        } else {// redis中没有查询数据库中最大的任务编号
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
