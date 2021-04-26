package cn.pioneeruniverse.dev.feignInterface;

import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.feignFallback.TestManageToSystemFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "system", fallbackFactory = TestManageToSystemFallback.class)
public interface TestManageToSystemInterface {

    /**
    * @author author
    * @Description
    * @Date 2020/9/7
    * @param tableName
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value = "/fieldTemplate/findFieldByTableName", method = RequestMethod.POST)
    Map<String,Object> findFieldByTableName(@RequestParam("tableName") String tableName);

    /**
    * @author author
    * @Description 新增信息
    * @Date 2020/9/7
    * @param messageJson
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value = "/message/insertMessage", method = RequestMethod.POST)
    Map<String, Object> insertMessage( @RequestParam("messageJson") String messageJson);

    /**
    * @author author
    * @Description 发送信息
    * @Date 2020/9/7
    * @param messageJson
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @RequestMapping(value = "/message/sendMessage", method = RequestMethod.POST)
    Map<String, Object> sendMessage(@RequestParam("messageJson") String messageJson);

    @RequestMapping(value = "/dataDic/getDataDicByTermCode",method = RequestMethod.POST)
    List<TblDataDic> getDataDicByTermCode(@RequestParam("termCode") String termCode);
}
