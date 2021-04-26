package cn.pioneeruniverse.common.dto;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 部门DTO类
 * @Date: Created in 10:32 2018/12/18
 * @Modified By:
 */
public class TblDeptInfoDTO extends BaseEntity {

    private static final long serialVersionUID = -7386338705559632423L;

  //部门名称
    private String deptName;
  //部门编码
    private String deptNumber; 
  //父部门编码
    private String parentDeptNumber; 
  //父部门ID
    private Long parentDeptId; 
  //所有父部门ID，如1,2,3
    private String parentDeptIds; 

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptNumber() {
        return deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this.deptNumber = deptNumber;
    }

    public Long getParentDeptId() {
        return parentDeptId;
    }

    public void setParentDeptId(Long parentDeptId) {
        this.parentDeptId = parentDeptId;
    }

    public String getParentDeptIds() {
        return parentDeptIds;
    }

    public void setParentDeptIds(String parentDeptIds) {
        this.parentDeptIds = parentDeptIds;
    }

    public String getParentDeptNumber() {
        return parentDeptNumber;
    }

    public void setParentDeptNumber(String parentDeptNumber) {
        this.parentDeptNumber = parentDeptNumber;
    }
}
