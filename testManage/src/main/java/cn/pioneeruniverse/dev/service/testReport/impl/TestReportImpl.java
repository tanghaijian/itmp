package cn.pioneeruniverse.dev.service.testReport.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.mysql.fabric.xmlrpc.base.Array;
import com.netflix.ribbon.proxy.annotation.Var;
import com.thoughtworks.xstream.mapper.Mapper.Null;

import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.utils.ExcelUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.TblDefectInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblReportMonthlySummaryMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblReportMonthlySystemMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.entity.TblReportMonthlySummary;
import cn.pioneeruniverse.dev.entity.TblReportMonthlySystem;
import cn.pioneeruniverse.dev.entity.TblRequirementFeature;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.service.testReport.ITestReport;
import cn.pioneeruniverse.dev.vo.TestReportParamVo;

@Transactional(readOnly = true)
@Service
public class TestReportImpl implements ITestReport {

	@Autowired
	private TblRequirementFeatureMapper requirementFeatureMapper;
	@Autowired
	private TblDefectInfoMapper defectInfoMapper;
	@Autowired
	private TblReportMonthlySummaryMapper tblReportMonthlySummaryMapper;
	@Autowired
	private TblReportMonthlySystemMapper tblReportMonthlySystemMapper;
	@Autowired
	private TblSystemInfoMapper tblSystemInfoMapper;
	@Autowired
	private RedisUtils redisUtils;
	private String oamSystem; // 运维类系统
	private String newSystem; // 新建类系统
	
	/**
	 * 查询数据
	 */
	@Override
	public Map<String, Object> queryReport(String time) {
		Map<String, Object> map = new HashMap<>();
		newSystem = tblSystemInfoMapper.selectSystemByClass(1);
		oamSystem = tblSystemInfoMapper.selectSystemByClass(2);
		String startDate = "";
		String endDate = "";
		if(time.equals("2020-04")) {
			startDate = time + "-01";
			endDate = time + "-25";
		}else {
			Integer month = Integer.valueOf(time.split("-")[1]);
			startDate = time.split("-")[0] +"-"+ (month-1>0?month-1:12) + "-26";
			endDate = time + "-25";
		}
		List<TblReportMonthlySummary> devVersionReportList = requirementFeatureMapper.selectDevVertionReport(startDate,
				endDate,oamSystem+","+newSystem);
		for (TblReportMonthlySummary tblReportMonthlySummary : devVersionReportList) {
			tblReportMonthlySummary.setYearMonth(time);
		}
		List<TblReportMonthlySystem> defectSystemList = defectInfoMapper.selectDefectProBySystem(startDate, endDate,-1,oamSystem+","+newSystem);
		map.put("devVersionReportList", devVersionReportList);
		map.put("defectSystemList", defectSystemList);
		return map;
	}
	
	/**
	 * 根据时间查询历史数据
	 */
	@Override
	public Map<String, Object> queryHisByTime(String time) {
		Map<String, Object> map = new HashMap<>();
		List<TblReportMonthlySummary> devVersionReportList = tblReportMonthlySummaryMapper.selectReportByTime(time);
		List<TblReportMonthlySystem> reportSystemList = tblReportMonthlySystemMapper.selectReportSystem(time);
		map.put("devVersionReportList", devVersionReportList);
		map.put("defectSystemList", reportSystemList);
		return map;
	}
	
	/**
	 * 查询是否有历史记录
	 */
	@Override
	public Map<String, Object> queryHis(String time) {
		Map<String, Object> map = new HashMap<>();
		int count1 = tblReportMonthlySummaryMapper.jugeHis(time);
		int count2 = tblReportMonthlySystemMapper.jugeHis(time);
		if(count1 == 0 && count2 == 0) {
			map.put("flag", true);
		}else {
			map.put("flag", false);
		}
		return map;
	}
	
	/**
	 * 保存历史
	 */
	@Override
	@Transactional(readOnly = false)
	public void addHis(String allDevVersionTableData, String allSystemTableData, HttpServletRequest request) {
		// TODO Auto-generated method stub
		List<TblReportMonthlySummary> summaries = JSONArray.parseArray(allDevVersionTableData,TblReportMonthlySummary.class);
		List<TblReportMonthlySystem> systems = JSONArray.parseArray(allSystemTableData,TblReportMonthlySystem.class);
		
		if(CollectionUtil.isNotEmpty(summaries)) {
			for (TblReportMonthlySummary tblReportMonthlySummary : summaries) {
				CommonUtil.setBaseValue(tblReportMonthlySummary, request);
			}
			String yearMonth = summaries.get(0).getYearMonth();
			tblReportMonthlySummaryMapper.deleteByYearMonth(yearMonth);
			tblReportMonthlySummaryMapper.batchInsert(summaries);
		}
		if(CollectionUtil.isNotEmpty(systems)) {
			for (TblReportMonthlySystem tblReportMonthlySystem : systems) {
				CommonUtil.setBaseValue(tblReportMonthlySystem, request);
			}
			String yearMonth = systems.get(0).getYearMonth();
			tblReportMonthlySystemMapper.deleteByYearMonth(yearMonth);
			tblReportMonthlySystemMapper.batchInsert(systems);
		}
	}
	
	public void export1(TestReportParamVo testReportParamVo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Workbook workbook = new XSSFWorkbook();
		String fileName = "软件质量分析月报（指标定义说明）.xlsx";
		String startYearDate = testReportParamVo.getTime().split("-")[0] + "-01";
		String time = startYearDate + "~" + testReportParamVo.getTime();
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		Object devVertionObject = redisUtils.get("tmp." + currentUserId + ".summaries");
		Object defectOamObject = redisUtils.get("tmp." + currentUserId + ".defectOam");
		Object defectNewObject = redisUtils.get("tmp." + currentUserId + ".defectNew");
		Object defectSystemMonthObject = redisUtils.get("tmp." + currentUserId + ".defectSystemMonth");
		Object defectSystemYearObject = redisUtils.get("tmp." + currentUserId + ".defectSystemYear");
		Object defectProjectMonthObject = redisUtils.get("tmp." + currentUserId + ".defectProjectMonth");
		Object defectProjectYearObject = redisUtils.get("tmp." + currentUserId + ".defectProjectYear");
		Object defectLevelObject = redisUtils.get("tmp." + currentUserId + ".defectLevel");
		Object worseProjectObject = redisUtils.get("tmp." + currentUserId + ".worseProject");
		Object remainDefectObject = redisUtils.get("tmp." + currentUserId + ".remainDefect");
		List<Map> devVertionList = new ArrayList<>();
		List<Map> defectOamList = new ArrayList<>();
		List<Map> defectNewList = new ArrayList<>();
		List<Map> defectProList = new ArrayList<>();
		List<Map> defectProYearList = new ArrayList<>();
		List<Map> defectProjectMonthList = new ArrayList<>();
		List<Map> defectProjectYearList = new ArrayList<>();
		List<Map> defectLevelList = new ArrayList<>();
		List<Map> worseProjectList = new ArrayList<>();
		List<Map> remainDefectList = new ArrayList<>();
		if (defectOamObject != null) {
			defectOamList = JSONArray.parseArray(defectOamObject.toString(), Map.class);
		}
		if (defectNewObject != null) {
			defectNewList = JSONArray.parseArray(defectNewObject.toString(), Map.class);
		}
		if (defectSystemMonthObject != null) {
			defectProList = JSONArray.parseArray(defectSystemMonthObject.toString(), Map.class);
		}
		if (defectSystemYearObject != null) {
			defectProYearList = JSONArray.parseArray(defectSystemYearObject.toString(), Map.class);
		}
		if (devVertionObject != null) {
			devVertionList = JSONArray.parseArray(devVertionObject.toString(), Map.class);
		}
		if (defectProjectMonthObject != null) {
			defectProjectMonthList = JSONArray.parseArray(defectProjectMonthObject.toString(), Map.class);
		}
		if (defectProjectYearObject != null) {
			defectProjectYearList = JSONArray.parseArray(defectProjectYearObject.toString(), Map.class);
		}
		if (defectLevelObject != null) {
			defectLevelList = JSONArray.parseArray(defectLevelObject.toString(), Map.class);
		}
		if (worseProjectObject != null) {
			worseProjectList = JSONArray.parseArray(worseProjectObject.toString(), Map.class);
		}
		if(remainDefectObject != null) {
			remainDefectList = JSONArray.parseArray(remainDefectObject.toString(),Map.class);
		}
		String monthTime = testReportParamVo.getTime();
		String curYear = monthTime.split("-")[0];
		String curMonth = monthTime.split("-")[1];
		List<String> yearMonthList = new ArrayList<>();
		for (int i = 12; i >= 1; i--) {
			yearMonthList.add(curYear + "-" + (i < 10 ? "0" : "") + i);
		}
		/*
		 * if (Integer.valueOf(curMonth) >= 4) { //当前月份是4月份后，则直接从1月开始 for (int i =
		 * Integer.valueOf(curMonth); i >= 1; i--) { yearMonthList.add(curYear + "年" +
		 * (i<10?"0":"") + i + "月"); } } else { //当前月份是4月以前，则往前退3个月 Calendar cal =
		 * Calendar.getInstance(); cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		 * for (int i = 1; i <= 3; i++) { cal.set(Calendar.MONTH,
		 * cal.get(Calendar.MONTH) - 1);
		 * yearMonthList.add(cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH)<10?"0":
		 * "")+cal.get(Calendar.MONTH)+"月"); }
		 * 
		 * }
		 */
		catalogue(workbook);
		summary(workbook, time);
		devVersion(workbook, devVertionList, time);
		defectPro(workbook, defectOamList,defectNewList, time, testReportParamVo.getDefectProImg());
		defectProBySystem(workbook, defectProList, monthTime, testReportParamVo.getDefectProMonthImg());
		defectProBySystemAllDate(workbook, defectProYearList, time, testReportParamVo.getDefectProYearImg());
		defectProByProject(workbook, defectProjectMonthList, monthTime, 1,
				testReportParamVo.getDefectProProjectMonthImg());
		defectProByProject(workbook, defectProjectYearList, time, 2, testReportParamVo.getDefectProProjectYearImg());
		defectLevel(workbook, defectLevelList, monthTime, testReportParamVo.getDefectLevelImg());
		defectProByWorseProject(workbook, worseProjectList, yearMonthList, curMonth);
		remainDefect(workbook,remainDefectList);
		ExcelUtil.download(fileName, workbook, request, response);
	}

	/**
	 * 获取图数据
	 * 
	 * @param startDate
	 * @param endDate
	 * @param systemIdStr
	 * @return
	 */
	public Map<String, Object> getChartData(String time, String systemIdStr,
			HttpServletRequest request) {
		newSystem = tblSystemInfoMapper.selectSystemByClass(1);
		oamSystem = tblSystemInfoMapper.selectSystemByClass(2);
		Integer year = Integer.valueOf(time.split("-")[0]);
		Integer month = Integer.valueOf(time.split("-")[1]);
		String monthStartDate = time + "-01";
		String endDate = DateUtil.getEndDayOfMonth(time);
		if(time.equals("2020-04")) {
			monthStartDate = time + "-01";
			endDate = time + "-25";
		}else {
			monthStartDate = time.split("-")[0] +"-"+ (month-1>0?month-1:12) + "-26";
			endDate = time + "-25";
		}
		Long currentUserId = CommonUtil.getCurrentUserId(request);
		List<String> list = null;
		if (StringUtils.isNotEmpty(systemIdStr) && !systemIdStr.equals("null")) {
			list = JSONArray.parseArray(systemIdStr, String.class);
		}
		List<Map<String, Object>> summaries = tblReportMonthlySummaryMapper.selectYearReport(year, month);
		List<Map<String, Object>> defectOamList = tblReportMonthlySystemMapper.selectDefectPro(year,month,oamSystem);
		List<Map<String, Object>> defectNewList = tblReportMonthlySystemMapper.selectDefectPro(year,month,newSystem);
		List<Map<String, Object>> defectSystemMonthList = tblReportMonthlySystemMapper.selectReportBySystem(year,month);
		List<Map<String, Object>> defectSystemYearList = tblReportMonthlySystemMapper.selectReportBySystemYear(year,month);
		List<Map<String, Object>> defectProjectMonthList = defectInfoMapper.selectDefectProByProject(monthStartDate,
				endDate, list, -1);
		List<Map<String, Object>> defectProjectYearList = defectInfoMapper.selectDefectProByProject("",
				endDate, list, -1);
		List<Map<String, Object>> defectLevelList = defectInfoMapper.selectDefectLevel(monthStartDate, endDate,oamSystem+","+newSystem);
		List<Map<String, Object>> worseProjectList = tblReportMonthlySystemMapper.selectWorseSystem(year,
				oamSystem);
		List<Map<String, Object>> remainDefectList = defectInfoMapper.selectRemainDefect("2010-01-01",endDate,oamSystem);
		Map<String, Object> severityLevelMap = JSON
				.parseObject(redisUtils.get("TBL_DEFECT_INFO_SEVERITY_LEVEL").toString(), Map.class);
		Map<String,Object> defectStatusMap = JSON
		.parseObject(redisUtils.get("TBL_DEFECT_INFO_DEFECT_STATUS").toString(), Map.class);
		Map<String,Object> defectSourceMap = JSON
				.parseObject(redisUtils.get("TBL_DEFECT_INFO_DEFECT_SOURCE").toString(), Map.class);
		/*获取缺陷等级图数据*/
		Map<String, Object> defectProLevelMap = getDefectLevelMap(defectLevelList,severityLevelMap);
		
		for(Map<String, Object> map:remainDefectList) {
			String severityLevel = "";
			String defectStatus = "";
			String defectSource = "";
			if(map.get("severityLevel") != null) {
				severityLevel = map.get("severityLevel").toString();
			}
			if(map.get("defectStatus") != null) {
				defectStatus = map.get("defectStatus").toString();
			}
			if(map.get("defectSource") != null) {
				defectSource = map.get("defectSource").toString();
			}
			map.put("severityLevelName", severityLevelMap.get(severityLevel) == null?"":(severityLevel + "-" + severityLevelMap.get(severityLevel).toString()));
			map.put("defectStatusName", defectStatusMap.get(defectStatus) == null?"":(defectStatus + "-" + defectStatusMap.get(defectStatus).toString()));
			map.put("defectSourceName", defectSourceMap.get(defectSource) == null?"":(defectSource + "-" + defectSourceMap.get(defectSource).toString()));
		}
		redisUtils.removeList("tmp.*");
		redisUtils.set("tmp." + currentUserId + ".summaries", JSON.toJSONString(summaries), 300l);
		redisUtils.set("tmp." + currentUserId + ".defectOam", JSON.toJSONString(defectOamList), 300l);
		redisUtils.set("tmp." + currentUserId + ".defectNew", JSON.toJSONString(defectNewList), 300l);
		redisUtils.set("tmp." + currentUserId + ".defectSystemMonth", JSON.toJSONString(defectSystemMonthList), 300l);
		redisUtils.set("tmp." + currentUserId + ".defectSystemYear", JSON.toJSONString(defectSystemYearList), 300l);
		redisUtils.set("tmp." + currentUserId + ".defectOamPro", JSON.toJSONString(defectOamList), 300l);
		redisUtils.set("tmp." + currentUserId + ".defectNewPro", JSON.toJSONString(defectNewList), 300l);
		redisUtils.set("tmp." + currentUserId + ".defectProjectMonth", JSON.toJSONString(defectProjectMonthList), 300l);
		redisUtils.set("tmp." + currentUserId + ".defectProjectYear", JSON.toJSONString(defectProjectYearList), 300l);
		redisUtils.set("tmp." + currentUserId + ".defectLevel", JSON.toJSONString(defectLevelList), 300l);
		redisUtils.set("tmp." + currentUserId + ".worseProject", JSON.toJSONString(worseProjectList), 300l);
		redisUtils.set("tmp." + currentUserId + ".remainDefect", JSON.toJSONString(remainDefectList), 300l);
		Map<String, Object> map = new HashMap<>();
		/* 缺陷率整体趋势图(按月) */
		Map<String, Object> defectProMap = new HashMap<>();
		List<String> timeList = new ArrayList<>();
		List<String> defectProList1 = new ArrayList<>();
		for (Map<String, Object> map2 : defectOamList) {
			timeList.add(map2.get("yearMonth").toString());
			String defectProStr = map2.get("defectPercent").toString();
			defectProList1
					.add(defectProStr);
		}

		defectProMap.put("time", timeList);
		defectProMap.put("defectPro", defectProList1);

		/* 本月缺陷率统计(按系统) */
		Map<String, Object> defectProMonthMap = new HashMap<>();
		List<String> systemList1 = new ArrayList<>();
		List<String> defectProList2 = new ArrayList<>();
//		for (Map<String, Object> map2 : defectProMonthList) {
//			systemList1.add(map2.get("systemName") == null ? " " : map2.get("systemName").toString());
//			String defectProStr = map2.get("defectPro").toString();
//			defectProList2
//					.add(String.format("%.2f", Double.valueOf(defectProStr.substring(0, defectProStr.length() - 1))));
//		}
		defectProMonthMap.put("systemName", systemList1);
		defectProMonthMap.put("defectPro", defectProList2);

		/* 全年缺陷率统计(按系统) */
		Map<String, Object> defectProYearMap = new HashMap<>();
		List<String> systemList2 = new ArrayList<>();
		List<String> defectProList3 = new ArrayList<>();
//		for (Map<String, Object> map2 : defectYearList) {
//			systemList2.add(map2.get("systemName") == null ? " " : map2.get("systemName").toString());
//			String defectProStr = map2.get("defectPro").toString();
//			defectProList3
//					.add(String.format("%.2f", Double.valueOf(defectProStr.substring(0, defectProStr.length() - 1))));
//		}
		defectProYearMap.put("systemName", systemList2);
		defectProYearMap.put("defectPro", defectProList3);

		/* 本月缺陷率统计(按项目) */
		Map<String, Object> defectProProjectMonthMap = new HashMap<>();
		List<String> projectList1 = new ArrayList<>();
		List<String> defectProList4 = new ArrayList<>();
		for (Map<String, Object> map2 : defectProjectMonthList) {
			projectList1.add(map2.get("projectName") == null ? " " : map2.get("projectName").toString());
			String defectProStr = map2.get("defectPro").toString();
			defectProList4
					.add(String.format("%.2f", Double.valueOf(defectProStr)));
		}
		defectProProjectMonthMap.put("projectName", projectList1);
		defectProProjectMonthMap.put("defectPro", defectProList4);

		/* 本月缺陷率统计(按项目) */
		Map<String, Object> defectProProjectYearMap = new HashMap<>();
		List<String> projectList2 = new ArrayList<>();
		List<String> defectProList5 = new ArrayList<>();
		for (Map<String, Object> map2 : defectProjectYearList) {
			projectList2.add(map2.get("projectName") == null ? " " : map2.get("projectName").toString());
			String defectProStr = map2.get("defectPro").toString();
			defectProList5
					.add(String.format("%.2f", Double.valueOf(defectProStr)));
		}
		defectProProjectYearMap.put("projectName", projectList2);
		defectProProjectYearMap.put("defectPro", defectProList5);

		map.put("defectPro", defectProMap);
		map.put("defectProMonth", defectProMonthMap);
		map.put("defectProYear", defectProYearMap);
		map.put("defectProProjectMonth", defectProProjectMonthMap);
		map.put("defectProProjectYear", defectProProjectYearMap);
		map.put("defectLevel", defectProLevelMap);
		return map;
	}

	/**
	 * 目录
	 * 
	 * @param workbook
	 */
	public void catalogue(Workbook workbook) {
		String sheetName = "目录";
		CreationHelper createHelper = workbook.getCreationHelper();
		CellStyle boldStyle = workbook.createCellStyle();
		CellStyle titleStyle = workbook.createCellStyle();
		CellStyle centerStyle = workbook.createCellStyle();
		CellStyle bgStyle = workbook.createCellStyle();
		centerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		bgStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		bgStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		bgStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		Font whiteFont = workbook.createFont();
		whiteFont.setColor(IndexedColors.WHITE.getIndex());
		bgStyle.setFont(whiteFont);
		CellStyle[] borderStyles = getBorderStyle(workbook);
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.setDisplayGridlines(false);
		Font boldFont = workbook.createFont();
		boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // 宽度
		boldStyle.setFont(boldFont);
		Font titleFont = workbook.createFont();
		titleFont.setFontName("微软雅黑"); // 微软雅黑
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // 宽度
		titleFont.setFontHeight((short) 400);
		titleStyle.setFont(titleFont);
		titleStyle.setWrapText(true);
		for (int i = 0; i <= 32; i++) {
			Row row = sheet.createRow(i);
			for (int j = 0; j <= 17; j++) {
				Cell cell = row.createCell(j);
				if (i == 0 && j > 0 && j < 17) {
					cell.setCellStyle(borderStyles[2]);
				}
				if (i == 32 && j > 0 && j < 17) {
					cell.setCellStyle(borderStyles[0]);
				}
				if (j == 0 && i > 0 && i < 32) {
					cell.setCellStyle(borderStyles[3]);
				}
				if (j == 17 && i > 0 && i < 32) {
					cell.setCellStyle(borderStyles[1]);
				}
				if (j == 9 && i > 0 && i < 32) {
					cell.setCellStyle(borderStyles[4]);
				}
				/* 标题 */
				if (i == 7 && j == 3) {
					cell.setCellStyle(titleStyle);
					cell.setCellValue("软件质量分析月报\r\n(测试管理组)");
				}
				/* 说明 */
				if (i == 28 && j == 4) {
					cell.setCellValue("测试管理组");
				}
				/* 目录 */
				if (i == 4 && j == 12) {
					cell.setCellStyle(boldStyle);
					cell.setCellValue("目录");
				}
				if (i == 22 && j == 3) {
					borderStyles[5].setFont(boldFont);
					cell.setCellStyle(borderStyles[5]);
					cell.setCellValue("文件状态");
				}
				if (i == 23 && j == 3) {
					borderStyles[5].setFont(boldFont);
					cell.setCellStyle(borderStyles[5]);
					cell.setCellValue("文件标示");
				}
				if (i == 24 && j == 3) {
					borderStyles[5].setFont(boldFont);
					cell.setCellStyle(borderStyles[5]);
					cell.setCellValue("作者");
				}
				if (i == 25 && j == 3) {
					borderStyles[5].setFont(boldFont);
					cell.setCellStyle(borderStyles[5]);
					cell.setCellValue("完成日期");
				}
				if (i == 22 && j == 4) {
					cell.setCellStyle(borderStyles[6]);
					cell.setCellValue("正式发布");
				}
				if (i == 23 && j == 4) {
					cell.setCellStyle(borderStyles[6]);
					cell.setCellValue("软件测试月报");
				}
				if (i == 24 && j == 4) {
					cell.setCellStyle(borderStyles[6]);
					cell.setCellValue("测试管理组");
				}
				if (i == 25 && j == 4) {
					cell.setCellStyle(borderStyles[6]);
					String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					cell.setCellValue(date);
				}
				if (i == 8 && j == 10) {
					cell.setCellStyle(bgStyle);
					cell.setCellValue("一");
				}
				if (i == 8 && j == 11) {
					cell.setCellStyle(borderStyles[5]);
					cell.setCellValue("2018年12月第三方测试项目情况概述");
				}
				if (i == 9 && j == 10) {
					cell.setCellStyle(centerStyle);
					cell.setCellValue("1、");
				}
				if (i == 9 && j == 11) {
					cell.setCellValue("本月测试情况概述");
				}
				if (i == 12 && j == 10) {
					cell.setCellStyle(bgStyle);
					cell.setCellValue("二");
				}
				if (i == 12 && j == 11) {
					cell.setCellStyle(borderStyles[5]);
					cell.setCellValue("2018年12月第三方测试项目情况分析");
				}
				if (i == 13 && j == 10) {
					cell.setCellStyle(centerStyle);
					cell.setCellValue("1、");
				}
				if (i == 13 && j == 11) {
					Hyperlink hyperlink = createHelper.createHyperlink(XSSFHyperlink.LINK_DOCUMENT);
					hyperlink.setAddress("#版本情况汇总!A1");
					cell.setHyperlink(hyperlink);
					cell.setCellValue("版本情况汇总");
				}
				if (i == 14 && j == 10) {
					cell.setCellStyle(centerStyle);
					cell.setCellValue("2、");
				}
				if (i == 14 && j == 11) {
					Hyperlink hyperlink = createHelper.createHyperlink(XSSFHyperlink.LINK_DOCUMENT);
					hyperlink.setAddress("#缺陷率整体趋势图!A1");
					cell.setHyperlink(hyperlink);
					cell.setCellValue("缺陷率整体趋势图");
				}
				if (i == 15 && j == 10) {
					cell.setCellStyle(centerStyle);
					cell.setCellValue("3、");
				}
				if (i == 15 && j == 11) {
					Hyperlink hyperlink = createHelper.createHyperlink(XSSFHyperlink.LINK_DOCUMENT);
					hyperlink.setAddress("#月缺陷率统计!A1");
					cell.setHyperlink(hyperlink);
					cell.setCellValue("月缺陷率统计");
				}
				if (i == 16 && j == 10) {
					cell.setCellStyle(centerStyle);
					cell.setCellValue("4、");
				}
				if (i == 16 && j == 11) {
					Hyperlink hyperlink = createHelper.createHyperlink(XSSFHyperlink.LINK_DOCUMENT);
					hyperlink.setAddress("#全年测试数量和质量统计!A1");
					cell.setHyperlink(hyperlink);
					cell.setCellValue("全年测试数量和质量统计");
				}
				if (i == 19 && j == 10) {
					cell.setCellStyle(bgStyle);
					cell.setCellValue("三");
				}
				if (i == 19 && j == 11) {
					cell.setCellStyle(borderStyles[5]);
					cell.setCellValue("2018年12月第三方开发团队情况分析");
				}
				if (i == 20 && j == 10) {
					cell.setCellStyle(centerStyle);
					cell.setCellValue("1、");
				}
				if (i == 20 && j == 11) {
					Hyperlink hyperlink = createHelper.createHyperlink(XSSFHyperlink.LINK_DOCUMENT);
					hyperlink.setAddress("#月各项目组质量统计!A1");
					cell.setHyperlink(hyperlink);
					cell.setCellValue("月各项目组质量统计");
				}
				if (i == 21 && j == 10) {
					cell.setCellStyle(centerStyle);
					cell.setCellValue("2、");
				}
				if (i == 21 && j == 11) {
					Hyperlink hyperlink = createHelper.createHyperlink(XSSFHyperlink.LINK_DOCUMENT);
					hyperlink.setAddress("#年各项目组质量统计!A1");
					cell.setHyperlink(hyperlink);
					cell.setCellValue("年各项目组质量统计");
				}
				if (i == 25 && j == 10) {
					cell.setCellStyle(bgStyle);
					cell.setCellValue("四");
				}
				if (i == 25 && j == 11) {
					cell.setCellStyle(borderStyles[5]);
					cell.setCellValue("缺陷分析");
				}
				if (i == 26 && j == 10) {
					cell.setCellStyle(centerStyle);
					cell.setCellValue("1、");
				}
				if (i == 26 && j == 11) {
					Hyperlink hyperlink = createHelper.createHyperlink(XSSFHyperlink.LINK_DOCUMENT);
					hyperlink.setAddress("#月缺陷等级分布!A1");
					cell.setHyperlink(hyperlink);
					cell.setCellValue("月缺陷等级分布");
				}
			}
		}
		CellRangeAddress titleCell = new CellRangeAddress(7, 11, 3, 6);
		sheet.addMergedRegion(titleCell);
		CellRangeAddress authorCell = new CellRangeAddress(28, 28, 4, 5);
		sheet.addMergedRegion(authorCell);
		for (int i = 8; i <= 32; i++) {
			if (i >= 22 && i <= 25) {
				CellRangeAddress cellRangeAddress = new CellRangeAddress(i, i, 4, 5);
				sheet.addMergedRegion(cellRangeAddress);
				ExcelUtil.setBorderStyle(XSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
			}
			if (i >= 8 && i <= 32) {
				CellRangeAddress cellRangeAddress = new CellRangeAddress(i, i, 11, 14);
				sheet.addMergedRegion(cellRangeAddress);
				if (i == 8 || i == 12 || i == 19 || i == 25) {
					ExcelUtil.setBorderStyle(XSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
				}
			}
		}
	}

	/**
	 * 测试情况概述
	 * 
	 * @param workbook
	 * @param time
	 */
	public void summary(Workbook workbook, String time) {
		String[] title = { "序号", "内容", "", "", "", "" };
		String sheetName = "测试情况概述";
		CellStyle titleStyle = workbook.createCellStyle();
		CellStyle headStyle = workbook.createCellStyle();
		CellStyle valueStyle = workbook.createCellStyle();
		CellStyle contentStyle = workbook.createCellStyle();
		CellStyle tipStyle = workbook.createCellStyle();
		CellStyle tipTableStyle = workbook.createCellStyle();
		String titleContent = "测试情况概述(" + time + ")";
		Sheet sheet = ExcelUtil.createSheet(title, 1, sheetName, workbook, 1, 2, titleContent, titleStyle, headStyle,
				valueStyle);
		valueStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		valueStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		CellRangeAddress headRegion = new CellRangeAddress(2, 2, 2, 6);
		sheet.addMergedRegion(headRegion);
		contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		Font font = workbook.createFont();
		font.setColor(IndexedColors.BLUE.getIndex());
		tipStyle.setFont(font);
		tipStyle.setWrapText(true);
		tipTableStyle.cloneStyleFrom(tipStyle);
		ExcelUtil.setValueStyle(tipTableStyle, HSSFCellStyle.BORDER_DOTTED, IndexedColors.BLUE.getIndex());
		for (int i = 1; i <= 7; i++) {
			Row row = sheet.createRow(i + 2);
			if (i <= 5 && i >= 2) {
				row.setHeightInPoints(150);
			} else {
				row.setHeightInPoints(50);
			}
			Cell cell = row.createCell(1);
			cell.setCellStyle(valueStyle);
			cell.setCellValue(i);
			for (int j = 2; j <= 6; j++) {
				Cell cell2 = row.createCell(j);
				cell2.setCellStyle(contentStyle);
			}
			CellRangeAddress region = new CellRangeAddress(i + 2, i + 2, 2, 6);
			sheet.addMergedRegion(region);
			ExcelUtil.setBorderStyle(XSSFCellStyle.BORDER_DOTTED, region, sheet, workbook);
		}
		Row row = sheet.createRow(11);
		Cell cell = row.createCell(1);
		cell.setCellStyle(tipStyle);
		cell.setCellValue("注1");

		Row row1 = sheet.createRow(12);
		row1.setHeightInPoints(30);
		Cell cell1 = row1.createCell(1);
		cell1.setCellStyle(tipStyle);
		cell1.setCellValue("全年任务数小于等于5个的系统，不参与按系统的全年累计排名。\r\n" + "全年任务数小于等于5个的项目组，不参与项目组的全年累计排名。");

		Row row2 = sheet.createRow(14);
		Cell cell2 = row2.createCell(1);
		cell2.setCellStyle(tipStyle);
		cell2.setCellValue("注2");

		Row row3 = sheet.createRow(15);
		Cell cell3 = row3.createCell(1);
		cell3.setCellStyle(tipStyle);
		cell3.setCellValue("测试风险评定（根据版本变更率评定）");

		String[][] riskArr = { { "<10%", "低" }, { "10%-20%", "中" }, { ">20%", "高" } };
		for (int i = 0; i < 3; i++) {
			Row row4 = sheet.createRow(16 + i);
			for (int j = 0; j < 2; j++) {
				Cell cell4 = row4.createCell(1 + j);
				cell4.setCellStyle(tipTableStyle);
				cell4.setCellValue(riskArr[i][j]);
			}
		}

		Row row5 = sheet.createRow(20);
		Cell cell5 = row5.createCell(1);
		cell5.setCellStyle(tipStyle);
		cell5.setCellValue("注3");

		Row row6 = sheet.createRow(21);
		Cell cell6 = row6.createCell(1);
		cell6.setCellStyle(tipStyle);
		cell6.setCellValue("开发质量评定（根据缺陷率评定）");

		String[][] devArr = { { "<2%", "较高" }, { "2%-4%", "一般" }, { ">4%", "较低" } };
		for (int i = 0; i < 3; i++) {
			Row row4 = sheet.createRow(22 + i);
			for (int j = 0; j < 2; j++) {
				Cell cell4 = row4.createCell(1 + j);
				cell4.setCellStyle(tipTableStyle);
				cell4.setCellValue(devArr[i][j]);
			}
		}

		Row row7 = sheet.createRow(26);
		Cell cell7 = row7.createCell(1);
		cell7.setCellStyle(tipStyle);
		cell7.setCellValue("注4");

		Row row8 = sheet.createRow(27);
		Cell cell8 = row8.createCell(1);
		cell8.setCellStyle(tipStyle);
		cell8.setCellValue("修复质量评定（根据平均修复伦次评定）");

		String[][] fixArr = { { "<1.05", "好" }, { "1.05-1.15", "一般" }, { ">4%", "差" } };
		for (int i = 0; i < 3; i++) {
			Row row4 = sheet.createRow(28 + i);
			for (int j = 0; j < 2; j++) {
				Cell cell4 = row4.createCell(1 + j);
				cell4.setCellStyle(tipTableStyle);
				cell4.setCellValue(fixArr[i][j]);
			}
		}

		CellRangeAddress region = new CellRangeAddress(12, 12, 1, 6);
		sheet.addMergedRegion(region);
		CellRangeAddress region1 = new CellRangeAddress(15, 15, 1, 3);
		sheet.addMergedRegion(region1);
		CellRangeAddress region2 = new CellRangeAddress(21, 21, 1, 3);
		sheet.addMergedRegion(region2);
		CellRangeAddress region3 = new CellRangeAddress(27, 27, 1, 3);
		sheet.addMergedRegion(region3);
	}

	/**
	 * 软件开发版本情况
	 * 
	 * @param workbook
	 * @param contents
	 * @param time
	 */
	public void devVersion(Workbook workbook, List<Map> contents, String time) {
		String[] title = { "周期", "计划内版本次数", "临时版本次数", "临时增加任务数", "临时删减任务数", "测试任务总数", "业务需求",
				"缺陷修复","版本变更率" };
		String sheetName = "版本情况汇总";
		CellStyle titleStyle = workbook.createCellStyle();
		CellStyle headStyle = workbook.createCellStyle();
		CellStyle valueStyle = workbook.createCellStyle();
		String titleContent = "软件开发版本情况概述(" + time + ")";
		int startRow = 4;
		Sheet sheet = ExcelUtil.createSheet(title, 1, sheetName, workbook, 1, 3, titleContent, titleStyle, headStyle,
				valueStyle);
		Row regionHeadRow = sheet.createRow(2);
		int startCol = 1;
		for (int i = 0; i < title.length; i++) {
			Cell cell = regionHeadRow.createCell(startCol);
			cell.setCellStyle(headStyle);
			if (startCol == 1) {
				cell.setCellValue("周期");
			} else if (startCol == 2) {
				cell.setCellValue("版本数");
			} else if (startCol == 4) {
				cell.setCellValue("测试任务");
			} else if (startCol == 8) {
				cell.setCellValue("任务类型");
			}
			startCol++;
		}
		CellRangeAddress region = new CellRangeAddress(2, 3, 1, 1);
		sheet.addMergedRegion(region);
		ExcelUtil.setBorderStyle(XSSFCellStyle.BORDER_DOTTED, region, sheet, workbook);
		CellRangeAddress region1 = new CellRangeAddress(2, 2, 2, 3);
		sheet.addMergedRegion(region1);
		ExcelUtil.setBorderStyle(XSSFCellStyle.BORDER_DOTTED, region1, sheet, workbook);
		CellRangeAddress region2 = new CellRangeAddress(2, 2, 4, 7);
		sheet.addMergedRegion(region2);
		ExcelUtil.setBorderStyle(XSSFCellStyle.BORDER_DOTTED, region2, sheet, workbook);
		CellRangeAddress region3 = new CellRangeAddress(2, 2, 8, 9);
		sheet.addMergedRegion(region3);
		ExcelUtil.setBorderStyle(XSSFCellStyle.BORDER_DOTTED, region3, sheet, workbook);
		String[] field = new String[] { "yearMonth", "planWindowsNumber", "tempWindowsNumber", "tempAddTaskNumber", "tempDelTaskNumber", "totalTaskNumber",
				"requirementNumber", "defectNumber", "changePercent", };
		Map<String, Object> formatMap = new HashMap<String, Object>();
		formatMap.put("changePercent", "SUM(e@:f@)/g@");
		printContent(workbook, contents, sheet, 4, 1, valueStyle, field, null, formatMap);

	}

	/**
	 * 缺陷率整体趋势图
	 * 
	 * @param workbook
	 * @param contents
	 * @param time
	 */
	public void defectPro(Workbook workbook, List<Map> oamContents,List<Map> newContents, String time, String imgUrl) {
		String[] title = { "月次", "测试任务数", "缺陷总数", "修复缺陷数", "设计用例数", "缺陷率", "漏检缺陷数", "检出率", "累计修复轮次", "平均修复轮次" };
		String sheetName = "缺陷率整体趋势图";
		String[] field = new String[] { "yearMonth", "taskNumber", "defectNumber", "repairedDefectNumber", "designCaseNumber",
				"defectPercent", "lastmonthUndefectedNumber", "checkPro", "totalRepairRound", "avgRepairRound" };
		Sheet sheet = fillDefectProTable(title, field, time, oamContents, newContents, sheetName, workbook);
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(imgUrl.split("base64,")[1]);
		Drawing patriarch = sheet.createDrawingPatriarch();
		ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 1, 1, 10, 19);
		patriarch.createPicture(anchor, workbook.addPicture(byteArr, XSSFWorkbook.PICTURE_TYPE_JPEG));
		sheet.setDisplayGridlines(false);
	}

	public void defectProBySystem(Workbook workbook, List<Map> contents, String time, String imgUrl) {
		String[] title = { "系统名称", "测试任务数", "缺陷总数", "修复缺陷数", "遗留缺陷数", "设计用例数", "缺陷率", "累计修复轮次", "平均修复轮次", "漏检缺陷数",
				"漏检缺陷归属" };
		String sheetName = "月缺陷率统计";
		
		String[] field = new String[] { "systemName", "taskNumber", "defectNumber", "repairedDefectNumber", "unrepairedDefectNumber",
				"designCaseNumber", "defectPercent", "totalRepairRound", "avgRepairRound", "lastmonthUndefectedNumber", "lastmonthUndefectedBelonger" };
		/*printContent(workbook, contents, sheet, startRow, 1, valueStyle, field, null, null);*/
		Sheet sheet = fillSystemTable(title,field,time,contents,sheetName,workbook);
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(imgUrl.split("base64,")[1]);
		Drawing patriarch = sheet.createDrawingPatriarch();
		ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 1, 1, 11, 19);
		patriarch.createPicture(anchor, workbook.addPicture(byteArr, XSSFWorkbook.PICTURE_TYPE_JPEG));
		sheet.setDisplayGridlines(false);
	}

	public void defectProBySystemAllDate(Workbook workbook, List<Map> contents, String time, String imgUrl) {
		String[] title = { "系统名称", "测试任务数", "缺陷总数", "修复缺陷数", "遗留缺陷数", "设计用例数", "缺陷率", "漏检缺陷数", "检出率", "累计修复轮次",
				"平均修复轮次" };
		String sheetName = "全年测试数量和质量统计";
		
		String[] field = new String[] { "systemName", "taskNumber", "defectNumber", "repairedDefectNumber", "unrepairedDefectNumber",
				"designCaseNumber", "defectPercent", "lastmonthUndefectedNumber", "checkPro", "totalRepairRound", "avgRepairRound" };
		Sheet sheet = fillSystemTable(title,field,time,contents,sheetName,workbook);
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(imgUrl.split("base64,")[1]);
		Drawing patriarch = sheet.createDrawingPatriarch();
		ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 1, 1, 11, 19);
		patriarch.createPicture(anchor, workbook.addPicture(byteArr, XSSFWorkbook.PICTURE_TYPE_JPEG));
		sheet.setDisplayGridlines(false);
	}

	/**
	 * 填充系统类报表(只适用于统计系统的缺陷率报表)
	 * @param title
	 * @param field
	 * @param time
	 * @param contents
	 * @param sheetName
	 * @param workbook
	 * @return
	 */
	public Sheet fillSystemTable(String[] title,String[] field,String time,List<Map> contents,String sheetName,Workbook workbook) {
		String titleOamContent = "缺陷率统计(" + time + ")运维类系统";
		CellStyle titleStyle = workbook.createCellStyle();
		CellStyle headStyle = workbook.createCellStyle();
		CellStyle valueStyle = workbook.createCellStyle();
		int startOamRow = 23;
		List<Map> oamSystemList = new ArrayList<>();
		List<Map> newSystemList = new ArrayList<>();
		List<String> oamSystemCodeList = new ArrayList<>();
		List<String> newSystemCodeList = new ArrayList<>();
		if (!StringUtils.isEmpty(oamSystem)) {
			oamSystemCodeList = Arrays.asList(oamSystem.split(","));
		}
		if(!StringUtils.isEmpty(newSystem)) {
			newSystemCodeList = Arrays.asList(newSystem.split(","));
		}
		for (Map map : contents) {
			if (oamSystemCodeList.contains(map.get("systemCode").toString())) {
				oamSystemList.add(map);
			}
			if(newSystemCodeList.contains(map.get("systemCode").toString())) {
				newSystemList.add(map);
			}
		}
		Sheet sheet = ExcelUtil.createSheet(title, 1, sheetName, workbook, 21, 22, titleOamContent, titleStyle, headStyle,
				valueStyle);
		startOamRow = printTable(sheet,valueStyle,oamSystemList,startOamRow,field,null);
		
		String titleNewContent = "缺陷率统计(" + time + ")新建类系统";
		createTitle(title, 1, sheet, workbook, startOamRow+1, startOamRow+2, titleNewContent, titleStyle, headStyle,
				valueStyle);
		int startNewRow = startOamRow + 3;
		printTable(sheet, valueStyle, newSystemList, startNewRow, field,null);
		return sheet;
	}
	/**
	 * 填充缺陷整体趋势表格(只适用于统计月份的缺陷率报表)
	 * @param title
	 * @param field
	 * @param time
	 * @param oamContents
	 * @param newContents
	 * @param sheetName
	 * @param workbook
	 * @param totals
	 * @return
	 */
	private Sheet fillDefectProTable(String[] title,String[] field,String time,List<Map> oamContents,List<Map> newContents,String sheetName,Workbook workbook) {
		Object[] totals1 = new Object[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		Object[] totals2 = new Object[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		String titleOamContent = "缺陷率整体趋势(" + time + ")运维类系统";
		CellStyle titleStyle = workbook.createCellStyle();
		CellStyle headStyle = workbook.createCellStyle();
		CellStyle valueStyle = workbook.createCellStyle();
		int startOamRow = 23;
		Sheet sheet = ExcelUtil.createSheet(title, 1, sheetName, workbook, 21, 22, titleOamContent, titleStyle, headStyle,
				valueStyle);
		startOamRow = printTable(sheet,valueStyle,oamContents,startOamRow,field,totals1);
		String titleNewContent = "缺陷率统计(" + time + ")新建类系统";
		createTitle(title, 1, sheet, workbook, startOamRow+1, startOamRow+2, titleNewContent, titleStyle, headStyle,
				valueStyle);
		int startNewRow = startOamRow + 3;
		valueStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		printTable(sheet, valueStyle, newContents, startNewRow, field,totals2);
		return sheet;
	}
	
	private int printTable(Sheet sheet,CellStyle valueStyle,List<Map> systemList,int startRow,String[] field,Object[] totals) {
		for(int i = 0;i <= systemList.size();i++) {
			Row row = sheet.createRow(startRow);
			if(i < systemList.size()) {
				Map<String, Object> map = systemList.get(i);
				for(int j = 0;j < field.length;j++) {
					Cell cell = row.createCell(j+1);
					cell.setCellStyle(valueStyle);
					if(map.get(field[j])!=null) {
						String content = map.get(field[j]).toString();
						if(field[j].equals("defectPercent")) {
							Double contentDou = Double.valueOf(content);
							String contentNum = (contentDou.equals(0d) ? "0" : String.format("%.2f", contentDou));
							cell.setCellValue(contentNum+"%");
						}else {
							String contentString = map.get(field[j]).toString();
							if (NumberUtils.isNumber(contentString) && contentString.length() < 10 && contentString.indexOf(".") == -1) {  //数字且位数小于10
								cell.setCellValue(Integer.valueOf(contentString));
							}else {
								cell.setCellValue(contentString);
							}
							if (totals != null && NumberUtils.isNumber(content) && content.indexOf(".") == -1) {
								totals[j - 1] = Integer.valueOf(totals[j - 1].toString()) + Integer.valueOf(content);
							}
						}
					}else {
						cell.setCellValue("");
					}
				}
			}else if(totals != null){
				Cell cell = row.createCell(1);
				cell.setCellStyle(valueStyle);
				cell.setCellValue("合计");
				for (int j = 0; j < totals.length; j++) {
					Cell valueCell = row.createCell(j + 2);
					valueCell.setCellStyle(valueStyle);
					String content = totals[j].toString();
					if (j == 4) {
						content = totals[3].toString().equals("0") ? "0%"
								: String.format("%.2f",
										Double.valueOf(totals[1].toString()) / Double.valueOf(totals[3].toString()) * 100)
										+ "%";
					} else if (j == 6) {
						content = "100%";
					} else if (j == 8) {
						content = totals[2].toString().equals("0") ? "0"
								: String.format("%.2f",
										Double.valueOf(totals[7].toString()) / Double.valueOf(totals[2].toString()));
					}
					valueCell.setCellValue(content);
				}
			}
			startRow++;
		}
		return startRow;
	}
	
	/**
	 * 创建表格头部(系统缺陷率报表临时用)
	 * @param title
	 * @param headColNum
	 * @param sheet
	 * @param workbook
	 * @param titleRowNumb
	 * @param headRowNum
	 * @param titleContent
	 * @param titleStyle
	 * @param headStyle
	 * @param valueStyle
	 */
	public void createTitle(String[] title, Integer headColNum, Sheet sheet,
			Workbook workbook, Integer titleRowNumb,Integer headRowNum,String titleContent, CellStyle titleStyle, CellStyle headStyle,
			CellStyle valueStyle) {
		// 创建头部
		Cell cell = null;
		Font titleFont = workbook.createFont();
		titleFont.setFontName("微软雅黑"); // 微软雅黑
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // 宽度
		titleFont.setItalic(false); // 是否使用斜体
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setFont(titleFont);
		titleStyle.setWrapText(true);

		Font headFont = workbook.createFont();
		headFont.setColor(IndexedColors.WHITE.getIndex());
		headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headStyle.setFont(headFont);
		headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
		headStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());// 设置背景色
		headStyle.setBorderRight(HSSFCellStyle.BORDER_DOTTED);
		headStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
		headStyle.setBorderLeft(HSSFCellStyle.BORDER_DOTTED);
		headStyle.setLeftBorderColor(IndexedColors.BLUE.getIndex());
		headStyle.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
		headStyle.setTopBorderColor(IndexedColors.BLUE.getIndex());
		headStyle.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
		headStyle.setBottomBorderColor(IndexedColors.BLUE.getIndex());

		Row titleRow = sheet.createRow(titleRowNumb);
		titleRow.setHeightInPoints(30);
		Cell titleCell = titleRow.createCell(headColNum);
		titleCell.setCellStyle(titleStyle);
		titleCell.setCellValue(titleContent);
		CellRangeAddress region = new CellRangeAddress(titleRowNumb, titleRowNumb, headColNum, headColNum+title.length-1);
		sheet.addMergedRegion(region);
		
		Row row = sheet.createRow(headRowNum);
		for (int i = 0, len = title.length; i < len; i++) {
			cell = row.createCell(headColNum);
			cell.setCellValue(title[i]);
			cell.setCellStyle(headStyle);
			headColNum++;
		}

		ExcelUtil.setValueStyle(valueStyle,HSSFCellStyle.BORDER_THIN,IndexedColors.GREY_50_PERCENT.getIndex());
	}
	
	public void defectProByProject(Workbook workbook, List<Map> contents, String time, Integer type, String imgUrl) {
		String[] title = { "项目组名称", "系统名称", "需求数", "缺陷总数", "修复缺陷数", "遗留缺陷数", "设计用例数", "缺陷率" };
		String sheetName = type == 1 ? "月各项目组质量统计" : "年各项目组质量统计";
		CellStyle titleStyle = workbook.createCellStyle();
		CellStyle headStyle = workbook.createCellStyle();
		CellStyle valueStyle = workbook.createCellStyle();
		String titleContent = "项目组质量统计(" + time + ")";
		int startRow = 25;
		Sheet sheet = ExcelUtil.createSheet(title, 0, sheetName, workbook, 23, 24, titleContent, titleStyle, headStyle,
				valueStyle);
		String[] field = new String[] { "projectName", "systemName", "taskCount", "defectCount", "fixedDefectCount",
				"remainDefect", "caseCount", "defectPro" };
		printContent(workbook, contents, sheet, startRow, 0, valueStyle, field, null, null);
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(imgUrl.split("base64,")[1]);
		Drawing patriarch = sheet.createDrawingPatriarch();
		ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 0, 1, 11, 20);
		patriarch.createPicture(anchor, workbook.addPicture(byteArr, XSSFWorkbook.PICTURE_TYPE_JPEG));
		sheet.setDisplayGridlines(false);
	}

	public void defectLevel(Workbook workbook, List<Map> contents, String time, String imgUrl) {
		String[] title = { "缺陷等级", "缺陷个数" };
		String sheetName = "月缺陷等级分布";
		CellStyle titleStyle = workbook.createCellStyle();
		CellStyle headStyle = workbook.createCellStyle();
		CellStyle valueStyle = workbook.createCellStyle();
		Map<String, Object> formatMap = new HashMap<>();
		formatMap.put("0", "left");
		formatMap.put("1", "center");
		String titleContent = "缺陷等级分布\r\n(" + time + ")";
		int startRow = 6;
		Sheet sheet = ExcelUtil.createSheet(title, 9, sheetName, workbook, 4, 5, titleContent, titleStyle, headStyle,
				valueStyle);
		String[] field = new String[] { "severityLevelName", "defectCount" };
		printContent(workbook, contents, sheet, startRow, 9, valueStyle, field, null, formatMap);
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(imgUrl.split("base64,")[1]);
		Drawing patriarch = sheet.createDrawingPatriarch();
		ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 1, 1, 8, 17);
		patriarch.createPicture(anchor, workbook.addPicture(byteArr, XSSFWorkbook.PICTURE_TYPE_JPEG));
		sheet.setDisplayGridlines(false);
	}
	/**
	 * 遗留缺陷清单
	 * @param workbook
	 * @param contents
	 */
	public void remainDefect(Workbook workbook, List<Map> contents) {
		String[] title = {"缺陷ID","项目","摘要","需求编号","状态","分配给","开发人","检测于版本","缺陷发现阶段","缺陷等级","创建人"};
		String sheetName = "遗留缺陷清单";
		CellStyle titleStyle = workbook.createCellStyle();
		CellStyle headStyle = workbook.createCellStyle();
		CellStyle valueStyle = workbook.createCellStyle();
		String titleContent = "遗留缺陷清单";
		int startRow = 2;
		Sheet sheet = ExcelUtil.createSheet(title, 0, sheetName, workbook, 0, 1, titleContent, titleStyle, headStyle,
				valueStyle);
		String[] field = new String[] { "defectCode", "systemName","defectSummary","requirementCode","defectStatusName",
				"assignUserId","developUserId","windowName","defectSourceName","severityLevelName","createBy" };
		printContent(workbook, contents, sheet, startRow, 0, valueStyle, field, null, null);
	}

	public Integer printContent(Workbook workbook, List<Map> contents, Sheet sheet, Integer startRow, Integer startCol,
			CellStyle cellStyle, String[] field, Object[] totals, Map<String, Object> formatMap) {
		for (int i = 0; i < contents.size(); i++) {
			Row row = sheet.createRow(startRow);
			Map<String, Object> map = contents.get(i);
			Integer col = startCol;
			for (int j = 0; j < field.length; j++) {
				Cell cell = row.createCell(col);
				if(formatMap != null) {
					CellStyle cellStyle2 = workbook.createCellStyle();
					cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					ExcelUtil.setValueStyle(cellStyle2, HSSFCellStyle.BORDER_THIN,IndexedColors.GREY_50_PERCENT.getIndex());
					if (formatMap.containsKey(field[j])) {
						cellStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
						String format = formatMap.get(field[j]).toString();
						format = format.replaceAll("@", (startRow + 1) + "");
						cell.setCellFormula(format);
					}else if(formatMap.containsKey(j+"")){
						String align = formatMap.get(j+"").toString();
						cellStyle2.setAlignment(align.equals("left")?HSSFCellStyle.ALIGN_LEFT:HSSFCellStyle.ALIGN_CENTER);
					}
					cell.setCellStyle(cellStyle2);
				} else {
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cell.setCellStyle(cellStyle);
					}
				String content = map.get(field[j]) == null ? "" : map.get(field[j]).toString();
				if (NumberUtils.isNumber(content) || content.indexOf("%") != -1) {
					
						Double contentDou = 0d;
						if(content.indexOf(".") != -1) {
							if (content.indexOf("%") != -1) {
								content = content.substring(0, content.length() - 1);
								contentDou = Double.valueOf(content);
								String contentNum = (contentDou.equals(0d) ? "0" : String.format("%.2f", contentDou));
								if (totals != null && NumberUtils.isNumber(content)) {
									totals[j - 1] = Double.valueOf(totals[j - 1].toString()) + Double.valueOf(contentNum);
								}
								content = contentNum + "%";
							} else {
								contentDou = Double.valueOf(content);
								content = contentDou.equals(0d) ? "0" : String.format("%.2f", contentDou);
								if (totals != null && NumberUtils.isNumber(content)) {
									totals[j - 1] = Double.valueOf(totals[j - 1].toString()) + Double.valueOf(content);
								}
							}
						}else if (totals != null && content.indexOf("%") == -1) { 
							if (totals[j - 1].toString().indexOf(".") != -1) {
								totals[j - 1] = Double.valueOf(totals[j - 1].toString()) + Integer.valueOf(content);
							} else {
								totals[j - 1] = Integer.valueOf(totals[j - 1].toString()) + Integer.valueOf(content);
							}
						}
				}
				if (NumberUtils.isNumber(content) && content.length() < 10 && content.indexOf(".") == -1) {  //数字且位数小于10
					cell.setCellValue(Integer.valueOf(content));
				} else {
					if (content.indexOf(",") != -1) {
						content = content.replaceAll(",", "\r\n");
					}
					cell.setCellValue(content);
				}
				col++;
			}
			startRow++;
		}
		return startRow;
	}

	/**
	 * 获取边框样式
	 * 
	 * @param workbook
	 * @return
	 */
	public CellStyle[] getBorderStyle(Workbook workbook) {
		CellStyle[] cellStyles = new CellStyle[11];
		for (int i = 0; i < 8; i++) {
			cellStyles[i] = workbook.createCellStyle();
			if (i <= 4) {
				cellStyles[i].setTopBorderColor(IndexedColors.BLUE.getIndex());
				cellStyles[i].setRightBorderColor(IndexedColors.BLUE.getIndex());
				cellStyles[i].setBottomBorderColor(IndexedColors.BLUE.getIndex());
				cellStyles[i].setLeftBorderColor(IndexedColors.BLUE.getIndex());
			}
		}
		cellStyles[0].setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyles[1].setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyles[2].setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyles[3].setBorderRight(HSSFCellStyle.BORDER_THIN);

		cellStyles[4].setBorderLeft(HSSFCellStyle.BORDER_DASH_DOT);

		cellStyles[5].setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyles[5].setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyles[5].setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyles[5].setBorderRight(HSSFCellStyle.BORDER_THIN);

		cellStyles[6].setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyles[6].setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyles[6].setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyles[6].setBorderRight(HSSFCellStyle.BORDER_THIN);

		return cellStyles;
	}

	/**
	 * 最差项目组统计
	 * 
	 * @param workbook
	 * @param worseProjectList
	 * @param yearMonthList
	 */
	private void defectProByWorseProject(Workbook workbook, List<Map> worseProjectList, List<String> yearMonthList,
			String month) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> newWorseProjectList = verticalToHorizontal(worseProjectList);
		CellStyle valueStyle = workbook.createCellStyle();
		String sheetName = "全年质量较差项目组缺陷率走势";
		Sheet sheet = workbook.createSheet(sheetName);
		ExcelUtil.setValueStyle(valueStyle, HSSFCellStyle.BORDER_THIN, IndexedColors.GREY_50_PERCENT.getIndex());
		int colWidth = sheet.getColumnWidth(0) * 2;
		int intFlag = 0;
		Set<String> defectTotalSet = new HashSet<>();
		for (int i = 0; i < newWorseProjectList.size(); i++) {
			if (intFlag > 2) {
				break;
			}
			Map<String, Object> worseProjectMap = newWorseProjectList.get(i);
			if (Integer.valueOf(worseProjectMap.get("reqCount").toString()) > 5) {
				if (!defectTotalSet.contains(worseProjectMap.get("defectTotal").toString())) {
					intFlag++;
					defectTotalSet.add(worseProjectMap.get("defectTotal").toString());
				}
			} else {
				continue;
			}
			String projectName = worseProjectMap.get("systemName").toString();
			Double defectTotalDou = Double.valueOf(worseProjectMap.get("defectTotal").toString());
			String defectTotal = defectTotalDou.equals(0d) ? "0%" : String.format("%.2f", defectTotalDou) + "%";
			int firstRow = 21 + intFlag * 4;
			Row row = sheet.createRow(firstRow);
			Cell cell = row.createCell(0);
			cell.setCellValue(projectName);
			Row row1 = sheet.createRow(firstRow + 1);
			Cell cell1 = row1.createCell(0);
			cell1.setCellStyle(valueStyle);
			cell1.setCellValue("月份");
			Row row2 = sheet.createRow(firstRow + 2);
			Cell cell2 = row2.createCell(0);
			cell2.setCellStyle(valueStyle);
			cell2.setCellValue("缺陷率");

			List<Map<String, Object>> timeList = (List<Map<String, Object>>) worseProjectMap.get("timeList");
			for (int j = yearMonthList.size(); j >= 1; j--) {
				Cell monthCell = row1.createCell(j);
				monthCell.setCellStyle(valueStyle);
				monthCell.setCellValue(yearMonthList.get(yearMonthList.size() - j));
				Integer monthInt = Integer.valueOf(yearMonthList.get(yearMonthList.size() - j).split("-")[1]);

				Cell defectCell = row2.createCell(j);
				defectCell.setCellStyle(valueStyle);
				String defectPro = "0%";
				for (int k = 0; k < timeList.size(); k++) { // 判断该月份是否有缺陷率
					String timeString = timeList.get(k).get("time").toString();
					if (timeString.equals(yearMonthList.get(yearMonthList.size() - j))) {
						defectPro = timeList.get(k).get("defectPro").toString() + "%";
						break;
					} else if (monthInt > Integer.valueOf(month)) {
						defectPro = " ";
					}
				}
				defectCell.setCellValue(defectPro);
				sheet.setColumnWidth(j, colWidth < 3000 ? 3000 : colWidth);
			}
			Cell totalCell = row1.createCell(yearMonthList.size() + 1);
			totalCell.setCellStyle(valueStyle);
			totalCell.setCellValue("总计");
			Cell defectTotalCell = row2.createCell(yearMonthList.size() + 1);
			defectTotalCell.setCellStyle(valueStyle);
			defectTotalCell.setCellValue(defectTotal);
			CellRangeAddress rangeCell = new CellRangeAddress(firstRow, firstRow, 0, 2);
			sheet.addMergedRegion(rangeCell);
		}

	}
	/**
	 * 内容由纵排变横排
	 * @param worseProjectList 纵排集合
	 * @return
	 */
	private List<Map<String, Object>> verticalToHorizontal(List<Map> worseProjectList){
		List<Map<String, Object>> newWorseProjectList = new ArrayList<>();      //横排集合
		for (int i = 0;i < worseProjectList.size();i++) {
			Map<String, Object> map = worseProjectList.get(i);
			if(map.get("systemName").toString().trim().isEmpty()) {
				continue;
			}
			List<Map<String, Object>> timeList = new ArrayList<>();
			Boolean flag = false;
			Double totalDefectPro = 0d;
			Integer defectCount = 0;
			Double caseCount = 0d;
			Integer reqCount = 0;
			for (Map<String, Object> map2 : newWorseProjectList) {
				if (map2.get("systemName").toString()
						.equals(map.get("systemName").toString())) {
					Map<String, Object> defectMap = new HashMap<>();
					timeList = (List<Map<String, Object>>) map2.get("timeList");
					Double defectProDou = Double.valueOf(map.get("defectPercent").toString());
					String defectProStr = String.format("%.2f", defectProDou);
					defectCount = Integer.valueOf(map2.get("defectCount").toString());
					caseCount = Double.valueOf(map2.get("caseCount").toString());
					reqCount = Integer.valueOf(map2.get("reqCount").toString());
					defectCount += Integer.valueOf(map.get("defectNumber").toString());
					caseCount += Double.valueOf(map.get("designCaseNumber").toString());
					reqCount += Integer.valueOf(map.get("taskNumber").toString());
					totalDefectPro = caseCount == 0 ? 0d : (defectCount / caseCount * 100);
					map2.put("defectTotal", totalDefectPro);
					map2.put("defectCount", defectCount);
					map2.put("caseCount", caseCount);
					map2.put("reqCount", reqCount);
					defectMap.put("time", map.get("yearMonth"));
					defectMap.put("defectPro", defectProStr);
					timeList.add(defectMap);
					map2.put("timeList", timeList);
					flag = true;
					break;
				}
			}
			if (!flag) { //newWorseProjectList没有该月数据
				HashMap<String, Object> map2 = new HashMap<>();
				Map<String, Object> defectMap = new HashMap<>();
				Double defectProDou = Double.valueOf(map.get("defectPercent").toString());
				String defectProStr = String.format("%.2f", defectProDou);
				defectCount += Integer.valueOf(map.get("defectNumber").toString());
				caseCount += Double.valueOf(map.get("designCaseNumber").toString());
				reqCount += Integer.valueOf(map.get("taskNumber").toString());
				totalDefectPro = caseCount == 0 ? 0d : (defectCount / caseCount * 100);
				defectMap.put("time", map.get("yearMonth"));
				defectMap.put("defectPro", defectProStr);
				timeList.add(defectMap);
				map2.put("timeList", timeList);
				map2.put("systemName", map.get("systemName") == null ? " " : map.get("systemName"));
				map2.put("defectTotal", totalDefectPro);
				map2.put("defectCount", defectCount);
				map2.put("caseCount", caseCount);
				map2.put("reqCount", reqCount);
				newWorseProjectList.add(map2);
			}
		}
		/* 排序 */
		newWorseProjectList.sort((Map<String, Object> map1,
				Map<String, Object> map2) -> (Double.valueOf(map2.get("defectTotal").toString())
						- Double.valueOf(map1.get("defectTotal").toString())) > 0d ? 1 : -1);
		return newWorseProjectList;
	}
	
	/**
	 * 缺陷等级集合编排格式
	 * @param defectLevelList
	 * @param severityLevelMap
	 * @return
	 */
	private Map<String, Object> getDefectLevelMap(List<Map<String, Object>> defectLevelList,Map<String, Object> severityLevelMap){
		List<String> levels = new ArrayList<>();
		for (Map<String, Object> map : defectLevelList) {
			map.put("severityLevelName",
					map.get("severityLevel")+"-"+severityLevelMap.get(map.get("severityLevel") == null ? "" : map.get("severityLevel").toString()));
			levels.add(map.get("severityLevel") == null ? "" : map.get("severityLevel").toString());
		}
		for (Map.Entry<String, Object> entry : severityLevelMap.entrySet()) {
			if (!levels.contains(entry.getKey())) {
				Map<String, Object> map = new HashMap<>();
				map.put("severityLevel", entry.getKey());
				map.put("severityLevelName", entry.getKey()+"-"+entry.getValue());
				map.put("defectCount", 0);
				defectLevelList.add(map);
			}
		}
		Collections.sort(defectLevelList, new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				// TODO Auto-generated method stub
				return Integer.valueOf(o1.get("severityLevel").toString())
						.compareTo(Integer.valueOf(o2.get("severityLevel").toString()));
			}
		});
		
		Map<String, Object> defectProLevelMap = new HashMap<>();
		List<String> levelList = new ArrayList<>();
		List<Map<String, Object>> defectCountList = new ArrayList<>();
		for (Map<String, Object> map2 : defectLevelList) {
			Object severityLevelObj = map2.get("severityLevelName");
			levelList.add(severityLevelObj == null ? " " : severityLevelObj.toString());
			Map<String, Object> defectLevelMap = new HashMap<>();
			defectLevelMap.put("name", severityLevelObj == null ? " " : severityLevelObj.toString());
			defectLevelMap.put("value", map2.get("defectCount").toString());
			defectCountList.add(defectLevelMap);
		}
		defectProLevelMap.put("level", levelList);
		defectProLevelMap.put("defectCount", defectCountList);
		return defectProLevelMap;
	}

	/**
	 * 获取所有系统
	 */
	@Override
	public List<TblSystemInfo> getAllSystem() {
		// TODO Auto-generated method stub
		List<TblSystemInfo> systemInfos = new ArrayList<>();
		systemInfos = tblSystemInfoMapper.selectSystemByCode();
		return systemInfos;
	}

	/**
	 * 获取运维系和新建系系统缺陷率表格
	 */
	@Override
	public Map<String, Object> getDefectProData(String time, String systemIdStr, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		newSystem = tblSystemInfoMapper.selectSystemByClass(1);
		oamSystem = tblSystemInfoMapper.selectSystemByClass(2);
		String startDate = time.split(" ~ ")[0];
		String endDate = time.split(" ~ ")[1];
		String oamSystemCodeString = "";
		String newSystemCodeString = "";
		if(systemIdStr.equals("")) {
			oamSystemCodeString = oamSystem;
			newSystemCodeString = newSystem;
		}else {
			String[] codeArr = systemIdStr.split(",");
			List<String> list = Arrays.asList(codeArr);
			for(String systemCode : oamSystem.split(",")) {
				if(list.contains(systemCode)) {
					oamSystemCodeString += systemCode + ",";
				}
			}
			
			for(String systemCode : newSystem.split(",")) {
				if(list.contains(systemCode)) {
					newSystemCodeString += systemCode + ",";
				}
			}
		}
		List<TblReportMonthlySystem> defectOamSystemList = oamSystemCodeString.equals("")?new ArrayList<>():defectInfoMapper.selectDefectProBySystem(startDate, endDate,-1,oamSystemCodeString);
		List<TblReportMonthlySystem> defectNewSystemList = newSystemCodeString.equals("")?new ArrayList<>():defectInfoMapper.selectDefectProBySystem(startDate, endDate,-1,newSystemCodeString);
		List<TblReportMonthlySystem> defectTotalList = new ArrayList<>();
		defectTotalList.addAll(defectOamSystemList);
		defectTotalList.addAll(defectNewSystemList);
		/* 本月缺陷率统计(按系统) */
		Map<String, Object> defectProMonthMap = new HashMap<>();
		List<String> systemList1 = new ArrayList<>();
		List<String> defectProList2 = new ArrayList<>();
		for (TblReportMonthlySystem tblReportMonthlySystem : defectTotalList) {
			systemList1.add(tblReportMonthlySystem.getSystemName());
			Double defectPercent = tblReportMonthlySystem.getDefectPercent();
			defectProList2
					.add(String.format("%.2f",defectPercent));
		}
		String origin = request.getHeader("Host");
		defectProMonthMap.put("systemName", systemList1);
		defectProMonthMap.put("defectPro", defectProList2);
		map.put("defectOamSystemList", defectOamSystemList);
		map.put("defectNewSystemList", defectNewSystemList);
		map.put("defectProMonthMap", defectProMonthMap);
		map.put("url", "http://"+origin+"/testManageui/testReport/toDefectProReportSample?timeRange="+time+"&systemIds="+systemIdStr);
		return map;
	}

	@Override
	public Map<String, Object> getDevVersionReport(String time, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		List<TblReportMonthlySummary> summarys = new ArrayList<>();
		newSystem = tblSystemInfoMapper.selectSystemByClass(1);
		oamSystem = tblSystemInfoMapper.selectSystemByClass(2);
		for(int i = 0;i < 12;i++) {
			String curDateStr = time +"-"+ (i<9?"0"+(i+1):(i+1)) + "-26";
			String startDate = i == 0?(time+"-01-01"):DateUtil.formatDate(DateUtil.addMonth(DateUtil.getDate(curDateStr, "yyyy-MM-dd"), -1), null);
			String endDate = time +"-"+ (i<9?"0"+(i+1):(i+1)) + "-25";
			List<TblReportMonthlySummary> devVersionReportList = requirementFeatureMapper.selectDevVertionReport(startDate,
					endDate,oamSystem+","+newSystem);
			if(CollectionUtil.isNotEmpty(devVersionReportList)) {
				devVersionReportList.get(0).setYearMonth(time+"-"+(i+1));
				summarys.add(devVersionReportList.get(0));
			}	
				
		}
		String origin = request.getHeader("Host");
		map.put("summarys", summarys);
		map.put("url", "http://"+origin+"/testManageui/testReport/toDevVersionReportSample?time="+time);
		return map;
	}

	/**
	 * 最差项目统计
	 */
	@Override
	public Map<String, Object> getWorseSystemReport(String time, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		newSystem = tblSystemInfoMapper.selectSystemByClass(1);
		oamSystem = tblSystemInfoMapper.selectSystemByClass(2);
		/*查询所有系统缺陷统计*/
		List<Map> worseReportList = new ArrayList<>();
		for(int i = 0;i < 12;i++) {
			String yearMonth = time +"-"+ (i<9?"0"+(i+1):(i+1));
			String curDateStr = yearMonth + "-26";
			String startDate = i == 0?(time+"-01-01"):DateUtil.formatDate(DateUtil.addMonth(DateUtil.getDate(curDateStr, "yyyy-MM-dd"), -1), null);
			String endDate = yearMonth + "-25";
			List<Map<String, Object>> list = defectInfoMapper.getWorseDefectByTime(startDate,
					endDate,oamSystem);
			if(CollectionUtil.isNotEmpty(list)) {
				for (Map<String, Object> map : list) {
					map.put("yearMonth", yearMonth);
				}
				worseReportList.addAll(list);
			}	
		}
		
		/*竖排变横排*/
		List<Map<String, Object>> newWorseProjectList = verticalToHorizontal(worseReportList);
		
		List<List<Map<String, Object>>> tables = new ArrayList<>();    //表格的list
		List<List<String>> defectPros = new ArrayList<>();		//图表的list
		int j = 1;   //用来判断第几个list
		for (Map<String, Object> map:newWorseProjectList) {
			List<Map<String, Object>> timeList = (List<Map<String, Object>>)map.get("timeList");
			Integer reqCount = Integer.valueOf(map.get("reqCount").toString());
			if(reqCount < 5) {
				continue;
			}
			if(j <= 3) {
				List<Map<String, Object>> tableList = new ArrayList<>();
				List<String> defectProList = new ArrayList<>();
				Map<String, Object> tableMap = new HashMap<>();
				tableMap.put("systemName", map.get("systemName"));
				for (int i=0;i<12;i++) {
					boolean flag = false;
					for(Map<String, Object> map2:timeList) {
						String yearMonth = time +"-"+ (i<9?"0"+(i+1):(i+1));
						if((yearMonth).equals(map2.get("time").toString())) {
							flag = true;
							tableMap.put((i+1)+"", map2.get("defectPro").toString().equals("0.00")?"0%":map2.get("defectPro").toString()+"%");
							defectProList.add(map2.get("defectPro").toString());
							break;
						}
					}
					if(!flag) {
						tableMap.put((i+1)+"", "0%");
						defectProList.add("0");
						
					}
				}
				tableMap.put("defectTotalPro", map.get("defectTotal").toString().equals("0.0")?"0%":map.get("defectTotal").toString()+"%");
				tableList.add(tableMap);
				tables.add(tableList);
				defectPros.add(defectProList);
				j++;
			}
		}
		String origin = request.getHeader("Host");
		result.put("tables", tables);
		result.put("defectPros", defectPros);
		result.put("url", "http://"+origin+"/testManageui/testReport/toWorseSystemReportSample?time="+time);
		return result;
	}

	/**
	 * 缺陷率整体趋势
	 */
	@Override
	public Map<String, Object> getDefectTotalReport(String time, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		newSystem = tblSystemInfoMapper.selectSystemByClass(1);
		oamSystem = tblSystemInfoMapper.selectSystemByClass(2);
		List<Map<String, Object>> defectTotalList = new ArrayList<>();
		List<String> defectTotalChartsList = new ArrayList<>();
		for(int i = 0;i < 12;i++) {
			String curDateStr = time +"-"+ (i<9?"0"+(i+1):(i+1)) + "-26";
			String startDate = i == 0?(time+"-01-01"):DateUtil.formatDate(DateUtil.addMonth(DateUtil.getDate(curDateStr, "yyyy-MM-dd"), -1), null);
			String endDate = time +"-"+ (i<9?"0"+(i+1):(i+1)) + "-25";
			Long startTime = DateUtil.getDate(startDate, "yyyy-MM-dd").getTime();
			if(System.currentTimeMillis() < startTime) {
				break;
			}
			List<Map<String, Object>> list = defectInfoMapper.getDefectTotalByTime(startDate,endDate,oamSystem);
			if(CollectionUtil.isNotEmpty(list)) {
				list.get(0).put("time", time+"-"+(i+1));
				String defectPro = list.get(0).get("defectPro").toString();
				String avgRepair = list.get(0).get("avgRepair").toString();
				if(Double.valueOf(defectPro) != 0d) {
					list.get(0).put("defectPro", String.format("%.2f", Double.valueOf(defectPro))+"%");
				}else {
					list.get(0).put("defectPro", "0%");
				}
				if(Double.valueOf(avgRepair) != 0d) {
					list.get(0).put("avgRepair", String.format("%.2f", Double.valueOf(avgRepair))+"%");
				}else {
					list.get(0).put("avgRepair", "0");
				}
				defectTotalList.add(list.get(0));
				defectTotalChartsList.add(list.get(0).get("defectPro").toString().replace("%", ""));
			}	
		}
		String origin = request.getHeader("Host");
		map.put("defectTotalList", defectTotalList);
		map.put("defectTotalChartsList", defectTotalChartsList);
		map.put("url", "http://"+origin+"/testManageui/testReport/toDefectTotalReportSample?time="+time);
		return map;
	}

	/**
	 * 获取缺陷等级统计
	 */
	@Override
	public Map<String, Object> getDefectLevelReport(String time, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		newSystem = tblSystemInfoMapper.selectSystemByClass(1);
		oamSystem = tblSystemInfoMapper.selectSystemByClass(2);
		String startDate = time.split(" ~ ")[0];
		String endDate = time.split(" ~ ")[1];
		List<Map<String, Object>> defectLevelList = defectInfoMapper.selectDefectLevel(startDate, endDate,oamSystem+","+newSystem);
		Map<String, Object> severityLevelMap = JSON
				.parseObject(redisUtils.get("TBL_DEFECT_INFO_SEVERITY_LEVEL").toString(), Map.class);
		Map<String, Object> defectLevelChartsMap = getDefectLevelMap(defectLevelList, severityLevelMap);
		String origin = request.getHeader("Host");
		map.put("defectLevelList", defectLevelList);
		map.put("defectLevelChartsMap", defectLevelChartsMap);
		map.put("url", "http://"+origin+"/testManageui/testReport/toDefectLevelReportSample?time="+time);
		return map;
	}

	/**
	 * 获取所有系统
	 */
	@Override
	public Map<String, Object> getSystemInfo(String systemName, String systemCode, Integer pageNumber,
			Integer pageSize, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		PageHelper.startPage(pageNumber, pageSize);
		List<TblSystemInfo> systemInfos = tblSystemInfoMapper.selectSystemByOamAndNew(systemName, systemCode);
		PageInfo<TblSystemInfo> pageInfo = new PageInfo<>(systemInfos);
		map.put("rows", pageInfo.getList());
		map.put("total", pageInfo.getTotal());
		return map;
	}
	/**
	 * 获取运维期或者项目期下的非敏捷系统
	 */
	@Override
	public Map<String, Object> getSystemInfoByAgile(String systemName, String systemCode, Integer pageNumber,
											 Integer pageSize, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		PageHelper.startPage(pageNumber, pageSize);
		List<TblSystemInfo> systemInfos = tblSystemInfoMapper.selectSystemByAgile(systemName, systemCode);
		PageInfo<TblSystemInfo> pageInfo = new PageInfo<>(systemInfos);
		map.put("rows", pageInfo.getList());
		map.put("total", pageInfo.getTotal());
		return map;
	}

	/**
	 * 获取运维期和新建类系统
	 * 增加敏捷类项目获取
	 */
	@Override
	public Map<String, Object> getOamAndNewSystem() {
		Map<String, Object> map = new HashMap<>();
		List<TblSystemInfo> newSystems = tblSystemInfoMapper.selectSystemBySystemClass(1);//运维期
		List<TblSystemInfo> oamSystems = tblSystemInfoMapper.selectSystemBySystemClass(2);//项目期
		List<TblSystemInfo> agileSystem = tblSystemInfoMapper.selectSystemByDevelopmentMode(1);//敏捷
		map.put("oamSystem", oamSystems);
		map.put("newSystem", newSystems);
		map.put("agileSystem", agileSystem);
		return map;
	}

	/**
	 * 添加系统类型
	 */
	@Override
	@Transactional(readOnly = false)
	public void addSystemClass(String systemIdStr, Integer systemClass, HttpServletRequest request) {
		if (systemClass != null && systemClass == 3) {//增加敏捷
			tblSystemInfoMapper.batchUpdateDevelopmentMode(systemIdStr, 1);
		} else if (systemClass != null && systemClass == -1) {//移除敏捷
			tblSystemInfoMapper.batchUpdateDevelopmentMode(systemIdStr, 2);
		} else {
			tblSystemInfoMapper.batchUpdateSystemClass(systemIdStr, systemClass);
		}

	}
}
