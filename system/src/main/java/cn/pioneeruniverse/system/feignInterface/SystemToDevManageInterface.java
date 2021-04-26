package cn.pioneeruniverse.system.feignInterface;



import cn.pioneeruniverse.common.dto.ResultDataDTO;
import cn.pioneeruniverse.system.feignFallback.SystemToDevManageFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
* @ClassName: SystemToDevManageInterface
* @Description: 系统模块和开发模块接口调用
* @author author
* @date 2020年8月12日 下午1:51:02
*
 */
@FeignClient(value = "devManage", fallbackFactory = SystemToDevManageFallback.class)
public interface SystemToDevManageInterface {

	/**
	 * 
	* @Title: modifySvnPassword
	* @Description: 修改SVN密码
	* @author author
	* @param currentUserId 当前用户
	* @param userScmAccount 仓库账号
	* @param userScmPassword 仓库密码
	* @param entryptUserScmPassword 加密后仓库密码
	* @return ResultDataDTO
	* @throws
	 */
    @RequestMapping(value = "version/modifySvnPassword", method = RequestMethod.POST)
    ResultDataDTO modifySvnPassword(@RequestParam("currentUserId") Long currentUserId, @RequestParam("userScmAccount") String userScmAccount,
                                    @RequestParam("userScmPassword") String userScmPassword, @RequestParam("entryptUserScmPassword") String entryptUserScmPassword);
  
    /**
     * 
    * @Title: modifyGitPassword
    * @Description: 修改git密码
    * @author author
    * @param userId 当前用户
    * @param userScmAccount 仓库账号
    * @param svnDefaultPassword 仓库密码
    * @param entryptPassword 加密后仓库密码
    * @return ResultDataDTO
    * @throws
     */
    @RequestMapping(value = "version/modifyGitPassword", method = RequestMethod.POST)
	ResultDataDTO modifyGitPassword(@RequestParam("currentUserId")Long userId,@RequestParam("userScmAccount") String userScmAccount, 
			@RequestParam("userScmPassword") String svnDefaultPassword,@RequestParam("entryptUserScmPassword")String entryptPassword);
}
