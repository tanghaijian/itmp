package cn.pioneeruniverse.project.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.project.feignInterface.ProjectManageWebToSystemInterface;
import com.alibaba.fastjson.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.velocity.tag.VelocityDataDict;
import cn.pioneeruniverse.dev.entity.ExtendedField;
import cn.pioneeruniverse.project.excel.ExcelUtil;
import cn.pioneeruniverse.project.entity.TblDeptInfo;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.feignInterface.ProjectManageWebToProjectManageInterface;

/**
 * @ClassName: RequirementController
 * @Description: (需求模块)
 * @author author
 * @date Jul 28, 2020 11:33:07 AM
 *
 */
@RestController
@RequestMapping("requirement")
public class RequirementController extends BaseController{

	private static Logger logger = LoggerFactory.getLogger(RequirementController.class);
	
	@Autowired
	private ProjectManageWebToProjectManageInterface requirementInterface;
	@Autowired
	private ProjectManageWebToSystemInterface userInterface;
	@Autowired
	private RedisUtils redisUtils;
	@Value("${requirement.att.url}")
	private String reqAttUrl;
	/**
	 *
	 * @Title: toRequirementManage
	 * @Description: 需求管理列表
	 * @author author
	 * @param request
	 * @return
	 */
	@RequestMapping(value="toRequirementManage")
	public ModelAndView RequirementManage(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.PROJECT_MANAGE_UI_URL+request.getRequestURI());
		view.addObject("reqAttUrl",reqAttUrl);
		view.setViewName("projectManagement/RequirementManage");
		return view;
	}

	/**
	 *
	 * @Title: getData
	 * @Description: 获取需求
	 * @author author
	 * @param rIds 需求id
	 * @return
	 */
	@RequestMapping(value="getData",method = RequestMethod.POST)
	public Map<String, Object> getData(Long rIds){
		Map<String, Object> result = new HashMap<>();
		try {
			List<TblDeptInfo> depts = userInterface.getDept();
			//List<TblUserInfo> users = userInterface.getUser();
			//result.put("users", users);
			result.put("depts", depts);
			if (rIds!=null && !"".equals(rIds.toString())) {
				Map<String, Object> map = requirementInterface.toEditRequirementById(rIds);
				result.putAll(map);
			}			
		} catch (Exception e) {
			result.put("status", "fail");
		}
		
		return result;
		
	}
	/**
	 *
	 * @Title: toRequirementDetail
	 * @Description: 详情
	 * @author author
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "toRequirementDetail")
	public ModelAndView toRequirementDetail(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("token", CommonUtil.getToken(request));
		modelAndView.addObject("url",Constants.DEV_MANAGE_UI_URL+request.getRequestURI());
		modelAndView.addObject("uid",CommonUtil.getCurrentUserId(request));
		modelAndView.addObject("username",CommonUtil.getCurrentUserName(request));
		modelAndView.addObject("reqAttUrl",reqAttUrl);
		modelAndView.setViewName("projectManagement/requirementDetail");
		return modelAndView;
	}

	/**
	 *
	 * @Title: getExcelRequirement
	 * @Description: 导出
	 * @author author
	 * @param excelData 查询条件
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getExcelRequirement")
	public void getExcelRequirement(String excelData, HttpServletRequest request, HttpServletResponse response) {
		List<TblRequirementInfo> list = new ArrayList<TblRequirementInfo>();
		try {
			VelocityDataDict dict= new VelocityDataDict();
			Map<String, String> result=dict.getDictMap("TBL_REQUIREMENT_INFO_REQUIREMENT_STATUS");
			Map<String, String> result1=dict.getDictMap("TBL_REQUIREMENT_INFO_REQUIREMENT_SOURCE");
			Map<String, String> result2=dict.getDictMap("TBL_REQUIREMENT_INFO_REQUIREMENT_TYPE");
			Map<String, String> result3=dict.getDictMap("TBL_REQUIREMENT_INFO_REQUIREMENT_PRIORITY");
			Map<String, String> result4=dict.getDictMap("TBL_REQUIREMENT_INFO_REQUIREMENT_PLAN");			
			Map<String, String> result5=dict.getDictMap("TBL_REQUIREMENT_INFO_IMPORTANT_REQUIREMENT_TYPE");
			Map<String, String> result6=dict.getDictMap("TBL_REQUIREMENT_INFO_REQUIREMENT_PROPERTY");
			Map<String, String> result7=dict.getDictMap("TBL_REQUIREMENT_INFO_REQUIREMENT_CLASSIFY");
			Map<String, String> result8=dict.getDictMap("TBL_REQUIREMENT_INFO_REQUIREMENT_SUBDIVISION");

			Long uid = CommonUtil.getCurrentUserId(request);
			LinkedHashMap map = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
			List<String> roleCodes = (List<String>) map.get("roles");
			list=requirementInterface.getExcelRequirement(excelData,uid,roleCodes);
			// list=userInterface.getExcelAllUser();
			String[] title = {"需求编号","需求名称","需求状态","需求来源","需求类型","需求优先级","需求计划","期望上线","计划上线",
					"实际上线","提出人","归属部门","开发处室","父需求","创建日期","更新日期","开始日期","重点需求","重点需求类型",
					"重点需求计划上线季度","是否延期","需求延误原因","变更次数","开发管理","需求管理","业务验收","需求描述",
					"计划联调日期","实际联调日期","直接收益","远期收益","隐性收益","直接成本节约","远期成本节约","预期收益","预期成本",
					"需求是否挂起","需求挂起日期","需求性质","需求分类","细分类型","验收描述","验收时效","是否数据迁移","工作量",
					"关闭时间","关联需求","系统"};
			
			//如果有自定义字段拼接title
			List<ExtendedField> extendedFields2 = findRequirementField(null);
			String string = "";
			if(extendedFields2!=null && extendedFields2.size()>0) {
				for (String t : title) {
					string+=t+",";
				}
				for (ExtendedField extendedField : extendedFields2) {
					String str = extendedField.getLabel()+"(自定义字段："+extendedField.getFieldName()+")";
					string+=str+",";
				}
				title = string.split(",");
			}
				
			// excel文件名
			String fileName = "需求信息" + DateUtil.getDateString(new Timestamp(new Date().getTime()),"yyyyMMddHHmmss")+ ".xls";
			// sheet名
			String sheetName = "需求信息表";
            String[][] content=null;
			if (list.size() > 0 && list != null) {
			    content = new String[list.size()][];
				for (int i = 0; i < list.size(); i++) {
					content[i] = new String[title.length];
					// TblUserInfo obj = list.get(i);
					content[i][0] = list.get(i).getRequirementCode();
					content[i][1] = list.get(i).getRequirementName();										
					content[i][2] = getData(result,list.get(i).getRequirementStatus());			
					content[i][3] = getData(result1,list.get(i).getRequirementSource());			
					content[i][4] = getData(result2,list.get(i).getRequirementType());									
					content[i][5] = getData(result3,list.get(i).getRequirementPriority());					
					content[i][6] = getData(result4,list.get(i).getRequirementPlan());
					content[i][7] = DateUtil.formatDate(list.get(i).getExpectOnlineDate(), DateUtil.fullFormat);
					content[i][8] = DateUtil.formatDate(list.get(i).getPlanOnlineDate(), DateUtil.fullFormat);
					content[i][9] = DateUtil.formatDate(list.get(i).getActualOnlineDate(), DateUtil.fullFormat);
					content[i][10] = getUserName(list.get(i).getApplyUserId());
					content[i][11] = getDeptName(list.get(i).getApplyDeptId());
					content[i][12] = getDeptName(list.get(i).getDevelopmentDeptId());
					content[i][13] = getRequirements(list.get(i).getParentId()==null?"":list.get(i).getParentId().toString());
					content[i][14] = DateUtil.getDateString(list.get(i).getCreateDate(),DateUtil.fullFormat);	
					content[i][15] = DateUtil.getDateString(list.get(i).getLastUpdateDate(),DateUtil.fullFormat);	
					content[i][16] = DateUtil.formatDate(list.get(i).getOpenDate(),DateUtil.fullFormat);
					content[i][17] = getWhether(list.get(i).getImportantRequirementStatus());										
					content[i][18] = getData(result5,list.get(i).getImportantRequirementType());
					content[i][19] = list.get(i).getImportantRequirementOnlineQuarter();	
					content[i][20] = getWhether(list.get(i).getImportantRequirementDelayStatus());	
					content[i][21] = list.get(i).getImportantRequirementDelayReason();	
					content[i][22] = getChangeCount(list.get(i).getChangeCount());	
					content[i][23] = getUserName(list.get(i).getDevelopmentManageUserId());
					content[i][24] = getUserName(list.get(i).getRequirementManageUserId());
					content[i][25] = getUserName(list.get(i).getRequirementAcceptanceUserId());
					content[i][26] = list.get(i).getRequirementOverview();
					
					content[i][27] = DateUtil.formatDate(list.get(i).getPlanIntegrationTestDate(), DateUtil.fullFormat);
					content[i][28] = DateUtil.formatDate(list.get(i).getActualIntegrationTestDate(), DateUtil.fullFormat);
					content[i][29] = list.get(i).getDirectIncome()==null?"":list.get(i).getDirectIncome().toString();
					content[i][30] = list.get(i).getForwardIncome()==null?"":list.get(i).getForwardIncome().toString();
					content[i][31] = list.get(i).getRecessiveIncome()==null?"":list.get(i).getRecessiveIncome().toString();
					content[i][32] = list.get(i).getDirectCostReduction()==null?"":list.get(i).getDirectCostReduction().toString();
					content[i][33] = list.get(i).getForwardCostReduction()==null?"":list.get(i).getForwardCostReduction().toString();
					content[i][34] = list.get(i).getAnticipatedIncome();
					content[i][35] = list.get(i).getEstimateCost();
					content[i][36] = getWhether(list.get(i).getHangupStatus());
					content[i][37] = DateUtil.formatDate(list.get(i).getHangupDate(), DateUtil.fullFormat);
					content[i][38] = getData(result6,list.get(i).getRequirementProperty());
					content[i][39] = getData(result7,list.get(i).getRequirementClassify());
					content[i][40] = getData(result8,list.get(i).getRequirementSubdivision());
					content[i][41] = list.get(i).getAcceptanceDescription();
					content[i][42] = list.get(i).getAcceptanceTimeliness();
					content[i][43] = getWhether(list.get(i).getDataMigrationStatus());
					content[i][44] = String.valueOf(list.get(i).getWorkload());
					content[i][45] = DateUtil.formatDate(list.get(i).getCloseTime(), DateUtil.fullFormat);
					content[i][46] = getRequirements(list.get(i).getRequirementIds());
					content[i][47] = getsystems(list.get(i).getId());
					
					List<ExtendedField> extendedFields = findRequirementField(list.get(i).getId());
					if(extendedFields!=null && extendedFields.size()>0) {
						for(int j=0;j<extendedFields.size();j++) {
							content[i][47+j+1] = extendedFields.get(j).getValueName()==null?"":	extendedFields.get(j).getValueName();
						}
					}
					
				}
			}
			// 创建HSSFWorkbook
			HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
			String useragent = request.getHeader("User-Agent");
			if (useragent.contains("Firefox")) {
				//fileName = "=?UTF-8?B?" + new BASE64Encoder().encode(fileName.getBytes("utf-8")) + "?=";				
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
	        } else {
	        	fileName = URLEncoder.encode(fileName, "utf-8");
	        	fileName = fileName.replace("+", " ");
	        }
			
			response.setContentType("application/vnd.ms-excel");
	        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e); 
		}
	}


	/**
	 *
	 * @Title: findRequirementField
	 * @Description: 查询需求拓展字段
	 * @author author
	 * @param id 需求ID
	 * @return
	 */
	private List<ExtendedField> findRequirementField(Long id) {
		Map<String,Object> map = requirementInterface.findRequirementField(id);
		Object obj = map.get("fields");
		String arr = JSONArray.toJSONString(obj);
		List<ExtendedField> list = JSONObject.parseArray(arr, ExtendedField.class);
		return list;
	}

	private String getsystems(Long id) {
		// TODO Auto-generated method stub
		String systems = "";
		List<String> list = requirementInterface.getsystems(id);
		if(list!=null && list.size() > 0) {
			systems = StringUtils.join(list, ",");
		}
		return systems;
	}
	
	// 发送响应流方法
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static String getData(Map<String, String> result1,String data) {
		String result="";
		if(data!=null) {		
			for(Map.Entry<String, String> entry:result1.entrySet()){
				if(entry.getKey().toLowerCase(Locale.ENGLISH).equals(data.toLowerCase(Locale.ENGLISH))) {
					result= entry.getValue().toString();
					break;
				}
			}
		}
		return result;		
	}
	
	private String getUserName(Long userId) {
		String userName="";
		if(userId != null && userId != 0){
			Map<String,Object> map = userInterface.findUserById(userId);
			if(map!=null&&map.size()>1) {
				String user = map.get("data").toString();
				if (user == null || user.equals("null")) {
				} else {
					Map<String, Object> ObjectSec = JSONObject.parseObject(user);
					userName = ObjectSec.get("userName").toString();
				}
			}
		}
		return userName;
	}
	
	private String getDeptName(Long deptId) {
		String deptName="";
		if(deptId != null) {
			Map<String, Object> map = userInterface.selectDeptById(deptId);
			if (map != null && map.size() > 1) {
				String dept = map.get("data").toString();
				if (dept == null || dept.equals("null")) {
				} else {
					Map<String, Object> ObjectSec = JSONObject.parseObject(dept);
					deptName = ObjectSec.get("deptName").toString();
				}
			}
		}
		return deptName;
	}
	
	private String getReqName(Long parentId) {
		String reqName = "";
		if(parentId!=null) {
			Map<String, Object> map = requirementInterface.toEditRequirementById(parentId);
			if (map != null && map.size() > 1) {
				String req = JSONObject.toJSONString(map.get("data"));
				if (req == null || req.equals("null")) {
				} else {
					Map<String, Object> ObjectSec = JSONObject.parseObject(req);
					reqName = ObjectSec.get("requirementCode").toString();
				}
			}
		}
		return reqName;
	}
	
	private String getRequirements(String reqIds) {
		String requirementCodes = "";
		if(reqIds != null) {
			List<String> list = requirementInterface.getRequirementsByIds(reqIds);
			if(list!=null && list.size() > 0) {
				requirementCodes = StringUtils.join(list,",");
			}
		}
		return requirementCodes;
	}
	
	private static String getWhether(String id) {
		String bool="";
		if(id!=null&&id.equals("1")) {
			bool="是";
		}else if(id!=null&&id.equals("2")){
			bool="否";
		}
		return bool;
	}

	private static String getChangeCount(Long id) {
		String number="";
		if(id!=null) {
			number=id.toString();		
		}
		return number;
	}
}
