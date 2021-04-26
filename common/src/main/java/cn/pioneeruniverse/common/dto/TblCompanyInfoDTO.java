package cn.pioneeruniverse.common.dto;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 公司DTO类
 * @Date: Created in 10:33 2018/12/18
 * @Modified By:
 */
public class TblCompanyInfoDTO extends BaseEntity {

    private static final long serialVersionUID = -3861010992935985500L;
  //公司名
    private String companyName;
  //公司简称
    private String companyShortName; 

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }
}
