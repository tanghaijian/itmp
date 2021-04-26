package cn.pioneeruniverse.common.message.email.utils;


import cn.pioneeruniverse.common.message.email.vo.EmailVO;
import cn.pioneeruniverse.common.message.email.vo.MailSenderInfoVO;

/**
 * 邮件发送工具类
 */
public class SendEmailUtils {
	
    private static SendEmailUtils instance = null;

    private MailSenderInfoVO mailInfo = new MailSenderInfoVO();// 这个类主要是设置邮件
    private SimpleMailSender sms = new SimpleMailSender();// 这个类主要来发送邮件

    public static SendEmailUtils getInstance(String mailAddress,String mailPort,String userName,String fromAddress) {
        if (instance == null) {
            instance = new SendEmailUtils(mailAddress,mailPort,userName,fromAddress);
        }
        return instance;
    }

    /**
     * 初始化对象方法
     */
    public SendEmailUtils(String mailAddress,String mailPort,String userName,String fromAddress) {
        mailInfo.setMailServerHost(mailAddress);
        mailInfo.setMailServerPort(mailPort);
        mailInfo.setValidate(false);
        mailInfo.setUserName(userName);
        mailInfo.setPassword("");// 您的邮箱密码
        mailInfo.setFromAddress(fromAddress);
    }
    
    /**
     * 发送
     * @param emailVo
     * @return
     * @throws Exception
     */
    public boolean sendEmail(EmailVO emailVo) throws Exception {
    	Boolean flag = null;
    	if(emailVo != null){
    		mailInfo.setToAddress(emailVo.getToAddress());
    		if(!("".equals(emailVo.getCcAddress()))){
    			mailInfo.setCcAddress(emailVo.getCcAddress());
    		}
    		mailInfo.setSubject(emailVo.getSubject());
    		mailInfo.setContent(emailVo.getContent());
    		flag =  sms.sendHtmlMail(mailInfo);// 发送文体格式
    	}
    	return flag;
    }

   
}
