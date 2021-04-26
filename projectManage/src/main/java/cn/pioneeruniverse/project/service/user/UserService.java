package cn.pioneeruniverse.project.service.user;
/**
 *
 * @ClassName: UserService
 * @Description: 用户实现接口
 * @author author
 *
 */
public interface UserService {
	/**
	 * @author author
	 * @Description 同步时查询用户信息，无视状态
	 * @Date 2020/9/4
	 * @param userAccount
	 * @return java.lang.Long
	 **/
	Long findIdByUserAccount(String userAccount);
	/**
	 * @author author
	 * @Description 根据用户名获取id
	 * @Date 2020/9/4
	 * @param userName
	 * @return java.lang.Long
	 **/
	Long findIdByUserName(String userName);
}
