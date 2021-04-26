package cn.pioneeruniverse.project.service.assetsLibrary;

import cn.pioneeruniverse.common.entity.BootStrapTablePage;
import cn.pioneeruniverse.project.entity.TblSystemDirectory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryPostAuthority;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryRole;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryRoleRelation;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryUserAuthority;
import cn.pioneeruniverse.project.entity.TblUserInfo;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 13:53 2019/12/5
 * @Modified By:
 */
public interface SystemDirectoryAuthorityService {

    BootStrapTablePage<TblSystemDirectoryUserAuthority> getDirectoryUserAuthority(BootStrapTablePage<TblSystemDirectoryUserAuthority> bootStrapTablePage, Long directoryId);

    BootStrapTablePage<TblSystemDirectoryPostAuthority> getDirectoryPostAuthority(BootStrapTablePage<TblSystemDirectoryPostAuthority> bootStrapTablePage, Long directoryId);

    void addOrUpdateDirectoryUserAuthority(List<TblSystemDirectoryUserAuthority> tblSystemDirectoryUserAuthorities, HttpServletRequest request);

    void deleteDirectoryUserAuthority(List<TblSystemDirectoryUserAuthority> tblSystemDirectoryUserAuthorities);

    void addOrUpdateDirectoryPostAuthority(List<TblSystemDirectoryPostAuthority> tblSystemDirectoryPostAuthorities, HttpServletRequest request);

    void deleteDirectoryPostAuthority(List<TblSystemDirectoryPostAuthority> tblSystemDirectoryPostAuthorities);

    Map<String, Boolean> getCurrentUserDirectoryAuthority(HttpServletRequest request, Long projectId, Long directoryId);

	void addOrUpdateSystemDirectoryRole(TblSystemDirectoryRole tblSystemDirectoryRole, HttpServletRequest request);

	List<TblSystemDirectoryRole> getAllSystemDirectoryRole(Long projectId);

	List<TblUserInfo> getSystemDirectoryRoleUserByRoleId(Long roleId, Integer pageNumber, Integer pageSize);

	Long getRoleUserCountByRoleId(Long roleId);

	List<TblSystemDirectoryRoleRelation> getSystemDirectoryRoleRelationByRoleId(Long roleId);

	List<TblSystemDirectory> getSystemDirectoryAuth(Long projectId, Long roleId);

	void addSystemDirectoryRoleUser(Long roleId, String uids, HttpServletRequest request);

	void cancelSystemDirectoryRoleUser(Long roleId, String uids, HttpServletRequest request);

	void saveSystemDirectoryAuth(List<TblSystemDirectoryRoleRelation> systemDirectoryRoleRelations, Long roleId, HttpServletRequest request);

	Map<String, Object> getCurrentUserSystemDirecoryAuth(Long projectId, Long systemDirectoryId, HttpServletRequest request);

}
