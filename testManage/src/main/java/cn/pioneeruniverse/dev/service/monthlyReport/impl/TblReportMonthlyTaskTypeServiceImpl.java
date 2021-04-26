package cn.pioneeruniverse.dev.service.monthlyReport.impl;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlyTaskTypeMapper;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyTaskType;
import cn.pioneeruniverse.dev.service.monthlyReport.TblReportMonthlyTaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TblReportMonthlyTaskTypeServiceImpl implements TblReportMonthlyTaskTypeService {
    @Autowired
    private TblReportMonthlyTaskTypeMapper tblReportMonthlyTaskTypeMapper;

    @Override
    @Transactional(readOnly = false)
    public void updateOrInsertTaskType(String time, HttpServletRequest request) {
        //开始时间
        String startDate = DateUtil.formatDate(DateUtil.addMonth(DateUtil.parseDate(time + "-26"), -1));
        //结束时间
        String endDate = time + "-26";
        //月报任务类型等级
        List<TblReportMonthlyTaskType> tblReportMonthlyTaskTypeList = tblReportMonthlyTaskTypeMapper.findTaskType(startDate, endDate);
        if (tblReportMonthlyTaskTypeList != null && tblReportMonthlyTaskTypeList.size() > 0) {
            //敏捷系统总数
            long agileCount = tblReportMonthlyTaskTypeList.stream().filter(e -> e.getSystemType() == 1).collect(Collectors.toList()).stream().mapToInt(TblReportMonthlyTaskType::getTaskNumber).sum();
            //运维系统总数
            long devCount = tblReportMonthlyTaskTypeList.stream().filter(e -> e.getSystemType() == 2).collect(Collectors.toList()).stream().mapToInt(TblReportMonthlyTaskType::getTaskNumber).sum();
            //项目系统总数
            long proCount = tblReportMonthlyTaskTypeList.stream().filter(e -> e.getSystemType() == 3).collect(Collectors.toList()).stream().mapToInt(TblReportMonthlyTaskType::getTaskNumber).sum();
            for (TblReportMonthlyTaskType tblReportMonthlyTaskType : tblReportMonthlyTaskTypeList) {
                if (tblReportMonthlyTaskType.getSystemType() == 1) {
                    tblReportMonthlyTaskType.setPercentage(Double.valueOf(tblReportMonthlyTaskType.getTaskNumber()) / agileCount);
                } else if (tblReportMonthlyTaskType.getSystemType() == 2) {
                    tblReportMonthlyTaskType.setPercentage(Double.valueOf(tblReportMonthlyTaskType.getTaskNumber()) / devCount);
                } else {
                    tblReportMonthlyTaskType.setPercentage(Double.valueOf(tblReportMonthlyTaskType.getTaskNumber()) / proCount);
                }
                tblReportMonthlyTaskType.setYearMonth(time);
                TblReportMonthlyTaskType taskType = tblReportMonthlyTaskTypeMapper.findTaskTypeByYearMonthAndSystemTypeAndType(time, tblReportMonthlyTaskType.getSystemType(),
                        tblReportMonthlyTaskType.getType());
                if (taskType == null) {
                    CommonUtil.setBaseValue(tblReportMonthlyTaskType, request);
                    tblReportMonthlyTaskType.setStatus(1);
                    tblReportMonthlyTaskTypeMapper.insert(tblReportMonthlyTaskType);
                } else {
                    tblReportMonthlyTaskType.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                    tblReportMonthlyTaskType.setLastUpdateDate(new Timestamp(new Date().getTime()));
                    tblReportMonthlyTaskType.setStatus(1);
                    tblReportMonthlyTaskTypeMapper.updateByYearMonthAndSystemTypeAndLevel(tblReportMonthlyTaskType);
                }
            }
        }
    }
}
