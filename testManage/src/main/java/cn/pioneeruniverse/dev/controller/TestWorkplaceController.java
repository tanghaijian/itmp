package cn.pioneeruniverse.dev.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.dev.entity.TblDefectInfo;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblTestTask;
import cn.pioneeruniverse.dev.service.defect.DefectService;
import cn.pioneeruniverse.dev.service.testtask.TestTaskService;
import cn.pioneeruniverse.dev.service.workTask.WorkTaskService;

/**
* 测试这边的个人工作台
* @author tingting
* @version 2020年4月1日 下午1:51:02
*/
@RestController
@RequestMapping("/testWorkplace")
public class TestWorkplaceController extends BaseController {
	
	@Autowired
	private TestTaskService testTaskService;
	@Autowired
	private WorkTaskService workTaskService;
	@Autowired
	private DefectService defectService;
	
	@RequestMapping("/getTestUserDask")
	public Map<String,Object> getTestUserDask(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try {
			Long uid = CommonUtil.getCurrentUserId(request);
			//根据开发管理岗查询测试任务
			List<TblRequirementFeature> featureList = testTaskService.getReqFeatureByCurrentUser(uid);
			//根据执行人查询测试任务
			List<TblRequirementFeature> excuteFeatureList = testTaskService.getReqFeatureByExcuteCurrentUser(uid);
			
			List<TblTestTask> testTaskList = workTaskService.getTestTaskByCurrentUser(uid);
			List<TblDefectInfo> defectList = defectService.getDefectByCurrentUser(uid);
			map.put("featureList", featureList);
			map.put("excuteFeatureList", excuteFeatureList);
			
			map.put("testTaskList", testTaskList);
			map.put("defectList", defectList);
		}catch (Exception e) {
			return super.handleException(e, "操作失败！");
		}
		return map;
	}
}
