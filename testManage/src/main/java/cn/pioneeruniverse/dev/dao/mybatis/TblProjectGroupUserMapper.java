package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblProjectGroupUser;

public interface TblProjectGroupUserMapper extends BaseMapper<TblProjectGroupUser> {

	/**
	* @author author
	* @Description 根据 USER_POST = 8 查询项目小组人员id
	* @Date 2020/9/22
	* @param 
	* @return java.util.List<java.lang.Long>
	**/
	List<Long> findUserIdByPost();

	/**
	* @author author
	* @Description 根据系统id查询系统下管来奶的项目组成员
	* @Date 2020/9/22
	* @param systemId
	* @return java.util.List<java.lang.Long>
	**/
	List<Long> findUserIdBySystemId(Long systemId);

}
