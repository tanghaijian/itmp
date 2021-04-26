package cn.pioneeruniverse.project.feignFallback;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.feignInterface.ProjectManageWebToProjectManageInterface;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Description: projectManagemWeb模块请求projectManage模块微服务接口熔断处理
 * @Date: Created in 15:11 2020/08/24
 * @Modified By:
 */
@Component
public class ProjectManageWebToProjectManageFallback implements FallbackFactory<ProjectManageWebToProjectManageInterface>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectManageWebToProjectManageFallback.class);

	private Map<String,Object> handleFeignError(Throwable cause){
		Map<String,Object> map = new HashMap<String,Object>();
		String message = "接口调用故障";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		cause.printStackTrace(new PrintStream(baos));  
		String exception = baos.toString(); 
		map.put("status", Constants.ITMP_RETURN_FAILURE);
		logger.error(message+":"+exception);
		map.put("errorMessage", message);
		return map;
	}


	@Override
	public ProjectManageWebToProjectManageInterface create(Throwable cause) {
		return new ProjectManageWebToProjectManageInterface(){

			@Override
			public Map<String, Object> toEditRequirementById(Long rids) {
				return handleFeignError(cause);
			}

			@Override
			public List<TblRequirementInfo> getExcelRequirement(String findRequirment, Long uid,List<String> roleCodes) {
				return null;
			}

			@Override
			public List<String> getRequirementsByIds(String reqIds) {
				return null;
			}

			@Override
			public Map<String, Object> findRequirementField(Long id) {
				return null;
			}

			@Override
			public List<String> getsystems(Long id) {
				return null;
			}

			@Override
			public Map<String, Object> selectProjectAndUserById(Long id, String type) {
				return null;
			}
			@Override
			public Map<String, Object> selectProjectById(Long id) {
				return null;
			}


			/*@Override
			public List<TblRequirementInfo> getAllRequirement(String requirmentJson, Integer pageIndex, Integer pageSize) {
				return null;
			}			
			@Override
			public int getCountRequirement(String requirmentJson) {
				return 0;
			}*/
			/*@Override
			public Map<String, Object> findRequirementById(Long id,Long parentId) {				
				return handleFeignError(cause);
			}*/
			/*@Override
			public List<TblDataDic> getDataDicList(String datadictype) {
				return null;
			}*/
						
		};
	}	
	
}
