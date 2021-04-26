package cn.pioneeruniverse.common.message.email.vo;

import java.util.Properties;

public class MailSenderInfoVO {

    private String mailServerHost; // 发送邮件的服务器的IP和端口
    private String mailServerPort = "25"; // 端口
    private String fromAddress; // 邮件发送者的地址
    private String userName; // 登陆邮件发送服务器的用户名和密码
    private String password;
    private boolean validate = false;// 是否需要身份验证
    private String toAddress;// 邮件接收人的地址
    private String ccAddress;  // 邮件抄送人地址
    private String subject;  // 邮件主题
    private String content;// 邮件的文本内容

    //获得邮件会话属性
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        return p;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }


    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String textContent) {
        this.content = textContent;
    }

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}

	public MailSenderInfoVO(String mailServerHost, String mailServerPort,
			String fromAddress, String userName, String password,
			boolean validate, String toAddress, String ccAddress,
			String subject, String content) {
		super();
		this.mailServerHost = mailServerHost;
		this.mailServerPort = mailServerPort;
		this.fromAddress = fromAddress;
		this.userName = userName;
		this.password = password;
		this.validate = validate;
		this.toAddress = toAddress;
		this.ccAddress = ccAddress;
		this.subject = subject;
		this.content = content;
	}

	public MailSenderInfoVO() {
		super();
	}
	
    
}
