package cn.pioneeruniverse.system.feignFallback.company;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.system.feignInterface.company.CompanyInterface;
import cn.pioneeruniverse.system.vo.company.Company;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CompanyFallback  implements FallbackFactory<CompanyInterface> {
	private final static Logger logger = LoggerFactory.getLogger(CompanyFallback.class);

	@Override
	public CompanyInterface create(Throwable cause) {
		return new CompanyInterface(){

			@Override
			public List<Company> getCompany() {
				return null;
			}
		
		};
	}

	/**
	 * @author author
	 * @Description 返回错误信息 和 异常信息
	 * @Date 2020/9/3
	 * @param cause 异常
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 *     map.put("status", Constants.ITMP_RETURN_FAILURE); 失败状态
	 *     map.put("errorMessage", message); 错误信息
	 **/
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

}
