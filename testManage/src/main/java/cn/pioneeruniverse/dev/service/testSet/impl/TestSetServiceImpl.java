package cn.pioneeruniverse.dev.service.testSet.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.dev.common.ExportExcel;
import cn.pioneeruniverse.dev.dao.mybatis.*;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.yiranUtil.PoiExcelUtil;
import com.alibaba.fastjson.JSONArray;
import jdk.nashorn.internal.ir.IdentNode;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.pioneeruniverse.common.bean.MergedRegionResult;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.ExcelUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.service.testSet.ITestSetService;

@Service
@Transactional(readOnly = true)
public class TestSetServiceImpl implements ITestSetService {
	@Autowired
	private TblTestTaskMapper testTaskMapper;
	@Autowired
	private TblTestSetMapper testSetMapper;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private TblTestSetCaseMapper testSetCaseMapper;
	@Autowired
	private TblCaseInfoMapper caseInfoMapper;
	@Autowired
	private TblTestSetCaseStepMapper testSetCaseStepMapper;
	@Autowired
	private TblCaseStepMapper caseStepMapper;
	@Autowired
	private TblTestSetExcuteRoundUserMapper testSetExcuteRoundUserMapper;
	@Autowired
	private TblSystemInfoMapper systemInfoMapper;
	@Autowired
	private TblUserInfoMapper userInfoMapper;

	/**
	*@author liushan
	*@Description selectTestTaskWithTestSet
	*@Date 2020/4/1
	*@Param [testTaskSet, request]
	*@return java.util.List<cn.pioneeruniverse.dev.entity.TblTestTask>
	**/
	@Override
	public  Map<String, Object> selectTestTaskWithTestSet(Map<String, Object> testTaskSet, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		testTaskSet.put("currentUserId",CommonUtil.getCurrentUserId(request));
		int pageNumber = Integer.parseInt(testTaskSet.get("pageNumber").toString());
		int pageSize = Integer.parseInt(testTaskSet.get("pageSize").toString());
		testTaskSet.put("pageNumber",(pageNumber - 1) * pageSize);
		testTaskSet.put("pageSize",pageSize);
		result.put("testTaskList",testTaskMapper.selectTestTaskByCuserAndExecution(testTaskSet));
		result.put("total", testTaskMapper.countTestTaskByCuserAndExecution(testTaskSet));
		return result;
	}

	/**
	*@author liushan
	*@Description 根据测试工作任务查询测试集
	*@Date 2020/3/18
	*@Param [testTaskId, request]
	*@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	**/
	@Override
	public List<Map<String, Object>> getTestSetBytestTaskId(Map<String, Object> param, HttpServletRequest request) {
		param.put("currentUserId",CommonUtil.getCurrentUserId(request));
		return testSetMapper.getTestSetByTestTaskId(param);
	}

	/**
	 *@author liushan
	 *@Description 复制添加多个案例
	 *@Date 2020/3/23
	 *@Param [testCaseIds]
	 *@return void
	 **/
	@Override
	@Transactional(readOnly = false)
	public void copyInsertTestCases(Long testSetId,Long[] testCaseIds, HttpServletRequest request) throws Exception {
		Long currentUserId = CommonUtil.getCurrentUserId(request);

		// 整体向下移动
		updateCaseOrder(testSetId, testCaseIds.length,currentUserId);
		
		for (int i = 0; i < testCaseIds.length ; i++) {
			Long id = testCaseIds[i];
			TblTestSetCase setCase = new TblTestSetCase(id,currentUserId);
			// 添加到案例管理表
			caseInfoMapper.insertByTestSetCaseId(setCase);
			caseStepMapper.insertByTestSetCaseId(id,setCase.getId(),currentUserId);

			TblCaseInfo caseInfo = caseInfoMapper.findCaseInfoById(setCase.getId());
			setCase = new TblTestSetCase(id,currentUserId,caseInfo.getCaseNumber(),++i);

			// 添加到测试集案例表
			testSetCaseMapper.insertById(setCase);
			testSetCaseStepMapper.insertByTestSetCaseId(id,setCase.getId(),currentUserId);
			i--;
		}
	}

	/**
	 *@author liushan
	 *@Description 表格行拖动
	 *@Date 2020/4/2
	 *@Param [testCaseId, laterIndex, request]
	 *@return void
	 **/
	@Override
	@Transactional(readOnly = false)
	public void moveIndexTestSetCase(Long testSetId, Long testCaseId, Integer beforeIndex, Integer laterIndex, HttpServletRequest request) throws Exception {
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		Map<String,Object> param = new HashMap<>();
		param.put("testSetId",testSetId);
		param.put("currentUserId",currentUserId);
		param.put("orderNum",1);
		if(beforeIndex > laterIndex){
			param.put("symbol","+");
			param.put("minIndex",laterIndex);
			param.put("maxIndex",beforeIndex-1);
		} else if(beforeIndex < laterIndex){
			param.put("symbol","-");
			param.put("minIndex",beforeIndex+1);
			param.put("maxIndex",laterIndex);
		}
		testSetCaseMapper.updateAddOrderByTestSetId(param);
		testSetCaseMapper.updateOrderByTestSetCaseId(testCaseId,currentUserId,laterIndex);
	}

	/**
	 *@author liushan
	 *@Description 输入排序号移动
	 *@Date 2020/3/25
	 *@Param [testSetCaseList, request]
	 *@return void
	 **/
	@Override
	@Transactional(readOnly = false)
	public void moveTestSetCase(Map<String,Object> param, HttpServletRequest request) throws Exception{
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		// 选中在最小最大范围以为未选中的数据
		List<TblTestSetCase> otherList = new ArrayList<>();
		param.put("currentUserId",currentUserId);
		List<TblTestSetCase> list = JSONArray.parseArray(param.get("testSetCases").toString().replaceAll("&quot;","\""),TblTestSetCase.class);
		param.put("list",list);
		Integer max = list.stream().mapToInt(TblTestSetCase::getOrderCase).max().getAsInt();
		Integer min = list.stream().mapToInt(TblTestSetCase::getOrderCase).min().getAsInt();
		Integer laterIndex = Integer.parseInt(param.get("laterIndex").toString());
		int size = list.size();
		Integer chooseIndex = null;
		Integer otherIndex = null;
		if(max.intValue() > laterIndex.intValue() && min.intValue() > laterIndex.intValue()){
			// 移动位置在所选list上方
			param.put("minIndex",laterIndex+1);
			param.put("maxIndex",max);
			otherList = testSetCaseMapper.findNoChooseCaseId(param);
			list.addAll(otherList);
			size = list.size();
			for (int i = 0; i < size; i++) {
				testSetCaseMapper.updateOrderByTestSetCaseId(list.get(i).getId(),currentUserId,laterIndex+1+i);
			}
			return;
		} else if(max.intValue() < laterIndex.intValue() &&  min.intValue() < laterIndex.intValue()){
			// 移动位置在所选list下方
			param.put("minIndex",min);
			param.put("maxIndex",laterIndex);
			otherList = testSetCaseMapper.findNoChooseCaseId(param);
			if(otherList != null && otherList.size() > 0){
				chooseIndex = min + otherList.size();
				otherIndex = min;
			}
		} else if(max.intValue() > laterIndex.intValue() && min.intValue() < laterIndex.intValue()){
			// 移动位置在所选list中间 例：4，5，9，10，13 移至 7 涉及范围 4~13 以外的6，7，8，11，12 分成 6~7和7~12
			param.put("minIndex",min);
			param.put("maxIndex",max);
			otherList = testSetCaseMapper.findNoChooseCaseId(param);
			Map<Boolean, List<TblTestSetCase>> splitList = otherList.stream().collect(Collectors.groupingBy( x -> x.getOrderCase() <= laterIndex));
			otherList = splitList.get(true);
			List<TblTestSetCase> falseList = splitList.get(false);
			if(falseList != null && falseList.size() > 0){
				list.addAll(falseList);
				size = list.size();
			}
			if(otherList != null && otherList.size() > 0){
				chooseIndex = min + otherList.size();
				otherIndex = min;
			}
		}

		if(chooseIndex != null && otherIndex != null){
			for (int i = 0; i < size; i++) {
				testSetCaseMapper.updateOrderByTestSetCaseId(list.get(i).getId(),currentUserId,chooseIndex+i);
			}
			for(int j = 0; j < otherList.size();j++){
				testSetCaseMapper.updateOrderByTestSetCaseId(otherList.get(j).getId(),currentUserId,otherIndex+j);
			}
		}
	}

	/**
	 * 查询测试集(分页)
	 * 
	 */
	@Override
	public Map<String, Object> getTestSet(Integer page, Integer rows, TblTestSet testSet,List<String> roleCodes, Integer tableType) {
		Map<String, Object> map = new HashMap<>();
		PageHelper.startPage(page, rows);
		List<Map<String, Object>> testSetList = null;
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")) {//当前登录用户有角色是系统管理员
			testSetList = testSetMapper.selectTestSet(testSet);
		}else {
			testSetList = testSetMapper.selectTestSetByCon(testSet);
		}
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(testSetList);
		if (tableType == 2) {
			map.put("total", pageInfo.getTotal());
			map.put("rows", pageInfo.getList());
		} else if(tableType == 1){
			map.put("rows", pageInfo.getList());
			map.put("total", pageInfo.getPages());
			map.put("records", pageInfo.getTotal());
			map.put("page", page);
		} else {
			map.put("rows", testSetList);
		}
		return map;
	}
	/**
	 * 查询测试集(分页)
	 * 
	 */
	@Override
	public Map<String, Object> getTestSet2(Integer page, Integer rows, TblTestSet testSet, Integer tableType) {
		Map<String, Object> map = new HashMap<>();
		PageHelper.startPage(page, rows);
		List<Map<String, Object>> testSetList = testSetMapper.selectTestSet(testSet);
		
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(testSetList);
		if (tableType == 2) {
			map.put("total", pageInfo.getTotal());
			map.put("rows", pageInfo.getList());
		} else if(tableType == 1){
			map.put("rows", pageInfo.getList());
			map.put("total", pageInfo.getPages());
			map.put("records", pageInfo.getTotal());
			map.put("page", page);
		} else {
			map.put("rows", testSetList);
		}
		return map;
	}
	
	/**
	 * 根据条件查询所有的测试集
	 */
	@Override
	public Map<String, Object> getAllTestSet(String nameOrNumber,String createBy,Long testTaskId) {
		Map<String, Object> map = new HashMap<>();
		List<Long> userIds = JSON.parseArray(createBy, Long.class);
		map.put("rows", testSetMapper.selectTestByCon(nameOrNumber,userIds,testTaskId));
		return map;
	}


	/**
	 *  获取导入数据单元格值
     *  2020-06-28
	 * @param testSetId
	 * @param currentUserId
	 * @param i
	 * @param startRow
	 * @param endRow
	 * @param request
	 * @param sheet
	 * @param testSetCase
	 * @param caseInfo
	 * @param caseSteps
	 * @param testSetCaseSteps
	 * @param systemId
	 * @return
	 */
	private Map getCellEstimate(Long testSetId, Long currentUserId, int i, int startRow, int endRow, HttpServletRequest request, Sheet sheet, TblTestSetCase testSetCase, TblCaseInfo caseInfo, List<TblCaseStep> caseSteps, List<TblTestSetCaseStep> testSetCaseSteps, Long systemId){
		Map<String, Object> mapValue = new HashMap<>(16);
		if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 0).getValue())){
			mapValue.put("flag",true);
			mapValue.put("errorMessage","导入模板第"+(i+1)+"行案例序号为空");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return mapValue;
		}
		if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 4).getValue())){
			mapValue.put("flag",true);
			mapValue.put("errorMessage","导入模板第"+(i+1)+"行案例描述为空");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return mapValue;
		}



		/**
		 * 导入的案例排在该测试集下的案例前面 orderCase字段
		 * 根据新增的案例数来决定原有的案例 序号 往后移几个数
		 */
		updateCaseOrder(testSetId,1,currentUserId);

		/**
		 * 测试案例
		 */
		//案例编号
		String caseNumber = getCaseNumber();
		caseInfo.setCaseNumber(caseNumber);
		//案例名称
		caseInfo.setCaseName(PoiExcelUtil.getMergedRegionValue(sheet, i, 4).getValue());
		//系统id
		caseInfo.setSystemId(Integer.valueOf(systemId+""));
//		caseInfo.setSystemId(1);
		//前置条件
		caseInfo.setCasePrecondition(PoiExcelUtil.getMergedRegionValue(sheet, i, 5).getValue());
		//输入数据
		caseInfo.setInputData(PoiExcelUtil.getMergedRegionValue(sheet, i, 6).getValue());
		//测试项
		caseInfo.setTestPoint(PoiExcelUtil.getMergedRegionValue(sheet, i, 3).getValue());
		//模块
		caseInfo.setModuleName(PoiExcelUtil.getMergedRegionValue(sheet, i, 2).getValue());
		//业务类型
		caseInfo.setBusinessType(PoiExcelUtil.getMergedRegionValue(sheet, i, 1).getValue());
		//预期结果
		caseInfo.setExpectResult(PoiExcelUtil.getMergedRegionValue(sheet, i, 10).getValue());
		//归档状态
		caseInfo.setArchiveStatus(1);

		/**
		 * 测试集案例
		 */
		//案例编号
		testSetCase.setCaseNumber(caseNumber);
		//案例名称
		testSetCase.setCaseName(PoiExcelUtil.getMergedRegionValue(sheet, i, 4).getValue());
		//系统id
		testSetCase.setSystemId(systemId);
//		testSetCase.setSystemId(Long.valueOf(1));
		//前置条件
		testSetCase.setCasePrecondition(PoiExcelUtil.getMergedRegionValue(sheet, i, 5).getValue());
		//输入数据
		testSetCase.setInputData(PoiExcelUtil.getMergedRegionValue(sheet, i, 6).getValue());
		//测试项
		testSetCase.setTestPoint(PoiExcelUtil.getMergedRegionValue(sheet, i, 3).getValue());
		//模块
		testSetCase.setModuleName(PoiExcelUtil.getMergedRegionValue(sheet, i, 2).getValue());
		//业务类型
		testSetCase.setBusinessType(PoiExcelUtil.getMergedRegionValue(sheet, i, 1).getValue());
		//预期结果
		testSetCase.setExpectResult(PoiExcelUtil.getMergedRegionValue(sheet, i, 10).getValue());
		//实际结果
		testSetCase.setCaseActualResult(PoiExcelUtil.getMergedRegionValue(sheet, i, 11).getValue());
		//执行结果
		if (StringUtils.isNotBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 12).getValue())){
            Integer valueCode = testSetCaseMapper.getValueCode(PoiExcelUtil.getMergedRegionValue(sheet, i, 12).getValue());
            if (valueCode == null){
                mapValue.put("flag",true);
                mapValue.put("errorMessage","导入模板第"+(i+1)+"行执行结果填写有误");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return mapValue;
            }
            testSetCase.setCaseExecuteResult(valueCode);
		}else{
			testSetCase.setCaseExecuteResult(1);
		}

		//测试集案例id
		testSetCase.setTestSetId(testSetId);
		//排序
		testSetCase.setOrderCase(1);
		CommonUtil.setBaseValue(testSetCase, request);
		testSetCaseMapper.insert(testSetCase);
		Long testSetCaseId = testSetCase.getId();
		caseInfoMapper.insertCaseInfo(caseInfo);
		Long caseId = caseInfo.getId();
		int count =0;
		for (int j = startRow; j <= endRow; j++) {
			//案例步骤内容判断
//			if (PoiExcelUtil.getMergedRegionValue(sheet, startRow, 0).isMerged() == false){
//				if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 7).getValue())){
//					mapValue.put("flag",true);
//					mapValue.put("errorMessage","导入模板第"+(i+1)+"行步骤为空");
//					return mapValue;
//				}
//				if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, startRow, 8).getValue())){
//					mapValue.put("flag",true);
//					mapValue.put("errorMessage","导入模板第"+(i+1)+"行步骤描述为空");
//					return mapValue;
//				}
//				if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, startRow, 9).getValue())){
//					mapValue.put("flag",true);
//					mapValue.put("errorMessage","导入模板第"+(i+1)+"行步骤预期结果为空");
//					return mapValue;
//				}
//			}

			if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 7).getValue()) && StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 8).getValue()) && StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 9).getValue())){
				continue;
			}

			/**
			 * 案例步骤
			 */
			TblCaseStep caseStep = new TblCaseStep();
			TblTestSetCaseStep testSetCaseStep = new TblTestSetCaseStep();
			testSetCaseStep.setTestSetCaseId(testSetCaseId);
			//步骤排序号
			count = count +1;
            caseStep.setStepOrder(count);
//			caseStep.setStepOrder(Integer.valueOf(PoiExcelUtil.getMergedRegionValue(sheet, j, 7).getValue()));
			//步骤描述
			caseStep.setStepDescription(PoiExcelUtil.getMergedRegionValue(sheet, j, 8).getValue());
			//步骤预期结果
			caseStep.setStepExpectedResult(PoiExcelUtil.getMergedRegionValue(sheet, j, 9).getValue());
			//案例表主键
			caseStep.setCaseId(caseId);
			CommonUtil.setBaseValue(caseStep, request);
			/**
			 * 测试集案例步骤
			 */
//			testSetCaseStep.setStepOrder(Integer.valueOf(PoiExcelUtil.getMergedRegionValue(sheet, j, 7).getValue()));
			testSetCaseStep.setStepOrder(count);
			testSetCaseStep.setStepDescription(PoiExcelUtil.getMergedRegionValue(sheet, j, 8).getValue());
			testSetCaseStep.setStepExpectedResult(PoiExcelUtil.getMergedRegionValue(sheet, j, 9).getValue());
			CommonUtil.setBaseValue(testSetCaseStep, request);
			caseSteps.add(caseStep);
			testSetCaseSteps.add(testSetCaseStep);
		}
		mapValue.put("flag",false);
		return mapValue;
	}

	//end

	/**
	 * 导入案例
	 */
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> importTestCase(MultipartFile file, Long testSetId, HttpServletRequest request, Integer type) throws Exception {
		Map<String, Object> map = new HashMap<>(16);
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		try {
			String fileName = file.getOriginalFilename();
			if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
				map.put("status", Constants.ITMP_RETURN_FAILURE);
				map.put("errorMessage", "上传文件格式出错");
				return map;
			}

			boolean isExcel2003 = true;
			if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
				isExcel2003 = false;
			}
			InputStream is = file.getInputStream();
			Workbook wb = null;
			if (isExcel2003) {
				wb = new HSSFWorkbook(is);
			} else {
				wb = new XSSFWorkbook(is);
			}
			Sheet sheet = wb.getSheetAt(0);
			if (sheet == null) {
				map.put("status", Constants.ITMP_RETURN_FAILURE);
				map.put("errorMessage", "标题未对应");
				return map;
			}

			Row titleRow = sheet.getRow(0);
			String[] title = {"案例序号", "业务类型", "模块", "测试项", "案例名称",
					"前置条件", "输入数据", "案例步骤", "预期结果", "实际结果", "执行结果"};
			for (int i = 0; i < title.length-3; i++) {
				if (!StringUtils.contains(titleRow.getCell(i).getStringCellValue(), title[i])) {
					map.put("status", Constants.ITMP_RETURN_FAILURE);
					map.put("errorMessage", "标题未对应");
					return map;
				}
			}

			int lastRowNum = sheet.getLastRowNum();
			if (lastRowNum == 1){
				map.put("status", Constants.ITMP_RETURN_FAILURE);
				map.put("errorMessage", "未填写导入数据!");
				return map;
			}
			List<TblCaseStep> caseSteps = new ArrayList<>();
			List<TblTestSetCaseStep> testSetCaseSteps = new ArrayList<>();

			//获取系统id
			Map<String, Object> relateSystem = testSetMapper.getRelateSystem(testSetId);
			Long systemId = Long.valueOf(relateSystem.get("id").toString());


			if (type != null && type == 1){
				testSetMapper.updateTestSetCaseByTestSetId(testSetId);
			}
			//获取第一行有多少列
			int cellCount = sheet.getRow(0).getLastCellNum() - sheet.getRow(0).getFirstCellNum();
			//循环表格行
 			for (int i = 2; i <= lastRowNum; i++) {
				Row row = sheet.getRow(i);
				//判断当前行数据是否为空 为空则不处理此行数据
				if (this.isExcelCellNull(row,cellCount)) {
					continue;
				}
                TblTestSetCase testSetCase = new TblTestSetCase();
				TblCaseInfo caseInfo = new TblCaseInfo();
				int startRow = i;
				int endRow = i;
				//单元格是否为合并单元格
//				MergedRegionResult result = ExcelUtil.isMergedRegion(sheet, i, 0);
				MergedRegionResult result = PoiExcelUtil.getMergedRegionValue(sheet, i, 0);
				if (result.isMerged()){
					startRow = result.getStartRow();
					endRow = result.getEndRow();
					if (PoiExcelUtil.rowContains(i,endRow)){
					Map cellEstimate = getCellEstimate(testSetId, currentUserId, i, startRow, endRow, request, sheet, testSetCase, caseInfo, caseSteps, testSetCaseSteps,systemId);
						if (Boolean.parseBoolean(cellEstimate.get("flag").toString())){
							cellEstimate.put("status", Constants.ITMP_RETURN_FAILURE);
							return cellEstimate;
						}
					}
				}else{
					startRow = i;
					endRow = i;
					Map cellEstimate = getCellEstimate(testSetId, currentUserId, i, startRow, endRow, request, sheet, testSetCase, caseInfo, caseSteps, testSetCaseSteps, systemId);
					if (Boolean.parseBoolean(cellEstimate.get("flag").toString())){
						cellEstimate.put("status", Constants.ITMP_RETURN_FAILURE);
						return cellEstimate;
					}
				}

			}
			if (!caseSteps.isEmpty() && !testSetCaseSteps.isEmpty()){
				caseStepMapper.batchInsert(caseSteps);
				testSetCaseStepMapper.batchInsert(testSetCaseSteps);
			}
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
			return map;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 导出案例模板
	 */
	@Override
	public void exportTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			new ExportExcel()
					.setWorkHead(TblTestSetCaseVo.class, CaseStepVo.class, "测试集案例模板", 7, 9, "案例步骤")
					.setDataListWithList(new ArrayList<>()).write(response,
					"测试集案例模板.xlsx");
//					"测试集案例模板.xlsx").dispose();
		} catch (Exception e) {
			throw e;
		}
	}



	/**
	 * 新增测试集
	 */
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> insertTestSet(TblTestSet tblTestSet, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		//新增
		if(tblTestSet.getId() == null) {
			tblTestSet.setTestSetNumber(getTestSetCode());
			CommonUtil.setBaseValue(tblTestSet, request);
			testSetMapper.insert(tblTestSet); // 插入数据
			Long id = tblTestSet.getId();
			map.put("id", id);
			
			//新增测试集 同时新建测试集的人就是执行人  添加执行人
			Long uid = CommonUtil.getCurrentUserId(request);
			Long[] arr = new Long[]{uid};
			String uids = JSON.toJSONString(arr);
			addExecuteUser(id, tblTestSet.getExcuteRound(), uids, request);
		}else {
			//修改
			tblTestSet.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			tblTestSet.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
			testSetMapper.updateByPrimaryKeySelective(tblTestSet);
		}
		return map;
	}

	/**
	 * 获取编号
	 * 
	 * @return
	 */
	public String getTestSetCode() {
		String testSetCode = "";
		int codeInt = 0;
		Object object = redisUtils.get("TBL_TEST_SET_TEST_SET_CODE");
		if (object != null && !"".equals(object)) {// redis有直接从redis中取
			String code = object.toString();
			if (!StringUtils.isBlank(code)) {
				codeInt = Integer.parseInt(code) + 1;
			}
		} else {// redis中没有查询数据库中最大的任务编号
			int length = Constants.ITMP_TESTSET_SET_CODE.length() + 1;
			String maxCod = testSetMapper.findMaxId();
			if (!StringUtils.isBlank(maxCod)) {
				String cod = maxCod.substring(length);
				codeInt = Integer.parseInt(cod) + 1;
			} else {
				codeInt = 1;
			}

		}
		DecimalFormat mat = new DecimalFormat("00000000");
		String codeString = mat.format(codeInt);

		testSetCode = Constants.ITMP_TESTSET_SET_CODE + codeString;
		redisUtils.set("TBL_TEST_SET_TEST_SET_CODE", codeString);
		return testSetCode;
	}

	/**
	 * 查询测试集案例(jqGrid)
	 */
	@Override
	public Map<String, Object> selectTestSetCaseByPage(Integer page, Integer rows, TblTestSetCase testSetCase) {
		Map<String, Object> map = new HashMap<>();
		PageHelper.startPage(page, rows);
		List<Map<String, Object>> list = testSetCaseMapper.selectByCon(testSetCase);
		Map<String, Object> caseType = JSON.parseObject(redisUtils.get("TBL_TEST_SET_CASE_TYPE").toString(), Map.class);
		Map<String, Object> caseExecuteResult = JSON
				.parseObject(redisUtils.get("TBL_TEST_SET_CASE_CASE_EXECUTE_RESULT").toString(), Map.class);
		for (Map<String, Object> map2 : list) {
			if(map2.get("caseType")!=null) {
				map2.put("caseTypeName", caseType.get(map2.get("caseType").toString()));
			}
			if(map2.get("caseExecuteResult")!=null) {
				map2.put("caseExecuteResultName", caseType.get(map2.get("caseExecuteResult").toString()));
			}
			
		}
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		map.put("rows", pageInfo.getList());
		map.put("total", pageInfo.getPages());
		map.put("page", page);
		map.put("records", pageInfo.getTotal());
		return map;
	}
	/**
	 * 查询测试集案例bootstrap
	 */
	@Override
	public Map<String, Object> getTestSetCaseBootStrap(Integer pageNumber, Integer pageSize,
			TblTestSetCase testSetCase) {
		Map<String, Object> map = new HashMap<>();
		PageHelper.startPage(pageNumber, pageSize);
		List<Map<String, Object>> list = testSetCaseMapper.selectByCon(testSetCase);
		Map<String, Object> caseType = JSON.parseObject(redisUtils.get("TBL_TEST_SET_CASE_TYPE").toString(), Map.class);
		Map<String, Object> caseExecuteResult = JSON
				.parseObject(redisUtils.get("TBL_TEST_SET_CASE_CASE_EXECUTE_RESULT").toString(), Map.class);
		for (Map<String, Object> map2 : list) {
			if(map2.get("caseType")!=null) {
				map2.put("caseTypeName", caseType.get(map2.get("caseType").toString()));
			}
			if(map2.get("caseExecuteResult")!=null) {
				map2.put("caseExecuteResultName", caseType.get(map2.get("caseExecuteResult").toString()));
			}
		}
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		map.put("total", pageInfo.getTotal());
		map.put("rows", list);
		return map;
	}

	

	/**
	 * 修改测试集案例
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateCase(TblTestSetCase testSetCase, HttpServletRequest request) {
		testSetCase.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		testSetCase.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		testSetCaseMapper.updateCase(testSetCase);
	}

	/**
	 * 批量移除测试集案例
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateManyCase(Long testSetId,String ids, HttpServletRequest request) throws Exception{
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		if (ids != null && !ids.isEmpty()) {

			List<TblTestSetCase> list = JSONArray.parseArray(ids.replaceAll("&quot;","\""),TblTestSetCase.class);
			Integer max = list.stream().mapToInt(TblTestSetCase::getOrderCase).max().getAsInt();
			Integer min = list.stream().mapToInt(TblTestSetCase::getOrderCase).min().getAsInt();
			List<Long> idList = CollectionUtil.extractToList(list,"id");

			TblTestSetCase tblTestSetCase = new TblTestSetCase();
			tblTestSetCase.setStatus(2);
			tblTestSetCase.setLastUpdateBy(currentUserId);
			tblTestSetCase.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
			Map<String, Object> map = new HashMap<>();
			map.put("list", idList);
			map.put("testSetCase", tblTestSetCase);
			testSetCaseMapper.updateManyStatus(map);
			//同时删除案例步骤
			testSetCaseStepMapper.updateStatusByCaseIds(idList);

			// 移除之后更新排序号
			Map<String,Object> param = new HashMap<>();
			param.put("list",list);
			param.put("minIndex",min);
			param.put("maxIndex",max);
			param.put("testSetId",testSetId);
			List<TblTestSetCase> otherList = testSetCaseMapper.findNoChooseCaseId(param);
			if(otherList != null){
				for (int i = 0; i < otherList.size(); i++) {
					testSetCaseMapper.updateOrderByTestSetCaseId(otherList.get(i).getId(),currentUserId,min+i);
				}
			}

			// 整体向下移动
			param.put("currentUserId",currentUserId);
			param.put("orderNum",list.size());
			param.put("symbol","-");
			param.put("minIndex",max+1);
			param.put("maxIndex",testSetCaseMapper.selectMaxOrder(testSetId));
			testSetCaseMapper.updateAddOrderByTestSetId(param);
		}
	}

	/**
	 * 批量添加案例
	 */
	@Override
	@Transactional(readOnly = false)
	public void batchInsertCase(Long testSetId, String caseStr, HttpServletRequest request) {
		if (!caseStr.isEmpty()) {
			List<TblTestSetCase> testSetCaseListBefore = JSON.parseArray(caseStr, TblTestSetCase.class);
			List<TblTestSetCase> testsetcaseListAdd = new ArrayList<>();
			//List<Long> caseIdList = new ArrayList<>();
			Long currentUserId = CommonUtil.getCurrentUserId(request);
			
			// 整体向下移动
			updateCaseOrder(testSetId, testSetCaseListBefore.size(),currentUserId);
			
			/*TblTestSetCase tblTestSetCaseParam = new TblTestSetCase();
			tblTestSetCaseParam.setStatus(1);
			tblTestSetCaseParam.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			tblTestSetCaseParam.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));*/
			int i = 0;
			for (TblTestSetCase tblTestSetCase : testSetCaseListBefore) {
				i++;
				//Long caseId = testSetCaseMapper.judgeTestSetCase(testSetId, tblTestSetCase.getCaseNumber());
				tblTestSetCase.setTestSetId(testSetId);
				tblTestSetCase.setCaseExecuteResult(1);
				tblTestSetCase.setOrderCase(i);
				//if(caseId == null) {    //在测试集案例表中没有则增加
					CommonUtil.setBaseValue(tblTestSetCase, request);
					testsetcaseListAdd.add(tblTestSetCase);
				/*}else {    //在测试集案例表中有则修改状态
					caseIdList.add(caseId);
				}*/
			}
			if(CollectionUtil.isNotEmpty(testsetcaseListAdd)) {
				testSetCaseMapper.batchInsert(testsetcaseListAdd);
			}
			/*if(CollectionUtil.isNotEmpty(caseIdList)) {
				Map<String, Object> map = new HashMap<>();
				map.put("list", caseIdList);
				map.put("testSetCase", tblTestSetCaseParam);
				testSetCaseMapper.updateManyStatus(map);
			}*/
		}
		

	}
	
	/**
	 * 批量添加案例步骤
	 */
	@Override
	@Transactional(readOnly = false)
	public void batchInsertCaseStep(Long testSetId,String idStr, HttpServletRequest request) {
		if(!idStr.isEmpty()) {
			List<String> caseNumberlist = JSON.parseArray(idStr, String.class);
			List<TblTestSetCaseStep> caseStepList = testSetCaseStepMapper.getCaseStepByCaseNumber(caseNumberlist,testSetId);
			for (TblTestSetCaseStep tblTestSetCaseStep : caseStepList) {
				CommonUtil.setBaseValue(tblTestSetCaseStep, request);
			}
			if(!caseStepList.isEmpty()) {
				testSetCaseStepMapper.batchInsert(caseStepList);
			}
		}
	}

	/**
	 * 修改测试集状态
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateTestSetStatus(Long id, Integer status, HttpServletRequest request) {
		TblTestSet testSet = new TblTestSet();
		testSet.setId(id);
		testSet.setStatus(status);
		testSet.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		testSet.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		testSetMapper.updateByPrimaryKeySelective(testSet);
	}

	/**
	 * 增加测试集案例
	 */
	@Override
	@Transactional(readOnly = false)
	public void addTestSetCase(TblTestSetCase testSetCase, List<TblTestSetCaseStep> testSetCaseStepList, HttpServletRequest request) {
		testSetCase.setCaseExecuteResult(1);
		CommonUtil.setBaseValue(testSetCase, request);
		testSetCase.setOrderCase(testSetCaseMapper.selectMaxOrder(testSetCase.getTestSetId()));
		testSetCaseMapper.insert(testSetCase);
		testSetCaseStepMapper.deleteByCaseId(testSetCase.getId());
		for (TblTestSetCaseStep tblTestSetCaseStep : testSetCaseStepList) {
			tblTestSetCaseStep.setTestSetCaseId(testSetCase.getId());
			CommonUtil.setBaseValue(tblTestSetCaseStep, request);
			testSetCaseStepMapper.insert(tblTestSetCaseStep);
		}
	}

	/**
	 * 新增执行人
	 */
	@Override
	@Transactional(readOnly = false)
	public void addExecuteUser(Long testSetId,Integer executeRound,String userIdStr, HttpServletRequest request) {
		if(!userIdStr.isEmpty()) {
			List<TblTestSetExcuteRoundUser> testSetExcuteRoundUserList = new ArrayList<>();
			List<Long> list = JSON.parseArray(userIdStr, Long.class);
			for (Long id : list) {
				TblTestSetExcuteRoundUser testSetExcuteRoundUser = new TblTestSetExcuteRoundUser();
				testSetExcuteRoundUser.setTestSetId(testSetId);
				testSetExcuteRoundUser.setExcuteRound(executeRound);
				testSetExcuteRoundUser.setExcuteUserId(id);
				CommonUtil.setBaseValue(testSetExcuteRoundUser, request);
				testSetExcuteRoundUserList.add(testSetExcuteRoundUser);
			}
			testSetExcuteRoundUserMapper.batchInsert(testSetExcuteRoundUserList);
		}
	}

	/**
	 * 移除执行人
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateExcuteUserStatus(Long id, HttpServletRequest request) {
		TblTestSetExcuteRoundUser testSetExcuteRoundUser = new TblTestSetExcuteRoundUser();
		testSetExcuteRoundUser.setId(id);
		testSetExcuteRoundUser.setStatus(2);
		testSetExcuteRoundUser.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		testSetExcuteRoundUser.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		testSetExcuteRoundUserMapper.updateByPrimaryKeySelective(testSetExcuteRoundUser);
	}

	/**
	 * 批量移除执行人
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateBatchStatus(String ids, HttpServletRequest request) {
		List<Long> idList = JSON.parseArray(ids, Long.class);
		testSetExcuteRoundUserMapper.updateBatchStatus(idList);
	}

	/**
	 * 根据轮次获取执行人
	 */
	@Override
	public Map<String, Object> getTestExcuteUser(Long testSetId, Integer excuteRound) {
		Map<String, Object> map = new HashMap<>();
		List<List<Map<String, Object>>> list = new ArrayList<>();
		Map<String, Object> param = new HashMap<>();
		TblTestSet testSet = testSetMapper.selectById(testSetId);
		for (int i = 1; i <= excuteRound; i++) {
			TblTestSetExcuteRoundUser testSetExcuteRoundUser = new TblTestSetExcuteRoundUser();
			testSetExcuteRoundUser.setTestSetId(testSetId);
			testSetExcuteRoundUser.setExcuteRound(i);
			list.add(testSetExcuteRoundUserMapper.selectByCon(testSetExcuteRoundUser));
		}
		map.put("excuteRoundList", list);
		map.put("testSet", testSet);
		return map;
	}

	/**
	 * 根据测试集id查询测试集案例
	 */
	@Override
	public Map<String, Object> getCaseByTestSetId(Integer page,Integer pageSize,Long testSetId,Integer executeRound,String executeResult) {
		Long executeResultCode = null;
		String string = redisUtils.get("TBL_TEST_SET_CASE_CASE_EXECUTE_RESULT").toString();
		JSONObject jsonObject = JSON.parseObject(string);
		for (String key : jsonObject.keySet()) {
			if(jsonObject.get(key).equals(executeResult)) {
				executeResultCode = Long.valueOf(key);
			}
		}
		Map<String, Object> map = new HashMap<>();
		PageHelper.startPage(page, pageSize);
		List<Map<String, Object>> list = testSetCaseMapper.selectCaseTree(testSetId,executeRound,executeResultCode);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		map.put("total", pageInfo.getTotal());
		map.put("rows", list);
		return map;
	}

	/**
	 * 根据测试任务id查询测试集
	 */
	@Override
	public List<TblTestSet> getTestSetByFeatureId(Long featureId) {
		return testSetMapper.getTestSetByFeatureId(featureId);
	}

	/**
	 * 获取用户列表(除去执行人)
	 */
	@Override
	public Map<String, Object> getUserTable(Integer page,Integer rows,Long testSetId, Integer executeRound,String userName,String companyName,String deptName) {
		Map<String, Object> map = new HashMap<>();
		PageHelper.startPage(page, rows);
		List<Map<String, Object>> testSetList = userInfoMapper.getUserTable(testSetId, executeRound,userName,companyName,deptName);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(testSetList);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
	}

	/**
	 * 获取测试执行树(测试任务+测试集)
	 */
	@Override
	public List<Map<String, Object>> getTaskTree(Long userId,String taskName,String testSetName,Integer requirementFeatureStatus) {
		List<Map<String,Object>> taskTree = new ArrayList<>();
		List<Map<String,Object>> testTree = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("taskName", taskName);
		paramMap.put("testSetName", testSetName);
		paramMap.put("requirementFeatureStatus", requirementFeatureStatus);
		paramMap.put("userId", userId);
		taskTree = testSetMapper.getTaskTree(paramMap);
		testTree = testSetMapper.getTestTree(paramMap);
		for (Map<String, Object> map : testTree) {
			String[] strings = map.get("executeRound").toString().split(",");
			List<String> list = Arrays.asList(strings);
			Set<String> set = new HashSet<>();
			set.addAll(list);
			String str = StringUtils.join(set.toArray(), ",");
			map.put("executeRound", str);
		}
		for (Map<String, Object> map : taskTree) {
			List<Map<String, Object>> testSetList = new ArrayList<>();
			for (Map<String, Object> map2 : testTree) {
				if(map.get("featureId").equals(map2.get("featureId"))) {
					if(map2.get("executeRound") != null && map2.get("executeRound").toString().split(",").length > 1) {
						String[] str = map2.get("executeRound").toString().split(",");
						Integer[] rounds = (Integer[])ConvertUtils.convert(str, Integer.class);
						Arrays.sort(rounds);
						String roundStr = Arrays.toString(rounds);
						map2.put("executeRound", roundStr.substring(1, roundStr.length()-1));
					}
					testSetList.add(map2);
					map.put("testSetList", testSetList);
				}
			}
		}
		return taskTree;
	}
	
	//案例编号
    private String getCaseNumber() {
        String featureCode = "";
        Integer codeInt =0;
        Object object = redisUtils.get(Constants.TMP_CASE_INFO_NUMBER);
        if (object != null &&!"".equals( object)) {
            // redis有直接从redis中取
            String code = object.toString();
            if (!StringUtils.isBlank(code)) {
                codeInt =Integer.parseInt(code)+1;
            }
        }else {
            // redis中没有查询数据库中最大的任务编号
            String maxCaseNo = caseInfoMapper.selectMaxCaseNumber();
            codeInt = 1;
            if (StringUtils.isNotBlank(maxCaseNo)) {
                codeInt = Integer.valueOf(maxCaseNo.substring(Constants.TMP_CASE_INFO_NUMBER.length())) + 1;
            }

        }
        featureCode = Constants.TMP_CASE_INFO_NUMBER + String.format("%08d", codeInt);
        redisUtils.set(Constants.TMP_CASE_INFO_NUMBER,String.format("%08d", codeInt) );
        return featureCode;
    }

    /**
     * 获取关联系统
     */
	@Override
	public Map<String, Object> getRelateSystem(Long testSetId) {
		return testSetMapper.getRelateSystem(testSetId);
	}

	@Override
	public List<TblTestSetCaseStep> getTestSetCase(Long caseId) {
		return testSetCaseStepMapper.getAllCaseStepByCaseId(caseId);
	}

	@Override
	public Map<String, Object> findOneTestSet(Long testSetId) {
		return testSetMapper.selectOneById(testSetId);
	}
	
	/**
	 * 引入测试集案例
	 * @author tingting
	 * @param flag 1 案例编号一致  、2 重新生成案例编号
	 * */
	@Override
	@Transactional(readOnly=false)
	public void leadInTestSetCase(Long testSetId, String caseStr, String idStr, Integer flag,
			HttpServletRequest request) {
		//测试集案例
		if (!caseStr.isEmpty()) {
			List<TblTestSetCase> testSetCaseListBefore = JSON.parseArray(caseStr, TblTestSetCase.class);
			List<TblTestSetCase> testsetcaseListAdd = new ArrayList<>();
			//List<Long> caseIdList = new ArrayList<>();
			List<String> caseNumberlist = new ArrayList<>();
			Long currentUserId = CommonUtil.getCurrentUserId(request);
			// 整体向下移动
			updateCaseOrder(testSetId, testSetCaseListBefore.size(),currentUserId);
			
			int i = 0;
			for (TblTestSetCase tblTestSetCase : testSetCaseListBefore) {
				i++;
				//Long caseId = testSetCaseMapper.judgeTestSetCase(testSetId, tblTestSetCase.getCaseNumber());
				tblTestSetCase.setTestSetId(testSetId);
				tblTestSetCase.setCaseExecuteResult(1);
				tblTestSetCase.setOrderCase(i);
				if(flag!=null && flag == 2) {//如果是 重新生成案例编号 就要全部新增
					tblTestSetCase.setCaseNumber(getCaseNumber() );
					CommonUtil.setBaseValue(tblTestSetCase, request);
					testsetcaseListAdd.add(tblTestSetCase);
					caseNumberlist.add(getCaseNumber() );//用新的案例编号
				}else {
					//if(caseId == null) {    //在测试集案例表中没有则增加
						CommonUtil.setBaseValue(tblTestSetCase, request);
						testsetcaseListAdd.add(tblTestSetCase);
					/*}else {    //在测试集案例表中有则修改状态   改成全部增加 因为orderCase排序
						caseIdList.add(caseId);
					}*/
				}
			}
			if(CollectionUtil.isNotEmpty(testsetcaseListAdd)) {
				testSetCaseMapper.batchInsert(testsetcaseListAdd);
			}
			
			//改成全部增加 因为orderCase排序
			/*TblTestSetCase tblTestSetCaseParam = new TblTestSetCase();
			tblTestSetCaseParam.setStatus(1);
			tblTestSetCaseParam.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
			tblTestSetCaseParam.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));*/
			
			/*if(CollectionUtil.isNotEmpty(caseIdList)) {
				Map<String, Object> map = new HashMap<>();
				map.put("list", caseIdList);
				map.put("testSetCase", tblTestSetCaseParam);
				testSetCaseMapper.updateManyStatus(map);
			}
			*/
			//测试集案例步骤
			if(!idStr.isEmpty() && flag!=null && flag == 1) {//如果是案例编号一致 用原来的案例编号
				caseNumberlist = JSON.parseArray(idStr, String.class);
			}
			
			List<TblTestSetCaseStep> caseStepList = testSetCaseStepMapper.getCaseStepByCaseNumber(caseNumberlist,testSetId);
			for (TblTestSetCaseStep tblTestSetCaseStep : caseStepList) {
				CommonUtil.setBaseValue(tblTestSetCaseStep, request);
			}
			if(!caseStepList.isEmpty()) {
				testSetCaseStepMapper.batchInsert(caseStepList);
			}
		}	
	}
	/**
	 * 引入测试集案例---查询测试集案例
	 * @author tingting
	 * @param otherTestSetId 当前测试集的id 
	 * @param testSetId 选中的测试集id
	 * */
	@Override
	public HashMap<String, Object> getOtherTestSetCase(Long otherTestSetId, Long testSetId, Long testTaskId,
			Long workTaskId,Integer page, Integer rows) {
		HashMap<String, Object> map = new HashMap<>();
		PageHelper.startPage(page, rows);
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("otherTestSetId", otherTestSetId);
		paramMap.put("testSetId", testSetId);
		paramMap.put("testTaskId", testTaskId);
		paramMap.put("workTaskId", workTaskId);
		List<TblTestSetCase> list = testSetCaseMapper.getOtherTestSetCase(paramMap);
		PageInfo<TblTestSetCase> pageInfo = new PageInfo<>(list);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
	}

	@Override
	@Transactional(readOnly=false)
	public Map<String,Object> leadInAllTestSetCase(Long otherTestSetId, Long testSetId, Long testTaskId, Long workTaskId,
			Integer flag, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		//查询出当前条件下所有的案例 
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("otherTestSetId", otherTestSetId);
		paramMap.put("testSetId", testSetId);
		paramMap.put("testTaskId", testTaskId);
		paramMap.put("workTaskId", workTaskId);
		List<TblTestSetCase> list = testSetCaseMapper.getOtherTestSetCase(paramMap);

		Long currentUserId = CommonUtil.getCurrentUserId(request);
		// 整体向下移动
		updateCaseOrder(testSetId, list.size(),currentUserId);
		
		//引入
		if(!list.isEmpty()&& list.size()>0) {
			String[] arr = new String[list.size()];
			for (int i=0; i<list.size(); i++) {
				arr[i] = list.get(i).getCaseNumber();
				list.get(i).setOrderCase(i+1);
			}
			leadInTestSetCase(otherTestSetId,JSON.toJSONString(list),JSON.toJSONString(arr),flag,request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		}else {
			map.put("status", Constants.ITMP_RETURN_FAILURE);
			map.put("message","没有可引入的案例！");
			return map;
		}
		
		return map;
		
	}
	
	/**
	 * 移动该测试集下原有案例的顺序
	 * @param testSetId 测试集id
	 * @param orderNum 新增的案例数 也就是原有测试案例要移动的数
	 * */ 
	public void updateCaseOrder(Long testSetId,Integer orderNum, Long userId) {
		// 整体向下移动
		Map<String,Object> param = new HashMap<>();
		param.put("testSetId",testSetId);
		param.put("currentUserId",userId);
		param.put("orderNum",orderNum);
		param.put("symbol","+");
		param.put("minIndex",1);
		param.put("maxIndex",testSetCaseMapper.selectMaxOrder(testSetId));
		testSetCaseMapper.updateAddOrderByTestSetId(param);
	}

	/**
	 *@author liushan
	 *@Description 根据测试案例获取步骤
	 *@Date 2020/5/18
	 *@Param [testCaseId, request]
	 *@return java.util.List<cn.pioneeruniverse.dev.entity.TblCaseStep>
	 **/
	@Override
	@Transactional(readOnly=false,rollbackFor = Exception.class)
	public List<CaseStepVo> getCaseStepByCaseId(Long testCaseId, HttpServletRequest request) throws Exception {
		return testSetCaseStepMapper.findByPrimaryKey(testCaseId);
	}

	private boolean isExcelCellNull(Row row, int cellCount) {
		if (row == null) {
			return true;
		}
		int count = 0;
		for (int c = 0; c < cellCount; c++) {
			Cell cell = row.getCell(c);
			if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK || StringUtils.isEmpty((cell + "").trim())) {
				count += 1;
			}
		}
		return count == cellCount;
	}
	
}
