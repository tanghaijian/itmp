package cn.pioneeruniverse.project.feignInterface;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.project.feignFallback.ProjectToSystemFallback;

/**
 * 
* @ClassName: ProjectToSystemInterface
* @Description: 项目和系统模块关联调用
* @author author
* @date 2020年8月13日 上午10:25:35
*
 */
@FeignClient(value = "system", fallbackFactory = ProjectToSystemFallback.class)
public interface ProjectToSystemInterface {

	/**
	 * 
	* @Title: insertMessage
	* @Description: 新增消息，新增，更新需求时
	* @author author
	* @param messageJson tblMessageInfo 的json格式
	* @return map key status:1正常，2异常
	* @throws
	 */
    @RequestMapping(value = "/message/insertMessage", method = RequestMethod.POST)
    Map<String, String> insertMessage(@RequestParam("messageJson") String messageJson);

    /**
     * 
    * @Title: findFieldByTableName
    * @Description: 通过表名获取自定义属性，新增/编辑需求
    * @author author
    * @param tableName 表名
    * @return map key: type-->map1 数据字典TBL_CUSTOM_FIELD_TEMPLATE_TYPE ，fieldTemp-->fieldTemp:数据库默认字段
    * @throws
     */
    @RequestMapping(value = "/fieldTemplate/findFieldByTableName", method = RequestMethod.POST)
    Map<String, Object> findFieldByTableName(@RequestParam("tableName") String tableName);

    /**
     * 
    * @Title: sendMessage
    * @Description: 发送邮件或微信时用
    * @author author
    * @param messageJson tblMessageInfo 的json格式
    * @return map key status:1正常，2异常
    * @throws
     */
    @RequestMapping(value = "/message/sendMessage", method = RequestMethod.POST)
    Map<String, String> sendMessage(@RequestParam("messageJson") String messageJson);

    /**
     * 
    * @Title: getUserInfoByUserIds
    * @Description: 通过选择的用户，获取用户信息(未用)
    * @author author
    * @param userIds 用户id
    * @return List<TblUserInfoDTO>的json格式
    * @throws
     */
    @RequestMapping(value = "/user/getUserInfoByUserIds", method = RequestMethod.POST)
    List<Map<String, Object>> getUserInfoByUserIds(@RequestParam("userIds") List<Long> userIds);


}
