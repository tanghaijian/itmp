package cn.pioneeruniverse.dev.service.defect.dev.impl;

import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectLogAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectLogMapper;
import cn.pioneeruniverse.dev.entity.TblDefectAttachement;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblDefectLog;
import cn.pioneeruniverse.dev.entity.TblDefectLogAttachement;
import cn.pioneeruniverse.dev.service.defect.DefectService;
import cn.pioneeruniverse.dev.service.defect.dev.TestDefectService;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Description:缺陷管理service层
 * Author:liushan
 * Date: 2018/12/10 下午 1:45
 */
@Transactional(readOnly = true)
@Service("devDefectService")
public class TestDefectServiceImpl implements TestDefectService {

    public final static Logger logger = LoggerFactory.getLogger(TestDefectServiceImpl.class);

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
        logger.info("测试缺陷同步数据信息："+opt+"："+ defectInfoStr);

        // 日志
        if(sysncMap.containsKey("log") && sysncMap.get("log") != null && !sysncMap.get("log").toString().equals("")){
            String log = sysncMap.get("log").toString();
            TblDefectLog defectLog = gson.fromJson(log, TblDefectLog.class);
            if(defectLog != null && defectLog.getId() != null){
                defectLogMapper.insertDefectLog(defectLog);
            }
        }
        // 缺陷
        String defect = sysncMap.get("defect").toString();
        if(defect != null && !defect.equals("")){
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
    }

    /**
     * 缺陷附件数据
     * @param objectJson
     */
    @Override
    @Transactional(readOnly = false)
    public void syncDefectAtt(String objectJson) throws Exception {
        Gson gson = new Gson();
        Map<String, Object> map = JSON.parseObject(objectJson);
        String opt = (String) map.get("opt");
        String defectAttStr = (String) map.get("value");
        logger.info("开发缺陷同步数据信息："+opt+"："+ defectAttStr);
        
        Map<String, Object> syncMap = JSON.parseObject(defectAttStr);
        // 日志
        String logAttachementStr = syncMap.get("logAttachement").toString();
        if(logAttachementStr != null && !logAttachementStr.equals("")){
            List<TblDefectLogAttachement> logList = gson.fromJson(logAttachementStr,new TypeToken<List<TblDefectLogAttachement>>(){}.getType());
            if(logList != null && logList.size() != 0){
                for(TblDefectLogAttachement logAttachement:logList){
                    logAttachementMapper.insertSelective(logAttachement);
                }
            }
        }

        // 附件
        if(syncMap.containsKey("defectAttachement") && syncMap.get("defectAttachement") != null && !syncMap.get("defectAttachement").toString().equals("")){
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
    *@author author
    *@Description 新增缺陷附件
    *@Date 2020/8/20
     * @param objectJson
    *@return void
    **/
    @Override
    @Transactional(readOnly = false)
    public void insertDefectAttachement(String objectJson) {
        Gson gson = new Gson();
        TblAttachementInfoDTO attachementInfoDTO = gson.fromJson(objectJson, TblAttachementInfoDTO.class);
        attachementMapper.insertDefectDTOAttachement(attachementInfoDTO);
    }
}
