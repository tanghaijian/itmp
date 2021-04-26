package cn.pioneeruniverse.dev.feignInterface;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.dev.feignFallback.TestManageWebToTestManageFallback;


/**
 * @Author:
 * @Description: testManagemWeb模块请求testManage模块微服务接口
 * @Date: Created in 15:06 2020/08/27
 * @Modified By:
 */
@FeignClient(value="testManage",fallbackFactory= TestManageWebToTestManageFallback.class)
public interface TestManageWebToTestManageInterface {

	/**
	 * @param
	 * @return java.util.Map<String, Object>
	 * @Description 添加
	 * @MethodName toAddData
	 * @author
	 * @Date 2020/08/27 15:17
	 */
	@RequestMapping(value="testtask/toAddData",method=RequestMethod.POST)
    Map<String, Object> toAddData();

	/**
	 * @param
	 * @return java.util.Map<String, Object>
	 * @Description 根据ID获取测试任务信息
	 * @MethodName getOneDevTask
	 * @author
	 * @param id 测试任务ID
	 * @Date 2020/08/27 15:17
	 */
	@RequestMapping(value="testtask/getOneTestTask3",method=RequestMethod.POST)
	Map<String, Object> getOneDevTask(@RequestParam("id") Long id);


}
