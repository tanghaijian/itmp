package cn.pioneeruniverse.dev.service.monthlyReport.impl;

import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.*;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.*;
import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.entity.monthlyReport.*;
import cn.pioneeruniverse.dev.feignInterface.TestManageToSystemInterface;
import cn.pioneeruniverse.dev.service.monthlyReport.*;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MonthlyReportServiceImpl implements MonthlyReportService {

    @Autowired
    private TblRequirementFeatureMapper requirementFeatureMapper;
    @Autowired
    private TblDefectInfoMapper defectInfoMapper;
    @Autowired
    private TblReportMonthlySummaryMapper tblReportMonthlySummaryMapper;
    @Autowired
    private TblReportMonthlySystemMapper tblReportMonthlySystemMapper;
    @Autowired
    private TblReportMonthlyBaseMapper tblReportMonthlyBaseMapper;
    @Autowired
    private TblReportMonthlySystemDataMapper tblReportMonthlySystemDataMapper;
    @Autowired
    private TblReportMonthlyDefectLevelMapper tblReportMonthlyDefectLevelMapper;
    @Autowired
    private TblReportMonthlyTaskTypeMapper tblReportMonthlyTaskTypeMapper;
    @Autowired
    private TblReportMonthlySystemTypeDataMapper tblReportMonthlySystemTypeDataMapper;
    @Autowired
    private TblReportCumulativeSystemDataMapper tblReportCumulativeSystemDataMapper;
    @Autowired
    private TblReportMonthlyModuleMapper tblReportMonthlyModuleMapper;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    private TblReportMonthlyConfigMapper tblReportMonthlyConfigMapper;
    @Autowired
    private TblSystemInfoMapper tblSystemInfoMapper;
    @Autowired
    private TestManageToSystemInterface testManageToSystemInterface;
    @Autowired
    private TblReportCumulativeSystemDataService tblReportCumulativeSystemDataService;
    @Autowired
    private TblReportMonthlySystemTypeDataService tblReportMonthlySystemTypeDataService;
    @Autowired
    private TblReportMonthlyTaskTypeService tblReportMonthlyTaskTypeService;
    @Autowired
    private TblReportMonthlyDefectLevelService tblReportMonthlyDefectLevelService;
    @Autowired
    private TblReportMonthlyModuleService tblReportMonthlyModuleService;
    @Autowired
    private TblReportMonthlyConfigService tblReportMonthlyConfigService;

    /**
     * 月报实时数据查询
     *
     * @param time
     * @return map
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> queryReport(String time, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        //开始时间
        String startDate = DateUtil.formatDate(DateUtil.addMonth(DateUtil.parseDate(time + "-26"), -1));
        //结束时间
        String endDate = time + "-25";
        //根据时间查找月报基本信息表
        List<TblReportMonthlyBase> tblReportMonthlyBaseList = tblReportMonthlyBaseMapper.findByYearMonth(time);
        //判断当前用户是不是预报审核人或者是管理员
        List<TblReportMonthlyConfig> tblReportMonthlyConfig = tblReportMonthlyConfigMapper.findByUserIdByUserType(CommonUtil.getCurrentUserId(request), 1);
        LinkedHashMap map2 = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
        List<String> roleCodes = (List<String>) map2.get("roles");
        //实时数据
        List<TblReportMonthlySystemData> defectSystemList = null;
        if ((roleCodes != null && roleCodes.contains("XITONGGUANLIYUAN")) || (tblReportMonthlyConfig != null && tblReportMonthlyConfig.size() > 0)) {//当前登录用户有角色是系统管理员
            defectSystemList = defectInfoMapper.selectMonthlyDefectProBySystem(startDate, endDate, -1, "", null);
        } else {
            defectSystemList = defectInfoMapper.selectMonthlyDefectProBySystem(startDate, endDate, -1, "", CommonUtil.getCurrentUserId(request));
        }
        //版本等信息<实时数据>
        List<TblReportMonthlyBase> devVersionReportList = requirementFeatureMapper.selectDevMonthlyVertionReport(startDate, endDate, "");
        //总览数据
        if (tblReportMonthlyBaseList != null && tblReportMonthlyBaseList.size() == 0) {
            //各个系统任务数 缺陷数等信息
            int repairedDefectNumber = 0;
            int repairRound = 0;
            int defectNumber = 0;
            for (TblReportMonthlySystemData tblReportMonthlySystem : defectSystemList) {
                repairRound = repairRound + tblReportMonthlySystem.getTotalRepairRound();
                repairedDefectNumber = repairedDefectNumber + tblReportMonthlySystem.getRepairedDefectNumber();
                defectNumber =defectNumber + tblReportMonthlySystem.getDefectNumber();
                tblReportMonthlySystem.setYearMonth(time);
            }
            devVersionReportList.get(0).setRepairRound(repairRound);
            devVersionReportList.get(0).setRepairedDefectNumber(repairedDefectNumber);
            devVersionReportList.get(0).setYearMonth(time);
            devVersionReportList.get(0).setStatus(1);
            CommonUtil.setBaseValue(devVersionReportList.get(0), request);
            devVersionReportList.get(0).setAuditStatus(0);
            if (repairedDefectNumber !=0){
                double avgRepairRound =Double.valueOf(new DecimalFormat("0.00").format((double)repairRound/repairedDefectNumber));
                //设置平均修复轮次
                devVersionReportList.get(0).setAvgRepairRound(avgRepairRound);
            }else {
                devVersionReportList.get(0).setAvgRepairRound(0.0);
            }
            devVersionReportList.get(0).setChangePercent(0.0);
            devVersionReportList.get(0).setDefectNumber(defectNumber);
            tblReportMonthlyBaseMapper.insertMonthBase(devVersionReportList.get(0));
            TblReportMonthlyConfig manager = tblReportMonthlyConfigService.getReportMonthlyManager();
            if (manager !=null){
                devVersionReportList.get(0).setUserName(manager.getUserName());
            }
            map.put("tblReportMonthlyBaseList", devVersionReportList);
        } else {
            assert tblReportMonthlyBaseList != null;
            devVersionReportList.get(0).setPlanWindowsNumber(tblReportMonthlyBaseList.get(0).getPlanWindowsNumber());
            devVersionReportList.get(0).setTempWindowsNumber(tblReportMonthlyBaseList.get(0).getTempWindowsNumber());
            devVersionReportList.get(0).setTempAddTaskNumber(tblReportMonthlyBaseList.get(0).getTempAddTaskNumber());
            devVersionReportList.get(0).setTempDelTaskNumber(tblReportMonthlyBaseList.get(0).getTempDelTaskNumber());
            devVersionReportList.get(0).setYearMonth(time);
            devVersionReportList.get(0).setId(tblReportMonthlyBaseList.get(0).getId());
            devVersionReportList.get(0).setAuditStatus(tblReportMonthlyBaseList.get(0).getAuditStatus());
            devVersionReportList.get(0).setUserName(tblReportMonthlyBaseList.get(0).getUserName());
            int repairedDefectNumber = 0;
            int repairRound = 0;
            int defectNumber =0;
            for (TblReportMonthlySystemData tblReportMonthlySystem : defectSystemList) {
                repairRound = repairRound + tblReportMonthlySystem.getTotalRepairRound();
                repairedDefectNumber = repairedDefectNumber + tblReportMonthlySystem.getRepairedDefectNumber();
                defectNumber =defectNumber + tblReportMonthlySystem.getDefectNumber();
                tblReportMonthlySystem.setYearMonth(time);
            }
            devVersionReportList.get(0).setRepairRound(repairRound);
            if (repairedDefectNumber !=0){
                double avgRepairRound =Double.valueOf(new DecimalFormat("##0.00").format((double)repairRound/repairedDefectNumber));
                //设置平均修复轮次
                devVersionReportList.get(0).setAvgRepairRound(avgRepairRound);
            }else {
                devVersionReportList.get(0).setAvgRepairRound(0.0);
            }
            //设置变更率
            if (devVersionReportList.get(0).getTotalTaskNumber() !=null && devVersionReportList.get(0).getTotalTaskNumber() !=0){
                double changePercent = ((double)tblReportMonthlyBaseList.get(0).getTempAddTaskNumber() +tblReportMonthlyBaseList.get(0).getTempDelTaskNumber())/devVersionReportList.get(0).getTotalTaskNumber() *100;
                devVersionReportList.get(0).setChangePercent(Double.valueOf(new DecimalFormat("##0").format(changePercent)));
            }
            devVersionReportList.get(0).setDefectNumber(defectNumber);
            devVersionReportList.get(0).setRepairRound(repairRound);
            devVersionReportList.get(0).setRepairedDefectNumber(repairedDefectNumber);
            map.put("tblReportMonthlyBaseList", devVersionReportList);
        }

        //根据时间查找月报系统数据表
        List<TblReportMonthlySystemData> tblReportMonthlySystemDataList = tblReportMonthlySystemDataMapper.findByYearMonth(time);
        //各个子系统数据
        if (tblReportMonthlySystemDataList != null && tblReportMonthlySystemDataList.size() == 0) {
            for (TblReportMonthlySystemData tblReportMonthlySystemData : defectSystemList) {
                CommonUtil.setBaseValue(tblReportMonthlySystemData, request);
                tblReportMonthlySystemData.preInsertOrUpdate(request);
                tblReportMonthlySystemData.setTaskNumber(0);
                tblReportMonthlySystemData.setType(0);
                tblReportMonthlySystemData.setAuditStatus(0);
                tblReportMonthlySystemData.setDefectNumber(0);
                tblReportMonthlySystemData.setDefectPercent(0.0);
                tblReportMonthlySystemData.setRepairedDefectNumber(0);
                tblReportMonthlySystemData.setUnrepairedDefectNumber(0);
                tblReportMonthlySystemData.setDesignCaseNumber(0);
                tblReportMonthlySystemData.setTotalRepairRound(0);
                tblReportMonthlySystemData.setAvgRepairRound(0.0);
                tblReportMonthlySystemData.setYearMonth(time);
                tblReportMonthlySystemDataMapper.insertAllColumn(tblReportMonthlySystemData);
            }
        } else {
            for (TblReportMonthlySystemData tblReportMonthlySystemData : defectSystemList) {
                assert tblReportMonthlySystemDataList != null;
                for (TblReportMonthlySystemData reportMonthlySystemData : tblReportMonthlySystemDataList) {
                    if (tblReportMonthlySystemData.getSystemId().equals(reportMonthlySystemData.getSystemId())) {
                        tblReportMonthlySystemData.setLastmonthUndefectedNumber(reportMonthlySystemData.getLastmonthUndefectedNumber());
                        tblReportMonthlySystemData.setLastmonthUndefectedBelonger(reportMonthlySystemData.getLastmonthUndefectedBelonger());
                        tblReportMonthlySystemData.setAuditStatus(reportMonthlySystemData.getAuditStatus());
                    }
                }
            }
        }
        //模板
        tblReportMonthlyModuleService.updateOrInsertModule(time, request);
        //开始时间
        String endTime = DateUtil.formatDate(DateUtil.addMonth(DateUtil.parseDate(time + "-26"), +1));
        map.put("updateStatus", belongCalendar(endDate, endTime));
        map.put("tblReportMonthlySystemDataList", defectSystemList);
        return map;
    }

    /**
     * 汇总
     * @param time
     * @param request
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> summaryMonthlyReport(String time, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        //开始时间
        String startDate = DateUtil.formatDate(DateUtil.addMonth(DateUtil.parseDate(time + "-26"), -1));
        //结束时间
        String endDate = time + "-25";
        //版本等信息
        List<TblReportMonthlyBase> devVersionReportList = requirementFeatureMapper.selectDevMonthlyVertionReport(startDate,
                endDate, "");
        //各个系统任务数 缺陷数等信息
        List<TblReportMonthlySystemData> defectSystemList = defectInfoMapper.selectMonthlyDefectProBySystem(startDate, endDate, -1, "", null);
        int repairedDefectNumber = 0;
        int repairRound = 0;
        for (TblReportMonthlySystemData tblReportMonthlySystem : defectSystemList) {
            repairRound = repairRound + tblReportMonthlySystem.getTotalRepairRound();
            repairedDefectNumber = repairedDefectNumber + tblReportMonthlySystem.getRepairedDefectNumber();
            tblReportMonthlySystem.setYearMonth(time);
        }
        devVersionReportList.get(0).setRepairRound(repairRound);
        devVersionReportList.get(0).setRepairedDefectNumber(repairedDefectNumber);
        devVersionReportList.get(0).setYearMonth(time);
        //根据时间查找月报基本信息表
        List<TblReportMonthlyBase> tblReportMonthlyBaseList = tblReportMonthlyBaseMapper.findByYearMonth(time);
        //根据时间查找月报系统数据表
        List<TblReportMonthlySystemData> tblReportMonthlySystemDataList = tblReportMonthlySystemDataMapper.findByYearMonth(time);
        //更新或者插入基本信息
        this.updateOrInsertMonthlyBase(tblReportMonthlyBaseList, devVersionReportList, request);
        //更新或者插入子系统信息
        this.updateOrInsertMonthlySystemData(tblReportMonthlySystemDataList, defectSystemList, request);
        //更新或插入模板信息
        //this.updateOrInsertModule(time,request);
        tblReportMonthlyModuleService.updateOrInsertModule(time, request);
        map.put("status", 1);
        return map;
    }

    /**
     * 判断是否有历史记录
     * @param time
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> queryHis(String time, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Integer count1 = tblReportMonthlyBaseMapper.jugeHis(time);
        Integer count2 = tblReportMonthlySystemDataMapper.jugeHis(time);
        if (count1 == 0 && count2 == 0) {
            map.put("flag", true);
        } else {
            map.put("flag", false);
        }
        return map;
    }

    /**
     * 添加历史
     * @param allDevVersionTableData
     * @param allSystemTableData
     * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public void addHis(String allDevVersionTableData, String allSystemTableData, HttpServletRequest request) {

        List<TblReportMonthlyBase> newTblReportMonthlyBaseList = JSONArray.parseArray(allDevVersionTableData, TblReportMonthlyBase.class);
        List<TblReportMonthlySystemData> newTblReportMonthlySystemDataList = JSONArray.parseArray(allSystemTableData, TblReportMonthlySystemData.class);
        String time = newTblReportMonthlyBaseList.get(0).getYearMonth();

        // 保存月报基本数据
        if (CollectionUtil.isNotEmpty(newTblReportMonthlyBaseList)) {
            TblReportMonthlyBase base = newTblReportMonthlyBaseList.get(0);
            base.setLastUpdateDate(new Timestamp(new Date().getTime()));
            base.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
            if (base.getTotalTaskNumber()!=null && base.getTotalTaskNumber() !=0){
                double changePercent = (((double)base.getTempAddTaskNumber()+base.getTempDelTaskNumber()) /base.getTotalTaskNumber())*100;
                base.setChangePercent(changePercent);
            }
            tblReportMonthlyBaseMapper.updateByYearMonth(base);
        }
        //月报系统数据

        for (TblReportMonthlySystemData tblReportMonthlySystemData : newTblReportMonthlySystemDataList) {
            tblReportMonthlySystemData.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
            tblReportMonthlySystemData.setLastUpdateDate(new Timestamp(new Date().getTime()));
            TblReportMonthlySystemData systemData = tblReportMonthlySystemDataMapper.findByYearMonthAndSystemId(time,tblReportMonthlySystemData.getSystemId());
            if (systemData == null){
                tblReportMonthlySystemData.setType(0);
                tblReportMonthlySystemDataMapper.insert(tblReportMonthlySystemData);
            }else {
                tblReportMonthlySystemData.setType(systemData.getType());
                tblReportMonthlySystemDataMapper.updateByYearMothAndSystemId(tblReportMonthlySystemData);
            }
        }

        //月报缺陷等级
        tblReportMonthlyDefectLevelService.updateOrInsertDefectLevel(time, request);
        //月报任务类型等级
        tblReportMonthlyTaskTypeService.updateOrInsertTaskType(time, request);
        //各个系统数据
        tblReportMonthlySystemTypeDataService.updateOrInsertSystemTypeData(time, request);
        //全年累计数据
        tblReportCumulativeSystemDataService.updateOrInsertCumulativeSystemTypeData(time, request);
    }

    /**
     * 查询历史记录
     * @param time
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> queryHisByTime(String time, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<TblReportMonthlyBase> tblReportMonthlyBaseList = tblReportMonthlyBaseMapper.findByYearMonth(time);
        //判断当前用户是不是预报审核人或者是管理员
        List<TblReportMonthlyConfig> tblReportMonthlyConfig = tblReportMonthlyConfigMapper.findByUserIdByUserType(CommonUtil.getCurrentUserId(request), 1);
        LinkedHashMap map2 = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
        List<String> roleCodes = (List<String>) map2.get("roles");
        List<TblReportMonthlySystemData> tblReportMonthlySystemDataList = null;
        if ((roleCodes != null && roleCodes.contains("XITONGGUANLIYUAN")) || (tblReportMonthlyConfig != null && tblReportMonthlyConfig.size() > 0)) {//当前登录用户有角色是系统管理员
            tblReportMonthlySystemDataList = tblReportMonthlySystemDataMapper.findByYearMonth(time);
        } else {
            tblReportMonthlySystemDataList = tblReportMonthlySystemDataMapper.findByYearMonthAndUserId(time, CommonUtil.getCurrentUserId(request));
        }

        map.put("tblReportMonthlyBaseList", tblReportMonthlyBaseList);
        map.put("tblReportMonthlySystemDataList", tblReportMonthlySystemDataList);
        //用于判断是否可以修改
        String endTime = DateUtil.formatDate(DateUtil.addMonth(DateUtil.parseDate(time + "-26"), +1));
        map.put("updateStatus", belongCalendar(time + "-26", endTime));
        return map;
    }

    @Override
    @Transactional(readOnly = false)
    public void testReport(String time, HttpServletRequest request) {
    }

    /**
     * 获取月报中的所有数据
     * @param time
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> getAllMonthReportData(String time, HttpServletRequest request) {
        HashMap<String, Object> allData = new HashMap<String, Object>();

        List<Data> listOne = new ArrayList<>();
        List<Data> listBase = new ArrayList<>();
        List<Data> listSystem = new ArrayList<>();
        List<Data> listAgile = new ArrayList<>();
        List<Data> listOperation = new ArrayList<>();
        List<Data> listNew = new ArrayList<>();
        List<Data> listDefectLevel = new ArrayList<>();


        //获取基础数据
        listSystem = this.getMonthBaseData(time);

        Date date = DateUtil.parseDate(time,"yyyy-MM");
        //创建Calendar实例
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);   //设置月报年月时间


        Data year = new Data("year", "数据年份", String.valueOf(cal.get(Calendar.YEAR)));
        Data month = new Data("month", "数据月份", String.valueOf(cal.get(Calendar.MONTH)+1));
        cal.add(Calendar.MONTH, 1);  //在月报年月时间基础上加一月得到报告年月时间

        Data reportYear = new Data("reportYear", "报告年份", String.valueOf(cal.get(Calendar.YEAR)));
        Data reportMonth = new Data("reportMonth", "报告月份", String.valueOf(cal.get(Calendar.MONTH)+1));

        listBase.add(year);
        listBase.add(month);
        listBase.add(reportYear);
        listBase.add(reportMonth);

        //获取系统分类数据
        Map<String, List<Data>> systemTypeDataMap = this.getMonthSystemTypeData(time);
        //敏捷类项目
        listAgile = systemTypeDataMap.get("agile");
        //运维期项目
        listOperation = systemTypeDataMap.get("operation");
        //项目期项目
        listNew = systemTypeDataMap.get("new");


        //获取月报系统数据
        Data avgRepairRoundMoreThanOne = this.getMonthSystemData(time);
        listSystem.add(avgRepairRoundMoreThanOne);


        //缺陷数据
        listDefectLevel = this.getMonthDefectLevel(time);


        Map<String, List> cumulativeSystemDataMap = this.getCumulativeSystemData(time);
        List<Data> cumulativeAgileList = cumulativeSystemDataMap.get("listAgile");
        if (cumulativeAgileList != null) {
            listAgile.addAll(cumulativeAgileList);
        }
        List<Data> cumulativeOperation = cumulativeSystemDataMap.get("listOperation");
        if (cumulativeOperation != null) {
            listOperation.addAll(cumulativeOperation);
        }


        Data mapBase = new Data("listBase", "基本数据类型", null, listBase);
        listOne.add(mapBase);

        Data mapSystem = new Data("listSystem", "系统数据类型", null, listSystem);
        listOne.add(mapSystem);

        Data mapAgile = new Data("listAgile", "敏捷类系统", null, listAgile);
        listOne.add(mapAgile);

        Data mapOperation = new Data("listOperation", "运维期系统", null, listOperation);

        listOne.add(mapOperation);

        Data mapNew = new Data("listNew", "项目期系统", null, listNew);
        listOne.add(mapNew);

        Data mapDefectLevel = new Data("listDefectLevel", "缺陷等级", null, listDefectLevel);
        listOne.add(mapDefectLevel);

        Map<String, Data> mapAll = listOne.stream().flatMap(m -> m.getList().stream()).collect(Collectors.toMap(Data::getCode, d -> d));


        //获取module数据
        Map<String, List<TblReportMonthlyModule>> modules = this.getMoudleTypeData(time, mapAll);
        Map<String, Object> pageData = this.getTableData(time);

        allData.put("pageData", pageData);
        allData.put("data", listOne);
        allData.put("module", modules);
        return allData;
    }

    /**
     * 获取缺陷清单
     * @param time
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> getRemainDefect(String time, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String oamSystem = tblSystemInfoMapper.selectSystemByClass(2);
        List<Map<String, Object>> remainDefectList = defectInfoMapper.selectRemainDefect("2010-01-01", time + "-25", oamSystem);
        //缺陷来源
        List<TblDataDic> defectSourceMap = testManageToSystemInterface.getDataDicByTermCode("TBL_DEFECT_INFO_DEFECT_SOURCE");
        //缺陷级别
        List<TblDataDic> defectLevelMap = testManageToSystemInterface.getDataDicByTermCode("TBL_DEFECT_INFO_SEVERITY_LEVEL");
        //缺陷状态
        List<TblDataDic> defectStatusMap = testManageToSystemInterface.getDataDicByTermCode("TBL_DEFECT_INFO_DEFECT_STATUS");
        for (Map<String, Object> remainDefect : remainDefectList) {
            //缺陷来源
            String sourceCode = remainDefect.get("defectSource").toString();
            String sourceName = defectSourceMap.stream().filter(e -> e.getValueCode().equals(sourceCode)).findFirst().orElse(new TblDataDic()).getValueName();
            remainDefect.put("defectSource", sourceCode + "-" + sourceName);
            //缺陷级别
            String levelCode = remainDefect.get("severityLevel").toString();
            String levelName = defectLevelMap.stream().filter(e -> e.getValueCode().equals(levelCode)).findFirst().orElse(new TblDataDic()).getValueName();
            remainDefect.put("severityLevel", levelCode + "-" + levelName);
            //缺陷状态
            String statusCode = remainDefect.get("defectStatus").toString();
            String statusName = defectStatusMap.stream().filter(e -> e.getValueCode().equals(statusCode)).findFirst().orElse(new TblDataDic()).getValueName();
            remainDefect.put("defectStatus", statusCode + "-" + statusName);
            //developUserId
            if (!remainDefect.containsKey("developUserId")) {
                remainDefect.put("developUserId", "");
            }
        }
        result.put("data", remainDefectList);
        return result;
    }


    private List<Data> getMonthBaseData(String date) {
        List<Data> listSystem = new ArrayList<>();
        //TblReportMonthlyBase base = new TblReportMonthlyBase();//基表，列表（检出率）
        List<TblReportMonthlyBase> baseList = tblReportMonthlyBaseMapper.findByYearMonth(date);
        assert baseList != null && baseList.size() > 0;
        TblReportMonthlyBase base = baseList.get(0);
        Data planWindowsNumber = new Data("planWindowsNumber", "计划内版本次数", base.getPlanWindowsNumber()==null ? 0:base.getPlanWindowsNumber());
        Data tempWindowsNumber = new Data("tempWindowsNumber", "临时版本次数", base.getTempWindowsNumber()==null ? 0 :base.getTempWindowsNumber());
        Data allWindowsNumber = new Data("allWindowsNumber", "总版本数", base.getPlanWindowsNumber() + base.getTempWindowsNumber());
        Data tempAddTaskNumber = new Data("tempAddTaskNumber", "临时增加任务数", base.getTempAddTaskNumber()==null ?0: base.getTempAddTaskNumber());
        Data tempDelTaskNumber = new Data("tempDelTaskNumber", "临时删除任务数", base.getTempDelTaskNumber()==null ?0 :base.getTempDelTaskNumber());
        Data changePercent = new Data("changePercent", "版本变更率", base.getChangePercent());
        Data risk = new Data("risk", "软件测试风险", base.getChangePercent() > 20 ? "高" : base.getChangePercent() > 10 ? "中" : "低");
        Data avgRepairRound = new Data("avgRepairRound", "平均缺陷修复轮次", base.getAvgRepairRound() ==null ?0 : base.getAvgRepairRound());
        Data undetectedNumber = new Data("undetectedNumber", "漏检数", base.getUndetectedNumber()==null ?0 : base.getUndetectedNumber());
        Data detectedRate = new Data("detectedRate", "检出率", base.getDetectedRate()==null? 100:base.getDetectedRate() );

        listSystem.add(planWindowsNumber);
        listSystem.add(tempWindowsNumber);
        listSystem.add(allWindowsNumber);
        listSystem.add(tempAddTaskNumber);
        listSystem.add(tempDelTaskNumber);
        listSystem.add(changePercent);
        listSystem.add(risk);
        listSystem.add(avgRepairRound);
        listSystem.add(undetectedNumber);
        listSystem.add(detectedRate);
        return listSystem;
    }

    private Map<String, List<Data>> getMonthSystemTypeData(String date) {
        Map<String, List<Data>> systemTypeDataMap = new HashMap<>();
        List<TblReportMonthlySystemTypeData> systemTypeDataList = tblReportMonthlySystemTypeDataMapper.findByYearTime(date);
        int code = 0;
        String name = null;
        String mapType = null;
        for (TblReportMonthlySystemTypeData systemTypeData : systemTypeDataList) {
            List<Data> systemTypeList = new ArrayList<>();
            if (systemTypeData.getSystemType() == 1) {
                code = 1;
                name = "敏捷类";
                mapType = "agile";
            } else if (systemTypeData.getSystemType() == 2) {
                code = 2;
                name = "运维期";
                mapType = "operation";
            } else {
                code = 3;
                name = "项目期";
                mapType = "new";
            }
            Data systemNum1 = new Data("systemNum" + code, name + "系统数", systemTypeData.getSystemNum()==null?0:systemTypeData.getSystemNum());
            Data totalTaskNumber1 = new Data("totalTaskNumber" + code, name + "系统测试任务总数", systemTypeData.getTotalTaskNumber()==null ? 0: systemTypeData.getTotalTaskNumber());

            Data designCaseNumber1 = new Data("designCaseNumber" + code, name + "系统设计用例数", systemTypeData.getDesignCaseNumber() == null ?0: systemTypeData.getDesignCaseNumber());
            Data defectNumber1 = new Data("defectNumber" + code, name + "系统缺陷数", systemTypeData.getDefectNumber() == null ? 0:systemTypeData.getDefectNumber());
            Double defectPercentNum1 = systemTypeData.getDefectPercent();
            Data defectPercent1 = new Data("defectPercent" + code, name + "系统缺陷率", defectPercentNum1);
            Data quality1 = new Data("quality" + code, name + "系统开发质量", defectPercentNum1 > 4 ? "较低" : defectPercentNum1 > 2 ? "一般" : "较高");

            systemTypeList.add(systemNum1);
            systemTypeList.add(totalTaskNumber1);
            systemTypeList.add(designCaseNumber1);
            systemTypeList.add(defectNumber1);
            systemTypeList.add(defectPercent1);
            systemTypeList.add(quality1);
            systemTypeDataMap.put(mapType, systemTypeList);
        }
        return systemTypeDataMap;

    }

    private Data getMonthSystemData(String time) {
        List<TblReportMonthlySystemData> systemDatas = tblReportMonthlySystemDataMapper.findByYearMonth(time);
        List<String> top = new ArrayList<>();
        for (TblReportMonthlySystemData d : systemDatas) {
            if (d.getAvgRepairRound() > 1) {
                if (d.getSystemType() == 1) {
                    top.add(d.getSystemName() + "(敏捷)");
                } else {
                    top.add(d.getSystemName());
                }

            }
        }
        String tops = String.join("、", top);//平均修复轮次大于1系统
        Data avgRepairRoundMoreThanOne = new Data("avgRepairRoundMoreThanOne", "平均修复轮次大于1系统", tops);
        return avgRepairRoundMoreThanOne;
    }

    private List<Data> getMonthDefectLevel(String time) {
        List<Data> listDefectLevel = new ArrayList<>();
        //TODO 排序得出主要缺陷
        // 所有系统 systemType=0,level=4
        TblReportMonthlyDefectLevel defectLevel0_4 = tblReportMonthlyDefectLevelMapper.findDefectLevelByYearMonth(time, 0, 4);
        //一般性缺陷(主要缺陷)
        if (defectLevel0_4 != null) {
            Data defectNumber0_4 = new Data("defectNumber0_4", "一般性缺陷数", defectLevel0_4.getDefectNumber());//一般性缺陷数
            Data percentage0_4 = new Data("percentage0_4", "一般性缺陷占比", defectLevel0_4.getPercentage());//一般性缺陷占比
            listDefectLevel.add(defectNumber0_4);
            listDefectLevel.add(percentage0_4);
        }

        //所有系统 systemType=0,level=5
        TblReportMonthlyDefectLevel defectLevel0_5 = tblReportMonthlyDefectLevelMapper.findDefectLevelByYearMonth(time, 0, 5);
        if (defectLevel0_5 != null) {
            Data defectNumber0_5 = new Data("defectNumber0_5", "严重缺陷数", defectLevel0_5.getDefectNumber());//严重缺陷数
            Data percentage0_5 = new Data("percentage0_5", "严重缺陷占比", defectLevel0_5.getPercentage());//严重缺陷占比
            listDefectLevel.add(defectNumber0_5);
            listDefectLevel.add(percentage0_5);
        }

        List<TblReportMonthlyDefectLevel> defectLevels = tblReportMonthlyDefectLevelMapper.findDefectLevelByLevel(time,  5);

        TblReportMonthlyDefectLevel defectLevelMax_5 = defectLevels.stream().filter(x -> x.getSystemType()!=0)
                .sorted(Comparator.comparing(TblReportMonthlyDefectLevel::getDefectNumber).reversed())
                .findFirst()
                .get();
        if(defectLevelMax_5 != null){
            String systemName = defectLevelMax_5.getSystemType() == 1 ? "敏捷类" : defectLevelMax_5.getSystemType() == 2 ? "运维期" : "项目期";

            Data defectLevelMaxSystemName = new Data("defectLevelMaxSystemName", "严重缺陷占比最大系统", systemName);//严重缺陷占比最大系统

            Data defectLevelMaxSystemPercentage = new Data("defectLevelMaxSystemPercentage", "严重缺陷占比最大系统缺陷数", defectLevelMax_5.getDefectNumber());//严重缺陷占比最大系统缺陷数

            listDefectLevel.add(defectLevelMaxSystemName);
            listDefectLevel.add(defectLevelMaxSystemPercentage);
        }
        return listDefectLevel;
    }

    private Map<String, List> getCumulativeSystemData(String time) {
        Map<String, List> systemDataMap = new HashMap<>();
        List<Data> listAgile = new ArrayList<>();
        List<Data> listOperation = new ArrayList();

        //项目
        List<TblReportCumulativeSystemData> cumulativeSystemDatas = tblReportCumulativeSystemDataMapper.findCumulativeSystemDataByYearMonth(time);

        List<TblReportCumulativeSystemData> cumulativeSystemDatas2 = new ArrayList<TblReportCumulativeSystemData>();//项目

        Integer cumulativeDesignCaseNumberNum = 0;
        Integer cumulativeDefectNumberNum = 0;

        for (TblReportCumulativeSystemData cumulativeSystemData : cumulativeSystemDatas) {
            if (cumulativeSystemData.getSystemType() == 1) {//敏捷
                cumulativeDesignCaseNumberNum += cumulativeSystemData.getDesignCaseNumber();
                cumulativeDefectNumberNum += cumulativeSystemData.getDefectNumber();
            } else if (cumulativeSystemData.getSystemType() == 2) {//运维
                if (cumulativeSystemData.getTaskNumber() > 5) {
                    cumulativeSystemDatas2.add(cumulativeSystemData);
                }
            }

        }
        double cumulativeDefectPercentNum = cumulativeDesignCaseNumberNum == 0 ? 0 : (cumulativeDefectNumberNum * 1.0 / cumulativeDesignCaseNumberNum);
        //敏捷类
        Data cumulativeDesignCaseNumber = new Data("cumulativeDesignCaseNumber", "全年累计设计用例数", cumulativeDesignCaseNumberNum);
        Data cumulativeDefectNumber = new Data("cumulativeDefectNumber", "全年累计缺陷数", cumulativeDefectNumberNum);
        Data cumulativeDefectPercent = new Data("cumulativeDefectPercent", "全年平均缺陷率", cumulativeDefectPercentNum);
        Data cumulativeQuality = new Data("cumulativeQuality", "全年平均开发质量", cumulativeDefectPercentNum > 4 ? "较低" : cumulativeDefectPercentNum > 2 ? "一般" : "较高");


        listAgile.add(cumulativeDesignCaseNumber);
        listAgile.add(cumulativeDefectNumber);
        listAgile.add(cumulativeDefectPercent);
        listAgile.add(cumulativeQuality);

        systemDataMap.put("listAgile", listAgile);

        List<TblReportCumulativeSystemData> cumulativeSortedSystemDatas2 = cumulativeSystemDatas2.stream().sorted((a, b) -> a.getDefectPercent().compareTo(b.getDefectPercent())).collect(Collectors.toList());

        //全年累计缺陷率最低的系统排名
        String cumulativeSystemTop = "";
        String cumulativeSystemTail = "";


        //全年累计缺陷率最高的系统排名
        for (int i = 0; i < cumulativeSortedSystemDatas2.size(); i++) {
            if (i < 2) {
                cumulativeSystemTop += (cumulativeSortedSystemDatas2.get(i).getSystemName() + "(缺陷率" + cumulativeSortedSystemDatas2.get(i).getDefectPercent() + "%)、");

            } else if (i == 2) {
                cumulativeSystemTop += (cumulativeSortedSystemDatas2.get(i).getSystemName() + "(缺陷率" + cumulativeSortedSystemDatas2.get(i).getDefectPercent() + "%)");

            }
            if (i == cumulativeSortedSystemDatas2.size() - 1) {
                cumulativeSystemTail += (cumulativeSortedSystemDatas2.get(i).getSystemName() + "(缺陷率" + cumulativeSortedSystemDatas2.get(i).getDefectPercent() + "%)、");
            } else if (i > cumulativeSortedSystemDatas2.size() - 4) {
                cumulativeSystemTail += (cumulativeSortedSystemDatas2.get(i).getSystemName() + "(缺陷率" + cumulativeSortedSystemDatas2.get(i).getDefectPercent() + "%)、");

            }
        }
        Data cumulativeSortedSystemDatasTop = new Data("cumulativeSortedSystemDatasTop", "运维期系统全年累计缺陷率最低的系统排名", cumulativeSystemTop);
        Data cumulativeSortedSystemDatasLail = new Data("cumulativeSortedSystemDatasLail", "运维期系统全年累计缺陷率最高的系统排名", cumulativeSystemTail);


        listOperation.add(cumulativeSortedSystemDatasTop);
        listOperation.add(cumulativeSortedSystemDatasLail);
        systemDataMap.put("listOperation", listOperation);

        return systemDataMap;
    }

    private Map<String, List<TblReportMonthlyModule>> getMoudleTypeData(String time, Map<String, Data> mapAll) {
        List<TblReportMonthlyModule> modules = tblReportMonthlyModuleMapper.findByDate(time);
        Map<String, List<TblReportMonthlyModule>> moduleMap = new HashMap();
        modules.forEach(x -> {
            //delModule(x, mapAll);
            String key = "module_" + x.getPage() + "_" + x.getArea();
            List<TblReportMonthlyModule> modulePageAreas = moduleMap.computeIfAbsent(key, k -> new ArrayList<>());
            modulePageAreas.add(x);
        });
        return moduleMap;
    }


    private Map<String, Object> getTableData(String time) {
        Map<String, Object> data = new HashMap<>();
        //当前年
        String year = DateUtil.formatDate(DateUtil.parseDate(time, "yyyy-MM"), "yyyy");
        //当前月
        String month = DateUtil.formatDate(DateUtil.parseDate(time, "yyyy-MM"), "MM");
        //本年第一天
        String firstDay = year + "-01";
        String nowMonth = getLastDayOfMonth1(Integer.parseInt(year), Integer.parseInt(month));

        //本年月报第一个月
        String startYearMonth = year + "-01";
        String[] times = new String[]{"2020-01","2020-02","2020-03","2020-04","2020-05","2020-06","2020-07","2020-08","2020-09","2020-10","2020-11","2020-12"};


        //page 3 运维期系统-非敏捷 测试缺陷率最低
        List<TblReportMonthlySystemData> devDefectRateIsTheLowestList = tblReportMonthlySystemDataMapper.findDefectRateSortByTimeAndTypeASC(time, 2, 8);
        data.put("page3_1", devDefectRateIsTheLowestList);
        //page 3 运维期系统-非敏捷 缺陷率最高
        List<TblReportMonthlySystemData> devDefectRateIsTheHighList = tblReportMonthlySystemDataMapper.findDefectRateSortByTimeAndTypeDESC(time, 2, 3);
        data.put("page3_2", devDefectRateIsTheHighList);
        //page 3 敏捷类系统 最低 2个
        List<TblReportMonthlySystemData> agileDefectRateIsTheLowestList = tblReportMonthlySystemDataMapper.findDefectRateSortByTimeAndTypeASC(time, 1, 2);
        //page 3 敏捷类系统 最高 3个
        List<TblReportMonthlySystemData> agileDefectRateIsTheHighList = tblReportMonthlySystemDataMapper.findDefectRateSortByTimeAndTypeDESC(time, 1, 3);
        Map<String,Object> agileMap = new HashMap<>();
        agileMap.put("green",agileDefectRateIsTheLowestList);
        agileMap.put("red",agileDefectRateIsTheHighList);
        data.put("page3_3", agileMap);
        //项目期系统-非敏捷
        List<TblReportMonthlySystemData> proDefectRateIsTheLowestList = tblReportMonthlySystemDataMapper.findDefectRateSortByTimeAndTypeASC(time, 3, 1);
        List<TblReportMonthlySystemData> proDefectRateIsTheHighList = tblReportMonthlySystemDataMapper.findDefectRateSortByTimeAndTypeDESC(time, 3, 1);
        Map<String,Object> proMap = new HashMap<>();
        proMap.put("green",proDefectRateIsTheLowestList);
        proMap.put("red",proDefectRateIsTheHighList);
        data.put("page3_4", proMap);

        //page 5 版本次数
        List<TblReportMonthlyBase> versionsNumberList = tblReportMonthlyBaseMapper.findVersionsNumberByYear(startYearMonth, time);
        for(int i = versionsNumberList.size(); i < times.length; i++){//增加未来年月空数据
            TblReportMonthlyBase reportMonthlyBase = new TblReportMonthlyBase();
            reportMonthlyBase.setYearMonth(times[i]);
            versionsNumberList.add(reportMonthlyBase);
        }

        data.put("page5", versionsNumberList);
        //page 6 版本变更率
        List<TblReportMonthlyBase> versionsChangeRateList = tblReportMonthlyBaseMapper.findVersionsChangeRateByYear(startYearMonth, time);
        data.put("page6", versionsChangeRateList);
        //page 7 敏捷项目
        List<TblReportMonthlySystemData> agileSystemDataList = tblReportMonthlySystemDataMapper.findTaskTypeByDate(time, 1);
        data.put("page7_1", agileSystemDataList);
        //page 7 敏捷业务需求和缺陷占比
        List<TblReportMonthlyTaskType> agileTaskTypeList = tblReportMonthlyTaskTypeMapper.findAgileTaskRateByDate(time, 1);
        data.put("page7_2", agileTaskTypeList);
        //page 8  运维期系统个各种各类型综合
        List<TblReportMonthlySystemTypeData> devList = tblReportMonthlySystemTypeDataMapper.findByYearAndSystemType( firstDay, nowMonth,2);
        data.put("page8_1", devList);
        //page 8 本月运维期所占百分比
        List<TblReportMonthlyTaskType> devTaskRateList = tblReportMonthlyTaskTypeMapper.findDevTaskRateByDate(time);
        List<TblReportMonthlyTaskType> devTaskList = this.devTaskRate(devTaskRateList);
        if (devTaskList != null && devTaskList.size() > 0) {
            devTaskRateList.addAll(devTaskList);
        }
        data.put("page8_2", devTaskRateList);
        //page 9 项目期项目
        List<TblReportMonthlySystemData> proSystemDataList = tblReportMonthlySystemDataMapper.findTaskTypeByDate(time, 3);
        data.put("page9_1", proSystemDataList);
        List<TblReportMonthlyTaskType> proTaskTypeList = tblReportMonthlyTaskTypeMapper.findAgileTaskRateByDate(time, 3);
        data.put("page9_2", proTaskTypeList);
        //page 10 修复轮次趋势图
        List<TblReportMonthlySystemData> avgRepairRoundList = tblReportMonthlySystemDataMapper.findAvgRepaitRound(startYearMonth, time);
        data.put("page10_1", avgRepairRoundList);
        //page 10 平均修复轮次大于1的系统
        List<TblReportMonthlySystemData> avgDataList = tblReportMonthlySystemDataMapper.findAvgRepaitRoundByTime(time);
        data.put("page10_2", avgDataList);
        //page 11 获取本月3个系统类型的总缺陷数
        List<TblReportMonthlyDefectLevel> defectLevelSystemTypeList = tblReportMonthlyDefectLevelMapper.findCountByByYearMonthAndSystemType(time);
        List<TblReportMonthlyDefectLevel> defectLevelList = this.fillDefectLevelData(defectLevelSystemTypeList);
        if (defectLevelList != null && defectLevelList.size() > 0) {
            defectLevelSystemTypeList.addAll(defectLevelList);
        }
        data.put("page11_1", defectLevelSystemTypeList);
        //page 11 获取本月各种缺陷等级的各个缺陷总数
        List<TblReportMonthlyDefectLevel> defectLevelTypeList = tblReportMonthlyDefectLevelMapper.findCountByByYearMonthAndLevel(time);
        //fillDefectLevelTypeData
        List<TblReportMonthlyDefectLevel> defectLevelType = this.fillDefectLevelTypeData(defectLevelTypeList);
        if (defectLevelType != null && defectLevelType.size() > 0) {
            defectLevelTypeList.addAll(defectLevelType);
        }
        data.put("page11_2", defectLevelTypeList);

        //page 12 漏检情况
        List<TblReportMonthlyBase> undetectedNumberList = tblReportMonthlyBaseMapper.findUndetectedNumberByYear(startYearMonth, time);
        data.put("page12", undetectedNumberList);
        //page 14 本月测试缺陷率统计（敏捷类系统）
        List<TblReportMonthlySystemData> agileDefectPercentList = tblReportMonthlySystemDataMapper.findDefectPercentByDateAndType(time, 1);
        data.put("page14", agileDefectPercentList);
        //page 15 本月测试缺陷率统计（运维期系统非敏捷）
        List<TblReportMonthlySystemData> devDefectPercentList = tblReportMonthlySystemDataMapper.findDevDefectPercentByDate(time);
        data.put("page15", devDefectPercentList);
        //page 17 全年测试缺陷率统计（敏捷类系统）
        List<TblReportCumulativeSystemData>  agileDefectPercent =  tblReportCumulativeSystemDataMapper.findByYearMonthAndSystemType(time,1);
        data.put("page17", agileDefectPercent);
        //page 18 全年测试缺陷率统计（运维期系统（非敏捷））
        List<TblReportCumulativeSystemData>  devDefectPercent =  tblReportCumulativeSystemDataMapper.findByYearMonthAndSystemType(time,2);
        data.put("page18", devDefectPercent);
        return data;
    }


    private List<TblReportMonthlyDefectLevel> fillDefectLevelData(List<TblReportMonthlyDefectLevel> defectLevelSystemTypeList) {
        List<TblReportMonthlyDefectLevel> defectLevelList = new ArrayList<>();
        long agile = defectLevelSystemTypeList.stream().filter(e -> e.getSystemType() == 1).count();
        long dev = defectLevelSystemTypeList.stream().filter(e -> e.getSystemType() == 2).count();
        long pro = defectLevelSystemTypeList.stream().filter(e -> e.getSystemType() == 3).count();
        if (agile == 0) {
            TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel = new TblReportMonthlyDefectLevel();
            tblReportMonthlyDefectLevel.setSystemType(1);
            tblReportMonthlyDefectLevel.setDefectNumber(0);
            defectLevelList.add(tblReportMonthlyDefectLevel);
        }
        if (dev == 0) {
            TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel = new TblReportMonthlyDefectLevel();
            tblReportMonthlyDefectLevel.setSystemType(2);
            tblReportMonthlyDefectLevel.setDefectNumber(0);
            defectLevelList.add(tblReportMonthlyDefectLevel);
        }
        if (pro == 0) {
            TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel = new TblReportMonthlyDefectLevel();
            tblReportMonthlyDefectLevel.setSystemType(3);
            tblReportMonthlyDefectLevel.setDefectNumber(0);
            defectLevelList.add(tblReportMonthlyDefectLevel);
        }
        return defectLevelList;
    }


    //defectLevelTypeList
    private List<TblReportMonthlyDefectLevel> fillDefectLevelTypeData(List<TblReportMonthlyDefectLevel> defectLevelTypeList) {
        List<TblReportMonthlyDefectLevel> defectLevelList = new ArrayList<>();
        long type1 = defectLevelTypeList.stream().filter(e -> e.getLevel() == 1).count();
        long type2 = defectLevelTypeList.stream().filter(e -> e.getLevel() == 2).count();
        long type3 = defectLevelTypeList.stream().filter(e -> e.getLevel() == 3).count();
        long type4 = defectLevelTypeList.stream().filter(e -> e.getLevel() == 4).count();
        long type5 = defectLevelTypeList.stream().filter(e -> e.getLevel() == 5).count();
        if (type1 == 0) {
            TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel = new TblReportMonthlyDefectLevel();
            tblReportMonthlyDefectLevel.setLevel(1);
            tblReportMonthlyDefectLevel.setDefectNumber(0);
            defectLevelList.add(tblReportMonthlyDefectLevel);
        }
        if (type2 == 0) {
            TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel = new TblReportMonthlyDefectLevel();
            tblReportMonthlyDefectLevel.setLevel(2);
            tblReportMonthlyDefectLevel.setDefectNumber(0);
            defectLevelList.add(tblReportMonthlyDefectLevel);
        }
        if (type3 == 0) {
            TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel = new TblReportMonthlyDefectLevel();
            tblReportMonthlyDefectLevel.setLevel(3);
            tblReportMonthlyDefectLevel.setDefectNumber(0);
            defectLevelList.add(tblReportMonthlyDefectLevel);
        }
        if (type4 == 0) {
            TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel = new TblReportMonthlyDefectLevel();
            tblReportMonthlyDefectLevel.setLevel(4);
            tblReportMonthlyDefectLevel.setDefectNumber(0);
            defectLevelList.add(tblReportMonthlyDefectLevel);
        }
        if (type5 == 0) {
            TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel = new TblReportMonthlyDefectLevel();
            tblReportMonthlyDefectLevel.setLevel(5);
            tblReportMonthlyDefectLevel.setDefectNumber(0);
            defectLevelList.add(tblReportMonthlyDefectLevel);
        }
        return defectLevelList;

    }


    private List<TblReportMonthlyTaskType> devTaskRate(List<TblReportMonthlyTaskType> devTaskRateList) {
        List<TblReportMonthlyTaskType> devlist = new ArrayList<>();
        long type1 = devTaskRateList.stream().filter(e -> e.getType() == 1).count();
        long type2 = devTaskRateList.stream().filter(e -> e.getType() == 2).count();
        if (type1 == 0) {
            TblReportMonthlyTaskType tblReportMonthlyTaskType = new TblReportMonthlyTaskType();
            tblReportMonthlyTaskType.setType(1);
            tblReportMonthlyTaskType.setTaskNumber(0);
            tblReportMonthlyTaskType.setPercentage(0.0);
            devlist.add(tblReportMonthlyTaskType);
        }
        if (type2 == 0) {
            TblReportMonthlyTaskType tblReportMonthlyTaskType = new TblReportMonthlyTaskType();
            tblReportMonthlyTaskType.setType(2);
            tblReportMonthlyTaskType.setTaskNumber(0);
            tblReportMonthlyTaskType.setPercentage(0.0);
            devlist.add(tblReportMonthlyTaskType);
        }
        return devlist;
    }

    private void updateOrInsertMonthlyBase(List<TblReportMonthlyBase> oldMonthlyBaseList, List<TblReportMonthlyBase> newMonthlyBaseList, HttpServletRequest request) {
        TblReportMonthlyBase newTblReportMonthlyBase = newMonthlyBaseList.get(0);
        newTblReportMonthlyBase.setAuditStatus(0);
        newTblReportMonthlyBase.preInsertOrUpdate(request);
        if (newTblReportMonthlyBase.getRepairedDefectNumber() != null && newTblReportMonthlyBase.getRepairedDefectNumber() != 0) {
            double avgRepairRound =(double) newTblReportMonthlyBase.getRepairRound() / newTblReportMonthlyBase.getRepairedDefectNumber();
            //平均修复轮次
            newTblReportMonthlyBase.setAvgRepairRound(avgRepairRound);
        } else {
            //平均修复轮次
            newTblReportMonthlyBase.setAvgRepairRound(0.0);
        }
        if (oldMonthlyBaseList != null && oldMonthlyBaseList.size() > 0 && newMonthlyBaseList.size() > 0) {
            TblReportMonthlyBase oldTblReportMonthlyBase = oldMonthlyBaseList.get(0);
            try {
                BeanUtils.copyProperties(oldTblReportMonthlyBase, newTblReportMonthlyBase);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tblReportMonthlyBaseMapper.updateByYearMonth(oldTblReportMonthlyBase);
        } else {
            //月报状态
            newTblReportMonthlyBase.setBaseStatus(0);
            tblReportMonthlyBaseMapper.batchInsert(newTblReportMonthlyBase);
        }
    }

    private void updateOrInsertMonthlySystemData(List<TblReportMonthlySystemData> oldTblReportMonthlySystemDataList, List<TblReportMonthlySystemData> newTblReportMonthlySystemDataList, HttpServletRequest request) {
        for (TblReportMonthlySystemData tblReportMonthlySystemData : newTblReportMonthlySystemDataList) {
            tblReportMonthlySystemData.setType(0);
            tblReportMonthlySystemData.preInsertOrUpdate(request);
            if (oldTblReportMonthlySystemDataList != null && oldTblReportMonthlySystemDataList.size() > 0 && newTblReportMonthlySystemDataList.size() > 0) {
                tblReportMonthlySystemDataMapper.updateByYearMothAndSystemId(tblReportMonthlySystemData);
            } else {
                tblReportMonthlySystemDataMapper.insertAllColumn(tblReportMonthlySystemData);
            }
        }

    }

//    public void delModule(TblReportMonthlyModule module, Map<String, Data> map) {
//
//        module.setContentValue(delModule(module.getContent(), map));
//
//    }

//    public String delModule(String module, Map<String, Data> map) {
//        int begin = module.indexOf("{{");
//        if (begin > 0) {
//            int end = module.indexOf("}}");
//            if (end > 0) {
//                String key = module.substring(begin + 2, end);
//                String value = map.get(key).getValue().toString();
//                module = module.replace("{{" + key + "}}", value);
//                return delModule(module, map);
//            } else {
//                return module;
//            }
//
//        } else {
//            return module;
//        }
//    }

    public static String getLastDayOfMonth1(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }


    public static boolean belongCalendar(String begin1, String end1) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        Date now = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse(begin1 + " 00:00:00");
            endTime = df.parse(end1 + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar date = Calendar.getInstance();
        date.setTime(now);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


}
