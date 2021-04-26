package cn.pioneeruniverse.system.service.menu.impl;

import java.util.*;
import java.util.regex.Pattern;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.system.entity.TblRoleInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.pioneeruniverse.system.dao.mybatis.menu.MenuDao;
import cn.pioneeruniverse.system.entity.TblMenuButtonInfo;
import cn.pioneeruniverse.system.service.menu.IMenuService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@Service("iMenuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, TblMenuButtonInfo> implements IMenuService {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private RedisUtils redisUtils;
    private static Pattern urlPattern = Pattern.compile("[/[\\w]{1,}]{1,}(\\?.+?)*");

    /**
     * 
    * @Title: getUserMenu
    * @Description: 获取用户拥有权限的菜单（不含按钮）
    * @author author
    * @param userId 用户ID
    * @return List<TblMenuButtonInfo> 菜单列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblMenuButtonInfo> getUserMenu(Long userId) {
        return menuDao.getUserMenu(userId);
    }

    
    /**
     * 
    * @Title: getUserAllMenuButton
    * @Description: 获取用户拥有权限的菜单和按钮
    * @author author
    * @param userId 用户ID
    * @return List<TblMenuButtonInfo>菜单列表
     */
    @Transactional(readOnly = true)
    @Override
    public List<TblMenuButtonInfo> getUserAllMenuButton(Long userId) {
        return menuDao.getUserAllMenuButton(userId);
    }

    /**
     * 
    * @Title: selectMenuById
    * @Description: 获取具体的菜单
    * @author author
    * @param id 菜单ID
    * @return TblMenuButtonInfo 菜单信息
     */
    @Override
    public TblMenuButtonInfo selectMenuById(Long id) {
        return menuDao.selectMenuById(id);
    }

    
    /**
     * 
    * @Title: selectMenuByParentId
    * @Description: 获取某个菜单的子菜单（不含按钮）
    * @author author
    * @param id 菜单ID
    * @return List<TblMenuButtonInfo>菜单列表
     */
    @Override
    public List<TblMenuButtonInfo> selectMenuByParentId(Long id) {
        return menuDao.selectMenuByParentId(id);
    }

    /**
     * 
    * @Title: insertMenu
    * @Description: 添加菜单
    * @author author
    * @param menu 菜单信息
    * @param request
     */
    @Override
    public void insertMenu(TblMenuButtonInfo menu, HttpServletRequest request) {
        if (menu.getId() != null) {
            menu.setParentId(menu.getId());
            if (menu.getParentIds() == null) {
                menu.setParentIds(menu.getId() + ",");
            } else {
                menu.setParentIds(menu.getParentIds() + menu.getId() + ",");
            }
        }
        if (menu.getMenuOrder() == null) {
            menu.setMenuOrder(1);
        }
        menu.setCreateBy(CommonUtil.getCurrentUserId(request));
        menu.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
        menuDao.insertMenu(menu);
        //添加成功后将菜单信息放入redis
        this.putAllMenuUrlToRedis();
    }

    /**
     * 
    * @Title: updateMenu
    * @Description: 更新菜单，主要设置状态
    * @author author
    * @param menu 菜单信息
    * @param request
     */
    @Override
    @Transactional
    public void updateMenu(TblMenuButtonInfo menu, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("validStatus", menu.getValidStatus());
        map.put("lastUpdateBy", CommonUtil.getCurrentUserId(request));
        if (menu.getParentIds() == null) {
            menu.setParentIds(menu.getId() + ",");
        } else {
            menu.setParentIds(menu.getParentIds() + menu.getId() + ",");
        }
        
        //获取所有子菜单信息
        List<TblMenuButtonInfo> menuButtonInfos = menuDao.getMenuByParentIds(menu);
        if (menuButtonInfos != null && menuButtonInfos.size() != 0) {
            for (TblMenuButtonInfo menuButtonInfo : menuButtonInfos) {
                map.put("id", menuButtonInfo.getId());
                //更新所有的子菜单状态
                menuDao.updateMenu(map);
            }
        }

    }

   /**
   *@author author
   *@Description 删除菜单，有下级菜单也删除
   *@Date 2020/8/28
   *@param tblMenuButtonInfo 需要删除的菜单
   *@param request
   **/
    @Override
    @Transactional
    public void deleteMenu(TblMenuButtonInfo tblMenuButtonInfo, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", tblMenuButtonInfo.getStatus());
        map.put("validStatus", tblMenuButtonInfo.getValidStatus());
        map.put("lastUpdateBy", CommonUtil.getCurrentUserId(request));
        if (tblMenuButtonInfo.getParentIds() == null) {
            tblMenuButtonInfo.setParentIds(tblMenuButtonInfo.getId() + ",");
        } else {
            tblMenuButtonInfo.setParentIds(tblMenuButtonInfo.getParentIds() + tblMenuButtonInfo.getId() + ",");
        }
        
        //查找子菜单
        List<TblMenuButtonInfo> menuButtonInfos = menuDao.getMenuByParentIds(tblMenuButtonInfo);
        if (menuButtonInfos != null && menuButtonInfos.size() != 0) {
            for (TblMenuButtonInfo menuButtonInfo : menuButtonInfos) {
                map.put("id", menuButtonInfo.getId());
                //更新子菜单状态
                menuDao.updateMenu(map);
                // 删除菜单权限关联表
                menuDao.deleteRoleMenu(map);
            }
        }

        this.putAllMenuUrlToRedis();
    }

    /**
     * 
    * @Title: updateThisMenu
    * @Description: 更新菜单
    * @author author
    * @param menu 菜单信息
    * @param request
     */
    @Override
    @Transactional
    public void updateThisMenu(TblMenuButtonInfo menu, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", menu.getId());
        map.put("menuButtonName", menu.getMenuButtonName());
        map.put("menuButtonType", menu.getMenuButtonType());
        map.put("menuButtonCode", menu.getMenuButtonCode());
        map.put("menuOrder", menu.getMenuOrder());
        map.put("url", menu.getUrl());
        map.put("css", menu.getCss());
        map.put("validStatus", menu.getValidStatus());
        map.put("lastUpdateBy", CommonUtil.getCurrentUserId(request));
        map.put("openStatus", menu.getOpenStatus());
        menuDao.updateMenu(map);

        //将菜单放入redis
        if (menu.getUrl() != null) {
            this.putAllMenuUrlToRedis();
        }
    }
    
    /**
    *@author author
    *@Description 根据code查询菜单按钮
    *@Date 2020/8/28
    *@param menuButtonCode 菜单编码
    *@return cn.pioneeruniverse.system.entity.TblMenuButtonInfo 菜单信息
    **/
    @Override
    @Transactional(readOnly = true)
    public TblMenuButtonInfo getMenuButtonByCode(String menuButtonCode) {
        return menuDao.getMenuButtonByCode(menuButtonCode);
    }

    
    /**
     * 
    * @Title: getAllMenuButtonUrl
    * @Description: 获取所有菜单的url
    * @author author
    * @return List<String> 菜单url列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> getAllMenuButtonUrl() {
        return menuDao.getAllMenuButtonUrl();
    }

    /**
    *@author author
    *@Description 获取菜单管理列表
    *@Date 2020/8/28
    *@param 
    *@return java.util.List<cn.pioneeruniverse.system.entity.TblMenuButtonInfo> 菜单列表
    **/
    @Override
    public List<TblMenuButtonInfo> getListMenu() {
        Map<String, TblMenuButtonInfo> resultMap = new LinkedHashMap<String, TblMenuButtonInfo>();
        TblMenuButtonInfo menuButtonInfo = new TblMenuButtonInfo();
        List<TblMenuButtonInfo> menuList = menuDao.getMenus();
        resultMap = menuList(menuList, resultMap);

        List<TblMenuButtonInfo> menu_list = new ArrayList<TblMenuButtonInfo>();
        for (Map.Entry<String, TblMenuButtonInfo> entry : resultMap.entrySet()) {
            menuButtonInfo.setId(entry.getValue().getId());
            menuButtonInfo.setLoaded(true);
            menuButtonInfo.setExpanded(false);
            menuButtonInfo.setMenuButtonName(entry.getValue().getMenuButtonName());
            menuButtonInfo.setParent(entry.getValue().getParentId());

            String parentIds;
            if (entry.getValue().getParentIds() == null || entry.getValue().getParentIds().equals("")) {
                parentIds = entry.getValue().getId() + ",";
                menuButtonInfo.setLevel(0);
            } else {
                parentIds = entry.getValue().getParentIds() + entry.getValue().getId() + ",";
                String[] parendIdsArr = entry.getValue().getParentIds().split(",");
                menuButtonInfo.setLevel(parendIdsArr.length);
            }

            List<TblRoleInfo> childrenList = menuDao.getChildrenMenu(parentIds);
            if (childrenList != null && childrenList.size() != 0) {
                menuButtonInfo.setLeaf(false);
            } else {
                menuButtonInfo.setLeaf(true);
            }
            menuButtonInfo.setLoaded(true);
            menuButtonInfo.setExpanded(false);
            menuButtonInfo.setId(entry.getValue().getId());
            menuButtonInfo.setCss(entry.getValue().getCss());
            menuButtonInfo.setMenu(entry.getValue().getMenuButtonName());
            menuButtonInfo.setParent(entry.getValue().getParentId());
            menuButtonInfo.setParentId(entry.getValue().getParentId());
            menuButtonInfo.setParentIds(entry.getValue().getParentIds());
            menuButtonInfo.setMenuButtonType(entry.getValue().getMenuButtonType());
            menuButtonInfo.setMenuButtonCode(entry.getValue().getMenuButtonCode());
            menuButtonInfo.setUrl(entry.getValue().getUrl());
            menuButtonInfo.setStatus(entry.getValue().getStatus());
            menuButtonInfo.setValidStatus(entry.getValue().getValidStatus());
            menuButtonInfo.setMenuOrder(entry.getValue().getMenuOrder());
            menuButtonInfo.setOpenStatus(entry.getValue().getOpenStatus());
            menu_list.add(menuButtonInfo);
            menuButtonInfo = new TblMenuButtonInfo();
        }

        return menu_list;

    }

    /**
    *@author author
    *@Description 递归查询菜单的ztree格式
    *@Date 2020/8/28
    *@param menuList 需要组装的菜单
    *@param resultMap 组装后返回的值
    *@return java.util.Map<java.lang.String,cn.pioneeruniverse.system.entity.TblMenuButtonInfo>
    **/
    public Map<String, TblMenuButtonInfo> menuList(List<TblMenuButtonInfo> menuList, Map<String, TblMenuButtonInfo> resultMap) {
        Map<String, Object> map = new HashMap<>();
        if (menuList != null && menuList.size() != 0) {
            for (TblMenuButtonInfo tblMenuButtonInfo : menuList) {
                if (tblMenuButtonInfo.getParentIds() != null) {
                    map.put("parentId", tblMenuButtonInfo.getParentIds() + tblMenuButtonInfo.getId() + ",");
                } else {
                    map.put("parentId", tblMenuButtonInfo.getId() + ",");
                }
                resultMap.put("menu_" + tblMenuButtonInfo.getId(), tblMenuButtonInfo);
                List<TblMenuButtonInfo> childrenMenus = menuDao.getChildrenAllMenus(map);
                menuList(childrenMenus, resultMap);
            }
        }
        return resultMap;
    }

    /**
    *@author author
    *@Description 查询菜单和权限
    *@Date 2020/8/28
    *@return java.util.List<cn.pioneeruniverse.system.entity.TblMenuButtonInfo> 菜单列表
    **/
    @Override
    public List<TblMenuButtonInfo> getMenusWithRole() {
        return menuDao.getMenusWithRole();
    }

    /**
    *@author author
    *@Description 查询所有菜单
    *@Date 2020/8/28
    *@return java.util.List<cn.pioneeruniverse.system.entity.TblMenuButtonInfo> 菜单列表
    **/
    @Override
    public List<TblMenuButtonInfo> getAllMenu() {
        return menuDao.getAllMenu();
    }

    /**
    *@author author
    *@Description 向redis中放入所有的菜单地址权限
    *@Date 2020/8/28
     * @param 
    **/
    @Override
    public void putAllMenuUrlToRedis() {
        Set<String> allMenuUrlCollection = new HashSet<>();
        for (String url : getAllMenuButtonUrl()) {
            if (StringUtils.isNotBlank(url) && urlPattern.matcher(url.substring(2)).matches()) {
                allMenuUrlCollection.add(url.substring(2));
            }
        }
        redisUtils.set("allMenuUrl", allMenuUrlCollection);
    }

    /**
    *@author author
    *@Description 获取实体类
    *@Date 2020/8/28
    * @param menuId菜单ID
    *@return cn.pioneeruniverse.system.entity.TblMenuButtonInfo菜单信息
    **/
    @Override
    public TblMenuButtonInfo getMenuEntity(Long menuId) {
        return menuDao.getMenuEntity(menuId);
    }
}
