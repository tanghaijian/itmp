package cn.pioneeruniverse.dev.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.entity.TblCaseInfo;
import cn.pioneeruniverse.dev.entity.TblCaseStep;
import cn.pioneeruniverse.dev.entity.TblTestSet;
import cn.pioneeruniverse.dev.entity.TblTestSetCase;
import cn.pioneeruniverse.dev.entity.TblTestSetCaseStep;
import cn.pioneeruniverse.dev.service.testCaseManage.TestCaseManageService;
import cn.pioneeruniverse.dev.service.testSet.ITestSetService;

@RestController
@RequestMapping("testSet")
public class TestSetController extends BaseController {

	@Autowired
	private ITestSetService testSetService;
	@Autowired
	private TestCaseManageService testCaseManageService;
	@Autowired
	private RedisUtils redisUtils;

	/**
	 *@author liushan
	 *@Description 根据测试案例获取步骤
	 *@Date 2020/4/2
	 *@Param [testSetCaseList, request]
	 *@return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	@PostMapping(value="getCaseStepByCaseId")
	public Map<String, Object> getCaseStepByCaseId(Long testCaseId,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			result.put("caseSteps", testSetService.getCaseStepByCaseId(testCaseId,request));
		} catch (Exception e) {
			return super.handleException(e,"操作失败！");
		}
		return result;
	}

	/**
	*@author liushan
	*@Description 表格行拖动
	*@Date 2020/4/2
	*@Param [testSetCaseList, request]
	*@return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@PostMapping(value="moveIndexTestSetCase")
	public Map<String, Object> moveIndexTestSetCase(Long testSetId,Long testCaseId,Integer beforeIndex,Integer laterIndex,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			testSetService.moveIndexTestSetCase(testSetId,testCaseId,beforeIndex,laterIndex,request);
		} catch (Exception e) {
			return super.handleException(e,"操作失败！");
		}
		return result;
	}

	/**
	 *@author liushan
	 *@Description 输入排序号拖动
	 *@Date 2020/3/25
	 *@Param [testSetCase, request]
	 *@return void
	 **/
	@PostMapping(value="moveTestSetCase")
	public Map<String, Object> moveTestSetCase(@RequestBody Map<String,Object> param,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			testSetService.moveTestSetCase(param,request);
		} catch (Exception e) {
			return super.handleException(e,"操作失败！");
		}
		return result;
	}

	/**
	 *@author liushan
	 *@Description 复制添加多个案例
	 *@Date 2020/3/23
	 *@Param []
	 *@return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	@PostMapping(value="copyInsertTestCases")
	public Map<String, Object> copyInsertTestCases(Long testSetId,Long[] testCaseIds,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			testSetService.copyInsertTestCases(testSetId,testCaseIds,request);
		} catch (Exception e) {
			return super.handleException(e,"操作失败！");
		}
		return result;
	}

	/**
	*@author liushan
	*@Description 查询所有的测试工作任务及所属的测试集
	*@Date 2020/3/17
	*@Param [page, rows, testSet, filters]
	*@return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@PostMapping("/selectTestTaskWithTestSet")
	public Map<String, Object> selectTestTaskWithTestSet(@RequestBody Map<String,Object> testTaskSet,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = testSetService.selectTestTaskWithTestSet(testTaskSet,request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			return super.handleException(e, "操作失败！");
		}
		return map;
	}

	/**
	*@author liushan
	*@Description 根据测试工作任务查询测试集
	*@Date 2020/3/18
	*@Param [testTaskId, request]
	*@return java.util.Map<java.lang.String,java.lang.Object>
	**/
	@PostMapping("/getTestSetBytestTaskId")
	public Map<String, Object> getTestSetBytestTaskId(@RequestBody Map<String, Object> param,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			map.put("testSetList",testSetService.getTestSetBytestTaskId(param,request));
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 获取所有的测试集
	 *
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getAllTestSet")
	public Map<String, Object> getAllTestSet(Integer page, Integer rows, TblTestSet testSet, String filters,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			testSet.setUid(CommonUtil.getCurrentUserId(request));
			LinkedHashMap usermap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
			List<String> roleCodes = (List<String>) usermap.get("roles");
			map = testSetService.getTestSet(page, rows, testSet,roleCodes, 1);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 获取所有的测试集
	 *
	 * @param testSet
	 * @param filters
	 * @return
	 */
	@RequestMapping("/getAllTestSet3")
	public Map<String, Object> getAllTestSet3(String nameOrNumber,String createBy,Long testTaskId,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = testSetService.getAllTestSet(nameOrNumber,createBy,testTaskId);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 获取所有的测试集(bootstrap)
	 *
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getAllTestSet2")
	public Map<String, Object> getAllTestSet2(Integer page, Integer rows, TblTestSet testSet) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = testSetService.getTestSet2(page, rows, testSet, 2);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}


	/**
	 * 根据测试任务ID查询测试集
	 *
	 * @param page
	 * @param rows
	 * @param testSet
	 * @return
	 */
	@RequestMapping("/getAllTestSetByFeatureId")
	public Map<String, Object> getAllTestSetByFeatureId(Long featureId) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<TblTestSet> list = testSetService.getTestSetByFeatureId(featureId);
			map.put("rows", list);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 获取关联的系统
	 *
	 * @param testSetId
	 * @return
	 */
	@RequestMapping("/getRelateSystem")
	public Map<String, Object> getRelateSystem(Long testSetId) {
		Map<String, Object> map = new HashMap<>();
		try {
			Map<String, Object> system = testSetService.getRelateSystem(testSetId);
			map.put("row", system);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 新增测试集
	 *
	 * @param testSet
	 * @param request
	 * @return
	 */
	@RequestMapping("/addTestSet")
	public Map<String, Object> addTestSet(TblTestSet testSet, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = testSetService.insertTestSet(testSet, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}
	/**
	 * 根据id获取测试集
	 * @param testSetId
	 * @return
	 */
	@RequestMapping(value = "findOneTestSet")
	public Map<String, Object> findOneTestSet(Long testSetId) {
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> map = testSetService.findOneTestSet(testSetId);
			result.put("rows", map == null?new HashMap<>():map);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e,  "操作失败！");
		}
		return result;
	}
	/**
	 * 获取测试集案例jqgrid
	 *
	 * @param page
	 * @param rows
	 * @param testSetCase
	 * @return
	 */
	@RequestMapping("/getTestSetCase")
	public Map<String, Object> getTestSetCase(Integer page, Integer rows, TblTestSetCase testSetCase) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = testSetService.selectTestSetCaseByPage(page, rows, testSetCase);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 获取测试集案例 bootstrap
	 *
	 * @param page
	 * @param rows
	 * @param testSetCase
	 * @return
	 */
	@RequestMapping("/getTestSetCaseBootStrap")
	public Map<String, Object> getTestSetCaseBootStrap(String testSetMap){
			/*Integer pageNumber, Integer pageSize, TblTestSetCase testSetCase) {*/
		Map<String, Object> map = new HashMap<>();
		try {
			 Map<String, Object> params = (Map) JSON.parse(testSetMap);
			 TblTestSetCase testSetCase = JsonUtil.fromJson(testSetMap, TblTestSetCase.class);
			 Integer pageNumber = 1;
			 Integer pageSize = Integer.MAX_VALUE;
			map = testSetService.getTestSetCaseBootStrap(pageNumber, pageSize, testSetCase);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}


	/**
	 * 根据测试集Id查询测试集案例
	 *
	 * @param testSetId
	 * @return
	 */
	@RequestMapping("/getTestSetCaseByTestSetId")
	public Map<String, Object> getTestSetCaseByTestSetId(Integer page, Integer pageSize, Long testSetId,
			Integer executeRound, String executeResult) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = testSetService.getCaseByTestSetId(page, pageSize, testSetId, executeRound, executeResult);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 移除关联的测试集案例
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/removeTestSetCase")
	public Map<String, Object> removeTestSetCase(TblTestSetCase testSet, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			testSetService.updateCase(testSet, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}

	/**
	 * 批量移除关联的测试集案例
	 *
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping("/removeManyTestSetCase")
	public Map<String, Object> removeManyTestSetCase(Long testSetId,String ids, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			testSetService.updateManyCase(testSetId,ids, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 关联测试集案例
	 *
	 * @param testSetId
	 * @param caseStr
	 * @param request
	 * @return
	 */
	@RequestMapping("relateTestSetCase")
	public Map<String, Object> relateTestSetCase(Long testSetId, String caseStr, String idStr,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			testSetService.batchInsertCase(testSetId, caseStr, request);
			testSetService.batchInsertCaseStep(testSetId, idStr, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}
	/**
	 * 引入测试集案例---查询测试集案例
	 * @author tingting
	 * @param otherTestSetId 当前测试集的id
	 * @param testSetId 选中的测试集id
	 *
	 * */
	@RequestMapping("getOtherTestSetCase")
	public Map<String, Object> getOtherTestSetCase(Long otherTestSetId,Long testSetId,Long testTaskId,Long workTaskId,
			Integer page, Integer rows,HttpServletRequest request){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			map = testSetService.getOtherTestSetCase(otherTestSetId,testSetId,testTaskId,workTaskId,page,rows);
		} catch (Exception e) {
			return super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 引入选择测试集案例
	 * @author tingting
	 * @param flag 1 案例编号一致  、2 重新生成案例编号
	 * */
	@RequestMapping("leadInTestSetCase")
	public Map<String, Object> leadInTestSetCase(Long testSetId,String caseStr, String idStr,Integer flag,
			HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		try {
			testSetService.leadInTestSetCase(testSetId, caseStr,idStr,flag, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		}catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}
	/**
	 * 引入全部测试集案例
	 * @author tingting
	 * @param flag 1 案例编号一致  、2 重新生成案例编号
	 * */
	@RequestMapping("leadInAllTestSetCase")
	public Map<String, Object> leadInAllTestSetCase(Long otherTestSetId,Long testSetId,Long testTaskId,Long workTaskId,
			Integer flag,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		try {
			map = testSetService.leadInAllTestSetCase( otherTestSetId,testSetId,testTaskId,workTaskId,flag, request);
			
		}catch (Exception e) {
			map = super.handleException(e,  "引入失败！");
		}
		return map;
	}



	/**
	 * 废弃测试集
	 *
	 * @param testSetId
	 * @param caseStr
	 * @param request
	 * @return
	 */
	@RequestMapping("removeTestSet")
	public Map<String, Object> removeTestSet(Long id, Integer status, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			testSetService.updateTestSetStatus(id, status, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, "操作失败！");
		}
		return map;
	}

	/**
	 * 新增测试集案例(也需新增到案例表)
	 *
	 * @param testSetCase
	 * @param testSetCaseStep
	 * @param request
	 * @return
	 */
	@RequestMapping("addTestCase")
	public Map<String, Object> addTestCase(String testSetCaseStr, String testSetCaseStepStr,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			TblCaseInfo caseInfo = JSON.parseObject(testSetCaseStr, TblCaseInfo.class); // 新增案例
			List<TblCaseStep> list2 = JSON.parseArray(testSetCaseStepStr, TblCaseStep.class);
			caseInfo.setCaseSteps(list2);
			caseInfo = testCaseManageService.insertCaseInfo(caseInfo, request);

			TblTestSetCase testSetCase = JSON.parseObject(testSetCaseStr, TblTestSetCase.class); // 新增测试集案例
			testSetCase.setCaseNumber(caseInfo.getCaseNumber());
			List<TblTestSetCaseStep> list = JSON.parseArray(testSetCaseStepStr, TblTestSetCaseStep.class);
			testSetService.addTestSetCase(testSetCase, list, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 新增执行人
	 *
	 * @param testSetExcuteRoundUser
	 * @param request
	 * @return
	 */
	@RequestMapping("addExcuteUser")
	public Map<String, Object> addExcuteUser(Long testSetId, Integer executeRound, String userIdStr,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			testSetService.addExecuteUser(testSetId, executeRound, userIdStr, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 移除执行人
	 *
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("removeExcuteUser")
	public Map<String, Object> removeExcuteUser(Long id, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			testSetService.updateExcuteUserStatus(id, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 移除多个执行人
	 *
	 * @param idStr
	 * @param request
	 * @return
	 */
	@RequestMapping("removeManyExcuteUser")
	public Map<String, Object> removeManyExcuteUser(String idStr, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			testSetService.updateBatchStatus(idStr, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, "操作失败！");
		}
		return map;
	}

	/**
	 * 根据执行轮次获取执行人
	 *
	 * @param testSetId
	 * @param excuteRound
	 * @return
	 */
	@RequestMapping("getExcuteUserByRound")
	public Map<String, Object> getExcuteUserByRound(Long testSetId, Integer excuteRound) {
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> map = testSetService.getTestExcuteUser(testSetId, excuteRound);
			result.putAll(map);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e,  "操作失败！");
		}
		return result;
	}

	/**
	 * 获取用户列表(除去执行人)
	 *
	 * @param page
	 * @param rows
	 * @param testSetId
	 * @param executeRound
	 * @return
	 */
	@RequestMapping("getUserTable")
	public Map<String, Object> getExcuteUserByRound(Integer pageNumber, Integer pageSize, Long testSetId,
			Integer executeRound, String userName, String companyName, String deptName) {
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> map = testSetService.getUserTable(pageNumber, pageSize, testSetId, executeRound,
					userName.equals("") ? null : userName, companyName.equals("") ? null : companyName,
					deptName.equals("") ? null : deptName);
			result.putAll(map);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e,  "操作失败！");
		}
		return result;
	}

	/**
	 * 导出模板
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("exportTemplet")
	public Map<String, Object> exportTemplet(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		try {
			testSetService.exportTemplet(request, response);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e,  "操作失败！");
		}
		return map;
	}

	/**
	 * 导入案例
	 * @param testSetId
	 * @param file
	 * @param request
	 * @param systemId
	 * @return
	 */
	@RequestMapping("importCase")
	public Map<String, Object> importCase(Long testSetId, MultipartFile file, HttpServletRequest request, Integer type) {
		Map<String, Object> result = new HashMap<>(8);
		try {
			Map<String, Object> map = testSetService.importTestCase(file, testSetId, request, type);
			result.putAll(map);
		} catch (Exception e) {
			result = super.handleException(e,  "操作失败！");
		}
		return result;
	}

	/**
	 * 获取测试执行树
	 *
	 * @param userId
	 * @param taskName
	 * @param testSetName
	 * @param requirementFeatureStatus
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getTaskTree")
	public Map<String, Object> getTaskTree(Long own, String taskName, String testSetName,
			Integer requirementFeatureStatus, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		try {
			Long userId = null;
			if (own == 1) {
				userId = CommonUtil.getCurrentUserId(request);
			}

			List<Map<String, Object>> list = testSetService.getTaskTree(userId, taskName.equals("") ? null : taskName,
					testSetName.equals("") ? null : testSetName, requirementFeatureStatus);
			result.put("rows", list);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e,  "操作失败！");
		}
		return result;
	}

	/**
	 * 查询案例详情
	 * @param caseId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "showCase")
	public Map<String, Object> showCase(Long caseId) {
		Map<String, Object> result = new HashMap<>();
		try {
			List<TblTestSetCaseStep> list = testSetService.getTestSetCase(caseId);
			result.put("rows", list);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e,  "操作失败！");
		}
		return result;
	}
}
