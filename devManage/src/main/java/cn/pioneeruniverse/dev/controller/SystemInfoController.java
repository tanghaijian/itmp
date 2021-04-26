package cn.pioneeruniverse.dev.controller;

import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.databus.DataBusRequestHead;
import cn.pioneeruniverse.common.databus.DataBusUtil;
import cn.pioneeruniverse.common.dto.SystemTreeVo;
import cn.pioneeruniverse.dev.dao.mybatis.TblTopSystemInfoMapper;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.feignInterface.DevManageToProjectManageInterface;
import cn.pioneeruniverse.dev.vo.TaskProjectVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.StringUtil;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.service.structure.IStructureService;
import cn.pioneeruniverse.dev.service.systeminfo.ISystemInfoService;
import cn.pioneeruniverse.dev.service.systemversion.ISystemVersionService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 系统信息配置 Controller
 *
 * @author:tingting
 * @version:2018年10月30日 下午3:38:26
 */

@RestController
@RequestMapping("systeminfo")
public class SystemInfoController extends BaseController {
    @Autowired
    private TblTopSystemInfoMapper tblTopSystemInfoMapper;

    @Autowired
    private ISystemInfoService systemInfoService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IStructureService iStructureService;
    @Autowired
    private ISystemVersionService systemVersionService;
    @Autowired
    private StructureController structureController;
    @Autowired
    private DeployController deployController;
    @Autowired
    private DevManageToProjectManageInterface toProjectManageInterface;
    @Value("${databuscc.automat.test}")
    private String databusccName;

    private final static Logger log = LoggerFactory.getLogger(SystemInfoController.class);


  /**
   *  查询系统信息
   * @param systemInfo 查询参数
   * @param status 系统状态
   * @param pages
   * @param row
   * @param request
   * @return Map<String, Object> key：value  parent 父节点， architectureType 系统架构， lastUpdateDate 更新时间，
   *     groupId "" createType 1 isLeaf false deployType 1 loaded true expanded false topSystemId
   *     systemName 系统名称， developmentMode 开发模式， id 主键id， createDate 创建日期，
   *     key_id 节点id， level 节点级别， snapshotRepositoryName 仓库名称，
   *     releaseRepositoryName "" productionDeployType 部署方式 ， createBy 创建者， systemCode
   *     系统编号 ，lastUpdateBy 更新人
   */
  @RequestMapping(value = "getAllSystemInfo", method = RequestMethod.POST)
  public Map<String, Object> getAllSystemInfo(
      TblSystemInfo systemInfo,
      Integer status,
      Integer pages,
      Integer row,
      HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        if (status != null) {
            systemInfo.setStatus(status);
        }
        try {
            Long uid = CommonUtil.getCurrentUserId(request);
            LinkedHashMap codeMap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
            List<String> roleCodes = (List<String>) codeMap.get("roles");

            //List<Map<String, Object>> list = systemInfoService.getAllSystemInfo(systemInfo, uid, roleCodes, pages, row);
            List<Map<String, Object>> list2 = systemInfoService.getAllSystemInfo(systemInfo, uid, roleCodes, 1, Integer.MAX_VALUE);

            //查询出此list下的topsystemid 记下前面分页个数 截取此id
            List<String> topIds = systemInfoService.getAllSystemInfoByTop(systemInfo, uid, roleCodes, pages, row);
            List<String> all = systemInfoService.getAllSystemInfoByTop(systemInfo, uid, roleCodes, 1, Integer.MAX_VALUE);

            List<Map<String, Object>> modules = new ArrayList<>();

            for(String topId:topIds){
                TblTopSystemInfo tblTopSystemInfo=tblTopSystemInfoMapper.selectById( Long .parseLong(topId));
                String topSystemName=tblTopSystemInfo.getSystemName();
                String topSystemCode=tblTopSystemInfo.getSystemCode();
                String topSystemId=tblTopSystemInfo.getId().toString();
                String topStatus=tblTopSystemInfo.getStatus().toString();
                Map<String,Object> topMap=new HashMap<>();
                topMap.put("parent","");
                topMap.put("isLeaf", false);
                topMap.put("level", 0);
                topMap.put("expanded", false);
                //顶级系统id标识，用于树形展示时判断
                topMap.put("key_id", "top"+topSystemId);
                topMap.put("systemName",topSystemName);
                topMap.put("systemCode",topSystemCode);
                topMap.put("id",topSystemId);
                topMap.put("projectId","");
                topMap.put("projectName","");
                topMap.put("groupId","");
                topMap.put("artifactId","");
                topMap.put("architectureType","");
                if (topStatus .equals("1")) {
                    topMap.put("status","有效");
                } else if (topStatus .equals("2")) {
                    topMap.put("status","无效");
                }

                modules.add(topMap);

                for (Map<String, Object> map2 : list2) {
                    //查询该系统顶层系统
                    String systemTopId= String.valueOf(map2.get("topSystemId"));

                    if(Objects.equals(systemTopId,topId)){
                        Long systemId = (Long) map2.get("id");
                        String parent_id = "parent_" + systemId;
                        map2.put("key_id", parent_id);
                        Integer status2 = (Integer) map2.get("status");
                        if (status2 == 1) {
                            map2.put("status", "有效");
                        } else if (status2 == 2) {
                            map2.put("status", "无效");
                        }
                        Integer architectureType = (Integer) map2.get("architectureType");
                        if (architectureType != null) {
                            if (2 == architectureType) {
                                map2.put("isLeaf", true);
                            } else if (1 == architectureType) {
                                map2.put("isLeaf", false);
                            }
                        } else {
                            map2.put("isLeaf", true);
                        }

                        map2.put("level", 1);
                        map2.put("expanded", false);
                        map2.put("loaded", true);
                        map2.put("parent", "top"+topSystemId);

                        modules.add(map2);

                        List<Map<String, Object>> module = systemInfoService.getSystemModuleBySystemId(systemId);
                        for (Map<String, Object> map3 : module) {
                            String moduleCode = (String) map3.get("moduleCode");
                            String moduleName = (String) map3.get("moduleName");
                            map3.remove("moduleCode");
                            map3.remove("moduleName");
                            map3.put("systemName", moduleName);
                            map3.put("systemCode", moduleCode);
                            Long moduleId = (Long) map3.get("id");
                            String child_id = "child_" + moduleId;
                            map3.put("key_id", child_id);
                            Integer status3 = (Integer) map3.get("status");
                            if (status3 == 1) {
                                map3.put("status", "有效");
                            } else if (status3 == 2) {
                                map3.put("status", "无效");
                            }
                            map3.put("level", 2);
                            map3.put("isLeaf", true);
                            map3.put("parent", "parent_" + systemId);
                            map3.put("expanded", false);
                            map3.put("loaded", true);
                        }
                        modules.addAll(module);

                    }
                }

            }

            map.put("rows", modules);
            map.put("total", all.size());
        } catch (Exception e) {
            super.handleException(e, e.getMessage());
            map.put("status", "fail");
        }

        return map;
    }

    /*
     * @RequestMapping(value = "getAllSystemInfo", method = RequestMethod.POST)
     * public JqGridPage<TblSystemInfo> getAllSystemInfo2(TblSystemInfo systemInfo,
     * HttpServletRequest request, HttpServletResponse response) {
     * JqGridPage<TblSystemInfo> jqGridPage = systemInfoService.findSystemInfo(new
     * JqGridPage<>(request, response), systemInfo); return jqGridPage; }
     */
    /**
     * 
    * @Title: selectAllSystemInfo
    * @Description: 查询所有子系统，一般用于弹框中选择子系统
    * @author author
    * @param systemInfo 封装的查询条件
    * @param pageNumber 第几页
    * @param pageSize 每页数量
    * @param request
    * @param projectId 项目id
    * @return Map<String,Object>
     */
    @RequestMapping(value = "selectAllSystemInfo")
    public Map<String, Object> selectAllSystemInfo(TblSystemInfo systemInfo, Integer pageNumber, Integer pageSize, HttpServletRequest request,Long projectId) {
        Map<String, Object> map = new HashMap<>();
        try {
            Long uid = CommonUtil.getCurrentUserId(request);
            LinkedHashMap codeMap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
            List<String> roleCodes = (List<String>) codeMap.get("roles");
            List<Map<String, Object>> list = systemInfoService.getAllSystemInfo(systemInfo, uid, roleCodes, pageNumber, pageSize,projectId);
            List<Map<String, Object>> list2 = systemInfoService.getAllSystemInfo(systemInfo, uid, roleCodes, 1,Integer.MAX_VALUE,projectId);
            //返回的数据
            map.put("rows", list);
            //总条数
            map.put("total", list2.size());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "fail");
        }
        return map;
    }


    /**
     * 获取项目信息
     * @param projectName 项目名称
     * @param projectCode 项目编码
     * @param request
     * @return Map<String, Object>
     */
    @RequestMapping(value = "getProjectListByProjectName")
    public Map<String, Object> selectProjectInfoAll(String projectName,String projectCode, HttpServletRequest request,Integer pageNumber, Integer pageSize,Integer sprintType) {
        Map<String, Object> map = new HashMap<>();
        try {
            Long uid = CommonUtil.getCurrentUserId(request);
            LinkedHashMap codeMap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
            List<String> roleCodes = (List<String>) codeMap.get("roles");
            List<TaskProjectVO> projectInfo = systemInfoService.getProjectListByProjectName(projectName,projectCode, uid, roleCodes,pageNumber,pageSize,sprintType);
            List<TaskProjectVO> count = systemInfoService.getProjectListByProjectName(projectName,projectCode, uid, roleCodes,1,Integer.MAX_VALUE,sprintType);
            //项目信息
            map.put("projectInfo",projectInfo);
            //总条数
            map.put("total", count.size());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "error");
        }
        return  map;
    }



    /**
     * 
    * @Title: selectAllSystemInfo2
    * @Description: 查询系统信息，并且排除notIds这些系统
    * @author author
    * @param systemInfo 封装的查询条件
    * @param pageNumber 第几页
    * @param pageSize 每页数量
    * @param flag 1排除notIds
    * @param notIds 排除在外的系统id
    * @param request
    * @return Map<String,Object>
     */
    @RequestMapping(value = "selectAllSystemInfo2")
    public Map<String, Object> selectAllSystemInfo2(TblSystemInfo systemInfo, Integer pageNumber, Integer pageSize,String flag,String notIds,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            //Long uid = CommonUtil.getCurrentUserId(request);
            List<Map<String, Object>> list = systemInfoService.getAllSystemInfo2(systemInfo, pageNumber, pageSize,flag,notIds);
            List<Map<String, Object>> list2 = systemInfoService.getAllSystemInfo2(systemInfo, 1,
                    Integer.MAX_VALUE,flag,notIds);

            map.put("rows", list);
            map.put("total", list2.size());

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "fail");
        }
        return map;
    }

    /**
     *  查询一个系统信息，系统配置-配置
     * @param id id
     * @param createType 创建方式
     * @return Map<String, Object>
     */
    @RequestMapping(value = "getOneSystemInfo", method = RequestMethod.POST)
    public Map<String, Object> getOneSytemInfo(Long id, String createType) {
        Map<String, Object> map = new HashMap<>();
        if (id != null) {
            TblSystemInfo systemInfo = systemInfoService.getOneSystemInfo(id);
            systemInfo.setCodeReviewUserName(systemInfoService.getBatchUserName(systemInfo.getCodeReviewUserIds()));
            List<TblSystemScm> list = systemInfoService.getBySystemId(id);
            for (TblSystemScm tblSystemScm : list) {
                tblSystemScm.setSubmitSuperUserName(systemInfoService.getBatchUserName(tblSystemScm.getSubmitSuperUserNames()));
                tblSystemScm.setSubmitUserName(systemInfoService.getBatchUserName(tblSystemScm.getSubmitUserNames()));
            }
            Map<String, Object> versionMap = systemVersionService.getSystemVersionByCon(id, 1);
            List<TblSystemVersion> systemVersionList = (List<TblSystemVersion>) versionMap.get("rows");
            List<TblCommissioningWindow> commissioningWindowList = systemInfoService.getAllTblCommissioningWindow();
//			String termCode = "TBL_SYSTEM_SCM_ENVIRONMENT_TYPE";
//			List<TblDataDic> dics = getDataFromRedis(termCode);
            List<TblDataDic> dics = systemInfoService.selectDictEnvBySystemId2(id);
            map.put("dics", dics);
            map.put("systemInfo", systemInfo);
            map.put("list", list);
            map.put("systemVersionList", systemVersionList);
            map.put("commissioningWindowList", commissioningWindowList);
            List<TblSystemJenkins> jenkinsList = new ArrayList<>();
            List<TblToolInfo> toolInfos = new ArrayList<>();
            // if(createType.equals("2")) {
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("SYSTEM_ID", id);
            columnMap.put("STATUS", 1);
            columnMap.put("create_type", 2);
            jenkinsList = systemInfoService.getsystemJenkinsManual(columnMap);
            for (TblSystemJenkins tt : jenkinsList) {
                tt.setRootPom(tt.getId() + "");
            }

            Map<String, Object> colMap = new HashMap<>();
            map.put("STATUS", 1);
            map.put("TOOL_TYPE", 4);
            toolInfos = systemInfoService.getToolinfoType(colMap);
            map.put("jenkinslist", jenkinsList);
            map.put("toolList", toolInfos);

            // }

        }

        return map;
    }

    /**
     *  系统配置-配置，选择创建方式为手动后，查询出已经配置的构建/部署信息配置
     * @param id 系统id
     * @return Map<String, Object>
     */
    @RequestMapping(value = "getOneSystemInfoManual", method = RequestMethod.POST)
    public Map<String, Object> getOneSystemInfoManual(Long id) {
        Map<String, Object> map = new HashMap<>();
        List<String> toolList = new ArrayList<>();
        if (id != null) {
            TblSystemInfo systemInfo = systemInfoService.getOneSystemInfo(id);
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("SYSTEM_ID", id);
            columnMap.put("STATUS", 1);
            //手动
            columnMap.put("create_type", 2);
            // columnMap.put("SYSTEM_SCM_ID", null);
            // columnMap.put("CREATE_TYPE", 2);//2为自定义
            // columnMap.put("jobType", id);
            // 获取该id下jobname信息
            List<TblSystemJenkins> jenkinsList = systemInfoService.getsystemJenkinsManual(columnMap);
            for (TblSystemJenkins tt : jenkinsList) {
                tt.setRootPom(tt.getId() + "");
            }

            Map<String, Object> colMap = new HashMap<>();
            map.put("STATUS", 1);
            //1:GIT，2:SVN，3:SONAR，4:JENKINS，5:ARTIFACTORY,6:NEXUS
            map.put("TOOL_TYPE", 4);
            List<TblToolInfo> toolInfos = systemInfoService.getToolinfoType(colMap);

            map.put("systemInfo", systemInfo);
            map.put("jenkinslist", jenkinsList);
            map.put("toolList", toolInfos);
        }
        return map;
    }


    private List<String> deletJobcron(String systemScm, long systemid) {
        // 新增逻辑 ，获取修改前scm数据
        List<TblSystemScm> beforScmlist = systemInfoService.getBySystemId(systemid);
        List<String> beforFlag = new ArrayList<>();
        for (TblSystemScm scm : beforScmlist) {
            beforFlag.add(scm.getId().toString());
        }

        List<String> flag = new ArrayList<>();
        if (systemScm != null && !"".equals(systemScm)) {
            JSONArray jsonArray = JSONArray.fromObject(systemScm);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                if (object.containsKey("id") & !object.getString("id").equals("")) {
                    flag.add(object.getString("id"));
                }

            }
        }
        // 拿到删除的scmId
        beforFlag.removeAll(flag);
        return beforFlag;

    }

    private List<String> deletModulJobcron(String systemModuleScms, long systemModuleId) {
        // ，获取修改前mmoudlescm数据
        Map<String, Object> map = new HashMap<>();
        map.put("status", 1);
        map.put("SYSTEM_MODULE_ID", systemModuleId);

        List<TblSystemModuleScm> beforModuleScmlist = systemInfoService.getSystemModuleByid(map);
        List<String> beforFlag = new ArrayList<>();
        for (TblSystemModuleScm scm : beforModuleScmlist) {
            beforFlag.add(scm.getId().toString());
        }

        List<String> flag = new ArrayList<>();
        if (systemModuleScms != null && !"".equals(systemModuleScms)) {
            JSONArray jsonArray = JSONArray.fromObject(systemModuleScms);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                if (object.containsKey("id")) {
                    flag.add(object.getString("id"));

                }

            }
        }
        // 拿到删除的scmId
        beforFlag.removeAll(flag);
        return beforFlag;

    }

    /**
     *  更新系统信息
     * @param request
     * @param systemInfo 系统
     * @param systemJenkins jenkins配置表
     * @param createTypes 系统创建类型
     * @return Map<String, Object
     */

    @RequestMapping(value = "updateSystemInfo", method = RequestMethod.POST)
    public Map<String, Object> updateSystemInfo(HttpServletRequest request, TblSystemInfo systemInfo,
                                                String systemJenkins, String createTypes) {
        Map<String, Object> map = new HashMap<>();

            TblSystemInfo oldTblsysteminfo=systemInfoService.getOneSystemInfo(systemInfo.getId());
            if(!oldTblsysteminfo.getSystemName().equals(systemInfo.getSystemName())){
                List<TblSystemInfo> systemInfo1 = systemInfoService.
                        getSyetemByNameOrCode(systemInfo.getSystemName(), null);
                if (systemInfo1.size() > 0) {
                    map.put("status", 2);
                    map.put("errorMessage", "系统名已存在");
                    return map;
                }
            }

            if(!oldTblsysteminfo.getSystemCode().equals(systemInfo.getSystemCode())){
                List<TblSystemInfo> systemInfo2 = systemInfoService.
                        getSyetemByNameOrCode(null, systemInfo.getSystemCode());
                if (systemInfo2.size() > 0) {
                    map.put("status", 2);
                    map.put("errorMessage", "系统编号已存在");
                    return map;
                }
            }




//        List<TblSystemInfo> systemInfo1 = systemInfoService.
//                getSyetemByNameOrCode(systemInfo.getSystemName(), null);
//        List<TblSystemInfo> systemInfo2 = systemInfoService.
//                getSyetemByNameOrCode(null, systemInfo.getSystemCode());
//        if (systemInfo1.size() > 1 && systemInfo2.size() > 1) {
//            map.put("status", 2);
//            map.put("errorMessage", "系统名与系统编号都已经存在");
//            return map;
//        } else if (systemInfo1.size() > 1) {
//            map.put("status", 2);
//            map.put("errorMessage", "系统名已存在");
//            return map;
//        } else if (systemInfo2.size() > 1) {
//            map.put("status", 2);
//            map.put("errorMessage", "系统编号已存在");
//            return map;
//        }
        if (createTypes.equals(Constants.CREATE_TYPE_MANUAL)) {
            // 判断是否已存在相同jobname
            String value = systemInfoService.judgeJobNames(systemJenkins, systemInfo.getId());
            if (!value.equals("")) {
                map.put("status", "rejobName");
                map.put("message", "已存在相同名称:" + value);
                return map;
            }
        }
        String command = systemInfo.getCompileCommand();
        if (StringUtil.isNotEmpty(command)) {
        	command = command.replaceAll("rm {1,}-rf {1,}\\*|rm {1,}-r {1,}\\*|cd {1,}/", "##ERROR##");
        	if (command.indexOf("##ERROR##") != -1) {
        		 map.put("status", 2);
                 map.put("errorMessage", "编译打包命令存在问题");
                 return map;
        	}
        }
        
        try {
        	//更新系统信息
            TblSystemInfo tblSystemInfo = systemInfoService.getOneSystemInfo(systemInfo.getId());

            systemInfoService.updateSystemInfo(request, systemInfo, null);
            if (createTypes.equals(Constants.CREATE_TYPE_MANUAL)) {
                systemInfoService.updateSystemInfoManual(request, systemInfo, systemJenkins);
            }
            SystemTreeVo systemTreeVo = new SystemTreeVo();
            systemTreeVo.setSystemTreeCode(systemInfo.getSystemCode());
            systemTreeVo.setSystemTreeName(systemInfo.getSystemName());
            toProjectManageInterface.insertFirstSystem(systemTreeVo, CommonUtil.getCurrentUserId(request));

            if (!tblSystemInfo.getSystemCode().equals(systemInfo.getSystemCode()) ||
                    !tblSystemInfo.getSystemName().equals(systemInfo.getSystemName())) {
                List<Map<String, Object>> ListMap = systemInfoService.getSystemModuleBySystemId(systemInfo.getId());

                String result = putSystem(systemInfo, ListMap);
                //系统编码改变后通知自动化测试
                sendDataBus(systemInfo, result);
            }

            TblSystemInfo tblSystemInfo1 = systemInfoService.getOneSystemInfo(systemInfo.getId());
            systemInfoService.updateTmpSystemInfo(tblSystemInfo1, 2);
            map.put("systemInfo", tblSystemInfo1);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }

        return map;
    }

    /**
     * 同步DataBus
     * @param systemInfo
     * @param result
     */
	private void sendDataBus(TblSystemInfo systemInfo, String result) {
		for (int i = 0; i < 3; i++) {
			try {
				DataBusUtil.send(databusccName, systemInfo.getSystemCode(), result);
				break;
			} catch (Exception e) {
				logger.error("sendDataBus try time:" + (i + 1) + " " + e.getMessage(), e);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	* @Title: updateSystemInfoManual
	* @Description: 手动创建方式更新系统：构建/部署信息配置
	* @author author
	* @param request
	* @param systemInfo 需要更新的系统信息
	* @param systemJenkins 构建/部署信息配置:jenkins中的job相关信息
	* @return Map<String,Object>
	 */
    @RequestMapping(value = "updateSystemInfoManual", method = RequestMethod.POST)
    public Map<String, Object> updateSystemInfoManual(HttpServletRequest request, TblSystemInfo systemInfo,
                                                      String systemJenkins) {
        Map<String, Object> map = new HashMap<>();
//		if(systemInfo.getCreateType()==2) {//自定义
        // 插入记录

        try {
            systemInfoService.updateSystemInfoManual(request, systemInfo, systemJenkins);
            map.put("status", "success");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            super.handleException(e, e.getMessage());
            map.put("status", "fail");
        }

        return map;
    }

    /**
     * 
    * @Title: deleteSystemJenkinsManual
    * @Description: 删除任务手动构建方式：构建/部署信息配置
    * @author author
    * @param id 具体的某一条Jenkins配置
    * @param request
    * @return Map<String,Object>
     */
    @RequestMapping(value = "deleteSystemJenkinsManual", method = RequestMethod.POST)
    public Map<String, Object> deleteSystemJenkinsManual(String id, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        try {
            int flag = systemInfoService.deleteSystemJenkinsManual(id);
//			//判断是否有定时任务在跑
//           暂且注释手动构建部署定时任务
//			if (flag == 1) {
//
//				structureController.setCornOne(id, request, "2", "");// 默认删除
//			} else {
//				deployController.setCornOne(id, request, "2", "");
//			}

            map.put("status", "success");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            super.handleException(e, e.getMessage());
            map.put("status", "fail");
        }

        return map;
    }

    /**
     * 删除子模块
     * @param id 子模块id
     * @return
     */
    @RequestMapping(value = "getOneSystemModule", method = RequestMethod.POST)
    public Map<String, Object> getOneSystemModule(Long id) {
        Map<String, Object> module = new HashMap<>();
        if (id != null) {
            module = systemInfoService.getOneSystemModule(id);
            Long systemId = (Long) module.get("systemId");
            List<TblSystemScm> list = systemInfoService.getBySystemId(systemId);
            TblSystemInfo systemInfo = systemInfoService.getOneSystemInfo(systemId);
            List<Integer> types = new ArrayList<>();
            List<TblSystemScm> systemScms = new ArrayList<>();
            for (TblSystemScm tblSystemScm : list) {
                systemScms.add(tblSystemScm);
                Integer type = tblSystemScm.getEnvironmentType();
                types.add(type);
            }
            // 根据type和architecture_type去数据字典表查询 返回map
            List<TblDataDic> typess = systemInfoService.findEnvironmentType(types);
            // 查询当前服务模块所属的环境
            List<TblSystemModuleScm> typess2 = systemInfoService.finsModuleScm(id);
            module.put("types", typess);
            module.put("types2", typess2);
            module.put("systemScms", systemScms);
            module.put("systemInfo", systemInfo);
        }
        return module;
    }


    /**
     *  获取部署脚本模版
     * @param templateType 脚本模版类型
     * @return  Map<String, Object>
     */
    @RequestMapping(value = "getDeployScripTemplate", method = RequestMethod.POST)
    public Map<String, Object> getDeployScripTemplate(String templateType) {
        List<TblSystemDeployScriptTemplate> list = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        if (!templateType.isEmpty()) {
            list = systemInfoService.getDeployScripTemplateByType(templateType);
        }
        //部署配置时，脚本模板
        result.put("scriptTemplateList", list);
        return result;
    }


    /**
     * 根据systemId获取所有的配置环境和微服务
     *
     * @param systemId 系统id
     * @return
     */
    @RequestMapping(value = "getModuleAndScm", method = RequestMethod.POST)
    public Map<String, Object> getModuleAndScm(Long systemId) {
        Map<String, Object> module = new HashMap<>();
        if (systemId != null) {
            List<Map<String, Object>> moduleSystemList = systemInfoService.getSystemModuleBySystemId(systemId);
            List<Map<String, Object>> types = systemInfoService.selectDictEnvBySystemId(systemId);
            if (types.size() > 0) {
                TblSystemDeploy systemDeploy = new TblSystemDeploy();
                systemDeploy.setServerIds("");
                Map<String, Object> map = systemInfoService.getDeployInfoByCon(systemId,
                        Integer.valueOf(types.get(0).get("envType").toString()),
                        moduleSystemList == null || moduleSystemList.isEmpty() ? null
                                : (Long) moduleSystemList.get(0).get("id"));
                module.putAll(map);
            }
            //查询模板类型
            Map<String, Object> templateType = JsonUtil.fromJson((String) redisUtils.get("TEMPLATE_TYPE"), Map.class);

            // 查询当前服务模块所属的环境
            module.put("templateType", templateType);
            module.put("types", types);
            module.put("systemModule", moduleSystemList);
        }
        return module;
    }

    /**
     * 根据systemId获取所有的自动化测试信息
     *
     * @param systemId
     * @return
     */
    @RequestMapping(value = "getAutoTest", method = RequestMethod.POST)
    public Map<String, Object> getAutoTest(Long systemId) {
        Map<String, Object> module = new HashMap<>();
        if (systemId != null) {
            List<Map<String, Object>> systemModuleList = systemInfoService.getSystemModuleBySystemId(systemId);
            List<Map<String, Object>> types = systemInfoService.selectDictEnvBySystemId(systemId);
            List<TblSystemAutomaticTestConfig> autoTestList = systemInfoService.selectAutoTestConfigBySystemId(systemId, "1");
            List<TblSystemAutomaticTestConfig> uiTestList = systemInfoService.selectAutoTestConfigBySystemId(systemId, "2");
            Map<String, Object> testSceneMap = JsonUtil.fromJson((String) redisUtils.get("TEST_SCENE"), Map.class);
            Map<String, Object> uiTestSceneMap = JsonUtil.fromJson((String) redisUtils.get("UITEST_SCENE"), Map.class);
            List<TblDataDic> testSceneList = new ArrayList<TblDataDic>();
            List<TblDataDic> uiTestSceneList = new ArrayList<TblDataDic>();
            for (Entry<String, Object> entry : testSceneMap.entrySet()) {
                TblDataDic dic = new TblDataDic();
                dic.setValueCode(entry.getKey());
                dic.setValueName(entry.getValue().toString());
                testSceneList.add(dic);
            }
            for (Entry<String, Object> entry : uiTestSceneMap.entrySet()) {
                TblDataDic dic = new TblDataDic();
                dic.setValueCode(entry.getKey());
                dic.setValueName(entry.getValue().toString());
                uiTestSceneList.add(dic);
            }
            module.put("types", types);
            module.put("systemModule", systemModuleList);
//			List<TblSystemAutomaticTestConfig> apiTestList=new ArrayList<>();
//			List<TblSystemAutomaticTestConfig> uiTestList=new ArrayList<>();
//			for(TblSystemAutomaticTestConfig tblSystemAutomaticTestConfig:autoTestList){
//				if(tblSystemAutomaticTestConfig.getTestType().equals("1")){
//					apiTestList.add(tblSystemAutomaticTestConfig);
//				}else{
//					uiTestList.add(tblSystemAutomaticTestConfig);
//				}
//			}


            module.put("autoTest", autoTestList);
            module.put("uiAutoTest", uiTestList);
            module.put("testScene", testSceneList);
            module.put("uiTestScene", uiTestSceneList);
        }
        return module;
    }

    /**
     * 获取部署配置
     *
     * @param systemId
     * @param environmentType
     * @param systemModuleId
     * @return
     */
    @RequestMapping(value = "getOneDeploy", method = RequestMethod.POST)
    public Map<String, Object> getOneDeploy(Long systemId, Integer environmentType, Long systemModuleId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> map = systemInfoService.getDeployInfoByCon(systemId, environmentType,
                    systemModuleId);
            result.putAll(map);
            //查询模板类型
            Map<String, Object> templateType = JsonUtil.fromJson((String) redisUtils.get("TEMPLATE_TYPE"), Map.class);

            // 查询当前服务模块所属的环境
            result.put("templateType", templateType);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "getDeployList", method = RequestMethod.POST)
    public Map<String, Object> getDeployList(Long systemId, Integer architectureType) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TblSystemDeploy> list = systemInfoService.getDeployList(systemId, architectureType);
            result.put("data", list);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 修改部署配置
     *
     * @param deployStr
     * @param addArrInfoStr
     * @param request
     * @return
     */
    @RequestMapping(value = "updateDeploy", method = RequestMethod.POST)
    public Map<String, Object> updateDeploy(String deployStr, String addArrInfoStr, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {

            // 增加定时任务刷新
            TblSystemDeploy systemDeploy = JSON.parseObject(deployStr, TblSystemDeploy.class);

            systemInfoService.addOrUpdateDeploy(deployStr, addArrInfoStr, request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);

            List<String> deleteModuleScmid = new ArrayList<>();
            List<String> deleteScmid = new ArrayList<>();
            TblSystemInfo tblSystemInfo = iStructureService.geTblSystemInfo(systemDeploy.getSystemId());
            if (tblSystemInfo.getCreateType() == 1) {// 只有是自动时才更新
                refreshCronDeploy(tblSystemInfo.getCreateType() + "", request, systemDeploy.getSystemId(), deleteScmid,
                        deleteModuleScmid);
            }
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }






    /**
     * 获取部署配置
     *
     * @param deployStr
     * @param addArrInfoStr
     * @param request
     * @return
     */
    @RequestMapping(value = "getTopSystem", method = RequestMethod.POST)
    public Map<String, Object> getTopSystem(String id) {
        Map<String, Object> result = new HashMap<>();
        try {

        TblTopSystemInfo tblTopSystemInfo= tblTopSystemInfoMapper.selectById(Long.parseLong(id));
        result.put("tblTopSystemInfo",tblTopSystemInfo);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }




    /**
     * 修改部署配置
     *
     * @param deployStr
     * @param addArrInfoStr
     * @param request
     * @return
     */
    @RequestMapping(value = "updateTopSystem", method = RequestMethod.POST)
    public Map<String, Object> updateTopSystem(TblTopSystemInfo tblTopSystemInfo, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {



             TblTopSystemInfo tblTopSystemInfoOld =tblTopSystemInfoMapper.selectById(tblTopSystemInfo.getId());
             if(!tblTopSystemInfo.getSystemCode().equals(tblTopSystemInfoOld.getSystemCode())){
                 Map<String,Object> param=new HashMap<>();
                 param.put("system_Code",tblTopSystemInfo.getSystemCode());
                 List<TblTopSystemInfo> tops=tblTopSystemInfoMapper.selectByMap(param);
                 if(tops!=null && tops.size()>0){
                     result.put("status", Constants.ITMP_RETURN_FAILURE);
                     result.put("errorMessage","已存在系统编码");
                     return result;
                 }
             }

             Long uid = CommonUtil.getCurrentUserId(request);
             tblTopSystemInfo.setLastUpdateBy(uid);
             tblTopSystemInfo.setLastUpdateDate( new Timestamp(new Date().getTime()));
             tblTopSystemInfoMapper.updateByPrimaryKeySelective(tblTopSystemInfo);


        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }


    /**
     * 修改部署配置
     *
     * @param deployStr
     * @param addArrInfoStr
     * @param request
     * @return
     */
    @RequestMapping(value = "getTopSysteminfosByCode", method = RequestMethod.POST)
    public Map<String, Object> getTopSysteminfosByCode(String systemCode, HttpServletRequest request) {
    	Map<String, Object> map = new HashMap<>();
        List<TblTopSystemInfo> tops=new ArrayList<>();
        try {
            tops=  systemInfoService.getTopSysteminfosByCode( systemCode);
            map.put("rows", tops);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return map;
    }


    /**
     * 修改自动化测试
     *
     * @param systemId
     * @param autoTestJson
     * @param request
     * @return
     */
    @RequestMapping(value = "updateAutoTest", method = RequestMethod.POST)
    public Map<String, Object> updateAutoTest(Long systemId, String autoTestJson, String uiAutoTestJson, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TblSystemAutomaticTestConfig> autoTestList = JSON.parseArray(autoTestJson, TblSystemAutomaticTestConfig.class);
            List<TblSystemAutomaticTestConfig> uiAutoTestJsonList = JSON.parseArray(uiAutoTestJson, TblSystemAutomaticTestConfig.class);
            systemInfoService.addOrUpdateAutoTest(systemId, autoTestList, "1", request);//api自动测试
            systemInfoService.addOrUpdateAutoTest(systemId, uiAutoTestJsonList, "2", request);//ui自动测试


            result.put("status", Constants.ITMP_RETURN_SUCCESS);

        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     *  更新子模块源码信息
     * @param request
     * @param systemModule 子模块
     * @param systemModuleScms 子模块管理配置表
     * @return
     */
    @RequestMapping(value = "updateSystemModuleScm", method = RequestMethod.POST)
    public Map<String, Object> updateSystemModuleScm(HttpServletRequest request, TblSystemModule systemModule,
                                                     String systemModuleScms) {
        Map<String, Object> map = new HashMap<>();
        try {
            TblSystemModule systemModule1 = iStructureService.getTblsystemModule(systemModule.getId());
            if(!systemModule.getModuleCode().equals(systemModule1.getModuleCode())){


                Map<String,Object> param=new HashMap<>();
                param.put("module_Code",systemModule.getModuleCode());
                param.put("status",1);
                List<TblSystemModule> systemModules =systemInfoService.getModuleByMap(param);
                if(systemModules!=null && systemModules.size()>0){
                    map.put("errorMessage", "模块编号重复");
                    map.put("status", "fail");
                    return map;
                }
            }
            if(!systemModule.getModuleName().equals(systemModule1.getModuleName())){


                Map<String,Object> param=new HashMap<>();
                param.put("module_Name",systemModule.getModuleName());
                param.put("status",1);
                List<TblSystemModule> systemModules =systemInfoService.getModuleByMap(param);
                if(systemModules!=null && systemModules.size()>0){
                    map.put("errorMessage", "模块名称重复");
                    map.put("status", "fail");
                    return map;
                }
            }

            systemInfoService.updateSystemModuleScm(request, systemModule, systemModuleScms);
            map.put("status", "success");

            List<String> deleteScmid = new ArrayList<>();
            TblSystemInfo tblSystemInfo = iStructureService.geTblSystemInfo(systemModule.getSystemId());
            List<String> deleteModuleScmid = new ArrayList<>();

            if (tblSystemInfo.getCreateType() == 1) {
                this.refreshCron(tblSystemInfo.getCreateType() + "", request, systemModule.getSystemId(), deleteScmid,
                        deleteModuleScmid);
                this.refreshCronDeploy(tblSystemInfo.getCreateType() + "", request, systemModule.getSystemId(),
                        deleteScmid, deleteModuleScmid);
            }
            if (!systemModule1.getModuleCode().equals(systemModule.getModuleCode()) ||
                    !systemModule1.getModuleName().equals(systemModule.getModuleName())) {
                List<Map<String, Object>> ListMap = systemInfoService.getSystemModuleBySystemId(tblSystemInfo.getId());

                String result = putSystem(tblSystemInfo, ListMap);
                sendDataBus(tblSystemInfo, result);
            }
            TblSystemModule systemModule2 = iStructureService.getTblsystemModule(systemModule.getId());
            map.put("systemModule", systemModule2);
        } catch (Exception e) {
            super.handleException(e, e.getMessage());
            map.put("status", "fail");
        }
        return map;
    }

    /**
     *  到增加系统子模块页面
     * @param id 系统id
     * @return
     */
    @RequestMapping(value = "toAddSystemModule", method = RequestMethod.POST)
    public Map<String, Object> toAddSystemModule(Long id) {
        Map<String, Object> map = new HashMap<>();
        // 根据系统id查询出系统信息及系统配置的信息
        List<TblSystemScm> list = systemInfoService.getBySystemId(id);
        List<Integer> types = new ArrayList<>();
        List<TblSystemScm> systemScms = new ArrayList<>();
        for (TblSystemScm tblSystemScm : list) {
            systemScms.add(tblSystemScm);
            Integer type = tblSystemScm.getEnvironmentType();
            types.add(type);
        }
        // 根据type和architecture_type去数据字典表查询 返回map
        List<TblDataDic> typess = systemInfoService.findEnvironmentType(types);
        TblSystemInfo systemInfo = systemInfoService.findById(id);
        map.put("systemInfo", systemInfo);
        map.put("type", typess);
        map.put("systemScams", systemScms);

        return map;
    }
    /**
     *  增加系统子模块
     * @param systemModule 子模块信息
     * @return  Map<String, Object>
     */
    @RequestMapping(value = "addSystemModule", method = RequestMethod.POST)
    public Map<String, Object> addSystemModule(HttpServletRequest request, TblSystemModule systemModule,
                                               String moduleArr) {
        Map<String, Object> map = new HashMap<>();
        try {



            if(systemModule.getModuleCode()!=null){


                Map<String,Object> param=new HashMap<>();
                param.put("module_Code",systemModule.getModuleCode());
                param.put("status",1);
                List<TblSystemModule> systemModules =systemInfoService.getModuleByMap(param);
                if(systemModules!=null && systemModules.size()>0){
                    map.put("errorMessage", "模块编号重复");
                    map.put("status", "fail");
                    return map;
                }
            }
            if(systemModule.getModuleName()!=null){


                Map<String,Object> param=new HashMap<>();
                param.put("module_Name",systemModule.getModuleName());
                param.put("status",1);
                List<TblSystemModule> systemModules =systemInfoService.getModuleByMap(param);
                if(systemModules!=null && systemModules.size()>0){
                    map.put("errorMessage", "模块名称重复");
                    map.put("status", "fail");
                    return map;
                }
            }








            Long moduleId = systemInfoService.insertModule(request, systemModule, moduleArr);
            map.put("status", "success");

            TblSystemInfo tblSystemInfo = iStructureService.geTblSystemInfo(systemModule.getSystemId());
            if (tblSystemInfo.getCreateType() == 1) {
                List<String> deleteModuleScmid = new ArrayList<>();
                List<String> deleteScmid = new ArrayList<>();
                this.refreshCron(tblSystemInfo.getCreateType() + "", request, systemModule.getSystemId(), deleteScmid,
                        deleteModuleScmid);
                this.refreshCronDeploy(tblSystemInfo.getCreateType() + "", request, systemModule.getSystemId(),
                        deleteScmid, deleteModuleScmid);
            }

            List<Map<String, Object>> ListMap = systemInfoService.getSystemModuleBySystemId(tblSystemInfo.getId());

            String result = putSystem(tblSystemInfo, ListMap);
            sendDataBus(tblSystemInfo, result);

            TblSystemModule systemModule1 = iStructureService.getTblsystemModule(moduleId);
            map.put("systemModule", systemModule1);
        } catch (Exception e) {
            // TODO: handle exception
            super.handleException(e, e.getMessage());
            map.put("status", "fail");
        }

        return map;
    }

    /**
     *  删除
     * @param request
     * @param id 主键
     * @return
     */
    @RequestMapping(value = "deleteSystem", method = RequestMethod.POST)
    public Map<String, Object> deleteSystem(HttpServletRequest request, Long id) {
        Map<String, Object> map = new HashMap<>();
        try {
            TblSystemInfo tblSystemInfo = iStructureService.geTblSystemInfo(id);
            List<TblSystemModule> moduleList = null;
            if (tblSystemInfo.getArchitectureType() != null && tblSystemInfo.getArchitectureType() == 1) { //1=微服务架构；2=传统架构
                moduleList = iStructureService.selectSystemModule(id);
            }
            systemInfoService.deleteSystem(request, tblSystemInfo, moduleList);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);

            //定时任务
            if (tblSystemInfo.getCreateType() != null && tblSystemInfo.getCreateType() == 1) {
                List<String> deleteModuleScmid = new ArrayList<>();
                List<String> deleteScmid = new ArrayList<>();
                this.refreshCron(tblSystemInfo.getCreateType() + "", request, id, deleteScmid, deleteModuleScmid);
                this.refreshCronDeploy(tblSystemInfo.getCreateType() + "", request, id, deleteScmid, deleteModuleScmid);
            }

        } catch (Exception e) {
            super.handleException(e, e.getMessage());
            map.put("status", Constants.ITMP_RETURN_FAILURE);
        }
        return map;
    }

    /**
     *  删除子模块
     * @param request
     * @param id 子模块id
     * @param systemId 系统id
     * @return
     */
    @RequestMapping(value = "deleteSystemModule", method = RequestMethod.POST)
    public Map<String, Object> deleteSystemModule(HttpServletRequest request, Long id, Long systemId) {
        Map<String, Object> map = new HashMap<>();
        try {
            TblSystemModule systemModule = iStructureService.getTblsystemModule(id);
            systemInfoService.deleteModule(request, systemModule);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);

            //定时任务
            TblSystemInfo tblSystemInfo = iStructureService.geTblSystemInfo(systemId);
            if (tblSystemInfo.getCreateType() != null && tblSystemInfo.getCreateType() == 1) {
                List<String> deleteModuleScmid = new ArrayList<>();
                List<String> deleteScmid = new ArrayList<>();
                this.refreshCron(tblSystemInfo.getCreateType() + "", request, systemId, deleteScmid, deleteModuleScmid);
                this.refreshCronDeploy(tblSystemInfo.getCreateType() + "", request, systemId, deleteScmid, deleteModuleScmid);
            }
            
            List<Map<String, Object>> ListMap = systemInfoService.getSystemModuleBySystemId(tblSystemInfo.getId());
            String result = putSystem(tblSystemInfo, ListMap);
            sendDataBus(tblSystemInfo, result);


        } catch (Exception e) {
            super.handleException(e, e.getMessage());
            map.put("status", Constants.ITMP_RETURN_FAILURE);
        }
        return map;
    }

    // 无条件查询所有系统信息
    @RequestMapping(value = "getAll", method = RequestMethod.POST)
    public List<TblSystemInfo> getAll() {
        List<TblSystemInfo> systemInfos = systemInfoService.findAll();
        return systemInfos;
    }

    @RequestMapping(value = "getToolByType", method = RequestMethod.POST)
    public Map<String, Object> getToolByType() {
        Map<String, Object> map = new HashMap<>();
        List<TblToolInfo> list = systemInfoService.getToolByType();
        map.put("toolList", list);
        return map;
    }

    //查询工具
    @RequestMapping(value = "getToolinfoType", method = RequestMethod.POST)
    public Map<String, Object> getToolinfotype(String type, Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("STATUS", 1);
        map.put("TOOL_TYPE", type);
        List<TblToolInfo> list = systemInfoService.getToolinfoType(map);
        List<TblDataDic> dics = systemInfoService.selectDictEnvBySystemId2(id);
        map.put("toolList", list);
        map.put("dics", dics);
        return map;
    }

    @RequestMapping("getSearchData")
    public Map<String, Object> getSearchData() {
        Map<String, Object> map = new HashMap<>();
        List<TblDataDic> dics = getDataFromRedis("TBL_SYSTEM_INFO_ARCHITECTURE_TYPE");
        map.put("dics", dics);
        return map;
    }

    private List<TblDataDic> getDataFromRedis(String termCode) {
        String result = redisUtils.get(termCode).toString();
        List<TblDataDic> dics = new ArrayList<>();
        if (!StringUtils.isBlank(result)) {
            Map<String, Object> maps = (Map<String, Object>) JSON.parse(result);
            for (Entry<String, Object> entry : maps.entrySet()) {
                TblDataDic dic = new TblDataDic();
                dic.setValueCode(entry.getKey());
                dic.setValueName(entry.getValue().toString());
                dics.add(dic);
            }
        }
        return dics;
    }

    @RequestMapping(value = "getInfoByCreateType", method = RequestMethod.POST)
    public Map<String, Object> getInfoByCreateType(Long id, String createType) {
        Map<String, Object> map = new HashMap<>();
        if (createType.equals(Constants.CREATE_TYPE_AUTO)) {
            if (id != null) {

                List<TblSystemScm> list = systemInfoService.getBySystemId(id);

                map.put("list", list);
            }
        } else {
            // 手动
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("SYSTEM_ID", id);
            columnMap.put("CREATE_TYPE", 2);// 2为自定义
            // 获取该id下jobname信息
            List<TblSystemJenkins> jenkinsList = systemInfoService.getsystemJenkinsManual(columnMap);

            map.put("list", jenkinsList);

        }
        return map;
    }

    @RequestMapping(value = "judgeJobName")
    public Map<String, Object> judgeJobName(String jobName) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("job_name", jobName);
        paramMap.put("job_type", 1);
        paramMap.put("status", 1);
        int size = systemInfoService.getsystemJenkinsManual(paramMap).size();
        if (size > 0) {
            map.put("status", Constants.ITMP_RETURN_FAILURE);
            map.put("message", "已存在相同名称");
            return map;
        } else {
            map.put("status", Constants.ITMP_RETURN_SUCCESS);

            return map;
        }

    }

    private void refreshCron(String createTypes, HttpServletRequest request, long systemid, List<String> deleteScmid,
                             List<String> deleteModuleScmid) {
        Map<String, Object> systemJenkinsParam = new HashMap<>();
        systemJenkinsParam.put("system_id", systemid);
        systemJenkinsParam.put("status", 1);
        if (createTypes.equals(Constants.CREATE_TYPE_AUTO)) {
            systemJenkinsParam.put("JOB_TYPE", 1);
            systemJenkinsParam.put("CREATE_TYPE", 1);
        } else {
            systemJenkinsParam.put("JOB_TYPE", 1);
            systemJenkinsParam.put("CREATE_TYPE", 2);
        }
        // 将手动构建部署的暂且注释
        if (createTypes.equals(Constants.CREATE_TYPE_AUTO)) {
            List<TblSystemJenkins> jenkinsList = iStructureService.selectJenkinsByMap(systemJenkinsParam);
            List<String> detejenkinsIds = new ArrayList<>();
            for (TblSystemJenkins ts : jenkinsList) {

                if (ts.getJobCron() != null) {
                    if (deleteScmid.contains(ts.getSystemScmId().toString())) {

                        ts.setJobCron("");
                        detejenkinsIds.add(ts.getId() + "");
                        // 将此scmid暂时恢复状态1
                        TblSystemScm tblSystemScm = new TblSystemScm();
                        tblSystemScm.setStatus(1);
                        tblSystemScm.setId(ts.getSystemScmId());
                        iStructureService.updateSystemScm(tblSystemScm);
                        Map<String, Object> map = new HashMap<>();
                        map.put("systemId", systemid);
                        map.put("status", 1);
                        map.put("systemScmId", ts.getSystemScmId());
                        iStructureService.updateModuleStatusBySystemId(map);
                    }

                    structureController.setCornOne(ts.getId() + "", request, createTypes, ts.getJobCron());
                    // 将tblsystemjenkins中删除的scmid状态逻辑删除状态
                    for (String id : detejenkinsIds) {
                        TblSystemJenkins tblSystemJenkins = iStructureService.selectSystemJenkinsById(id);
                        tblSystemJenkins.setStatus(2);
                        iStructureService.updateJenkins(tblSystemJenkins);

                    }
                    // 将中删除的scmid状态逻辑恢复删除状态
                    for (String id : deleteScmid) {
                        if (id.equals(ts.getSystemScmId().toString())) {
                            TblSystemScm tblSystemScm = new TblSystemScm();
                            tblSystemScm.setStatus(2);
                            tblSystemScm.setId(Long.parseLong(id));
                            iStructureService.updateSystemScm(tblSystemScm);

                            Map<String, Object> map = new HashMap<>();
                            map.put("systemId", systemid);
                            map.put("status", 2);
                            map.put("systemScmId", id);
                            iStructureService.updateModuleStatusBySystemId(map);
                        }

                    }

                }
            }
        }

    }

    private void refreshCronDeploy(String createTypes, HttpServletRequest request, long systemid,
                                   List<String> deleteScmid, List<String> deleteModuleScmid) {

        // 将状态为2的modulescm暂为1
        // 更新相关数据
        Map<String, Object> systemJenkinsParam = new HashMap<>();

        systemJenkinsParam.put("system_id", systemid);
        systemJenkinsParam.put("status", 1);
        if (createTypes.equals(Constants.CREATE_TYPE_AUTO)) {
            systemJenkinsParam.put("JOB_TYPE", 2);
            systemJenkinsParam.put("CREATE_TYPE", 1);


            //自动源码部署
        } else {
            systemJenkinsParam.put("JOB_TYPE", 2);
            systemJenkinsParam.put("CREATE_TYPE", 2);

        }
        // 暂且注释手动注释
        if (createTypes.equals(Constants.CREATE_TYPE_AUTO)) {
            List<TblSystemJenkins> jenkinsList = iStructureService.selectJenkinsByMap(systemJenkinsParam);

            List<String> detejenkinsIds = new ArrayList<>();
            for (TblSystemJenkins ts : jenkinsList) {
                if (ts.getEnvironmentType() != null && ts.getSystemScmId() == null) {
                    //这条记录是制品部署的 舍弃
                    continue;
                }

                if (ts.getJobCron() != null) {

                    if (deleteScmid.contains(ts.getSystemScmId() + "")) {
                        ts.setJobCron("");
                        detejenkinsIds.add(ts.getId() + "");
                        // 将此scmid暂时恢复状态1
                        TblSystemScm tblSystemScm = new TblSystemScm();
                        tblSystemScm.setStatus(1);
                        tblSystemScm.setId(ts.getSystemScmId());
                        iStructureService.updateSystemScm(tblSystemScm);
                        Map<String, Object> map = new HashMap<>();
                        map.put("systemId", systemid);
                        map.put("status", 1);
                        map.put("systemScmId", ts.getSystemScmId());
                        iStructureService.updateModuleStatusBySystemId(map);
                    }

                    deployController.setCornOne(ts.getId() + "", request, createTypes, ts.getJobCron());
                    // 将tblsystemjenkins中删除的scmid状态逻辑删除状态
                    for (String id : detejenkinsIds) {
                        TblSystemJenkins tblSystemJenkins = iStructureService.selectSystemJenkinsById(id);
                        tblSystemJenkins.setStatus(2);
                        iStructureService.updateJenkins(tblSystemJenkins);

                    }
                    // 刷新任务
                    // 将中删除的scmid状态逻辑恢复删除状态
                    for (String id : deleteScmid) {
                        if (id.equals(ts.getSystemScmId().toString())) {
                            TblSystemScm tblSystemScm = new TblSystemScm();
                            tblSystemScm.setStatus(2);
                            tblSystemScm.setId(Long.parseLong(id));
                            iStructureService.updateSystemScm(tblSystemScm);

                            Map<String, Object> map = new HashMap<>();
                            map.put("systemId", systemid);
                            map.put("status", 2);
                            map.put("systemScmId", Long.parseLong(id));
                            iStructureService.updateModuleStatusBySystemId(map);
                        }

                    }
                }
            }

        }
    }
    // }

    /*
     * ztt 根据传入的userId 查询该用户所在项目下的所有系统id
     *
     */
    @RequestMapping(value = "findSystemIdByUserId")
    public List<Long> findSystemIdByUserId(Long id) {
        List<Long> systemIds = systemInfoService.findSystemIdByUserId(id);
        return systemIds;
    }

    /**
     * 获取所有环境
     *
     * @return
     */
    @RequestMapping(value = "getAllEnvironment", method = RequestMethod.POST)
    public Map<String, Object> getAllEnvironment() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> list = systemInfoService.selectDictEnv();
            map.put("list", list);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    /**
     * 配置环境
     *
     * @param id
     * @param envType
     * @return
     */
    @RequestMapping(value = "configEnvironment", method = RequestMethod.POST)
    public Map<String, Object> configEnvironment(Long id, String envType, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            systemInfoService.configEnvironment(id, envType, request);
            TblSystemInfo systemInfo = systemInfoService.getOneSystemInfo(id);
            systemInfoService.updateTmpSystemInfo(systemInfo, 2);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "findEnvIds", method = RequestMethod.POST)
    public Map<String, Object> findEnvIds(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            List<Long> ids = systemInfoService.findEnvIds(id);
            map.put("data", ids);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "findSystemByProject", method = RequestMethod.POST)
    public Map<String, Object> findSystemByProject(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<TblSystemInfo> list = systemInfoService.findSystemByProject(request);
            map.put("rows", list);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }


    /**
     *  查询顶层系统信息
     * @param systemCode top系统编码
     * @param request
     * @return
     */
    @RequestMapping(value = "getTopSystemInfoByCode", method = RequestMethod.POST)
    public Map<String, Object> getTopSystemInfoByCode(String systemCode,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<TblTopSystemInfo> list = systemInfoService.getTopSystemInfoByCode(systemCode,request);
            map.put("rows", list);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }


    /**
     *  增加系统
     * @param systemInfo 子系统
     * @param request
     * @param systemJenkins jenkins
     * @param tblTopSystemInfo 主系统
     * @param createTypes 创建类型
     * @return Map<String, Object>
     */
    @RequestMapping(value = "addSystem", method = RequestMethod.POST)
    public Map<String, Object> addSystem(TblSystemInfo systemInfo, HttpServletRequest request,
                                         String systemJenkins, String tblTopSystemInfo,String createTypes) {
        Map<String, Object> map = new HashMap<>();
        try {
            TblTopSystemInfo tblTopSystem= com.alibaba.fastjson.JSONObject.parseObject(tblTopSystemInfo,TblTopSystemInfo.class);

            List<TblSystemInfo> systemInfo1 = systemInfoService.
                    getSyetemByNameOrCode(systemInfo.getSystemName(), null);
            List<TblSystemInfo> systemInfo2 = systemInfoService.
                    getSyetemByNameOrCode(null, systemInfo.getSystemCode());
            if (systemInfo1.size() > 0 && systemInfo2.size() > 0) {
                map.put("status", 2);
                map.put("errorMessage", "系统名与系统编号都已经存在");
            } else if (systemInfo1.size() > 0) {
                map.put("status", 2);
                map.put("errorMessage", "系统名已存在");
            } else if (systemInfo2.size() > 0) {
                map.put("status", 2);
                map.put("errorMessage", "系统编号已存在");
            } else {
                if(tblTopSystem.getId()!=null && !tblTopSystem.getId().toString().equals("")){
                    systemInfo.setTopSystemId(tblTopSystem.getId());
                }else{
                    tblTopSystem=  systemInfoService.addTopSystem(tblTopSystem,request);
                    systemInfo.setTopSystemId(tblTopSystem.getId());
                }
                systemInfoService.insertItmpSystem(systemInfo, request);
                systemInfo = systemInfoService.getSystemByCode(systemInfo.getSystemCode());

                if (createTypes.equals(Constants.CREATE_TYPE_MANUAL)) {
                    systemInfoService.updateSystemInfoManual(request, systemInfo, systemJenkins);
                }

                SystemTreeVo systemTreeVo = new SystemTreeVo();
                systemTreeVo.setSystemTreeCode(systemInfo.getSystemCode());
                systemTreeVo.setSystemTreeName(systemInfo.getSystemName());
                toProjectManageInterface.insertFirstSystem(systemTreeVo, CommonUtil.getCurrentUserId(request));

                String result = putSystem(systemInfo, null);
                sendDataBus(systemInfo, result);

                map.put("systemInfo", systemInfo);
                map.put("status", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            map.put("status", Constants.ITMP_RETURN_FAILURE);
            map.put("errorMessage", "系统错误");
        }
        return map;
    }

    /**
     *   根据系统id获取系统源码配置表
     * @param syetemInfo
     * @return
     */
    @RequestMapping(value = "getSystemScmBySystemId", method = RequestMethod.POST)
    public Map<String, Object> getSystemScmBySystemId(TblSystemInfo syetemInfo) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = systemInfoService.getScmBySystemId(syetemInfo.getId(), syetemInfo.getArchitectureType());
            List<Map<String, Object>> systemModuleList =
                    systemInfoService.getSystemModuleBySystemId(syetemInfo.getId());
            List<TblDataDic> dics = systemInfoService.selectDictEnvBySystemId2(syetemInfo.getId());
            map.put("dics", dics);
            map.put("syetemInfo", syetemInfo);
            map.put("systemModuleList", systemModuleList);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "delScm", method = RequestMethod.POST)
    public Map<String, Object> delScm(Long id, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            systemInfoService.delScm(id, request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
            //定时任务
            if (id != null) {
                TblSystemScm tblsystemscm = iStructureService.getTblsystemScmById(id);
                List<String> deleteScmid = new ArrayList<>();
                deleteScmid.add(String.valueOf(id));
                List<String> deleteModuleScmid = new ArrayList<>();
                refreshCron("1", request, tblsystemscm.getSystemId(), deleteScmid, deleteModuleScmid);
                refreshCronDeploy("1", request, tblsystemscm.getSystemId(), deleteScmid, deleteModuleScmid);
            }

        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    /**
     *  删除子模块源码配置
     * @param modelScm
     * @param request
     * @return
     */
    @RequestMapping(value = "delModuleScm", method = RequestMethod.POST)
    public Map<String, Object> delModuleScm(TblSystemModuleScm modelScm, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            String delScmId = systemInfoService.delModuleScm(modelScm, request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
            //定时任务
            if (!delScmId.equals("")) {
                TblSystemScm tblsystemscm = iStructureService.getTblsystemScmById(Long.parseLong(delScmId));
                List<String> deleteScmid = new ArrayList<>();
                deleteScmid.add(delScmId);
                List<String> deleteModuleScmid = new ArrayList<>();
                refreshCron("1", request, tblsystemscm.getSystemId(), deleteScmid, deleteModuleScmid);
                refreshCronDeploy("1", request, tblsystemscm.getSystemId(), deleteScmid, deleteModuleScmid);
            }

        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    /**
     *  更新系统源码
     * @param syetemScm
     * @param request
     * @return
     */
    @RequestMapping(value = "updateSystemScm", method = RequestMethod.POST)
    public Map<String, Object> updateSystemScm(String syetemScm, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<TblSystemScm> systemScmList = JSON.parseArray(syetemScm, TblSystemScm.class);
            systemInfoService.updateScm(systemScmList, request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);

            //定时任务
            if (systemScmList.size() > 0) {
                List<String> deleteScmid = new ArrayList<>();
                List<String> deleteModuleScmid = new ArrayList<>();
                refreshCron("1", request, systemScmList.get(0).getSystemId(), deleteScmid, deleteModuleScmid);
                refreshCronDeploy("1", request, systemScmList.get(0).getSystemId(), deleteScmid, deleteModuleScmid);
            }
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "updateSystemModuleScm1", method = RequestMethod.POST)
    public Map<String, Object> updateSystemModuleScm1(String syetemScm, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<TblSystemModuleScm> moduleScmList = JSON.parseArray(syetemScm, TblSystemModuleScm.class);
            systemInfoService.updateModelScm(moduleScmList, request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
            //定时任务
            if (moduleScmList.size() > 0) {
                List<String> deleteScmid = new ArrayList<>();
                List<String> deleteModuleScmid = new ArrayList<>();
                refreshCron("1", request, moduleScmList.get(0).getSystemId(), deleteScmid, deleteModuleScmid);
                refreshCronDeploy("1", request, moduleScmList.get(0).getSystemId(), deleteScmid, deleteModuleScmid);
            }

        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    public String putSystem(TblSystemInfo systemInfo, List<Map<String, Object>> listMap) {
        Map<String, Object> mapAll = new LinkedHashMap<>();
        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("systemCode", systemInfo.getSystemCode());
        requestBody.put("systemName", systemInfo.getSystemName());
        if (listMap == null) {
            requestBody.put("subSystemInfo", new Map[0]);
        } else {
            Map[] subSystemInfo = new Map[listMap.size()];
            for (int i = 0; i < listMap.size(); i++) {
                Map<String, Object> subSystem = new HashMap<>();
                subSystem.put("subSystemCode", listMap.get(i).get("moduleCode").toString());
                subSystem.put("subSystemName", listMap.get(i).get("moduleName").toString());
                subSystemInfo[i] = subSystem;
            }
            requestBody.put("subSystemInfo", subSystemInfo);
        }
        mapAll.put("requestHead", DataBusRequestHead.getRequestHead());
        mapAll.put("requestBody", requestBody);
        String result = JSON.toJSONString(mapAll, SerializerFeature.WriteDateUseDateFormat);
        return result;
    }


    /**
     * 获取该系统下子模块信息
     *
     * @param systemId
     * @param systemId
     * @param request
     * @return
     */
    @RequestMapping(value = "getModulesBySystemId", method = RequestMethod.POST)
    public Map<String, Object> getModulesBySystemId(Long systemId, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TblSystemModule> modules = iStructureService.selectSystemModule(systemId);


            result.put("modules", modules);

        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     *   获取系统名称
     * @param systemId
     * @return
     */
    @RequestMapping(value = "getSystemNameById", method = RequestMethod.POST)
    public String getSystemNameById(Long systemId) {
        TblSystemInfo systemInfo = systemInfoService.findById(systemId);
        if (systemInfo != null) {
            return systemInfo.getSystemName();
        }
        return null;
    }


    /**
     *  增加主系统
     * @param tblTopSystemInfo 主系统信息
     * @param request
     * @return
     */
    @RequestMapping(value = "addTopSystem", method = RequestMethod.POST)
    public Map<String, Object> addTopSystem(TblTopSystemInfo tblTopSystemInfo, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
           map=systemInfoService.addTopSystem(tblTopSystemInfo,map,request);

        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }


}
