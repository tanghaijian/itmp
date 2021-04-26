package cn.pioneeruniverse.dev.service.monthlyReport.impl;

import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlyUndetectedSummaryMapper;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyUndetectedSummary;
import cn.pioneeruniverse.dev.service.monthlyReport.TblReportMonthlyUndetectedSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class TblReportMonthlyUndetectedSummaryServiceImpl implements TblReportMonthlyUndetectedSummaryService {


    @Autowired
    private TblReportMonthlyUndetectedSummaryMapper tblReportMonthlyUndetectedSummaryMapper;


    @Override
    public Map<String, Object> findAll(HttpServletRequest request) {
        Map<String ,Object> resutl = new HashMap<>();
        resutl.put("data",tblReportMonthlyUndetectedSummaryMapper.findAll());
        return resutl;
    }

    @Override
    @Transactional(readOnly = false)
    public void updateUndetectedSummary(TblReportMonthlyUndetectedSummary tblReportMonthlyUndetectedSummary,HttpServletRequest request) {
        tblReportMonthlyUndetectedSummary.preInsertOrUpdate(request);
        tblReportMonthlyUndetectedSummaryMapper.updateById(tblReportMonthlyUndetectedSummary);
    }

    @Override
    @Transactional(readOnly = false)
    public void addUndetectedSummary(TblReportMonthlyUndetectedSummary tblReportMonthlyUndetectedSummary, HttpServletRequest request) {
        tblReportMonthlyUndetectedSummary.preInsertOrUpdate(request);
        tblReportMonthlyUndetectedSummaryMapper.insertAllColumn(tblReportMonthlyUndetectedSummary);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUndetectedSummary(TblReportMonthlyUndetectedSummary tblReportMonthlyUndetectedSummary, HttpServletRequest request) {
        tblReportMonthlyUndetectedSummaryMapper.deleteById(tblReportMonthlyUndetectedSummary.getId());
    }


}
