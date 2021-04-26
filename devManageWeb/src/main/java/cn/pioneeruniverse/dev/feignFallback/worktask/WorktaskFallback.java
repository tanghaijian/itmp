package cn.pioneeruniverse.dev.feignFallback.worktask;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.dev.feignInterface.worktask.WorktaskInterface;
import feign.hystrix.FallbackFactory;

/**
 * @author author
 * @Description 非使用文件
 * @Date 2020/9/4
 * @return
 **/
@Component
public class WorktaskFallback implements FallbackFactory<WorktaskInterface>{
	  private static final Logger logger = LoggerFactory.getLogger(WorktaskInterface.class);
		 
		
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
	public WorktaskInterface create(Throwable cause) {
		return new WorktaskInterface() {
		
	};
	}
}
