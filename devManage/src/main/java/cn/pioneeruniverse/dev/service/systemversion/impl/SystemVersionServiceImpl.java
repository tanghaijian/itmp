package cn.pioneeruniverse.dev.service.systemversion.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemVersionMapper;
import cn.pioneeruniverse.dev.entity.TblSystemVersion;
import cn.pioneeruniverse.dev.service.systemversion.ISystemVersionService;

@Service
@Transactional(readOnly = true)
public class SystemVersionServiceImpl implements ISystemVersionService {

    @Autowired
    private TblSystemVersionMapper systemVersionMapper;
    @Autowired
    private TblSystemInfoMapper systemInfoMapper;


    /**
    * @author author
    * @Description 添加版本
    * @Date 2020/9/14
    * @param systemVersion
    * @param request
    * @return java.lang.Integer
    **/
    @Override
    @Transactional(readOnly = false)
    public Integer insertVersion(TblSystemVersion systemVersion,HttpServletRequest request) {
        Integer succ = 0;
        List<TblSystemVersion> systemList = systemVersionMapper.getSystemVersionByVersion(systemVersion);
        if(systemList==null||systemList.size()<=0){
            CommonUtil.setBaseValue(systemVersion, request);
            systemVersionMapper.insertVersion(systemVersion);
        }else{
            succ = 1;
        }
        return succ;
    }

    @Override
    @Transactional(readOnly = true)
    public TblSystemVersion getSystemVersionBySystemVersionId(Long systemVersionId) {
        return systemVersionMapper.selectByPrimaryKey(systemVersionId);
    }

    /**
    * @author author
    * @Description 修改版本
    * @Date 2020/9/14
    * @param systemVersion
    * @param request
    * @return java.lang.Integer
    **/
    @Override
    @Transactional(readOnly = false)
    public Integer updateVersion(TblSystemVersion systemVersion, HttpServletRequest request) {
        Integer succ = 0;
        TblSystemVersion systemVersion1 = systemVersionMapper.selectByPrimaryKey(systemVersion.getId());
        if(systemVersion.getGroupFlag().equals(systemVersion1.getGroupFlag())&&
                systemVersion.getVersion().equals(systemVersion1.getVersion()) ){
            if(!systemVersion.getEnvironmentTypes().equals(systemVersion1.getEnvironmentTypes())){
                systemVersion1.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                systemVersion1.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
                systemVersion1.setEnvironmentTypes(systemVersion.getEnvironmentTypes());
                systemVersionMapper.updateByPrimaryKeySelective(systemVersion1);
            }


        }else {
            systemVersion.setSystemId(systemVersion1.getSystemId());
            List<TblSystemVersion> systemList = systemVersionMapper.getSystemVersionByVersion(systemVersion);
            if(systemList==null||systemList.size()<=0){
                systemVersion.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                systemVersion.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
                systemVersionMapper.updateByPrimaryKeySelective(systemVersion);
            }else{
                succ = 1;
            }
        }
        return succ;
    }
    /**
    * @author author
    * @Description 关闭或者开启版本
    * @Date 2020/9/14
    * @param systemVersion
    * @param request
    * @return java.lang.Integer
    **/
    @Override
    @Transactional(readOnly = false)
    public Integer closeOrOpenSystemVersion (TblSystemVersion systemVersion, HttpServletRequest request){
        systemVersion.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
        systemVersion.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        return systemVersionMapper.updateByPrimaryKeySelective(systemVersion);
    }
    /**
    * @author author
    * @Description 根据条件查询所有版本
    * @Date 2020/9/14
    * @param systemId
    * @param status
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    public Map<String, Object> getSystemVersionByCon(Long systemId, Integer status) {
        Map<String, Object> map = new HashMap<>();
        TblSystemVersion systemVersion = new TblSystemVersion();
        systemVersion.setSystemId(systemId);
        systemVersion.setStatus(status);
        List<TblSystemVersion> systemVersions = systemVersionMapper.getSystemVersionByCon(systemVersion);
        map.put("rows", systemVersions);
        map.put("system", systemInfoMapper.getOneSystemInfo(systemId));
        return map;
    }
    /**
    * @author author
    * @Description 根据条件查询所有版本
    * @Date 2020/9/14
    * @param systemId
    * @param status
    * @param env
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    public Map<String, Object> getSystemVersionByConNew(Long systemId, Integer status,String env) {
        Map<String, Object> map = new HashMap<>();
        TblSystemVersion systemVersion = new TblSystemVersion();
        systemVersion.setSystemId(systemId);
        systemVersion.setStatus(status);
        List<TblSystemVersion> systemVersions = systemVersionMapper.getSystemVersionByCon(systemVersion);
        List<TblSystemVersion> newSystemVersions=new ArrayList<>();
        for(TblSystemVersion tblSystemVersion:systemVersions){
            if(tblSystemVersion.getEnvironmentTypes()!=null){
                for(String envNew:tblSystemVersion.getEnvironmentTypes().split(",")){
                    if(envNew.equals(env)){
                        newSystemVersions.add(tblSystemVersion);
                        break;
                    }
                }
            }
        }
        map.put("rows", newSystemVersions);
        map.put("system", systemInfoMapper.getOneSystemInfo(systemId));
        return map;
    }

    /**
     * 根据条件查询所有版本
     */
    @Override
    public Map<String, Object> getSystemVersionByBd(Long systemId,String envType, Integer status) {
        Map<String, Object> map = new HashMap<>();
//        TblSystemVersion systemVersion = new TblSystemVersion();
//        systemVersion.setSystemId(systemId);
//        systemVersion.setStatus(status);
        Map<String, Object> param = new HashMap<>();
        param.put("systemId",systemId);
        param.put("status",status);
        param.put("envType",envType);
        List<TblSystemVersion> systemVersions = systemVersionMapper.getSystemVersionByBd(param);
        map.put("rows", systemVersions);
        map.put("system", systemInfoMapper.getOneSystemInfo(systemId));
        return map;
    }



}
