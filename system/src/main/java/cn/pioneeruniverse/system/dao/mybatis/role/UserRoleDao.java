package cn.pioneeruniverse.system.dao.mybatis.role;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.system.entity.TblUserRole;

/**
 *
 * @ClassName:UserRoleDao
 * @Description:用户角色dao
 * @author author
 * @date 2020年8月19日
 *
 */
public interface UserRoleDao extends BaseMapper<TblUserRole> {
	/**
	 * 插入
	 * @param list
	 */

	void insertUserRole(List<TblUserRole> list);

	/**
	 * 删除
	 * @param list
	 */
	
	void delUserRole(List<Long> list);

	/**
	 * 删除
	 * @param list
	 * @param lastUpdateBy 更新者
	 */
	
	void delUserRoleByRoleId(@Param("list")List<Long> list,@Param("lastUpdateBy")Long lastUpdateBy);

	/**
	 * 删除
	 * @param list
	 * @param lastUpdateBy
	 */
	
	void delUserRoleByUserIds(@Param("list")List<Long> list,@Param("lastUpdateBy")Long lastUpdateBy);
	
	void delUserRoleByUserId(Long userId);

	/**
	 * 通过用户id获取角色
	 * @param id
	 * @return List<Long>
	 */

	List<Long> findRoleIdByUserId(Long id);
}
