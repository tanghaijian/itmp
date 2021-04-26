package cn.pioneeruniverse.dev.feignFallback;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.dev.entity.TblDeptInfo;
import org.springframework.stereotype.Component;

import cn.pioneeruniverse.dev.feignInterface.TestManageWebToSystemInterface;
import feign.hystrix.FallbackFactory;

/**
*  类说明 
* @author:tingting
* @version:2019年3月8日 下午3:22:21 
*/
@Component
public class TestManageWebToSystemFallBack implements FallbackFactory<TestManageWebToSystemInterface>{

	@Override
	public TestManageWebToSystemInterface create(Throwable cause) {
		return new TestManageWebToSystemInterface() {

			@Override
			public Map<String, Object> getMenuByCode(String menuButtonCode) {
				return null;
			}

			@Override
			public List<TblDeptInfo> getDept() {
				return null;
			}


		};
	}

	

}
