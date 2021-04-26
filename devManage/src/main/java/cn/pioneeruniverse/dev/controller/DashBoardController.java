package cn.pioneeruniverse.dev.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.velocity.tag.VelocityDataDict;
import cn.pioneeruniverse.dev.service.dashboard.DashBoardService;

/**
 * @ClassName: DashBoardController
 * @Description: DashBoard工能
 * @author author
 * @date Sep 7, 2020 14:33:07 AM
 *
 */
@RestController
@RequestMapping("dashBoard")
public class DashBoardController extends BaseController{
	
	@Autowired
	private DashBoardService dashBoardService;
		
	private static Logger log = LoggerFactory.getLogger(DashBoardController.class);

	/** 个人工作台
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * */
	@RequestMapping(value= "getUserDesk")
	public Map<String, Object> getUserDesk(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		try {
			Long id=CommonUtil.getCurrentUserId(request);				
			map=dashBoardService.getUserDesk(id);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return map;
	}

	/** 获取当前登陆人所属项目组
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * */
	@RequestMapping(value= "getAllProject")
	public Map<String, Object> getAllProject(HttpServletRequest request) {
		Long id = CommonUtil.getCurrentUserId(request);
		Map<String, Object> map = new HashMap<>();
		try {
			map = dashBoardService.getAllProject(id);
		}catch (Exception e) {
			log.error(e.getMessage(), e);		
		}
		return map;
	}

	 /** DashBoard
	 * @param systemId  系统id
	 * @param startDate 累计流图开始时间
	 * @param endDate   累计流图结束时间
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * */
	@RequestMapping(value= "getDashBoard")
	public Map<String, Object> getDashBoard(Long systemId,String startDate,String endDate) {
		Map<String,Object> map = new HashMap<>();
		try {			
			map=dashBoardService.getDashBoard(systemId,startDate,endDate);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return map;
	}

	/** 累计流图
	 * @param systemId  系统id
	 * @param startDate 累计流图开始时间
	 * @param endDate   累计流图结束时间
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * */
	@RequestMapping(value= "getTheCumulative")
	public Map<String, Object> getTheCumulative(Long systemId,String startDate,String endDate) {
		Map<String,Object> map = new HashMap<>();
		try {			
			map=dashBoardService.getTheCumulative(systemId,startDate,endDate);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return map;
	}
	/** 价值流图
	 * @param timeTraceId  系统id
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * */
	@RequestMapping(value= "valueStreamMapping")
	public Map<String, Object> valueStreamMapping(Long timeTraceId){
		Map<String,Object> map = new HashMap<>();
		try {
			map=dashBoardService.valueStreamMapping(timeTraceId);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return map;
	}
	/** 获取投产窗口关联开发任务状态
	 * @param windowId 投产窗口id
	 * @param systemId 系统id
	 *@return  Map<String, Object>
	 * */
	@RequestMapping(value= "getFratureStatus")
	public Map<String, Object> getFratureStatus(@RequestParam(value="windowId")Long windowId,
												@RequestParam(value="systemId")Long systemId) {
		Map<String, Object> map = new HashMap<>();
		try {
			map=dashBoardService.getFratureStatus(windowId,systemId);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return map;
	}

	/** 获取子系统代码质量情况
	 * @param systemModuleId 子模块id
	 *
	 * */
	@RequestMapping(value= "getSonarByModuleId")
	public Map<String, Object> getSonarByModuleId(Long systemModuleId) {
		Map<String, Object> map = new HashMap<>();
		try {
			map=dashBoardService.getSonarByModuleId(systemModuleId);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return map;
	}
	/** 环境类型
	 * @param environmentType 环境类型id
	 *
	 * */
	@RequestMapping(value= "getEnvironmentType")
	public String getEnvironmentType(Long environmentType) {
		JSONObject jsonObj = new JSONObject();
		VelocityDataDict dict= new VelocityDataDict();
		try {
			Map<String, String> result=dict.getDictMap("TBL_SYSTEM_SCM_ENVIRONMENT_TYPE");
			if(environmentType!=null) {
				for(Map.Entry<String, String> entry:result.entrySet()){
					if(Long.valueOf(entry.getKey())==environmentType) {
						jsonObj.put("typeValue",entry.getValue());
						break;
					}
				}
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return jsonObj.toJSONString();
	}
}
