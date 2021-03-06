package cn.pioneeruniverse.dev.service.defect.test.impl;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.bean.PropertyInfo;
import cn.pioneeruniverse.common.bean.ReflectFieledType;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.constants.DicConstants;
import cn.pioneeruniverse.common.dto.TblAttachementInfoDTO;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.CommonUtils;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.utils.ExcelUtil;
import cn.pioneeruniverse.common.utils.HttpUtil;
import cn.pioneeruniverse.common.utils.PageWithBootStrap;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.dev.dao.mybatis.TblCommissioningWindowMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectLogAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectLogMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectRemarkAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectRemarkMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemVersionMapper;
import cn.pioneeruniverse.dev.entity.DefectInfoVo;
import cn.pioneeruniverse.dev.entity.TblCommissioningWindow;
import cn.pioneeruniverse.dev.entity.TblDefectAttachement;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblDefectLog;
import cn.pioneeruniverse.dev.entity.TblDefectLogAttachement;
import cn.pioneeruniverse.dev.entity.TblDefectRemark;
import cn.pioneeruniverse.dev.entity.TblDefectRemarkAttachement;
import cn.pioneeruniverse.dev.entity.TblDevTask;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblRequirementInfo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.entity.TblSystemVersion;
import cn.pioneeruniverse.dev.feignInterface.DevManageToProjectManageInterface;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import cn.pioneeruniverse.dev.feignInterface.DevManageToTestManageInterface;
import cn.pioneeruniverse.dev.service.defect.test.DefectService;
import cn.pioneeruniverse.dev.service.worktask.WorkTaskService;

/**
 * Description:????????????service???
 * Author:liushan
 * Date: 2018/12/10 ?????? 1:45
 */
@Transactional(readOnly = true)
@Service("defectService")
public class DefectServiceImpl implements DefectService {

    public final static Logger logger = LoggerFactory.getLogger(DefectServiceImpl.class);

    private static final String LOG_TYPE_1 = "????????????";
    private static final String LOG_TYPE_2 = "????????????";
    private static final String LOG_TYPE_3 = "????????????";

    @Autowired
    private TblDefectInfoMapper defectInfoMapper;
    @Autowired
    private TblSystemInfoMapper systemInfoMapper;
    @Autowired
    private TblDefectLogMapper defectLogMapper;
    @Autowired
    private TblDefectLogAttachementMapper logAttachementMapper;
    @Autowired
    private TblDefectRemarkMapper defectRemarkMapper;
    @Autowired
    private TblDefectRemarkAttachementMapper remarkAttachementMapper;
    @Autowired
    private TblDefectAttachementMapper attachementMapper;
    @Autowired
    private TblDevTaskMapper devTaskMapper;
    @Autowired
    private TblCommissioningWindowMapper windowMapper;
    @Autowired
    private TblRequirementFeatureMapper featureMapper;
    @Autowired
    private TblSystemVersionMapper systemVersionMapper;
    @Autowired
    private DevManageToTestManageInterface toTestManageInterface;
    @Autowired
    private DevManageToSystemInterface devManageToSystemInterface;
    @Autowired
    private DevManageToProjectManageInterface devManageToProjectManageInterface;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private WorkTaskService workTaskService;
    @Autowired
    private S3Util s3Util;
    
    //?????????????????????????????????true:feign ???false httpclient
    @Value("${itmp.devTest.integration}")
    private Boolean integration;
    //??????????????????
    @Value("${itmp.test.url}")
    private String devTestUrl;

    /**
     * 
    * @Title: getDefectEntity
    * @Description: ???????????? ??????????????????
    * @author author
    * @param defectId
    * @return
    * @throws
     */
    public TblDefectInfo getDefectEntity(Long defectId) {
        TblDefectInfo defectInfo = defectInfoMapper.getDefectEntity(defectId);
        //?????????????????????
        if (defectInfo != null && defectInfo.getProjectGroupId() != null) {
            Map<String, Object> projectGroup = devManageToProjectManageInterface.getProjectGroupByProjectGroupId(defectInfo.getProjectGroupId());
            if (projectGroup != null && !projectGroup.isEmpty()) {
                defectInfo.setProjectGroupName((String) projectGroup.get("projectGroupName"));
            }
        }
        //???????????????????????????????????????
        if (defectInfo != null && defectInfo.getAssetSystemTreeId() != null) {
            Map<String, Object> assetSystemTree = devManageToProjectManageInterface.getAssetSystemTreeById(defectInfo.getAssetSystemTreeId());
            if (assetSystemTree != null && !assetSystemTree.isEmpty()) {
                defectInfo.setAssetSystemTreeName((String)assetSystemTree.get("businessSystemTreeName"));
            }
        }
        //???????????????????????????
        if (defectInfo != null && defectInfo.getDetectedSystemVersionId() != null) {
            TblSystemVersion detectedSystemVersion = systemVersionMapper.selectByPrimaryKey(defectInfo.getDetectedSystemVersionId());
            if (detectedSystemVersion != null) {
                defectInfo.setDetectedSystemVersionName(detectedSystemVersion.getVersion());
            }
        }
        //?????????????????????????????????
        if (defectInfo != null && defectInfo.getRepairSystemVersionId() != null) {
            TblSystemVersion repairSystemVersion = systemVersionMapper.selectByPrimaryKey(defectInfo.getRepairSystemVersionId());
            if (repairSystemVersion != null) {
                defectInfo.setRepairSystemVersionName(repairSystemVersion.getVersion());
            }
        }
        return defectInfo;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param key
     * @return
     */
    public Map<String, Object> getDicKey(String key) {
        Object object = redisUtils.get(key);
        Map<String, Object> map = JSON.parseObject(object.toString());
        return map;
    }

    /**
    * @author author
    * @Description ??????????????????
    * @Date 2020/9/18
    * @param id
    * @param request
    * @return void
    **/
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void removeDefect(Long id, HttpServletRequest request) throws Exception {
        this.removeDefectById(id);

        // ??????????????????????????????
        String url = devTestUrl + "testManage/defect/syncDefect";
        TblDefectInfo tblDefectInfo = new TblDefectInfo();
        tblDefectInfo.setId(id);
        this.syncOpt(url, "remove", tblDefectInfo);
    }

    /**
    * @author author
    * @Description ?????????????????? ?????????
    * @Date 2020/9/18
    * @param id
    * @return void
    **/
    public void removeDefectById(Long id) {
        defectInfoMapper.removeDefect(id);
        attachementMapper.removeAttachements(id);
        // ?????????????????????id
        Long[] remarkId = defectRemarkMapper.findRemarkByDefectId(id);
        if (remarkId != null && remarkId.length > 0) {
            remarkAttachementMapper.removeRemarkAttachements(remarkId);
            defectRemarkMapper.removeDefectRemark(remarkId);
        }
    }

    /**
     * ????????????????????????
     * ?????????????????????????????????????????????????????????
     *
     * @param tblDefectInfo
     * @param defectRemark
     * @param request
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Long updateDefectwithTBC(TblDefectInfo tblDefectInfo, TblDefectRemark defectRemark, Double actualWorkload, HttpServletRequest request) throws Exception {

        TblDefectLog defectLog = new TblDefectLog();
        defectLog.setDefectId(tblDefectInfo.getId());

        // ??????????????????????????????
        TblDefectInfo old_defectInfo = this.old_defectInfo(tblDefectInfo.getId());

        tblDefectInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
        tblDefectInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));

        // ???????????????????????????
        TblDefectInfo newDefect = new TblDefectInfo();
        // ???????????????:????????????????????????????????????
        if (tblDefectInfo.getDefectStatus() != null) {
            if (tblDefectInfo.getDefectStatus().intValue() == 2 && tblDefectInfo.getAssignUserId() != null) {
                defectLog.setLogType(LOG_TYPE_3);
                defectInfoMapper.updateDefectAssignUser(tblDefectInfo);

            } else {
                defectLog.setLogType(LOG_TYPE_2);
                // ???????????????????????????????????????????????????
                if (tblDefectInfo.getDefectStatus().intValue() == 3 && tblDefectInfo.getRejectReason() != null) {
                    defectInfoMapper.updateDefectRejectReason(tblDefectInfo);
                }

                // ??????????????????????????????????????????????????????
                if (tblDefectInfo.getDefectStatus().intValue() == 4) {
                    defectInfoMapper.updateDefectStatus(tblDefectInfo);
                }

                // ????????????????????? ????????????????????? ???????????????
                if (tblDefectInfo.getDefectStatus().intValue() == 5 || tblDefectInfo.getDefectStatus().intValue() == 8 && tblDefectInfo.getSolveStatus() != null) {
                    defectInfoMapper.updateDefectSolveStatus(tblDefectInfo);
                    if (old_defectInfo.getDevTaskId() != null) {
                        TblDevTask devTask = new TblDevTask();
                        devTask.setId(old_defectInfo.getDevTaskId());
                        // ??????????????????????????? ???3 ????????????
                        if (tblDefectInfo.getDefectStatus().intValue() == 5) {
                            devTask.setDevTaskStatus(3);
                        }
                        devTask.setActualWorkload(actualWorkload);
                        devTask.setActualStartDate(tblDefectInfo.getActualStartDate());
                        devTask.setActualEndDate(tblDefectInfo.getActualEndDate());
                        workTaskService.Handle(devTask, "", "", request);
                    }
                }
            }
            newDefect = defectInfoMapper.selectByPrimaryKey(tblDefectInfo.getId());
        }

        // ????????????
        Long logId = this.insertDefectLog(defectLog, request);

        // ????????????
        if (defectRemark == null) {
            defectRemark = new TblDefectRemark();
        }
        if (StringUtils.isBlank(defectRemark.getDefectRemark())) {
            defectRemark.setDefectRemark("?????????????????????");
        }
        Map<String, Object> remarkMap = new HashMap<>();
        remarkMap.put("remark", defectRemark.getDefectRemark());

        Map<String, Object> sysncMap = new HashMap<>();
        try {
            updateFieledsReflect(old_defectInfo, newDefect, remarkMap, logId);
            TblDefectLog log = defectLogMapper.selectById(logId);
            // ????????????
            sysncMap.put("log", JSON.toJSON(log));
        } catch (Exception e) {
            logger.error("??????????????????", e);
            throw new Exception(e);
        }

        // ????????????????????????
        sysncMap.put("defect", JSON.toJSON(newDefect));
        String devUrl = devTestUrl + "testManage/defect/syncDefect";
        this.syncOpt(devUrl, "update", sysncMap);


        // ???????????????????????????????????????????????????,??????????????????
        if (ObjectUtils.equals(tblDefectInfo.getDefectStatus(), 3) && tblDefectInfo.getRejectReason() != null) {
            sendMessageForDefectStatusUpdate(old_defectInfo);
        }
        // ??????????????????????????????????????????????????????,??????????????????
        if (ObjectUtils.equals(tblDefectInfo.getDefectStatus(), 4)) {
            sendMessageForDefectStatusUpdate(old_defectInfo);
        }
        // ????????????????????? ????????????????????? ????????????????????????????????????????????????
        if ((ObjectUtils.equals(tblDefectInfo.getDefectStatus(), 5) || ObjectUtils.equals(tblDefectInfo.getDefectStatus(), 8)) && tblDefectInfo.getSolveStatus() != null) {
            sendMessageForDefectStatusUpdate(old_defectInfo);
        }
        return logId;
    }

    /**
     * ???????????????
     * @param tblDefectInfo
     * @param defectRemark
     * @param request
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Long updateDefectStatus(TblDefectInfo tblDefectInfo, TblDefectRemark defectRemark, HttpServletRequest request) throws Exception {

        // ?????????????????????
        TblDefectInfo old_defectInfo = this.old_defectInfo(tblDefectInfo.getId());

        TblDefectLog defectLog = new TblDefectLog();
        defectLog.setLogType(LOG_TYPE_2);
        defectLog.setDefectId(tblDefectInfo.getId());

        // ????????????????????????????????????????????????????????? ?????????????????????????????????
        defectInfoMapper.updateDefectStatus(tblDefectInfo);

        // ??????????????????
        TblDefectInfo newDefect = defectInfoMapper.selectByPrimaryKey(tblDefectInfo.getId());

        // ??????
        Long logId = this.insertDefectLog(defectLog, request);

        // ??????
        if (StringUtils.isBlank(defectRemark.getDefectRemark())) {
            defectRemark.setDefectRemark("?????????????????????");
        }
        Map<String, Object> remarkMap = new HashMap<>();
        remarkMap.put("remark", defectRemark.getDefectRemark());

        // ????????????
        Map<String, Object> sysncMap = new HashMap<>();
        try {
            updateFieledsReflect(old_defectInfo, newDefect, remarkMap, logId);
            TblDefectLog log = defectLogMapper.selectById(logId);
            sysncMap.put("log", JSON.toJSON(log));
        } catch (Exception e) {
            logger.error("??????????????????", e);
            throw new Exception(e);
        }

        // ????????????????????????
        sysncMap.put("defect", JSON.toJSON(newDefect));
        String devUrl = devTestUrl + "testManage/defect/syncDefect";
        this.syncOpt(devUrl, "update", sysncMap);

        // ???????????????????????????????????????
        sendMessageForDefectStatusUpdate(newDefect);
        return logId;
    }

    /**
     * ???????????????????????????
     *
     * @param defectId
     * @return
     */
    private TblDefectInfo old_defectInfo(Long defectId) {
        TblDefectInfo old_defectInfo = new TblDefectInfo();
        TblDefectInfo oldDefect = defectInfoMapper.selectByPrimaryKey(defectId);
        BeanUtils.copyProperties(oldDefect, old_defectInfo);
        return old_defectInfo;
    }

    /**
     * ??????????????? ?????????????????????????????? 1???,????????????????????????????????? 2???
     * ??????????????????????????????????????????????????????
     * @param tblDefectInfo
     * @param request
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> insertDefect(TblDefectInfo tblDefectInfo, HttpServletRequest request) throws Exception {
        // ????????????
        Map<String, Object> map = new HashMap<>();
        // ??????????????????
        Map<String, Object> sysncMap = new HashMap<>();

        // ????????????
        tblDefectInfo.setSubmitUserId(CommonUtil.getCurrentUserId(request));
        tblDefectInfo.setSubmitDate(DateUtil.getDate(new Date().toString(), "yyyy-mm-dd"));
        tblDefectInfo = (TblDefectInfo) CommonUtil.setBaseValue(tblDefectInfo, request);
        tblDefectInfo.setDefectCode(getDefectCode());
        defectInfoMapper.insertDefect(tblDefectInfo);

        Long defectId = tblDefectInfo.getId();

        // ????????????
        TblDefectLog defectLog = new TblDefectLog();
        defectLog.setDefectId(defectId);
        defectLog.setLogType(LOG_TYPE_1);
        Long logId = this.insertDefectLog(defectLog, request);

        try {
            TblDefectInfo info = new TblDefectInfo();
            updateFieledsReflect(info, tblDefectInfo, new HashMap<>(), logId);
            TblDefectLog log = defectLogMapper.selectById(logId);
            // ????????????
            sysncMap.put("log", JSON.toJSON(log));
        } catch (Exception e) {
            logger.error("??????????????????", e);
            throw new Exception(e);
        }

        // ??????????????????
        sysncMap.put("defect", JSON.toJSON(tblDefectInfo));
        String url = devTestUrl + "testManage/defect/syncDefect";
        this.syncOpt(url, "insert", sysncMap);

        // ????????????
        map.put("defectId", defectId);
        map.put("logId", logId);
        return map;
    }

    /**
     * 
    * @Title: updateDefect
    * @Description: ??????????????????
    * @author author
    * @param tblDefectInfo ????????????
    * @param request
    * @return
    * @throws Exception
    * @throws
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> updateDefect(TblDefectInfo tblDefectInfo, HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Long defectId = tblDefectInfo.getId();

        // ????????????????????????
        TblDefectInfo old_defectInfo = new TblDefectInfo();
        TblDefectInfo oldDefect = defectInfoMapper.selectByPrimaryKey(defectId);
        if (oldDefect != null) {
            BeanUtils.copyProperties(oldDefect, old_defectInfo);

            tblDefectInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
            tblDefectInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
            //????????????
            defectInfoMapper.updateDefect(tblDefectInfo);

            // ??????????????? ??? ????????????id ?????????????????????????????????ID
            if (tblDefectInfo.getAssignUserId() != null && oldDefect.getDevTaskId() != null) {
                TblDevTask devTask = new TblDevTask();
                devTask.setId(oldDefect.getDevTaskId());
                devTask.setDevUserId(tblDefectInfo.getAssignUserId());
                devTaskMapper.updateDevTask(devTask);
            }

            // ????????????????????????
            TblDefectInfo newDefect = defectInfoMapper.selectByPrimaryKey(defectId);

            //??????????????????
            TblDefectLog defectLog = new TblDefectLog();
            defectLog.setDefectId(defectId);
            defectLog.setLogType(LOG_TYPE_3);
            Long logId = this.insertDefectLog(defectLog, request);

            // ??????????????????
            Map<String, Object> sysncMap = new HashMap<>();
            try {
                updateFieledsReflect(old_defectInfo, newDefect, new HashMap<>(), logId);
                TblDefectLog log = defectLogMapper.selectById(logId);
                // ????????????
                sysncMap.put("log", JSON.toJSON(log));
            } catch (Exception e) {
                logger.error("??????????????????", e);
                throw new Exception(e);
            }

            // ???????????????????????????????????????
            sysncMap.put("defect", JSON.toJSON(newDefect));
            String url = devTestUrl + "testManage/defect/syncDefect";
            this.syncOpt(url, "update", sysncMap);

            //demo
            //???????????????????????????????????????????????????
            if (!ObjectUtils.equals(old_defectInfo.getDefectStatus(), newDefect.getDefectStatus())) {
                Long projectId = defectInfoMapper.getDefectInfoById(newDefect.getId());
                sendMessage(newDefect, projectId);
            }

            map.put("defectId", defectId);
            map.put("logId", logId);
        }
        return map;
    }

    /**
     * ??????????????????
     *
     * @param defectLog
     * @param request
     */
    public Long insertDefectLog(TblDefectLog defectLog, HttpServletRequest request) {
        defectLog = this.setDefectLog(defectLog, request);
        defectLog.setCreateBy(CommonUtil.getCurrentUserId(request));
        defectLog.setCreateDate(new Timestamp(new Date().getTime()));
        defectLogMapper.insertDefectLog(defectLog);
        return defectLog.getId();
    }


    /**
    *@author liushan
    *@Description ??????????????????????????????
    *@Date 2020/7/21
    *@Param [page, defectInfo, currentUserId, request]
    *@return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.dev.entity.DefectInfoVo>
    **/
    @Override
    public JqGridPage<DefectInfoVo> findDefectListPage(JqGridPage<DefectInfoVo> page, DefectInfoVo defectInfo, Long currentUserId, HttpServletRequest request) throws Exception {
        DefectInfoVo defectInfoVo = page.filtersToEntityField(defectInfo);

        Boolean flag = new CommonUtils().currentUserWithAdmin(request);
        if (flag == false) {
            Map<String, Object> map = new HashMap<>();
            map.put("currentUserId", currentUserId);
            // ????????????????????????????????????ID
            List<TblSystemInfo> systemInfoList = systemInfoMapper.findSystemWithProjectByUserId(map);
            defectInfoVo.setSystemInfoList(systemInfoList);
        }

        int rows = page.getJqGridPrmNames().getRows();
        int pageNum = page.getJqGridPrmNames().getPage();

//        defectInfoVo.setPage((pageNum - 1) * rows);
//        defectInfoVo.setRows(rows);
        PageHelper.startPage(pageNum, rows);

        List<TblDefectInfo> list = defectInfoMapper.findDefectList(defectInfoVo);
//        int count = defectInfoMapper.countFindDefectList(defectInfoVo);

        PageInfo<TblDefectInfo> pageInfo = new PageInfo<>(list);
//        pageInfo.setPages((count+rows-1)/rows);
//        pageInfo.setTotal(count);
//        pageInfo.setPageNum(pageNum);
        page.processDataForResponse(pageInfo);
        return page;
    }

    /**
    * @author author
    * @Description ??????id??????????????????
    * @Date 2020/9/18
    * @param defectId
    * @return cn.pioneeruniverse.dev.entity.TblDefectInfo
    **/
    @Override
    public TblDefectInfo getDefectById(Long defectId) {
        return defectInfoMapper.getDefectById(defectId);
    }

    /**
     * ????????????
     *
     * @param files
     * @param defectId
     * @param logId
     * @param request
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String updateFiles(MultipartFile[] files, Long defectId, Long logId, HttpServletRequest request) throws Exception {

        if (files.length > 0 && files != null) {

            Map<String, Object> syscMap = new HashMap<>();
            List<TblDefectAttachement> list = new ArrayList<TblDefectAttachement>();
            List<TblDefectLogAttachement> logList = new ArrayList<TblDefectLogAttachement>();

            for (MultipartFile file : files) {
                if (Objects.isNull(file) || file.isEmpty()) {
                    return "NULL";
                }
                TblAttachementInfoDTO attachementInfoDTO = updateFile(file);
                TblDefectAttachement defectAttachement = new TblDefectAttachement();
                defectAttachement = (TblDefectAttachement) CommonUtil.setBaseValue(defectAttachement, request);
                defectAttachement.setDefectId(defectId);
                defectAttachement.setFileType(attachementInfoDTO.getFileType());
                defectAttachement.setFileS3Key(attachementInfoDTO.getFileS3Key());
                defectAttachement.setFileS3Bucket(s3Util.getDefectBucket());
                defectAttachement.setFilePath(attachementInfoDTO.getFilePath());
                defectAttachement.setFileNameOld(attachementInfoDTO.getFileNameOld());
                attachementMapper.insertDefectAttachement(defectAttachement);
                list.add(defectAttachement);

                TblDefectLogAttachement logAttachement = insertLogAttachement(attachementInfoDTO, logId, request);
                logList.add(logAttachement);
            }

            // ????????????????????????
            syscMap.put("defectAttachement", list);
            syscMap.put("logAttachement", logList);
            String url = devTestUrl + "testManage/defect/syncDefectAtt";
            this.syncOpt(url, "insert", syscMap);
        }

        return "SUCCESS";
    }

    /**
     * ????????????????????????
     *
     * @param files
     * @param logId
     * @param request
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public String updateRemarkLogFiles(MultipartFile[] files, Long logId, HttpServletRequest request) throws Exception {
        if (files.length > 0 && files != null) {
            Map<String, Object> syscMap = new HashMap<>();
            List<TblDefectLogAttachement> logList = new ArrayList<TblDefectLogAttachement>();
            for (MultipartFile file : files) {
                if (Objects.isNull(file) || file.isEmpty()) {
                    return "NULL";
                }
                TblAttachementInfoDTO attachementInfoDTO = updateFile(file);

                TblDefectLogAttachement logAttachement = insertLogAttachement(attachementInfoDTO, logId, request);
                logList.add(logAttachement);
            }
            //??????????????????????????????
            syscMap.put("logAttachement", logList);
            String url = devTestUrl + "testManage/defect/syncDefectAtt";
            this.syncOpt(url, "insert", syscMap);
        }
        return "SUCCESS";
    }

    /**
     * ??????????????????
     *
     * @param attachementInfoDTO
     * @param logId
     */
    private TblDefectLogAttachement insertLogAttachement(TblAttachementInfoDTO attachementInfoDTO, Long logId, HttpServletRequest request) {
        TblDefectLogAttachement logAttachement = new TblDefectLogAttachement();
        logAttachement.setDefectLogId(logId);
        logAttachement.setFileType(attachementInfoDTO.getFileType());
        logAttachement.setFileS3Key(attachementInfoDTO.getFileS3Key());
        logAttachement.setFileS3Bucket(s3Util.getDefectBucket());
        logAttachement.setFilePath(attachementInfoDTO.getFilePath());
        logAttachement.setFileNameOld(attachementInfoDTO.getFileNameOld());
        logAttachement = (TblDefectLogAttachement) CommonUtil.setBaseValue(logAttachement, request);
        logAttachementMapper.insertSelective(logAttachement);
        return logAttachement;
    }

    /**
     * ??????????????????
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = false)
    public TblAttachementInfoDTO updateFile(MultipartFile file) throws Exception {
        TblAttachementInfoDTO attachementInfoDTO = new TblAttachementInfoDTO();
        InputStream inputStream = file.getInputStream();
        String fileName = file.getOriginalFilename();
        fileName = fileName.substring(file.getOriginalFilename().lastIndexOf("\\") + 1);
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);//?????????
        String oldFileName = fileName.substring(0, fileName.lastIndexOf("."));
        /*File serverfile = new File(attachementInfoDTO.getFilePath());
        if(!serverfile.getParentFile().exists()) {
            serverfile.getParentFile().mkdirs();
        }
        serverfile.createNewFile();
        file.transferTo(serverfile);*/
        Random random = new Random();
		String i = String.valueOf(random.nextInt());
        String keyname = s3Util.putObject(s3Util.getDefectBucket(), i, inputStream);
        attachementInfoDTO.setFileS3Key(keyname);
        attachementInfoDTO.setFilePath(attachementInfoDTO.getFilePath() + fileName);
        attachementInfoDTO.setFileNameOld(fileName);
        attachementInfoDTO.setFileType(fileType);
        return attachementInfoDTO;
    }

    /**
    * @author author
    * @Description ??????????????????????????????
    * @Date 2020/9/18
    * @param path ????????????
    * @return cn.pioneeruniverse.dev.entity.TblDefectAttachement
    **/
    @Override
    public TblDefectAttachement getDefectAttByUrl(String path) {
        return attachementMapper.getDefectAttByUrl(path);
    }

    /**
    * @author author
    * @Description ??????????????????????????????????????????
    * @Date 2020/9/18
    * @param defectId ????????????
    * @return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectAttachement>
    **/
    @Override
    public List<TblDefectAttachement> findAttListByDefectId(Long defectId) {
        return attachementMapper.findAttListByDefectId(defectId);
    }

    /**
     * ??????id??????????????????
     *
     * @param defectId
     * @return
     */
    @Override
    public List<TblDefectLog> getDefectLogsById(Long defectId) {
        return defectLogMapper.getDefectLogsById(defectId);
    }

    /**
    *@author liushan
    *@Description ???????????????????????????
    *@Date 2020/7/31
    * @param defectId ??????id
    *@return java.util.Map<java.lang.String,java.lang.Object> ???????????????
    **/
    @Override
    public Map<String, Object> getDefectRecentLogById(Long defectId) {
        Map<String, Object> result = new HashMap<String, Object>();
        // ?????????????????????????????????????????????id
        Long logId = defectLogMapper.getDefectMaxLogId(defectId);
        TblDefectInfo defectInfo = defectInfoMapper.getDefectById(defectId);

        // ??????????????????
        if (defectInfo.getDevTaskId() != null) {
            TblDevTask devTask = workTaskService.getDevTaskById(defectInfo.getDevTaskId());
            result.put("devTask", devTask);
        }

        // ????????????
        if (defectInfo.getRequirementFeatureId() != null) {
            TblRequirementFeature feature = featureMapper.getFeatureById(defectInfo.getRequirementFeatureId());
            result.put("feature", feature);
        }

        // ????????????????????????
        result.put("data", defectLogMapper.getDefectRecentLogById(logId));

        return result;
    }
    
    /**
    *@author liushan
    *@Description ????????????
    *@Date 2020/7/31
    * @param ids ?????????????????????ids
    * @param defectId ??????id
    * @param logId ??????id
    * @param request
    *@return void
    **/
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void removeAtts(Long[] ids, Long defectId, Long logId, HttpServletRequest request) throws Exception {
        if (ids != null && ids.length != 0) {

            Map<String, Object> syscMap = new HashMap<>();
            List<TblDefectAttachement> list = new ArrayList<TblDefectAttachement>();
            List<TblDefectLogAttachement> logList = new ArrayList<TblDefectLogAttachement>();

            for (int i = 0, len = ids.length; i < len; i++) {
                TblDefectAttachement defectAttachement = new TblDefectAttachement();
                defectAttachement.setLastUpdateDate(new Timestamp(new Date().getTime()));
                defectAttachement.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                defectAttachement.setId(ids[i]);
                attachementMapper.removeAttachement(defectAttachement);
                list.add(defectAttachement);

                // ?????????????????????????????????
                defectAttachement = attachementMapper.getAttById(ids[i]);
                TblDefectLogAttachement logAttachement = new TblDefectLogAttachement();
                logAttachement = (TblDefectLogAttachement) CommonUtil.setBaseValue(logAttachement, request);
                logAttachement.setDefectLogId(logId);
                logAttachement.setFileNameOld(defectAttachement.getFileNameOld());
                logAttachement.setFilePath(defectAttachement.getFilePath());
                logAttachement.setFileS3Bucket(defectAttachement.getFileS3Bucket());
                logAttachement.setFileS3Key(defectAttachement.getFileS3Key());
                logAttachement.setFileType(defectAttachement.getFileType());
                logAttachement.setStatus(2);
                logAttachementMapper.insertSelective(logAttachement);
                logList.add(logAttachement);
            }

            syscMap.put("defectAttachement", list);
            syscMap.put("logAttachement", logList);

            String url = devTestUrl + "devManage/defect/syncDefectAtt";

            // ???????????????????????????????????????????????????????????????????????????
            this.syncOpt(url, "remove", syscMap);
        }

    }

    /**
     * ???????????????????????????????????????
     * ??????????????????????????????
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @Override
    public void export(JqGridPage<DefectInfoVo> page, DefectInfoVo defectInfoVo, HttpServletResponse response, HttpServletRequest request) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        String[] title = {"????????????", "????????????", "????????????", "????????????", "????????????", "????????????",
                "????????????", "????????????", "????????????", "????????????", "?????????", "????????????", "????????????", "????????????",
                "??????????????????", "??????????????????", "??????????????????"};
        String sheetName = "????????????";
        String filename = "????????????" + DateUtil.getDateString(new Timestamp(new Date().getTime()), "yyyyMMddHHmmss") + ".xlsx";

        Font font = workbook.createFont();
        font.setFontName("????????????"); //????????????
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //??????
        font.setItalic(false); //??????????????????
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//????????????

        CellStyle headStyle = workbook.createCellStyle();
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // ????????????
        headStyle.setFont(font);

        CellStyle valueStyle = workbook.createCellStyle();

        DefectInfoVo defectInfoForExport = page.filtersToEntityField(defectInfoVo);
        Boolean flag = new CommonUtils().currentUserWithAdmin(request);
        if (flag == false) {
            Map<String, Object> map = new HashMap<>();
            map.put("currentUserId", CommonUtil.getCurrentUserId(request));
            // ????????????????????????????????????ID
            List<TblSystemInfo> systemInfoList = systemInfoMapper.findSystemWithProjectByUserId(map);
            defectInfoForExport.setSystemInfoList(systemInfoList);
        }
        defectInfoForExport.setPage(null);
        defectInfoForExport.setRows(null);
        List<TblDefectInfo> list = defectInfoMapper.findDefectList(defectInfoForExport);
        String[][] value = new String[0][15];
        if (list != null && list.size() > 0) {
            value = new String[list.size()][];
            for (int i = 0, len = list.size(); i < len; i++) {
                value[i] = new String[title.length];
                value[i][0] = list.get(i).getDefectCode();
                value[i][1] = list.get(i).getDefectSummary();
                value[i][2] = list.get(i).getDefectStatus() != null ? getDicValue(DicConstants.TEST_DEFECT_STATUS, list.get(i).getDefectStatus()) : "";
                value[i][3] = list.get(i).getDefectSource() != null ? getDicValue(DicConstants.TEST_DEFECT_SOURCE, list.get(i).getDefectSource()) : "";
                value[i][4] = list.get(i).getSeverityLevel() != null ? getDicValue(DicConstants.TEST_SEVERITY_LEVEL, list.get(i).getSeverityLevel()) : "";
                value[i][5] = list.get(i).getEmergencyLevel() != null ? getDicValue(DicConstants.TEST_EMERGENCY_LEVEL, list.get(i).getEmergencyLevel()) : "";
                value[i][6] = list.get(i).getDefectType() != null ? getDicValue(DicConstants.TEST_DEFECT_TYPE, list.get(i).getDefectType()) : "";
                value[i][7] = list.get(i).getRepairRound() != null ? list.get(i).getRepairRound().toString() : "";
                value[i][8] = list.get(i).getWindowName();
                value[i][9] = list.get(i).getAssignUserName();
                value[i][10] = list.get(i).getSubmitUserName();
                value[i][11] = DateUtil.getDateString(list.get(i).getSubmitDate(), "yyyy???MM???dd???");
                value[i][12] = list.get(i).getSystemName();
                value[i][13] = list.get(i).getRequirementCode();
                TblDevTask devTask = devTaskMapper.getDevTaskById(list.get(i).getDevTaskId());
                value[i][14] = devTask != null ? devTask.getDevTaskName() : "";
                String featureName = featureMapper.getFeatureName(list.get(i).getRequirementFeatureId());
                value[i][15] = featureName != null ? featureName : "";
                List<TblRequirementFeature> featureList = featureMapper.getFeatureByREQCode(list.get(i).getRequirementCode());
                value[i][16] = featureList != null && featureList.size() > 0 ? featureList.get(0).getFeatureName() : "";
            }
        }
        ExcelUtil.export(title, sheetName, filename, value, workbook, 0, headStyle, valueStyle, request, response);
    }
    
    /**
    *@author liushan
    *@Description ??????????????????
    *@Date 2020/7/31
     * @param dicKey  ????????????code??????
     * @param dicTableKey key
    *@return java.lang.String
    **/
    private String getDicValue(String dicKey, Integer dicTableKey) {
        Map<String, Object> dic = getDicKey(dicKey);
        return (String) dic.get(dicTableKey.toString());
    }

    /**
    * @author author
    * @Description ???????????????????????????
    * @Date 2020/9/18
    * @param systemInfo ????????????
    * @param pageNumber ??????
    * @param pageSize ??????
    * @param request
    * @return java.util.Map<java.lang.String,java.lang.Object>
    *  result.put("rows", list); ?????????????????????
    *  result.put("total", total); ?????????
    **/
    @Override
    public Map<String, Object> getAllSystemInfo(TblSystemInfo systemInfo, Integer pageNumber, Integer pageSize, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        map = PageWithBootStrap.setPageNumberSize(map, pageNumber, pageSize);
        map.put("systemInfo", systemInfo);
        map.put("currentUserId", CommonUtil.getCurrentUserId(request));
        List<TblSystemInfo> list = systemInfoMapper.findSystemWithProjectByUserId(map);
        int total = systemInfoMapper.CountFindSystemWithProjectByUserId(map);

        result.put("rows", list);
        result.put("total", total);
        return result;
    }

    /**
    *@author liushan
    *@Description ???????????????????????????
    *@Date 2020/7/31
    * @param requirementInfo ??????????????????
    * @param pageNumber
    * @param pageSize
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementInfo>
    **/
    public List<TblRequirementInfo> getAllRequirement(TblRequirementInfo requirementInfo, Integer pageNumber, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map = PageWithBootStrap.setPageNumberSize(map, pageNumber, pageSize);
        map.put("req", requirementInfo);
        return defectInfoMapper.getAllRequirement(map);
    }

    /**
     * ?????????????????????????????????
     *
     * @param window  ??????????????????
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public Map<String, Object> getAllComWindow(TblCommissioningWindow window, Integer pageNumber, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map = PageWithBootStrap.setPageNumberSize(map, pageNumber, pageSize);
        map.put("win", window);
        List<TblCommissioningWindow> list = windowMapper.getAllComWindow(map);
        int total = windowMapper.getAllComWindowTotal(map);

        Map<String, Object> result = new HashMap<>();
        result.put("rows", list);
        result.put("total", total);
        return result;
    }


    /**
     * ?????????????????? ????????????
     * ????????????
     *
     * @param oldObject
     * @param newObject
     * @param logId
     */
    @Transactional(readOnly = false)
    public void updateFieledsReflect(Object oldObject, Object newObject, Map<String, Object> remarkMap, Long logId) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
        Class<?> oldDemo = null;
        Class<?> newDemo = null;
        List<Map<String, Object>> list = new ArrayList<>();
        if (!remarkMap.isEmpty()) {
            list.add(remarkMap);
        }
        oldDemo = Class.forName(oldObject.getClass().getName());
        newDemo = Class.forName(newObject.getClass().getName());

        Field[] oldFields = oldDemo.getDeclaredFields();
        for (Field oldField : oldFields) {

            oldField.setAccessible(true);
            String oldDclareName = oldField.getName();
            Object oldDeclareValue = oldField.get(oldObject);
            PropertyInfo propertyInfo = oldField.getAnnotation(PropertyInfo.class);
            if (propertyInfo == null) {
                continue;
            }
            Field newField = newDemo.getDeclaredField(oldDclareName);
            newField.setAccessible(true);
            Object newDeclareValue = newField.get(newObject);

            // ????????????
            if (oldDclareName.equals("commissioningWindowId") && oldDeclareValue != null) {
                oldDeclareValue = windowMapper.getWindowName((Long) oldDeclareValue);
            }
            if (oldDclareName.equals("commissioningWindowId") && newDeclareValue != null) {
                newDeclareValue = windowMapper.getWindowName((Long) newDeclareValue);
            }

           /* // ????????????
            if (oldDclareName.equals("systemId") && oldDeclareValue != null){
                oldDeclareValue = systemInfoMapper.getSystemNameById((Long)oldDeclareValue);
            }
            if (oldDclareName.equals("systemId") && newDeclareValue != null){
                newDeclareValue = systemInfoMapper.getSystemNameById((Long)newDeclareValue);
            }*/

            // ??????????????????

            //????????? ?????????
            if (oldDclareName.equals("submitUserId") || oldDclareName.equals("assignUserId") && oldDeclareValue != null) {
                oldDeclareValue = defectInfoMapper.getUserNameById((Long) oldDeclareValue);
            }
            if (oldDclareName.equals("submitUserId") || oldDclareName.equals("assignUserId") && newDeclareValue != null) {
                newDeclareValue = defectInfoMapper.getUserNameById((Long) newDeclareValue);
            }

            String[] fieldTypeName = newField.getType().getName().split("\\.");
            if (!ReflectFieledType.listType.contains(fieldTypeName[fieldTypeName.length - 1])) {
                updateFieledsReflect(oldDeclareValue, newDeclareValue, remarkMap, logId);
                continue;
            }

            if (oldDeclareValue == null || newDeclareValue == null) {
                if (oldDeclareValue == null && newDeclareValue == null) {
                    continue;
                } else {
                    if (oldDeclareValue == null) {
                        if (oldField.getType().equals(Date.class)) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            newDeclareValue = simpleDateFormat.format(newDeclareValue);
                        }
                        // ??????????????????
                        Map<String, Object> newMap = new HashMap<>();
                        newMap.put("fieldName", propertyInfo.name());
                        newMap.put("oldValue", "&nbsp;&nbsp;");
                        newMap.put("newValue", newDeclareValue.toString().equals("") ? "&nbsp;&nbsp;" : newDeclareValue.toString());
                        list.add(newMap);
                    } else {
                        // ??????????????????
                        if (oldField.getType().equals(Timestamp.class)) {
                            continue;
                        } else {
                            if (oldField.getType().equals(Date.class)) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                oldDeclareValue = simpleDateFormat.format(oldDeclareValue);
                            }
                            Map<String, Object> delMap = new HashMap<>();
                            delMap.put("fieldName", propertyInfo.name());
                            delMap.put("oldValue", oldDeclareValue.toString());
                            delMap.put("newValue", "?????????");
                            list.add(delMap);
                        }

                    }
                    continue;
                }
            }
            // ??????????????????
            if (!oldDeclareValue.toString().equals(newDeclareValue.toString())) {
                if (oldField.getType().equals(Timestamp.class)) {
                    continue;
                } else {
                    if (oldField.getType().equals(Date.class)) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        oldDeclareValue = simpleDateFormat.format(oldDeclareValue);
                    }
                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("fieldName", propertyInfo.name());
                    updateMap.put("oldValue", oldDeclareValue.toString().equals("") ? "&nbsp;&nbsp;" : oldDeclareValue.toString());
                    updateMap.put("newValue", newDeclareValue.toString().equals("") ? "&nbsp;&nbsp;" : newDeclareValue.toString());
                    list.add(updateMap);
                }
            }

        }
        Gson gson = new Gson();
        String logDetail = gson.toJson(list);
        TblDefectLog log = new TblDefectLog();
        log.setLogDetail(logDetail);
        log.setId(logId);
        // ??????????????????
        defectLogMapper.updateLogById(log);
    }

    /**
    *@author liushan
    *@Description ???????????????????????????
    *@Date 2020/7/31
     * @param defectLog
 * @param request
    *@return cn.pioneeruniverse.dev.entity.TblDefectLog
    **/
    private TblDefectLog setDefectLog(TblDefectLog defectLog, HttpServletRequest request) {
        defectLog.setUserId(CommonUtil.getCurrentUserId(request));
        defectLog.setUserName(CommonUtil.getCurrentUserName(request));
        defectLog.setUserAccount(CommonUtil.getCurrentUserAccount(request));
        defectLog.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
        defectLog.setLastUpdateDate(new Timestamp(new Date().getTime()));
        return defectLog;
    }
    
    /**
    *@author liushan
    *@Description ??????????????????????????????
    *@Date 2020/7/31
     * @param 
    *@return java.lang.String
    **/
    private String getDefectCode() {
        String featureCode = "";
        Integer codeInt = 0;
        Object object = redisUtils.get("BUG-");
        if (object != null && !"".equals(object)) {
            // redis????????????redis??????
            String code = object.toString();
            if (!StringUtils.isBlank(code)) {
                codeInt = Integer.parseInt(code) + 1;
            }
        } else {
            // redis????????????????????????????????????????????????
            String maxDefectNo = this.selectMaxDefectCode();
            codeInt = 1;
            if (StringUtils.isNotBlank(maxDefectNo)) {
                codeInt = Integer.valueOf(maxDefectNo.substring(Constants.TMP_TEST_DEFECT_CODE.length())) + 1;
            }

        }
        featureCode = Constants.TMP_TEST_DEFECT_CODE + String.format("%08d", codeInt);
        redisUtils.set("BUG-", String.format("%08d", codeInt));
        return featureCode;
    }

    /**
    *@author liushan
    *@Description ???????????????????????????
    *@Date 2020/7/31
     * @param 
    *@return java.lang.String
    **/
    private String selectMaxDefectCode() {
        return defectInfoMapper.selectMaxDefectCode();
    }


    /**
    *@author liushan
    *@Description ????????????
    *@Date 2020/7/31
     * @param e
 * @param message
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    public Map<String, Object> handleException(Exception e, String message) {
        logger.error(message + ":" + e.getMessage(), e);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", Constants.ITMP_RETURN_FAILURE);
        map.put("errorMessage", message);
        return map;
    }


    /**
    *@author liushan
    *@Description ??????????????????
    *@Date 2020/7/31
    * @param url ???????????????
    * @param opt ??????????????????
    * @param object ????????????
    *@return void
    **/
    private void syncOpt(String url, String opt, Object object) throws Exception {
        Gson gson = new Gson();
        Map<String, Object> syncMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        syncMap.put("opt", opt);
        syncMap.put("value", gson.toJson(object));
        String json = gson.toJson(syncMap);
        logger.info("itmp.test.url:" + url);
        logger.info("integration:" + integration);
        // integration ????????????httpUtil,???????????????????????????
        if (integration == false) {
            String result = HttpUtil.doPost(url, json, 6000, 6000, null);
            resultMap = JSON.parseObject(result);
        } else if (integration == true) {
            String urlOpt = url.substring(url.lastIndexOf("/") + 1);
            //????????????
            if (urlOpt.equals("syncDefect")) {
                resultMap = toTestManageInterface.syncDefect(json);
            } else if (urlOpt.equals("syncDefectAtt")) {//????????????
                resultMap = toTestManageInterface.syncDefectAtt(json);
            }
        }

        if (resultMap.get("status") != null && resultMap.get("status").equals("2")) {
            logger.error((String) resultMap.get("errorMessage"));
            throw new Exception((String) resultMap.get("errorMessage"));
        }
    }

    /**
    *@author liushan
    *@Description ????????????
    *@Date 2020/7/31
    * @param defectId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectRemark>
    **/
    @Override
    @Transactional(readOnly = true)
    public List<TblDefectRemark> getRemarkByDefectId(Long defectId) {
        // TODO Auto-generated method stub
        List<TblDefectRemark> list = defectRemarkMapper.getRemarkByDefectId(defectId);
        return list;
    }

    /**
    *@author liushan
    *@Description ??????????????????
    *@Date 2020/7/31
    * @param defectId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDefectRemarkAttachement>
    **/
    @Override
    @Transactional(readOnly = true)
    public List<TblDefectRemarkAttachement> getRemarkAttsByDefectId(Long defectId) {
        // TODO Auto-generated method stub
        return remarkAttachementMapper.getRemarkAttsByDefectId(defectId);
    }

    /**
    *@author liushan
    *@Description ????????????  ???????????? ??????????????????????????????
    *@Date 2020/7/31
    * @param tblDefectRemark ????????????
    * @param files ??????
    * @param request
    *@return void
    **/
    @Override
    @DataSource(name = "itmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addDefectRemark(TblDefectRemark tblDefectRemark, List<TblDefectRemarkAttachement> files,
                                HttpServletRequest request) {
        // TODO Auto-generated method stub
        tblDefectRemark.setCreateBy(CommonUtil.getCurrentUserId(request));
        tblDefectRemark.setUserId(CommonUtil.getCurrentUserId(request));
        tblDefectRemark.setUserName(CommonUtil.getCurrentUserName(request));
        tblDefectRemark.setUserAccount(CommonUtil.getCurrentUserAccount(request));
        tblDefectRemark.setCreateDate(DateUtil.getCurrentTimestamp());
        tblDefectRemark.setStatus(1);
        defectRemarkMapper.insertDefectRemark(tblDefectRemark);
        Long id = tblDefectRemark.getId();
        if (null != files && files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                files.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
                files.get(i).setDefectRemarkId(id);
                files.get(i).setCreateDate(DateUtil.getCurrentTimestamp());
            }
            remarkAttachementMapper.addRemarkAttachement(files);
        }

        // ?????????????????????
        SpringContextHolder.getBean(DefectService.class).addTmpDefectRemark(tblDefectRemark, files, request);
    }

    /**
    *@author liushan
    *@Description ????????????????????? ????????????
    *@Date 2020/7/31
    * @param tblDefectRemark
    * @param files
    * @param request
    *@return void
    **/
    @Override
    @DataSource(name = "tmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void addTmpDefectRemark(TblDefectRemark tblDefectRemark, List<TblDefectRemarkAttachement> files,
                                   HttpServletRequest request) {
        // TODO Auto-generated method stub
        tblDefectRemark.setCreateBy(CommonUtil.getCurrentUserId(request));
        tblDefectRemark.setUserId(CommonUtil.getCurrentUserId(request));
        tblDefectRemark.setUserName(CommonUtil.getCurrentUserName(request));
        tblDefectRemark.setUserAccount(CommonUtil.getCurrentUserAccount(request));
        tblDefectRemark.setCreateDate(DateUtil.getCurrentTimestamp());
        tblDefectRemark.setStatus(1);
        defectRemarkMapper.insertDefectRemark(tblDefectRemark);
        Long id = tblDefectRemark.getId();
        if (null != files && files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                files.get(i).setCreateBy(CommonUtil.getCurrentUserId(request));
                files.get(i).setDefectRemarkId(id);
                files.get(i).setCreateDate(DateUtil.getCurrentTimestamp());
            }
            remarkAttachementMapper.addRemarkAttachement(files);
        }
    }

    /**
    *@author liushan
    *@Description ?????????????????????????????? ??????
    *@Date 2020/7/31
    * @param id ??????id
    * @param commissioningWindowId  ????????????id
    *@return void
    **/
    @Override
    @DataSource(name = "itmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateCommssioningWindowId(Long id,Long commissioningWindowId) {
        defectInfoMapper.updateCommssioningWindowId1(id,commissioningWindowId);
        SpringContextHolder.getBean(DefectService.class).updateTmpCommssioningWindowId(id, commissioningWindowId);
    }

    /**
     *@author liushan
     *@Description ?????????????????????????????? ??????
     *@Date 2020/7/31
     * @param id ??????id
     * @param commissioningWindowId  ????????????id
     *@return void
     **/
    @Override
    @DataSource(name = "tmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void updateTmpCommssioningWindowId(Long id,Long commissioningWindowId) {
        defectInfoMapper.updateCommssioningWindowId1(id,commissioningWindowId);
    }

    /**
    *@author liushan
    *@Description ?????????????????????
    *@Date 2020/7/31
    * @param id
    *@return void
    **/
    @Override
    @DataSource(name = "itmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void closeDefect(Long id) {
        defectInfoMapper.closeDefect(id);
        SpringContextHolder.getBean(DefectService.class).closeTmpDefect(id);
    }

    /**
     *@author liushan
     *@Description ?????????????????????
     *@Date 2020/7/31
     * @param id
     *@return void
     **/
    @Override
    @DataSource(name = "tmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void closeTmpDefect(Long id) {
        defectInfoMapper.closeDefect(id);
    }

    /**
     * @param tblDefectInfo
     * @return void
     * @Description ???????????????????????????????????????
     * @MethodName sendMessageForDefectStatusUpdate
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/9/29 14:43
     */
    private void sendMessageForDefectStatusUpdate(TblDefectInfo tblDefectInfo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("messageTitle", "???????????????????????????");
        map.put("messageContent", tblDefectInfo.getDefectCode() + "|" + tblDefectInfo.getDefectSummary());
        map.put("messageReceiverScope", 2);
        map.put("messageReceiver", tblDefectInfo.getAssignUserId());
        Map<String, String> result = devManageToSystemInterface.insertMessage(JSON.toJSONString(map));
        if (result != null && !result.isEmpty()) {
            if (StringUtils.equals(result.get("status").toString(), Constants.ITMP_RETURN_FAILURE)) {
                throw new Exception(result.get("errorMessage").toString());
            }
        }
    }


    /**
     * ???????????????????????????????????????(?????????2 || 10)
     * @param tblDefectInfo
     * @param projectId
     * @throws Exception
     */
    private void sendMessage(TblDefectInfo tblDefectInfo, Long projectId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("messageTitle", "???????????????????????????");
        map.put("messageContent", tblDefectInfo.getDefectCode() + "|" + tblDefectInfo.getDefectSummary());
        map.put("messageReceiverScope", 3);
        map.put("messageReceiver", tblDefectInfo.getAssignUserId());
        map.put("projectId",projectId);
        Map<String, String> result = devManageToSystemInterface.insertMessage(JSON.toJSONString(map));
        if (result != null && !result.isEmpty()) {
            if (StringUtils.equals(result.get("status").toString(), Constants.ITMP_RETURN_FAILURE)) {
                throw new Exception(result.get("errorMessage").toString());
            }
        }
    }

}
