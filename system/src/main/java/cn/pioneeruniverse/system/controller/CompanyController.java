package cn.pioneeruniverse.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.dto.TblCompanyInfoDTO;
import cn.pioneeruniverse.system.service.company.ICompanyService;

/**
 * 
* @ClassName: CompanyController
* @Description: 公司controller
* @author author
* @date 2020年8月4日 下午10:00:10
*
 */
@RestController
@RequestMapping("company")
public class CompanyController {


    @Autowired
    private ICompanyService iCompanyService;

    /**
     * 
    * @Title: getAllCompanyInfo
    * @Description: 获取所有公司
    * @author author
    * @return List<TblCompanyInfoDTO> 公司列表
    * @throws
     */
    @RequestMapping(value = "getAllCompanyInfo", method = RequestMethod.POST)
    public List<TblCompanyInfoDTO> getAllCompanyInfo() {
        return iCompanyService.getAllCompanyInfo();
    }
}
