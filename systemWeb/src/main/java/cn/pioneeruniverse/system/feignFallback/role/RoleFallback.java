package cn.pioneeruniverse.system.feignFallback.role;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.system.feignInterface.role.RoleInterface;
import cn.pioneeruniverse.system.vo.role.TblRoleInfo;
import feign.hystrix.FallbackFactory;


/**
 * 
* @ClassName: RoleFallback
* @Description: 模块间角色feign调用熔断降级处理
* @author author
* @date 2020年9月4日 上午9:54:24
*
 */
@Component
public class RoleFallback implements FallbackFactory<RoleInterface>{
private static Logger logger = LoggerFactory.getLogger(RoleFallback.class);
	


/**
 * 
* @Title: handleFeignError
* @Description: 统一记录异常信息
* @author author
* @param cause
* @return
* @return Map<String,Object>
 */
	private Map<String,Object> handleFeignError(Throwable cause){
		Map<String,Object> map = new HashMap<String,Object>();
		String message = "角色接口调用故障";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		cause.printStackTrace(new PrintStream(baos));  
		String exception = baos.toString(); 
		map.put("status", Constants.ITMP_RETURN_FAILURE);
		logger.error(message+":"+exception);
		map.put("errorMessage", message);
		return map;
	}



	@Override
	public RoleInterface create(Throwable cause) {
		return new RoleInterface(){

			@Override
			public Map<String, Object> getAllRole(String roleJson, Integer pageIndex,Integer pageSize) {
				return handleFeignError(cause);
			}

			@Override
			public Map<String, Object> insertRole(TblRoleInfo role) {
				return handleFeignError(cause);
			}

			@Override
			public Map<String, Object> findRoleById(Long roleId) {
				return handleFeignError(cause);
			}

			@Override
			public Map<String, Object> updateRole(TblRoleInfo role) {
				return handleFeignError(cause);
			}

			@Override
			public Map<String, Object> delRole(String roleIds,Long lastUpdateBy) {
				return handleFeignError(cause);
			}

			/*@Override
			public Map<String, Object> getRoleMenu(Long id) {
				return handleFeignError(cause);
			}*/

		};
	}



}
