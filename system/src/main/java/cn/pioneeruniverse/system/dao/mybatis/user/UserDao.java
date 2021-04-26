package cn.pioneeruniverse.system.dao.mybatis.user;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.system.entity.TblUserInfo;

public interface UserDao extends BaseMapper<TblUserInfo> {

    List<TblUserInfo> getAllUser(TblUserInfo user);

    void insertUser(TblUserInfo user);

    void updateUser(TblUserInfo user);

    TblUserInfo getUserAcc(String userAccount);

    TblUserInfo getUserRoleByAcc(String userAccount);

    TblUserInfo getAllUserRoleByAcc(String userAccount);

    TblUserInfo findUserById(Long id);

    void updatePassword(TblUserInfo user);

    void resetPassword(TblUserInfo user);

    Long getPreAccUser(String userAccount);

    void delUserByIds(@Param("list") List<Long> list, @Param("lastUpdateBy") Long lastUpdateBy);

    String selectMaxEmpNo();

    List<TblUserInfo> getExcelAllUser();

    List<TblUserInfo> getUserByRoleId(Map<String, Object> map);

    List<TblUserInfo> findUserWithNoRole(Map<String, Object> map);

    List<TblUserInfo> getUser();

    List<TblUserInfoDTO> getCodeBaseUserDetailByUserScmAccount(Map<String, Object> map);

    List<TblUserInfoDTO> getUserListForCodeBase(TblUserInfoDTO tblUserInfoDTO);

    List<TblUserInfoDTO> getUserListForCodeReview(@Param("tblUserInfoDTO") TblUserInfoDTO tblUserInfoDTO, @Param("currentUserId") Long currentUserId);

    List<TblUserInfo> getAllUserModal(Map<String, Object> map);

    List<Map<String, Object>> getAllDevUser(Map<String, Object> map);

    List<Map<String, Object>> getAllTestUser(Map<String, Object> map);

    /**
     * 根据ID查询同一项目组用户
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectById(Map<String, Object> map);

    //根据项目组
    List<Map<String, Object>> selectByProjectId(Map<String, Object> map);

    List<TblUserInfo> getAllUserModal2(Map<String, Object> map);

    int getAllUserModalTotal(Map<String, Object> map);

    int insertUserByUnique(TblUserInfo user);

    TblUserInfo getUserDetailByUserIdForSvnAccountPasswordCreate(@Param("userId") Long userId);

    Integer getSvnAccountCount(@Param("userScmAccount") String userScmAccount);

    int createSvnAccountByUserId(@Param("userScmAccount") String userScmAccount, @Param("id") Long id);

    int createSvnPasswordByUserId(@Param("userScmPassword") String userScmPassword, @Param("id") Long id);

    Integer getAllUserModalCount2(Map<String, Object> map);

    String getMySvnPassword(@Param("currentUserId") Long currentUserId);

    int updateMySvnPassword(@Param("userScmPassword") String userScmPassword, @Param("currentUserId") Long currentUserId);

    String getUserNameById(Long id);

    String getUserNamesByUserIds(String ids);

    Long getIdByNum(TblUserInfo user);

    Long getIdByUserAccount(TblUserInfo user);

    List<Map<String, Object>> getAllproject();

    List<TblUserInfoDTO> getBatchUserDetailByIds(@Param("ids") List<Long> ids);

	List<TblUserInfo> getAllUserModal3(Map<String, Object> map);
	
	List<TblUserInfo> getUserByNameOrACC(@Param("uid")Long uid, @Param("userName")String userName);

}
