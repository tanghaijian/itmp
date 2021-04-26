package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.service.CustomFieldTemplate.ICustomFieldTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 拓展字段controller
 * Author:
 * Date: 2018/12/10 下午 1:41
 */
@RestController
@RequestMapping("fieldTemplate")
public class CustomFieldTemplateController extends BaseController {

	@Autowired
	private ICustomFieldTemplateService iCustomFieldTemplateService;

	/**
	 * 测试任务扩展字段数据查询
	 * @return Map<String, Object>
	 */
	@RequestMapping("/findFieldByReqFeature")
	public Map<String, Object> findFieldByReqFeature() {
		Map<String, Object> map = new HashMap<>();
		try {
			List<ExtendedField> extendedFields=iCustomFieldTemplateService.findFieldByReqFeature(null);
			map.put("field", extendedFields);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}

	/**
	 * 缺陷扩展字段数据查询
	 * @return Map<String, Object>
	 */
	@RequestMapping("/findFieldByDefect")
	public Map<String, Object> findFieldByDefect() {
		Map<String, Object> map = new HashMap<>();
		try {
			List<ExtendedField> extendedFields=iCustomFieldTemplateService.findFieldByDefect(null);
			map.put("field", extendedFields);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}

	/**
	 * 测试案例扩展字段数据查询
	 * @return Map<String, Object>
	 */
	@RequestMapping("/findFieldByTestCase")
	public Map<String, Object> findFieldByTestCase() {
			Map<String, Object> map = new HashMap<>();
		try {
			List<ExtendedField> extendedFields=iCustomFieldTemplateService.findFieldByTestCase(null);
			map.put("field", extendedFields);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
}
