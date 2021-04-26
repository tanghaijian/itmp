package cn.pioneeruniverse.common.utils;

import org.apache.commons.lang.StringEscapeUtils;


public class HTMLDecoderUtil {

    /**
     *  html内容解码
     * @param value
     * @return
     */
    public static String unescapeHtml(String value){
        return StringEscapeUtils.unescapeHtml(value);
    }

    /**
     *  html内容编码
     * @param value
     * @return
     */
    public static String escapeHtml(String value){
        return StringEscapeUtils.escapeHtml(value);
    }


}
