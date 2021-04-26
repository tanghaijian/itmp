package cn.pioneeruniverse.dev.feignFallback;

import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import cn.pioneeruniverse.dev.feignInterface.DevManageToSystemInterface;
import feign.hystrix.FallbackFactory;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 15:56 2018/12/14
 * @Modified By:
 */
@Component
public class DevManageToSystemFallback implements FallbackFactory<DevManageToSystemInterface> {

    private static final Logger logger = LoggerFactory.getLogger(DevManageToSystemFallback.class);

    @Override
    public DevManageToSystemInterface create(Throwable throwable) {
        return new DevManageToSystemInterface() {


            @Override
            public List<TblUserInfoDTO> getCodeBaseUsersDetail(Map<String, String> svnUserAuth) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public TblUserInfoDTO getCodeBaseUserDetailByUserScmAccount(String userScmAccount) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }
            
            @Override
            public List<TblUserInfoDTO> getCodeBaseUserDetailByUserScmAccountList(String userScmAccounts) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, String> getDevUserNameAndReviewersNameForCodeReivew(Long devUserId, String codeReviewUserIds) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, Object> findUserById(Long userId) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, Object> selectDeptById(Long id) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, Object> findRolesByUserID(Long userId) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, Object> findFieldByTableName(String tableName) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, String> createSvnAccountPassword(Long userId) {
                logger.error(throwable.getMessage(), throwable.getCause());
                throw new RuntimeException(throwable.getCause());
            }

            @Override
            public Map<String, String> insertMessage(String message) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }
            
            @Override
            public Map<String, String> sendMessage(String message) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, String> getMenuByCode(String menuButtonCode) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }


        };
    }
}
