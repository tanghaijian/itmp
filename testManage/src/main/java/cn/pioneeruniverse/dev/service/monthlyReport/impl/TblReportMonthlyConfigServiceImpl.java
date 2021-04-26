package cn.pioneeruniverse.dev.service.monthlyReport.impl;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.monthlyReport.TblReportMonthlyConfigMapper;
import cn.pioneeruniverse.dev.entity.monthlyReport.TblReportMonthlyConfig;
import cn.pioneeruniverse.dev.service.monthlyReport.TblReportMonthlyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class TblReportMonthlyConfigServiceImpl implements TblReportMonthlyConfigService {

    @Autowired
    private TblReportMonthlyConfigMapper tblReportMonthlyConfigMapper;
    @Autowired
    RedisUtils redisUtils;

    @Override
    @Transactional(readOnly = false)
    public void updateMonthlyConfig(TblReportMonthlyConfig tblReportMonthlyConfig, HttpServletRequest request) {
        TblReportMonthlyConfig monthlyConfig =  tblReportMonthlyConfigMapper.findBySystemIdAndUserTypeAndUserId(tblReportMonthlyConfig.getSystemId(),tblReportMonthlyConfig.getUserType());
        tblReportMonthlyConfig.preInsertOrUpdate(request);
        if (monthlyConfig == null){
            tblReportMonthlyConfigMapper.insert(tblReportMonthlyConfig);
        }else {
            tblReportMonthlyConfig.setId(monthlyConfig.getId());
            tblReportMonthlyConfigMapper.updateMonthlyConfig(tblReportMonthlyConfig);
        }

    }

    @Override
    public String getCurrentSystemDataChecker(Long systemId) {
        return tblReportMonthlyConfigMapper.getUserNameBySystemId(systemId);
    }

    @Override
    public Map<String, String> getCurrentUserRole(HttpServletRequest request) {
        Map<String,String> resultMap = new HashMap<>();
        Long userId = CommonUtil.getCurrentUserId(request);
        if(userId != null) {
            LinkedHashMap map2 = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
            List<String> roleCodes = (List<String>) map2.get("roles");
            if (roleCodes != null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户有角色是系统管理员
                resultMap.put("role", "0");
                return resultMap;
            }

            List<TblReportMonthlyConfig> tblReportMonthlyConfigList = tblReportMonthlyConfigMapper.findByUserId(userId);
            if (tblReportMonthlyConfigList != null && tblReportMonthlyConfigList.size() > 0) {
                long count = tblReportMonthlyConfigList.stream().filter(e -> e.getUserType() == 1).count();
                if (count > 0) {
                    resultMap.put("role", "1");
                    return resultMap;
                }
                count = tblReportMonthlyConfigList.stream().filter(e -> e.getUserType() == 2).count();
                if (count > 0) {
                    resultMap.put("role", "2");
                    return resultMap;
                }

            }
        }
        resultMap.put("role","3");
        return resultMap;
    }

    @Override
    public TblReportMonthlyConfig getReportMonthlyManager() {
        if (tblReportMonthlyConfigMapper.getReportMonthlyManager()!=null && tblReportMonthlyConfigMapper.getReportMonthlyManager().size()>0){
            return tblReportMonthlyConfigMapper.getReportMonthlyManager().get(0);
        }
        return null;
    }
}
