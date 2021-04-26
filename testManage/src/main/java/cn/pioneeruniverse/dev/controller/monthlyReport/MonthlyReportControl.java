package cn.pioneeruniverse.dev.controller.monthlyReport;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.entity.monthlyReport.*;
import cn.pioneeruniverse.dev.service.monthlyReport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("monthlyReport")
public class MonthlyReportControl extends BaseController {

    @Autowired
    private MonthlyReportService monthlyReport;
    @Autowired
    private TblReportMonthlyBaseService tblReportMonthlyBaseService;
    @Autowired
    private TblReportMonthlySystemDataService tblReportMonthlySystemDataService;
    @Autowired
    private TblReportMonthlyConfigService tblReportMonthlyConfigService;
    @Autowired
    private TblReportMonthlyModuleService tblReportMonthlyMoudleService;
    @Autowired
    private TblReportMonthlyUndetectedSummaryService tblReportMonthlyUndetectedSummaryService;
    @Autowired
    private TblReportCumulativeSystemDataService tblReportCumulativeSystemDataService;


    /**
     * 查询月报报表
     * @param time
     * @return
     */
    @RequestMapping("queryReport")
    public Map<String, Object> queryReport(String time,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.putAll(monthlyReport.queryReport(time,request));
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }


    /**
     *月报汇总
     * @param time
     * @return
     */
    @RequestMapping("summaryMonthlyReport")
    public Map<String,Object> summaryMonthlyReport(String time,HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        try {
            monthlyReport.summaryMonthlyReport(time,request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        }catch (Exception e){
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }



    /**
     * 查询是否有历史记录
     * @param time
     * @return
     */
    @RequestMapping("queryHis")
    public Map<String, Object> queryHis(String time,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.putAll(monthlyReport.queryHis(time,request));
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 插入历史记录
     * @param allDevVersionTableData
     * @param allSystemTableData
     * @param request
     * @return
     */
    @RequestMapping("insertHis")
    public Map<String, Object> insertHis(String allDevVersionTableData, String allSystemTableData, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            monthlyReport.addHis(allDevVersionTableData, allSystemTableData, request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 查询历史记录
     * @param time
     * @return
     */
    @RequestMapping("queryHisByTime")
    public Map<String, Object> queryHisByTime(String time,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.putAll(monthlyReport.queryHisByTime(time, request));
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }
    @RequestMapping("testReport")
    public Map<String, Object> testReport(String time,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            monthlyReport.testReport(time,request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 获取所有的月报信息
     * @param time
     * @param request
     * @return
     */
    @RequestMapping("getAllMonthReportData")
    public Map<String, Object> getAllMonthReportData(String time,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.putAll(monthlyReport.getAllMonthReportData(time,request));
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 更新基本信息
     * @param tblReportMonthlyBase
     * @param request
     * @return
     */
    @RequestMapping("updateMonthlyBaseData")
    public Map<String,Object> updateMonthlyBaseData(TblReportMonthlyBase tblReportMonthlyBase,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            tblReportMonthlyBaseService.updateMonthlyBase(tblReportMonthlyBase,request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 更新各个系统漏捡数和上月漏检归属信息
     * @param tblReportMonthlySystemData
     * @param request
     * @return
     */
    @RequestMapping("updateMonthlySystemData")
    public Map<String,Object> updateMonthlySystemData(TblReportMonthlySystemData tblReportMonthlySystemData, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            tblReportMonthlySystemDataService.updateMonthlySystemData(tblReportMonthlySystemData,request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 配置负责人
     * @param tblReportMonthlyConfig
     * @param request
     * @return
     */
    @RequestMapping("addMonthlyConfig")
    public Map<String,Object> addMonthlyConfig(TblReportMonthlyConfig tblReportMonthlyConfig, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            tblReportMonthlyConfigService.updateMonthlyConfig(tblReportMonthlyConfig,request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 发起评审
     * @param tblReportMonthlyBase
     * @param request
     * @return
     */
    @RequestMapping("initiateAudit")
    public Map<String,Object> initiateAudit(TblReportMonthlyBase tblReportMonthlyBase, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            tblReportMonthlyBaseService.updateMonthlyBaseAuditStatus(tblReportMonthlyBase,request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 获取当前系统审核人
     * @param systemId
     * @param request
     * @return
     */
    @RequestMapping("getCurrentSystemDataChecker")
    public Map<String,Object> getCurrentSystemDataChecker(long systemId, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.put("userName", tblReportMonthlyConfigService.getCurrentSystemDataChecker(systemId));
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 获取当前用户的角色
     * @param request
     * @return
     */
    @RequestMapping("getCurrentUserRole")
    public Map<String,Object> getCurrentUserRole(HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.putAll(tblReportMonthlyConfigService.getCurrentUserRole(request));
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 修改模板（月报中修改模板）
     * @param tblReportMonthlyModule
     * @param request
     * @return
     */
    @RequestMapping("updateModule")
    public Map<String,Object> updateModule(TblReportMonthlyModule tblReportMonthlyModule, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            tblReportMonthlyMoudleService.updateModule(tblReportMonthlyModule);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 添加模板
     * @param date
     * @param module
     * @param num
     * @param content
     * @param request
     * @return
     */
    @RequestMapping("addModule")
    public Map<String,Object> addModule(String date,String module,int num,String content, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.putAll(tblReportMonthlyMoudleService.addModule( date, module, num, content,request));
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 删除模板
     * @param id
     * @param date
     * @param module
     * @param num
     * @param request
     * @return
     */
    @RequestMapping("deleteModule")
    public Map<String,Object> deleteModule(Long id,String date,String module,int num, HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            tblReportMonthlyMoudleService.deleteModule(id, date, module, num,request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     *获取所有漏检情况总结
     * @param request
     * @return
     */
    @RequestMapping("getAllUndetectedSummary")
    public Map<String,Object> getAllUndetectedSummary(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        try {
            result.putAll(tblReportMonthlyUndetectedSummaryService.findAll(request));
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 更新漏检情况总结
     * @param tblReportMonthlyUndetectedSummary
     * @param request
     * @return
     */
    @RequestMapping("updateUndetectedSummary")
    public Map<String,Object> updateUndetectedSummary(TblReportMonthlyUndetectedSummary tblReportMonthlyUndetectedSummary, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        try {
            tblReportMonthlyUndetectedSummaryService.updateUndetectedSummary(tblReportMonthlyUndetectedSummary,request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 新增漏检情况总结
     * @param tblReportMonthlyUndetectedSummary
     * @param request
     * @return
     */
    @RequestMapping("addUndetectedSummary")
    public Map<String,Object> addUndetectedSummary(TblReportMonthlyUndetectedSummary tblReportMonthlyUndetectedSummary, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        try {
            tblReportMonthlyUndetectedSummaryService.addUndetectedSummary(tblReportMonthlyUndetectedSummary,request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 删除漏检情况总结
     * @param tblReportMonthlyUndetectedSummary
     * @param request
     * @return
     */
    @RequestMapping("deleteUndetectedSummary")
    public Map<String,Object> deleteUndetectedSummary(TblReportMonthlyUndetectedSummary tblReportMonthlyUndetectedSummary, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        try {
            tblReportMonthlyUndetectedSummaryService.deleteUndetectedSummary(tblReportMonthlyUndetectedSummary,request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     *获取遗留缺陷清单
     * @param yearMonth
     * @param request
     * @return
     */
    @RequestMapping("getRemainDefect")
    public Map<String,Object> getRemainDefect(String yearMonth, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        try {
            result.putAll(monthlyReport.getRemainDefect(yearMonth,request));
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 获取本月的系统数据
     * @param yearMonth
     * @param request
     * @return
     */
    @RequestMapping("getThisMonthlySystemData")
    public Map<String,Object> getThisMonthlySystemData(String yearMonth, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        try {
            result.putAll(tblReportMonthlySystemDataService.findByMonthly(yearMonth));
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 获取本年的系统数据
     * @param yearMonth
     * @param request
     * @return
     */
    @RequestMapping("getThisYearSystemData")
    public Map<String,Object> getThisYearSystemData(String yearMonth, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        try {
            result.putAll(tblReportCumulativeSystemDataService.findByYearMonth(yearMonth));
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }

    /**
     * 审核人审核系统
     * @param tblReportMonthlySystemData
     * @param request
     * @return
     */
    @RequestMapping("auditSystemData")
    public Map<String,Object> auditSystemData(TblReportMonthlySystemData tblReportMonthlySystemData, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        try {
            tblReportMonthlySystemDataService.auditSystemData(tblReportMonthlySystemData,request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            result = super.handleException(e, e.getMessage());
        }
        return result;
    }


}
