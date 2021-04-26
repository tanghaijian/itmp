package cn.pioneeruniverse.dev.service.testCaseManage.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.dev.common.ExportExcel;
import cn.pioneeruniverse.dev.entity.*;
import cn.pioneeruniverse.dev.vo.ArchivedCaseVO;
import cn.pioneeruniverse.dev.yiranUtil.PoiExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import cn.pioneeruniverse.common.bean.MergedRegionResult;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.utils.ExcelUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.TblArchivedCaseMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblArchivedCaseStepMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblCaseCatalogMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblCaseInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblCaseStepMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemInfoMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblUserInfoMapper;
import cn.pioneeruniverse.dev.service.testCaseManage.TestCaseManageService;
import sun.misc.BASE64Encoder;

@Service
public class TestCaseManageServiceImpl implements TestCaseManageService {
	
	@Autowired
	private TblCaseInfoMapper tblCaseInfoMapper;
	
	@Autowired
	private TblSystemInfoMapper tblSystemInfoMapper;
	
	@Autowired
	private TblCaseCatalogMapper tblCaseCatalogMapper;
	
	@Autowired
	private TblUserInfoMapper tblUserInfoMapper;
	
	@Autowired
	private TblCaseStepMapper tblCaseStepMapper;
	
	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private TblArchivedCaseMapper tblArchivedCaseMapper;
	
	@Autowired
	private TblArchivedCaseStepMapper tblArchivedCaseStepMapper;
	
	@Autowired
	private TblSystemInfoMapper systemInfoMapper;
	//测试案例列表显示
	@Override
	@Transactional(readOnly = true)
	public List<TblCaseInfo> getCaselist(TblCaseInfo tblCaseInfo, String filters, Integer page, Integer rows, HttpServletRequest request) {
		Map<String,Object> configMap = new HashMap<>();
		if(filters != null&&!filters.equals("")) {
			Map filter = JSON.parseObject(filters, Map.class);
			List<Map<String, Object>> ruleList = JSON.parseObject(filter.get("rules").toString(), List.class);
			for (Map<String, Object> map2 : ruleList) {
				String key = map2.get("field").toString();
				String value = map2.get("data").toString().trim();
				if(key.equals("caseType")) {
					if(value.equals("1")) {
						configMap.put("type", 1);
					}
					if(value.equals("2")) {
						configMap.put("type", 2);
					}
				}
				if(key.equals("archiveStatus")) {
					if(value.equals("1")) {
						configMap.put("aStatus", 1);
					}
					if(value.equals("2")) {
						configMap.put("aStatus", 2);
					}
				}
				if(key.equals("systemName")) {
					List<Long> systemIds = tblSystemInfoMapper.getSystemIdBySystemName(value);
					configMap.put("systemIds", systemIds);
				}
				if(key.equals("userName")) {
					List<Long> userIds = tblUserInfoMapper.getUserIdByUserName(value);
					configMap.put("userIds", userIds);
				}
				configMap.put(key, value);
			}
		}
		Integer start = (page-1)*rows;
		if(tblCaseInfo.getCaseCatalogId() != null) {
			TblCaseCatalog caseCatalog = tblCaseCatalogMapper.selectByPrimaryKey(Long.valueOf(tblCaseInfo.getCaseCatalogId().toString()));
			tblCaseInfo.setSystemId(Integer.valueOf(caseCatalog.getSystemId().toString()));
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("tblCaseInfo", tblCaseInfo);
		map.put("filter", configMap);
		map.put("start", start);
		map.put("rows", rows);

		LinkedHashMap usermap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
		List<String> roleCodes = (List<String>) usermap.get("roles");
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")){ //系统管理员
			List<TblCaseInfo> list = tblCaseInfoMapper.getCaseInfos(map);
			return list;
		}else{
			map.put("uid",CommonUtil.getCurrentUserId(request));
			List<TblCaseInfo> list = tblCaseInfoMapper.getCaseInfoCondition(map);
			return list;
		}

	}

	//新增案例涉及系统的下拉框
	@Override
	@Transactional(readOnly = true)
	public List<TblSystemInfo> getAllSystem(String systemName) {
		// TODO Auto-generated method stub
		if(systemName == null) {
			return tblSystemInfoMapper.getSystems();
		}else {
			return tblSystemInfoMapper.getSystemBySystemName(systemName);
		}
	}

	//新增案例
	@Override
	@Transactional(readOnly = false)
	public TblCaseInfo insertCaseInfo(TblCaseInfo tblCaseInfo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		if(tblCaseInfo.getCaseCatalogId() == null) {
			tblCaseInfo.setCaseCatalogId(1);
		}
		tblCaseInfo.setStatus(1);  //状态
		tblCaseInfo.setArchiveStatus(1); //归档状态
		tblCaseInfo.setCreateDate(new Timestamp(new Date().getTime()));//创建时间
		tblCaseInfo.setCreateBy(CommonUtil.getCurrentUserId(request));//创建人
		tblCaseInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblCaseInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tblCaseInfo.setCaseNumber(getCaseNumber());  //案例编号
		tblCaseInfoMapper.insertCaseInfo(tblCaseInfo);
		//获得这条插入数据的id
		Long id = tblCaseInfo.getId();
		List<TblCaseStep> steps = tblCaseInfo.getCaseSteps();
		if(steps != null && steps.size() != 0) {
			for (TblCaseStep tblCaseStep : steps) {
				tblCaseStep.setCaseId(id);
				tblCaseStep.setStatus(1);
				tblCaseStep.setCreateBy(CommonUtil.getCurrentUserId(request));
				tblCaseStep.setCreateDate(new Timestamp(new Date().getTime()));
				tblCaseStepMapper.insertCaseStep(tblCaseStep);
			}
		}
		return tblCaseInfo;
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
            String maxCaseNo = tblCaseInfoMapper.selectMaxCaseNumber();
            codeInt = 1;
            if (StringUtils.isNotBlank(maxCaseNo)) {
                codeInt = Integer.valueOf(maxCaseNo.substring(Constants.TMP_CASE_INFO_NUMBER.length())) + 1;
            }

        }
        featureCode = Constants.TMP_CASE_INFO_NUMBER + String.format("%08d", codeInt);
        redisUtils.set(Constants.TMP_CASE_INFO_NUMBER,String.format("%08d", codeInt) );
        return featureCode;
    }

    //案例删除
	@Override
	@Transactional(readOnly = false)
	public void deleteCaseInfo(List<Long> ids, HttpServletRequest request) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<>();
		map.put("currentUser", CommonUtil.getCurrentUserId(request));
		map.put("time", new Timestamp(new Date().getTime()));
		map.put("ids", ids);
		tblCaseInfoMapper.deleteCaseInfo(map);
	}

	//删除案例的同时删除归档案例
	@Override
	@Transactional(readOnly = false)
	public void deleteArchivedCase(List<Long> ids, HttpServletRequest request) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<>();
		map.put("currentUser", CommonUtil.getCurrentUserId(request));
		map.put("time", new Timestamp(new Date().getTime()));
		map.put("ids", ids);
		tblArchivedCaseMapper.deleteArchivedCase(map);
	}
	
	//案例详情及案例编辑数据回显
	@Override
	@Transactional(readOnly = true)
	public TblCaseInfo selectCaseInfoById(Long id) {
		// TODO Auto-generated method stub
		TblCaseInfo tblCaseInfo = tblCaseInfoMapper.findCaseInfoById(id);
		Long createById = tblCaseInfo.getCreateBy();
		Long lastUpdateById = tblCaseInfo.getLastUpdateBy();
		if(createById != null) {
			String creatUser = tblUserInfoMapper.getUserNameById(createById);
			tblCaseInfo.setUserName(creatUser);
		}
		if(lastUpdateById != null) {
			String lastUpdateUser = tblUserInfoMapper.getUserNameById(lastUpdateById);
			tblCaseInfo.setLastUpdateUser(lastUpdateUser);
		}
		tblCaseInfo.setCreateTime(tblCaseInfo.getCreateDate());
		tblCaseInfo.setLastUpdateTime(tblCaseInfo.getLastUpdateDate());
		Long systemId = tblCaseInfo.getSystemId().longValue();
		if(systemId != null) {
			String systemName = tblSystemInfoMapper.getSystemNameById(systemId);
			tblCaseInfo.setSystemName(systemName);
		}
		List<TblCaseStep> list = tblCaseStepMapper.findCaseStepByCaseId(id);
		if(list.size() != 0) {
			tblCaseInfo.setCaseSteps(list);
		}
		return tblCaseInfo;
	}
	
	
	/**
	 * 查询要导出的案例和案例步骤
	 */
	@Override
	@Transactional(readOnly = false)
	public List<TblCaseInfo> getCaseAndSteps(TblCaseInfo tblCaseInfo, String filters) {
		// TODO Auto-generated method stub
		Map<String,Object> configMap = new HashMap<>();
		if(filters != null&&!filters.equals("")) {
			Map filter = JSON.parseObject(filters, Map.class);
			List<Map<String, Object>> ruleList = JSON.parseObject(filter.get("rules").toString(), List.class);
			for (Map<String, Object> map2 : ruleList) {
				String key = map2.get("field").toString();
				Object value = map2.get("data");
				if(key.equals("caseType")) {
					if(value.equals("1")) {
						configMap.put("type", 1);
					}
					if(value.equals("2")) {
						configMap.put("type", 2);
					}
				}
				if(key.equals("archiveStatus")) {
					if(value.equals("未归档")) {
						configMap.put("type", 1);
					}
					if(value.equals("已归档")) {
						configMap.put("type", 2);
					}
				}
				if(key.equals("systemName")) {
					List<Long> systemIds = tblSystemInfoMapper.getSystemIdBySystemName(value.toString());
					configMap.put("systemIds", systemIds);
				}
				if(key.equals("userName")) {
					List<Long> userIds = tblUserInfoMapper.getUserIdByUserName(value.toString());
					configMap.put("userIds", userIds);
				}
				configMap.put(key, value);
			}
		}
//		List<Long> uIds = null;
//		if(tblCaseInfo.getUserName() != null&&!tblCaseInfo.getUserName().equals("")) {
//			uIds = tblUserInfoMapper.getUserIdByUserName(tblCaseInfo.getUserName());
//		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("tblCaseInfo", tblCaseInfo);
//		map.put("uIds", uIds);
		map.put("filter", configMap);
		map.put("start", 0);
		map.put("rows", Integer.MAX_VALUE);
		List<TblCaseInfo> list = tblCaseInfoMapper.getCaseInfos(map);
		for (TblCaseInfo caseInfo : list) {
			Integer caseType = caseInfo.getCaseType();
			if(caseType != null && Integer.valueOf(caseType) == 1) {
				caseInfo.setType("正面");
			}
			if(caseType != null && Integer.valueOf(caseType) == 2) {
				caseInfo.setType("反面");
			}
			List<TblCaseStep> caseStepList = tblCaseStepMapper.findCaseStepByCaseId(caseInfo.getId());
			caseInfo.setCaseSteps(caseStepList);
		}
		return list;
	}


	/**
	 * 测试案例导出 2020 - 06 - 28
	 * @param list
	 * @param request
	 * @param response
	 * @throws Exception
	 * wu cheng
	 */
	@Override
	public void exportExcel(List<TblCaseInfo> list, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		for (int i = 0; i < list.size(); i++){
			list.get(i).setCaseNumber(String.valueOf(i+1));
		}
		new ExportExcel()
				.setWorkHead(TblCaseInfo.class, TblCaseStep.class, "测试案例管理表", 7, 9, "案例步骤")
				.setDataListWithList(list).write(response,
				"案例管理" + new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒").format(System.currentTimeMillis())+".xlsx");
//				"案例管理" + new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒").format(System.currentTimeMillis())+".xlsx").dispose();

	}
	
	 public static void exportExcel(String []title,HSSFSheet sheet,String filename,String [][]values,
			HSSFWorkbook workbook,Integer headRowNum,HSSFCellStyle headStyle,HSSFCellStyle valueStyle,
			HttpServletRequest request, HttpServletResponse response) throws  Exception{

		// 创建头部
		HSSFCell cell = null;

		headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());// 设置背景色
		headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

		HSSFRow row = sheet.createRow(headRowNum);
		for(int i=0;i<title.length;i++){
			cell = row.createCell(i);
			row.setHeightInPoints(25);
			cell.setCellValue(title[i]);
			cell.setCellStyle(headStyle);
			int colWidth = sheet.getColumnWidth(i)*2;
			sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
		}

		valueStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 纯色使用前景颜色填充
		valueStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		valueStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		valueStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		valueStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		valueStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		valueStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		valueStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		valueStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		valueStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

		//创建内容
		for(int i=0;i<values.length;i++){
			row = sheet.createRow(i + 1);
			for(int j=0;j<values[i].length;j++){
				//将内容按顺序赋给对应的列对象
				row.createCell(j).setCellValue(values[i][j]);
				row.getCell(j).setCellStyle(valueStyle);
			}
		}

		//导出 也就是下载功能， 使用输出流
		String useragent = request.getHeader("User-Agent");
		if (useragent.contains("Firefox")) {
			filename = "=?UTF-8?B?" + new BASE64Encoder().encode(filename.getBytes("utf-8")) + "?=";
		} else {
			filename = URLEncoder.encode(filename, "utf-8");
			filename = filename.replace("+", " ");
		}
		OutputStream out = response.getOutputStream();

		//导出  输出流 设置下载头信息 Content-Disposition 设置mime类型
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename="+filename);
		workbook.write(out);
		out.flush();
		out.close();
	 }
	 
	  // 自适应宽度(中文支持)
//	    private static void setSizeColumn(HSSFSheet sheet, int size) {
//	        for (int columnNum = 0; columnNum < size; columnNum++) {
//	            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
//	            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
//	                HSSFRow currentRow;
//	                //当前行未被使用过
//	                if (sheet.getRow(rowNum) == null) {
//	                    currentRow = sheet.createRow(rowNum);
//	                } else {
//	                    currentRow = sheet.getRow(rowNum);
//	                }
//	                if (currentRow.getCell(columnNum) != null) {
//	                    HSSFCell currentCell = currentRow.getCell(columnNum);
//	                    if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
//	                        int length = currentCell.getStringCellValue().getBytes().length;
//	                        if (columnWidth < length) {
//	                            columnWidth = length;
//	                        }
//	                    }
//	                }
//	            }
//	            sheet.setColumnWidth(columnNum, columnWidth * 256);
//	        }
//	    }


	//编辑案例
//	@Override
//	@Transactional(readOnly = false)
//	public void editCaseInfo(TblCaseInfo caseInfo, HttpServletRequest request) {
//		// TODO Auto-generated method stub
//		Long id = caseInfo.getId();
//		//根据caseId把之前的所有步骤解绑
//		HashMap<String, Object> map = new HashMap<>();
//		Long userId = CommonUtil.getCurrentUserId(request);
//		Timestamp time = new Timestamp(new Date().getTime());
//		map.put("id", id);
//		map.put("userId", userId);
//		map.put("time", time);
//		tblCaseStepMapper.updateCaseStepByCaseId(map);
//		caseInfo.setLastUpdateBy(userId);
//		caseInfo.setLastUpdateDate(time);
//		tblCaseInfoMapper.updateCaseInfo(caseInfo);
//		List<TblCaseStep> steps = caseInfo.getCaseSteps();
//		if(steps != null && steps.size() > 0) {
//			for (TblCaseStep tblCaseStep : steps) {
//				tblCaseStep.setCaseId(id);
//				tblCaseStep.setStatus(1);
//				tblCaseStep.setCreateBy(userId);
//				tblCaseStep.setCreateDate(time);
//				tblCaseStepMapper.insertCaseStep(tblCaseStep);
//			}
//		}
//	}
	
	//编辑案例
	@Override
	@Transactional(readOnly = false)
	public void editCaseInfo(TblCaseInfo caseInfo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Long id = caseInfo.getId();
		Long userId = CommonUtil.getCurrentUserId(request);
		Timestamp time = new Timestamp(new Date().getTime());
		caseInfo.setLastUpdateBy(userId);
		caseInfo.setLastUpdateDate(time);
		tblCaseInfoMapper.updateCaseInfo(caseInfo);
		List<Long> stepIds = tblCaseStepMapper.getStepIdsByCaseId(id);
		List<TblCaseStep> steps = caseInfo.getCaseSteps();
		List<Long> list = new ArrayList<>();
		if(steps != null && steps.size() > 0) {
			for (TblCaseStep tblCaseStep : steps) {
				if(tblCaseStep.getId()==null) {  //新增
					tblCaseStep.setCaseId(id);
					tblCaseStep.setStatus(1);
					tblCaseStep.setCreateBy(userId);
					tblCaseStep.setCreateDate(time);
					tblCaseStepMapper.insertCaseStep(tblCaseStep);
				}else {          //修改
					Long stepId = tblCaseStep.getId();
					tblCaseStep.setLastUpdateBy(userId);
					tblCaseStep.setLastUpdateDate(time);
					tblCaseStepMapper.updateStep(tblCaseStep);
					list.add(stepId);
				}
			}
			List<Long> listAll = new ArrayList<>();
			List<Long> resultList= new ArrayList<>();
			listAll.addAll(stepIds);
			listAll.addAll(list);
			for (int i = 0; i < listAll.size(); i++) {
				if(stepIds.contains(listAll.get(i)) && list.contains(listAll.get(i))){
					continue;
				}else{
					resultList.add(listAll.get(i));
				}
			}
			if(resultList.size() != 0) {
				Map<String, Object> map = new HashMap<>();
				map.put("userId", userId);
				map.put("time", time);
				map.put("resultList", resultList);
				tblCaseStepMapper.deleteCaseSteps(map);
			}
		}else {
			tblCaseStepMapper.deleteCaseStep(id);
		}
	}
	
	//编辑案例的同时也更新归档案例
//	@Override
//	@Transactional(readOnly = false)
//	public void editArchivedCase(TblCaseInfo caseInfo, HttpServletRequest request) {
//		// TODO Auto-generated method stub
//		Long id = caseInfo.getId();
//		HashMap<String, Object> map = new HashMap<>();
//		Long userId = CommonUtil.getCurrentUserId(request);
//		Timestamp time = new Timestamp(new Date().getTime());
//		map.put("id", id);
//		map.put("userId", userId);
//		map.put("time", time);
//		tblArchivedCaseStepMapper.updateArchivedCaseStepByCseId(map);
//		caseInfo.setLastUpdateBy(userId);
//		caseInfo.setLastUpdateDate(time);
//		tblArchivedCaseMapper.updateArchivedCase(caseInfo);
//		List<TblCaseStep> steps = caseInfo.getCaseSteps();
//		if(steps != null && steps.size() > 0) {
//			for (TblCaseStep tblCaseStep : steps) {
//				tblCaseStep.setCaseId(id);
//				tblCaseStep.setStatus(1);
//				tblCaseStep.setCreateBy(userId);
//				tblCaseStep.setCreateDate(time);
//				tblArchivedCaseStepMapper.insertArchivedCaseStep(tblCaseStep);
//			}
//		}
//		
//	}

	//接口
	@Override
	@Transactional(readOnly = true)
	public HashMap<String, Object> getCaseInfo(Long testSetId,TblCaseInfo caseInfo, Integer page, Integer rows) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<>();
		PageHelper.startPage(page, rows);
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("testSetId",testSetId);
		paramMap.put("caseInfo",caseInfo);
		
		List<Map<String, Object>> list = tblCaseInfoMapper.getCaseInfo(paramMap);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
	}
	
	//接口
	@Override
	@Transactional(readOnly = true)
	public Long getIdByCaseNumber(String caseNumber) {
		// TODO Auto-generated method stub
		return tblCaseInfoMapper.getIdByCaseNumber(caseNumber);
	}


	/**
	 * 获取导入数据单元格值
	 *  2020-06-28
	 * @param caseCatalog
	 * @param caseCatalogId
	 * @param i
	 * @param startRow
	 * @param endRow
	 * @param request
	 * @param sheet
	 * @param caseInfo
	 * @return
	 */
	private Map getCellEstimate(TblCaseCatalog caseCatalog, Integer caseCatalogId,int i, int startRow, int endRow, HttpServletRequest request, Sheet sheet, TblCaseInfo caseInfo){
		Map<String, Object> mapValue = new HashMap<>(16);
		if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 0).getValue())){
			mapValue.put("flag",true);
			mapValue.put("errorMessage","导入模板第"+(i+1)+"行案例序号为空");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return mapValue;
		}
		if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 4).getValue())){
			mapValue.put("flag",true);
			mapValue.put("errorMessage","导入模板第"+(i+1)+"行案例名称为空");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return mapValue;
		}

		//案例编号
		caseInfo.setCaseNumber(getCaseNumber());
		//案例名称
		caseInfo.setCaseName(PoiExcelUtil.getMergedRegionValue(sheet, i, 4).getValue());
		//案例描述
//		caseInfo.setCaseDescription(PoiExcelUtil.getMergedRegionValue(sheet, i, 11).getValue());
		//系统id
		caseInfo.setSystemId(Integer.valueOf(caseCatalog.getSystemId().toString()));
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
		//案例类型
		caseInfo.setCaseType(1);
		//归档状态
		caseInfo.setArchiveStatus(1);
		//案例目录id
		caseInfo.setCaseCatalogId(caseCatalogId);
		//状态
		caseInfo.setStatus(1);
		//创建者
		caseInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
		//创建时间
		caseInfo.setCreateDate(new Timestamp(new Date().getTime()));
		tblCaseInfoMapper.insertCaseInfo(caseInfo);
		long caseId = caseInfo.getId();
		int count =0;
		for (int j = startRow; j <= endRow; j++) {
			if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 7).getValue()) && StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 8).getValue()) && StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 9).getValue())){
				continue;
			}

			/**
			 * 案例步骤
			 */
			TblCaseStep caseStep = new TblCaseStep();
			caseStep.setCaseId(caseId);
			//步骤排序号
			count = count + 1;
			caseStep.setStepOrder(count);
//			caseStep.setStepOrder(Integer.valueOf(PoiExcelUtil.getMergedRegionValue(sheet, j, 7).getValue()));
			//步骤描述
			caseStep.setStepDescription(PoiExcelUtil.getMergedRegionValue(sheet, j, 8).getValue());
			//步骤预期结果
			caseStep.setStepExpectedResult(PoiExcelUtil.getMergedRegionValue(sheet, j, 9).getValue());
			caseStep.setCreateBy(CommonUtil.getCurrentUserId(request));
			caseStep.setCreateDate(new Timestamp(new Date().getTime()));
			caseStep.setStatus(1);
			tblCaseStepMapper.insertCaseStep(caseStep);
		}
		mapValue.put("flag",false);
		return mapValue;
	}


	/**
	 * 测试案例管理导入
	 * 2020-06-28
	 * @param file
	 * @param caseCatalogId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> importExcel(MultipartFile file,Integer caseCatalogId, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>(16);
		TblCaseCatalog caseCatalog = tblCaseCatalogMapper.selectByPrimaryKey(Long.valueOf(caseCatalogId.toString()));
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
//			String[] title = {"案例序号", "业务类型", "模块", "测试项", "案例名称",
//					"前置条件", "输入数据", "案例步骤", "预期结果", "实际结果", "执行结果"，"案例描述"};
			String[] title = {"案例序号", "业务类型", "模块", "测试项", "案例描述",
					"前置条件", "输入数据", "案例步骤", "预期结果"};
			for (int i = 0; i < title.length -1; i++) {
				if (!titleRow.getCell(i).getStringCellValue().equals(title[i])) {
					map.put("status", Constants.ITMP_RETURN_FAILURE);
					map.put("errorMessage", "第"+(i+1)+"列标题未对应");
					return map;
				}
			}
			int caseCount = 0;
			int lastRowNum = sheet.getLastRowNum();
			if (lastRowNum == 1){
				map.put("status", Constants.ITMP_RETURN_FAILURE);
				map.put("errorMessage", "未填写导入数据!");
				return map;
			}

			//循环表格行
			for (int i = 2; i <= lastRowNum; i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				for (int j = 0; j <= lastRowNum; j++) {
					if(row.getCell(j) != null) {
						row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
					}else {
						row.createCell(j).setCellValue("");
					}
				}
				TblCaseInfo caseInfo = new TblCaseInfo();
				int startRow = i;
				int endRow = i;
				//单元格是否为合并单元格
				MergedRegionResult result = ExcelUtil.isMergedRegion(sheet, i, 0);
				if (result.isMerged()){
					//合并单元格数据处理
					startRow = result.getStartRow();
					endRow = result.getEndRow();
					if (PoiExcelUtil.rowContains(i,endRow)){
						Map cellEstimate = getCellEstimate(caseCatalog, caseCatalogId, i, startRow, endRow, request, sheet, caseInfo);
						if (Boolean.parseBoolean(cellEstimate.get("flag").toString())){
							cellEstimate.put("status", Constants.ITMP_RETURN_FAILURE);
							return cellEstimate;
						}
						caseCount++;
					}
				}else{
					//非合并单元格数据处理
					startRow = i;
					endRow = i;
					Map cellEstimate = getCellEstimate(caseCatalog, caseCatalogId, i, startRow, endRow, request, sheet, caseInfo);
					if (Boolean.parseBoolean(cellEstimate.get("flag").toString())){
						cellEstimate.put("status", Constants.ITMP_RETURN_FAILURE);
						return cellEstimate;
					}
					caseCount++;
				}
			}
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
			map.put("caseCount", caseCount);
			return map;
		} catch (Exception e) {
			throw e;
		}
	}


	/**
	 * 测试案例管理导出模板
	 * 2020-06-28
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Override
	public void exportTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new ExportExcel()
				.setWorkHead(ArchivedCaseVO.class, CaseStepVo.class, "案例管理", 7, 9, "案例步骤")
				.setDataListWithList(new ArrayList<>()).write(response,
				"测试案例管理模板.xlsx");
//				"测试案例管理模板.xlsx").dispose();
	}
			

	//归档案例
	@Override
	@Transactional(readOnly = false)
	public void archivingCase(List<String> ids, HttpServletRequest request) {
		// TODO Auto-generated method stub
		//修改归档状态
		HashMap<String, Object> map = new HashMap<>();
		map.put("ids", ids);
		map.put("userId", CommonUtil.getCurrentUserId(request));
		map.put("time", new Timestamp(new Date().getTime()));
		tblCaseInfoMapper.updateArchiveStatus(map);
		//查询出需要归档的案例和案例步骤
		List<TblCaseStep> caseStepList = new ArrayList<>();
		List<TblCaseInfo> caseList = tblCaseInfoMapper.getCaseByIds(ids);
		//把查询出来的案例和案例步骤同步到归档案例和归档案例步骤表中
		if(caseList != null && caseList.size() != 0) {
			for (TblCaseInfo tblCaseInfo : caseList) {
				tblArchivedCaseMapper.archivingCase(tblCaseInfo);
				for( TblCaseStep tblCaseStep:tblCaseInfo.getCaseSteps()) {
					tblCaseStep.setCaseId(tblCaseInfo.getId());
					caseStepList.add(tblCaseStep);
				}
			}
		}
		if(caseStepList != null && caseStepList.size() != 0) {
			for (TblCaseStep tblCaseStep : caseStepList) {
				tblArchivedCaseStepMapper.archivingCaseStep(tblCaseStep);
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<String> getArchivedCaseIds() {
		// TODO Auto-generated method stub
		return tblArchivedCaseMapper.getArchivedCaseIds();
		
	}

	//接口
	@Override
	@Transactional(readOnly=false)
	public void updateCaseStep(String testSetCase, String testCaseStep, Long testUserId) throws Exception {
		try {
			Map<String, Object> result=null;
			Map<String, Object> map = new HashMap<>();
			result = (Map)JSON.parse(testSetCase);
			List<Map<String,Object>> listObjectSec = JSONArray.parseObject(testCaseStep,List.class);

			result.put("testUserId",testUserId);
			Long id = tblCaseInfoMapper.getIdByCaseNumber(result.get("caseNumber").toString());
			result.put("cid", id);
			tblCaseInfoMapper.updateCase(result);
			tblCaseStepMapper.deleteCaseStep(id);
			map.put("list", listObjectSec);
			map.put("caseId", id);
			map.put("createBy",testUserId);
			tblCaseStepMapper.insertStep(map);

			}catch (Exception e) {
				throw e;
			}
	}
	
	/**
	 * 获取案例树
	 */
	@Override
	public Map<String, Object> getCaseTreeBySystemId(Long systemId,HttpServletRequest request) {
		TblCaseCatalog tblCaseCatalog = new TblCaseCatalog();
		tblCaseCatalog.setSystemId(systemId);
		LinkedHashMap usermap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
		List<String> roleCodes = (List<String>) usermap.get("roles");
		if (roleCodes==null || !roleCodes.contains("XITONGGUANLIYUAN")){ //系统管理员
			tblCaseCatalog.setUid(CommonUtil.getCurrentUserId(request));
		}
		List<Map<String,Object>> znodes = tblCaseCatalogMapper.selectCaseCatalogsByCon(tblCaseCatalog);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("znodes", znodes);
		return map;
	}

	/**
	 * 新增或修改案例目录
	 * @param tblCaseCatalog
	 */
	@Override
	public Map<String,Object> addOrUpdateCaseCatalog(TblCaseCatalog tblCaseCatalog,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Long id = tblCaseCatalog.getId();
		if(id == null) {//TODO 更新的基本信息需要处理
			CommonUtil.setBaseValue(tblCaseCatalog, request);
			tblCaseCatalogMapper.insert(tblCaseCatalog);
		}else {
			tblCaseCatalogMapper.updateByPrimaryKeySelective(tblCaseCatalog);
		}
		map.put("tblCaseCatalog", tblCaseCatalog);
		return map;
	}

	@Override
	public List<TblCaseCatalog> getCatalogBySystemId(Long systemId) {
		return tblCaseCatalogMapper.selectCaseCatalogsBySystemId(systemId);
	}


	
}
