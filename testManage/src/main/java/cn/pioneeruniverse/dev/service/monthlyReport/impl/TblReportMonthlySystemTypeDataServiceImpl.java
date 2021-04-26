package cn.pioneeruniverse.dev.service.monthlyReport.impl;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlySystemTypeDataMapper;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlySystemTypeData;
import cn.pioneeruniverse.dev.service.monthlyReport.TblReportMonthlySystemTypeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TblReportMonthlySystemTypeDataServiceImpl implements TblReportMonthlySystemTypeDataService {

    @Autowired
    private TblReportMonthlySystemTypeDataMapper tblReportMonthlySystemTypeDataMapper;

    @Override
    @Transactional(readOnly = false)
    public void updateOrInsertSystemTypeData(String time, HttpServletRequest request) {
        //开始时间
        String startDate = DateUtil.formatDate(DateUtil.addMonth(DateUtil.parseDate(time + "-26"), -1));
        //结束时间
        String endDate = time + "-26";
        List<TblReportMonthlySystemTypeData> tblReportMonthlySystemTypeDataList = tblReportMonthlySystemTypeDataMapper.findSystemTypeData(startDate, endDate);
        for (TblReportMonthlySystemTypeData tblReportMonthlySystemTypeData : tblReportMonthlySystemTypeDataList) {
            tblReportMonthlySystemTypeData.setYearMonth(time);
            tblReportMonthlySystemTypeData.setStatus(1);
            TblReportMonthlySystemTypeData systemTypeData = tblReportMonthlySystemTypeDataMapper.findTaskTypeByYearMonthAndSystemType(time, tblReportMonthlySystemTypeData.getSystemType());
            if (systemTypeData == null) {
                CommonUtil.setBaseValue(tblReportMonthlySystemTypeData, request);
                tblReportMonthlySystemTypeDataMapper.insert(tblReportMonthlySystemTypeData);
            } else {
                tblReportMonthlySystemTypeData.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                tblReportMonthlySystemTypeData.setLastUpdateDate(new Timestamp(new Date().getTime()));
                tblReportMonthlySystemTypeDataMapper.updateByYearMonthAndSystemType(tblReportMonthlySystemTypeData);
            }
        }
    }
}
