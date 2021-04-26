package cn.pioneeruniverse.dev.feignInterface.tool;

import cn.pioneeruniverse.dev.entity.TblToolInfo;
import cn.pioneeruniverse.dev.feignFallback.tool.ToolFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2018/10/30
 * Time: 上午 9:28
 */
@FeignClient(value="devManage",fallbackFactory=ToolFallback.class)
public interface ToolInterface {
   /* @RequestMapping(value = "tool/list",method = RequestMethod.POST)
     Map<String,Object> findList();

    @RequestMapping(value = "tool/update",method = RequestMethod.POST)
    Map<String,Object> updateTool(@RequestBody TblToolInfo tblToolInfo);*/
}
