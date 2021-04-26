package cn.pioneeruniverse.dev.feignFallback;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.feignInterface.TestManageToDevManageInterface;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
public class TestManageToDevManageFallback  extends BaseController implements FallbackFactory<TestManageToDevManageInterface>{

    @Override
    public TestManageToDevManageInterface create(Throwable cause) {
        return new TestManageToDevManageInterface(){

            @Override
            public Map<String, Object> syncDefect(String objectJson) throws Exception{
                return handleFeignError(cause,objectJson);
            }

            @Override
            public Map<String, Object> syncDefectAtt(String objectJson) throws Exception{
                return handleFeignError(cause,objectJson);
            }

			@Override
			public Map<String, Object> synReqFeatureDeployStatus(Long requirementId, Long systemId, String deployStatus,
					String loginfo) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<String, Object> updateReqFeatureTimeTrace(String jsonString) {
				// TODO Auto-generated method stub
				return null;
			}

            @Override
            public Map<String, Object> syncDefectWithFiles(String json) {
                return handleFeignError(cause,json);
            }


			@Override
			public Map<String, Object> synReqFeaturewindow(Long requirementId, Long systemId,
					Long commissioningWindowId, String loginfo, String beforeName, String afterName) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<String, Object> synReqFeatureDept(Long requirementId, Long systemId, Long deptId, String loginfo,
					String deptBeforeName, String deptAfterName) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<String, Object> getDevTaskBySystemAndRequirement(Long systemId, Long requirementId) {
				// TODO Auto-generated method stub
				return null;
			}

            @Override
            public Map<String, Object> getSystemVersionBySystemVersionId(Long systemVersionId) {
                logger.error(cause.getMessage(), cause.getCause());
                return null;
            }

            @Override
            public void updateSprintWorkLoad(String param) {

            }

            @Override
            public Map<String, Object> getRequireFeature(String code, Long requireFeatureId) {
                return null;
            }

            @Override
            public Map<String, Object> getProjectPlanTree(Long projectId) {
                return null;
            }

        };
    }

    private Map<String,Object> handleFeignError(Throwable cause,String value){
        Map<String,Object> map = new HashMap<String,Object>();
        String message = "测试接口调用故障";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cause.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        map.put("status", Constants.ITMP_RETURN_FAILURE);
        logger.error(message+":" + exception);
        logger.error(message+":" + value);
        map.put("errorMessage", message);
        return map;
    }
}
