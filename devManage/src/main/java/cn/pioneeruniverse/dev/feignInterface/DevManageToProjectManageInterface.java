package cn.pioneeruniverse.dev.feignInterface;

import cn.pioneeruniverse.common.dto.SystemTreeVo;
import cn.pioneeruniverse.dev.feignFallback.DevManageToProjectManageFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 
* @ClassName: DevManageToProjectManageInterface
* @Description: devManage模块和ProjectManage模块通信
* @author author
* @date 2020年8月11日 下午9:54:25
*
 */
@FeignClient(value = "projectManage", fallbackFactory = DevManageToProjectManageFallback.class)
public interface DevManageToProjectManageInterface {

	/**
	 * 
	* @Title: insertFirstSystem
	* @Description: 插入第一层系统树
	* @author author
	* @param systemTreeVo 系统树信息
	* @param currentUserId 当前用户
	* @return map key status=1 正常，2异常
	 */
    @RequestMapping(value = "/systemTree/insertFirstSystem", method = RequestMethod.POST)
    Map<String, Object> insertFirstSystem(@RequestBody SystemTreeVo systemTreeVo,
                                          @RequestParam("currentUserId") Long currentUserId);

    /**
     * 
    * @Title: getProjectGroupByProjectGroupId
    * @Description: 通过项目小组ID获取项目小组
    * @author author
    * @param projectGroupId 项目小组ID
    * @return map key status=1正常，2=异常
    *                 data 小组信息
     */
    @RequestMapping(value = "/project/getProjectGroupByProjectGroupId", method = RequestMethod.POST)
    Map<String, Object> getProjectGroupByProjectGroupId(@RequestParam("projectGroupId") Long projectGroupId);

    /**
     * 
    * @Title: getAssetSystemTreeById
    * @Description: 通过ID获取资产系统树
    * @author author
    * @param id
    * @return TblAssetSystemTree的map形式，即字段-value
     */
    @RequestMapping(value = "/systemTree/getAssetSystemTreeById", method = RequestMethod.POST)
    Map<String, Object> getAssetSystemTreeById(@RequestParam("id") Long id);
}
