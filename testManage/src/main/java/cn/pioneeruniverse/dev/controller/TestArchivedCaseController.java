package cn.pioneeruniverse.dev.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.entity.TblArchivedCase;
import cn.pioneeruniverse.dev.entity.TblCaseInfo;
import cn.pioneeruniverse.dev.entity.TblCaseStep;
import cn.pioneeruniverse.dev.service.testArchivedCase.TestArchivedCaseService;

@RestController
@RequestMapping("testArchivedCase")
public class TestArchivedCaseController extends BaseController {

	@Autowired
	private TestArchivedCaseService testArchivedCaseService;
	
	/**
	 * 归档案例列表展示
	 * @param tblArchivedCase
	 * @param filters
	 * @param page
	 * @param rows
	 * @return
	 * wjdz
	 * 2019年3月19日
	 * 上午11:04:28
	 */
	@RequestMapping(value="getArchivedCases",method=RequestMethod.POST)
	public Map<String, Object> getArchivedCases(TblArchivedCase tblArchivedCase,String filters, Integer page,Integer rows,HttpServletRequest request){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblArchivedCase> list = testArchivedCaseService.getArchivedCases(tblArchivedCase, filters, page, rows, request);
			List<TblArchivedCase> list2 = testArchivedCaseService.getArchivedCases(tblArchivedCase, filters ,1,Integer.MAX_VALUE, request);
			double total = Math.ceil(list2.size()*1.0/rows);
			map.put("records", list2.size());    //查询的总条目数
			map.put("page", page);					//当前页
			map.put("total", total);             //总页数
			map.put("data", list);				//每页数据
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 归档案例详情和编辑数据回显
	 * @param id 案例id
	 * @return Map<String, Object>
	 * wjdz
	 * 2019年3月19日
	 * 上午11:06:46
	 */
	@RequestMapping(value="getArchivedCaseById",method=RequestMethod.POST)
	public Map<String, Object> getArchivedCaseById(Long id){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblArchivedCase tblArchivedCase = testArchivedCaseService.getArchivedCaseById(id);
			map.put("data", tblArchivedCase);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 新建归档案例  同时也新建测试案例
	 * @param tblCaseInfo 案例信息
	 * @param request
	 * @return Map<String, Object>
	 * wjdz
	 * 2019年3月19日
	 * 下午5:00:42
	 */
	@RequestMapping(value="insertArchivedCase",method=RequestMethod.POST)
	public Map<String, Object> insertArchivedCase( String tblCaseInfo,String caseSteps, HttpServletRequest request){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblCaseInfo caseInfo = JSON.parseObject(tblCaseInfo, TblCaseInfo.class);
			List<TblCaseStep> list = JSONArray.parseArray(caseSteps, TblCaseStep.class);
			caseInfo.setCaseSteps(list);
			testArchivedCaseService.insertTestCase(caseInfo, request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 移除归档案例
	 * @param ids
	 * @return
	 * wjdz
	 * 2019年3月20日
	 * 上午10:14:22
	 */
	@RequestMapping(value="removeArchivedTest",method=RequestMethod.POST)
	public Map<String, Object> removeArchivedTest(@RequestParam("ids[]")List<Long> ids, HttpServletRequest request){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			testArchivedCaseService.removeArchivedTest(ids, request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
	}
	
	//导出模板
	@RequestMapping(value="exportTemplet")
	public Map<String, Object> exportTemplet(HttpServletRequest request, HttpServletResponse response){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			testArchivedCaseService.exportTemplet(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 导入归档案例
	 * @param file
	 * @param request
	 * @return
	 * wjdz
	 * 2019年3月20日
	 * 下午3:09:02
	 */
	@RequestMapping(value="importExcel",method=RequestMethod.POST)
	public Map<String, Object> importExcel(MultipartFile file,Integer caseCatalogId,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> map = testArchivedCaseService.importExcel(file,caseCatalogId, request);
			result.putAll(map);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 导出归档案例
	 * @param
	 * @param filters
	 * @param request
	 * @param response
	 * @throws Exception
	 * wjdz
	 * 2019年3月21日
	 * 下午4:59:09
	 */
	@RequestMapping(value="exportExcel")
	public void exportExcel(TblArchivedCase tblArchivedCase,String filters, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String f = null;
		if(filters.equals("undefined")) {
			filters = null;
			f = filters;
		}else {
			f = filters.replaceAll("\\\\", "");
			f = f.substring(1,f.length()-1);
		}
		List<TblArchivedCase> list = testArchivedCaseService.getCaseAndSteps(tblArchivedCase, f);
		testArchivedCaseService.exportExcel(list, request, response);
	}
	
	/**
	 * 案例编辑
	 * @param tblCaseInfo
	 * @param request
	 * @return
	 * wjdz
	 * 2019年2月19日
	 * 上午11:26:25
	 */
	@RequestMapping(value="editCaseInfo",method=RequestMethod.POST)
	public Map<String, Object> editCaseInfo(String tblCaseInfo,String caseSteps, HttpServletRequest request){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblCaseInfo caseInfo = JSON.parseObject(tblCaseInfo, TblCaseInfo.class);
			List<TblCaseStep> list = JSONArray.parseArray(caseSteps, TblCaseStep.class);
			caseInfo.setCaseSteps(list);
			testArchivedCaseService.editArchivedCase(caseInfo, request);
			testArchivedCaseService.editCaseInfo(caseInfo, request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			super.handleException(e, e.getMessage());
		}
		return map;
	}
	
}
