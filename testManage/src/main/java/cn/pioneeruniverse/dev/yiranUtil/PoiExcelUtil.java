package cn.pioneeruniverse.dev.yiranUtil;

import cn.pioneeruniverse.common.bean.MergedRegionResult;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.dev.vo.ExportParameters;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wucheng
 * @date 2020/6/19
 */
public class PoiExcelUtil {

    /**
     *  导入数据为空处理
     * @param map
     * @return
     */
    public static Map rowEmptyProcessing(Map map, int lastRowNum){
        if (lastRowNum == 1){
            map.put("status", Constants.ITMP_RETURN_FAILURE);
            map.put("errorMessage", "未填写导入数据!");
            return map;
        }
        return map;
    }

    /**
     * excel导出文件名
     * @param ep
     * @return
     */
    public static String getExcelFileName(ExportParameters ep){
        String fileName = "";
        if (StringUtils.isNotBlank(ep.getSystemName())){
            fileName = fileName + ep.getSystemName() +"/";
        }

        if (ep.getTestStage() != null && ep.getTestStage() == 1){
            fileName = fileName +"系测/";
        }else if (ep.getTestStage() != null && ep.getTestStage() == 2){
            fileName = fileName +"版测/";
        }

        if (StringUtils.isNotBlank(ep.getRequirementCode())){
            fileName = fileName + ep.getRequirementCode() +"/";
        }

        if (StringUtils.isNotBlank(ep.getTestTaskName())){
            fileName = fileName + ep.getTestTaskName();
        }

        fileName.replace("/","_");
        return fileName+".xlsx";
    }


    /**
     *
     * @param rowIndex  行下标
     * @param endRow 合并单元行结束下标
     * @return
     */
    public static boolean rowContains(int rowIndex, int endRow){
        if(rowIndex == endRow){
            return true;
        }
        return false;
    }

    /**
     * 获取单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static MergedRegionResult getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            Row fRow = sheet.getRow(firstRow);
            Cell fCell = fRow.getCell(firstColumn);
            String value = "";
            if (fCell != null) {
                fCell.setCellType(Cell.CELL_TYPE_STRING);
                value = fCell.getStringCellValue();
            }
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return new MergedRegionResult(true, firstRow, lastRow, firstColumn, lastColumn, value);
                }
            }
        }
        //处理非合并单元格数值为数字错误问题
        Row fRow1 = sheet.getRow(row);
        Cell fCell = fRow1.getCell(column);
        if (fCell != null) {
            fCell.setCellType(Cell.CELL_TYPE_STRING);
        }
        String str = fRow1.getCell(column) == null ? null : fRow1.getCell(column).getStringCellValue();
        return new MergedRegionResult(false, 0, 0, 0, 0, str);
    }

    /**
     *
     * @param sheet
     * @param i
     * @return
     */
    public static Map getCellType(Sheet sheet, int i) {
        Map<String, Object> mapValue = new HashMap<>(16);
//        if (StringUtils.isBlank(getMergedRegionValue(sheet, i, 1).getValue())) {
//            mapValue.put("flag", true);
//            mapValue.put("errorMessage", "导入模板第" + (i + 1) + "行业务类型为空");
//            return mapValue;
//        }
//        if (StringUtils.isBlank(getMergedRegionValue(sheet, i, 2).getValue())) {
//            mapValue.put("flag", true);
//            mapValue.put("errorMessage", "导入模板第" + (i + 1) + "行模块为空");
//            return mapValue;
//        }
//        if (StringUtils.isBlank(getMergedRegionValue(sheet, i, 3).getValue())) {
//            mapValue.put("flag", true);
//            mapValue.put("errorMessage", "导入模板第" + (i + 1) + "行测试项为空");
//            return mapValue;
//        }
//        if (StringUtils.isBlank(getMergedRegionValue(sheet, i, 5).getValue())) {
//            mapValue.put("flag", true);
//            mapValue.put("errorMessage", "导入模板第" + (i + 1) + "行前置条件为空");
//            return mapValue;
//        }
//        if (StringUtils.isBlank(getMergedRegionValue(sheet, i, 6).getValue())) {
//            mapValue.put("flag", true);
//            mapValue.put("errorMessage", "导入模板第" + (i + 1) + "行输入数据为空");
//            return mapValue;
//        }
//        if (StringUtils.isBlank(getMergedRegionValue(sheet, i, 10).getValue())) {
//			mapValue.put("flag",true);
//			mapValue.put("errorMessage","导入模板第"+(i+1)+"行预期结果为空");
//			return mapValue;
//		}
        if (StringUtils.isBlank(PoiExcelUtil.getMergedRegionValue(sheet, i, 4).getValue())) {
            mapValue.put("flag", true);
            mapValue.put("errorMessage", "导入模板第" + (i + 1) + "行案例名称为空");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return mapValue;
        }
        return mapValue;
    }





}
