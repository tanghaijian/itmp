package cn.pioneeruniverse.system.vo.user;
import java.util.Date;
import cn.pioneeruniverse.system.vo.BaseVo;
  
/**
 * @deprecated
* @ClassName: ExcelUser
* @Description: 人员导出excel所需要的人员vo
* @author author
* @date 2020年9月4日 上午10:49:34
*
 */
public class ExcelUser  extends BaseVo{
	//姓名
	private String userName;
	//账号
	private String userAccount;
	//公司名
	private String companyName;
	//部门名
	private String deptName;
	//用户状态
	private String userStatus;
	//角色名
	private String roleName;
	//入职日期
	private Date entryDate;
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
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	

}
