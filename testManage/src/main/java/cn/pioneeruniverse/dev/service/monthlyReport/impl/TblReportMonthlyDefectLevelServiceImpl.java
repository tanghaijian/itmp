package cn.pioneeruniverse.dev.service.monthlyReport.impl;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlyDefectLevelMapper;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyDefectLevel;
import cn.pioneeruniverse.dev.service.monthlyReport.TblReportMonthlyDefectLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TblReportMonthlyDefectLevelServiceImpl implements TblReportMonthlyDefectLevelService {

    @Autowired
    private TblReportMonthlyDefectLevelMapper tblReportMonthlyDefectLevelMapper;

    @Override
    @Transactional(readOnly = false)
    public void updateOrInsertDefectLevel(String time, HttpServletRequest request) {
        //开始时间
        String startDate = DateUtil.formatDate(DateUtil.addMonth(DateUtil.parseDate(time + "-26"), -1));
        //结束时间
        String endDate = time + "-26";
        //月报缺陷等级
        List<TblReportMonthlyDefectLevel> tblReportMonthlyDefectLevelList = tblReportMonthlyDefectLevelMapper.findDefectLevel(startDate, endDate);

        if (tblReportMonthlyDefectLevelList != null && tblReportMonthlyDefectLevelList.size() > 0) {
            //敏捷系统缺陷总数
            long agileCount = tblReportMonthlyDefectLevelList.stream().filter(e -> e.getSystemType() == 1 && e.getLevel()!=1).collect(Collectors.toList()).stream().mapToInt(TblReportMonthlyDefectLevel::getDefectNumber).sum();
            //运维系统缺陷总数
            long devCount = tblReportMonthlyDefectLevelList.stream().filter(e -> e.getSystemType() == 2 && e.getLevel()!=1).collect(Collectors.toList()).stream().mapToInt(TblReportMonthlyDefectLevel::getDefectNumber).sum();
            //项目系统缺陷总数
            long proCount = tblReportMonthlyDefectLevelList.stream().filter(e -> e.getSystemType() == 3 && e.getLevel() !=1).collect(Collectors.toList()).stream().mapToInt(TblReportMonthlyDefectLevel::getDefectNumber).sum();
            for (TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel : tblReportMonthlyDefectLevelList) {
                if (tblReportMonthlyDefectLevel.getSystemType() == 1) {
                    tblReportMonthlyDefectLevel.setPercentage(Double.valueOf(tblReportMonthlyDefectLevel.getDefectNumber()) / agileCount);
                } else if (tblReportMonthlyDefectLevel.getSystemType() == 2) {
                    tblReportMonthlyDefectLevel.setPercentage(Double.valueOf(tblReportMonthlyDefectLevel.getDefectNumber()) / devCount);
                } else {
                    tblReportMonthlyDefectLevel.setPercentage(Double.valueOf(tblReportMonthlyDefectLevel.getDefectNumber()) / proCount);
                }
                tblReportMonthlyDefectLevel.setYearMonth(time);
                TblReportMonthlyDefectLevel monthlyDefectLevel = tblReportMonthlyDefectLevelMapper.findDefectLevelByYearMonth(time, tblReportMonthlyDefectLevel.getSystemType(),
                        tblReportMonthlyDefectLevel.getLevel());
                if (monthlyDefectLevel == null) {
                    CommonUtil.setBaseValue(tblReportMonthlyDefectLevel, request);
                    tblReportMonthlyDefectLevel.setStatus(1);
                    tblReportMonthlyDefectLevelMapper.insert(tblReportMonthlyDefectLevel);
                } else {
                    tblReportMonthlyDefectLevel.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                    tblReportMonthlyDefectLevel.setLastUpdateDate(new Timestamp(new Date().getTime()));
                    tblReportMonthlyDefectLevel.setStatus(1);
                    tblReportMonthlyDefectLevelMapper.updateByYearMonthAndSystemTypeAndLevel(tblReportMonthlyDefectLevel);
                }
            }
            //插入所有项目缺陷
            long defectCount = agileCount + devCount + proCount;
            if (defectCount != 0) {
                Map<Integer, Integer> defectCountMap = new HashMap<>();
                //建议性缺陷总数
                Integer suggestDefectCount = tblReportMonthlyDefectLevelList.stream().filter(e -> e.getLevel() == 1).collect(Collectors.toList()).stream().mapToInt(TblReportMonthlyDefectLevel::getDefectNumber).sum();
                defectCountMap.put(1, suggestDefectCount);
                //文字错误缺陷总数
                Integer textErrorDefectCount = tblReportMonthlyDefectLevelList.stream().filter(e -> e.getLevel() == 2).collect(Collectors.toList()).stream().mapToInt(TblReportMonthlyDefectLevel::getDefectNumber).sum();
                defectCountMap.put(2, textErrorDefectCount);
                //轻微缺陷总数
                Integer mirrorDefectCount = tblReportMonthlyDefectLevelList.stream().filter(e -> e.getLevel() == 3).collect(Collectors.toList()).stream().mapToInt(TblReportMonthlyDefectLevel::getDefectNumber).sum();
                defectCountMap.put(3, mirrorDefectCount);
                //一般性缺陷总数
                Integer generalDefectCount = tblReportMonthlyDefectLevelList.stream().filter(e -> e.getLevel() == 4).collect(Collectors.toList()).stream().mapToInt(TblReportMonthlyDefectLevel::getDefectNumber).sum();
                defectCountMap.put(4, generalDefectCount);
                //严重缺陷总数
                Integer seriousDefectCount = tblReportMonthlyDefectLevelList.stream().filter(e -> e.getLevel() == 5).collect(Collectors.toList()).stream().mapToInt(TblReportMonthlyDefectLevel::getDefectNumber).sum();
                defectCountMap.put(5, seriousDefectCount);
                for (int i = 1; i <= 5; i++) {
                    TblReportMonthlyDefectLevel tblReportMonthlyDefectLevel = new TblReportMonthlyDefectLevel();
                    tblReportMonthlyDefectLevel.setStatus(1);
                    tblReportMonthlyDefectLevel.setSystemType(0);
                    CommonUtil.setBaseValue(tblReportMonthlyDefectLevel, request);
                    tblReportMonthlyDefectLevel.setLevel(i);
                    TblReportMonthlyDefectLevel monthlyDefectLevel = tblReportMonthlyDefectLevelMapper.findDefectLevelByYearMonth(time, 0, i);
                    if (monthlyDefectLevel == null) {
                        tblReportMonthlyDefectLevel.setPercentage(Double.valueOf(defectCountMap.get(i)) / defectCount);
                        tblReportMonthlyDefectLevel.setDefectNumber(defectCountMap.get(i));
                        tblReportMonthlyDefectLevel.setYearMonth(time);
                        tblReportMonthlyDefectLevelMapper.insert(tblReportMonthlyDefectLevel);
                    } else {
                        monthlyDefectLevel.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                        monthlyDefectLevel.setLastUpdateDate(new Timestamp(new Date().getTime()));
                        monthlyDefectLevel.setStatus(1);
                        monthlyDefectLevel.setPercentage(Double.valueOf(defectCountMap.get(i)) / defectCount);
                        monthlyDefectLevel.setDefectNumber(defectCountMap.get(i));
                        tblReportMonthlyDefectLevelMapper.updateByYearMonthAndSystemTypeAndLevel(monthlyDefectLevel);
                    }
                }
            }
        }
    }
}
