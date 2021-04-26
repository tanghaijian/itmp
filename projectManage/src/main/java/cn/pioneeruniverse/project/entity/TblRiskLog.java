package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.utils.CommonUtil;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
/**
 *@author liushan
 *@Description  风险日志
 *@Date 2020/8/7
 *@return
 **/
@TableName("tbl_risk_log")
public class TblRiskLog extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private Long riskId;// 风险主键
	
	private String logType;// 日志类型
	
	private String logDetail;// 日志明细
	
	private Long userId;// 用户表主键
	
	private String userName;// 用户姓名
	
	private String userAccount;// 用户登录账号

	public TblRiskLog(){}

	public TblRiskLog(Long riskId,String logType,String logDetail,HttpServletRequest request){
		Long userId = CommonUtil.getCurrentUserId(request);
		this.setRiskId(riskId);
		this.setLogType(logType);
		this.setLogDetail(logDetail);
		this.setUserId(userId);
		this.setUserName(CommonUtil.getCurrentUserName(request));
		this.setUserAccount(CommonUtil.getCurrentUserAccount(request));
		this.setStatus(1);
		this.setCreateBy(userId);
		this.setCreateDate(new Timestamp(new Date().getTime()));
		this.setLastUpdateBy(userId);
		this.setLastUpdateDate(new Timestamp(new Date().getTime()));
	}

	public Long getRiskId() {
		return riskId;
	}

	public void setRiskId(Long riskId) {
		this.riskId = riskId;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getLogDetail() {
		return logDetail;
	}

	public void setLogDetail(String logDetail) {
		this.logDetail = logDetail;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	
	
}
