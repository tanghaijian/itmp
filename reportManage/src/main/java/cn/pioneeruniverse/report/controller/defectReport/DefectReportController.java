package cn.pioneeruniverse.report.controller.defectReport;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.ResponseMessageModel;
import cn.pioneeruniverse.report.entity.ReportUiFavorite;
import cn.pioneeruniverse.report.service.defectReport.IDefectReportService;


/**
 * 
* @ClassName: DefectReportController
* @Description: 缺陷报表controller
* @author author
* @date 2020年8月3日 上午10:38:55
*
 */
@RestController
@RequestMapping("defectReport")
public class DefectReportController {

    @Autowired
    private IDefectReportService defectReportService;

    /**
     * 报表参数收藏
     * @param defectReport 收藏条件封装
     * @param menuUrl  页面对应的url
     * @param filterName 收藏名
     * @param request
     * @return
     */
    @RequestMapping("/collectCondition")
    public ResponseMessageModel collectCondition(String defectReport, String menuUrl, String filterName, HttpServletRequest request){
        return defectReportService.addDefectReport(defectReport, request, menuUrl, filterName);
    }

    /**
     * 
    * @Title: selectDefectReportList
    * @Description: 选择收藏方案列表信息
    * @author author
    * @param menuUrl：该页面的url
    * @param request
    * @return 收藏方案列表
    * @throws
     */
    @RequestMapping("/selectDefectReportList")
    public List<ReportUiFavorite> selectDefectReportList(String menuUrl , HttpServletRequest request){
        return defectReportService.selectDefectReportList(menuUrl, CommonUtil.getCurrentUserId(request));
    }

    /**
     * 
    * @Title: updateDefectReport
    * @Description: 取消收藏方案
    * @author author
    * @param id 需要取消的方案ID
    * @return
    * @throws
     */
    @RequestMapping("/updateDefectReport")
    public Integer updateDefectReport(Long id){
        return defectReportService.updateDefectReport(id);
    }


    /**
     * 
    * @Title: selectDefectReportById
    * @Description: 选择具体某一次收藏
    * @author author
    * @param id 选择的收藏方案ID
    * @param menuUrl 本页面的url
    * @return
    * @throws
     */
    @RequestMapping("/selectDefectReportById")
    public ReportUiFavorite selectDefectReportById(Long id,String menuUrl){
        return defectReportService.selectDefectReportById(id,menuUrl);
    }


    /**
     * 
    * @Title: updateInfoById
    * @Description: 更新报表收藏方案
    * @author author
    * @param reportUiFavorite
    * @return
    * @throws
     */
    @PostMapping("/updateInfo")
    public Integer updateInfoById(ReportUiFavorite reportUiFavorite){
        return defectReportService.updateInfoById(reportUiFavorite);
    }



}
