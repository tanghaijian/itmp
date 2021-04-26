package cn.pioneeruniverse.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.project.entity.TblRequirementFeature;
import cn.pioneeruniverse.project.service.requirement.RequirementFeatureService;

@RestController
@RequestMapping("requirementFeature")
public class RequirementFeatureController extends BaseController{
	
	@Autowired
	private RequirementFeatureService requirementFeatureService;
	
	private static Logger log = LoggerFactory.getLogger(RequirementFeatureController.class);
	
	/**
	 * 每日跑批：查询表tbl_requirement_feature数据：
	 * where条件：状态为非完成实施并且取消，并且status=1 或者满足在表tbl_sprint_info所选日期内存在
	 * 插入到表tbl_requirement_feature_history.REQUIREMENT_FEATURE_ID与CREATE_DATE唯一。
	 * 合计tbl_requirement_feature_history预估剩余工作量插入tbl_sprint_burnout
	 * @param parameterJson
	 * @return
	 */
	@RequestMapping(value = "executeFeatureToHistoryJob", method = RequestMethod.POST)
	public Map<String, Object> executeFeatureToHistoryJob(@RequestBody String parameterJson) {
		Map<String, Object> result = new HashMap<String, Object>();	
		try {
			log.info("tbl_requirement_feature历史跑批，tbl_sprint_burnout燃尽图跑批：开始");
			requirementFeatureService.executeFeatureToHistoryJob(parameterJson);
			log.info("tbl_requirement_feature历史跑批，tbl_sprint_burnout燃尽图跑批：结束");
		} catch (Exception e) {
			log.error("mes:" + e.getMessage(), e);
			return handleException(e, "执行需求跑批失败");			
		}
		return result;
	}
		 
	/**
	* @author author
	* @Description 模糊匹配(未使用)
	* @Date 2020/9/18
	* @param request
* @param featureName
	* @return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@RequestMapping(value = "getAllFeature", method = RequestMethod.POST)
	public Map<String, Object> getAllFeature(HttpServletRequest request, String featureName) {
		Map<String, Object> result = new HashMap<String, Object>();	
		try {
			List<TblRequirementFeature> trf = requirementFeatureService.getAllFeature(featureName);
			result.put("trf",trf);
		} catch (Exception e) {
			log.error("mes:" + e.getMessage(), e);
			return handleException(e, "获取需求失败");			
		}
		return result;
	}	
}
