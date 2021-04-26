package cn.pioneeruniverse.dev.feignFallback.systeminfo;

import org.springframework.stereotype.Component;

import cn.pioneeruniverse.dev.feignInterface.systeminfo.SystemInfoInterface;
import feign.hystrix.FallbackFactory;

/**
*  类说明 
* @author:tingting
* @version:2018年10月30日 下午5:27:42 
*/
@Component
public class SystemInfoFallback implements FallbackFactory<SystemInfoInterface>{

	@Override
	public SystemInfoInterface create(Throwable cause) {
		// TODO Auto-generated method stub
		return new SystemInfoInterface() {
			
		

			
		};
	}

}
