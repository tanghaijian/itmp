package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.common.dto.TblDeptInfoDTO;
import cn.pioneeruniverse.dev.entity.TblUserInfo;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @ClassName: TblUserInfoMapper
 * @Description: 用户mapper接口
 * @author author
 *
 */
public interface TblUserInfoMapper extends BaseMapper<TblUserInfo> {

	/**
	 * 查询
	 * @param userId
	 * @return
	 */
	TblUserInfo getUserById(Long userId);

	TblDeptInfoDTO findDeptById(Long deptId);

	/**
	 * 通过账号查找用户
	 * @param userAccount
	 * @return
	 */
	List<TblUserInfo> getUserByAccount(String userAccount);

	/*同步时查询用户信息，无视状态 */
	Long findIdByUserAccount(String userAccount);
	List<String> findRoleByUserIds(Map<String,Object> map);

    List<String> findUserNameByIds(List<Long> userId1);

    List<TblUserInfo> findBySystemId(@Param("systemId") Long systemId, @Param("userName") String userName);
}
