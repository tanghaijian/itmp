package cn.pioneeruniverse.common.Rest;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.service.systeminfo.ISystemInfoService;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

/**
 * @deprecated
 * Description: 同步系统
 * Author:liushan
 * Date: 2018/11/21 下午 7:21
 */
@RestController()
@RequestMapping("system")
public class SystemDataUtil extends BaseController {

    @Autowired
    private ISystemInfoService systemInfoService;

    /**
     * 
    * @Title: updateSystemData
    * @Description: 接收报文
    * @author author
    * @param systemData 系统数据
    * @param response
    * @param request void
     */
    @RequestMapping(value = "updateSystemData",method = RequestMethod.POST,produces="application/json;utf-8")
    public void updateSystemData(@RequestBody String systemData, HttpServletResponse response, HttpServletRequest request) {
        List<Object> systemList = new ArrayList<>();
        try{
            if (StringUtils.isNotBlank(systemData)){
                systemList = JSONObject.parseArray(systemData);
                TblSystemInfo systemInfo = new TblSystemInfo();
                if ( systemList != null && systemList.size() != 0){
                   for (int i = 0,len = systemList.size();i < len; i ++){
                       Gson gson = new Gson();
                       Map<String, Object> map = new HashMap<String, Object>();
                       map = gson.fromJson(systemList.get(i).toString(), map.getClass());
                       for (Map.Entry<String, Object> entiy:map.entrySet()) {
                    	   //系统编码
                           if (entiy.getKey().equals("applicationCdoe")){
                               systemInfo.setSystemCode(entiy.getValue().toString());
                           }
                           //系统名称
                           if (entiy.getKey().equals("applicationName")){
                                systemInfo.setSystemName(entiy.getValue().toString());
                           }
                           //系统简称
                           if (entiy.getKey().equals("applicationAbbName")){
                                systemInfo.setSystemShortName(entiy.getValue().toString());
                           }
                           //系统类型
                           if(entiy.getKey().equals("applicationType")){
                               systemInfo.setSystemType(entiy.getValue().toString());
                           }
                       }
                       if (systemInfo != null ){
                           TblSystemInfo system = systemInfoService.getSystemByCode(systemInfo.getSystemCode());
                           if (system == null){
                               systemInfo.setCreateBy(1L);
                               systemInfo.setCreateDate(new Timestamp(new Date().getTime()));
                               systemInfo.setLastUpdateBy(1L);
                               systemInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
                               systemInfoService.insertSystem(systemInfo);
                           } else {
                               system.setSystemName(systemInfo.getSystemName());
                               system.setSystemType(systemInfo.getSystemType());
                               system.setSystemShortName(systemInfo.getSystemShortName());
                               system.setLastUpdateBy(1L);
                               system.setLastUpdateDate(new Timestamp(new Date().getTime()));
                               systemInfoService.updateSystemById(system);
                               systemInfoService.updateTmpSystemInfo(system,1);
                           }
                           systemInfo = new TblSystemInfo();
                       }
                   }
                }
                response.setIntHeader("status",0);
            }
        }catch(Exception e){
            e.printStackTrace();
            response.setIntHeader("status",2);
            response.setHeader("appCode", "500");
            response.setHeader("appMessage", e.getMessage());
            logger.error("修改用户信息" + ":" + e.getMessage(), e);
            this.handleException(e, "修改系统信息失败");
        }
    }
}
