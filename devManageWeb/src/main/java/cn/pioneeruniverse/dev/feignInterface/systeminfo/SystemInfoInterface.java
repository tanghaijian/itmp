package cn.pioneeruniverse.dev.feignInterface.systeminfo;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.feignFallback.systeminfo.SystemInfoFallback;


/**
*  类说明 
* @author:tingting
* @version:2018年10月30日 下午3:35:10 
*/
@FeignClient(value="devManage",fallbackFactory=SystemInfoFallback.class)
public interface SystemInfoInterface {
	
}
