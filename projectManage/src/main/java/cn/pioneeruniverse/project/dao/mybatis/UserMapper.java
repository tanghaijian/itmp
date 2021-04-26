package cn.pioneeruniverse.project.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import cn.pioneeruniverse.project.entity.TblUserInfo;

public interface UserMapper {

	/**
	* @author author
	* @Description 根据用户姓名查询ID
	* @Date 2020/9/4
	* @param userName
	* @return java.lang.Long
	**/
	Long findIdByUserName(String userName);

	/**
	* @author author
	* @Description 根据ID集合查询用户姓名
	* @Date 2020/9/4
	* @param userIds
	* @return java.util.List<java.lang.String>
	**/
	List<String> findUserNameByIds(List<Long> userIds);

	/**
	* @author author
	* @Description 同步时查询用户信息，无视状态
	* @Date 2020/9/4
	* @param userAccount
	* @return java.lang.Long
	**/
	Long findIdByUserAccount(String userAccount);

	TblUserInfo selectUserById(Long userId);

	List<TblUserInfo> selectUser(HashMap<String, Object> map);

	Long selectUserIdByUserName(String userName);

	String getUserNameById(Long id);

	List<TblUserInfoDTO> getUserInfoByUserIds(@Param("ids")List<Long> userIds);

    List<Long> findIdByUserNameNew(String projectManageName);
}
