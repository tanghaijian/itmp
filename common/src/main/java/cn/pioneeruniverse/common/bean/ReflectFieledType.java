package cn.pioneeruniverse.common.bean;

import java.util.Arrays;
import java.util.List;

/**
 * Description: 反射对比的字段类型
 * 存放需要添加日志的字段类型
 * Author:liushan
 * Date: 2018/12/21 下午 3:03
 */
public class ReflectFieledType {
    public static final List<String> listType = Arrays.asList("String","Number","long","Long","Integer","int","BigDecimal",
            "char","byte","boolean","Date","DateTime","Timestamp","Boolean","List","Double","Set");
}
