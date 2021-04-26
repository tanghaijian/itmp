package cn.pioneeruniverse.common.bean;

import cn.pioneeruniverse.common.annotion.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 动态数据源切换AOP
 * @Date: Created in 10:16 2019/1/22
 * @Modified By:
 */
@Aspect
public class MultiSourceExAop implements Ordered {

    private Logger log = LoggerFactory.getLogger(MultiSourceExAop.class);

    @Resource(name = "itmpDruid")
    DruidProperties itmpDruidProperties;

    @Pointcut(value = "@annotation(cn.pioneeruniverse.common.annotion.DataSource)")
    private void cut() {
    }

    @Around("cut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = null;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        methodSignature = (MethodSignature) signature;

        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        DataSource datasource = currentMethod.getAnnotation(DataSource.class);
        if (datasource != null) {
            DataSourceContextHolder.setDataSourceType(datasource.name());
            log.debug("设置数据源为：" + datasource.name());
        } else {
            //未用@DataSource标记的方法默认使用测试管理平台数据源
            DataSourceContextHolder.setDataSourceType(itmpDruidProperties.getDataSourceName());
            log.debug("设置数据源为：" + itmpDruidProperties.getDataSourceName());
        }
        try {
            return point.proceed();
        } finally {
            log.debug("清空数据源信息！");
            DataSourceContextHolder.clearDataSourceType();
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
