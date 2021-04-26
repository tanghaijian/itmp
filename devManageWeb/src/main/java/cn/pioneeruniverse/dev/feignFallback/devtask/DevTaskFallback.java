package cn.pioneeruniverse.dev.feignFallback.devtask;

import java.util.Map;

import org.springframework.stereotype.Component;

import cn.pioneeruniverse.dev.feignInterface.devtask.DevTaskInterface;
import feign.hystrix.FallbackFactory;

/**
*  类说明 
* @author:tingting
* @version:2018年11月13日 下午3:22:21 
*/
@Component
public class DevTaskFallback implements FallbackFactory<DevTaskInterface>{

	@Override
	public DevTaskInterface create(Throwable cause) {
		// TODO Auto-generated method stub
		return new DevTaskInterface() {
			
			@Override
			public Map<String, Object> toAddData() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<String, Object> getOneDevTask(Long id) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	

}
