package cn.pioneeruniverse.common.message.email.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import cn.pioneeruniverse.common.message.email.vo.MailSenderInfoVO;
import cn.pioneeruniverse.common.message.email.vo.MyAuthenticatorVO;

public class SimpleMailSender {

    /**
     * 以文本格式发送邮件
     *
     * @param mailInfo 待发送的邮件的信息
     */
    public boolean sendTextMail(MailSenderInfoVO mailInfo) throws Exception{
        // 判断是否需要身份认证
        MyAuthenticatorVO authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticatorVO(mailInfo.getUserName(),
                    mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session
                .getDefaultInstance(pro, authenticator);
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 收件人
            String to = mailInfo.getToAddress();
            InternetAddress[] iaToList = new InternetAddress().parse(to);
            mailMessage.setRecipients(Message.RecipientType.TO, iaToList);
            // 抄送人
            if(mailInfo.getCcAddress() != null){
                String cc = mailInfo.getCcAddress();
                InternetAddress[] iaToListcs = new InternetAddress().parse(cc);
                mailMessage.setRecipients(Message.RecipientType.CC, iaToListcs);
            }
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
            //mailMessage.setText(mailContent);
            //指定邮箱内容及ContentType和编码方式
            mailMessage.setContent(mailContent, "text/html;charset=utf-8");
            // 发送邮件
            Transport.send(mailMessage);
            return true;
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mailInfo 待发送的邮件信息
     * @throws Exception 
     */
    public boolean sendHtmlMail(MailSenderInfoVO mailInfo) throws Exception {
        // 判断是否需要身份认证
        MyAuthenticatorVO authenticator = null;
        Properties pro = mailInfo.getProperties();
        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticatorVO(mailInfo.getUserName(),
                    mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session
                .getDefaultInstance(pro, authenticator);
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 收件人
            if(mailInfo.getToAddress()!=null){
                String to = mailInfo.getToAddress();
                InternetAddress[] iaToList = new InternetAddress().parse(to);
                mailMessage.setRecipients(Message.RecipientType.TO, iaToList);
            }
            // 抄送人
            if(mailInfo.getCcAddress() != null){
                String cc = mailInfo.getCcAddress();
                InternetAddress[] iaToListcs = new InternetAddress().parse(cc);
                mailMessage.setRecipients(Message.RecipientType.CC, iaToListcs);
            }
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
    }

}