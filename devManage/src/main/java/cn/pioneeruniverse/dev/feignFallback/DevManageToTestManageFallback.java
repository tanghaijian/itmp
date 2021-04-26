package cn.pioneeruniverse.dev.feignFallback;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.feignInterface.DevManageToTestManageInterface;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: liushan
 * @Description:
 * @Date: Created in 15:56 2018/12/14
 * @Modified By:
 */
@Component
public class DevManageToTestManageFallback  extends BaseController implements FallbackFactory<DevManageToTestManageInterface>{

    @Override
    public DevManageToTestManageInterface create(Throwable cause) {
        return new DevManageToTestManageInterface(){

            @Override
            public Map<String, Object> syncDefect(String objectJson) {
                return handleFeignError(cause,objectJson);
            }

            @Override
            public Map<String, Object> syncDefectAtt(String objectJson) {
                return handleFeignError(cause,objectJson);
            }

			@Override
			public Map<String, Object> synReqFeatureDeployStatus(Long requirementId, Long systemId, String deployStatus,
					String loginfo) {
				// TODO Auto-generated method stub
				return null;
			}

            @Override
            public Map<String, Object> synReqFeatureDeployStatus1(String questionNumber, String deployStatus, String loginfo) {
                return null;
            }

            @Override
            public Map<String, Object> insertDefectAttachement(String defectAttache) {
                return null;
            }


			@Override
			public Map<String, Object> synReqFeaturewindow(Long requirementId, Long systemId,
					Long commissioningWindowId, String loginfo, String beforeName, String afterName) {
				// TODO Auto-generated method stub
				return null;
			}

            @Override
            public Map<String, Object> synReqFeaturewindow1(String questionNumber, Long commissioningWindowId, String loginfo, String beforeName, String afterName) {
                return null;
            }

            @Override
			public Map<String, Object> synReqFeatureDept(Long requirementId, Long systemId, Integer deptId,
					String loginfo, String deptBeforeName, String deptAfterName) {
				// TODO Auto-generated method stub
				return null;
			}

            @Override
            public Map<String, Object> detailEnvDate(String list, String envName, String timestamp) {
                System.out.println("+++++++++++++++++++");
                return null;
            }

            @Override
            public Map<String, Object> test1(String envName) {
                System.out.println("+++++++++++++++++++");
                return null;
            }

            @Override
            public String getNewFeatureCode() {
                System.out.println("+++++++++++++++++++");
                return null;
            }
        };
    }

    private Map<String,Object> handleFeignError(Throwable cause,Object objectJson){
        Map<String,Object> map = new HashMap<String,Object>();
        String message = "开发接口调用故障";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cause.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        map.put("status", Constants.ITMP_RETURN_FAILURE);
        logger.error(message+":"+exception);
        logger.error(message+":"+objectJson.toString());
        map.put("errorMessage", message);
        return map;
    }
}
