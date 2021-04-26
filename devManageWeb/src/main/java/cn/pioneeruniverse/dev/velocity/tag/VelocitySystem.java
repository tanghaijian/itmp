package cn.pioneeruniverse.dev.velocity.tag;

import cn.pioneeruniverse.common.dto.TblCompanyInfoDTO;
import cn.pioneeruniverse.common.dto.TblDataDicDTO;
import cn.pioneeruniverse.common.dto.TblDeptInfoDTO;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.dev.entity.TblDeptInfo;
import cn.pioneeruniverse.dev.feignInterface.DevManageWebToProjectManageInterface;
import cn.pioneeruniverse.dev.feignInterface.DevManageWebToSystemInterface;
import cn.pioneeruniverse.dev.feignInterface.devtask.UserInterface;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 19:05 2018/12/17
 * @Modified By:
 */
public class VelocitySystem {

    private static DevManageWebToSystemInterface devManageWebToSystemInterface = SpringContextHolder.getBean(DevManageWebToSystemInterface.class);

    private static DevManageWebToProjectManageInterface devManageWebToProjectManageInterface = SpringContextHolder.getBean(DevManageWebToProjectManageInterface.class);

    public List<TblDeptInfoDTO> getDept() {
        return devManageWebToSystemInterface.getAllDeptInfo();
    }

    public List<TblCompanyInfoDTO> getCompany() {
        return devManageWebToSystemInterface.getAllCompanyInfo();
    }
    
    public Map<String, Object> getMenuByCode(String code) {
        return devManageWebToSystemInterface.getMenuByCode(code);
    }

    public List<Map<String, Object>> getProject() {
        return devManageWebToProjectManageInterface.getAllProjectInfo();
    }

}
