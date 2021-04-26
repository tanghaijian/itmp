package cn.pioneeruniverse.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.system.entity.TblMenuButtonInfo;
import cn.pioneeruniverse.system.service.menu.IMenuService;

import javax.servlet.http.HttpServletRequest;

/**
 * 
* @ClassName: MenuController
* @Description: 系统菜单管理菜单对应的controller
* @author author
* @date 2020年7月29日 下午4:35:11
*
 */
@RestController
@RequestMapping("menu")
public class MenuController {

    private static Logger logger = LoggerFactory.getLogger(MenuController.class);

    // 菜单接口
    @Autowired
    private IMenuService iMenuService;


    /**
     * 
    * @Title: getMenuEntity
    * @Description: 通过菜单ID获取菜单的基本信息,在新增下级获取父id，修改时获取自身信息
    * @author author
    * @param menuId：菜单ID
    * @return map key值为status时，如果为1则正常返回，如果为2则异常返回；
    *             Key值为data时，则返回页面所需要的数据。以下方法同理
    * @throws
     */
    @RequestMapping(value = "getMenuEntity", method = RequestMethod.POST)
    public Map<String, Object> getMenuEntity(Long menuId) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        if (menuId == null) {
            return this.handleException(new Exception(), "查询失败,参数为空");
        }
        try {
            TblMenuButtonInfo menuButtonInfo = iMenuService.getMenuEntity(menuId);
            result.put("menuInfo", menuButtonInfo);
        } catch (Exception e) {
            return this.handleException(e, "查询失败");
        }
        return result;
    }

    /**
     * 
    * @Title: deleteMenu
    * @Description: 删除菜单
    * @author author
    * @param tblMenuButtonInfo：菜单按钮对象
    * @param request
    * @return map Key：status=1 正常返回 ，2异常返回 
    * @throws
     */
    @RequestMapping(value = "deleteMenu", method = RequestMethod.POST)
    public Map<String, Object> deleteMenu(@RequestBody TblMenuButtonInfo tblMenuButtonInfo, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        if (tblMenuButtonInfo == null) {
            return this.handleException(new Exception(), "删除用户菜单失败,参数为空");
        } else {
            try {
                iMenuService.deleteMenu(tblMenuButtonInfo, request);
            } catch (Exception e) {
                return this.handleException(e, "删除用户菜单失败");
            }
        }
        return result;
    }

    /**
     * 
    * @Title: listData
    * @Description: 菜单列表信息，以树形结构展示
    * @author author
    * @return map Key：status=1 正常返回 ，2异常返回 
    *                  data返回的菜单数据
    * @throws
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Map<String, Object> listData() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            List<TblMenuButtonInfo> list = iMenuService.getListMenu();
            result.put("data", list);
        } catch (Exception e) {
            return this.handleException(e, "获取用户菜单失败");
        }
        return result;
    }

    /**
     * 
    * @Title: insertMenu
    * @Description: 新增菜单信息
    * @author author
    * @param menu：菜单对象
    * @param request
    * @return map Key：status=1 正常返回 ，2异常返回 
    * @throws
     */
    @RequestMapping(value = "insertMenu", method = RequestMethod.POST)
    public Map<String, Object> insertMenu(@RequestBody TblMenuButtonInfo menu, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        if (menu == null) {
            return this.handleException(new Exception(), "新增菜单失败,参数为空");
        } else {
            try {
                iMenuService.insertMenu(menu, request);
            } catch (Exception e) {
                return this.handleException(e, "新增菜单失败");
            }
        }
        return result;
    }

    /**
     * 
    * @Title: updateThisMenu
    * @Description: 更新菜单，菜单的所有信息
    * @author author
    * @param menu：菜单对象
    * @param request
    * @return
    * @throws
     */
    @RequestMapping(value = "updateThisMenu", method = RequestMethod.POST)
    public Map<String, Object> updateThisMenu(@RequestBody TblMenuButtonInfo menu, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        if (menu == null) {
            return this.handleException(new Exception(), "更新菜单失败,参数为空");
        } else {
            try {
                iMenuService.updateThisMenu(menu, request);
            } catch (Exception e) {
                return this.handleException(e, "更新菜单失败");
            }
        }

        return result;
    }

    /**
     * 
    * @Title: updateMenu
    * @Description: 更新菜单，置为无效/有效功能
    * @author author
    * @param menu：需要更新的菜单对象
    * @param request
    * @return
    * @throws
     */
    @RequestMapping(value = "updateMenu", method = RequestMethod.POST)
    public Map<String, Object> updateMenu(@RequestBody TblMenuButtonInfo menu, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        if (menu == null) {
            return this.handleException(new Exception(), "更新菜单失败,参数为空");
        } else {
            try {
                iMenuService.updateMenu(menu, request);
            } catch (Exception e) {
                return this.handleException(e, "更新菜单失败");
            }
        }

        return result;
    }

    /**
     * 
    * @Title: getUserMenu
    * @Description: 获取userId这个人所拥有权限的菜单列表
    * @author author
    * @param userId：用户ID
    * @return
    * @throws
     */
    @RequestMapping(value = "getUserMenu", method = RequestMethod.POST)
    public List<TblMenuButtonInfo> getUserMenu(Long userId) {
        try {
            return iMenuService.getUserMenu(userId);
        } catch (Exception e) {
            logger.error("查询用户菜单异常，异常原因：" + e.getMessage(), e);
            return null;
        }
    }

  /**
   * 
  * @Title: getAllMenu
  * @Description: 获取所有的菜单信息
  * @author author
  * @return
  * @throws
   */
    @RequestMapping(value = "getAllMenu", method = RequestMethod.POST)
    public Map<String, Object> getAllMenu() {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);

        try {
            List<TblMenuButtonInfo> list = iMenuService.getAllMenu();
            result.put("data", JSONObject.toJSONString(list));
        } catch (Exception e) {
            return this.handleException(e, "获取用户菜单失败");
        }
        return result;

    }

    /**
     * 
    * @Title: selectMenuById
    * @Description: 获取某一条菜单信息
    * @author author
    * @param id：菜单ID
    * @return
    * @throws
     */
    @RequestMapping(value = "selectMenuById", method = RequestMethod.POST)
    public Map<String, Object> selectMenuById(Long id) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            TblMenuButtonInfo menu = iMenuService.selectMenuById(id);
            result.put("data", JSONObject.toJSONString(menu));
        } catch (Exception e) {
            return this.handleException(e, "获取菜单详情失败");
        }
        return result;
    }

   /**
    * 
   * @Title: selectMenuByParentId
   * @Description: 获取某一条菜单下的所有子菜单
   * @author author
   * @param id：菜单ID
   * @return map key status=1正常，2异常
   *                 data List<TblMenuButtonInf> 菜单列表
   * @throws
    */
    @RequestMapping(value = "selectMenuByParentId", method = RequestMethod.POST)
    public Map<String, Object> selectMenuByParentId(Long id) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            List<TblMenuButtonInfo> list = iMenuService.selectMenuByParentId(id);
            result.put("data", JSONObject.toJSONString(list));
        } catch (Exception e) {
            return this.handleException(e, "获取子菜单失败");
        }
        return result;
    }

    /**
     * 
    * @Title: getMenusWithRole
    * @Description: 获取所有菜单，并且返回拥有此菜单的所有的角色
    * @author author
    * @return
    * @throws
     */
    @RequestMapping(value = "getMenusWithRole", method = RequestMethod.POST)
    public Map<String, Object> getMenusWithRole() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            List<TblMenuButtonInfo> list = iMenuService.getMenusWithRole();
            result.put("data", JSONObject.toJSONString(list));
        } catch (Exception e) {
            return this.handleException(e, "获取所有菜单失败");
        }
        return result;
    }

    /**
     * 
    * @Title: getMenuByCode
    * @Description: 通过编码获取一条菜单信息
    * @author author
    * @param menuButtonCode
    * @return TblMenuButtonInfo
     */
    @RequestMapping(value = "getMenuByCode", method = RequestMethod.POST)
    public TblMenuButtonInfo getMenuByCode(String menuButtonCode) {
        return iMenuService.getMenuButtonByCode(menuButtonCode);
    }


    /**
     * 
    * @Title: handleException
    * @Description: 异常处理公共方法，status=2固定，errorMessage:异常信息
    * @author author
    * @param e：异常对象
    * @param message：异常信息
    * @return
    * @throws
     */
    public Map<String, Object> handleException(Exception e, String message) {
        e.printStackTrace();
        logger.error(message + ":" + e.getMessage(), e);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", Constants.ITMP_RETURN_FAILURE);
        map.put("errorMessage", message);
        return map;
    }
}
