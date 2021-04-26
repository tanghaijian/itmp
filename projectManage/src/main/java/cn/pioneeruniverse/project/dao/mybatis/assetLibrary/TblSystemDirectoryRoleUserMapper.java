package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryRoleUser;
import cn.pioneeruniverse.project.entity.TblUserInfo;

public interface TblSystemDirectoryRoleUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblSystemDirectoryRoleUser record);

    int insertSelective(TblSystemDirectoryRoleUser record);

    TblSystemDirectoryRoleUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryRoleUser record);

    int updateByPrimaryKey(TblSystemDirectoryRoleUser record);

	List<TblUserInfo> getSystemDirectoryRoleUserByRoleId(Map<String, Object> map);

	Long getRoleUserCountByRoleId(Long systemDirectoryRoleId);

	void batchInsert(List<TblSystemDirectoryRoleUser> list);

	void cancelSystemDirectoryRoleUser(@Param("roleId")Long roleId,@Param("uids") String uids);
}