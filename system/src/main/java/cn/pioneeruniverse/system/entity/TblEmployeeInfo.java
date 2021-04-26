package cn.pioneeruniverse.system.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * Description: 接收用户信息数据
 * Author:liushan
 * Date: 2018/11/26 上午 9:46
 */
public class TblEmployeeInfo extends BaseEntity {
    private static final long serialVersionUID = 3423006941064695095L;
    private Long empId;
 // 用户代码
    private String empCode; 
 // 用户归属处室代码
    private String sectionCode; 
 // 用户归属部门代码
    private String depCode; 
 // 用户归属机构代码
    private String orgCode; 
 // 用户级别：1：普通员工 2：处长 3：部门领导 4：分管领导 5：总经理
    private Long empLevel; 
 // 用户邮箱
    private String empEmail; 
 // 用户姓名
    private String empName; 
 // 用户类型:1：内部员工  2：外部员工
    private String empType; 
 // 用户手机
    private String empPhone; 
 // 当ITCD分配到这个人时，或状态发生变化时，是否邮件通知 0：不通知  1：通知
    private String empEmailnotice; 
 // 用户身份证号
    private String empCertification; 
 // 毕业院校
    private String empInstitution; 
 // 1：中专 2：大专 3：本科  4：研究生 5：博士生 0：其他
    private String empEdu; 
 // 所属供应商公司名称
    private String empSupplier; 
 // 1：有效 0：无效 2：异常
    private String empState; 
    //单点登录名
    private String casLoginname;

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Long getEmpLevel() {
        return empLevel;
    }

    public void setEmpLevel(Long empLevel) {
        this.empLevel = empLevel;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpEmailnotice() {
        return empEmailnotice;
    }

    public void setEmpEmailnotice(String empEmailnotice) {
        this.empEmailnotice = empEmailnotice;
    }

    public String getEmpCertification() {
        return empCertification;
    }

    public void setEmpCertification(String empCertification) {
        this.empCertification = empCertification;
    }

    public String getEmpInstitution() {
        return empInstitution;
    }

    public void setEmpInstitution(String empInstitution) {
        this.empInstitution = empInstitution;
    }

    public String getEmpEdu() {
        return empEdu;
    }

    public void setEmpEdu(String empEdu) {
        this.empEdu = empEdu;
    }

    public String getEmpSupplier() {
        return empSupplier;
    }

    public void setEmpSupplier(String empSupplier) {
        this.empSupplier = empSupplier;
    }

    public String getEmpState() {
        return empState;
    }

    public void setEmpState(String empState) {
        this.empState = empState;
    }

    public String getCasLoginname() {
        return casLoginname;
    }

    public void setCasLoginname(String casLoginname) {
        this.casLoginname = casLoginname;
    }
}
