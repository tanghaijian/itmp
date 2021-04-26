package cn.pioneeruniverse.system.feignFallback.menu;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.system.feignInterface.menu.MenuInterface;
import cn.pioneeruniverse.system.vo.menu.TblMenuButtonInfo;
import feign.hystrix.FallbackFactory;
/**
 *
 * @ClassName:MenuFallback
 * @Description:菜单错误处理类
 * @author author
 * @date 2020年8月16日
 *
 */
@Component
public class MenuFallback implements FallbackFactory<MenuInterface> {

    private static Logger logger = LoggerFactory.getLogger(MenuFallback.class);


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
    public MenuInterface create(Throwable cause) {
        return new MenuInterface() {
            @Override
            public List<Map<String, Object>> getUserMenu(Long userId) {
                logger.error("查询用户菜单接口异常，异常原因：" + cause.getMessage(), cause);
                return null;
            }

            @Override
            public Map<String, Object> selectMenuById(Long id) {
                return handleFeignError(cause);
            }

            @Override
            public Map<String, Object> getMenuByCode(String menuButtonCode) {
                return null;
            }

            @Override
            public Map<String, Object> selectMenuByParentId(Long id) {
                return handleFeignError(cause);
            }

            @Override
            public Map<String, Object> insertMenu(TblMenuButtonInfo menu) {
                return handleFeignError(cause);
            }

            @Override
            public Map<String, Object> updateMenu(TblMenuButtonInfo menu) {
                return handleFeignError(cause);
            }

            @Override
            public Map<String, Object> getMenusWithRole() {
                return handleFeignError(cause);
            }

            @Override
            public Map<String, Object> getAllMenu() {
                return handleFeignError(cause);
            }
        };
    }
}
