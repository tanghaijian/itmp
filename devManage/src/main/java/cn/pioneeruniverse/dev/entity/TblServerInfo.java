package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
/**
 *
 * @ClassName: TblServerInfo
 * @Description: 服务实体类
 * @author author
 *
 */
@TableName("tbl_server_info")
public class TblServerInfo extends BaseEntity {

	private static final long serialVersionUID = 6875852489685419330L;

    private Long systemId;//系统id

	private String hostName;//主机名称

    private String ip;//ip
    
    private Long sshPort;//端口

    private String sshUserAccount;//用户名

    private String sshUserPassword;//密码

    @TableField(exist = false)
    private String systemName;//系统名称

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName == null ? null : hostName.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }
    
    public Long getSshPort() {
		return sshPort;
	}

	public void setSshPort(Long sshPort) {
		this.sshPort = sshPort;
	}

	public String getSshUserAccount() {
        return sshUserAccount;
    }

    public void setSshUserAccount(String sshUserAccount) {
        this.sshUserAccount = sshUserAccount == null ? null : sshUserAccount.trim();
    }

    public String getSshUserPassword() {
        return sshUserPassword;
    }

    public void setSshUserPassword(String sshUserPassword) {
        this.sshUserPassword = sshUserPassword == null ? null : sshUserPassword.trim();
    }

}