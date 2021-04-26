package cn.pioneeruniverse.system.dao.mybatis.company;

import java.util.List;

import cn.pioneeruniverse.common.dto.TblCompanyInfoDTO;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.system.entity.Company;

//公司 company 
public interface CompanyDao extends BaseMapper<Company> {

    List<Company> getCompany();

    List<TblCompanyInfoDTO> getAllCompanyInfo();

}
