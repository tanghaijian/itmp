package cn.pioneeruniverse.dev.service.monthlyReport.impl;

import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlyBaseMapper;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlySystemDataMapper;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyBase;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlySystemData;
import cn.pioneeruniverse.dev.feignInterface.TestManageToSystemInterface;
import cn.pioneeruniverse.dev.service.monthlyReport.TblReportMonthlyBaseService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@Service
public class TblReportMonthlyBaseMapperServiceImpl implements TblReportMonthlyBaseService {

    @Autowired
    private TblReportMonthlyBaseMapper tblReportMonthlyBaseMapper;
    @Autowired
    private TestManageToSystemInterface testManageToSystemInterface;
    @Autowired
    private TblReportMonthlySystemDataMapper tblReportMonthlySystemDataMapper;

    @Override
    @Transactional(readOnly = false)
    public void updateMonthlyBase(TblReportMonthlyBase tblReportMonthlyBase, HttpServletRequest request) {
       //CommonUtil.setBaseValue(tblReportMonthlyBase,request);
        tblReportMonthlyBase.preInsertOrUpdate(request);
        tblReportMonthlyBaseMapper.updateMonthlyBase(tblReportMonthlyBase);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateMonthlyBaseAuditStatus(TblReportMonthlyBase tblReportMonthlyBase, HttpServletRequest request) {
        tblReportMonthlyBase.preInsertOrUpdate(request);
        tblReportMonthlyBase.setAuditStatus(1);
        tblReportMonthlyBaseMapper.updateMonthlyBaseAuditStatus(tblReportMonthlyBase);
        //查出用户id 系统名称用于发送邮件和微信
        List<TblReportMonthlySystemData> systemDataList = tblReportMonthlySystemDataMapper.getSystemDataUserIdAndSystemName(tblReportMonthlyBase.getYearMonth());

        //收件人去重
        Set<String> ids  = systemDataList.stream().map(s -> s.getUserId().toString()).collect(Collectors.toSet());
        String userIdString = String.join(",",ids);

        Map<String, Object> emWeMap = new HashMap<String, Object>();
        String yearMonth = tblReportMonthlyBase.getYearMonth();
        Date date = DateUtil.parseDate(yearMonth,"yyyy-MM");
        //创建Calendar实例
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);   //设置年月时间
        //获取当月最后一天
        cal.add(Calendar.MONTH,1);//month+1
        cal.set(Calendar.DAY_OF_MONTH,0);//day-1
        String month = String.valueOf(cal.get(Calendar.MONTH)+1);
        String day = String.valueOf(cal.get(Calendar.DATE));
        emWeMap.put("messageTitle", "【IT开发测试管理系统】- " + month + "月测试月报审核");
        emWeMap.put("messageContent",  month + "月月报测试数据审核，请于" + month + "月" + day + "日12点前处理，谢谢。");
        emWeMap.put("messageReceiver", userIdString);//接收人测试管理岗和执行人 用户表主键
        emWeMap.put("sendMethod", 3);//发送方式 3 邮件和微信
        testManageToSystemInterface.sendMessage(JSON.toJSONString(emWeMap));
    }


}
