package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.entity.TestFirstResultInfo;
import cn.pioneeruniverse.dev.service.deploy.IDeployService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AutomatTestController
 * @Description: 自动化测试平台结果controller
 * @author author
 * @date Sep 7, 2020 14:33:07 AM
 *
 */
@RestController
@RequestMapping(value = "automatTest")
public class AutomatTestController {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IDeployService iDeployService;

    private final static Logger log = LoggerFactory.getLogger(AutomatTestController.class);

    /**
     *@Description 自动化测试平台返回结果
     *@Date 2020/7/21
     *@Param [publishResult] 自动化测试平台测试结果报文内容
     **/
    @RequestMapping(value = "automatedTestingResult", method = RequestMethod.POST)
    public void testResult(@RequestBody String publishResult,HttpServletResponse response) {
        Map<String, Object> head = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        log.info("自动化测试平台返回结果" + publishResult);
        try {
            if (!StringUtils.isBlank(publishResult)) {
                Map<String, Object> data = JSON
                        .parseObject(JSON.parseObject(publishResult).get("requestBody").toString());

                log.info("测试结果报文--------->>>>>>"+JSON.toJSONString(data,true));
                Map<String, Object> antoMap = putSecondMap(data);
                log.info("回调报文--------->>>>>>"+JSON.toJSONString(antoMap,true));
                iDeployService.callBackAutoTest(antoMap);
                removeRedis(antoMap);
                //返回报文
                map.put("consumerSeqNo", "itmgr");
                map.put("status", 0);
                map.put("seqNo", "");
                map.put("providerSeqNo", "");
                map.put("esbCode", "");
                map.put("esbMessage", "");
                map.put("appCode", "0");
                map.put("appMessage", "");
                head.put("responseHead", map);

                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(head));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("自动化测试平台返回信息处理失败" + ":" + e.getMessage(), e);
        }
    }

    /**
     * 
    * @Title: putSecondMap
    * @Description: 为部署回调组装参数
    * @author author
    * @param resultMap 自动化测试平台返回的结果
    * @return Map<String, Object>
     */
    public Map<String, Object> putSecondMap(Map<String, Object> resultMap) {
        TestFirstResultInfo testFirstResultInfo=JSON.parseObject(JSON.toJSONString(resultMap), TestFirstResultInfo.class);
        Map<String, Object> map = new HashMap<>();
       //组装测试结果tbl_system_automatic_test_result
        if(testFirstResultInfo.getTestRequestNumber()!=null && !testFirstResultInfo.getTestRequestNumber().equals("")){
            map=(Map<String, Object>)redisUtils.get(testFirstResultInfo.getTestRequestNumber());
            //测试类型：1:API自动化测试，2:UI自动化测试）
            map.put("testType",testFirstResultInfo.getTestType());
            //测试申请单号
            map.put("testRequestNumber",testFirstResultInfo.getTestRequestNumber());
            //成功数目
            map.put("successNumber",testFirstResultInfo.getSuccessNumber());
            //失败数目
            map.put("failedNumber",testFirstResultInfo.getFailedNumber());
            //测试结果（1:通过，2:未通过）
            map.put("testResult",testFirstResultInfo.getTestResult());
            //测试结果链接
            map.put("testResultDetailUrl",testFirstResultInfo.getTestResultDetailUrl());
        }else{
            TestFirstResultInfo t=testFirstResultInfo.getSubSystemInfo().get(0);
            map=(Map<String, Object>)redisUtils.get(t.getSubTestRequestNumber());
            Map<String, Object> testConfig = JSON.parseObject(JSON.toJSONString(map.get("testConfig")));
            List<Map<String, Object>> listMaps = (List<Map<String, Object>>)testConfig.get("subSystemInfo");

            //组装子系统测试结果
            for (Map<String, Object> listMap:listMaps){
            	//子系统测试模块结果
                if(t.getSubSystemCode().equals(listMap.get("subSystemCode").toString())){
                    listMap.put("subTestRequestNumber",t.getSubTestRequestNumber());
                    listMap.put("subSuccessNumber",t.getSubSuccessNumber());
                    listMap.put("subFailedNumber",t.getSubFailedNumber());
                    listMap.put("subTestResult",t.getSubTestResult());
                    listMap.put("subTestResultDetailUrl",t.getSubTestResultDetailUrl());
                }
            }
            testConfig.put("subSystemInfo",listMaps);
          //测试配置tbl_system_automatic_test_config
            map.put("testConfig",testConfig);
            map.put("testType",testFirstResultInfo.getTestType());
            map.put("testRequestNumber",testFirstResultInfo.getTestRequestNumber());
            map.put("successNumber",testFirstResultInfo.getSuccessNumber());
            map.put("failedNumber",testFirstResultInfo.getFailedNumber());
            map.put("testResult",testFirstResultInfo.getTestResult());
            map.put("testResultDetailUrl",testFirstResultInfo.getTestResultDetailUrl());
        }
        return map;
    }

    /**
     * 
    * @Title: removeRedis
    * @Description: 从redis中移出相关的测试结果，在发送到测试平台前，系统会把测试的相关信息放于redis中，测试结果返回后移出相关信息
    * @author author
    * @param map key:testRequestNumber 测试请求编号
    *                testConfig  测试配置
    *                
     */
    public void removeRedis(Map<String, Object> map) {
        if(!map.get("testRequestNumber").toString().equals("")){
            redisUtils.remove(map.get("testRequestNumber").toString());
        }else{
            Map<String, Object> testConfig = JSON.parseObject(map.get("testConfig").toString());
            List<Map<String, Object>> listMaps = (List<Map<String, Object>>)testConfig.get("subSystemInfo");
            for (Map<String, Object> listMap:listMaps){
                if(listMap.get("subTestRequestNumber")!=null&&!listMap.get("subTestRequestNumber").toString().equals("")){
                    redisUtils.remove(listMap.get("subTestRequestNumber").toString());
                }
            }

        }
    }
    //获取redist信息
    /*@RequestMapping(value = "getRedisConfig", method = RequestMethod.GET)
    public HashMap<Object,Object> getRedisConfig(){
        Set<String> keys = redisUtils.getKeys("");

        HashMap<Object,Object> map = new HashMap<>();
        int i=0;
        for (String key:keys){
            //redisUtils.remove(key);
            System.out.println(i+">>"+redisUtils.getType(key)+":      "+key);
            Object value = redisUtils.get(key);
            map.put(key,value);
            i++;
        }
        return map;
    }*/
}
