package cn.pioneeruniverse.dev.feignFallback.devtask;


import java.util.List;

import org.springframework.stereotype.Component;

import cn.pioneeruniverse.dev.entity.TblDeptInfo;
import cn.pioneeruniverse.dev.entity.TblUserInfo;
import cn.pioneeruniverse.dev.feignInterface.devtask.UserInterface;
import feign.hystrix.FallbackFactory;


@Component
public class UserFallback  implements FallbackFactory<UserInterface>{

	@Override
	public UserInterface create(Throwable cause) {
		// TODO Auto-generated method stub
		return new UserInterface() {
			
			@Override
			public List<TblDeptInfo> getDept() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	




	 
	
	
	
}
