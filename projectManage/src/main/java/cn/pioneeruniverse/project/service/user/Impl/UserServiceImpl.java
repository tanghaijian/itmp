package cn.pioneeruniverse.project.service.user.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.pioneeruniverse.project.dao.mybatis.UserMapper;
import cn.pioneeruniverse.project.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	


	/**
	* @author author
	* @Description 同步时查询用户信息，无视状态
	* @Date 2020/9/4
	* @param userAccount
	* @return java.lang.Long
	**/
	@Override
	public Long findIdByUserAccount(String userAccount) {
		return userMapper.findIdByUserAccount(userAccount);
	}



	/**
	* @author author
	* @Description 根据用户名获取id
	* @Date 2020/9/4
	* @param userName
	* @return java.lang.Long
	**/
	@Override
	public Long findIdByUserName(String userName) {
		return userMapper.findIdByUserName(userName);
	}

}
