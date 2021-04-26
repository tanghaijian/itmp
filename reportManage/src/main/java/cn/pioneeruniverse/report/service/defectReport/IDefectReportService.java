package cn.pioneeruniverse.report.service.defectReport;

import cn.pioneeruniverse.common.utils.ResponseMessageModel;
import cn.pioneeruniverse.report.dto.DefectReportDTO;
import cn.pioneeruniverse.report.entity.ReportUiFavorite;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IDefectReportService {

    /**
     * 报表收藏数据增加
     * @param defectReport
     * @param request
     * @return
     */
    ResponseMessageModel addDefectReport(String defectReport, HttpServletRequest request, String menuUrl, String filterName);

    /**
     * 报表列表数据
     * @param menuUrl
     * @return
     */
    List<ReportUiFavorite> selectDefectReportList(String menuUrl, Long createBy);

    /**
     * 报表收藏数据删除
     * @param id
     * @return
     */
    Integer updateDefectReport(Long id);


    ReportUiFavorite selectDefectReportById(Long id,String menuUrl);

    /**
     *  报表内容修改
     * @param reportUiFavorite
     * @return
     */
    Integer updateInfoById(ReportUiFavorite reportUiFavorite);



}
