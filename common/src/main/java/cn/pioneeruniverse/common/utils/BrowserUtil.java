package cn.pioneeruniverse.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
*  类说明 
* @author:tingting
* @version:2019年1月2日 下午12:01:19 
*/
public class BrowserUtil {
	//判断是否是IE浏览器   
    public static boolean isMSBrowser(HttpServletRequest request) {  
        String[] IEBrowserSignals = {"MSIE", "Trident", "Edge"};  
        String userAgent = request.getHeader("User-Agent");  
        for (String signal : IEBrowserSignals) {              
        	if (userAgent.contains(signal)){  
                return true;  
            }  
        }  
        return false;  
    }
}
