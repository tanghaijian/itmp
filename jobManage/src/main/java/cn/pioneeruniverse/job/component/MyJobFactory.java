package cn.pioneeruniverse.job.component;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * 
* @ClassName: MyJobFactory
* @Description: quartz 的job工厂，创建Job实例
* @author author
* @date 2020年8月24日 上午9:43:00
*
 */
@Component 
public class MyJobFactory extends AdaptableJobFactory {
	
    @Autowired  
    private AutowireCapableBeanFactory capableBeanFactory;  
     
    @Override  
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {  
      Object jobInstance = super.createJobInstance(bundle);  
      capableBeanFactory.autowireBean(jobInstance); 
      return jobInstance;  
    }  
}
