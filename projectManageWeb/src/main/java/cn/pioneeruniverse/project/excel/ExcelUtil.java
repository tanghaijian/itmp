package cn.pioneeruniverse.project.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @Author:
 * @Description: 导出工具类
 * @Date: Created in 15:06 2020/08/25
 * @Modified By:
 */
public class ExcelUtil {


    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,String [][]values, HSSFWorkbook wb){   	
        if(wb == null){
            wb = new HSSFWorkbook();				// 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        }        
        HSSFSheet sheet = wb.createSheet(sheetName);// 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet        
        HSSFRow row = sheet.createRow(0);			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制         
        HSSFCellStyle style = getColumnTopStyle(wb);// 第四步，创建单元格，并设置值表头 设置表头居中      
        HSSFCellStyle style1 = getStyle(wb);		// 第四步，创建单元格，表格样式
        HSSFCell cell = null;						//声明列对象 
        for(int i=0;i<title.length;i++){	 		//创建标题
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }        
        for(int i=0;i<values.length;i++){			//创建内容
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){ 
            	HSSFCell cells = null;
            	cells=row.createCell(j);
            	cells.setCellStyle(style1);
            	cells.setCellValue(values[i][j]);	 //将内容按顺序赋给对应的列对象
            }
        }
             			
		// 让列宽随着导出的列长自动适应
		for (int colNum = 0; colNum <title.length; colNum++) {
			int columnWidth = sheet.getColumnWidth(colNum) / 256;
			for (int rowNum = 0; rowNum <=sheet.getLastRowNum(); rowNum++) {
				HSSFRow currentRow;
				// 当前行未被使用过
				if (sheet.getRow(rowNum) == null) {
					currentRow = sheet.createRow(rowNum);
				} else {
					currentRow = sheet.getRow(rowNum);
				}
				if (currentRow.getCell(colNum) != null) {
					HSSFCell currentCell = currentRow.getCell(colNum);					
					int length = currentCell.getStringCellValue().getBytes().length;
					
					if (columnWidth < length&&length<255) {
						columnWidth = length;
					}
				}
			}						
			sheet.setColumnWidth(colNum, (columnWidth+4) * 256);						
		}
        return wb;
    }   
	 
	public static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {	
		HSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑"); //微软雅黑
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度
        font.setItalic(false); //是否使用斜体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示

        HSSFCellStyle headStyle = workbook.createCellStyle();
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
        headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());// 设置背景色221,235,247
        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        headStyle.setFont(font);        
		return headStyle;
	}
    
	
	public static HSSFCellStyle getStyle(HSSFWorkbook workbook) {
		HSSFCellStyle valueStyle = workbook.createCellStyle();
        valueStyle.setWrapText(true);
		return valueStyle;
	}
	
}
