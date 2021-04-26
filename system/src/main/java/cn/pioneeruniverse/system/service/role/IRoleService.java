package cn.pioneeruniverse.system.service.role;

import java.util.List;

import cn.pioneeruniverse.system.entity.TblUserInfo;
import com.github.pagehelper.PageInfo;

import cn.pioneeruniverse.system.entity.TblMenuButtonInfo;
import cn.pioneeruniverse.system.entity.TblRoleInfo;
import cn.pioneeruniverse.system.entity.TblUserDefaultPage;

import javax.servlet.http.HttpServletRequest;
/**
 *
 * @ClassName: IRoleService
 * @Description: 角色service
 * @author author
 *
 */
public interface IRoleService {

    TblRoleInfo findRoleById(Long id);

    /**
     * 查询
     * @param userId
     * @return
     */
    List<TblRoleInfo> findUserRole(Long userId);

    List<TblRoleInfo> getUserAllRole(Long userId);

    List<TblRoleInfo> getRoleWithMenu();

    Boolean insertRole(TblRoleInfo role, HttpServletRequest request);

    void updateRole(TblRoleInfo role, HttpServletRequest request);

    List<TblRoleInfo> getRoleByMenuId(Long menuId);

    PageInfo<TblRoleInfo> getAllRole(TblRoleInfo bean, Integer pageIndex, Integer pageSize);

    /**
     * 删除
     * @param roleIds
     * @param lastUpdateBy
     */

    void deleteRoles(List<Long> roleIds, Long lastUpdateBy);

    List<TblRoleInfo> getRoleMenu(Long id);

    List<TblUserInfo> getRoleUser(Long id, Integer pageNumber, Integer pageSize);

    void insertRoleUser(Long[] userId, Long id, HttpServletRequest request);

    List<TblUserInfo> findUserWithNoRole(Long roleId, TblUserInfo userInfo, Integer pageNumber, Integer pageSize);

    void updateRoleWithUser(Long[] userId, Long roleId, HttpServletRequest request);

    void updateRoleMenu(Long id, Long[] menuIds, HttpServletRequest request);

    void updateRoleName(TblRoleInfo roleInfo, HttpServletRequest request);

    void authRoleMenu();

    void getAdminRole();

	List<TblMenuButtonInfo> getUserMenu(HttpServletRequest request);

	void saveDefaultPage(List<TblUserDefaultPage> list, Long userId);

	List<TblUserDefaultPage> getDefaultPage(Long userId);
}
