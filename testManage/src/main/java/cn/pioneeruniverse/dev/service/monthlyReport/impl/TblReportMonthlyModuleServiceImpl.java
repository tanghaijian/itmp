package cn.pioneeruniverse.dev.service.monthlyReport.impl;

import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlyModuleMapper;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyModule;
import cn.pioneeruniverse.dev.service.monthlyReport.TblReportMonthlyModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class TblReportMonthlyModuleServiceImpl implements TblReportMonthlyModuleService {

    @Autowired
    private TblReportMonthlyModuleMapper tblReportMonthlyModuleMapper;

    @Override
    @Transactional(readOnly = false)
    public void updateModule(TblReportMonthlyModule tblReportMonthlyModule) {
        tblReportMonthlyModuleMapper.updateMonthlyModule(tblReportMonthlyModule);
    }

    @Override
    @Transactional(readOnly = false)
    public Map<String,Object> addModule(String date, String module, int num, String content, HttpServletRequest request) {
        Map<String,Object> result = new HashMap<>();
        TblReportMonthlyModule tblReportMonthlyModule = new TblReportMonthlyModule();
        String moduleArray[] = module.split("_");
        int page = Integer.valueOf(moduleArray[1]);
        int area = Integer.valueOf(moduleArray[2]);
        tblReportMonthlyModule.setNum(num +1);
        tblReportMonthlyModule.setYearMonth(date);
        tblReportMonthlyModule.setPage(page);
        tblReportMonthlyModule.setArea(area);
        tblReportMonthlyModule.setContent(content);
        tblReportMonthlyModule.preInsertOrUpdate(request);
        //排序
        tblReportMonthlyModuleMapper.updateModuleNum(date,page,area,num,1);

        tblReportMonthlyModuleMapper.insert(tblReportMonthlyModule);
        result.put("data",tblReportMonthlyModule);
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteModule(Long id,String date,String module,int num,HttpServletRequest request) {
        tblReportMonthlyModuleMapper.deleteById(id);
        String moduleArray[] = module.split("_");
        int page = Integer.valueOf(moduleArray[1]);
        int area = Integer.valueOf(moduleArray[2]);
        tblReportMonthlyModuleMapper.updateModuleNum(date,page,area,num,0);

    }

    @Override
    public void updateOrInsertModule(String time, HttpServletRequest request) {
        //上个月时间
        String startDate = DateUtil.formatDate(DateUtil.addMonth(DateUtil.parseDate(time), -1),"yyyy-MM");
        //获取当月模板
        List<TblReportMonthlyModule> monthlyModuleList = tblReportMonthlyModuleMapper.findByDate(time);
        if (monthlyModuleList == null || monthlyModuleList.size() ==0) {
            tblReportMonthlyModuleMapper.insertMonthlyModule(startDate,time);
        }
    }

}
