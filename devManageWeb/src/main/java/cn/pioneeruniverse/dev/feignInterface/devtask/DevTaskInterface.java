package cn.pioneeruniverse.dev.feignInterface.devtask;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.dev.feignFallback.devtask.DevTaskFallback;


/**
*  类说明 
* @author:tingting
* @version:2018年11月13日 下午3:35:10 
*/
@FeignClient(value="devManage",fallbackFactory=DevTaskFallback.class)
public interface DevTaskInterface {
	
	@RequestMapping(value="devtask/toAddData",method=RequestMethod.POST)
    Map<String, Object> toAddData();
	@RequestMapping(value="devtask/getOneDevTask3",method=RequestMethod.POST)
	Map<String, Object> getOneDevTask(@RequestParam("id") Long id);


}
