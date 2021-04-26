package cn.pioneeruniverse.common.bean;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Description: 反射取到字段对应的中文名称
 * Author:liushan
 * Date: 2018/12/20 上午 9:16
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface PropertyInfo {

    /**
     * 字段中文名称
     **/
    String name() default "";

    /**
     * 字段规则类型
     **/
    String ruleType() default "text";

    /**
     * 精密度；精确
     **/
    int precision() default 0;

    /**
     * 规定字段的长度
     **/
    int length() default 0;

    /**
     * 提示
     **/
    String hint() default "";

    /**
     *@author liushan
     *@Description 数据字典
     *@Date 2019/12/20
     *@Param [] 数据字典编码
     *@return java.lang.String
     **/
    String dicTermCode() default "";

    /**
     * (实体类必填项)反射的时候，该字段需参不参与反射，
     * 默认true，参与反射，结果集出现该字段
     * false：则不参与反射，结果集不出现该字段
     **/
    boolean isFieldShow() default true;

    /**
     * 该字段参与反射，但是会有默认值，
     * 默认true:正常反射操作，
     * false:反射时，结果集保存，该字段名称旧数据，该字段名称旧数据，适用于字段数据较大，不方便存储数据库的字段
     **/
    boolean isDefaultValue() default true;
}
