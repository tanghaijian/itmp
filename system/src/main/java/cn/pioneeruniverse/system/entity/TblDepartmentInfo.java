package cn.pioneeruniverse.system.entity;

import java.util.Date;

/**
 * Description: 同步部门接口报文bean 
 * 系统只用了VALIDSTATUS、ORG_REGISTRATION_NAME、NEW_BRANCH_CODE、ORG_PARENT_ORG_ID
 * Author:liushan
 * Date: 2018/11/26 下午 4:26
 */
public class TblDepartmentInfo/* extends BaseEntity */{
    //private static final long serialVersionUID = -7835749472522858285L;
   /* private Long depId; // 部门流水号
    private String depCode;//	部门代码
    private String depName;//	部门名称
    private String depState;//	1：部门有效 0：注销部门 2：异常部门
    private String orgCode;//	部门归属机构id
    private String optCode;*/

	//无对应
    private String CASHIER;   
  //部门成立日期
    private Date ORG_DATE_OF_REGISTRATION; 
  //名称
    private String INSURER_NAME;
  //状态
    private Long BRANCH_STATUS;
    private String CIRC_BRANCH_CODE;
    private Long AREA_CODE;
    private Date LICENSE_END_DATE;
    private Long TRC_NUM;
    private Date OPERATETIME;
    private String BRANCH_TYPE;
    private String ENG_ADDRESS_NAME;
    private String NATIONALITY_CODE;
    private String LICENSE_NO;
    private String ENG_BRANCH_NAME;
  //部门编码
    private Long NEW_BRANCH_CODE;
    private Long BRANCH_CATEGORY;
    private String LICENSE_BEGIN_DATE;
    private Long BRANCH_LEVEL;
  //上一级部门编码
    private Long ORG_PARENT_ORG_ID;
  //是否有效0无效，1有效
    private Long VALID_STATUS;
  //部门名
    private String ORG_REGISTRATION_NAME;
	public String getCASHIER() {
		return CASHIER;
	}
	public void setCASHIER(String cASHIER) {
		CASHIER = cASHIER;
	}
	public Date getORG_DATE_OF_REGISTRATION() {
		return ORG_DATE_OF_REGISTRATION;
	}
	public void setORG_DATE_OF_REGISTRATION(Date oRG_DATE_OF_REGISTRATION) {
		ORG_DATE_OF_REGISTRATION = oRG_DATE_OF_REGISTRATION;
	}
	public String getINSURER_NAME() {
		return INSURER_NAME;
	}
	public void setINSURER_NAME(String iNSURER_NAME) {
		INSURER_NAME = iNSURER_NAME;
	}
	public Long getBRANCH_STATUS() {
		return BRANCH_STATUS;
	}
	public void setBRANCH_STATUS(Long bRANCH_STATUS) {
		BRANCH_STATUS = bRANCH_STATUS;
	}
	public String getCIRC_BRANCH_CODE() {
		return CIRC_BRANCH_CODE;
	}
	public void setCIRC_BRANCH_CODE(String cIRC_BRANCH_CODE) {
		CIRC_BRANCH_CODE = cIRC_BRANCH_CODE;
	}
	public Long getAREA_CODE() {
		return AREA_CODE;
	}
	public void setAREA_CODE(Long aREA_CODE) {
		AREA_CODE = aREA_CODE;
	}
	public Date getLICENSE_END_DATE() {
		return LICENSE_END_DATE;
	}
	public void setLICENSE_END_DATE(Date lICENSE_END_DATE) {
		LICENSE_END_DATE = lICENSE_END_DATE;
	}
	public Long getTRC_NUM() {
		return TRC_NUM;
	}
	public void setTRC_NUM(Long tRC_NUM) {
		TRC_NUM = tRC_NUM;
	}
	public Date getOPERATETIME() {
		return OPERATETIME;
	}
	public void setOPERATETIME(Date oPERATETIME) {
		OPERATETIME = oPERATETIME;
	}
	public String getBRANCH_TYPE() {
		return BRANCH_TYPE;
	}
	public void setBRANCH_TYPE(String bRANCH_TYPE) {
		BRANCH_TYPE = bRANCH_TYPE;
	}
	public String getENG_ADDRESS_NAME() {
		return ENG_ADDRESS_NAME;
	}
	public void setENG_ADDRESS_NAME(String eNG_ADDRESS_NAME) {
		ENG_ADDRESS_NAME = eNG_ADDRESS_NAME;
	}
	public String getNATIONALITY_CODE() {
		return NATIONALITY_CODE;
	}
	public void setNATIONALITY_CODE(String nATIONALITY_CODE) {
		NATIONALITY_CODE = nATIONALITY_CODE;
	}
	public String getLICENSE_NO() {
		return LICENSE_NO;
	}
	public void setLICENSE_NO(String lICENSE_NO) {
		LICENSE_NO = lICENSE_NO;
	}
	public String getENG_BRANCH_NAME() {
		return ENG_BRANCH_NAME;
	}
	public void setENG_BRANCH_NAME(String eNG_BRANCH_NAME) {
		ENG_BRANCH_NAME = eNG_BRANCH_NAME;
	}
	public Long getNEW_BRANCH_CODE() {
		return NEW_BRANCH_CODE;
	}
	public void setNEW_BRANCH_CODE(Long nEW_BRANCH_CODE) {
		NEW_BRANCH_CODE = nEW_BRANCH_CODE;
	}
	public Long getBRANCH_CATEGORY() {
		return BRANCH_CATEGORY;
	}
	public void setBRANCH_CATEGORY(Long bRANCH_CATEGORY) {
		BRANCH_CATEGORY = bRANCH_CATEGORY;
	}
	public String getLICENSE_BEGIN_DATE() {
		return LICENSE_BEGIN_DATE;
	}
	public void setLICENSE_BEGIN_DATE(String lICENSE_BEGIN_DATE) {
		LICENSE_BEGIN_DATE = lICENSE_BEGIN_DATE;
	}
	public Long getBRANCH_LEVEL() {
		return BRANCH_LEVEL;
	}
	public void setBRANCH_LEVEL(Long bRANCH_LEVEL) {
		BRANCH_LEVEL = bRANCH_LEVEL;
	}
	public Long getORG_PARENT_ORG_ID() {
		return ORG_PARENT_ORG_ID;
	}
	public void setORG_PARENT_ORG_ID(Long oRG_PARENT_ORG_ID) {
		ORG_PARENT_ORG_ID = oRG_PARENT_ORG_ID;
	}
	public Long getVALID_STATUS() {
		return VALID_STATUS;
	}
	public void setVALID_STATUS(Long vALID_STATUS) {
		VALID_STATUS = vALID_STATUS;
	}
	public String getORG_REGISTRATION_NAME() {
		return ORG_REGISTRATION_NAME;
	}
	public void setORG_REGISTRATION_NAME(String oRG_REGISTRATION_NAME) {
		ORG_REGISTRATION_NAME = oRG_REGISTRATION_NAME;
	} 

    
}
