package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblUserInfo
* @Description: 用户实体类
* @author author
* @date 2020年7月31日 下午7:27:12
*
 */
@TableName("tbl_user_info")
public class TblUserInfo extends BaseEntity  {

	  private static final long serialVersionUID = -1421203092465310527L;
	  //用户账号
	    private String userAccount;
	  //用户密码
	    private String userPassword;
	    //用户姓名
	    private String userName;
	  //工号
	    private String employeeNumber;
	  //邮箱
	    private String email;
	    //性别
	    private String gender;
	  //生日
	    private Date birthday;
	    //用户类型：（1:内部人员，2:外部人员）
	    private Integer userType;
	    //公司
	    private Long companyId;
	  //入职日期
	    private Date entryDate;
	  //离职日期
	    private Date leaveDate;
	    //部门ID
	    private Long deptId;
	    //用户状态（1：正常、2：无效）
	    private Integer userStatus;
	  //是否允许访问（主要是判断是否已经修改过密码0：未，1：是）
	    private Integer isAllowed;
	  //仓库密码
	    private String userScmPassword;
	  //仓库账号
	    private String userScmAccount;
		public String getUserAccount() {
			return userAccount;
		}
		public void setUserAccount(String userAccount) {
			this.userAccount = userAccount;
		}
		public String getUserPassword() {
			return userPassword;
		}
		public void setUserPassword(String userPassword) {
			this.userPassword = userPassword;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getEmployeeNumber() {
			return employeeNumber;
		}
		public void setEmployeeNumber(String employeeNumber) {
			this.employeeNumber = employeeNumber;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public Date getBirthday() {
			return birthday;
		}
		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}
		public Integer getUserType() {
			return userType;
		}
		public void setUserType(Integer userType) {
			this.userType = userType;
		}
		public Long getCompanyId() {
			return companyId;
		}
		public void setCompanyId(Long companyId) {
			this.companyId = companyId;
		}
		public Date getEntryDate() {
			return entryDate;
		}
		public void setEntryDate(Date entryDate) {
			this.entryDate = entryDate;
		}
		public Date getLeaveDate() {
			return leaveDate;
		}
		public void setLeaveDate(Date leaveDate) {
			this.leaveDate = leaveDate;
		}
		public Long getDeptId() {
			return deptId;
		}
		public void setDeptId(Long deptId) {
			this.deptId = deptId;
		}
		public Integer getUserStatus() {
			return userStatus;
		}
		public void setUserStatus(Integer userStatus) {
			this.userStatus = userStatus;
		}
		public Integer getIsAllowed() {
			return isAllowed;
		}
		public void setIsAllowed(Integer isAllowed) {
			this.isAllowed = isAllowed;
		}
		public String getUserScmPassword() {
			return userScmPassword;
		}
		public void setUserScmPassword(String userScmPassword) {
			this.userScmPassword = userScmPassword;
		}
	    public String getUserScmAccount() {
		 return userScmAccount;
	    }

	    public void setUserScmAccount(String userScmAccount) {
		  this.userScmAccount = userScmAccount;
	     }
	    
}
