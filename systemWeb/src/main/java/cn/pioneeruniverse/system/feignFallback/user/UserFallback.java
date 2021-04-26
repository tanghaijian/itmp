package cn.pioneeruniverse.system.feignFallback.user;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.common.dto.ResultDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.system.feignInterface.user.UserInterface;
import cn.pioneeruniverse.system.vo.company.Company;
import cn.pioneeruniverse.system.vo.dept.TblDeptInfo;
import cn.pioneeruniverse.system.vo.user.TblUserInfo;
import feign.hystrix.FallbackFactory;

/**
 * 
* @ClassName: UserFallback
* @Description: 模块间用户feign调用熔断降级处理
* @author author
* @date 2020年9月4日 上午9:55:57
*
 */
@Component
public class UserFallback implements FallbackFactory<UserInterface> {

    private static final Logger logger = LoggerFactory.getLogger(UserFallback.class);


    private Map<String, Object> handleFeignError(Throwable cause) {
        Map<String, Object> map = new HashMap<String, Object>();
        String message = "接口调用故障";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cause.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        map.put("status", Constants.ITMP_RETURN_FAILURE);
        logger.error(message + ":" + exception);
        map.put("errorMessage", message);
        return map;
    }


    @Override
    public UserInterface create(Throwable cause) {
        return new UserInterface() {
            @Override
            public Map<String, Object> saveUser(String user, String roleIds) {
                return handleFeignError(cause);
            }


            @Override
            public Map<String, Object> findUserById(Long userId) {
                return handleFeignError(cause);
            }

            @Override
            public Map<String, Object> getUserWithRoles(String userAccount) {
                return handleFeignError(cause);
            }

            @Override
            public Map<String, Object> updatePassword(TblUserInfo user) {
                return handleFeignError(cause);
            }

			/*@Override
            public Map getAllUser(String userJson, Integer pageIndex, Integer pageSize) {
				return handleFeignError(cause);
			}*/

            @Override
            public Map<String, Object> resetPassword(TblUserInfo user) {
                return handleFeignError(cause);
            }

            @Override
            public Map<String, Object> getPreUser(String userAccount) {
                return handleFeignError(cause);
            }

            @Override
            public Map<String, Object> delUser(String userIds, Long lastUpdateBy) {
                return handleFeignError(cause);
            }

            @Override
            public ResultDataDTO login(String userAccount, String password) {
                return ResultDataDTO.ABNORMAL("登录验证异常，异常原因：" + cause.getMessage());
            }

            @Override
            public ResultDataDTO afterCasLogin(String userCode, String userName) {
                return ResultDataDTO.ABNORMAL("处理单点登录成功后操作程序出现异常，异常原因：" + cause.getMessage());
            }


            @Override
            public Map<String, Object> updateUser(String newUser, String roleIds) {
                return handleFeignError(cause);
            }


            @Override
            public String isBindUser(Long userId, String cardUID) {
                // TODO Auto-generated method stub
                return null;
            }


            @Override
            public String updateBind(Long userId, String writeUID) {
                // TODO Auto-generated method stub
                return null;
            }


            @Override
            public String isBindUserHF(Long userId, String cardUID) {
                // TODO Auto-generated method stub
                return null;
            }


            @Override
            public String updateBindHF(Long userId, String writeUID) {
                // TODO Auto-generated method stub
                return null;
            }


            @Override
            public Map<String, Object> getCompanyOpt() {
                // TODO Auto-generated method stub
                return handleFeignError(cause);
            }

            @Override
            public List<Company> getCompany() {
                // TODO Auto-generated method stub
                return null;
            }


            @Override
            public List<TblDeptInfo> getDept() {
                // TODO Auto-generated method stub
                return null;
            }


        };
    }


}
