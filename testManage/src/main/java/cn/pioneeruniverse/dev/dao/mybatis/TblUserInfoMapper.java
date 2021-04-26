package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblUserInfo;
/**
 *
 * @ClassName: TblUserInfoMapper
 * @Description: 用户mapper
 * @author author
 *
 */
public interface TblUserInfoMapper {

	/**
	 * 通过id查询
	 * @param createById
	 * @return
	 */
	String getUserNameById(Long createById);

	/**
	 * 查询所有的测试用户
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getAllTestUser(Map<String, Object> map);
	List<Long> getUserIdByUserName(String string);
	List<Map<String, Object>> getUserTable(@Param("testSetId") Long testSetId,@Param("executeRound") Integer executeRound,@Param("userName") String userName,@Param("companyName") String companyName,@Param("deptName") String deptName);
	TblUserInfo getUserById(Long userId);

	/*同步时查询用户信息，无视状态 */
	Long findIdByUserAccount(String userAccount);

	List<Long> selectIdByUserNameSql(@Param("userNameSql")String userNameSql);

	List<TblUserInfo> getUserByNameOrACC(@Param("uid")Long uid, @Param("userName")String userName);
}
