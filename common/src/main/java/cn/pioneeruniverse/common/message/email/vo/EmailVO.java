package cn.pioneeruniverse.common.message.email.vo;
/**
 *
 * @ClassName:EmailVO
 * @Description
 * @author author
 * @date 2020年8月26日
 *
 */
public class EmailVO {
	  
    private String toAddress;// 邮件接收人的地址
    private String ccAddress;  // 邮件抄送人地址
    private String subject;  // 邮件主题
    private String content; // 邮件的文本内容
    
    
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public EmailVO(String toAddress, String ccAddress, String subject,
			String content) {
		super();
		this.toAddress = toAddress;
		this.ccAddress = ccAddress;
		this.subject = subject;
		this.content = content;
	}
	public EmailVO() {
		super();
	}
    
	

}
