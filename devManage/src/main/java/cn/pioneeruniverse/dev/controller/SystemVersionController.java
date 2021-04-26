package cn.pioneeruniverse.dev.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.entity.TblSystemVersion;
import cn.pioneeruniverse.dev.service.systemversion.ISystemVersionService;

@RestController
@RequestMapping("systemVersion")
public class SystemVersionController extends BaseController {

    @Autowired
    private ISystemVersionService systemVersionService;
    @Autowired
    private TblSystemInfoMapper tblSystemInfoMapper;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 根据条件查询所有版本
     *
     * @param systemId
     * @return Map<String, Object> status=1正常返回，2异常返回
     */
    @RequestMapping("getSystemVersionByCon")
    public Map<String, Object> getSystemVersionByCon(Long systemId,Integer status) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = systemVersionService.getSystemVersionByCon(systemId, status);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }
    /**
     * 根据条件查询所有版本
     *
     * @param systemId 系统id
     * @return Map<String, Object> status=1正常返回，2异常返回
     */
    @RequestMapping("getSystemVersionByConNew")
    public Map<String, Object> getSystemVersionByConNew(Long systemId,Integer status,String env) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = systemVersionService.getSystemVersionByConNew(systemId, status,env);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }
    /**
     * 根据条件所属环境查询所有版本(构建部署)
     *
     * @param systemId 系统id
     * @return Map<String, Object> status=1正常返回，2异常返回
     */
    @RequestMapping("getSystemVersionByBd")
    public Map<String, Object> getSystemVersionByBd(Long systemId,String envType,Integer status) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = systemVersionService.getSystemVersionByBd(systemId,envType, status);
            map.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    /**
     * 添加 系统版本
     *
     * @param systemVersion系统版本
     * @param request
     * @return Map<String, Object> status=1正常返回，2异常返回
     */
    @RequestMapping("addSystemVersion")
    public Map<String, Object> addSystemVersion(TblSystemVersion systemVersion,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            Integer succ = systemVersionService.insertVersion(systemVersion,request);
            if(succ==0){
                map.put("status", Constants.ITMP_RETURN_SUCCESS);
            }else{
                map.put("status", Constants.ITMP_RETURN_FAILURE);
                map.put("errorMessage", "已存在相同的版本标签");
            }
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    /**
     * @param systemId 系统id
     * @return Map<String, Object>
     * @Description 获取当前用系统版本所选环境类型
     * @MethodName getEnvironmentTypes

     */
    @RequestMapping(value = "getEnvironmentTypes", method = RequestMethod.POST)
    public Map<String, Object> getEnvironmentTypes(Long systemId) {
        Object redisEnvType = redisUtils.get("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
        Map<String, Object> envMap = JSON.parseObject(redisEnvType.toString());
        List<Map<String,Object>> result=new ArrayList<>();
       Map<String, Object> map = new HashMap<>();
       try {
       TblSystemInfo tblSystemInfo= tblSystemInfoMapper.selectById(systemId);
       String envTypes=tblSystemInfo.getEnvironmentType();
       List<String> envs=new ArrayList<>();
       if(tblSystemInfo.getEnvironmentType()!=null){
          envs= Arrays.asList(envTypes.split(","));
       }
       for(String env:envs){
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
     * 修改系统版本
     *
     * @param systemVersion
     * @param request
     * @return Map<String, Object> status=1正常返回，2异常返回
     */
    @RequestMapping("updateSystemVersion")
    public Map<String, Object> updateSystemVersion(TblSystemVersion systemVersion, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            Integer succ = systemVersionService.updateVersion(systemVersion, request);
            if(succ==0){
                map.put("status", Constants.ITMP_RETURN_SUCCESS);
            }else{
                map.put("status", Constants.ITMP_RETURN_FAILURE);
                map.put("errorMessage", "已存在相同的版本标签");
            }
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    /**
     * 关闭或者开启版本
     *
     * @param systemVersion
     * @param request
     * @return Map<String, Object> status=1正常返回，2异常返回
     */
    @RequestMapping("closeOrOpenSystemVersion")
    public Map<String, Object> closeOrOpenSystemVersion(TblSystemVersion systemVersion, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            Integer succ = systemVersionService.closeOrOpenSystemVersion(systemVersion, request);
            if(succ>0){
                map.put("status", Constants.ITMP_RETURN_SUCCESS);
            }else{
                map.put("status", Constants.ITMP_RETURN_FAILURE);
                map.put("errorMessage", "操作失败");
            }
        } catch (Exception e) {
            map = super.handleException(e, e.getMessage());
        }
        return map;
    }

    /**
     * 
    * @Title: getSystemVersionBySystemVersionId
    * @Description: 获取系统版本详情
    * @author author
    * @param systemVersionId 系统版本id
    * @return TblSystemVersion
     */
    @RequestMapping(value = "getSystemVersionBySystemVersionId", method = RequestMethod.POST)
    public TblSystemVersion getSystemVersionBySystemVersionId(Long systemVersionId) {
        return systemVersionService.getSystemVersionBySystemVersionId(systemVersionId);
    }
}
