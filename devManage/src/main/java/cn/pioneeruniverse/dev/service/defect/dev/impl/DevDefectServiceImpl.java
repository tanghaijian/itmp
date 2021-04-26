package cn.pioneeruniverse.dev.service.defect.dev.impl;

import cn.pioneeruniverse.dev.dao.mybatis.*;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.service.defect.dev.DevDefectService;
import cn.pioneeruniverse.dev.service.defect.test.DefectService;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * Description:缺陷管理service层
 * Author:liushan
 * Date: 2018/12/10 下午 1:45
 */
@Service("devDefectService")
@Transactional(readOnly = true)
public class DevDefectServiceImpl implements DevDefectService {

    public final static Logger logger = LoggerFactory.getLogger(DevDefectServiceImpl.class);

    @Autowired
    private TblDefectInfoMapper defectInfoMapper;
    @Autowired
    private TblDefectLogMapper defectLogMapper;
    @Autowired
    private TblDefectAttachementMapper attachementMapper;
    @Autowired
    private TblDefectLogAttachementMapper logAttachementMapper;
    @Autowired
    private DefectService defectService;


    /**
     * 同步缺陷数据
     * @param objectJson
     */
    @Override
    @Transactional(readOnly = false)
    public void syncDefect(String objectJson) throws Exception{
        Gson gson = new Gson();
        Map<String, Object> map = JSON.parseObject(objectJson);
        String opt = (String) map.get("opt");
        String defectInfoStr = (String) map.get("value");

        Map<String,Object> sysncMap = JSON.parseObject(defectInfoStr);
        logger.info("开发缺陷同步数据信息："+opt+"："+ defectInfoStr);

        if(sysncMap.containsKey("log") && sysncMap.get("log") != null && !sysncMap.get("log").toString().equals("")){
            String log = sysncMap.get("log").toString();
            TblDefectLog defectLog = gson.fromJson(log, TblDefectLog.class);
            if(defectLog != null && defectLog.getId() != null){
                defectLogMapper.insertDefectLog(defectLog);
            }
        }

        String defect = sysncMap.get("defect").toString();
        TblDefectInfo defectInfo = gson.fromJson(defect, TblDefectInfo.class);
        if(defectInfo != null && defectInfo.getId() != null){
            switch (opt){
                case "insert":
                    defectInfoMapper.insertDefect(defectInfo);
                    break;
                case "update":
                    TblDefectInfo tblDefectInfo = defectInfoMapper.getDefectById(defectInfo.getId());
                    if(tblDefectInfo != null){
                        defectInfoMapper.updateDefect(defectInfo);
                    } else {
                        defectInfoMapper.insertDefect(defectInfo);
                    }
                    break;
                case "remove":
                    defectService.removeDefectById(defectInfo.getId());
                    break;
                case "delete":
                    defectInfoMapper.deleteDefectById(defectInfo.getId());
                    break;
                default:break;
            }
        }
    }

    /**
     * 缺陷附件数据
     * @param objectJson
     */
    @Override
    @Transactional(readOnly = false)
    public void syncDefectAtt(String objectJson) throws Exception{
        Gson gson = new Gson();
        Map<String, Object> map = JSON.parseObject(objectJson);
        String opt = (String) map.get("opt");
        String defectAttStr = (String) map.get("value");
        logger.info("开发缺陷同步数据信息："+opt+"："+ defectAttStr);

        Map<String, Object> syncMap = JSON.parseObject(defectAttStr);
        String logAttachementStr = syncMap.get("logAttachement").toString();
        List<TblDefectLogAttachement> logList = gson.fromJson(logAttachementStr,new TypeToken<List<TblDefectLogAttachement>>(){}.getType());

        if(logList != null && logList.size() != 0){
            for(TblDefectLogAttachement logAttachement:logList){
                logAttachementMapper.insertSelective(logAttachement);
            }
        }

        //同步缺陷附件
        if(syncMap.containsKey("defectAttachement")){
            String defectAttachementStr = syncMap.get("defectAttachement").toString();
            List<TblDefectAttachement> defectAttList = gson.fromJson(defectAttachementStr,new TypeToken<List<TblDefectAttachement>>(){}.getType());
            if(defectAttList != null && defectAttList.size() != 0){
                switch (opt){
                    case "insert":
                        for (TblDefectAttachement defectAttachement: defectAttList) {
                            attachementMapper.insertDefectAttachement(defectAttachement);
                        }
                        break;
                    case "remove":
                        for (TblDefectAttachement defectAttachement: defectAttList) {
                            attachementMapper.removeAttachement(defectAttachement);
                        }
                        break;
                    default:break;
                }
            }
        }
    }

    /**
     * 缺陷实体信息和文件 同时处理
     * @param objectJson
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false)
    public void syncDefectWithFiles(String objectJson) throws Exception {
        Map<String, Object> map = JSON.parseObject(objectJson);
        map = JSON.parseObject(map.get("value").toString());
        String defectJson = map.get("defectMap").toString();
        String filesJson = map.get("filesMap").toString();
        this.syncDefectOpt(defectJson);
        this.syncDefectAttOpt(filesJson);
    }

    /**
    *@author author
    *@Description 同步缺陷统一处理
    *@Date 2020/8/21
     * @param defectJson
    *@return void
    **/
    @Transactional(readOnly = false)
    public void syncDefectOpt(String defectJson) throws Exception {
        Gson gson = new Gson();
        Map<String, Object> defectMap = JSON.parseObject(defectJson);

        // 处理缺陷操作
        String defectOpt = (String) defectMap.get("opt");

        if(defectMap.containsKey("log") && defectMap.get("log") != null && !defectMap.get("log").toString().equals("")){
            String log = defectMap.get("log").toString();
            TblDefectLog defectLog = gson.fromJson(log, TblDefectLog.class);
            if(defectLog != null && defectLog.getId() != null){
                defectLogMapper.insertDefectLog(defectLog);
            }
        }

        String defect = defectMap.get("defect").toString();
        if(defect != null && !defect.equals("")){
            TblDefectInfo defectInfo = gson.fromJson(defect, TblDefectInfo.class);
            if(defectInfo != null && defectInfo.getId() != null){
                switch (defectOpt){
                    case "insert":
                        defectInfoMapper.insertDefect(defectInfo);
                        break;
                    case "update":
                        TblDefectInfo tblDefectInfo = defectInfoMapper.getDefectById(defectInfo.getId());
                        if(tblDefectInfo != null){
                            defectInfoMapper.updateDefect(defectInfo);
                        } else {
                            defectInfoMapper.insertDefect(defectInfo);
                        }
                        break;
                    case "remove":
                        // 逻辑删除 status 2
                        defectService.removeDefectById(defectInfo.getId());
                        break;
                    case "delete":
                        // 物理删除
                        defectInfoMapper.deleteDefectById(defectInfo.getId());
                        break;
                    default:break;
                }
            }
        }
    }

    /**
     * 
    * @Title: syncDefectAttOpt
    * @Description: 同步缺陷附件
    * @author author
    * @param filesJson 附件信息
    * @throws Exception
    * @throws
     */
    @Transactional(readOnly = false)
    public void syncDefectAttOpt(String filesJson) throws Exception {
        Gson gson = new Gson();
        Map<String, Object> filesMap = JSON.parseObject(filesJson);
        String opt = (String) filesMap.get("opt");//同步类型，insert or remove

        //缺陷处理日志附件同步
        if(filesMap.containsKey("logAttachement") && filesMap.get("logAttachement") != null && !filesMap.get("logAttachement").toString().equals("")){
            String logAttachementStr = filesMap.get("logAttachement").toString();
            List<TblDefectLogAttachement> logList = gson.fromJson(logAttachementStr,new TypeToken<List<TblDefectLogAttachement>>(){}.getType());

            if(logList != null && logList.size() != 0){
                for(TblDefectLogAttachement logAttachement:logList){
                    logAttachementMapper.insertSelective(logAttachement);
                }
            }
        }

        //缺陷附件同步
        if(filesMap.containsKey("defectAttachement") && filesMap.get("defectAttachement") != null && !filesMap.get("defectAttachement").toString().equals("")){
            String defectAttachementStr = filesMap.get("defectAttachement").toString();
            List<TblDefectAttachement> defectAttList = gson.fromJson(defectAttachementStr,new TypeToken<List<TblDefectAttachement>>(){}.getType());
            if(defectAttList != null && defectAttList.size() != 0){
                switch (opt){
                    case "insert":
                        for (TblDefectAttachement defectAttachement: defectAttList) {
                            attachementMapper.insertDefectAttachement(defectAttachement);
                        }
                        break;
                    case "remove":
                        for (TblDefectAttachement defectAttachement: defectAttList) {
                            attachementMapper.removeAttachement(defectAttachement);
                        }
                        break;
                    default:break;
                }
            }
        }

    }

    /**
     * devTaskId
     * reqFid
     * defectId
     * @param objectJson 编辑缺陷 修改开发管理平台的参数
     */
    @Override
    @Transactional(readOnly = false)
    public void updateDevDefect(String objectJson) {
        Map<String, Object> map = JSON.parseObject(objectJson);
        defectInfoMapper.updateDevDefect(map);
    }


}
