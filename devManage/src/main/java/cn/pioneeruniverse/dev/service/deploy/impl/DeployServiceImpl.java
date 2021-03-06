package cn.pioneeruniverse.dev.service.deploy.impl;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.databus.DataBusRequestHead;
import cn.pioneeruniverse.common.databus.DataBusUtil;
import cn.pioneeruniverse.common.databus.DisposeDetail;
import cn.pioneeruniverse.common.databus.SubDisposeDetail;
import cn.pioneeruniverse.common.dto.TblDeptInfoDTO;
import cn.pioneeruniverse.common.jenkins.JenkinsUtil;
import cn.pioneeruniverse.common.utils.*;
import cn.pioneeruniverse.common.velocity.tag.VelocityDataDict;
import cn.pioneeruniverse.dev.controller.DevTaskController;
import cn.pioneeruniverse.dev.dao.mybatis.*;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import cn.pioneeruniverse.dev.feignInterface.DevManageToTestManageInterface;
import cn.pioneeruniverse.dev.service.build.IJenkinsBuildService;
import cn.pioneeruniverse.dev.service.common.JenkinsBuildStateService;
import cn.pioneeruniverse.dev.vo.common.request.JenkinsJobBuildStateQuery;
import cn.pioneeruniverse.dev.service.deploy.IDeployService;
import cn.pioneeruniverse.dev.service.devtask.DevTaskService;
import cn.pioneeruniverse.dev.service.structure.IStructureService;
import cn.pioneeruniverse.dev.service.structure.impl.StructureServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("iDeployServiceImpl")
public class DeployServiceImpl implements IDeployService {
    private final static Logger log = LoggerFactory.getLogger(DeployServiceImpl.class);
    @Autowired
    private TblSystemJenkinsMapper tblSystemJenkinsMapper;
    @Autowired
    private TblSystemJenkinsJobRunMapper tblSystemJenkinsJobRunMapper;
    @Autowired
    private TblSystemModuleJenkinsJobRunMapper tblSystemModuleJenkinsJobRunMapper;
    @Autowired
    private TblSystemScmMapper tblSystemScmMapper;
    @Autowired
    private TblToolInfoMapper tblToolInfoMapper;
    @Autowired
    private TblRequirementFeatureMapper tblRequirementFeatureMapper;
    @Autowired
    private IJenkinsBuildService iJenkinsBuildService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    private TblSystemInfoMapper tblSystemInfoMapper;
    @Autowired
    private TblSystemModuleMapper tblSystemModuleMapper;
    @Autowired
    private TblSystemSonarMapper tblSystemSonarMapper;
    @Autowired
    private TblSystemModuleScmMapper tblSystemModuleScmMapper;
    @Autowired
    private TblSystemDeployMapper tblSystemDeployMapper;
    @Autowired
    private TblServerInfoMapper tblServerInfoMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private TblArtifactInfoMapper tblArtifactInfoMapper;
    @Autowired
    private TblSystemDeployScriptMapper tblSystemDeployScriptMapper;
    @Autowired
    private TblSystemInfoMapper systemInfoMapper;
    @Autowired
    private TblRequirementInfoMapper requirementInfoMapper;    //??????mapper
    @Autowired
    private TblRequirementFeatureTimeTraceMapper timeTraceMapper;
    @Autowired
    private TblCommissioningWindowMapper commissioningWindowMapper;
    @Autowired
    private DevManageToSystemInterface systemInterface;
    @Autowired
    private DevTaskService devTaskService;
    @Autowired
    private TblUserInfoMapper userInfoMapper;
    @Autowired
    private StructureServiceImpl structureServiceImpl;
    @Autowired
    private TblSystemAutomaticTestResultMapper tblSystemAutomaticTestResultMapper;
    @Autowired
    private TblSystemAutomaticTestConfigMapper tblSystemAutomaticTestConfigMapper;
    @Autowired
    private DevTaskController devTaskController;
    @Autowired
    private TblProjectInfoMapper tblProjectInfoMapper;
    @Autowired
    private IStructureService iStructureService;
    @Autowired
    private DevManageToSystemInterface devManageToSystemInterface;
    @Autowired
    private DevManageToTestManageInterface devManageToTestManageInterface;
    @Autowired
    private TblSystemDeployeUserConfigMapper tblSystemDeployeUserConfigMapper;
    @Autowired
    private TblUserInfoMapper tblUserInfoMapper;
    @Autowired
    private TblSystemDbConfigMapper tblSystemDbConfigMapper;
    @Autowired
    JenkinsBuildStateService jenkinsBuildStateService;

    @Value("${databuscc.defectName}")
    private String databusccName;
    @Value("${s3.logBucket}")
    private String logBucket;
    @Autowired
    private S3Util s3Util;
    private static ExecutorService threadPool;

    {
        threadPool = Executors.newCachedThreadPool();

    }

    /**
     * ????????????????????????
     *
     * @param systemId ??????id
     * @return Map<String, Object>
     * @author weiji
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDeployJobName(String systemId, HttpServletRequest request) {
        Object tblToolInfo = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
        Map<String, Object> envMap = JSON.parseObject(tblToolInfo.toString());
        Map<String, Object> map = new HashMap<>();
        map.put("system_id", systemId);
        //map.put("DEPLOY_STATUS", 1);
        map.put("STATUS", 1);
        map.put("JOB_TYPE", 2);// ??????
        map.put("create_type", 2);// ??????
        try {
            List<TblSystemJenkins> list = tblSystemJenkinsMapper.selectByMap(map);
            List<TblSystemJenkins> systemJenkins = new ArrayList<>();
            for (TblSystemJenkins tt : list) {
                if (tt.getEnvironmentType() != null) {
                    tt.setRootPom(tt.getId() + "");
                    if (judgeEnvByuser(request, Long.parseLong(systemId), tt.getEnvironmentType().toString())) {
                        tt.setEnvironmentTypeName(envMap.get(tt.getEnvironmentType().toString()).toString());
                        systemJenkins.add(tt);
                    }
                }
            }
            if (list != null && list.size() > 0) {
                map.put("ztreeObjs", getZtree(list));
            } else {
                map.put("ztreeObjs", "");
            }
            map.put("status", "success");
            map.put("list", systemJenkins);
            return map;
        } catch (Exception e) {


            map.put("status", "fail");
            this.handleException(e);
            return map;
        }

    }


    private List<ZtreeObj> getZtree(List<TblSystemJenkins> list) {

        String[] a = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getJobPath() == null || list.get(i).getJobPath().equals("/")) {
                String jobName = list.get(i).getJobName();
                jobName = jobName.replaceAll("/", "--");
                a[i] = jobName + "," + list.get(i).getRootPom() + "," + list.get(i).getEnvironmentTypeName();
            } else {
                if (list.get(i).getJobPath().endsWith("/")) {
                    String jobName = list.get(i).getJobName();
                    jobName = jobName.replaceAll("/", "--");
                    a[i] =
                            list.get(i).getJobPath().substring(0, list.get(i).getJobPath().length() - 1)
                                    + "/"
                                    + jobName + "," + list.get(i).getRootPom() + "," + list.get(i).getEnvironmentTypeName();
                } else {
                    String jobName = list.get(i).getJobName();
                    jobName = jobName.replaceAll("/", "--");
                    a[i] = list.get(i).getJobPath() + "/" + jobName + "," + list.get(i).getRootPom() + "," + list.get(i).getEnvironmentTypeName();
                }
            }
        }


        List<ZtreeObj> ztreeObjs = new ArrayList<>();

        for (int y = 0; y < a.length; y++) {
            String[] b = a[y].split("/");
            b = deleteArrayNull(b);
            String ids = "";
            for (int i = 0; i < b.length; i++) {

                ZtreeObj ztreeObj = new ZtreeObj();
                if (!b[i].equals("")) {
                    if (b.length == 1) {
                        String[] ps = b[i].split(",");
                        String oldName = ps[0].replaceAll("--", "/");
                        String name = oldName;
                        String uid = ps[1];
                        String envName = ps[2];
                        ztreeObj.setRealId(uid);
                        ztreeObj.setDocType(envName);
                        ztreeObj.setName(name);


                        ztreeObj.setpId("0");

                        ztreeObj.setId(b[i]);

                    } else if (b.length == 2) {
                        if (i == 0) {
                            ztreeObj.setpId("0");
                            ztreeObj.setName(b[i]);
                            ztreeObj.setId(b[i]);
                        } else {
                            String[] ps = b[i].split(",");
                            String oldName = ps[0].replaceAll("--", "/");
                            String name = oldName;
                            String uid = ps[1];
                            String envName = ps[2];
                            ztreeObj.setRealId(uid);
                            ztreeObj.setDocType(envName);
                            ztreeObj.setName(name);

                            ztreeObj.setpId(b[i - 1]);

                            ztreeObj.setId(b[i]);
                        }
                    } else {
                        if (i == 0) {
                            ztreeObj.setpId("0");
                            ztreeObj.setName(b[i]);
                            ids = ids + b[i];
                            ztreeObj.setId(ids);
                        } else {
                            if (i == (b.length - 1)) {
                                String[] ps = b[i].split(",");
                                String oldName = ps[0].replaceAll("--", "/");
                                String name = oldName;
                                String uid = ps[1];
                                String envName = ps[2];
                                ztreeObj.setRealId(uid);
                                ztreeObj.setDocType(envName);
                                ztreeObj.setName(name);
                                ztreeObj.setpId(ids);
                                ids = ids + b[i];
                                ztreeObj.setId(ids);

                            } else {

                                ztreeObj.setpId(ids);
                                ids = ids + b[i];
                                ztreeObj.setName(b[i]);
                                ztreeObj.setId(ids);
                            }
                        }
                    }


                    ztreeObjs.add(ztreeObj);
                }
            }


        }

        return ztreeObjs;
    }


    private String[] deleteArrayNull(String string[]) {
        String strArr[] = string;

        // step1: ????????????list????????????????????????
        ArrayList<String> strList = new ArrayList<String>();
        for (int i = 0; i < strArr.length; i++) {
            strList.add(strArr[i]);
        }

        // step2: ??????list????????????????????????
        while (strList.remove(null)) ;
        while (strList.remove("")) ;

        // step3: ???list???????????????????????????????????????????????????????????????
        String strArrLast[] = strList.toArray(new String[strList.size()]);

        return strArrLast;
    }

    /**
     * ????????????????????????
     *
     * @param systemJenkisId jenkins?????????id
     * @return Map<String, Object>
     * @author weiji
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getJobNameParam(String systemJenkisId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", systemJenkisId);
        try {
            List<TblSystemJenkins> list = tblSystemJenkinsMapper.selectByMap(map);
            // ??????jenkinstool
            TblToolInfo tblToolInfo = tblToolInfoMapper.selectByPrimaryKey(list.get(0).getToolId());
            String paramJson = iJenkinsBuildService.getJobParameter(tblToolInfo, list.get(0), list.get(0).getJobName());
            //????????????
            paramJson = iStructureService.detailParam(paramJson, list.get(0).getSystemId().toString(), list.get(0).getJobName(), systemJenkisId);
            map.put("status", "success");
            map.put("paramJson", paramJson);
            map.put("jobName", list.get(0).getJobName());
            return map;
        } catch (Exception e1) {
            map.put("status", "fail");
            this.handleException(e1);
            return map;
        }

    }

    /**
     * ????????????
     *
     * @param systemJenkisId ??????jenkins?????????id
     * @param jsonParam      ??????????????????
     * @return Map<String, Object>
     * @author weiji
     */

    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> buildJobManualDeploy(HttpServletRequest request, String systemJenkisId, String jsonParam) {
        Map<String, Object> paramMap = new HashMap<>();
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long usrId = CommonUtil.getCurrentUserId(request);
            Map<String, Object> map = new HashMap<>();
            map.put("id", systemJenkisId);
            List<TblSystemJenkins> list = tblSystemJenkinsMapper.selectByMap(map);
            TblSystemJenkins tblSystemJenkins = list.get(0);
            // ??????jenkinstoolinfo
            TblToolInfo jenkinsToolInfo = tblToolInfoMapper.selectByPrimaryKey(tblSystemJenkins.getToolId());
            String jobName = tblSystemJenkins.getJobName();
            try {
                boolean isRun = iJenkinsBuildService.checkStartBuilding2Manual(jenkinsToolInfo, tblSystemJenkins, jobName);
                if (!isRun) {
                    paramMap.put("status", Constants.ITMP_RETURN_FAILURE);
                    paramMap.put("message", "??????????????????????????????,???????????????!");
                    return paramMap;
                }
            } catch (Exception e) {
                this.handleException(e);
            }

//			if (tblSystemJenkins.getDeployStatus() == 2) {
//				paramMap.put("status", Constants.ITMP_RETURN_FAILURE);
//				paramMap.put("message", "????????????????????????!");
//				return paramMap;
//			}

//			tblSystemJenkins.setBuildStatus(2);// ?????????
            tblSystemJenkins.setDeployStatus(2);//?????????


            // ???????????????
            tblSystemJenkinsMapper.updateById(tblSystemJenkins);
            //??????????????????
            String buildParameter = jsonParam;
            buildParameter = buildParameter.replaceAll("\"", "");
            buildParameter = buildParameter.replaceAll("\\{", "");
            buildParameter = buildParameter.replaceAll("\\}", "");
            buildParameter = buildParameter.replaceAll(",", ";");
            TblSystemJenkinsJobRun ttr = new TblSystemJenkinsJobRun();
            ttr.setSystemJenkinsId(Long.parseLong(systemJenkisId));
            ttr.setSystemId(tblSystemJenkins.getSystemId());
            ttr.setJobName(jobName);
            ttr.setRootPom(".");
            ttr.setBuildStatus(1);// ??????
            ttr.setStartDate(timestamp);
            ttr.setStatus(1);
            ttr.setCreateDate(timestamp);
            ttr.setJobType(2);
            ttr.setBuildParameter(buildParameter);
            ttr.setGoalsOptions("mvn clean install -Dmaven.test.skip=true");
            ttr.setCreateBy(usrId);
            ttr.setCreateType(2);// ??????
            try {
                int jobNumber = iJenkinsBuildService.getNextBuildNumber(jenkinsToolInfo, tblSystemJenkins, jobName);
                paramMap.put("jobNumber", jobNumber);
                ttr.setJobRunNumber(jobNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tblSystemJenkinsJobRunMapper.insertNew(ttr);
            long jobid = ttr.getId();

            TblSystemModuleJenkinsJobRun tj = new TblSystemModuleJenkinsJobRun();
            tj.setSystemJenkinsJobRun(jobid);
            tj.setCreateDate(timestamp);
            tj.setStatus(1);
            tj.setCreateBy(usrId);
            tj.setSystemId(tblSystemJenkins.getSystemId());
            tj.setJobName(jobName);
            tj.setCreateType(2);
            tj.setJobType(2);
            tblSystemModuleJenkinsJobRunMapper.insertNew(tj);
            long moduleJobRunId = tj.getId();
            TblUserInfo tblUserInfo = userInfoMapper.getUserById(usrId);
            paramMap.put("tblUserInfo", tblUserInfo);
            paramMap.put("jobRunId", jobid);
            paramMap.put("systemJenkinsId", systemJenkisId);
            paramMap.put("moduleJobRunId", moduleJobRunId);
            paramMap.put("jobName", jobName);
            paramMap.put("jenkinsToolInfo", jenkinsToolInfo);
            paramMap.put("tblSystemJenkins", tblSystemJenkins);
            return paramMap;
        } catch (NumberFormatException e) {
            this.handleException(e);


        }
        return paramMap;
    }

    /**
     * ????????????????????????
     *
     * @param systemInfo     ?????????????????????
     * @param pageSize
     * @param pageNum
     * @param scmBuildStatus ????????????
     * @param checkIds       ????????????id
     * @param checkModuleIds ????????????id
     * @return Map<String, Object>
     * @author weiji
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAllManualSystemInfo(String systemInfo, Integer pageSize, Integer pageNum, String scmBuildStatus,
                                                      Long uid, String[] refreshIds, String[] checkIds, String[] checkModuleIds, HttpServletRequest request) throws Exception {
        Object tblToolInfo = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
        Map<String, Object> envMap = JSON.parseObject(tblToolInfo.toString());
        getDeleteEnvName("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE", envMap);
        Map<String, Object> map = new HashMap<>();
        TblSystemInfo tblSystemInfo = new TblSystemInfo();
        if (StringUtils.isNotBlank(systemInfo)) {
            tblSystemInfo = JSONObject.parseObject(systemInfo, TblSystemInfo.class);
        }
        List<Map<String, Object>> moduleList = new ArrayList<>();
        Map<String, Object> systeminfoMap = new HashMap<>();
        int start = (pageNum - 1) * pageSize;
        systeminfoMap.put("start", start);
        systeminfoMap.put("pageSize", pageSize);
        systeminfoMap.put("jobType", 2);
        systeminfoMap.put("systemInfo", tblSystemInfo);
        LinkedHashMap codeMap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
        List<String> roleCodes = (List<String>) codeMap.get("roles");
        if (roleCodes != null && roleCodes.contains("XITONGGUANLIYUAN")) {
        } else {
            systeminfoMap.put("uid", uid);
        }
        List<Map<String, Object>> sysInfoList = tblSystemInfoMapper.getAllSystemInfoByBuild(systeminfoMap);
        start = 0;
        systeminfoMap.put("start", start);
        systeminfoMap.put("pageSize", Integer.MAX_VALUE);
        List<Map<String, Object>> size = tblSystemInfoMapper.getAllSystemInfoByBuild(systeminfoMap);
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List<Map<String, Object>> endList = new ArrayList<>();

        if (sysInfoList != null && sysInfoList.size() > 0) {
            String systemIds = CollectionUtil.extractToString(sysInfoList, "id", ",");

            /************?????????sysInfoList?????????sql???????????????????????????????????????**************/


            /************???????????????????????????TblSystemJenkinsJobRun**************/
            EntityWrapper<TblSystemJenkinsJobRun> jobRunManualWrapper = new EntityWrapper<TblSystemJenkinsJobRun>();
            jobRunManualWrapper.in("SYSTEM_ID", systemIds);
            jobRunManualWrapper.eq("CREATE_TYPE", 2);
            jobRunManualWrapper.eq("JOB_TYPE", 2);
            jobRunManualWrapper.eq("BUILD_STATUS", 1);// 1?????????
            List<TblSystemJenkinsJobRun> jobRuns = tblSystemJenkinsJobRunMapper.selectList(jobRunManualWrapper);

            String systemJenkinsIds = CollectionUtil.extractToString(jobRuns, "systemJenkinsId", ",");
            /************???????????????????????????TblSystemJenkins**************/
            EntityWrapper<TblSystemJenkins> systemJenkinsManualWrapper = new EntityWrapper<TblSystemJenkins>();
            systemJenkinsManualWrapper.in("ID", systemJenkinsIds);
            List<TblSystemJenkins> systemJenkinsManualList = tblSystemJenkinsMapper.selectList(systemJenkinsManualWrapper);


            for (Map<String, Object> sysInfomap : sysInfoList) {
                Integer deployType = 1;// ??????????????????
                if (sysInfomap.get("deployType") != null) {
                    deployType = (Integer) sysInfomap.get("deployType");
                }
                sysInfomap.put("deployType", deployType);

                Long id = (Long) sysInfomap.get("id");
                String createType = "";
                if (sysInfomap.get("createType") == null) {
                    continue;
                } else {
                    createType = sysInfomap.get("createType").toString();
                }
                String parent_id = "parent_" + id + "";
                if (createType.equals(Constants.CREATE_TYPE_MANUAL)) {
                    // ??????
                    String nowJobName = "";
                    for (TblSystemJenkinsJobRun jobRun : jobRuns) {
                        if (jobRun.getSystemId().equals(id)) {
                            for (TblSystemJenkins temp : systemJenkinsManualList) {
                                if (temp.getId().equals(jobRun.getSystemJenkinsId())) {
                                    String envName = jobRun.getJobName();
                                    String path = temp.getJobPath() == null ? "" : temp.getJobPath();
                                    path = JenkinsUtil.addSlash(path, "/|\\\\", "/", true);
                                    nowJobName = nowJobName + path + envName + "????????????(??????:" + jobRun.getJobRunNumber() + "),";
                                    break;
                                }
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(nowJobName)) {
                        nowJobName = nowJobName.substring(0, nowJobName.length() - 1);
                        sysInfomap.put("nowStatus", "true");// ????????????
                        sysInfomap.put("nowJobName", nowJobName);
                    } else {
                        sysInfomap.put("nowStatus", "false");// ????????????
                        sysInfomap.put("nowJobName", "");
                    }

                } else {
                    // ??????????????????

                    String envids = "";
                    //??????????????????
                    String packEnvids = "";
                    String nowEnvironmentType = "";
                    String environmentType = (String) sysInfomap.get("environmentType");
                    //if (deployType == 1) {
                    //????????????
                    List<TblSystemScm> buildstatus = tblSystemScmMapper.selectBuildingBySystemidDeploy(id);
                    //????????????
                    buildstatus = detailSystemSccms(buildstatus);


                    //?????????????????????
                    detailArtEnv(sysInfomap, environmentType, envMap, request);
                    // ??????????????????
                    Map<String, Object> clmap = new HashMap<>();
                    clmap.put("systemId", id);
                    clmap.put("createType", 1);
                    clmap.put("jobType", 2);
                    clmap.put("status", 1);
                    List<TblSystemJenkins> list = tblSystemJenkinsMapper.selectBreakName(clmap);


                    if ((buildstatus != null && buildstatus.size() > 0) || (list != null && list.size() > 0)) {
                        //??????
                        for (TblSystemScm t : buildstatus) {
                            String envName = envMap.get(t.getEnvironmentType().toString()).toString();
                            if (t.getDeployStatus() == 3) {
                                nowEnvironmentType = nowEnvironmentType + envName + "(??????)" + "?????????????????????,";
                            } else {
                                nowEnvironmentType = nowEnvironmentType + envName + "(??????)" + "????????????,";
                            }

                            envids = envids + t.getEnvironmentType().toString() + ",";
                        }
                        //nowEnvironmentType = nowEnvironmentType.substring(0, nowEnvironmentType.length() - 1);

                        //??????
                        for (TblSystemJenkins t : list) {
                            String envName = envMap.get(t.getEnvironmentType().toString()).toString();
                            if (t.getDeployStatus() == 3) {
                                nowEnvironmentType = nowEnvironmentType + envName + "(??????)" + "?????????????????????,";
                            } else {
                                nowEnvironmentType = nowEnvironmentType + envName + "(??????)" + "????????????,";
                            }
                            packEnvids = packEnvids + t.getEnvironmentType().toString() + ",";

                        }
                        nowEnvironmentType = nowEnvironmentType.substring(0, nowEnvironmentType.length() - 1);


                        sysInfomap.put("packEnvids", packEnvids);// ????????????
                        sysInfomap.put("nowStatus", "true");// ????????????
                        sysInfomap.put("nowEnvironmentType", nowEnvironmentType);
                        sysInfomap.put("envids", envids);
                    } else {
                        sysInfomap.put("packEnvids", "");// ????????????
                        sysInfomap.put("nowStatus", "false");// ????????????
                        sysInfomap.put("nowEnvironmentType", "");
                        sysInfomap.put("envids", "");
                    }
                    //				} else {
                    //					//?????????????????????
                    //					detailArtEnv(sysInfomap, tblSystemInfoEnvs.getEnvironmentType(), envMap,request);
                    //					// ??????????????????
                    //					Map<String, Object> clmap = new HashMap<>();
                    //					clmap.put("systemId", id);
                    //					clmap.put("createType", 1);
                    //					clmap.put("jobType", 2);
                    //					clmap.put("status", 1);
                    //					List<TblSystemJenkins> list = tblSystemJenkinsMapper.selectBreakName(clmap);
                    //					if (list != null && list.size() > 0) {
                    //						for (TblSystemJenkins t : list) {
                    //							String envName = envMap.get(t.getEnvironmentType().toString()).toString();
                    //							if (t.getDeployStatus() == 3) {
                    //								nowEnvironmentType = nowEnvironmentType + envName + "?????????????????????,";
                    //							} else {
                    //								nowEnvironmentType = nowEnvironmentType + envName + "????????????,";
                    //							}
                    //							envids = envids + t.getEnvironmentType().toString() + ",";
                    //
                    //						}
                    //						nowEnvironmentType = nowEnvironmentType.substring(0, nowEnvironmentType.length() - 1);
                    //						sysInfomap.put("nowStatus", "true");// ????????????
                    //						sysInfomap.put("nowEnvironmentType", nowEnvironmentType);
                    //						sysInfomap.put("envids", envids);// ?????????????????????
                    //					} else {
                    //						sysInfomap.put("nowStatus", "false");// ????????????
                    //						sysInfomap.put("nowEnvironmentType", "");
                    //						sysInfomap.put("envids", "");
                    //
                    //					}
                    //
                    //				}
                    // ?????????systemid???????????????
                    Map<String, Object> scmMap = new HashMap<>();
                    scmMap.put("SYSTEM_ID", id);
                    scmMap.put("status", 1);
                    // ????????????
                    String choiceEnvids = "";
                    List<TblSystemScm> tblSystemScms = tblSystemScmMapper.selectByMap(scmMap);
                    //????????????
                    tblSystemScms = detailSystemSccms(tblSystemScms);


                    for (TblSystemScm tblSystemScm : tblSystemScms) {
                        if (envids.indexOf(tblSystemScm.getEnvironmentType().toString()) == -1) {// ?????????
                            String envType = tblSystemScm.getEnvironmentType().toString();
                            //????????????????????????
                            boolean deployUser = judgeEnvByuser(request, id, envType);
                            if (judgeDeleteEnv("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE", envType)) {
                            } else {
                                String envName = envMap.get(envType).toString();
                                //????????????????????????system ?????????
                                if (environmentType != null && !environmentType.equals("")) {
                                    boolean systemEnvsFlag = judegeSystemEnvs(environmentType, envType);
                                    if (systemEnvsFlag && deployUser) {
                                        choiceEnvids = choiceEnvids + envType + ":" + envName + ",";
                                    }
                                }

                            }
                        }
                    }
                    if (!choiceEnvids.equals("")) {
                        choiceEnvids = choiceEnvids.substring(0, choiceEnvids.length() - 1);
                    }
                    sysInfomap.put("choiceEnvids", choiceEnvids);// ????????????
                }
                // ????????????????????????
                List<TblSystemJenkinsJobRun> jobrunlist = new ArrayList<>();
                if (createType.equals(Constants.CREATE_TYPE_AUTO)) {
                    if (deployType == 1) {// ??????????????????
                        jobrunlist = tblSystemJenkinsJobRunMapper.selectAutoDeployLastTimeBySystemId(id);
                    } else {// ????????????

                        jobrunlist = tblSystemJenkinsJobRunMapper.selectAutoDeployLastTimeBySystemIdArtifact(id);
                    }
                } else {// ??????
                    jobrunlist = tblSystemJenkinsJobRunMapper.selectLastTimeBySystemIdManualDeploy(id);
                }

                String lastBuildTime = "";
                String buildStatus = "";
                String status = "";
                String environmentType = "";
                String lastJobName = "";
                if (jobrunlist != null && jobrunlist.size() > 0) {

                    if (jobrunlist.get(0).getBuildStatus() == null) {
                        status = "?????????";
                        buildStatus = "4"; // ?????????
                    } else {
                        environmentType = jobrunlist.get(0).getEnvironmentType() + "";
                        buildStatus = jobrunlist.get(0).getBuildStatus() + "";
                    }
                    lastJobName = jobrunlist.get(0).getJobName();
                    if (jobrunlist.get(0).getEndDate() == null) {
                        lastBuildTime = "";
                    } else {
                        lastBuildTime = sdf.format(jobrunlist.get(0).getEndDate());
                    }
                } else {
                    buildStatus = "4";
                    lastBuildTime = "";
                    status = "?????????";
                }
                sysInfomap.put("lastJobName", lastJobName);
                sysInfomap.put("environmentType", environmentType);
                sysInfomap.put("lastBuildTime", lastBuildTime);
                sysInfomap.put("buildStatus", buildStatus);
                sysInfomap.put("status", status);
                sysInfomap.put("level", "0");
                if (refreshIds != null && Arrays.asList(refreshIds).contains(String.valueOf(id))) {
                    sysInfomap.put("expanded", "true");
                } else {
                    sysInfomap.put("expanded", "false");
                }
                if (checkIds != null && Arrays.asList(checkIds).contains(String.valueOf(id))) {
                    sysInfomap.put("check", "true");
                } else {
                    sysInfomap.put("check", "false");
                }
                sysInfomap.put("parent", "");
                sysInfomap.put("loaded", "true");
                sysInfomap.put("key_id", parent_id);
                sysInfomap.put("systemId", id);
                sysInfomap.put("deployType", deployType);
                if (sysInfomap.get("architectureType").toString().equals(Constants.SERVER_MICRO_TYPE)
                ) {// 1????????????
                    sysInfomap.put("isLeaf", "false");
                    endList.add(sysInfomap);
                    List<TblSystemModule> lists = this.selectSystemModule(id);// systemid???????????????

                    // ??????????????????
                    Map<String, Object> moduleParam = new HashMap<>();
                    moduleParam.put("systemId", id);
                    moduleParam.put("jobType", 2);//?????????2
                    List<Map<String, Object>> nowModuleMessages = tblSystemJenkinsJobRunMapper
                            .selectModuleBuildMessagesNowAutoDeployNews(moduleParam);


                    List<Map<String, Object>> moduleMessages = tblSystemJenkinsJobRunMapper
                            .selectModuleBuildMessageAutoDeployNews(moduleParam);


                    for (TblSystemModule tblSystemModule : lists) {
                        Map<String, Object> moduleData = new HashMap<>();
                        if (checkModuleIds != null && Arrays.asList(checkModuleIds).contains(String.valueOf(tblSystemModule.getId()))) {
                            moduleData.put("check", "true");
                        } else {
                            moduleData.put("check", "false");
                        }
                        moduleData.put("systemId", id);
                        moduleData.put("deployType", deployType);
                        moduleData.put("level", "1");
                        moduleData.put("isLeaf", "true");
                        moduleData.put("expanded", "false");
                        moduleData.put("parent", parent_id);
                        moduleData.put("loaded", "true");
                        moduleData.put("systemCode", tblSystemModule.getModuleCode());
                        moduleData.put("id", tblSystemModule.getId() + "");
                        moduleData.put("systemName", tblSystemModule.getModuleName());
                        List<Map<String, Object>> nowModuleMessage = selectModuleRunInfoByMaps(tblSystemModule.getId().toString(), nowModuleMessages);
                        String nowstatus = "false";
                        if (nowModuleMessage != null && nowModuleMessage.size() > 0) {
                            String nowEnvironmentType = "";
                            for (Map<String, Object> moumaps : nowModuleMessage) {
                                if (moumaps != null) {
                                    String buildStatusItmp = moumaps.get("buildStatus").toString();
                                    String jobName = moumaps.get("jobName").toString();
                                    if (buildStatusItmp.equals("1") || buildStatusItmp.equals("4")) {
                                        nowstatus = "true";
                                        String envName = envMap.get(moumaps.get("environmentType").toString()).toString();
                                        if (buildStatusItmp.equals("1")) {
                                            if (jobName.endsWith("_packagedeploy")) {
                                                nowEnvironmentType = nowEnvironmentType + envName + "(??????)" + "????????????,";
                                            } else {
                                                nowEnvironmentType = nowEnvironmentType + envName + "(??????)" + "????????????,";
                                            }
                                        } else {
                                            if (jobName.endsWith("_packagedeploy")) {
                                                nowEnvironmentType = nowEnvironmentType + envName + "(??????)" + "?????????????????????,";
                                            } else {
                                                nowEnvironmentType = nowEnvironmentType + envName + "(??????)" + "?????????????????????,";
                                            }
                                        }
                                    }

                                }

                            }
                            if (!nowEnvironmentType.equals("")) {
                                nowEnvironmentType = nowEnvironmentType.substring(0, nowEnvironmentType.length() - 1);
                            }
                            moduleData.put("nowStatus", nowstatus);// ????????????
                            moduleData.put("nowEnvironmentType", nowEnvironmentType);

                        } else {
                            moduleData.put("nowStatus", "false");// ????????????
                            moduleData.put("nowEnvironmentType", "");
                        }

                        // ??????????????????
                        // List<Map<String, Object>> moduleMessage = selectModuleRunInfoByMaps(tblSystemModule.getId().toString(),moduleMessages);
                        moduleParam.put("systemModuleId", tblSystemModule.getId().toString());
                        List<Map<String, Object>> moduleMessage = tblSystemJenkinsJobRunMapper
                                .selectModuleBuildMessageAutoDeployNewsByModuleId(moduleParam);
                        if (moduleMessage != null && moduleMessage.size() > 0) {
                            // ????????????
                            for (Map<String, Object> mapResult : moduleMessage) {
                                if (mapResult.get("endDate") != null && !mapResult.get("endDate").toString().equals("")) {
                                    moduleData.put("lastBuildTime", sdf.format(mapResult.get("endDate")));
                                    moduleData.put("buildStatus", mapResult.get("buildStatus"));
                                    moduleData.put("environmentType", mapResult.get("environmentType"));
                                    break;
                                } else {
                                    moduleData.put("lastBuildTime", "");
                                    moduleData.put("buildStatus", "4");
                                    moduleData.put("environmentType", "");
                                }

                            }

                        } else {
                            moduleData.put("lastBuildTime", "");
                            moduleData.put("buildStatus", "4");
                            moduleData.put("environmentType", "");
                        }

                        moduleData.put("createType", createType);
                        String child_id = "child_" + tblSystemModule.getId() + "";
                        moduleData.put("key_id", child_id);
                        moduleList.add(moduleData);
                        endList.add(moduleData);

                    }

                } else {
                    sysInfomap.put("isLeaf", "true");
                    endList.add(sysInfomap);
                }

            }
        }
        sysInfoList.addAll(moduleList);
        map.put("rows", endList);
        map.put("total", size.size());

        return map;
    }

    private List<Map<String, Object>> selectModuleRunInfoByMaps(String id, List<Map<String, Object>> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : list) {
            if (map != null && map.get("systemModuleId") != null) {
                String systemModuleId = map.get("systemModuleId").toString();
                if (systemModuleId.equals(id)) {
                    result.add(map);
                }
            }
        }
        return result;

    }

    public static long fromDateStringToLong(String inVal) {
        Date date = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        try {
            date = inputFormat.parse(inVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date != null) {
            return date.getTime();
        } else {
            return 0;
        }

    }

    /**
     * ??????true????????????false?????????
     */

    private boolean judgeEnvByuser(HttpServletRequest request, long systemId, String envType) {
        Long currentUserId = CommonUtil.getCurrentUserId(request);
        Map<String, Object> param = new HashMap<>();

        param.put("SYSTEM_ID", systemId);
        param.put("STATUS", 1);
        param.put("ENVIRONMENT_TYPE", envType);
        List<TblSystemDeployeUserConfig> tblSystemDeployeUserConfigs = tblSystemDeployeUserConfigMapper.selectByMap(param);
        if (tblSystemDeployeUserConfigs != null && tblSystemDeployeUserConfigs.size() > 0) {

            TblSystemDeployeUserConfig tblSystemDeployeUserConfig = tblSystemDeployeUserConfigs.get(0);
            String userIds = tblSystemDeployeUserConfig.getUserIds();
            if (userIds != null) {
                List<String> list = Arrays.asList(userIds.split(","));
                if (list.contains(currentUserId.toString())) {
                    return true;
                } else {
                    return false;
                }
            }

        }
        return true;
    }


    //??????????????????
    private void detailArtEnv(Map<String, Object> sysInfomap, String environmentType, Map<String, Object> envMap, HttpServletRequest request) {
//		Object redisEnvInfo = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
//		Map<String, Object> redisEnvInfoMap = JSON.parseObject(redisEnvInfo.toString());
        Long id = (Long) sysInfomap.get("id");
        String choiceAtrEnvids = "";
        if (environmentType != null) {
            String[] envs = environmentType.split(",");
            for (String env : envs) {
                boolean deployUser = judgeEnvByuser(request, id, env);

                if (judgeDeleteEnv("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE", env)) {

                } else {
                    if (deployUser) {
                        String envName = envMap.get(env).toString();
                        choiceAtrEnvids = choiceAtrEnvids + env + ":" + envName + ",";
                    }
                }
            }
        }
        if (!choiceAtrEnvids.equals("")) {
            choiceAtrEnvids = choiceAtrEnvids.substring(0, choiceAtrEnvids.length() - 1);
        }
        sysInfomap.put("choiceAtrEnvids", choiceAtrEnvids);
    }

    /**
     * ??????????????????????????????
     *
     * @param sysId
     * @param serverType ????????????
     * @param systemName ????????????
     * @param env        ????????????
     * @param modules    ?????????ids
     * @return Map<String, Object>
     */

    @Transactional
    @Override

    public Map<String, Object> buildJobAutoDeploy(HttpServletRequest request, String sysId, String serverType,
                                                  String env, String systemName, String modules) {
        Map<String, Object> result = new HashMap<>();
        long usrId = CommonUtil.getCurrentUserId(request);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Object redisEnvType = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
        Map<String, Object> envMap = JSON.parseObject(redisEnvType.toString());
        String envName = envMap.get(env).toString();
        TblSystemInfo tSystemInfo = tblSystemInfoMapper.getOneSystemInfo(Long.parseLong(sysId));

        List<String> modulesList = new ArrayList<>();
        String jobrun = "";
        Integer systemId = Integer.parseInt(sysId);
        // ??????????????????id
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("systemId", systemId);
        mapParam.put("environmentType", env);
        mapParam.put("status", 1);
        // ????????????scm????????? env???systemid????????????
        List<TblSystemScm> countList = this.getScmByParam(mapParam);
        if (countList == null || countList.size() == 0) {
            result.put("status", Constants.ITMP_RETURN_FAILURE);
            result.put("message", "?????????????????????????????????");
            return result;
        }
        TblSystemScm tblSystemScm = countList.get(0);
        // ????????????????????????????????????
        if (tblSystemScm.getDeployStatus() == 2) {
            result.put("status", Constants.ITMP_RETURN_FAILURE);
            result.put("message", "????????????????????????");
            return result;
        }

        JenkinsJobBuildStateQuery query = JenkinsJobBuildStateQuery.builder()
                .jobType(JenkinsJobBuildStateQuery.JOB_TYPE_DEPLOY)
                .systemScmId(tblSystemScm.getId())
                .timedTaskFlag(true)
                .systemId(systemId)
                .build();
        boolean isBuildding = jenkinsBuildStateService.isBuildding(query);
        if (isBuildding) {
            result.put("status", Constants.ITMP_RETURN_FAILURE);
            result.put("message", "?????????????????????????????????????????????????????????");
            return result;
        }

        if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {// ?????????
            String[] alModule = modules.split(",");
            for (int i = 0; i < alModule.length; i++) {
                // ??????????????????????????????
                modulesList = this.Judge(alModule[i], modulesList, tblSystemScm.getId(), systemId);
            }
            // ?????????????????????????????????????????????
            if (modulesList.size() == 0) {
                result.put("status", Constants.ITMP_RETURN_FAILURE);
                result.put("message", "???????????????????????????");
                return result;
            }
        }
        //?????????module
        List<String> checkModuleList = new ArrayList<>();
        checkModuleList.addAll(modulesList);
        result.put("checkModuleList", checkModuleList);
        if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {
            modulesList = this.sortAndDetailModuleid(modulesList, tblSystemScm.getId(), systemId);
        }
        // ?????????????????????????????????
        TblSystemScm systemScmUpdate = tblSystemScm;
        systemScmUpdate.setId(tblSystemScm.getId());
        systemScmUpdate.setDeployStatus(2);// 2????????????
        systemScmUpdate.setLastUpdateDate(timestamp);
        systemScmUpdate.setLastUpdateBy(usrId);
        this.updateSystemScmBuildStatus(systemScmUpdate);
        String jobname = tSystemInfo.getSystemCode() + "_" + env + "_" + String.valueOf(systemId) + "_deploy";
        // ???????????????sysid???env ??? TBL_SYSTEM_JENKINS??????????????????????????????????????????
        TblSystemJenkins tblSystemJenkins = new TblSystemJenkins();
        tblSystemJenkins.setCreateDate(timestamp);
        tblSystemJenkins.setJobName(jobname);
        tblSystemJenkins.setRootPom("./");
        tblSystemJenkins.setSystemId((long) systemId);
        tblSystemJenkins.setSystemScmId(tblSystemScm.getId());
        tblSystemJenkins.setStatus(1);// 1??????2??????
        tblSystemJenkins.setGoalsOptions("mvn clean deploy -Dmaven.test.skip=true");
        tblSystemJenkins.setCreateBy(usrId);
        tblSystemJenkins.setJobType(2);
        tblSystemJenkins.setBuildStatus(2);// 2?????????
        tblSystemJenkins.setCreateType(1);
        long jconfId = this.creOrUpdSjenkins(tblSystemJenkins, env, "1", "2");
        // ??????toolid
        tblSystemJenkins = tblSystemJenkinsMapper.selectById(jconfId);
        TblToolInfo jenkinsTool = this.geTblToolInfo(tblSystemJenkins.getToolId());
        // ??????build?????????
        long systemScmId = tblSystemScm.getId();
        List<TblSystemModuleScm> tblSystemModuleScmList = new ArrayList<>();
        List<TblSystemModuleJenkinsJobRun> moduleRunJobList = new ArrayList<>();
        TblSystemModuleJenkinsJobRun resultModuleRun = new TblSystemModuleJenkinsJobRun();
        List<TblSystemModule> tblSystemModuleList = new ArrayList<>();
        if (jconfId > 0) {
            // TBL_SYSTEM_JENKINS_JOB_RUN ??????
            TblSystemJenkinsJobRun ttr = new TblSystemJenkinsJobRun();
            ttr.setSystemJenkinsId((long) jconfId);
            ttr.setSystemId((long) systemId);
            ttr.setJobName(jobname);
            ttr.setRootPom(".");
            ttr.setBuildStatus(1);// ??????
            ttr.setStartDate(timestamp);
            ttr.setStatus(1);
            ttr.setCreateDate(timestamp);
            ttr.setEnvironmentType(Integer.parseInt(env));
            ttr.setGoalsOptions("mvn clean install -Dmaven.test.skip=true");
            ttr.setCreateBy(usrId);
            ttr.setCreateType(1);
            ttr.setJobType(2);// ??????


            try {
                int jobNumber = iJenkinsBuildService.getNextBuildNumber(jenkinsTool, tblSystemJenkins, jobname);
                result.put("jobNumber", jobNumber);
                ttr.setJobRunNumber(jobNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }


            long jobid = this.insertJenkinsJobRun(ttr);// jobId jenkins???????????????id
            jobrun = jobid + "";
            if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {// ?????????1?????????module???
                for (String id : modulesList) {// ????????????
                    TblSystemModuleJenkinsJobRun tj = new TblSystemModuleJenkinsJobRun();
                    tj.setSystemJenkinsJobRun(jobid);
                    tj.setSystemModuleId((long) Integer.parseInt(id));
                    tj.setCreateDate(timestamp);
                    tj.setStatus(1);// ??????
                    tj.setCreateBy(usrId);
                    tj.setSystemId((long) systemId);
                    tj.setSystemScmId(systemScmId);
                    tj.setJobName(jobname);
                    tj.setCreateType(1);// 1????????????
                    tj.setJobType(2); // ??????
                    tj.setBuildStatus(1);
                    long moduleRunId = this.insertJenkinsModuleJobRun(tj);
                    // ????????????
                    TblSystemModuleJenkinsJobRun mrid = this.selectByModuleRunId(moduleRunId);
                    moduleRunJobList.add(mrid);
                    tblSystemModuleList.add(this.getTblsystemModule((long) Integer.parseInt(id)));
                    Map<String, Object> moduleParam = new HashMap<>();
                    moduleParam.put("systemId", systemId);
                    // ??????
                    moduleParam.put("systemScmId", systemScmId);
                    moduleParam.put("status", 1);
                    moduleParam.put("systemModuleId", id);
                    List<TblSystemModuleScm> scmList = this.getModuleScmByParam(moduleParam);
                    if (scmList != null && scmList.size() > 0) {
                        tblSystemModuleScmList.add(scmList.get(0));
                    }

                }

            } else {
                // ????????????????????????modulejobrun????????????sonar????????????
                TblSystemModuleJenkinsJobRun tj = new TblSystemModuleJenkinsJobRun();
                tj.setSystemJenkinsJobRun(jobid);
                tj.setSystemScmId(systemScmId);
                tj.setSystemId((long) systemId);
                tj.setCreateDate(timestamp);
                tj.setStatus(1);// ??????
                tj.setCreateBy(usrId);
                tj.setJobName(jobname);
                tj.setCreateType(1);
                tj.setJobType(2);// ????????????
                tj.setBuildStatus(1);
                Long trModuleRunId = this.insertJenkinsModuleJobRun(tj);
                resultModuleRun = this.selectByModuleRunId(trModuleRunId);
            }

        }
        TblToolInfo sourceTool = new TblToolInfo();

        // ??????svntoolinfo
        if (tblSystemScm.getToolId() != null) {
            sourceTool = this.geTblToolInfo(tblSystemScm.getToolId());
        }
        // ??????serverdeploy??????
        this.getDeploy(checkModuleList, env, sysId, result);
        result.put("moduleList", modulesList);
        result.put("resultModuleRun", resultModuleRun);
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        result.put("serverType", serverType);
        tSystemInfo.setDeployType(1);
        result.put("tSystemInfo", tSystemInfo);
        result.put("tblSystemScm", tblSystemScm);
        result.put("tblSystemModuleList", tblSystemModuleList);
        result.put("tblSystemModuleScmList", tblSystemModuleScmList);

        if (tblSystemModuleScmList.size() > 0) {
            List<TblToolInfo> sourceToolList = new ArrayList<>();
            //?????????
            for (TblSystemModuleScm tms : tblSystemModuleScmList) {
                sourceToolList.add(tblToolInfoMapper.selectByPrimaryKey(tms.getToolId()));
            }
            result.put("sourceToolList", sourceToolList);
        }

        result.put("moduleAllList", tblSystemModuleMapper.selectSystemModule(tSystemInfo.getId()));
        result.put("tblSystemJenkins", tblSystemJenkins);
        result.put("jenkinsTool", jenkinsTool);
        result.put("sourceTool", sourceTool);
        result.put("jobrun", jobrun);
        result.put("moduleRunJobList", moduleRunJobList);
        result.put("envName", envName);


        return result;
    }

    //??????????????????scm?????????
    public List<TblSystemScm> getScmByParam(Map<String, Object> map) {

        try {
            return tblSystemScmMapper.getScmByParam(map);
        } catch (Exception e) {
            this.handleException(e);
            throw e;
        }

    }

    /**
     * ??????????????????jenkins?????????
     *
     * @param env ??????
     * @return List<TblToolInfo>
     */
    private List<TblToolInfo> getJenkinsByEnv(String env) {
        Map<String, Object> jenkinsParam = new HashMap<>();
        jenkinsParam.put("toolType", 4);
        jenkinsParam.put("environmentType", env);
        jenkinsParam.put("status", 1);
        List<TblToolInfo> jenktools = tblToolInfoMapper.selectToolByEnv(jenkinsParam);
        return jenktools;
    }

    /**
     * ?????????????????????????????????????????????TblSystemJenkins ???????????????????????????????????????????????????
     *
     * @param env        ??????
     * @param createType ????????????
     * @param jobType    ????????????
     * @return long
     */
    public long creOrUpdSjenkins(TblSystemJenkins tblSystemJenkins, String env, String createType, String jobType) {
        // ??????????????????systemid ??????
        try {
            Map<String, Object> mapParam = new HashMap<>();
            mapParam.put("system_Id", tblSystemJenkins.getSystemId());
            if (tblSystemJenkins.getSystemScmId() != null) {
                mapParam.put("system_Scm_Id", tblSystemJenkins.getSystemScmId());

            } else {
                mapParam.put("ENVIRONMENT_TYPE", tblSystemJenkins.getEnvironmentType());
            }
            mapParam.put("status", 1);
            mapParam.put("job_Type", jobType);
            mapParam.put("CREATE_TYPE", createType);
            List<TblSystemJenkins> list = tblSystemJenkinsMapper.selectByMap(mapParam);
            if (list != null && list.size() > 0) {
                TblSystemJenkins tblSystemJenkinsTemp = list.get(0);
                //?????????
                tblSystemJenkins.setToolId(tblSystemJenkinsTemp.getToolId());
                tblSystemJenkins.setId(tblSystemJenkinsTemp.getId());
                String jobNameFlag = "";
                jobNameFlag = tblSystemJenkinsTemp.getJobName();
                if (jobNameFlag == null || jobNameFlag.equals("")) {// ?????????????????????
                    if (tblSystemJenkinsTemp.getToolId() == null) {
                        List<TblToolInfo> jenktools = getJenkinsByEnv(env);
                        // ??????????????????
                        int n = this.getRandom(jenktools.size());
                        TblToolInfo tblToolInfo = jenktools.get(n);
                        tblSystemJenkins.setToolId(tblToolInfo.getId());
                    }
                    tblSystemJenkins.setLastUpdateDate(tblSystemJenkins.getCreateDate());
                    tblSystemJenkins.setLastUpdateBy(tblSystemJenkins.getCreateBy());
                    tblSystemJenkinsMapper.updateById(tblSystemJenkins);
                    return tblSystemJenkins.getId();
                } else {
                    if (tblSystemJenkins.getSystemScmId() == null) {//????????????
                        tblSystemJenkinsTemp.setDeployStatus(2);

                    }
                    //?????????toolid??????????????????
                    TblToolInfo netJenkinsInfo = tblToolInfoMapper.selectByPrimaryKey(tblSystemJenkinsTemp.getToolId());
                    if (netJenkinsInfo.getEnvironmentType() != null &&
                            netJenkinsInfo.getStatus() == 1 && judgEnv(netJenkinsInfo.getEnvironmentType(), env)) {

                    } else {
                        //????????????jenkins
                        List<TblToolInfo> jenktools = getJenkinsByEnv(env);
                        int n = this.getRandom(jenktools.size());
                        TblToolInfo tblToolInfo = jenktools.get(n);
                        tblSystemJenkinsTemp.setToolId(tblToolInfo.getId());
                    }
                    tblSystemJenkinsTemp.setLastUpdateDate(tblSystemJenkins.getCreateDate());
                    tblSystemJenkinsTemp.setLastUpdateBy(tblSystemJenkins.getCreateBy());
                    tblSystemJenkinsTemp.setJobName(tblSystemJenkins.getJobName());
                    tblSystemJenkinsMapper.updateById(tblSystemJenkinsTemp);
                    return tblSystemJenkinsTemp.getId();
                }

            } else {
                // ????????????tool
                List<TblToolInfo> jenktools = getJenkinsByEnv(env);
                int n = this.getRandom(jenktools.size());
                TblToolInfo tblToolInfo = jenktools.get(n);
                tblSystemJenkins.setToolId(tblToolInfo.getId());
                tblSystemJenkinsMapper.insertNew(tblSystemJenkins);
                return tblSystemJenkins.getId();
            }
        } catch (Exception e) {
            this.handleException(e);
            throw e;
        }

    }

    private List<TblSystemModuleScm> judge(String moduleid, Long scmId, Integer systemId) {
        Map<String, Object> map = new HashMap<>();
        map.put("systemId", systemId);
        map.put("systemScmId", scmId);
        map.put("systemModuleId", moduleid);
        map.put("status", 1);
        return tblSystemModuleScmMapper.judge(map);
    }

    public void updateSystemScmBuildStatus(TblSystemScm t) {
        tblSystemScmMapper.updateByPrimaryKeySelective(t);

    }

    private List<String> Judge(String moduleid, List<String> modulesList, Long scmId, Integer systemId) {
        // ?????????moduleid??????????????????
        List<TblSystemModuleScm> moduleScmList = this.judge(moduleid, scmId, systemId);
        if (moduleScmList != null && moduleScmList.size() > 0) {
            /* modulesList.add(String.valueOf(moduleScmList.get(0).getId())); */
            modulesList.add(moduleid);
        }

        return modulesList;
    }

    public TblToolInfo geTblToolInfo(long id) {
        try {
            return tblToolInfoMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            this.handleException(e);
            throw e;
        }
    }

    public long insertJenkinsJobRun(TblSystemJenkinsJobRun tblSystemJenkinsJobRun) {

        try {
            tblSystemJenkinsJobRunMapper.insertNew(tblSystemJenkinsJobRun);

            return tblSystemJenkinsJobRun.getId();
        } catch (Exception e) {
            this.handleException(e);
            throw e;
        }
    }

    public long insertJenkinsModuleJobRun(TblSystemModuleJenkinsJobRun tblSystemModuleJenkinsJobRun) {
        try {
            tblSystemModuleJenkinsJobRunMapper.insertNew(tblSystemModuleJenkinsJobRun);
            return tblSystemModuleJenkinsJobRun.getId();
        } catch (Exception e) {
            this.handleException(e);
            throw e;
        }
    }

    //???????????????????????????sonar??????
    public Long getRandomSonarId(String env) {
        // ???????????????sonarid
        Map<String, Object> sonarParam = new HashMap<>();
        sonarParam.put("toolType", 3);
        sonarParam.put("environmentType", env);
        List<TblToolInfo> sonartools = tblToolInfoMapper.selectToolByEnv(sonarParam);
        int n = this.getRandom(sonartools.size());
        return sonartools.get(n).getId();
    }

    private TblSystemSonar assembleSonar(Integer systemId, String env, TblSystemInfo tSystemInfo,
                                         TblSystemScm tblSystemScm, TblSystemModule tblSystemModule, Timestamp timestamp, long usrId,
                                         long randomSonar, long systemJenkinsId) {
        // insert Jenkins????????????sonar?????????
        try {
            TblSystemSonar tblSystemSonar = new TblSystemSonar();
            tblSystemSonar.setCreateDate(timestamp);
            String projectKey = "";
            if (tblSystemModule == null) {// ????????????
                projectKey = tSystemInfo.getSystemCode() + "_" + env + "_deploy";
            } else {// ???????????????
                projectKey = tSystemInfo.getSystemCode() + "_" + tblSystemModule.getModuleCode() + "_" + env
                        + "_deploy";
                tblSystemSonar.setSystemModuleId(tblSystemModule.getId());

            }
            tblSystemSonar.setSonarProjectKey(projectKey);
            tblSystemSonar.setSystemJenkinsId(systemJenkinsId);
            tblSystemSonar.setSonarProjectName(projectKey);
            tblSystemSonar.setStatus(1);// ??????
            tblSystemSonar.setSonarJavaBinaries(".");
            tblSystemSonar.setSystemScmId(tblSystemScm.getSystemId());
            tblSystemSonar.setSonarSources(".");
            tblSystemSonar.setSystemScmId(tblSystemScm.getId());
            tblSystemSonar.setSystemId((long) systemId);
            tblSystemSonar.setCreateBy(usrId);
            tblSystemSonar = this.creOrUpdSonar(tblSystemSonar, randomSonar);
            return tblSystemSonar;
        } catch (Exception e) {
            this.handleException(e);
            throw e;
        }
    }

    //??????????????????????????????TblSystemSonar??????
    public TblSystemSonar creOrUpdSonar(TblSystemSonar tblSystemSonar, long sonarId) {

        try {
            TblSystemSonar sonarTemp = new TblSystemSonar();
//
//			sonarTemp.setSystemId(tblSystemSonar.getSystemId());
//			sonarTemp.setSystemScmId(tblSystemSonar.getSystemScmId());
//			sonarTemp.setSystemModuleId(tblSystemSonar.getSystemModuleId());
            // sonarTemp = tblSystemSonarMapper.selectOne(sonarTemp);

            Map<String, Object> mapParam = new HashMap<>();
            mapParam.put("systemId", tblSystemSonar.getSystemId());
            mapParam.put("systemScmId", tblSystemSonar.getSystemScmId());
            mapParam.put("systemJenkinsId", tblSystemSonar.getSystemJenkinsId());

            mapParam.put("systemModuleId", tblSystemSonar.getSystemModuleId());
            List<TblSystemSonar> sonarTemps = tblSystemSonarMapper.selectByMapLimit(mapParam);

            if (sonarTemps != null && sonarTemps.size() > 0) {

                sonarTemp = sonarTemps.get(0);
                sonarTemp.setLastUpdateDate(tblSystemSonar.getCreateDate());
                sonarTemp.setLastUpdateBy(tblSystemSonar.getCreateBy());
                tblSystemSonarMapper.updateById(sonarTemp);
                // ?????????
                return sonarTemp;
            } else {
                // ????????????
                tblSystemSonar.setToolId(sonarId);
                tblSystemSonarMapper.insertNew(tblSystemSonar);
                return tblSystemSonar;
            }
        } catch (Exception e) {
            this.handleException(e);
            throw e;
        }
    }

    public TblSystemModule getTblsystemModule(long id) {

        return tblSystemModuleMapper.selectByPrimaryKey(id);
    }

    public TblSystemModuleJenkinsJobRun selectByModuleRunId(long id) {

        return tblSystemModuleJenkinsJobRunMapper.selectById(id);
    }

    public List<TblSystemModuleScm> getModuleScmByParam(Map<String, Object> map) {

        return tblSystemModuleScmMapper.getModuleScmByParam(map);
    }

    public List<TblSystemSonar> getSonarByMap(Map<String, Object> columnMap) {
        return tblSystemSonarMapper.selectByMap(columnMap);
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param modules  ?????????
     * @param env      ??????
     * @param systemId ??????id
     * @return
     */
    private void getDeploy(List<String> modules, String env, String systemId, Map<String, Object> result) {
        List<TblSystemDeploy> serverDeploys = new ArrayList<>();
        List<TblSystemDeployScript> tblSystemDeployScriptList = new ArrayList<>();
        if (modules.size() == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("ENVIRONMENT_TYPE", env);
            map.put("SYSTEM_ID", systemId);
            map.put("status", 1);
            List<TblSystemDeploy> list = tblSystemDeployMapper.selectByMap(map);
            if (list.size() == 0) {

            } else {
                serverDeploys.add(list.get(0));
            }

        } else {

            for (String id : modules) {
                Map<String, Object> map = new HashMap<>();
                map.put("ENVIRONMENT_TYPE", env);
                map.put("SYSTEM_ID", systemId);
                map.put("SYSTEM_MODULE_ID", id);
                map.put("status", 1);
                List<TblSystemDeploy> list = tblSystemDeployMapper.selectByMap(map);
                if (list.size() > 0) {
                    serverDeploys.add(list.get(0));
                }
            }
//			EntityWrapper<TblSystemDeploy> wrapper = new EntityWrapper<TblSystemDeploy>();
//			wrapper.eq("ENVIRONMENT_TYPE", env);
//			wrapper.eq("SYSTEM_ID", systemId);
//			wrapper.in("SYSTEM_MODULE_ID", modules);
//			wrapper.orderBy("DEPLOY_SEQUENCE,SYSTEM_MODULE_ID");
//			serverDeploys = tblSystemDeployMapper.selectList(wrapper);
        }
        result.put("tblSystemDeployList", serverDeploys);
        List<String> ids = new ArrayList<>();

        for (TblSystemDeploy tblSystemDeploy : serverDeploys) {
            String serverIds = tblSystemDeploy.getServerIds();
            String[] array = serverIds.split(",");
            for (int i = 0; i < array.length; i++) {
                if (!ids.contains(array[i])) {
                    ids.add(array[i]);
                }
            }

            //??????????????????
            List<TblSystemDeployScript> scripts = tblSystemDeployScriptMapper.selectScriptOrder(tblSystemDeploy.getId());
            tblSystemDeployScriptList.addAll(scripts);
        }
        result.put("tblSystemDeployScriptList", tblSystemDeployScriptList);
        Map<String, Object> serverMap = new HashMap<>();
        serverMap.put("ids", ids);
        List<TblServerInfo> serverList = new ArrayList<>();
        if (ids.size() == 0) {

        } else {
            serverList = tblServerInfoMapper.selectByIds(serverMap);
        }

        result.put("tblServerInfoList", serverList);

        // return serverDeploys;
    }

    /**
     * ????????????????????????
     *
     * @param id   ??????id
     * @param pageNumber
     * @param pageSize
     * @param jobType    ????????????
     * @param jenkinsId  ??????JENKINS?????????id
     * @param envType    ????????????
     * @param flag       ????????????
     * @return Map<String, Object>
     * @author weiji
     */

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> selectMessageBySystemIdAndPage(long id, Integer pageNumber, Integer pageSize,
                                                                    String type, Integer jobType, String jenkinsId, String envType, String flag) {
        Map<String, Object> mapParam = new HashMap<>();
        int start = (pageNumber - 1) * pageSize;
        mapParam.put("start", start);
        mapParam.put("pageSize", pageSize);
        mapParam.put("systemId", id);
        mapParam.put("createType", type);
        mapParam.put("jobType", jobType);

        if (flag.equals("2")) {
            //??????type???2??????????????????
            if (type.equals("2")) {
                mapParam.put("systemJenkinsId", jenkinsId);
            } else {
                mapParam.put("envType", envType);
            }
        }
        List<Map<String, Object>> maps = tblSystemJenkinsJobRunMapper
                .selectMessageBySystemIdAndPageAutoDeploy(mapParam);
        return maps;
    }

    /**
     * ??????????????????????????????
     *
     * @param jobRunId   ??????JENKINS???????????????id
     * @param createType ????????????
     * @return Map<String, Object>
     * @author weiji
     */
    @Override
    public Map<String, Object> getDeployMessageById(String jobRunId, String createType) {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Map<String, Object> map = new HashMap<>();
        try {
            TblSystemJenkinsJobRun jorRun = tblSystemJenkinsJobRunMapper.selectById(Long.parseLong(jobRunId));
            //???????????????????????????
            TblSystemInfo tblSystemInfo = tblSystemInfoMapper.selectById(jorRun.getSystemId());
            if (tblSystemInfo.getDeployType() != null) {
                map.put("deployType", tblSystemInfo.getDeployType());
            }


            //String text= jorRun.getBuildLogs();


            String s3 = jorRun.getBuildLogs();
            String text = "";
            try {
                text = s3Util.getStringByS3(s3.split(",")[1], s3.split(",")[0]);
            } catch (Exception e) {
                this.handleException(e);
            }


            map.put("content", jorRun);
            map.put("buildLogs", text);
            map.put("buildStatus", jorRun.getStatus());
            if (jorRun.getStartDate() != null) {
                map.put("startDate", sdf.format(jorRun.getStartDate()));
            } else {
                map.put("startDate", "");
            }

            if (jorRun.getEndDate() != null) {
                map.put("endDate", sdf.format(jorRun.getEndDate()));
            } else {
                map.put("endDate", "");
            }
            List<Map<String, Object>> list = new ArrayList<>();
            if (createType.equals(Constants.CREATE_TYPE_AUTO) && !jorRun.getJobName().contains("packagedeploy")) {//??????jobrun??????????????????????????????
                // ????????????????????????sonar??????
                //list = tblSystemModuleJenkinsJobRunMapper.selectModuleJobRunByjobRunId(Long.parseLong(jobRunId)); ????????????sonar????????????
            } else {
            }
            String ybug = "";
            String yVulnerabilities = "";
            String yCodeSmells = "";
            String yCoverage = "";
            String yduplications = "";
            String xValue = "";

            List<Map<String, Object>> maps = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (Map<String, Object> trs : list) {
                    Map<String, Object> resultMap = new HashMap<>();
                    if (trs.get("SONAR_BUGS") != null) {
                        ybug = trs.get("SONAR_BUGS").toString();
                    }
                    if (trs.get("SONAR_VULNERABILITIES") != null) {
                        yVulnerabilities = trs.get("SONAR_VULNERABILITIES").toString();
                    }
                    if (trs.get("SONAR_CODE_SMELLS") != null) {
                        yCodeSmells = trs.get("SONAR_CODE_SMELLS").toString();
                    }

                    if (trs.get("SONAR_DUPLICATIONS") != null) {
                        yduplications = trs.get("SONAR_DUPLICATIONS").toString();
                    }

                    if (trs.get("SONAR_COVERAGE") != null) {
                        yCoverage = trs.get("SONAR_COVERAGE").toString();
                    }

                    if (trs.get("LAST_UPDATE_DATE") != null) {
                        xValue = sdf.format(trs.get("LAST_UPDATE_DATE"));
                    }
                    Map<String, Object> sonarQueryMap = new HashMap<>();

                    if (createType.equals(Constants.CREATE_TYPE_AUTO)) {// ????????????
                        // ?????????
                        if (trs.get("SYSTEM_MODULE_ID") != null) {
                            TblSystemModule tModule = this
                                    .getTblsystemModule(Long.parseLong(trs.get("SYSTEM_MODULE_ID").toString()));
                            resultMap.put("moduleame", tModule.getModuleName());
                            sonarQueryMap.put("SYSTEM_MODULE_ID", trs.get("SYSTEM_MODULE_ID"));

                        }
                        sonarQueryMap.put("SYSTEM_ID", trs.get("SYSTEM_ID"));

                        // ???projectkey??????

                        sonarQueryMap.put("SYSTEM_SCM_ID", trs.get("SYSTEM_SCM_ID"));

                    } else {

                        // ????????????
                        long systemJenkinsId = jorRun.getSystemJenkinsId();
                        sonarQueryMap.put("SYSTEM_ID", trs.get("SYSTEM_ID"));
                        sonarQueryMap.put("SYSTEM_JENKINS_ID", systemJenkinsId);

                    }

                    List<TblSystemSonar> sonarList = this.selectSonarByMap(sonarQueryMap);
                    String projectKey = sonarList.get(0).getSonarProjectKey();
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String projectDateTime = "";
                    if (trs.get("LAST_UPDATE_DATE") != null) {
                        projectDateTime = format.format(trs.get("LAST_UPDATE_DATE"));
                        projectDateTime = projectDateTime.replace(" ", "T");
                        projectDateTime = projectDateTime + "+0800";
                    }

                    resultMap.put("projectKey", projectKey);
                    resultMap.put("projectDateTime", projectDateTime);

                    resultMap.put("toolId", sonarList.get(0).getToolId());

                    resultMap.put("bug", ybug);
                    resultMap.put("Vulnerabilities", yVulnerabilities);
                    resultMap.put("CodeSmells", yCodeSmells);
                    resultMap.put("duplications", yduplications + "%");
                    resultMap.put("Coverage", yCoverage + "%");
                    resultMap.put("xValue", xValue);
                    maps.add(resultMap);

                }

            }
            //??????test???
            getTestResult(map, jobRunId, jorRun.getSystemId().toString());
            map.put("list", maps);

            return map;
        } catch (NumberFormatException e) {
            this.handleException(e);
            throw e;
        }
    }

    //??????sonar??????
    public List<TblSystemSonar> selectSonarByMap(Map<String, Object> columnMap) {

        if (columnMap.get("SYSTEM_ID") != null && columnMap.get("SYSTEM_ID").equals("")) {
            TblSystemModule tr = new TblSystemModule();

            tr.setId(Long.parseLong(columnMap.get("SYSTEM_MODULE_ID").toString()));
            tr = tblSystemModuleMapper.selectOne(tr);
            columnMap.put("SYSTEM_ID", tr.getSystemId());
        }
        return tblSystemSonarMapper.selectByMap(columnMap);
    }


    /**
     * ??????????????????????????????????????????
     *
     * @param sysId            ??????id
     * @param serverType       ????????????
     * @param env              ??????
     * @param systemName       ????????????
     * @param tblSystemJenkins
     * @return Map<String, Object>
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> buildJobAutoDeployScheduled(HttpServletRequest request, String sysId, String serverType,
                                                           String env, String systemName, TblSystemJenkins tblSystemJenkins) {

        Map<String, Object> result = new HashMap<>();
        long usrId = CommonUtil.getCurrentUserId(request);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Object redisEnvType = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
        Map<String, Object> envMap = JSON.parseObject(redisEnvType.toString());
        String envName = envMap.get(env).toString();
        TblSystemInfo tSystemInfo = tblSystemInfoMapper.getOneSystemInfo(Long.parseLong(sysId));
        List<String> modulesList = new ArrayList<>();
        String jobrun = "";
        Integer systemId = Integer.parseInt(sysId);
        // ??????????????????id
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("systemId", systemId);
        mapParam.put("environmentType", env);
        mapParam.put("status", 1);
        // ????????????scm????????? env???systemid????????????
        List<TblSystemScm> countList = this.getScmByParam(mapParam);
        if (countList == null || countList.size() == 0) {
            result.put("status", Constants.ITMP_RETURN_FAILURE);
            result.put("message", "?????????????????????????????????");
            return result;
        }
        TblSystemScm tblSystemScm = countList.get(0);
        // ????????????????????????????????????
//		if (tblSystemScm.getBuildStatus() == 2) {
//			result.put("status", Constants.ITMP_RETURN_FAILURE);
//			result.put("message", "?????????????????????????????????");
//			return result;
//		}

        // ????????????????????????module
        List<TblSystemModule> allmoduleList = this.selectSystemModule(Long.parseLong(sysId));
        String modules = "";

        if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {// ?????????
            for (TblSystemModule tblSystemModule : allmoduleList) {
                modules = modules + tblSystemModule.getId() + ",";
            }

            String[] alModule = modules.split(",");
            for (int i = 0; i < alModule.length; i++) {
                // ??????????????????????????????
                modulesList = this.Judge(alModule[i], modulesList, tblSystemScm.getId(), systemId);
            }

            // ?????????????????????????????????????????????
//			if (modulesList.size() == 0) {
//				result.put("status", Constants.ITMP_RETURN_FAILURE);
//				result.put("message", "?????????????????????????????????");
//				return result;
//			}

        }
        // ??????
        if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {
            modulesList = this.sortAndDetailModuleid(modulesList, tblSystemScm.getId(), systemId);
        }
        // String jobname = tSystemInfo.getSystemCode() + "_" + envName + "_" +
        // String.valueOf(systemId)+"_"+"deployScheduled";
        String jobname = tblSystemJenkins.getCronJobName();

        long jconfId = tblSystemJenkins.getId();
        TblToolInfo jenkinsTool = new TblToolInfo();
        // ??????toolid
        jenkinsTool = this.geTblToolInfo(tblSystemJenkins.getToolId());
        // ??????build?????????
        long systemScmId = tblSystemScm.getId();
        List<TblSystemModuleScm> tblSystemModuleScmList = new ArrayList<>();
        List<TblSystemModuleJenkinsJobRun> moduleRunJobList = new ArrayList<>();
        TblSystemModuleJenkinsJobRun resultModuleRun = new TblSystemModuleJenkinsJobRun();
        List<TblSystemModule> tblSystemModuleList = new ArrayList<>();
        if (jconfId > 0) {
            // TBL_SYSTEM_JENKINS_JOB_RUN ??????
            TblSystemJenkinsJobRun ttr = new TblSystemJenkinsJobRun();
            ttr.setSystemJenkinsId((long) jconfId);
            ttr.setSystemId((long) systemId);
            ttr.setJobName(jobname);
            ttr.setRootPom(".");
            ttr.setBuildStatus(1);// ??????
            ttr.setStartDate(timestamp);
            ttr.setStatus(1);
            ttr.setCreateDate(timestamp);
            ttr.setEnvironmentType(Integer.parseInt(env));
            ttr.setGoalsOptions("mvn clean install -Dmaven.test.skip=true");
            ttr.setCreateBy(usrId);
            ttr.setCreateType(1);
            ttr.setJobType(2);// ????????????
            try {
                int jobNumber = iJenkinsBuildService.getNextBuildNumber(jenkinsTool, tblSystemJenkins, jobname);
                result.put("jobNumber", jobNumber);
                ttr.setJobRunNumber(jobNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long jobid = this.insertJenkinsJobRun(ttr);// jobId jenkins???????????????id
            jobrun = jobid + "";
            if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {// ?????????1?????????module???
                for (String id : modulesList) {// ????????????
                    TblSystemModuleJenkinsJobRun tj = new TblSystemModuleJenkinsJobRun();
                    tj.setSystemJenkinsJobRun(jobid);
                    tj.setSystemModuleId((long) Integer.parseInt(id));
                    tj.setCreateDate(timestamp);
                    tj.setStatus(1);// ??????
                    tj.setCreateBy(usrId);
                    tj.setSystemId((long) systemId);
                    tj.setSystemScmId(systemScmId);
                    tj.setJobName(jobname);
                    tj.setCreateType(1);// 1????????????
                    tj.setJobType(2);// ????????????

                    long moduleRunId = this.insertJenkinsModuleJobRun(tj);
                    // ????????????
                    TblSystemModuleJenkinsJobRun mrid = this.selectByModuleRunId(moduleRunId);
                    moduleRunJobList.add(mrid);
                    tblSystemModuleList.add(this.getTblsystemModule((long) Integer.parseInt(id)));
                    Map<String, Object> moduleParam = new HashMap<>();
                    moduleParam.put("systemId", systemId);
                    // ??????
                    moduleParam.put("systemScmId", systemScmId);
                    moduleParam.put("systemModuleId", id);
                    moduleParam.put("status", 1);
                    List<TblSystemModuleScm> scmList = this.getModuleScmByParam(moduleParam);
                    if (scmList != null && scmList.size() > 0) {
                        tblSystemModuleScmList.add(scmList.get(0));
                    }

                }

            } else {
                // ????????????????????????modulejobrun????????????sonar????????????
                TblSystemModuleJenkinsJobRun tj = new TblSystemModuleJenkinsJobRun();
                tj.setSystemJenkinsJobRun(jobid);
                tj.setSystemScmId(systemScmId);
                tj.setSystemId((long) systemId);
                tj.setCreateDate(timestamp);
                tj.setStatus(1);// ??????
                tj.setCreateBy(usrId);
                tj.setJobName(jobname);
                tj.setCreateType(1);
                tj.setJobType(2);// ????????????
                Long trModuleRunId = this.insertJenkinsModuleJobRun(tj);
                resultModuleRun = this.selectByModuleRunId(trModuleRunId);
            }

        }
        TblToolInfo sourceTool = new TblToolInfo();

        // ??????svntoolinfo
        if (tblSystemScm.getToolId() != null) {
            sourceTool = this.geTblToolInfo(tblSystemScm.getToolId());
        }

        // ????????????????????????????????????sonar?????????toolid
//		Map<String, Object> columnMap = new HashMap<>();
//		columnMap.put("SYSTEM_SCM_ID", tblSystemScm.getId());
//		columnMap.put("SYSTEM_ID", systemId);
//		List<TblSystemSonar> lists = this.getSonarByMap(columnMap);
//		Long randomSonar = new Long(0);
//		if (lists == null || lists.size() == 0) {
//			// ??????????????????toolld
//			randomSonar = this.getRandomSonarId(env);
//
//		} else {
//			randomSonar = lists.get(0).getToolId();
//		}

        // ????????????server??????
        // ??????serverdeploy??????
        this.getDeploy(modulesList, env, sysId, result);

//		if (serverType.equals(Constants.SERVER_MICRO_TYPE) ) {// ?????????
//
//			List<TblSystemSonar> tblSystemSonarList = new ArrayList<TblSystemSonar>();
//
//			for (TblSystemModule tblSystemModule : tblSystemModuleList) {
//
//				TblSystemSonar tblSystemSonar = assembleSonar(systemId, env, tSystemInfo, tblSystemScm,
//						tblSystemModule, timestamp, usrId, randomSonar, jconfId);
//				tblSystemSonarList.add(tblSystemSonar);
//			}
//
//			result.put("tblSystemSonarList", tblSystemSonarList);
//		} else {
//			TblSystemSonar tblSystemSonar = assembleSonar(systemId, env, tSystemInfo, tblSystemScm, null, timestamp,
//					usrId, randomSonar, jconfId);
//			result.put("tblSystemSonar", tblSystemSonar);
//			result.put("resultModuleRun", resultModuleRun);
//		}
//		result.put("status", Constants.ITMP_RETURN_SUCCESS);
        result.put("resultModuleRun", resultModuleRun);
        result.put("serverType", serverType);
        result.put("tSystemInfo", tSystemInfo);
        result.put("tblSystemScm", tblSystemScm);
        result.put("tblSystemModuleList", tblSystemModuleList);
        result.put("tblSystemModuleScmList", tblSystemModuleScmList);
        if (tblSystemModuleScmList.size() > 0) {
            List<TblToolInfo> sourceToolList = new ArrayList<>();
            //?????????
            for (TblSystemModuleScm tms : tblSystemModuleScmList) {
                sourceToolList.add(tblToolInfoMapper.selectByPrimaryKey(tms.getToolId()));
            }
            result.put("sourceToolList", sourceToolList);
        }

        result.put("moduleAllList", tblSystemModuleMapper.selectSystemModule(tSystemInfo.getId()));
        result.put("tblSystemJenkins", tblSystemJenkins);
        result.put("jenkinsTool", jenkinsTool);
        result.put("sourceTool", sourceTool);
        result.put("jobrun", jobrun);
        result.put("moduleRunJobList", moduleRunJobList);
        result.put("moduleList", modulesList);
        result.put("envType", env);
        result.put("envName", envName);
        result.put("userId", CommonUtil.getCurrentUserId(request));
        return result;
    }


    //??????
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> buildJobAutoDeployBatch(String env, String data, HttpServletRequest request) {

        long usrId = CommonUtil.getCurrentUserId(request);
        Map<String, Object> map = new HashMap<>();
        JSONArray jsonArray = JSONObject.parseArray(data);
        for (int y = 0; y < jsonArray.size(); y++) {
            // ????????????
            JSONObject jobject = jsonArray.getJSONObject(y);
            String sysId = jobject.getString("sysId");
            String systemName = jobject.getString("systemName");
            String serverType = jobject.getString("serverType");
            String modules = jobject.getString("modules");

            Map<String, Object> result = new HashMap<>();

            Object redisEnvType = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
            Map<String, Object> envMap = JSON.parseObject(redisEnvType.toString());
            String envName = envMap.get(env).toString();
            TblSystemInfo tSystemInfo = tblSystemInfoMapper.getOneSystemInfo(Long.parseLong(sysId));

            Integer systemId = Integer.parseInt(sysId);
            // ??????????????????id
            Map<String, Object> mapParam = new HashMap<>();
            mapParam.put("systemId", systemId);
            mapParam.put("environmentType", env);
            mapParam.put("status", 1);
            // ????????????scm????????? env???systemid????????????
            List<TblSystemScm> countList = this.getScmByParam(mapParam);
            if (countList == null || countList.size() == 0) {
                continue;
            }
            TblSystemScm tblSystemScm = countList.get(0);
            // ????????????????????????????????????
            if (tblSystemScm.getDeployStatus() == 2) {

                continue;
            }

            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                    PlatformTransactionManager txManager = webApplicationContext
                            .getBean(PlatformTransactionManager.class);
                    TransactionStatus status = txManager.getTransaction(def);
                    String jobrun = "";
                    List<String> modulesList = new ArrayList<>();
                    // ?????????????????????????????????
                    try {
                        if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {// ?????????

                            String[] alModule = modules.split(",");
                            for (int i = 0; i < alModule.length; i++) {
                                // ??????????????????????????????
                                modulesList = Judge(alModule[i], modulesList, tblSystemScm.getId(), systemId);
                            }

                        }
                        if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {
                            modulesList = sortAndDetailModuleid(modulesList, tblSystemScm.getId(), systemId);
                        }
                        TblSystemScm systemScmUpdate = new TblSystemScm();
                        systemScmUpdate.setId(tblSystemScm.getId());
                        systemScmUpdate.setBuildStatus(2);// 2????????????
                        systemScmUpdate.setLastUpdateDate(timestamp);
                        systemScmUpdate.setLastUpdateBy(usrId);
                        tblSystemScmMapper.updateByPrimaryKeySelective(systemScmUpdate);
                        String jobname = tSystemInfo.getSystemCode() + "_" + env + "_" + String.valueOf(systemId)
                                + "_deploy";
                        // ???????????????sysid???env ??? TBL_SYSTEM_JENKINS??????????????????????????????????????????
                        TblSystemJenkins tblSystemJenkins = new TblSystemJenkins();
                        tblSystemJenkins.setCreateDate(timestamp);
                        tblSystemJenkins.setJobName(jobname);
                        tblSystemJenkins.setRootPom("./");
                        tblSystemJenkins.setSystemId((long) systemId);
                        tblSystemJenkins.setSystemScmId(tblSystemScm.getId());
                        tblSystemJenkins.setStatus(1);// 1??????2??????
                        tblSystemJenkins.setGoalsOptions("mvn clean deploy -Dmaven.test.skip=true");
                        tblSystemJenkins.setCreateBy(usrId);
                        tblSystemJenkins.setJobType(3);// 1??????2??????//????????????
                        tblSystemJenkins.setBuildStatus(2);// 2?????????
                        tblSystemJenkins.setCreateType(1);
                        long jconfId = creOrUpdSjenkins(tblSystemJenkins, env, "1", tblSystemJenkins.getJobType() + "");
                        TblToolInfo jenkinsTool = new TblToolInfo();
                        // ??????toolid
                        jenkinsTool = geTblToolInfo(tblSystemJenkins.getToolId());
                        // ??????build?????????
                        long systemScmId = tblSystemScm.getId();
                        List<TblSystemModuleScm> tblSystemModuleScmList = new ArrayList<>();
                        List<TblSystemModuleJenkinsJobRun> moduleRunJobList = new ArrayList<>();
                        TblSystemModuleJenkinsJobRun resultModuleRun = new TblSystemModuleJenkinsJobRun();
                        List<TblSystemModule> tblSystemModuleList = new ArrayList<>();
                        if (jconfId > 0) {
                            // TBL_SYSTEM_JENKINS_JOB_RUN ??????
                            TblSystemJenkinsJobRun ttr = new TblSystemJenkinsJobRun();
                            ttr.setSystemJenkinsId((long) jconfId);
                            ttr.setSystemId((long) systemId);
                            ttr.setJobName(jobname);
                            ttr.setRootPom(".");
                            ttr.setBuildStatus(1);// ??????
                            ttr.setStartDate(timestamp);
                            ttr.setStatus(1);
                            ttr.setCreateDate(timestamp);
                            ttr.setEnvironmentType(Integer.parseInt(env));
                            ttr.setGoalsOptions("mvn clean install -Dmaven.test.skip=true");
                            ttr.setCreateBy(usrId);
                            ttr.setCreateType(1);
                            ttr.setJobType(3);// ????????????
                            long jobid = insertJenkinsJobRun(ttr);// jobId jenkins???????????????id
                            jobrun = jobid + "";
                            if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {// ?????????1?????????module???
                                for (String id : modulesList) {// ????????????
                                    TblSystemModuleJenkinsJobRun tj = new TblSystemModuleJenkinsJobRun();
                                    tj.setSystemJenkinsJobRun(jobid);
                                    tj.setSystemModuleId((long) Integer.parseInt(id));
                                    tj.setCreateDate(timestamp);
                                    tj.setStatus(1);// ??????
                                    tj.setCreateBy(usrId);
                                    tj.setSystemId((long) systemId);
                                    tj.setSystemScmId(systemScmId);
                                    tj.setJobName(jobname);
                                    tj.setCreateType(1);// 1????????????
                                    tj.setJobType(3);// ????????????

                                    long moduleRunId = insertJenkinsModuleJobRun(tj);
                                    // ????????????
                                    TblSystemModuleJenkinsJobRun mrid = selectByModuleRunId(moduleRunId);
                                    moduleRunJobList.add(mrid);
                                    tblSystemModuleList.add(getTblsystemModule((long) Integer.parseInt(id)));
                                    Map<String, Object> moduleParam = new HashMap<>();
                                    moduleParam.put("systemId", systemId);
                                    // ??????
                                    moduleParam.put("systemScmId", systemScmId);
                                    moduleParam.put("systemModuleId", id);
                                    List<TblSystemModuleScm> scmList = getModuleScmByParam(moduleParam);
                                    if (scmList != null && scmList.size() > 0) {
                                        tblSystemModuleScmList.add(scmList.get(0));
                                    }

                                }

                            } else {
                                // ????????????????????????modulejobrun????????????sonar????????????
                                TblSystemModuleJenkinsJobRun tj = new TblSystemModuleJenkinsJobRun();
                                tj.setSystemJenkinsJobRun(jobid);
                                tj.setSystemScmId(systemScmId);
                                tj.setSystemId((long) systemId);
                                tj.setCreateDate(timestamp);
                                tj.setStatus(1);// ??????
                                tj.setCreateBy(usrId);
                                tj.setJobName(jobname);
                                tj.setCreateType(1);
                                tj.setJobType(3);// ????????????
                                Long trModuleRunId = insertJenkinsModuleJobRun(tj);
                                resultModuleRun = selectByModuleRunId(trModuleRunId);
                            }

                        }
                        TblToolInfo sourceTool = new TblToolInfo();

                        // ??????svntoolinfo
                        if (tblSystemScm.getToolId() != null) {
                            sourceTool = geTblToolInfo(tblSystemScm.getToolId());
                        }

                        // ????????????????????????????????????sonar?????????toolid
                        Map<String, Object> columnMap = new HashMap<>();
                        columnMap.put("SYSTEM_SCM_ID", tblSystemScm.getId());
                        columnMap.put("SYSTEM_ID", systemId);
                        List<TblSystemSonar> lists = getSonarByMap(columnMap);
                        Long randomSonar = new Long(0);
                        if (lists == null || lists.size() == 0) {
                            // ??????????????????toolld
                            randomSonar = getRandomSonarId(env);

                        } else {
                            randomSonar = lists.get(0).getToolId();
                        }

                        // ??????serverdeploy??????
                        getDeploy(modulesList, env, sysId, result);

                        if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {// ?????????

                            List<TblSystemSonar> tblSystemSonarList = new ArrayList<TblSystemSonar>();

                            for (TblSystemModule tblSystemModule : tblSystemModuleList) {

                                TblSystemSonar tblSystemSonar = assembleSonar(systemId, env, tSystemInfo,
                                        tblSystemScm, tblSystemModule, timestamp, usrId, randomSonar, jconfId);
                                tblSystemSonarList.add(tblSystemSonar);
                            }

                            result.put("tblSystemSonarList", tblSystemSonarList);
                        } else {
                            TblSystemSonar tblSystemSonar = assembleSonar(systemId, env, tSystemInfo, tblSystemScm,
                                    null, timestamp, usrId, randomSonar, jconfId);
                            result.put("tblSystemSonar", tblSystemSonar);
                            result.put("resultModuleRun", resultModuleRun);
                        }
                        result.put("status", Constants.ITMP_RETURN_SUCCESS);
                        result.put("serverType", serverType);
                        result.put("tSystemInfo", tSystemInfo);
                        result.put("tblSystemScm", tblSystemScm);
                        result.put("tblSystemModuleList", tblSystemModuleList);
                        result.put("tblSystemModuleScmList", tblSystemModuleScmList);
                        result.put("tblSystemJenkins", tblSystemJenkins);
                        result.put("jenkinsTool", jenkinsTool);
                        result.put("sourceTool", sourceTool);
                        result.put("jobrun", jobrun);
                        result.put("moduleRunJobList", moduleRunJobList);
                        if (serverType.equals(Constants.SERVER_MICRO_TYPE)) {
                            iJenkinsBuildService.buildMicroAutoDeployJob(result);
                        } else {
                            iJenkinsBuildService.buildGeneralAutoDeployJob(result);
                        }
                        txManager.commit(status);
                    } catch (Exception e) {

                        handleException(e);

                        txManager.rollback(status);

                    }

                }
            });

        }

        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        return map;

    }
    //???????????????sonar????????????

    @Override
    public Map<String, Object> getSonarMessageMincro(String systemId, String startDate, String endDate) {
        Map<String, Object> resultMap = new HashMap<>();
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List<Map<String, Object>> list = new ArrayList<>();
        // ??????????????????????????????id
        try {
            List<TblSystemModule> modules = this.selectSystemModule(Long.parseLong(systemId));
            for (TblSystemModule module : modules) {
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> mapParam = new HashMap<>();
                mapParam.put("startDate", startDate);
                mapParam.put("endDate", endDate);
                mapParam.put("moduleId", module.getId());
                mapParam.put("systemId", systemId);
                List<TblSystemModuleJenkinsJobRun> tmjr = tblSystemModuleJenkinsJobRunMapper
                        .selectSonarBySystemidAndDateMincroDeploy(mapParam);
                String yBug = "[";
                String yVulnerabilities = "[";
                String yCodeSmells = "[";
                String yCoverage = "[";
                String yDuplications = "[";
                String xValue = "[";

                if (tmjr == null || tmjr.size() == 0) {
                    continue;
                }

                if (tmjr != null && tmjr.size() > 0) {
                    for (TblSystemModuleJenkinsJobRun trs : tmjr) {

                        if (trs.getSonarBugs() != null) {
                            yBug = yBug + trs.getSonarBugs() + ",";
                        } else {
                            yBug = yBug + "0" + ",";
                        }
                        if (trs.getSonarVulnerabilities() != null) {
                            yVulnerabilities = yVulnerabilities + trs.getSonarVulnerabilities() + ",";
                        } else {
                            yVulnerabilities = yVulnerabilities + "0" + ",";
                        }

                        if (trs.getSonarCodeSmells() != null) {
                            yCodeSmells = yCodeSmells + trs.getSonarCodeSmells() + ",";
                        } else {
                            yCodeSmells = yCodeSmells + "0" + ",";
                        }
                        if (trs.getSonarDuplications() != null) {
                            yDuplications = yDuplications + trs.getSonarDuplications() + "%,";
                        } else {
                            yDuplications = yDuplications + "0.0%" + ",";
                        }

                        if (trs.getSonarCoverage() != null) {
                            yCoverage = yCoverage + trs.getSonarCoverage() + "%,";
                        } else {
                            yCoverage = yCoverage + "0.0%" + ",";
                        }

                        if (trs.getLastUpdateDate() != null) {
                            xValue = xValue + sdf.format(trs.getLastUpdateDate()) + ",";
                        } else {
                            xValue = xValue + "0" + ",";
                        }

                    }

                }
                yBug = yBug.substring(0, yBug.length() - 1) + "]";
                yVulnerabilities = yVulnerabilities.substring(0, yVulnerabilities.length() - 1) + "]";
                yCodeSmells = yCodeSmells.substring(0, yCodeSmells.length() - 1) + "]";
                yCoverage = yCoverage.substring(0, yCoverage.length() - 1) + "]";
                yDuplications = yDuplications.substring(0, yDuplications.length() - 1) + "]";
                xValue = xValue.substring(0, xValue.length() - 1) + "]";

                map.put("moduleName", module.getModuleName());
                map.put("Bugs", yBug);
                map.put("Vulnerabilities", yVulnerabilities);
                map.put("Code Smells", yCodeSmells);
                map.put("Coverage", yCoverage);
                map.put("Duplications", yDuplications);
                map.put("xValue", xValue);
                list.add(map);
            }

            resultMap.put("list", list);
            resultMap.put("status", "1");// ??????
            resultMap.put("createType", 1);
            return resultMap;
        } catch (NumberFormatException e) {
            this.handleException(e);
            throw e;
        }

    }

    /**
     * ??????sonar????????????
     *
     * @param systemId  ??????id
     * @param startDate
     * @param endDate
     * @return map
     * @author weiji
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSonarMessage(String systemId, String startDate, String endDate) {
        Map<String, Object> map = new HashMap<>();
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            // ????????????
            Map<String, Object> mapParam = new HashMap<>();
            mapParam.put("startDate", startDate);
            mapParam.put("endDate", endDate);
            mapParam.put("systemId", systemId);
            List<TblSystemModuleJenkinsJobRun> tmjr = tblSystemModuleJenkinsJobRunMapper
                    .selectSonarBySystemidAndDateDeploy(mapParam);
            String ybug = "[";
            String yVulnerabilities = "[";
            String yCodeSmells = "[";
            String yCoverage = "[";
            String yduplications = "[";
            String xValue = "[";

            if (tmjr == null || tmjr.size() == 0) {
                map.put("status", "2");// ????????????????????????
                return map;

            }

            if (tmjr != null && tmjr.size() > 0) {
                for (TblSystemModuleJenkinsJobRun trs : tmjr) {

                    // ??????
                    if (trs.getSonarBugs() != null) {
                        ybug = ybug + trs.getSonarBugs() + ",";
                    } else {
                        ybug = ybug + "0" + ",";
                    }
                    if (trs.getSonarVulnerabilities() != null) {
                        yVulnerabilities = yVulnerabilities + trs.getSonarVulnerabilities() + ",";
                    } else {
                        yVulnerabilities = yVulnerabilities + "0" + ",";
                    }

                    if (trs.getSonarCodeSmells() != null) {
                        yCodeSmells = yCodeSmells + trs.getSonarCodeSmells() + ",";
                    } else {
                        yCodeSmells = yCodeSmells + "0" + ",";
                    }
                    if (trs.getSonarDuplications() != null) {
                        yduplications = yduplications + trs.getSonarDuplications() + "%,";
                    } else {
                        yduplications = yduplications + "0.0%" + ",";
                    }

                    if (trs.getSonarCoverage() != null) {
                        yCoverage = yCoverage + trs.getSonarCoverage() + "%,";
                    } else {
                        yCoverage = yCoverage + "0.0%" + ",";
                    }

                    if (trs.getLastUpdateDate() != null) {
                        xValue = xValue + sdf.format(trs.getLastUpdateDate()) + ",";
                    } else {
                        xValue = xValue + "0" + ",";
                    }

                }

            }
            ybug = ybug.substring(0, ybug.length() - 1) + "]";
            yVulnerabilities = yVulnerabilities.substring(0, yVulnerabilities.length() - 1) + "]";
            ;
            yCodeSmells = yCodeSmells.substring(0, yCodeSmells.length() - 1) + "]";
            ;
            yCoverage = yCoverage.substring(0, yCoverage.length() - 1) + "]";
            ;
            yduplications = yduplications.substring(0, yduplications.length() - 1) + "]";
            ;
            xValue = xValue.substring(0, xValue.length() - 1) + "]";
            ;

            map.put("Bugs", ybug);
            map.put("Vulnerabilities", yVulnerabilities);
            map.put("Code Smells", yCodeSmells);
            map.put("Coverage", yCoverage);
            map.put("Duplications", yduplications);
            map.put("xValue", xValue);
            map.put("status", "1");
            return map;
        } catch (Exception e) {
            this.handleException(e);
            throw e;
        }

    }

    //??????
    @Transactional(readOnly = false)
    @Override
    public Map<String, Object> buildManualdeployScheduled(HttpServletRequest request, String systemJenkisId) {

        Map<String, Object> paramMap = new HashMap<>();
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long usrId = CommonUtil.getCurrentUserId(request);
            Map<String, Object> map = new HashMap<>();
            map.put("id", systemJenkisId);
            List<TblSystemJenkins> list = this.selectJenkinsByMap(map);
            TblSystemJenkins tblSystemJenkins = list.get(0);
            String jobName = tblSystemJenkins.getJobName();
            // tblSystemJenkins.setBuildStatus(1);// ?????????
            // ???????????????
            tblSystemJenkinsMapper.updateById(tblSystemJenkins);
            // ??????jenkinstoolinfo
            TblToolInfo jenkinsToolInfo = this.geTblToolInfo(tblSystemJenkins.getToolId());
            // ??????tbl_system_sonar
            TblSystemSonar tblSystemSonar = assembleSonarManual(timestamp, tblSystemJenkins.getSystemId(), usrId,
                    Long.parseLong(systemJenkisId));
            TblSystemJenkinsJobRun ttr = new TblSystemJenkinsJobRun();
            ttr.setSystemJenkinsId(Long.parseLong(systemJenkisId));
            ttr.setSystemId(tblSystemJenkins.getSystemId());
            ttr.setJobName(jobName);
            ttr.setRootPom(".");
            ttr.setBuildStatus(1);
            ttr.setStartDate(timestamp);
            ttr.setStatus(1);
            ttr.setCreateDate(timestamp);
            ttr.setJobType(2);

            ttr.setGoalsOptions("mvn clean install -Dmaven.test.skip=true");
            ttr.setCreateBy(usrId);
            ttr.setCreateType(2);// ??????
            long jobid = this.insertJenkinsJobRun(ttr);
            TblSystemModuleJenkinsJobRun tj = new TblSystemModuleJenkinsJobRun();
            tj.setSystemJenkinsJobRun(jobid);
            tj.setCreateDate(timestamp);
            tj.setStatus(1);
            tj.setCreateBy(usrId);
            tj.setSystemId(tblSystemJenkins.getSystemId());
            tj.setJobName(jobName);
            tj.setCreateType(2);
            tj.setJobType(2);
            long moduleJobRunId = this.insertJenkinsModuleJobRun(tj);
            // paramMap.put("jsonParam", jsonParam);
            paramMap.put("jobRunId", jobid);
            paramMap.put("systemJenkinsId", systemJenkisId);
            paramMap.put("moduleJobRunId", moduleJobRunId);
            paramMap.put("systemSonarId", tblSystemSonar.getId());
            paramMap.put("jobName", jobName);
            paramMap.put("jenkinsToolInfo", jenkinsToolInfo);
        } catch (Exception e) {
            this.handleException(e);
            throw e;

        }
        return paramMap;
    }

    private TblSystemSonar assembleSonarManual(Timestamp timestamp, long systemId, long usrId, long systemJenkinsId) {
        TblSystemSonar tblSystemSonar = new TblSystemSonar();
        tblSystemSonar.setCreateDate(timestamp);
        tblSystemSonar.setStatus(1);// ??????
        tblSystemSonar.setSystemId(systemId);
        tblSystemSonar.setCreateBy(usrId);
        tblSystemSonar.setSystemJenkinsId(systemJenkinsId);
        tblSystemSonar = this.creOrUpdSonarManual(tblSystemSonar);
        return tblSystemSonar;
    }

    public TblSystemSonar creOrUpdSonarManual(TblSystemSonar tblSystemSonar) {
        TblSystemSonar sonarParam = new TblSystemSonar();
        sonarParam.setSystemId(tblSystemSonar.getSystemId());
        sonarParam.setSystemJenkinsId(tblSystemSonar.getSystemJenkinsId());
        // sonarParam.setJobname()
        TblSystemSonar sonarFlag = tblSystemSonarMapper.selectOne(sonarParam);
        if (sonarFlag == null) {
            // ???????????????
            tblSystemSonarMapper.insertNew(tblSystemSonar);
            return tblSystemSonar;

        } else {
            return sonarFlag;
        }

    }

    private List<String> sortAndDetailModuleid(List<String> moduleId, long scmId, long systemId) {
        // ???????????????jar???
        Map<String, Object> param = new HashMap<>();
        param.put("system_id", systemId);
        param.put("status", 1);
        param.put("BUILD_DEPENDENCY", 1);
        List<TblSystemModule> moduleList = tblSystemModuleMapper.selectByMap(param);
        for (TblSystemModule tm : moduleList) {
            if (!moduleId.contains(String.valueOf(tm.getId()))) {
                //???????????????module??????????????????
                List<TblSystemModuleScm> moduleScms = judge(String.valueOf(tm.getId()), scmId, Integer.parseInt(String.valueOf(systemId)));
                if (moduleScms != null && moduleScms.size() > 0) {
                    moduleId.add(String.valueOf(tm.getId()));
                }

            }

        }
        Map<String, Object> moduleParam = new HashMap<>();
        moduleParam.put("ids", moduleId);
        List<TblSystemModule> endList = new ArrayList<>();
        if (moduleId.size() > 0) {
            endList = tblSystemModuleMapper.sortModule(moduleParam);
        }
        List<String> result = new ArrayList<>();
        for (TblSystemModule tm : endList) {
            result.add(String.valueOf(tm.getId()));
        }

        return result;

    }

    /**
     * ??????module
     *
     * @param systemId
     * @author weiji
     * @returnList<TblSystemModule>
     */

    @Override
    public List<TblSystemModule> getModuleInfo(String systemId) {
        Map<String, Object> moduleParam = new HashMap<>();
        moduleParam.put("system_id", systemId);
        moduleParam.put("status", 1);
        return tblSystemModuleMapper.selectByMap(moduleParam);

    }

    //?????????????????????

    @Override
    public List<Map<String, Object>> getArtifactInfo(String systemId, String moduleId, String env) {
        Map<String, Object> moduleParam = new HashMap<>();
        moduleParam.put("SYSTEM_MODULE_ID", moduleId);
        moduleParam.put("system_id", systemId);
        moduleParam.put("ENVIRONMENT_TYPE", env);
        List<Map<String, Object>> lists = tblArtifactInfoMapper.findArtInfo(moduleParam);
        return lists;

    }

    /**
     * ????????????????????????
     *
     * @param sysId       ??????id
     * @param artifactids ?????????id
     * @param env         ????????????
     * @return Map<String, Object>
     * @author weiji
     */
    @Override
    public Map<String, Object> buildPackageDeploy(HttpServletRequest request, String sysId, String env,
                                                  String[] artifactids) {
        Map<String, Object> result = new HashMap<>();
        //???????????????????????????????????????
        result = flagArtifacting(sysId, env);
        if (result.get("status").equals(Constants.ITMP_RETURN_FAILURE)) {
            return result;
        }
        long usrId = CommonUtil.getCurrentUserId(request);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Object redisEnvType = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
        Map<String, Object> envMap = JSON.parseObject(redisEnvType.toString());
        String envName = envMap.get(env).toString();
        TblSystemInfo tSystemInfo = tblSystemInfoMapper.getOneSystemInfo(Long.parseLong(sysId));
        List<String> modulesList = new ArrayList<>();
        String jobrun = "";
        Integer systemId = Integer.parseInt(sysId);
        String jobname = tSystemInfo.getSystemCode() + "_" + env + "_" + String.valueOf(systemId)
                + "_packagedeploy";
        // ???????????????sysid???env ??? TBL_SYSTEM_JENKINS??????????????????????????????????????????
        TblSystemJenkins tblSystemJenkins = new TblSystemJenkins();
        //tblSystemJenkins.setSystemScmId(tblSystemScmitmp.getId());
        tblSystemJenkins.setCreateDate(timestamp);
        tblSystemJenkins.setJobName(jobname);
        tblSystemJenkins.setRootPom("./");
        tblSystemJenkins.setSystemId((long) systemId);
        tblSystemJenkins.setStatus(1);// 1??????2??????
        tblSystemJenkins.setGoalsOptions("mvn clean deploy -Dmaven.test.skip=true");
        tblSystemJenkins.setCreateBy(usrId);
        tblSystemJenkins.setJobType(2);// 1??????2????????????3????????????
        tblSystemJenkins.setDeployStatus(2);// 2?????????
        tblSystemJenkins.setCreateType(1);
        tblSystemJenkins.setEnvironmentType(Integer.parseInt(env));
        long jconfId = this.creOrUpdSjenkins(tblSystemJenkins, env, "1", tblSystemJenkins.getJobType() + "");

        tblSystemJenkins = tblSystemJenkinsMapper.selectById(jconfId);
        TblToolInfo jenkinsTool = new TblToolInfo();
        // ??????toolid
        jenkinsTool = this.geTblToolInfo(tblSystemJenkins.getToolId());
        // ??????build?????????

        List<TblSystemModuleJenkinsJobRun> moduleRunJobList = new ArrayList<>();

        // ??????list
        List<TblSystemModule> tblSystemModuleList = new ArrayList<>();
        List<TblArtifactInfo> TblArtifactInfoList = new ArrayList<>();
        for (String id : artifactids) {
            TblArtifactInfo tblArtifactInfo = tblArtifactInfoMapper.selectById(Long.parseLong(id));
            TblArtifactInfoList.add(tblArtifactInfo);
            if (tblArtifactInfo.getSystemModuleId() != null) {
                modulesList.add(String.valueOf(tblArtifactInfo.getSystemModuleId()));
            }
        }

        for (String moduleId : modulesList) {
            TblSystemModule tblSystemModule = tblSystemModuleMapper.selectById(Long.parseLong(moduleId));

            tblSystemModuleList.add(tblSystemModule);

        }
        if (jconfId > 0) {
            // TBL_SYSTEM_JENKINS_JOB_RUN ??????
            TblSystemJenkinsJobRun ttr = new TblSystemJenkinsJobRun();
            ttr.setSystemJenkinsId((long) jconfId);
            ttr.setSystemId((long) systemId);
            ttr.setJobName(jobname);
            ttr.setRootPom(".");
            ttr.setBuildStatus(1);// ??????
            ttr.setStartDate(timestamp);
            ttr.setStatus(1);
            ttr.setCreateDate(timestamp);
            ttr.setEnvironmentType(Integer.parseInt(env));
            ttr.setGoalsOptions("mvn clean install -Dmaven.test.skip=true");
            ttr.setCreateBy(usrId);
            ttr.setCreateType(1);
            ttr.setJobType(2);// ??????
            try {
                int jobNumber = iJenkinsBuildService.getNextBuildNumber(jenkinsTool, tblSystemJenkins, jobname);
                result.put("jobNumber", jobNumber);
                ttr.setJobRunNumber(jobNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long jobid = this.insertJenkinsJobRun(ttr);// jobId jenkins???????????????id
            jobrun = jobid + "";
            for (String id : modulesList) {
                TblSystemModuleJenkinsJobRun tj = new TblSystemModuleJenkinsJobRun();
                tj.setSystemJenkinsJobRun(jobid);
                tj.setSystemModuleId((long) Integer.parseInt(id));
                tj.setCreateDate(timestamp);
                tj.setStatus(1);// ??????
                tj.setCreateBy(usrId);
                tj.setSystemId((long) systemId);
                tj.setJobName(jobname);
                tj.setCreateType(1);// 1????????????
                tj.setJobType(2); // ??????
                tj.setBuildStatus(1);
                long moduleRunId = this.insertJenkinsModuleJobRun(tj);
                // ????????????
                TblSystemModuleJenkinsJobRun mrid = this.selectByModuleRunId(moduleRunId);
                moduleRunJobList.add(mrid);

            }

            //?????????
            if (modulesList.size() == 0) {
                TblSystemModuleJenkinsJobRun tjr = new TblSystemModuleJenkinsJobRun();
                tjr.setSystemJenkinsJobRun(jobid);
                tjr.setCreateDate(timestamp);
                tjr.setStatus(1);// ??????
                tjr.setCreateBy(usrId);
                tjr.setSystemId((long) systemId);
                tjr.setJobName(jobname);
                tjr.setCreateType(1);// 1????????????
                tjr.setJobType(2); // ??????
                tjr.setBuildStatus(1);
                long moduleRunId = this.insertJenkinsModuleJobRun(tjr);
                moduleRunJobList.add(tjr);
            }

        }
        //??????????????????
        this.getDeploy(modulesList, env, sysId, result);
        //?????????????????????
        this.getDbConfig(result, sysId, env);
        //?????????module
        List<String> checkModuleList = new ArrayList<>();
        checkModuleList.addAll(modulesList);
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        tSystemInfo.setDeployType(2);
        result.put("tSystemInfo", tSystemInfo);
        result.put("tblSystemModuleList", tblSystemModuleList);
        result.put("checkModuleList", checkModuleList);
        result.put("tblSystemJenkins", tblSystemJenkins);
        result.put("jenkinsTool", jenkinsTool);
        result.put("jobrun", jobrun);
        result.put("moduleRunJobList", moduleRunJobList);
        result.put("tblArtifactInfoList", TblArtifactInfoList);
        result.put("moduleList", modulesList);
        result.put("envName", envName);
        return result;
    }

    /**
     * @param map
     * @param systemId ??????ID
     * @param env      ??????
     * @throws
     * @Title: getDbConfig
     * @Description: ??????????????????????????????????????????????????????SQL
     * @author author
     */
    private void getDbConfig(Map<String, Object> map, String systemId, String env) {
        Map<String, Object> param = new HashMap<>();
        param.put("system_id", systemId);
        param.put("ENVIRONMENT_TYPE", env);
        param.put("status", 1);
        List<TblSystemDbConfig> tblSystemDbConfigList = tblSystemDbConfigMapper.selectByMap(param);
        Object driverVersion = redisUtils.get("TBL_DATA_DIC_DRIVER_VERSION");
        Map<String, Object> driverVersionMap = JSON.parseObject(driverVersion.toString());
        for (TblSystemDbConfig tblSystemDbConfig : tblSystemDbConfigList) {
            if (tblSystemDbConfig.getDriverVersion() != null) {
                tblSystemDbConfig.setDriverVersionName(driverVersionMap.get(tblSystemDbConfig.getDriverVersion().toString()).toString());
            }
        }
        map.put("tblSystemDbConfigList", tblSystemDbConfigList);

    }

    private Map<String, Object> flagArtifacting(String sysId, String env) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("ENVIRONMENT_TYPE", env);
        mapParam.put("SYSTEM_ID", sysId);
        mapParam.put("STATUS", 1);
        mapParam.put("JOB_TYPE", 2);
        mapParam.put("CREATE_TYPE", 1);
        mapParam.put("DEPLOY_STATUS", 2);
        List<TblSystemJenkins> list = tblSystemJenkinsMapper.selectByMap(mapParam);
        if (list != null && list.size() > 0) {
            result.put("status", Constants.ITMP_RETURN_FAILURE);
            result.put("message", "????????????????????????!");
        } else {
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        }
        return result;

    }


    public long judgeSystemjenkins(TblSystemJenkins tblSystemJenkins, String env, String createType, String jobType) {
        // ??????????????????systemid ??????
        try {
            Map<String, Object> mapParam = new HashMap<>();
            mapParam.put("system_Id", tblSystemJenkins.getSystemId());
            mapParam.put("system_Scm_Id", tblSystemJenkins.getSystemScmId());
            mapParam.put("status", 1);
            mapParam.put("job_Type", jobType);
            mapParam.put("CREATE_TYPE", createType);
            List<TblSystemJenkins> list = tblSystemJenkinsMapper.selectByMap(mapParam);
            if (list != null && list.size() > 0) {
                TblSystemJenkins tblSystemJenkinsTemp = list.get(0);
                // ?????????
                tblSystemJenkins.setToolId(tblSystemJenkinsTemp.getToolId());
                tblSystemJenkinsTemp.setLastUpdateDate(tblSystemJenkins.getCreateDate());
                tblSystemJenkinsTemp.setLastUpdateBy(tblSystemJenkins.getCreateBy());
                tblSystemJenkinsMapper.updateById(tblSystemJenkinsTemp);
                return tblSystemJenkinsTemp.getId();

            } else {
                Map<String, Object> sParam = new HashMap<>();
                sParam.put("system_Id", tblSystemJenkins.getSystemId());
                sParam.put("status", 1);
                List<TblSystemJenkins> jenkinsList = tblSystemJenkinsMapper.selectByMap(sParam);
                if (jenkinsList.size() == 0) {
                    // ????????????tool
                    Map<String, Object> jenParam = new HashMap<>();
                    jenParam.put("toolType", 4);
                    jenParam.put("environmentType", env);
                    jenParam.put("status", 1);
                    List<TblToolInfo> jenktools = tblToolInfoMapper.selectToolByEnv(jenParam);
                    int n = this.getRandom(jenktools.size());

                    TblToolInfo tblToolInfo = jenktools.get(n);
                    tblSystemJenkins.setToolId(tblToolInfo.getId());
                } else {

                    tblSystemJenkins.setToolId(jenkinsList.get(0).getToolId());
                }
                tblSystemJenkinsMapper.insertNew(tblSystemJenkins);
                return tblSystemJenkins.getId();
            }
        } catch (Exception e) {
            this.handleException(e);
            throw e;
        }

    }

    @Override
    public Map<String, Object> pushData(TblSystemInfo system, Long[] featureIds,
                                        VersionInfo versionInfo, String systemPackageName, List<Map<String, Object>> subSystemList) {

        Map<String, Object> mapAll = new LinkedHashMap<>();
        mapAll.put("requestHead", DataBusRequestHead.getRequestHead());

        Map<String, Object> mapBody = new HashMap<>();//requestBody
        Map<String, Object> systemInfo = new HashMap<>();//systemInfo
        systemInfo.put("systemName", system.getSystemName());
        systemInfo.put("systemCode", system.getSystemCode());
        systemInfo.put("systemPackageName", systemPackageName);
        systemInfo.put("systemType", systemPackageName.equals("") ? 2 : 1);
        systemInfo.put("subSystemList", subSystemList);
        mapBody.put("systemInfo", systemInfo);

        if (versionInfo.getVersionManager() != null) {
            TblUserInfo userInfo = userInfoMapper.getUserById(Long.valueOf(versionInfo.getVersionManager()));
            versionInfo.setVersionManager(userInfo.getUserAccount());
        }
        if (versionInfo.getDepartmant() != null) {
            TblDeptInfoDTO deptInfoDTO = userInfoMapper.findDeptById(Long.valueOf(versionInfo.getDepartmant()));
            versionInfo.setDepartmant(deptInfoDTO.getDeptNumber());
        }
        if (featureIds != null && featureIds.length > 0) {
            TblCommissioningWindow window = commissioningWindowMapper.findWindowByFeatureId(featureIds[0]);
            versionInfo.setReleaseTime(window.getWindowDate());
        }
        mapBody.put("versionInfo", versionInfo);

        List<TblRequirementInfo> requirement = requirementInfoMapper.findRequirementByFeatureIds(featureIds);
        List<TblDefectInfo> defectList = tblRequirementFeatureMapper.findDftByReqFIds(featureIds);
        Map[] demandInfoList = new Map[requirement.size() + defectList.size()];
        if (requirement != null && requirement.size() > 0) {
            for (int i = 0; i < requirement.size(); i++) {
                Map<String, Object> req = new HashMap<>();
                req.put("demandName", requirement.get(i).getRequirementName());
                req.put("demandNumber", requirement.get(i).getRequirementCode());
                demandInfoList[i] = req;
            }

        }
        if (defectList != null && defectList.size() > 0) {
            for (int i = 0; i < defectList.size(); i++) {
                Map<String, Object> def = new HashMap<>();
                def.put("demandName", defectList.get(i).getDefectSummary());
                def.put("demandNumber", defectList.get(i).getDefectCode());
                demandInfoList[i] = def;
            }
        }
        mapBody.put("demandInfoList", demandInfoList);
        mapAll.put("requestBody", mapBody);
        return mapAll;
    }

    @Override
    @Transactional(readOnly = false)
    public void updateCommissioningResultData(Map<String, Object> data) {
        //??????????????????
        try {
            String resultInfo = data.get("resultInfo").toString();
            //??????????????????1????????? 2????????? 3???????????? 4????????????
            String disposeStatus = JSON.parseObject(resultInfo).get("disposeStatus").toString();
            //????????????
            String disposeDate = JSON.parseObject(resultInfo).get("disposeDate").toString();
            //??????????????????
            String releaseTime = JSON.parseObject(resultInfo).get("releaseTime").toString();
            //?????????0???PRD-IN 1???PRD-OUT???
            String versionType = JSON.parseObject(resultInfo).get("versionType").toString();
            //dataBus????????????
            String versionNumber = JSON.parseObject(resultInfo).get("versionNumber").toString();
            //??????
            String remark = JSON.parseObject(resultInfo).get("remark").toString();

            //??????????????????
            VelocityDataDict dict = new VelocityDataDict();
            Map<String, String> result = dict.getDictMap("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
            String env = getData(result, "0".equals(versionType) ? "PRD-IN" : "PRD-OUT");

            TblUserInfo userInfo = userInfoMapper.getUserById(Long.valueOf(1));
            Map<String, Object> sendMap = new HashMap<>();
            sendMap.put("userId", 1);
            sendMap.put("userAccount", userInfo.getUserAccount());
            sendMap.put("userName", userInfo.getUserName());
            String sendTask = JSON.toJSONString(sendMap);

            //????????????
            String systemInfo = data.get("systemInfo").toString();
            //????????????
            String systemCode = JSON.parseObject(systemInfo).get("systemCode").toString();

            //??????????????????????????????
            TblSystemInfo system = systemInfoMapper.getSystemByCode(systemCode);

            if (system != null && disposeStatus != null) {
                //????????????????????????????????????????????????
                List<TblRequirementFeature> features =
                        tblRequirementFeatureMapper.findFestureByWindowDate(releaseTime, system.getId());
                String systemLog = "";
                //??????redis????????????????????????
                if (redisUtils.get(versionNumber) != null) {
                    Map<String, Object> resultLog = new HashMap<>();
                    if ("1".equals(disposeStatus)) {
                        resultLog.put("status", 2);
                        resultLog.put("log", "?????????????????????");
                    } else if ("2".equals(disposeStatus)) {
                        resultLog.put("status", 3);
                        resultLog.put("log", "?????????????????????");
                    } else if ("3".equals(disposeStatus)) {
                        resultLog.put("status", 3);
                        resultLog.put("log", "??????????????????," + remark + "???");
                    } else if ("4".equals(disposeStatus)) {
                        String systemType = JSON.parseObject(systemInfo).get("systemType").toString();
                        if (systemType.equals("1")) {
                            String str = JSON.parseObject(systemInfo).get("disposeDetail").toString();
                            List<DisposeDetail> disposeDetails = JSONArray.parseArray(str, DisposeDetail.class);
                            if (disposeDetails.size() > 0) {
                                for (DisposeDetail d : disposeDetails) {
                                    systemLog += "(?????????" + d.getInstanceName() + "???ip???" + d.getInstanceIp() + "????????????????????????)???";
                                }
                                systemLog = systemLog.substring(0, systemLog.length() - 1);
                            }
                        } else if (systemType.equals("2")) {
                            String str = JSON.parseObject(systemInfo).get("subSystemResults").toString();
                            List<SubDisposeDetail> disposeDetails = JSONArray.parseArray(str, SubDisposeDetail.class);
                            if (disposeDetails.size() > 0) {
                                for (SubDisposeDetail d : disposeDetails) {
                                    String sl = "";
                                    for (DisposeDetail d1 : d.getSubDisposeDetails()) {
                                        sl += "(?????????" + d1.getInstanceName() + "???ip???" + d1.getInstanceIp() + "????????????????????????)???";
                                    }
                                    sl = sl.substring(0, sl.length() - 1);
                                    systemLog += d.getSubSystemCode() + "?????????{" + sl + "}???";
                                }
                                systemLog = systemLog.substring(0, systemLog.length() - 1);
                            }
                        }
                        resultLog.put("status", 3);
                        resultLog.put("log", "??????????????????," + remark + "???" + systemLog);
                    }
                    Map<String, Object> autoMap = JSON.parseObject(JSON.toJSONString(redisUtils.get(versionNumber)));
                    resultLog.put("jobRunId", autoMap.get("jobRunId").toString());
                    resultLog.put("autoMap", autoMap);
                    log.info("??????????????????");
                    structureServiceImpl.detailAutoLog(resultLog);
                }
                Integer status = 0;
                for (TblRequirementFeature feature : features) {
                    //??????????????????????????????
                    if (!StringUtils.isBlank(feature.getId().toString()) && !StringUtils.isBlank(env)) {
                        if ("1".equals(disposeStatus)) {
                            status = 2;
                        } else if ("2".equals(disposeStatus)) {
                            status = 3;
                        } else if ("3".equals(disposeStatus)) {
                            status = 1;
                        } else if ("4".equals(disposeStatus)) {
                            status = 4;
                        }
                        log.info("----???????????????,????????????----id=" + feature.getId() + "------??????=" + env + "------??????=" + status);
                        devTaskService.updateDeployStatusOne(feature.getId().toString(), env, sendTask, status);
                    }
                    //?????????????????????
                    if ("1".equals(disposeStatus)) {
                        log.info("---------------------------??????????????????,????????????----------------------------");
                        TblRequirementFeatureTimeTrace timeTrace = new TblRequirementFeatureTimeTrace();
                        timeTrace.setRequirementFeatureId(feature.getId());
                        timeTrace.setRequirementFeatureProdCompleteTime(DateUtil.parseDate(disposeDate, DateUtil.fullFormat));
                        timeTraceMapper.updateReqFeatureProdCompleteTime(timeTrace);
                        synDefectInfo(feature.getId());
                    }
                }
            }
            //redis??????????????????
            redisUtils.remove(versionNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ????????????id??????????????????
     *
     * @param artifactids ?????????ids
     * @return List<TblCommissioningWindow>
     * @author weiji
     */
    @Override
    public List<TblCommissioningWindow> getWindowsByartId(String artifactids) {

        Map<String, Object> map = new HashMap<>();
        List<String> listParam = new ArrayList<>();
        String[] ids = artifactids.split(",");
        for (String s : ids) {
            listParam.add(s);
        }
        map.put("artIds", listParam);
        List<TblCommissioningWindow> list = commissioningWindowMapper.getWindowsByartId(map);//??????
        List<TblCommissioningWindow> beforDate = new ArrayList<>();
        List<TblCommissioningWindow> afterDate = new ArrayList<>();
        List<TblCommissioningWindow> resultList = new ArrayList<>();
        Date nowDate = new Date();
        for (TblCommissioningWindow tblCommissioningWindow : list) {
            Date date = tblCommissioningWindow.getWindowDate();
            if (nowDate.compareTo(date) >= 0) {//??????
                beforDate.add(tblCommissioningWindow);
            } else {//?????????
                afterDate.add(tblCommissioningWindow);
            }
        }

        //????????????
        int beforsize = beforDate.size();
        if (beforsize != 0) {
            resultList.addAll(beforDate);
        }
        //????????????
        int aftersize = afterDate.size();
        if (aftersize != 0) {
            resultList.addAll(afterDate);
        }


//        List<TblCommissioningWindow> endList=detailList(resultList);
        return resultList;
    }

    private List<TblCommissioningWindow> detailList(List<TblCommissioningWindow> resultList) {
        List<String> list = new ArrayList<>();
        for (TblCommissioningWindow tblCommissioningWindow : resultList) {
            String id = tblCommissioningWindow.getId().toString();
            list.add(id);

        }
        List<TblCommissioningWindow> endList = new ArrayList<>();
        for (TblCommissioningWindow tblCommissioningWindow1 : resultList) {
            String id1 = tblCommissioningWindow1.getId().toString();
            if (!list.contains(id1)) {
                endList.add(tblCommissioningWindow1);
            }

        }
        return endList;

    }

    //????????????????????????id
    @Override
    public List<Map<String, Object>> getWindowsBySystemId(long systemId, String env) {
        List<TblCommissioningWindow> list = commissioningWindowMapper.getAllWindow();
        List<Map<String, Object>> resultList = new ArrayList<>();
        String defaultWindowId = "";
        //??????????????????????????????
        TblSystemInfo tblSystemInfo = tblSystemInfoMapper.selectById(systemId);
        if (tblSystemInfo.getDevelopmentMode() != null && tblSystemInfo.getDevelopmentMode() == 2) {
            //??????????????????????????????????????????
            Map<String, Object> mapParam = new HashMap<>();
            mapParam.put("systemId", systemId);
            mapParam.put("environmentType", env);
            mapParam.put("status", 1);
            List<TblSystemScm> countList = tblSystemScmMapper.getScmByParam(mapParam);
            if (countList != null && countList.size() > 0) {
                defaultWindowId = String.valueOf(countList.get(0).getCommissioningWindowId());
            }

        }

        for (TblCommissioningWindow tblCommissioningWindow : list) {
            Map<String, Object> map = new HashMap<>();
            if (!defaultWindowId.equals("") && defaultWindowId.equals(String.valueOf(tblCommissioningWindow.getId()))) {
                map.put("default", "true");
            } else {
                map.put("default", "false");
            }
            map.put("id", tblCommissioningWindow.getId());
            map.put("windowName", tblCommissioningWindow.getWindowName());
            resultList.add(map);
        }

        return resultList;
    }

    /**
     * ??????????????????
     *
     * @param systemId ??????id
     * @return Map<String, Object>
     * @author weiji
     */
    @Override
    public List<TblCommissioningWindow> getWindowsLimit(long systemId) {

        TblSystemInfo tblSystemInfo = tblSystemInfoMapper.selectById(systemId);
        TblCommissioningWindow beforeTime = commissioningWindowMapper.selectBeforeTimeOrderBy();
        List<TblCommissioningWindow> afterTime = commissioningWindowMapper.selectAfterTimeLimit();

        if (tblSystemInfo.getDevelopmentMode() != null && tblSystemInfo.getDevelopmentMode() == 2 && afterTime.size() > 0) {
            afterTime.get(0).setFeatureStatus("defaultSelect");
        }


        if (beforeTime != null) {
            afterTime.add(beforeTime);

        }
        //Collections.reverse(afterTime);

        Collections.sort(afterTime, new Comparator<TblCommissioningWindow>() {

            @Override
            public int compare(TblCommissioningWindow o1, TblCommissioningWindow o2) {
                return o1.getWindowDate().compareTo(o2.getWindowDate());
            }
        });


        return afterTime;

    }

    /**
     * ????????????id????????????????????????
     *
     * @param artifactids ?????????ids
     * @return List<DevTaskVo>
     * @author weiji
     */

    @Override
    public List<DevTaskVo> getReqFeaByartId(String artifactids) {

        Map<String, Object> map = new HashMap<>();
        List<String> listParam = new ArrayList<>();
        String[] ids = artifactids.split(",");
        for (String s : ids) {
            listParam.add(s);
        }
        map.put("artIds", listParam);
        return tblRequirementFeatureMapper.getReqFeaByartId(map);
    }

    @Override
    public List<Map<String, Object>> getTestConfig(long systemId, List<String> moduleIds, String envType, String testType) {
        TblSystemInfo tblSystemInfo = tblSystemInfoMapper.selectById(systemId);
        Integer architectureType = tblSystemInfo.getArchitectureType();
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Map<String, Object>> returnList = new ArrayList<>();
        List<String> flagList = new ArrayList<>();
        if (moduleIds.size() > 0) {
            for (int i = 0; i < moduleIds.size(); i++) {
                //long moduleId = Long.parseLong(moduleIds.get(i));
                //Integer moduleRunId = moduleRunIds.get(i);
                Long moduleId = Long.parseLong(moduleIds.get(i));
                Map<String, Object> map = new HashMap<>();
                map.put("status", 1);
                map.put("system_id", systemId);
                map.put("SYSTEM_MODULE_ID", moduleId);
                map.put("ENVIRONMENT_TYPE", envType);
                map.put("test_type", testType);
                List<TblSystemAutomaticTestConfig> lists = tblSystemAutomaticTestConfigMapper.selectByMap(map);
                List<String> testSceneList = new ArrayList<>();
                if (!lists.isEmpty()) {

                    TblSystemModule tblSystemModule = tblSystemModuleMapper.selectById(moduleId);
                    for (TblSystemAutomaticTestConfig tblSystemAutomaticTestConfig : lists) {

                        String testScene = "";
                        if (tblSystemAutomaticTestConfig.getTestScene() != null) {
                            testScene = tblSystemAutomaticTestConfig.getTestScene();
                            String[] testSceneArray = testScene.split(",");
                            for (String scene : testSceneArray) {

                                if (!flagList.contains(scene)) {
                                    flagList.add(scene);
                                }

                                Map<String, Object> resultMap = new HashMap<>();
                                resultMap.put("systemCode", tblSystemInfo.getSystemCode());
                                resultMap.put("systemName", tblSystemInfo.getSystemName());
                                resultMap.put("testSceneCode", scene);
                                resultMap.put("environmentType", envType);
                                if (architectureType == 1) { //???????????????
                                    resultMap.put("subSystemCode", tblSystemModule.getModuleCode());
                                    resultMap.put("subSystemName", tblSystemModule.getModuleName());
                                    resultMap.put("subModuleId", tblSystemModule.getId());
                                }
                                resultList.add(resultMap);
                            }
                        }


                    }
                }


            }
            for (String scene : flagList) {
                Map<String, Object> mapScene = new HashMap<>();
                List<Map<String, Object>> listScene = new ArrayList<>();
                for (Map<String, Object> map : resultList) {

                    String testSceneCode = map.get("testSceneCode").toString();
                    if (scene.equals(testSceneCode)) {
                        Map<String, Object> subMapScene = new HashMap<>();
                        mapScene.put("systemCode", map.get("systemCode").toString());
                        mapScene.put("systemName", map.get("systemName").toString());
                        mapScene.put("testSceneCode", map.get("testSceneCode").toString());
                        mapScene.put("environmentType", map.get("environmentType").toString());
                        subMapScene.put("subSystemCode", map.get("subSystemCode").toString());
                        subMapScene.put("subSystemName", map.get("subSystemName").toString());
                        subMapScene.put("testSceneCode", map.get("testSceneCode").toString());
                        subMapScene.put("subModuleId", map.get("subModuleId").toString());
                        listScene.add(subMapScene);
                        mapScene.put("subSystemInfo", listScene);

                    }


                }
                returnList.add(mapScene);
            }

            return returnList;


        } else {
            //????????????

            Map<String, Object> map = new HashMap<>();
            map.put("status", 1);
            map.put("system_id", systemId);
            map.put("ENVIRONMENT_TYPE", envType);
            map.put("test_type", testType);
            List<TblSystemAutomaticTestConfig> lists = tblSystemAutomaticTestConfigMapper.selectByMap(map);
            if (!lists.isEmpty()) {
                for (TblSystemAutomaticTestConfig tblSystemAutomaticTestConfig : lists) {
                    String testScene = "";
                    if (tblSystemAutomaticTestConfig.getTestScene() != null) {
                        testScene = tblSystemAutomaticTestConfig.getTestScene();
                        String[] testSceneArray = testScene.split(",");
                        for (String scene : testSceneArray) {
                            Map<String, Object> resultMap = new HashMap<>();
                            resultMap.put("systemCode", tblSystemInfo.getSystemCode());
                            resultMap.put("systemName", tblSystemInfo.getSystemName());
                            resultMap.put("testSceneCode", scene);
                            resultMap.put("environmentType", envType);
                            resultList.add(resultMap);
                        }
                    }


                }
            }
            return resultList;
        }

    }


    public List<TblSystemScm> judgeScmBuildStatus(String systemIds) {
        return tblSystemScmMapper.judgeScmBuildStatus(systemIds);
    }

    public List<Map<String, Object>> selectModuleBuildMessage(Map<String, Object> map) {
        return tblSystemJenkinsJobRunMapper.selectModuleBuildMessage(map);
    }

    public int countTaskBySystemId(Long id) {

        return tblRequirementFeatureMapper.countTaskBySystemId(id);
    }

    public List<TblSystemJenkins> selectJenkinsByMap(Map<String, Object> map) {

        return tblSystemJenkinsMapper.selectByMap(map);
    }

    public List<TblSystemScm> selectBuildingBySystemid(Long id) {
        return tblSystemScmMapper.selectBuildingBySystemid(id);
    }

    public List<TblSystemJenkinsJobRun> selectLastTimeBySystemId(long id) {

        return tblSystemJenkinsJobRunMapper.selectLastTimeBySystemId(id);

    }

    public List<TblSystemJenkinsJobRun> selectLastTimeBySystemIdManual(Long id) {

        return tblSystemJenkinsJobRunMapper.selectLastTimeBySystemIdManual(id);
    }

    public List<TblSystemModule> selectSystemModule(long systemId) {

        return tblSystemModuleMapper.selectSystemModule(systemId);
    }

    public List<Map<String, Object>> selectModuleBuildMessagesNow(Map<String, Object> map) {
        return tblSystemJenkinsJobRunMapper.selectModuleBuildMessagesNow(map);
    }


    public void handleException(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage(), e);

    }

    private Boolean flagAdmin(String userId) {
        Boolean flag = false;
        Map<String, Object> userMap = systemInterface.findUserById(Long.valueOf(userId));
        if (userMap != null) {
            Map<String, Object> user = JSON.parseObject(userMap.get("data").toString());
            JSONArray array = JSON.parseArray(user.get("userRoles").toString());
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = array.getJSONObject(i);
                if (json.get("id").toString().equals("1")) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private int getRandom(int size) {
        SecureRandom random = new SecureRandom();
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int n = random.nextInt(size);
        return n;
    }

    private Boolean judgEnv(String envs, String env) {
        Boolean flag = false;
        if (envs != null) {
            String[] envFlags = envs.split(",");
            for (String envFlag : envFlags) {
                if (envFlag.equals(env)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static String getData(Map<String, String> map, String data) {
        String result = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(data)) {
                result = entry.getKey();
                break;
            }
        }
        return result;
    }

    private boolean judegeSystemEnvs(String environmentType, String env) {
        boolean flag = false;
        String[] systemEnvs = environmentType.split(",");
        for (String envFlag : systemEnvs) {
            if (envFlag.equals(env)) {
                flag = true;
            }
        }
        return flag;
    }

    //???????????????????????????
    private List<TblSystemScm> detailSystemSccms(List<TblSystemScm> tblSystemScms) {
        List<TblSystemScm> tblSystemScmsNew = new ArrayList<>();
        for (TblSystemScm tblSystemScm : tblSystemScms) {
            if (tblSystemScm.getEnvironmentType() != null && !tblSystemScm.getEnvironmentType().equals("")) {

                tblSystemScmsNew.add(tblSystemScm);
            }
        }
        return tblSystemScmsNew;

    }

    private void detailTestInfo(Map<String, Object> map) {
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        TblSystemAutomaticTestResult tblSystemAutomaticTestResult = new TblSystemAutomaticTestResult();
        tblSystemAutomaticTestResultMapper.insert(tblSystemAutomaticTestResult);
        long jobRunId = Long.parseLong(map.get("jobRunId").toString());
        TblSystemJenkinsJobRun tblSystemJenkinsJobRun = tblSystemJenkinsJobRunMapper.selectById(jobRunId);
        String deployStatus = map.get("status").toString();
        tblSystemJenkinsJobRun.setBuildStatus(Integer.parseInt(deployStatus));
        tblSystemJenkinsJobRun.setEndDate(endTime);
        tblSystemJenkinsJobRun.setLastUpdateDate(endTime);
        tblSystemJenkinsJobRunMapper.updateById(tblSystemJenkinsJobRun);
        //??????modulerun??????
        String moduleRunIds = map.get("moduleRunIds").toString();

        for (String moduleRunId : moduleRunIds.split(",")) {
            TblSystemModuleJenkinsJobRun tblSystemModuleJenkinsJobRun = tblSystemModuleJenkinsJobRunMapper.selectById(Long.parseLong(moduleRunId));
            tblSystemModuleJenkinsJobRun.setCreateDate(tblSystemJenkinsJobRun.getStartDate());
            tblSystemModuleJenkinsJobRun.setLastUpdateDate(endTime);
            tblSystemModuleJenkinsJobRunMapper.updateById(tblSystemModuleJenkinsJobRun);
        }
    }

    @Override
    @Transactional
    public void callBackAutoTest(Map<String, Object> map) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String testType = map.get("testType").toString();
            //?????????????????????id
            String jobRunId = map.get("jobRunId").toString();
            String scheduled = map.get("scheduled").toString();
            log.info("------jobRunId=" + jobRunId + "----testType=" + testType);
            Map<String, Object> testConfig = (Map<String, Object>) map.get("testConfig");
            if (testType.equals("1")) {
                log.info("api???????????????");
                if (testConfig.get("subSystemInfo") != null) {//?????????
                    List<Map<String, Object>> subModuleList = (List<Map<String, Object>>) testConfig.get("subSystemInfo");
                    for (Map<String, Object> submap : subModuleList) {
                        if (submap.get("subTestResult") != null && submap.get("subSuccessNumber") != null) {
                            String subTestResult = submap.get("subTestResult").toString();
                            String subSuccessNumber = submap.get("subSuccessNumber").toString();
                            String subFailedNumber = submap.get("subFailedNumber").toString();
                            String subTestResultDetailUrl = submap.get("subTestResultDetailUrl").toString();
                            String subTestRequestNumber = submap.get("subTestRequestNumber").toString();
                            Map<String, Object> paramMap = new HashMap<>();
                            paramMap.put("TEST_REQUEST_NUMBER", subTestRequestNumber);
                            paramMap.put("STATUS", 1);
                            paramMap.put("SYSTEM_MODULE_ID", submap.get("subModuleId").toString());
                            List<TblSystemAutomaticTestResult> resultList = tblSystemAutomaticTestResultMapper.selectByMap(paramMap);
                            log.info("resultList" + resultList.size());
                            if (resultList != null && resultList.size() > 0) {
                                //??????result??????
                                TblSystemAutomaticTestResult tblSystemAutomaticTestResult = resultList.get(0);
                                tblSystemAutomaticTestResult.setTestResult(Integer.parseInt(subTestResult));
                                tblSystemAutomaticTestResult.setFailedNumber(Integer.parseInt(subFailedNumber));
                                tblSystemAutomaticTestResult.setSuccessNumber(Integer.parseInt(subSuccessNumber));
                                tblSystemAutomaticTestResult.setTestResultDetailUrl(subTestResultDetailUrl);
                                tblSystemAutomaticTestResult.setLastUpdateDate(timestamp);
                                tblSystemAutomaticTestResultMapper.updateById(tblSystemAutomaticTestResult);
                                if (isTestResultEnd(jobRunId)) {
                                    //??????????????????
                                    detailTestJobRunInfo(map, timestamp);
                                }

                            }
                        }
                    }
                } else {
                    String testResult = map.get("testResult").toString();
                    String successNumber = map.get("successNumber").toString();
                    String failedNumber = map.get("failedNumber").toString();
                    String testResultDetailUrl = map.get("testResultDetailUrl").toString();
                    String testQeuestNumber = map.get("testRequestNumber").toString();
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("TEST_REQUEST_NUMBER", testQeuestNumber);
                    paramMap.put("STATUS", 1);
                    List<TblSystemAutomaticTestResult> resultList = tblSystemAutomaticTestResultMapper.selectByMap(paramMap);
                    log.info("resultList" + resultList.size());
                    if (resultList != null && resultList.size() > 0) {
                        //??????result??????
                        TblSystemAutomaticTestResult tblSystemAutomaticTestResult = resultList.get(0);
                        tblSystemAutomaticTestResult.setTestResult(Integer.parseInt(testResult));
                        tblSystemAutomaticTestResult.setFailedNumber(Integer.parseInt(failedNumber));
                        tblSystemAutomaticTestResult.setSuccessNumber(Integer.parseInt(successNumber));
                        tblSystemAutomaticTestResult.setTestResultDetailUrl(testResultDetailUrl);
                        tblSystemAutomaticTestResult.setLastUpdateDate(timestamp);
                        tblSystemAutomaticTestResultMapper.updateById(tblSystemAutomaticTestResult);
                        if (isTestResultEnd(jobRunId)) {
                            //??????????????????
                            detailTestJobRunInfo(map, timestamp);
                        }

                    }


                }
            } else {
                //ui???????????????
                log.info("ui???????????????");
                String testResult = map.get("testResult").toString();
                String successNumber = map.get("successNumber").toString();
                String failedNumber = map.get("failedNumber").toString();
                String testResultDetailUrl = map.get("testResultDetailUrl").toString();
                String testQeuestNumber = map.get("testRequestNumber").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("TEST_REQUEST_NUMBER", testQeuestNumber);
                log.info("TEST_REQUEST_NUMBER=" + testQeuestNumber);
                paramMap.put("STATUS", 1);
                List<TblSystemAutomaticTestResult> resultList = tblSystemAutomaticTestResultMapper.selectByMap(paramMap);
                log.info("resultList" + resultList.size());
                if (resultList != null && resultList.size() > 0) {
                    //??????result??????
                    TblSystemAutomaticTestResult tblSystemAutomaticTestResult = resultList.get(0);
                    tblSystemAutomaticTestResult.setTestResult(Integer.parseInt(testResult));
                    tblSystemAutomaticTestResult.setFailedNumber(Integer.parseInt(failedNumber));
                    tblSystemAutomaticTestResult.setSuccessNumber(Integer.parseInt(successNumber));
                    tblSystemAutomaticTestResult.setTestResultDetailUrl(testResultDetailUrl);
                    tblSystemAutomaticTestResult.setLastUpdateDate(timestamp);
                    tblSystemAutomaticTestResultMapper.updateById(tblSystemAutomaticTestResult);
                    if (isTestResultEnd(jobRunId)) {
                        //??????????????????
                        detailTestJobRunInfo(map, timestamp);
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("??????" + ":" + e.getMessage(), e);
        }
    }

    @Override
    public String goContinueLog(String url, String toolId) {
        TblToolInfo jenkinsToolInfo = tblToolInfoMapper.selectByPrimaryKey(Long.parseLong(toolId));
        try {
            iJenkinsBuildService.continuePipeline(jenkinsToolInfo, url);
            return "??????????????????????????????";
        } catch (Exception e) {
            this.handleException(e);
            return "???????????????????????????";
        }
    }

    private void detailTestJobRunInfo(Map<String, Object> map, Timestamp timestamp) {
        log.info("????????????????????????map" + JSON.toJSONString(map, true));
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = sdf.format(timestamp);
        String jobRunId = map.get("jobRunId").toString();
        String scheduled = map.get("scheduled").toString();
        //????????????????????????????????????
        if (scheduled.equals("false")) { //????????????????????????
            String deployType = map.get("deployType").toString();
            //Integer envType=Integer.parseInt(map.get("envType").toString());
            if (deployType.equals("1")) {//????????????
                String scmId = map.get("scmId").toString();
                TblSystemScm tblSystemScm = tblSystemScmMapper.selectById(scmId);
                tblSystemScm.setDeployStatus(1);
                tblSystemScmMapper.updateById(tblSystemScm);
            } else {
                String jenkinsId = map.get("jenkinsId").toString();
                TblSystemJenkins tblSystemJenkins = tblSystemJenkinsMapper.selectById(jenkinsId);
                tblSystemJenkins.setDeployStatus(1);
                tblSystemJenkinsMapper.updateById(tblSystemJenkins);
            }

        }
        TblSystemJenkinsJobRun tblSystemJenkinsJobRun = tblSystemJenkinsJobRunMapper.selectById(jobRunId);
        //?????????jobrun?????????????????????
        Map<String, Object> judgeParam = new HashMap<>();
        judgeParam.put("SYSTEM_JENKINS_JOB_RUN", jobRunId);
        judgeParam.put("status", 1);
        judgeParam.put("TEST_RESULT", 2);
        List<TblSystemAutomaticTestResult> judgeList = tblSystemAutomaticTestResultMapper.selectByMap(judgeParam);
        if (judgeList.size() > 0) {//???????????????
            tblSystemJenkinsJobRun.setBuildStatus(5);
        } else {
            tblSystemJenkinsJobRun.setBuildStatus(2);
        }
        tblSystemJenkinsJobRun.setEndDate(timestamp);
        tblSystemJenkinsJobRun.setLastUpdateDate(timestamp);


        String bucketName = tblSystemJenkinsJobRun.getBuildLogs().split(",")[1];
        String key = tblSystemJenkinsJobRun.getBuildLogs().split(",")[0];
        String log = s3Util.getStringByS3(bucketName, key);
        //String log=tblSystemJenkinsJobRun.getBuildLogs();
        byte[] bytes = new byte[0];
        try {
            bytes = log.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log = log + "\n???????????????????????????:\n" + date;
        //tblSystemJenkinsJobRun.setBuildLogs(log);
        s3Util.deleteObject(bucketName, key);

        s3Util.putObjectLogs(bucketName, key, log);

        tblSystemJenkinsJobRunMapper.updateById(tblSystemJenkinsJobRun);
        List<Map<String, Object>> maps = tblSystemModuleJenkinsJobRunMapper.selectModuleJobRunByjobRunId(Long.parseLong(jobRunId));
        for (Map<String, Object> bean : maps) {
            Map<String, Object> param = new HashMap<>();
            param.put("SYSTEM_JENKINS_JOB_RUN", jobRunId);
            param.put("status", 1);
            long id = (long) bean.get("ID");
            param.put("SYSTEM_MODULE_ID", id);
            List<TblSystemAutomaticTestResult> moduleResultList = tblSystemAutomaticTestResultMapper.selectByMap(param);
            TblSystemModuleJenkinsJobRun tblSystemModuleJenkinsJobRun = tblSystemModuleJenkinsJobRunMapper.selectById(id);
            if (moduleResultList != null && moduleResultList.size() > 0) {
                if (moduleResultList.get(0).getTestResult() == 1) {
                    tblSystemModuleJenkinsJobRun.setBuildStatus(2);
                } else {
                    tblSystemModuleJenkinsJobRun.setBuildStatus(5);
                }


            } else {
                tblSystemModuleJenkinsJobRun.setBuildStatus(5);
            }
            tblSystemModuleJenkinsJobRun.setLastUpdateDate(timestamp);
            //tblSystemModuleJenkinsJobRun.setCreateDate(tblSystemJenkinsJobRun.getStartDate());
            tblSystemModuleJenkinsJobRunMapper.updateById(tblSystemModuleJenkinsJobRun);
        }
        //????????????????????????
        if (map.get("taskMapFlag").toString().equals("2") && scheduled.equals("false")) {
            Map<String, Object> taskMap = (Map<String, Object>) map.get("taskMap");
            detailReqstatus(taskMap);
        }

        //????????????
        sendMessage(tblSystemJenkinsJobRun, map, "2");

    }


    private boolean isTestResultEnd(String jobRunId) {
        Map<String, Object> param = new HashMap<>();
        param.put("SYSTEM_JENKINS_JOB_RUN", jobRunId);
        param.put("status", 1);
        List<TblSystemAutomaticTestResult> list = tblSystemAutomaticTestResultMapper.selectByMap(param);
        int flag = 0;
        for (TblSystemAutomaticTestResult tr : list) {
            if (tr.getTestResult() == null && tr.getTestResultDetailUrl() == null) {
                flag = flag + 1;

            }
        }
        log.info("????????????????????????flag=" + flag);
        if (flag == 0) {
            return true;
        } else {
            return false;
        }


    }


    private void detailReqstatus(Map<String, Object> taskMap) {
        log.info("??????????????????taskmap--------->>>>>>" + JSON.toJSONString(taskMap, true));
        Map<String, Object> sendMap = new HashMap<>();
        String reqFeatureqIds = taskMap.get("reqFeatureIds").toString();
        String userId = taskMap.get("userId").toString();
        String userAccount = taskMap.get("userAccount").toString();
        String envType = taskMap.get("envType").toString();
        String status = taskMap.get("status").toString();
        String userName = taskMap.get("userName").toString();
        sendMap.put("userId", userId);
        sendMap.put("userAccount", userAccount);
        sendMap.put("userName", userName);
        String sendTask = JSON.toJSONString(sendMap);
        Map<String, Object> remap = new HashMap<>();
        remap.put("reqFeatureIds", reqFeatureqIds);
        remap.put("requirementFeatureFirstTestDeployTime", new Timestamp(new Date().getTime()));
        String json = JsonUtil.toJson(remap);
        devTaskController.updateDeployStatus(reqFeatureqIds, envType, sendTask, Integer.parseInt(status));
        devTaskService.updateReqFeatureTimeTraceForDeploy(json);
    }

    private String getEnvName(String envType, String termCode) {
        Map<String, Object> param = new HashMap<>();
        param.put("envType", envType);
        param.put("termCode", termCode);
        List<Map<String, Object>> lists = tblSystemJenkinsJobRunMapper.getEnvName(param);
        if (lists.size() > 0) {
            return lists.get(0).get("VALUE_NAME").toString();
        } else {
            return "";
        }
    }

    private void getDeleteEnvName(String termCode, Map<String, Object> envMap) {
        Map<String, Object> param = new HashMap<>();
        param.put("status", "2");
        param.put("termCode", termCode);
        List<Map<String, Object>> lists = tblSystemJenkinsJobRunMapper.getDeleteEnvName(param);


        for (Map<String, Object> map : lists) {
            String valueName = map.get("VALUE_NAME").toString();
            String valueCode = map.get("VALUE_CODE").toString();
            envMap.put(valueCode, valueName);
        }

    }

    private boolean judgeDeleteEnv(String termCode, String envType) {
        boolean flag = false;
        Map<String, Object> param = new HashMap<>();
        param.put("status", "2");
        param.put("termCode", termCode);
        List<Map<String, Object>> lists = tblSystemJenkinsJobRunMapper.getDeleteEnvName(param);
        for (Map<String, Object> map : lists) {

            String valueCode = map.get("VALUE_CODE").toString();
            if (valueCode.equals(envType)) {
                flag = true;

            }
        }
        return flag;
    }


    private void getTestResult(Map<String, Object> map, String jonRunId, String systemId) {
        Map<String, Object> testSceneMap = JsonUtil.fromJson((String) redisUtils.get("TEST_SCENE"), Map.class);
        Map<String, Object> param = new HashMap<>();
        param.put("SYSTEM_JENKINS_JOB_RUN", jonRunId);
        param.put("SYSTEM_ID", systemId);
        param.put("status", 1);
        List<Map<String, Object>> testResultList = new ArrayList<>();
        List<Map<String, Object>> uiTestResultList = new ArrayList<>();
        List<TblSystemAutomaticTestResult> lists = tblSystemAutomaticTestResultMapper.selectByMap(param);
        for (TblSystemAutomaticTestResult t : lists) {
            Map<String, Object> itmap = new HashMap<>();
            if (t.getSystemModuleId() != null) {
                TblSystemModule tblSystemModule = tblSystemModuleMapper.selectById(t.getSystemModuleId());
                String moduleName = tblSystemModule.getModuleName();
                itmap.put("moduleName", moduleName);
            } else {
                TblSystemInfo tblSystemInfo = tblSystemInfoMapper.selectById(t.getSystemId());
                itmap.put("systemName", tblSystemInfo.getSystemName());
            }

            Integer successNumber = t.getSuccessNumber();
            Integer failedNumber = t.getFailedNumber();
            String testResultDetailUrl = t.getTestResultDetailUrl();
            Integer testResult = t.getTestResult();
            Integer testType = t.getTestType();


            itmap.put("successNumber", successNumber);
            itmap.put("failedNumber", failedNumber);
            if (testResult == 1) {
                itmap.put("testResult", "??????");
            } else {
                itmap.put("testResult", "??????");
            }

            itmap.put("testResultDetailUrl", testResultDetailUrl);
            String testScene = t.getTestScene();
            if (testSceneMap.get(testScene) != null) {
                itmap.put("testScene", testSceneMap.get(testScene).toString());
            } else {
                itmap.put("testScene", "?????????");
            }
            if (testType != null) {
                if (testType == 1) {
                    itmap.put("testType", "API???????????????");
                    testResultList.add(itmap);
                } else {
                    itmap.put("testType", "UI???????????????");
                    uiTestResultList.add(itmap);
                }
            }


        }
        map.put("testResultList", testResultList);
        map.put("uiTestResultList", uiTestResultList);

    }


    @Override
    public void detailTestRequestNumber(Map<String, Object> autoMap) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Map<String, Object> testConfig = (Map<String, Object>) autoMap.get("testConfig");
        String systemId = autoMap.get("systemId").toString();
        String jobRunId = autoMap.get("jobRunId").toString();
        String environnmentType = autoMap.get("environnmentType").toString();
        String testScene = testConfig.get("testSceneCode").toString();
        if (autoMap.get("testType").equals("1")) { //api???????????????
            if (testConfig.get("subSystemInfo") != null) { //?????????
                List<Map<String, Object>> subModuleList = (List<Map<String, Object>>) testConfig.get("subSystemInfo");
                for (Map<String, Object> subMap : subModuleList) {
                    String testRequestNumber = subMap.get("subTestRequestNumber").toString();
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("TEST_REQUEST_NUMBER", testRequestNumber);
                    paramMap.put("TEST_SCENE", testScene);
                    paramMap.put("SYSTEM_MODULE_ID", subMap.get("subModuleId").toString());
                    paramMap.put("STATUS", 1);
                    paramMap.put("TEST_TYPE", 1);
                    List<TblSystemAutomaticTestResult> flagList = tblSystemAutomaticTestResultMapper.selectByMap(paramMap);
                    if (flagList.size() > 0) {
                        log.info("????????????????????????:" + testRequestNumber + "--????????????");

                    } else {

                        TblSystemAutomaticTestResult tblSystemAutomaticTestResult = new TblSystemAutomaticTestResult();
                        tblSystemAutomaticTestResult.setSystemJenkinsJobRun(Long.parseLong(jobRunId));
                        tblSystemAutomaticTestResult.setTestRequestNumber(testRequestNumber);
                        tblSystemAutomaticTestResult.setTestScene(testScene);
                        tblSystemAutomaticTestResult.setEnvironmentType(Integer.parseInt(environnmentType));
                        tblSystemAutomaticTestResult.setCreateDate(timestamp);
                        tblSystemAutomaticTestResult.setTestType(1);
                        tblSystemAutomaticTestResult.setSystemId(Long.parseLong(systemId));
                        tblSystemAutomaticTestResult.setStatus(1);
                        tblSystemAutomaticTestResult.setSystemModuleId(Long.parseLong(subMap.get("subModuleId").toString()));
                        tblSystemAutomaticTestResultMapper.insertNew(tblSystemAutomaticTestResult);
                    }
                }
            } else {
                //????????????
                String testRequestNumber = autoMap.get("testRequestNumber").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("TEST_REQUEST_NUMBER", testRequestNumber);
                paramMap.put("TEST_SCENE", testScene);
                paramMap.put("STATUS", 1);
                paramMap.put("TEST_TYPE", 1);

                List<TblSystemAutomaticTestResult> flagList = tblSystemAutomaticTestResultMapper.selectByMap(paramMap);
                if (flagList.size() > 0) {
                    log.info("????????????????????????:" + testRequestNumber + "--????????????");
                } else {
                    TblSystemAutomaticTestResult tblSystemAutomaticTestResult = new TblSystemAutomaticTestResult();
                    tblSystemAutomaticTestResult.setSystemJenkinsJobRun(Long.parseLong(jobRunId));
                    tblSystemAutomaticTestResult.setTestRequestNumber(testRequestNumber);
                    tblSystemAutomaticTestResult.setTestScene(testScene);
                    tblSystemAutomaticTestResult.setEnvironmentType(Integer.parseInt(environnmentType));
                    tblSystemAutomaticTestResult.setCreateDate(timestamp);
                    tblSystemAutomaticTestResult.setTestType(1);
                    tblSystemAutomaticTestResult.setStatus(1);
                    tblSystemAutomaticTestResult.setSystemId(Long.parseLong(systemId));
                    tblSystemAutomaticTestResultMapper.insert(tblSystemAutomaticTestResult);
                }

            }
        } else {
            //ui????????????
            String testRequestNumber = autoMap.get("testRequestNumber").toString();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("TEST_REQUEST_NUMBER", testRequestNumber);
            paramMap.put("TEST_SCENE", testScene);
            paramMap.put("STATUS", 1);
            paramMap.put("TEST_TYPE", 2);

            List<TblSystemAutomaticTestResult> flagList = tblSystemAutomaticTestResultMapper.selectByMap(paramMap);
            if (flagList.size() > 0) {
                log.info("????????????????????????:" + testRequestNumber + "--????????????");
            } else {
                TblSystemAutomaticTestResult tblSystemAutomaticTestResult = new TblSystemAutomaticTestResult();
                tblSystemAutomaticTestResult.setSystemJenkinsJobRun(Long.parseLong(jobRunId));
                tblSystemAutomaticTestResult.setTestRequestNumber(testRequestNumber);
                tblSystemAutomaticTestResult.setTestScene(testScene);
                tblSystemAutomaticTestResult.setEnvironmentType(Integer.parseInt(environnmentType));
                tblSystemAutomaticTestResult.setCreateDate(timestamp);
                tblSystemAutomaticTestResult.setSystemId(Long.parseLong(systemId));
                tblSystemAutomaticTestResult.setTestType(2);
                tblSystemAutomaticTestResult.setStatus(1);
                tblSystemAutomaticTestResultMapper.insert(tblSystemAutomaticTestResult);
            }


        }

        //	}


    }


    @Override
    public String judgeFeatureIds(Long[] featureIds) {
        String result = "";
        for (int i = 0; i < featureIds.length; i++) {
            TblRequirementFeature tblRequirementFeature = tblRequirementFeatureMapper.selectById(featureIds[i]);
            if (tblRequirementFeature.getRequirementId() == null) {

                result = tblRequirementFeature.getFeatureCode() + ",";
            }
        }
        if (!result.equals("")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;

    }

    @Override
    public void detailEnvDate(String reqFeatureqIds, String envType, Timestamp timestamp) {
        if (reqFeatureqIds != null && !reqFeatureqIds.equals("")) {
            List<Map<String, Object>> list = new ArrayList<>();
            Object tblToolInfo = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
            Map<String, Object> envMap = JSON.parseObject(tblToolInfo.toString());
            String envName = envMap.get(envType).toString();
            if (envName.startsWith(Constants.XICE) || envName.startsWith(Constants.BANCE)) {
                String[] ids = reqFeatureqIds.split(",");
                for (String id : ids) {
                    TblRequirementFeature tblRequirementFeature = tblRequirementFeatureMapper.selectById(Long.parseLong(id));
                    if (tblRequirementFeature.getSystemId() != null && tblRequirementFeature.getRequirementId() != null) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("requirement", tblRequirementFeature.getRequirementId().toString());
                        map.put("systemId", tblRequirementFeature.getSystemId().toString());
                        list.add(map);
                    }
                }

                String timestampStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
                devManageToTestManageInterface.detailEnvDate(JSON.toJSONString(list), envName, timestampStr);

            }
        }


    }

    /*??????????????????????????? liushan */
    private void sendMessage(TblSystemJenkinsJobRun tblSystemJenkinsJobRun, Map<String, Object> backMap, String jobType) {
        TblSystemInfo tblSystemInfo = iStructureService.getTblSystemInfoById(tblSystemJenkinsJobRun.getSystemId());
        if (tblSystemInfo.getProjectIds() != null && !tblSystemInfo.getProjectIds().equals("") && backMap.get("userId") != null && backMap.get("envName") != null) {
            String creatUserId = backMap.get("userId").toString();
            String envName = backMap.get("envName").toString();
            Map<String, Object> map = new HashMap<>();
            String stauts = "";
            if (jobType.equals("1")) {
                map.put("messageTitle", tblSystemInfo.getSystemName() + "-" + envName + "-" + (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "????????????" : "????????????"));
            } else {
                map.put("messageTitle", tblSystemInfo.getSystemName() + "-" + envName + "-" + (tblSystemJenkinsJobRun.getBuildStatus() == 2 ? "????????????" : "????????????"));
            }
            map.put("messageContent", "????????????" + tblSystemJenkinsJobRun.getStartDate() + "-????????????" + tblSystemJenkinsJobRun.getEndDate());
            map.put("messageReceiverScope", 2);
            Map<String, Object> paramMap = new HashMap<>();
            String[] projects = tblSystemInfo.getProjectIds().split(",");
            for (String project : projects) {
                paramMap.put("projectId", project);
                String userIds = "";
                List<Map<String, Object>> list = tblProjectInfoMapper.getUserIdByProjectId(paramMap);
                for (Map<String, Object> userMap : list) {
                    userIds = userIds + userMap.get("userId").toString() + ",";
                }
                if (!userIds.equals("")) {
                    userIds = userIds.substring(0, userIds.length() - 1);
                }
                //????????????????????????
                map.put("messageReceiver", userIds);
                map.put("createBy", creatUserId);
                devManageToSystemInterface.insertMessage(JSON.toJSONString(map));
            }
        }


    }


    private void synDefectInfo(Long featureId) {
        List<TblDefectInfo> defectInfoList = tblRequirementFeatureMapper.findDftByReqFId(featureId);
        for (TblDefectInfo tblDefectInfo : defectInfoList) {
            if (tblDefectInfo.getCreateType() == 2) {
                Map<String, Object> mapAll = new LinkedHashMap<>();
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("problemNumber", tblDefectInfo.getDefectCode());
                if (tblDefectInfo.getCheckStatus() == 1) {
                    requestBody.put("operationType", 2);
                } else if (tblDefectInfo.getCheckStatus() == 3) {
                    requestBody.put("operationType", 3);
                }
                requestBody.put("comment", "");
                requestBody.put("releaseTime", new Timestamp(new Date().getTime()));
                mapAll.put("requestHead", DataBusRequestHead.getRequestHead());
                mapAll.put("requestBody", requestBody);

                String result = JSON.toJSONString(mapAll, SerializerFeature.WriteDateUseDateFormat);
                DataBusUtil.send(databusccName, tblDefectInfo.getDefectCode(), result);

            }
        }
    }

    /**
     * ??????????????????
     *
     * @param systemId ??????id
     * @return List<TblSystemDeployeUserConfig>s
     * @author weiji
     */
    @Override
    public List<TblSystemDeployeUserConfig> getDeployUsers(Long systemId) {

        Object tblToolInfo = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
        Map<String, Object> envMap = JSON.parseObject(tblToolInfo.toString());
        Map<String, Object> param = new HashMap<>();
        param.put("SYSTEM_ID", systemId);
        param.put("STATUS", 1);
        List<TblSystemDeployeUserConfig> tblSystemDeployeUserConfigs = tblSystemDeployeUserConfigMapper.selectByMap(param);
        for (TblSystemDeployeUserConfig tblSystemDeployeUserConfig : tblSystemDeployeUserConfigs) {
            String userIds = tblSystemDeployeUserConfig.getUserIds();
            List<TblUserInfo> tblUserInfos = new ArrayList<>();
            tblSystemDeployeUserConfig.setEnvironmentTypeName(envMap.get(tblSystemDeployeUserConfig.getEnvironmentType().toString()).toString());
            for (String userId : userIds.split(",")) {
                TblUserInfo tblUserInfo = tblUserInfoMapper.getUserById(Long.parseLong(userId));
                tblUserInfos.add(tblUserInfo);
            }
            tblSystemDeployeUserConfig.setTblUserInfos(tblUserInfos);
        }
        return tblSystemDeployeUserConfigs;
    }

    /**
     * ?????????????????????
     *
     * @param tblSystemDeployeUserConfig ???????????????????????????
     * @return
     * @author weiji
     */

    @Override
    public void addDeployUser(TblSystemDeployeUserConfig tblSystemDeployeUserConfig, HttpServletRequest request) {
        Long currentUserId = CommonUtil.getCurrentUserId(request);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        tblSystemDeployeUserConfig.setCreateDate(timestamp);
        tblSystemDeployeUserConfig.setCreateBy(currentUserId);
        tblSystemDeployeUserConfig.setStatus(1);
        tblSystemDeployeUserConfigMapper.insertSelective(tblSystemDeployeUserConfig);
    }

    /**
     * ??????????????????
     *
     * @param tblSystemDeployeUserConfig ???????????????????????????
     * @return
     * @author weiji
     */
    @Override
    public void updateDeployUser(TblSystemDeployeUserConfig tblSystemDeployeUserConfig, HttpServletRequest request) {

        Long currentUserId = CommonUtil.getCurrentUserId(request);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        tblSystemDeployeUserConfig.setLastUpdateDate(timestamp);
        tblSystemDeployeUserConfig.setLastUpdateBy(currentUserId);

        tblSystemDeployeUserConfigMapper.updateByPrimaryKeySelective(tblSystemDeployeUserConfig);
    }

    /**
     * ??????????????????
     *
     * @param id ???????????????????????????id
     * @return
     * @author weiji
     */
    @Override
    public void deleteDeployUser(Long id, HttpServletRequest request) {
        TblSystemDeployeUserConfig tblSystemDeployeUserConfig = new TblSystemDeployeUserConfig();
        tblSystemDeployeUserConfig.setId(id);
        Long currentUserId = CommonUtil.getCurrentUserId(request);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        tblSystemDeployeUserConfig.setLastUpdateDate(timestamp);
        tblSystemDeployeUserConfig.setLastUpdateBy(currentUserId);
        tblSystemDeployeUserConfig.setStatus(2);
        tblSystemDeployeUserConfigMapper.updateByPrimaryKeySelective(tblSystemDeployeUserConfig);
    }

    /**
     * ????????????????????????id
     *
     * @param systemId ??????id
     * @return
     * @author weiji
     */
    @Override
    public List<Map<String, Object>> getEnvsBySystemId(long systemId) {
        Object tblToolInfo = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
        Map<String, Object> envMap = JSON.parseObject(tblToolInfo.toString());
        List<Map<String, Object>> list = new ArrayList<>();
        TblSystemInfo tblSystemInfo = tblSystemInfoMapper.selectById(systemId);

        Map<String, Object> param = new HashMap<>();
        param.put("SYSTEM_ID", systemId);
        param.put("STATUS", 1);
        List<TblSystemDeployeUserConfig> tblSystemDeployeUserConfigs = tblSystemDeployeUserConfigMapper.selectByMap(param);
        List<String> envs = new ArrayList<>();
        for (TblSystemDeployeUserConfig tblSystemDeployeUserConfig : tblSystemDeployeUserConfigs) {
            envs.add(tblSystemDeployeUserConfig.getEnvironmentType().toString());
        }
        if (tblSystemInfo.getEnvironmentType() != null) {
            for (String envType : tblSystemInfo.getEnvironmentType().split(",")) {
                if (!envs.contains(envType)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("envVaule", envType);
                    map.put("envName", envMap.get(envType));
                    list.add(map);
                }

            }
        }
        return list;
    }
}