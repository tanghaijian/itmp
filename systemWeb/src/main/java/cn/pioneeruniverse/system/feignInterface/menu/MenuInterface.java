package cn.pioneeruniverse.system.feignInterface.menu;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.system.feignFallback.menu.MenuFallback;
import cn.pioneeruniverse.system.vo.menu.TblMenuButtonInfo;

/**
 * 
* @ClassName: MenuInterface
* @Description: systemWeb和system之间菜单调用接口，在页面velocity自定义函数中使用
* @author author
* @date 2020年9月4日 上午10:24:03
*
 */
@FeignClient(value = "system", fallbackFactory = MenuFallback.class)
public interface MenuInterface {

	/**
	 * 
	* @Title: getUserMenu
	* @Description: 获取某人所拥有的菜单列表
	* @author author
	* @param userId 用户ID
	* @return List<Map<String,Object>> TblMenuButtonInfo转换成map后的列表
	 */
    @RequestMapping(value = "menu/getUserMenu", method = RequestMethod.POST)
    List<Map<String, Object>> getUserMenu(@RequestParam("userId") Long userId);


    /**
    * @deprecated
    * @Title: selectMenuById
    * @Description: 通过ID获取菜单
    * @author author
    * @param id 菜单ID
    * @return Map<String,Object> TblMenuButtonInfo的map形式
     */
    @RequestMapping(value = "menu/selectMenuById", method = RequestMethod.POST)
    Map<String, Object> selectMenuById(@RequestParam("id") Long id);

    /**
     * 
    * @Title: getMenuByCode
    * @Description: 通过编码获取菜单
    * @author author
    * @param menuButtonCode 菜单编码
    * @return Map<String,Object>  TblMenuButtonInfo的map形式
     */
    @RequestMapping(value = "menu/getMenuByCode", method = RequestMethod.POST)
    Map<String, Object> getMenuByCode(@RequestParam("menuButtonCode") String menuButtonCode);


    /**
    * @deprecated
    * @Title: selectMenuByParentId
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @author author
    * @param id
    * @return Map<String,Object> key status=1正常，2异常
    *                                data List<TblMenuButtonInf> 菜单列表
     */
    @RequestMapping(value = "menu/selectMenuByParentId", method = RequestMethod.POST)
    Map<String, Object> selectMenuByParentId(@RequestParam("id") Long id);


    /**
    * @deprecated 
    * @Title: insertMenu
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @author author
    * @param menu
    * @return Map<String,Object>
     */
    @RequestMapping(value = "menu/insertMenu", method = RequestMethod.POST)
    Map<String, Object> insertMenu(@RequestBody TblMenuButtonInfo menu);

    /**
     * @deprecated
    * @Title: updateMenu
    * @Description: 
    * @author author
    * @param menu
    * @return Map<String,Object>
     */
    @RequestMapping(value = "menu/updateMenu", method = RequestMethod.POST)
    Map<String, Object> updateMenu(@RequestBody TblMenuButtonInfo menu);


    /**
     * @deprecated 
    * @Title: getMenusWithRole
    * @Description: 
    * @author author
    * @return Map<String,Object>
     */
    @RequestMapping(value = "menu/getMenusWithRole", method = RequestMethod.POST)
    Map<String, Object> getMenusWithRole();

    /**
     * @deprecated
    * @Title: getAllMenu
    * @Description: 
    * @author author
    * @return Map<String,Object>
     */
    @RequestMapping(value = "menu/getAllMenu", method = RequestMethod.POST)
    Map<String, Object> getAllMenu();
}
