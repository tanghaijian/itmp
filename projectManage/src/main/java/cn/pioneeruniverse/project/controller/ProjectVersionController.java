package cn.pioneeruniverse.project.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.project.dao.mybatis.SystemInfoMapper;
import cn.pioneeruniverse.project.entity.TblSystemInfo;
import cn.pioneeruniverse.project.entity.TblSystemVersion;
import cn.pioneeruniverse.project.service.projectversion.IProjectVersionService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 
* @ClassName: ProjectVersionController
* @Description: 运维项目-版本管理
* @author author
* @date 2020年8月28日 下午6:45:48
*
 */
@RestController
@RequestMapping("projectVersion")
public class ProjectVersionController extends BaseController {

    @Autowired
    private IProjectVersionService projectVersionService;
    @Autowired
    private SystemInfoMapper tblSystemInfoMapper;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 根据项目查询所有系统
     * @param projectId 项目ID
     * @return map key:allSystemList  value:List<TblSystemInfo>返回的系统列表
     */
    @RequestMapping("getSystemByProjectId")
    public Map<String, Object> getSystemByProjectId(Long projectId) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = projectVersionService.selectSystemByProjectId(projectId);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    /**
     * 根据项目查询所有版本
     * @param projectId 项目ID
     * @return map key :rows value :List<TblSystemVersion>返回的系统版本列表
     */
    @RequestMapping("getProjectVersionByCon")
    public Map<String, Object> getProjectVersionByCon(Long projectId) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = projectVersionService.getProjectVersionByCon(projectId);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    /**
     * 新增版本
     *
     * @param systemVersion 版本信息
     * @param request
     * @return Map<String, Object> status=1正常，2异常
     */
    @RequestMapping("addProjectVersion")
    public Map<String, Object> addProjectVersion(TblSystemVersion systemVersion,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            projectVersionService.insertVersion(systemVersion,request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }
    /**
     * @param systemIds 系统id
     * @return Map<String, Object> Key：environmentTypes 
     *                            value: List<map>形如：[{"envName":"DEV-私有","env":"1"}]
     * @Description 获取当前用系统版本所选环境类型
     * @MethodName getEnvironmentTypes

     */
    @RequestMapping(value = "getEnvironmentTypes", method = RequestMethod.POST)
    public Map<String, Object> getEnvironmentTypes(String systemIds) {
        Map<String, Object> map = new HashMap<>();
        Object redisEnvType = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
        Map<String, Object> envMap = JSON.parseObject(redisEnvType.toString());
        List<Map<String,Object>> result=new ArrayList<>();
        String[] systemIdArray=systemIds.split(",");
        List<String> all=new ArrayList<>();
        try {
        	//从多选的项目中循环查出环境类型
            for(String systemId:systemIdArray){
            TblSystemInfo tblSystemInfo= tblSystemInfoMapper.selectById(Long.parseLong(systemId));
            String envTypes=tblSystemInfo.getEnvironmentType();
            List<String> envs=new ArrayList<>();
            if(tblSystemInfo.getEnvironmentType()!=null){
                envs= Arrays.asList(envTypes.split(","));
            }
            for(String env:envs){
                if(!all.contains(env)){
                    all.add(env);
                }
            }

            }

            for(String env:all){
                Map<String,Object> envm=new HashMap<>();
                envm.put("env",env);
                envm.put("envName",envMap.get(env).toString());
                result.add(envm);
            }


            map.put("environmentTypes",result);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }


    /**
     * 获取编辑的数据修改
     * @param systemVersion 系统版本
     * @return Map<String, Object> status=1正常，2异常
     */
    @RequestMapping("toUpdateProjectVersion")
    public Map<String, Object> toUpdateProjectVersion(TblSystemVersion systemVersion) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = projectVersionService.getProjectVersionBySystemVersion(systemVersion);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }
    /**
     * 编辑提交
     *
     * @param systemVersion 系统版本
     * @param request
     * @return Map<String, Object> status=1正常，2异常
     */
    @RequestMapping("updateProjectVersion")
    public Map<String, Object> updateProjectVersion(TblSystemVersion systemVersion, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            projectVersionService.updateVersion(systemVersion, request);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    /**
     * 关闭或者开启版本
     *
     * @param systemVersion 系统版本
     * @param request
     * @return Map<String, Object> status=1正常，2异常
     */
    @RequestMapping("closeOrOpenSystemVersion")
    public Map<String, Object> closeOrOpenSystemVersion(TblSystemVersion systemVersion, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
             projectVersionService.closeOrOpenSystemVersion(systemVersion, request);
             map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }
}
