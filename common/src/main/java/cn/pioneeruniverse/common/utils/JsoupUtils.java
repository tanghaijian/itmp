package cn.pioneeruniverse.common.utils;

import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 17:46 2019/5/15
 * @Modified By:
 */
public class JsoupUtils {
    /**
     * 使用自带的basicWithImages 白名单
     * 允许的便签有a,b,blockquote,br,cite,code,dd,dl,dt,em,i,li,ol,p,pre,q,small,span,
     * strike,strong,sub,sup,u,ul,img
     * 以及a标签的href,img标签的src,align,alt,height,width,title属性
     */
    private static final Whitelist whitelist = Whitelist.basicWithImages();
    /**
     * 配置过滤化参数,不对代码进行格式化
     */
    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

    static {
        whitelist.removeAttributes("a", "href");
        whitelist.removeAttributes("img", "src");
    }

    public static String cleanForXssHttpServletRequest(String content) {
        return Jsoup.clean(content, "", whitelist, outputSettings);
    }
    
    
    public static String cleanForXssJson(String content) {
    	try {
    		return Jsoup.clean(content,"",whitelist, outputSettings);
    	}catch(Exception e) {
    		return content;
    	}
    }
    
//    public static void main(String args[]) {
//    	
//    	String html = "{\"realname\":\"xxx\",\"username\":\"<script>alert(1);</script>\"}";
//    	
//    	System.out.println(JsoupUtils.cleanForXssHttpServletRequest(html));
//    }

}
