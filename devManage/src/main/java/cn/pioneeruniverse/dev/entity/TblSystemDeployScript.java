package cn.pioneeruniverse.dev.entity;

import com.github.pagehelper.StringUtil;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName: TblSystemDeployScript
 * @Description: 部署脚本类
 * @author author
 * @date 2020年8月12日 下午16:20
 *
 */
public class TblSystemDeployScript extends BaseEntity{

	private static final long serialVersionUID = 2342206953167986666L;

	private Long systemDeployId;//系统部署ID

    private Integer stepOrder;//脚本顺序

    private Byte operateType;//操作类型

    private String userName;//用户名

    private String password;//密码

    private Integer waitTime;//等待时长

    private String script;//脚本

	public Long getSystemDeployId() {
		return systemDeployId;
	}

	public void setSystemDeployId(Long systemDeployId) {
		this.systemDeployId = systemDeployId;
	}

	public Integer getStepOrder() {
		return stepOrder;
	}

	public void setStepOrder(Integer stepOrder) {
		this.stepOrder = stepOrder;
	}

	public Byte getOperateType() {
		return operateType;
	}

	public void setOperateType(Byte operateType) {
		this.operateType = operateType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Integer waitTime) {
		this.waitTime = waitTime;
	}

	public String getScript() {
		String scriptTemp = script;
		if (StringUtil.isNotEmpty(script)) {
			scriptTemp = script.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
		}
		return scriptTemp;
	}

	public void setScript(String script) {
		this.script = script;
	}
    
    
}