package cn.pioneeruniverse.dev.feignInterface.structure;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.dev.feignFallback.structure.StructureFallback;


@FeignClient(value="devManage",fallbackFactory=StructureFallback.class)
public interface StructureInterface {
	/**
	* @author author
	* @Description 系统条件分页查询
	* @Date 2020/9/16
	* @param pageIndex
	* @param pageSize
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@RequestMapping(value="structure/getAllSystemInfo",method=RequestMethod.POST)
    Map<String,Object> getAllSystemInfo( @RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize);
	
	/**
	* @author author
	* @Description 获取系统模块
	* @Date 2020/9/16
	* @param id
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@RequestMapping(value="structure/getSystemModule",method=RequestMethod.POST)
	Map<String,Object> getSystemModule( @RequestParam("id") Integer id);  

	/**
	* @author author
	* @Description  自动构建（创建job）
	* @Date 2020/9/16
	* @param systemId
	* @param systemName
	* @param serverType
	* @param module
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@RequestMapping(value="structure/creatJenkinsJob",method=RequestMethod.POST)
	Map<String,Object> creatJenkinsJob( @RequestParam("systemId") Integer systemId, @RequestParam("systemName")String systemName,@RequestParam("serverType")String serverType,@RequestParam("module")String[] module);


	
	
}
