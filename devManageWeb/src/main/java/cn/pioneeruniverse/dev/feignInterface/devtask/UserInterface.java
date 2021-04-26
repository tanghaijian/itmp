package cn.pioneeruniverse.dev.feignInterface.devtask;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.pioneeruniverse.dev.entity.TblDeptInfo;
import cn.pioneeruniverse.dev.entity.TblUserInfo;
import cn.pioneeruniverse.dev.feignFallback.devtask.UserFallback;



@FeignClient(value="system",fallbackFactory = UserFallback.class)
public interface UserInterface {

	/**
	* @author author
	* @Description 查询部门
	* @Date 2020/9/4
	* @param 
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblDeptInfo>
	**/
	@RequestMapping(value="user/getDept",method=RequestMethod.POST)
	List<TblDeptInfo> getDept();
	
	
	
}
