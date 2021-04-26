package cn.pioneeruniverse.system.service.menu;

import java.util.List;

import cn.pioneeruniverse.system.entity.TblMenuButtonInfo;

import javax.servlet.http.HttpServletRequest;

public interface IMenuService {

    List<TblMenuButtonInfo> getUserMenu(Long userId);

    List<TblMenuButtonInfo> getUserAllMenuButton(Long userId);

    TblMenuButtonInfo selectMenuById(Long id);

    List<TblMenuButtonInfo> selectMenuByParentId(Long id);

    void insertMenu(TblMenuButtonInfo menu, HttpServletRequest request);

    void updateMenu(TblMenuButtonInfo menu, HttpServletRequest request);

    List<TblMenuButtonInfo> getMenusWithRole();

    List<TblMenuButtonInfo> getAllMenu();
    /**
     *@author author
     *@Description 获取菜单管理列表
     *@Date 2020/8/28
     * @param
     *@return java.util.List<cn.pioneeruniverse.system.entity.TblMenuButtonInfo>
     **/
    List<TblMenuButtonInfo> getListMenu();
    /**
     *@author author
     *@Description 删除菜单，有下级菜单也删除
     *@Date 2020/8/28
     * @param tblMenuButtonInfo
     * @param request
     *@return void
     **/
    void deleteMenu(TblMenuButtonInfo tblMenuButtonInfo, HttpServletRequest request);
    /**
     *@author author
     *@Description 修改当前行
     *@Date 2020/8/28
     * @param menu
     * @param request
     *@return void
     **/
    void updateThisMenu(TblMenuButtonInfo menu, HttpServletRequest request);

    /* void insertChildrenMenu(TblMenuButtonInfo menu);*/
    /**
    *@author author
    *@Description 根据code查询菜单按钮
    *@Date 2020/8/28
     * @param menuButtonCode
    *@return cn.pioneeruniverse.system.entity.TblMenuButtonInfo
    **/
    TblMenuButtonInfo getMenuButtonByCode(String menuButtonCode);

    /**
    *@author author
    *@Description  获取所有菜单按钮地址
    *@Date 2020/8/31
     * @param 
    *@return java.util.List<java.lang.String>
    **/
    List<String> getAllMenuButtonUrl();

    /**
    *@author author
    *@Description  想redis中放入所有的菜单地址权限
    *@Date 2020/8/31
     * @param 
    *@return void
    **/
    void putAllMenuUrlToRedis();

    /**
    *@author author
    *@Description 获取实体类
    *@Date 2020/8/31
     * @param menuId
    *@return cn.pioneeruniverse.system.entity.TblMenuButtonInfo
    **/
    TblMenuButtonInfo getMenuEntity(Long menuId);
}
