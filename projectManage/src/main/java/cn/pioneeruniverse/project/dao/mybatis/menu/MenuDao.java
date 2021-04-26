package cn.pioneeruniverse.project.dao.mybatis.menu;

import cn.pioneeruniverse.project.entity.TblMenuButtonInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;
/**
 *
 * @ClassName:MenuDao
 * @Description:菜单dao
 * @author author
 * @date 2020年8月16日
 *
 */
public interface MenuDao extends BaseMapper<TblMenuButtonInfo> {
    /**
     * 查询
     * @param map
     * @return
     */

    List<TblMenuButtonInfo> getMenusWithTYPE(Map<String, Object> map);

    /**
     * 查询子菜单
     * @param map
     * @return
     */

    List<TblMenuButtonInfo> getChildrenMenus(Map<String, Object> map);

    List<TblMenuButtonInfo> findRoleMenu(String parentIds);

    List<TblMenuButtonInfo> getButton(Map<String, Object> map);

    TblMenuButtonInfo getButtonByRid(Map<String, Object> map);

    List<TblMenuButtonInfo> selectMenuByParentId(Long id);
}
