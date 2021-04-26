package cn.pioneeruniverse.dev.service.monthlyReport.impl;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportCumulativeSystemDataMapper;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlySystemDataMapper;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlySystemTypeDataMapper;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportCumulativeSystemData;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlySystemData;
import cn.pioneeruniverse.dev.service.monthlyReport.TblReportCumulativeSystemDataService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class TblReportCumulativeSystemDataServiceImpl implements TblReportCumulativeSystemDataService {


    @Autowired
    private TblReportMonthlySystemTypeDataMapper tblReportMonthlySystemTypeDataMapper;
    @Autowired
    private TblReportCumulativeSystemDataMapper tblReportCumulativeSystemDataMapper;
    @Autowired
    private TblReportMonthlySystemDataMapper tblReportMonthlySystemDataMapper;

    @Override
    @Transactional(readOnly = false)
    public void updateOrInsertCumulativeSystemTypeData(String time, HttpServletRequest request) {
        //1.获取这个月的年份 获取上个月的年份
        //上个月时间
        String lastMonthly = DateUtil.formatDate(DateUtil.addMonth(DateUtil.parseDate(time, "yyyy-MM"), -1), "yyyy-MM");
        //当前年
        String nowMonthlyYear = DateUtil.formatDate(DateUtil.parseDate(time, "yyyy-MM"), "yyyy");
        String lastMonthlyYear = DateUtil.formatDate(DateUtil.parseDate(lastMonthly, "yyyy-MM"), "yyyy");

        //2.如果上个月的年份跟这个月的年份是一样的情况下 就取上个月的累计数据 否则不取
        List<TblReportCumulativeSystemData> lastMonthlyCumulativeSystemDataList = null;
        if (nowMonthlyYear.equals(lastMonthlyYear)) {
            lastMonthlyCumulativeSystemDataList = tblReportCumulativeSystemDataMapper.findCumulativeSystemDataByYearMonth(lastMonthly);
        }
        //删除当前月的数据
        tblReportCumulativeSystemDataMapper.deleteByYearMonth(time);
        this.insertCumulativeAddSystemData(lastMonthlyCumulativeSystemDataList, time, request);
    }

    @Override
    public List<TblReportCumulativeSystemData> findByYearMonthAndSystemType(String yearMonth, Integer systemType) {
        return null;
    }

    @Override
    public Map<String, Object> findByYearMonth(String yearMonth) {
        //当前年
        Map<String, Object> result = new HashMap<>();
        List<TblReportCumulativeSystemData> agileDataList = tblReportCumulativeSystemDataMapper.findByYearMonthAndSystemType(yearMonth, 1);
        List<TblReportCumulativeSystemData> devDataList = tblReportCumulativeSystemDataMapper.findByYearMonthAndSystemType(yearMonth, 2);
        List<TblReportCumulativeSystemData> proDataList = tblReportCumulativeSystemDataMapper.findByYearMonthAndSystemType(yearMonth, 3);
        result.put("agileDataList", agileDataList);
        result.put("devDataList", devDataList);
        result.put("proDataList", proDataList);
        return result;
    }


    private void insertCumulativeSystemData(String time) {
        String month = DateUtil.formatDate(DateUtil.parseDate(time, "yyyy-MM"), "MM");
        List<TblReportMonthlySystemData> tblReportMonthlySystemDataList = tblReportMonthlySystemDataMapper.findByYearMonth(time);
        for (TblReportMonthlySystemData tblReportMonthlySystemData : tblReportMonthlySystemDataList) {
            TblReportCumulativeSystemData tblReportCumulativeSystemData = new TblReportCumulativeSystemData();
            try {
                BeanUtils.copyProperties(tblReportCumulativeSystemData, tblReportMonthlySystemData);
                tblReportCumulativeSystemData.setStartMonth(Integer.parseInt(month));
                tblReportCumulativeSystemData.setEndMonth(Integer.parseInt(month));
                //漏捡数
                tblReportCumulativeSystemData.setUndefectedNumber(tblReportMonthlySystemData.getLastmonthUndefectedNumber());
                tblReportCumulativeSystemData.setId(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tblReportCumulativeSystemDataMapper.insert(tblReportCumulativeSystemData);
        }
    }

    private void insertCumulativeAddSystemData(List<TblReportCumulativeSystemData> lastMonthlyCumulativeSystemDataList, String time, HttpServletRequest request) {
        String month = DateUtil.formatDate(DateUtil.parseDate(time, "yyyy-MM"), "MM");
        List<TblReportMonthlySystemData> tblReportMonthlySystemDataList = tblReportMonthlySystemDataMapper.findByYearMonth(time);


        Iterator<TblReportCumulativeSystemData> first = lastMonthlyCumulativeSystemDataList.iterator();
        while (first.hasNext()) {
            TblReportCumulativeSystemData firstData = first.next();
            Iterator<TblReportMonthlySystemData> second = tblReportMonthlySystemDataList.iterator();
            while (second.hasNext()) {
                TblReportMonthlySystemData secondData = second.next();
                if (secondData.getSystemId().equals(firstData.getSystemId()) && secondData.getSystemType().equals(firstData.getSystemType())) {
                    //任务数
                    Integer taskNumber = firstData.getTaskNumber() + secondData.getTaskNumber();
                    firstData.setTaskNumber(taskNumber);
                    //测试发现缺陷数
                    int defectNumber = firstData.getDefectNumber() + secondData.getDefectNumber();
                    firstData.setDefectNumber(defectNumber);
                    //Integer REPAIRED_DEFECT_NUMBER
                    //修复缺陷数
                    int repairedDefectNumber = firstData.getRepairedDefectNumber() + secondData.getRepairedDefectNumber();
                    firstData.setRepairedDefectNumber(repairedDefectNumber);
                    //UNREPAIRED_DEFECT_NUMBER 遗留缺陷数
                    Integer unrepairedDefectNumber = firstData.getUnrepairedDefectNumber() + secondData.getUnrepairedDefectNumber();
                    firstData.setUndefectedNumber(unrepairedDefectNumber);
                    //DESIGN_CASE_NUMBER 设计用例数
                    int designCaseNumber = firstData.getDesignCaseNumber() + secondData.getDesignCaseNumber();
                    firstData.setDesignCaseNumber(designCaseNumber);
                    //DEFECT_PERCENT 缺陷率
                    if (designCaseNumber != 0) {
                        firstData.setDefectPercent(((double) defectNumber / designCaseNumber)*100);
                    } else {
                        firstData.setDefectPercent((double) 0);
                    }
                    //UNDEFECTED_NUMBER 漏检缺陷数
                    int undefectedNumber = firstData.getUndefectedNumber() + secondData.getLastmonthUndefectedNumber();
                    firstData.setUndefectedNumber(undefectedNumber);
                    //检出率
                    if (defectNumber != 0) {
                        firstData.setDefectedRate((1 - ((double)undefectedNumber / defectNumber)) * 100);
                    } else {
                        firstData.setDefectedRate(100.00);
                    }
                    //TOTAL_REPAIR_ROUND 累计修复轮次
                    int totalRepairRound = firstData.getTotalRepairRound() + secondData.getTotalRepairRound();
                    firstData.setTotalRepairRound(totalRepairRound);
                    //累计修复轮次AVG_REPAIR_ROUND
                    if (repairedDefectNumber != 0) {
                        double avgRepairRound = (double) totalRepairRound / repairedDefectNumber;
                        firstData.setAvgRepairRound(avgRepairRound);
                    } else {
                        firstData.setAvgRepairRound((double) 0);
                    }
                    firstData.setCreateBy(CommonUtil.getCurrentUserId(request));
                    firstData.setCreateDate(new Timestamp(new Date().getTime()));
                    firstData.setYearMonth(time);
                    firstData.setEndMonth(Integer.parseInt(month));
                    firstData.setId(null);
                    //插入交集
                    tblReportCumulativeSystemDataMapper.insert(firstData);
                    second.remove();
                    first.remove();
                    break;
                }
            }
        }
      for (TblReportCumulativeSystemData cumulativeSystemData : lastMonthlyCumulativeSystemDataList){
          cumulativeSystemData.setId(null);
          cumulativeSystemData.setYearMonth(time);
          tblReportCumulativeSystemDataMapper.insert(cumulativeSystemData);
      }
        for (TblReportMonthlySystemData systemData : tblReportMonthlySystemDataList){
            TblReportCumulativeSystemData cumulativeSystemData = new TblReportCumulativeSystemData();
            cumulativeSystemData.setId(null);
            cumulativeSystemData.setDefectPercent(systemData.getDefectPercent());
            cumulativeSystemData.setYearMonth(systemData.getYearMonth());
            cumulativeSystemData.setCreateDate(systemData.getCreateDate());
            if (systemData.getDefectNumber() != null && systemData.getDefectNumber() != 0) {
                cumulativeSystemData.setDefectedRate( ((1 - (double)systemData.getLastmonthUndefectedNumber() / systemData.getDefectNumber()) * 100));
            } else {
                cumulativeSystemData.setDefectedRate(100.0);
            }
            cumulativeSystemData.setAvgRepairRound(systemData.getAvgRepairRound());
            cumulativeSystemData.setTotalRepairRound(systemData.getTotalRepairRound());
            cumulativeSystemData.setCreateBy(systemData.getCreateBy());
            cumulativeSystemData.setDesignCaseNumber(systemData.getDesignCaseNumber());
            cumulativeSystemData.setRepairedDefectNumber(systemData.getRepairedDefectNumber());
            cumulativeSystemData.setDefectNumber(systemData.getDefectNumber());
            cumulativeSystemData.setTaskNumber(systemData.getTaskNumber());
            cumulativeSystemData.setSystemId(systemData.getSystemId());
            cumulativeSystemData.setSystemName(systemData.getSystemName());
            cumulativeSystemData.setSystemType(systemData.getSystemType());
            cumulativeSystemData.setLastUpdateBy(systemData.getLastUpdateBy());
            cumulativeSystemData.setLastUpdateDate(systemData.getLastUpdateDate());
            cumulativeSystemData.setStatus(systemData.getStatus());
            cumulativeSystemData.setStartMonth(Integer.parseInt(month));
            cumulativeSystemData.setEndMonth(Integer.parseInt(month));
            cumulativeSystemData.setUndefectedNumber(systemData.getLastmonthUndefectedNumber());
            tblReportCumulativeSystemDataMapper.insert(cumulativeSystemData);
        }
    }


}
