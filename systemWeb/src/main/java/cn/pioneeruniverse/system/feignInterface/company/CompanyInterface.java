package cn.pioneeruniverse.system.feignInterface.company;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.pioneeruniverse.system.vo.company.Company;

/**
 * 
* @ClassName: CompanyInterface
* @Description: 系统前端调用后端公司接口
* @author author
* @date 2020年8月19日 下午9:10:46
*
 */
@FeignClient(value="system",fallbackFactory=CompanyInterface.class)
public interface CompanyInterface {
	
	/**
	 * 
	* @Title: getCompany
	* @Description: 获取所有公司，填充下拉框使用
	* @author author
	* @return List<Company>公司列表
	 */
	@RequestMapping(value="company/getCompany",method=RequestMethod.POST)
	List<Company> getCompany();
}
