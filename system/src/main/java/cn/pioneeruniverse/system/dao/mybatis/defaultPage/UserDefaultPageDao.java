package cn.pioneeruniverse.system.dao.mybatis.defaultPage;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.system.entity.TblUserDefaultPage;

public interface UserDefaultPageDao extends BaseMapper<TblUserDefaultPage> {

	/**
	* @author author
	* @Description  逻辑删除某人的默认页面（登录后显示的tab页）
	* @Date 2020/9/4
	* @param userId
	* @return void
	**/
	void updateDefaultPage(Long userId);

	/**
	* @author author
	* @Description 保存默认tab页
	* @Date 2020/9/4
	* @param tblUserDefaultPage
	* @return void
	**/
	void saveDefaultPage(TblUserDefaultPage tblUserDefaultPage);

	/**
	* @author author
	* @Description 获取默认tab页
	* @Date 2020/9/4
	* @param currentUserId
	* @return java.util.List<java.lang.Long>
	**/
	List<Long> getDefaultPage(Long currentUserId);

	/**
	* @author author
	* @Description 获取默认tab页，以及页面的菜单按钮信息
	* @Date 2020/9/4
	* @param userId
	* @return java.util.List<cn.pioneeruniverse.system.entity.TblUserDefaultPage>
	**/
	List<TblUserDefaultPage> getDefaultPage2(Long userId);

}
