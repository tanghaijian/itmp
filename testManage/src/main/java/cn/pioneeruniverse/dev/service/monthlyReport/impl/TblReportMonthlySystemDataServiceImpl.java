package cn.pioneeruniverse.dev.service.monthlyReport.impl;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportCumulativeSystemDataMapper;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlyAuditMapper;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlyBaseMapper;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlySystemDataMapper;
import cn.pioneeruniverse.dev.entity.TblReportMonthlySystem;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportCumulativeSystemData;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyAudit;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyBase;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlySystemData;
import cn.pioneeruniverse.dev.service.monthlyReport.TblReportMonthlySystemDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TblReportMonthlySystemDataServiceImpl implements TblReportMonthlySystemDataService {

    @Autowired
    private TblReportMonthlySystemDataMapper tblReportMonthlySystemDataMapper;

    @Autowired
    private TblReportMonthlyAuditMapper tblReportMonthlyAuditMapper;

    @Autowired
    private TblReportMonthlyBaseMapper tblReportMonthlyBaseMapper;
    @Autowired
    private TblReportCumulativeSystemDataMapper tblReportCumulativeSystemDataMapper;


    @Override
    @Transactional(readOnly = false)
    public void updateMonthlySystemData(TblReportMonthlySystemData tblReportMonthlySystemData, HttpServletRequest request) {
        tblReportMonthlySystemData.preInsertOrUpdate(request);
        tblReportMonthlySystemDataMapper.updateMonthlySystemDate(tblReportMonthlySystemData);
        //漏捡数计算
        tblReportMonthlyBaseMapper.updateUndefectedNumber(tblReportMonthlySystemData.getYearMonth());

        //往累计表中更新数据
        String nowMonthly = tblReportMonthlySystemData.getYearMonth();
        String lastMonthly = DateUtil.formatDate(DateUtil.addMonth(DateUtil.parseDate(nowMonthly,"yyyy-MM"), -1), "yyyy-MM");

        String month = DateUtil.formatDate(DateUtil.parseDate(nowMonthly, "yyyy-MM"), "MM");
        //获取上个月的累计数据
        TblReportCumulativeSystemData lastCumulativeSystemData = tblReportCumulativeSystemDataMapper.findByYearMonthAndSystemId(lastMonthly,tblReportMonthlySystemData.getSystemId(),tblReportMonthlySystemData.getSystemType());
        //获取本月的累计数据
        tblReportCumulativeSystemDataMapper.deleteByYearMonthAndSystemType(nowMonthly,tblReportMonthlySystemData.getSystemId(),tblReportMonthlySystemData.getSystemType());
        if (lastCumulativeSystemData == null){
                TblReportCumulativeSystemData nowCumulativeSystemData = new TblReportCumulativeSystemData();
                BeanUtils.copyProperties(tblReportMonthlySystemData,nowCumulativeSystemData);
                nowCumulativeSystemData.setId(null);
                nowCumulativeSystemData.setStartMonth(Integer.parseInt(month));
                double defectPercent = this.avgCount(tblReportMonthlySystemData.getDefectNumber(),0,tblReportMonthlySystemData.getLastmonthUndefectedNumber(),0);
                nowCumulativeSystemData.setDefectedRate(defectPercent*100);
                nowCumulativeSystemData.setEndMonth(Integer.parseInt(month));
                nowCumulativeSystemData.setUndefectedNumber(tblReportMonthlySystemData.getLastmonthUndefectedNumber());
                tblReportCumulativeSystemDataMapper.insert(nowCumulativeSystemData);

        }else {
                TblReportCumulativeSystemData nowCumulativeSystemData = new TblReportCumulativeSystemData();
                BeanUtils.copyProperties(tblReportMonthlySystemData,nowCumulativeSystemData);
                nowCumulativeSystemData.setId(null);
                nowCumulativeSystemData.setUndefectedNumber(lastCumulativeSystemData.getUndefectedNumber()+tblReportMonthlySystemData.getLastmonthUndefectedNumber());
                double defectPercent = this.avgCount(tblReportMonthlySystemData.getDefectNumber(),lastCumulativeSystemData.getDefectNumber(),
                        tblReportMonthlySystemData.getLastmonthUndefectedNumber(),lastCumulativeSystemData.getUndefectedNumber());
                nowCumulativeSystemData.setDefectedRate(defectPercent*100);
                nowCumulativeSystemData.setStartMonth(lastCumulativeSystemData.getStartMonth());
                nowCumulativeSystemData.setEndMonth(Integer.parseInt(month));
                tblReportCumulativeSystemDataMapper.insert(nowCumulativeSystemData);
        }
    }


    @Override
    public Map<String, Object> findByMonthly(String yearMonth) {
        Map<String,Object> result = new HashMap<>();
        List<TblReportMonthlySystemData> agileDataList = tblReportMonthlySystemDataMapper.findByYearMonthlyAndSystemType(yearMonth,"1");
        List<TblReportMonthlySystemData> devDataList = tblReportMonthlySystemDataMapper.findByYearMonthlyAndSystemType(yearMonth,"2");
        List<TblReportMonthlySystemData> proDataList = tblReportMonthlySystemDataMapper.findByYearMonthlyAndSystemType(yearMonth,"3");
        result.put("agileDataList",agileDataList);
        result.put("devDataList",devDataList);
        result.put("proDataList",proDataList);

        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public void auditSystemData(TblReportMonthlySystemData tblReportMonthlySystemData, HttpServletRequest request) {
        tblReportMonthlySystemData.preInsertOrUpdate(request);
        TblReportMonthlySystemData systemData = new TblReportMonthlySystemData();
        BeanUtils.copyProperties(tblReportMonthlySystemData,systemData);

        tblReportMonthlySystemData.setLastmonthUndefectedBelonger(null);
        tblReportMonthlySystemData.setAuditStatus(1);
        tblReportMonthlySystemData.setLastmonthUndefectedBelonger(null);
        tblReportMonthlySystemDataMapper.updateMonthlySystemDate(tblReportMonthlySystemData);
        //插入类型为1的数据
        systemData.setType(1);
        systemData.preInsertOrUpdate(request);
        systemData.setId(null);
        Long data1Id = this.insertOrUpdateSystemData(systemData);
        //插入类型为2的数据
        systemData.setType(2);
        systemData.setId(null);
        Long data2Id = this.insertOrUpdateSystemData(systemData);
        TblReportMonthlyAudit tblReportMonthlyAudit = new TblReportMonthlyAudit();
        tblReportMonthlyAudit.setYearMonth(systemData.getYearMonth());
        tblReportMonthlyAudit.setData1Id(data1Id);
        tblReportMonthlyAudit.setData2Id(data2Id);
        tblReportMonthlyAudit.setSystemId(systemData.getSystemId());
        tblReportMonthlyAudit.setSystemName(systemData.getSystemName());
        tblReportMonthlyAudit.setUserName(CommonUtil.getCurrentUserName(request));
        tblReportMonthlyAudit.setUserId(CommonUtil.getCurrentUserId(request));
        tblReportMonthlyAudit.preInsertOrUpdate(request);
        tblReportMonthlyAuditMapper.insert(tblReportMonthlyAudit);
    }


    private double avgCount(Integer firstDefectNumber,Integer secondDefectNumber,Integer firstUnDefectNumber,Integer secondUndefectNumber){
        int newDefectNumber = Optional.ofNullable(firstDefectNumber).orElse(0);
        int oldDefectNumber =Optional.ofNullable(secondDefectNumber).orElse(0);
        int newUndefectedNumber = Optional.ofNullable(firstUnDefectNumber).orElse(0);
        int oldUndefectedNumber = Optional.ofNullable(secondUndefectNumber).orElse(0);

        if (newDefectNumber + oldDefectNumber +newUndefectedNumber+oldUndefectedNumber !=0){
            Double defectedRate = (double)(1- (newDefectNumber+oldDefectNumber / (newDefectNumber + oldDefectNumber +newUndefectedNumber+oldUndefectedNumber))*100);
           return defectedRate;
        }else {
            return 100.00;
        }
    }

    private Long insertOrUpdateSystemData(TblReportMonthlySystemData tblReportMonthlySystemData){
        TblReportMonthlySystemData systemData = tblReportMonthlySystemDataMapper.findByYearMonthAndTypeAndSystemId(tblReportMonthlySystemData.getYearMonth(),
                tblReportMonthlySystemData.getSystemId(),tblReportMonthlySystemData.getType());
        if (systemData == null){
            tblReportMonthlySystemDataMapper.insert(tblReportMonthlySystemData);
        }else {
            tblReportMonthlySystemData.setId(systemData.getId());
            tblReportMonthlySystemDataMapper.updateById(tblReportMonthlySystemData);
        }
        return tblReportMonthlySystemData.getId();

    }
}
