package cn.pioneeruniverse.system.service.company;


import java.util.List;

import cn.pioneeruniverse.common.dto.TblCompanyInfoDTO;
import com.baomidou.mybatisplus.service.IService;

import cn.pioneeruniverse.system.entity.Company;

public interface ICompanyService extends IService<Company> {

    /**
    * @author author
    * @Description 获取所有有效公司的ID和名称
    * @Date 2020/9/9
    * @param
    * @return java.util.List<cn.pioneeruniverse.system.entity.Company>
    **/
    List<Company> getCompany();
    /**
     *
     * @Title: getAllCompanyInfo
     * @Description: 获取所有有效的公司
     * @author author
     * @return List<TblCompanyInfoDTO> 公司信息
     * @throws
     */
    List<TblCompanyInfoDTO> getAllCompanyInfo();

}
