package cn.pioneeruniverse.common.message.email.vo;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
* @author author
* @Description 确定人信息
* @Date 2020/9/14
* @return
**/
public class MyAuthenticatorVO extends Authenticator{   
    String userName=null;   
    String password=null;   
        
    public MyAuthenticatorVO(){   
    }   
    public MyAuthenticatorVO(String username, String password) {    
        this.userName = username;    
        this.password = password;    
    }    
    protected PasswordAuthentication getPasswordAuthentication(){   
        return new PasswordAuthentication(userName, password);   
    }   
}   
