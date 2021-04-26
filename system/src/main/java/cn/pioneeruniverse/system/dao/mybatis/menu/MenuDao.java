package cn.pioneeruniverse.system.dao.mybatis.menu;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.system.entity.TblMenuButtonInfo;
import cn.pioneeruniverse.system.entity.TblRoleInfo;

public interface MenuDao extends BaseMapper<TblMenuButtonInfo> {

    /**
    *@author author
    *@Description 获取用户菜单
    *@Date 2020/8/28
     * @param userId
    *@return java.util.List<cn.pioneeruniverse.system.entity.TblMenuButtonInfo>
    **/
    List<TblMenuButtonInfo> getUserMenu(Long userId);

    /**
    *@author author
    *@Description 用户的所有菜单按钮
    *@Date 2020/8/31
     * @param userId
    *@return java.util.List<cn.pioneeruniverse.system.entity.TblMenuButtonInfo>
    **/
    List<TblMenuButtonInfo> getUserAllMenuButton(Long userId);

    TblMenuButtonInfo selectMenuById(Long id);

    List<TblMenuButtonInfo> selectMenuByParentId(Long id);

    List<TblMenuButtonInfo> selectUserMenuByParentId(Map<String, Object> map);

    void insertMenu(TblMenuButtonInfo menu);

    void updateMenu(Map<String, Object> map);

    List<TblMenuButtonInfo> getMenusWithRole();

    List<TblMenuButtonInfo> getAllMenu();

    List<TblMenuButtonInfo> getButton(Map<String, Object> map);

    TblMenuButtonInfo getButtonByRid(Map<String, Object> map);

    List<TblRoleInfo> findRoleMenu(String parentIds);

    List<TblMenuButtonInfo> getMenusWithTYPE(Map<String, Object> map);

    List<TblMenuButtonInfo> getChildrenMenus(Map<String, Object> map);

    List<TblMenuButtonInfo> getMenus();

    List<TblMenuButtonInfo> getChildrenAllMenus(Map<String, Object> map);

    List<TblRoleInfo> getChildrenMenu(String parentIds);

    List<TblMenuButtonInfo> getMenuByParentIds(TblMenuButtonInfo tblMenuButtonInfo);

    void deleteRoleMenu(Map<String, Object> map);

    TblMenuButtonInfo getMenuButtonByCode(String menuButtonCode);

    List<String> getAllMenuButtonUrl();

    TblMenuButtonInfo getMenuEntity(Long menuId);

	List<TblMenuButtonInfo> getUserMenu2(Long userId);

	List<TblMenuButtonInfo> selectSonMenu(Long id);

}
