package cn.pioneeruniverse.dev.velocity.tag;

import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.dev.feignInterface.TestManageWebToSystemInterface;
import java.util.Map;

/**
 *  类说明 
* @author:tingting
* @version:2019年3月8日 下午3:22:21
 * 
 */
public class VelocitySystem {

    private static TestManageWebToSystemInterface testManageWebToSystemInterface = SpringContextHolder.getBean(TestManageWebToSystemInterface.class);

    public Map<String, Object> getMenuByCode(String code) {
        return testManageWebToSystemInterface.getMenuByCode(code);
    }

}
