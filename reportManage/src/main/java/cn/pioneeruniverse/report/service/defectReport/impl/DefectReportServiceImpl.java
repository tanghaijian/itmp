package cn.pioneeruniverse.report.service.defectReport.impl;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.ResponseMessageModel;
import cn.pioneeruniverse.report.dao.mybatis.report.defectReportDao.DefectReportMapper;
import cn.pioneeruniverse.report.entity.ReportUiFavorite;
import cn.pioneeruniverse.report.service.defectReport.IDefectReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

@Service
public class DefectReportServiceImpl implements IDefectReportService {

    @Autowired
    private DefectReportMapper defectReportMapper;

    /**
     * 报表收藏数据增加
     * @param defectReport
     * @param request
     * @return
     */
    @Override
    public ResponseMessageModel addDefectReport(String defectReport, HttpServletRequest request, String menuUrl, String filterName) {
        ResponseMessageModel responseMessageModel = new ResponseMessageModel();
        try{
            ReportUiFavorite reportUiFavorite = new ReportUiFavorite();
            //收藏列表名
            reportUiFavorite.setFilterName(filterName);
            //收藏内容
            reportUiFavorite.setFavoriteContent(defectReport);
            //当前系统时间
            reportUiFavorite.setCreateDate(new Timestamp(System.currentTimeMillis()));
            //当前登录用户id
            reportUiFavorite.setUserId(CommonUtil.getCurrentUserId(request));
            //状态
            reportUiFavorite.setStatus(1);
            //菜单url
            reportUiFavorite.setMenuUrl(menuUrl);
            //创建者
            reportUiFavorite.setCreateBy(CommonUtil.getCurrentUserId(request));

            Integer number = defectReportMapper.addDefectReport(reportUiFavorite);
            //返回更新条数
            responseMessageModel.setCode(number);
            responseMessageModel.setMsg(number);
            return responseMessageModel;
        }catch (Exception e){
            e.printStackTrace();
            responseMessageModel.setCode(0);
            responseMessageModel.setMsg(0);
            return responseMessageModel;
        }

    }

    /**
     * 根据页面url和收藏者获取该人名下的收藏方案
     * @param menuUrl 本页面url
     * @param createBy 收藏者
     * @param request
     * @return
     */
    @Override
    public List<ReportUiFavorite> selectDefectReportList(String menuUrl, Long createBy) {
        return defectReportMapper.selectDefectReportList(menuUrl, createBy);
    }

    /**
     * 报表收藏数据删除
     * @param id
     * @return
     */
    @Override
    public Integer updateDefectReport(Long id) {
        return defectReportMapper.updateDefectReport(id);
    }

    /**
     * id查询收藏报表数据
     * @param id
     * @return
     */
    @Override
    public ReportUiFavorite selectDefectReportById(Long id,String menuUrl) {
    	//将本此选择的收藏方案设置为最近使用的一次方案
    	defectReportMapper.updateSelectByUrl(id, menuUrl);
    	defectReportMapper.updateSelectById(id);
        return defectReportMapper.selectDefectReportById(id);
    }

    /**
     * 
    * @Title: updateInfoById
    * @Description: 根据ID更新收藏方案
    * @author author
    * @param reportUiFavorite
    * @return
    * @throws
     */
    @Override
    public Integer updateInfoById(ReportUiFavorite reportUiFavorite) {
        int number = 0;
        try{
            number = defectReportMapper.updateInfoById(reportUiFavorite);
        }catch (Exception e){
            e.printStackTrace();
            number = 0;
        }
        return number;
    }


}
