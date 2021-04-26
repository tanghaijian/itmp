package cn.pioneeruniverse.common.annotion;

import java.lang.annotation.*;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:数据源标识
 * @Date: Created in 10:37 2019/1/22
 * @Modified By:
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataSource {
    String name() default "";
}
