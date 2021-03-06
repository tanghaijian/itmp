package cn.pioneeruniverse.dev.service.testArchivedCase.impl;

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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

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
import cn.pioneeruniverse.dev.service.testArchivedCase.TestArchivedCaseService;
import cn.pioneeruniverse.dev.service.testCaseManage.impl.TestCaseManageServiceImpl;
import sun.misc.BASE64Encoder;

@Service
public class TestArchivedCaseServiceImpl implements TestArchivedCaseService {
	
	@Autowired
	private TblArchivedCaseMapper tblArchivedCaseMapper;
	
	@Autowired
	private TblArchivedCaseStepMapper tblArchivedCaseStepMapper;
	
	@Autowired
	private TblCaseCatalogMapper tblCaseCatalogMapper;
	
	@Autowired
	private TblSystemInfoMapper tblSystemInfoMapper;
	
	@Autowired
	private TblUserInfoMapper tblUserInfoMapper;
	
	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private TblCaseInfoMapper tblCaseInfoMapper;
	
	@Autowired
	private TblCaseStepMapper tblCaseStepMapper;

	/**
	* @author author
	* @Description ??????????????????
	* @Date 2020/9/8
	* @param tblArchivedCase
	* @param filters
	* @param page
	* @param rows
	* @param request
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblArchivedCase>
	**/
	@Override
	@Transactional(readOnly=true)
	public List<TblArchivedCase> getArchivedCases(TblArchivedCase tblArchivedCase, String filters, Integer page,
			Integer rows, HttpServletRequest request) {
		// TODO Auto-generated method stub
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
		TblCaseCatalog caseCatalog = tblCaseCatalogMapper.selectByPrimaryKey(Long.valueOf(tblArchivedCase.getCaseCatalogId().toString()));
		HashMap<String, Object> map = new HashMap<>();
		tblArchivedCase.setSystemId(Integer.valueOf(caseCatalog.getSystemId().toString()));
		map.put("tblArchivedCase", tblArchivedCase);
		map.put("filter", configMap);
		map.put("start", start);
		map.put("rows", rows);


		LinkedHashMap usermap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
		List<String> roleCodes = (List<String>) usermap.get("roles");
		if (roleCodes!=null && roleCodes.contains("XITONGGUANLIYUAN")){ //???????????????
			List<TblArchivedCase> list = tblArchivedCaseMapper.getCaseInfos(map);
			return list;
		}else{  //??????????????????
			map.put("uid",CommonUtil.getCurrentUserId(request));
			List<TblArchivedCase> list = tblArchivedCaseMapper.getCaseInfoCondition(map);
			return list;
		}

	}

	/**
	* @author author
	* @Description ????????????
	* @Date 2020/9/8
	* @param id
	* @return cn.pioneeruniverse.dev.entity.TblArchivedCase
	**/
	@Override
	@Transactional(readOnly=true)
	public TblArchivedCase getArchivedCaseById(Long id) {
		// TODO Auto-generated method stub
		TblArchivedCase tblArchivedCase = tblArchivedCaseMapper.getArchivedCaseById(id);
		Long createById = tblArchivedCase.getCreateBy();
		Long lastUpdateById = tblArchivedCase.getLastUpdateBy();
		if(createById != null) {
			String creatUser = tblUserInfoMapper.getUserNameById(createById);
			tblArchivedCase.setUserName(creatUser);
		}
		if(lastUpdateById != null) {
			String lastUpdateUser = tblUserInfoMapper.getUserNameById(lastUpdateById);
			tblArchivedCase.setLastUpdateUser(lastUpdateUser);
		}
		tblArchivedCase.setCreateTime(tblArchivedCase.getCreateDate());
		tblArchivedCase.setLastUpdateTime(tblArchivedCase.getLastUpdateDate());
		Long systemId = tblArchivedCase.getSystemId().longValue();
		if(systemId != null) {
			String systemName = tblSystemInfoMapper.getSystemNameById(systemId);
			tblArchivedCase.setSystemName(systemName);
		}
		List<TblArchivedCaseStep> list = tblArchivedCaseStepMapper.findCaseStepByCaseId(id);
		if(list.size() != 0) {
			tblArchivedCase.setCaseSteps(list);
		}
		return tblArchivedCase;
	}

	/**
	* @author author
	* @Description ??????????????????
	* @Date 2020/9/8
	* @param tblCaseInfo
	* @param request
	* @return void
	**/
	@Override
	@Transactional(readOnly=false)
	public void insertTestCase(TblCaseInfo tblCaseInfo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		if(tblCaseInfo.getCaseCatalogId() == null) {
			tblCaseInfo.setCaseCatalogId(1);
		}
		tblCaseInfo.setStatus(1);  //??????
		tblCaseInfo.setArchiveStatus(2); //????????????
		tblCaseInfo.setCreateDate(new Timestamp(new Date().getTime()));//????????????
		tblCaseInfo.setCreateBy(CommonUtil.getCurrentUserId(request));//?????????
		tblCaseInfo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
		tblCaseInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tblCaseInfo.setCaseNumber(getCaseNumber());  //????????????
		tblCaseInfoMapper.insertCaseInfo(tblCaseInfo);
		//???????????????????????????id
		Long id = tblCaseInfo.getId();
		tblCaseInfo.setId(id);
		tblArchivedCaseMapper.archivingCase(tblCaseInfo);
		List<TblCaseStep> steps = tblCaseInfo.getCaseSteps();
		if(steps != null && steps.size() != 0) {
			for (TblCaseStep tblCaseStep : steps) {
				tblCaseStep.setCaseId(id);
				tblCaseStep.setStatus(1);
				tblCaseStep.setCreateBy(CommonUtil.getCurrentUserId(request));
				tblCaseStep.setCreateDate(new Timestamp(new Date().getTime()));
				tblCaseStepMapper.insertCaseStep(tblCaseStep);
				tblCaseStep.setId(tblCaseStep.getId());
				tblArchivedCaseStepMapper.archivingCaseStep(tblCaseStep);
			}
		}
	}
	
	//????????????
    private String getCaseNumber() {
        String featureCode = "";
        Integer codeInt =0;
        Object object = redisUtils.get(Constants.TMP_CASE_INFO_NUMBER);
        if (object != null &&!"".equals( object)) {
            // redis????????????redis??????
            String code = object.toString();
            if (!StringUtils.isBlank(code)) {
                codeInt =Integer.parseInt(code)+1;
            }
        }else {
            // redis????????????????????????????????????????????????
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

    /**
    * @author author
    * @Description ??????????????????(???????????????????????????
	* ??????????????????????????????????????????????????????
	* ????????????????????????????????????????????????????????????????????????????????????????????????)
    * @Date 2020/9/8
    * @param ids
	* @param request
    * @return void
    **/
	@Override
	@Transactional(readOnly=false)
	public void removeArchivedTest(List<Long> ids, HttpServletRequest request) {
		// TODO Auto-generated method stub
		List<String> caseNumberList = tblArchivedCaseMapper.getCaseNumberByIds(ids);
		tblArchivedCaseMapper.deleteArchivedCase(ids);
		tblArchivedCaseStepMapper.deleteArchivedCaseStep(ids);
		HashMap<String, Object> map = new HashMap<>();
		map.put("updateUser", CommonUtil.getCurrentUserId(request));
		map.put("updateTime", new Timestamp(new Date().getTime()));
		map.put("ids", caseNumberList);
		tblCaseInfoMapper.updateCaseArchivedStatus(map);
	}

	/**
	* @author author
	* @Description ????????????
	* @Date 2020/9/8
	* @param request
	* @param response
	* @return void
	**/
	@Override
	public void exportTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new ExportExcel()
				.setWorkHead(ArchivedCaseVO.class, CaseStepVo.class, "????????????", 7, 9, "????????????")
				.setDataListWithList(new ArrayList<>()).write(response,
				"??????????????????.xlsx");
//				"??????????????????.xlsx").dispose();
	}

	/**
	 * ??????????????????????????????
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
			mapValue.put("errorMessage","???????????????"+(i+1)+"?????????????????????");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return mapValue;
		}
		if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 4).getValue())){
			mapValue.put("flag",true);
			mapValue.put("errorMessage","???????????????"+(i+1)+"?????????????????????");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return mapValue;
		}

		//????????????
		caseInfo.setCaseNumber(getCaseNumber());
		//????????????
		caseInfo.setCaseName(PoiExcelUtil.getMergedRegionValue(sheet, i, 4).getValue());
		//????????????
//		caseInfo.setCaseDescription(caseDescription);
		//??????
		caseInfo.setSystemId(Integer.valueOf(caseCatalog.getSystemId().toString()));
		//????????????id
		caseInfo.setCaseCatalogId(caseCatalogId);
		//????????????
		caseInfo.setCasePrecondition(PoiExcelUtil.getMergedRegionValue(sheet, i, 5).getValue());
		//????????????
		caseInfo.setInputData(PoiExcelUtil.getMergedRegionValue(sheet, i, 6).getValue());
		//?????????
		caseInfo.setTestPoint(PoiExcelUtil.getMergedRegionValue(sheet, i, 3).getValue());
		//??????
		caseInfo.setModuleName(PoiExcelUtil.getMergedRegionValue(sheet, i, 2).getValue());
		//????????????
		caseInfo.setBusinessType(PoiExcelUtil.getMergedRegionValue(sheet, i, 1).getValue());
		//????????????
		caseInfo.setExpectResult(PoiExcelUtil.getMergedRegionValue(sheet, i, 10).getValue());
		//???????????????1.????????????2.????????????
		caseInfo.setArchiveStatus(2);
		caseInfo.setStatus(1);
		caseInfo.setCreateBy(CommonUtil.getCurrentUserId(request));
		caseInfo.setCreateDate(new Timestamp(new Date().getTime()));
		tblArchivedCaseMapper.archivingCase(caseInfo);
		Long id = caseInfo.getId();
		tblCaseInfoMapper.insertCaseInfo(caseInfo);

		/**
		 * ????????????
		 */
		int count = 0;
		for (int j = startRow; j <= endRow; j++) {
			if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 7).getValue()) && StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 8).getValue()) && StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 9).getValue())){
				continue;
			}
			TblCaseStep caseStep = new TblCaseStep();
			//???????????????
			count = count + 1;
			caseStep.setStepOrder(count);
//			caseStep.setStepOrder(Integer.valueOf(PoiExcelUtil.getMergedRegionValue(sheet, j, 7).getValue()));
			//????????????
			caseStep.setStepDescription(PoiExcelUtil.getMergedRegionValue(sheet, j, 8).getValue());
			//??????????????????
			caseStep.setStepExpectedResult(PoiExcelUtil.getMergedRegionValue(sheet, j, 9).getValue());
			caseStep.setCaseId(id);
			CommonUtil.setBaseValue(caseStep, request);
			//??????????????????
			tblCaseStepMapper.insertCaseStep(caseStep);
			tblArchivedCaseStepMapper.archivingCaseStep(caseStep);
		}

		mapValue.put("flag",false);
		return mapValue;
	}


    /**
     * ?????????????????? 2020-06-28
     * @param file
     * @param caseCatalogId
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly=false)
    public Map<String, Object> importExcel(MultipartFile file,Integer caseCatalogId, HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        TblCaseCatalog caseCatalog = tblCaseCatalogMapper.selectByPrimaryKey(Long.valueOf(caseCatalogId.toString()));
        try {
            String fileName = file.getOriginalFilename();
            if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
                map.put("status", Constants.ITMP_RETURN_FAILURE);
                map.put("errorMessage", "????????????????????????");
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
                map.put("errorMessage", "???????????????");
                return map;
            }
            Row titleRow = sheet.getRow(0);
//            String[] title = { "????????????", "????????????", "????????????","????????????",
//                    "?????????","??????","????????????","????????????","????????????", "????????????", "??????????????????" };//????????????
			String[] title = {"????????????", "????????????", "??????", "?????????", "????????????",
					"????????????", "????????????", "????????????", "????????????"};
			for (int i = 0; i < title.length-1; i++) {
                if (!titleRow.getCell(i).getStringCellValue().equals(title[i])) {
                    map.put("status", Constants.ITMP_RETURN_FAILURE);
                    map.put("errorMessage", "???????????????");
                    return map;
                }
            }
            int lastRowNum = sheet.getLastRowNum();
			if (lastRowNum == 1){
				map.put("status", Constants.ITMP_RETURN_FAILURE);
				map.put("errorMessage", "?????????????????????!");
				return map;
			}
			//??????????????????
            int caseCount = 0;
			//???????????????
            for (int i = 2; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                for (int j = 0; j <= lastRowNum; j++) {
                    if(row.getCell(j) != null) {
                        row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    }
                }
                TblCaseInfo caseInfo = new TblCaseInfo();
                int startRow = i;
                int endRow = i;
				//?????????????????????????????????
				MergedRegionResult result = ExcelUtil.isMergedRegion(sheet, i, 0);
				if (result.isMerged()){
					//???????????????????????????
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
	* @author author
	* @Description ??????????????????????????????????????????
	* @Date 2020/9/8
	* @param tblArchivedCase
	* @param filters
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblArchivedCase>
	**/
	@Override
	@Transactional(readOnly = false)
	public List<TblArchivedCase> getCaseAndSteps(TblArchivedCase tblArchivedCase, String filters) {
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
		HashMap<String, Object> map = new HashMap<>();
		map.put("tblArchivedCase", tblArchivedCase);
		map.put("filter", configMap);
		map.put("start", 0);
		map.put("rows", Integer.MAX_VALUE);
		List<TblArchivedCase> list = tblArchivedCaseMapper.getCaseInfos(map);
		for (TblArchivedCase archivedCase: list) {
			Integer caseType = archivedCase.getCaseType();
			if(caseType != null && Integer.valueOf(caseType) == 1) {
				archivedCase.setType("??????");
			}
			if(caseType != null && Integer.valueOf(caseType) == 2) {
				archivedCase.setType("??????");
			}
			List<TblArchivedCaseStep> caseStepList = tblArchivedCaseStepMapper.findCaseStepByCaseId(archivedCase.getId());
			archivedCase.setCaseSteps(caseStepList);
		}
		return list;
	}


	/**
	 * ??????????????????  2020 - 06 - 28
	 * @param list
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Override
	public void exportExcel(List<TblArchivedCase> list, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		for (int i = 0; i < list.size(); i++){
			list.get(i).setCaseNumber(String.valueOf(i+1));
		}
		new ExportExcel()
				.setWorkHead(TblArchivedCase.class, TblArchivedCaseStep.class, "?????????????????????", 7, 9, "????????????")
				.setDataListWithList(list).write(response,
				"????????????" + new SimpleDateFormat("yyyy???MM???dd???HH???mm???ss???").format(System.currentTimeMillis())+".xlsx");
//				"????????????" + new SimpleDateFormat("yyyy???MM???dd???HH???mm???ss???").format(System.currentTimeMillis())+".xlsx").dispose();

	}

	
	 public static void exportExcel(String []title,HSSFSheet sheet,String filename,String [][]values,HSSFWorkbook workbook,Integer headRowNum,HSSFCellStyle headStyle,HSSFCellStyle valueStyle, HttpServletRequest request, HttpServletResponse response) throws  Exception{
	        // ????????????
	        HSSFCell cell = null;

	        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());// ???????????????
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

	        valueStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// ??????????????????????????????
	        valueStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
	        valueStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        valueStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
	        valueStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        valueStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
	        valueStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        valueStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
	        valueStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        valueStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

	        //????????????
	        for(int i=0;i<values.length;i++){
	            row = sheet.createRow(i + 1);
	            for(int j=0;j<values[i].length;j++){
	                //??????????????????????????????????????????
	                row.createCell(j).setCellValue(values[i][j]);
	                row.getCell(j).setCellStyle(valueStyle);
	            }
	        }

	        //?????? ???????????????????????? ???????????????
	        String useragent = request.getHeader("User-Agent");
	        if (useragent.contains("Firefox")) {
	            filename = "=?UTF-8?B?" + new BASE64Encoder().encode(filename.getBytes("utf-8")) + "?=";
	        } else {
	            filename = URLEncoder.encode(filename, "utf-8");
	            filename = filename.replace("+", " ");
	        }
	        OutputStream out = response.getOutputStream();

	        //??????  ????????? ????????????????????? Content-Disposition ??????mime??????
	        response.setContentType("application/vnd.ms-excel");
	        response.setHeader("Content-Disposition", "attachment;filename="+filename);
	        workbook.write(out);
	        out.flush();
	        out.close();
	    }
	 
	  // ???????????????(????????????)
//	    private static void setSizeColumn(HSSFSheet sheet, int size) {
//	        for (int columnNum = 0; columnNum < size; columnNum++) {
//	            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
//	            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
//	                HSSFRow currentRow;
//	                //????????????????????????
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

		/**
		* @author author
		* @Description ????????????
		* @Date 2020/9/8
		* @param caseInfo
		* @param request
		* @return void
		**/
		@Override
		@Transactional(readOnly = false)
		public void editCaseInfo(TblCaseInfo caseInfo, HttpServletRequest request) {
			// TODO Auto-generated method stub
			Long id = caseInfo.getId();
			//??????caseId??????????????????????????????
			HashMap<String, Object> map = new HashMap<>();
			Long userId = CommonUtil.getCurrentUserId(request);
			Timestamp time = new Timestamp(new Date().getTime());
			map.put("id", id);
			map.put("userId", userId);
			map.put("time", time);
			tblCaseStepMapper.updateCaseStepByCaseId(map);
			caseInfo.setLastUpdateBy(userId);
			caseInfo.setLastUpdateDate(time);
			tblCaseInfoMapper.updateCaseInfo(caseInfo);
			List<TblCaseStep> steps = caseInfo.getCaseSteps();
			if(steps != null && steps.size() > 0) {
				for (TblCaseStep tblCaseStep : steps) {
					tblCaseStep.setCaseId(id);
					tblCaseStep.setStatus(1);
					tblCaseStep.setCreateBy(userId);
					tblCaseStep.setCreateDate(time);
					tblCaseStepMapper.insertCaseStep(tblCaseStep);
				}
			}
		}
		
		/**
		* @author author
		* @Description ??????????????????????????????????????????
		* @Date 2020/9/8
		* @param caseInfo
		* @param request
		* @return void
		**/
		@Override
		@Transactional(readOnly = false)
		public void editArchivedCase(TblCaseInfo caseInfo, HttpServletRequest request) {
			// TODO Auto-generated method stub
			Long id = caseInfo.getId();
			HashMap<String, Object> map = new HashMap<>();
			Long userId = CommonUtil.getCurrentUserId(request);
			Timestamp time = new Timestamp(new Date().getTime());
			map.put("id", id);
			map.put("userId", userId);
			map.put("time", time);
			tblArchivedCaseStepMapper.updateArchivedCaseStepByCseId(map);
			caseInfo.setLastUpdateBy(userId);
			caseInfo.setLastUpdateDate(time);
			tblArchivedCaseMapper.updateArchivedCase(caseInfo);
			List<TblCaseStep> steps = caseInfo.getCaseSteps();
			if(steps != null && steps.size() > 0) {
				for (TblCaseStep tblCaseStep : steps) {
					tblCaseStep.setCaseId(id);
					tblCaseStep.setStatus(1);
					tblCaseStep.setCreateBy(userId);
					tblCaseStep.setCreateDate(time);
					tblArchivedCaseStepMapper.insertArchivedCaseStep(tblCaseStep);
				}
			}
			
		}

}
