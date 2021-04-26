package cn.pioneeruniverse.project.velocity.tag;

import cn.pioneeruniverse.common.dto.TblCompanyInfoDTO;
import cn.pioneeruniverse.common.dto.TblDeptInfoDTO;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.project.feignInterface.ProjectManageWebToSystemInterface;

import java.util.List;

/**
 * 
* @ClassName: VelocitySystem
* @Description: 页面velocity自定义函数system实现类
* @author author
* @date 2020年8月31日 下午4:43:21
*
 */
public class VelocitySystem {

    private static ProjectManageWebToSystemInterface reqManageWebToSystemInterface = SpringContextHolder.getBean(ProjectManageWebToSystemInterface.class);

    /**
     * 
    * @Title: getDept
    * @Description: 获取所有部门
    * @author author
    * @return
    * @throws
     */
    public List<TblDeptInfoDTO> getDept() {
        return reqManageWebToSystemInterface.getAllDeptInfo();
    }

    /**
     * 
    * @Title: getCompany
    * @Description: 获取所有公司
    * @author author
    * @return
    * @throws
     */
    public List<TblCompanyInfoDTO> getCompany() {
        return reqManageWebToSystemInterface.getAllCompanyInfo();
    }

}
