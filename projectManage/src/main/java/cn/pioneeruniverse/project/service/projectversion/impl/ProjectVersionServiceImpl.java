package cn.pioneeruniverse.project.service.projectversion.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.project.dao.mybatis.ProjectVersionMapper;
import cn.pioneeruniverse.project.entity.TblSystemInfo;
import cn.pioneeruniverse.project.entity.TblSystemVersion;
import cn.pioneeruniverse.project.service.projectversion.IProjectVersionService;

@Service
@Transactional(readOnly = true)
public class ProjectVersionServiceImpl implements IProjectVersionService {

    @Autowired
    private ProjectVersionMapper projectVersionMapper;

    /**
     * 
    * @Title: selectSystemByProjectId
    * @Description: 获取项目关联的系统
    * @author author
    * @param projectId 项目id
    * @return Map<String, Object>
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> selectSystemByProjectId(Long projectId) {
        Map<String, Object> map = new HashMap<>();
        List<TblSystemInfo> allSystemList = projectVersionMapper.selectSystemByProjectId(projectId);
        //系统信息
        map.put("allSystemList",allSystemList);
        return map;
    }

    /**
    * @author author
    * @Description 根据条件查询所有版本
    * @Date 2020/9/9
    * @param projectId 项目Id
    * @return Map<String, Object>
    **/
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getProjectVersionByCon(Long projectId) {
        Map<String, Object> map = new HashMap<>();
        List<TblSystemVersion> systemVersions = projectVersionMapper.getProjectVersionByCon(projectId);
       //系统版本
        map.put("rows", systemVersions);
        return map;
    }

    /**
    * @author author
    * @Description 添加版本
    * @Date 2020/9/9
    * @param systemVersion 系统版本信息
    * @param request
    **/
    @Override
    @Transactional(readOnly = false)
    public void insertVersion(TblSystemVersion systemVersion, HttpServletRequest request) {
        String [] systemIds = systemVersion.getSystemIds().split(",");
        for (String systemId : systemIds){
            systemVersion.setSystemId(Long.valueOf(systemId));
            List<TblSystemVersion> systemList = projectVersionMapper.getSystemVersionByVersion(systemVersion);
            if(systemList == null||systemList.size()<=0){
            	//给公共字段赋值，比如更新人，更新时间等
                CommonUtil.setBaseValue(systemVersion, request);
                projectVersionMapper.insertVersion(systemVersion);
            }
        }
    }

    /**
    * @author author
    * @Description 获取修改信息
    * @Date 2020/9/9
    * @param systemVersion
    * @return Map<String, Object>
    **/
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getProjectVersionBySystemVersion(TblSystemVersion systemVersion){
        Map<String, Object> map = new HashMap<>();
        List<TblSystemInfo> allSystemList = projectVersionMapper.selectSystemByProjectId(systemVersion.getProjectId());
        List<TblSystemVersion> systemList = projectVersionMapper.getProjectVersionByVersion(systemVersion);
        //系统版本信息
        map.put("systemList", systemList);
        //系统信息
        map.put("allSystemList", allSystemList);
        return map;
    }
    /**
    * @author author
    * @Description 修改版本
    * @Date 2020/9/9
    * @param systemVersion 系统版本
    * @param request
    **/
    @Override
    @Transactional(readOnly = false)
    public void updateVersion(TblSystemVersion systemVersion, HttpServletRequest request) {
        //旧版本信息
        List<TblSystemVersion> beforeUpdate = projectVersionMapper.getBeforeUpdate(systemVersion.getIds());
        systemVersion.setSystemId(Long.valueOf(4));
        projectVersionMapper.deleteVersion(systemVersion.getIds());
        String [] systemArr = systemVersion.getSystemIds().split(",");
        for(String systemId : systemArr){
            systemVersion.setSystemId(Long.valueOf(systemId));
            List<TblSystemVersion> systemList = projectVersionMapper.getSystemVersionByVersion(systemVersion);
            if(systemList == null||systemList.size()<=0){
                Integer status = getStatus(beforeUpdate,systemId);
                systemVersion.setStatus(status);
                CommonUtil.setBaseValue(systemVersion, request);
                projectVersionMapper.insertVersion(systemVersion);
            }
        }
    }
    
    /**
     * 
    * @Title: closeOrOpenSystemVersion
    * @Description:系统版本开关
    * @author author
    * @param systemVersion 系统版本
    * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public void closeOrOpenSystemVersion(TblSystemVersion systemVersion, HttpServletRequest request){
        String [] ids = systemVersion.getIds().split(",");
        for (String id:ids){
            TblSystemVersion systemVersion1 = new TblSystemVersion();
            systemVersion1.setId(Long.valueOf(id));
            systemVersion1.setStatus(systemVersion.getStatus());
            systemVersion1.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
            systemVersion1.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
            projectVersionMapper.updateStatusById(systemVersion1);
        }
    }

   private Integer getStatus(List<TblSystemVersion> beforeUpdate,String systemId){
        Integer status = 1;
       for (TblSystemVersion version:beforeUpdate){
           if(version.getSystemId().toString().equals(systemId)){
               status = version.getStatus();
           }
       }
       return status;
   }
}
