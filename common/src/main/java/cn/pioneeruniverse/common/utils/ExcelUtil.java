
package cn.pioneeruniverse.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import cn.pioneeruniverse.common.bean.MergedRegionResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtil {

	private final static String excel2003L =".xls";    //2003- 版本的excel

	private final static String excel2007U =".xlsx";   //2007+ 版本的excel

	/**
	 * 导出Excel
	 * 
	 * @param sheetName sheet名称
	 * @param title     标题
	 * @param values    内容
	 * @param wb        HSSFWorkbook对象
	 * @return
	 */
	public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

		// 第一步，创建一个HSSFWorkbook，对应一个Excel文件

		if (wb == null) {

			wb = new HSSFWorkbook();

		}

		// 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet

		HSSFSheet sheet = wb.createSheet(sheetName);

		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制

		HSSFRow row = sheet.createRow(0);

		// 第四步，创建单元格，并设置值表头 设置表头居中

		HSSFCellStyle style = wb.createCellStyle();

		// style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 声明列对象

		HSSFCell cell = null;

		// 创建标题

		for (int i = 0; i < title.length; i++) {

			cell = row.createCell(i);

			cell.setCellValue(title[i]);

			cell.setCellStyle(style);

		}

		// 创建内容

		for (int i = 0; i < values.length; i++) {

			row = sheet.createRow(i + 1);

			for (int j = 0; j < values[i].length; j++) {

				// 将内容按顺序赋给对应的列对象

				row.createCell(j).setCellValue(values[i][j]);

			}

		}

		return wb;

	}


	public List<List<Object>> getBankListWithFirstRowsByExcel(InputStream in, MultipartFile file) throws Exception{
		//创建Excel工作薄
		Workbook work = this.getWorkbook(in,file);
		if(null == work){
			throw new Exception("创建Excel工作薄为空！");
		}
		List<List<Object>> list = this.getWorkListWithFirstRows(work);
		return list;
	}

	public  Workbook getWorkbook(InputStream inStr,MultipartFile file) throws Exception{

		Workbook wb = null;

		String fileName = file.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf("."));

		if(excel2003L.equals(fileType)){
			try{
				wb = new HSSFWorkbook(inStr);  //2003-
			} catch (Exception e){
				wb = new XSSFWorkbook(file.getInputStream());  //2007+
			}
		}else if(excel2007U.equals(fileType)){

			wb = new XSSFWorkbook(inStr);  //2007+

		}else{

			throw new Exception("解析的文件格式有误！");

		}

		return wb;

	}

	private List<List<Object>> getWorkListWithFirstRows(Workbook work){
		List<List<Object>> list = new ArrayList<List<Object>>();
		Sheet sheet = null;  //页数
		Row row = null;  //行数
		Cell cell = null;  //列数

		//遍历Excel中所有的sheet
		for (int i = 0; i < work.getNumberOfSheets(); i++) {
			sheet = work.getSheetAt(i);
			if(sheet==null){continue;}
			//遍历当前sheet中的所有行
			for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
				row = sheet.getRow(j);
				if(row==null){continue;}
				//遍历所有的列
				List<Object> li = new ArrayList<Object>();
				for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
					cell = row.getCell(y);
					li.add(this.getValue(cell));
				}
				list.add(li);
			}
		}
		return list;
	}

	/**
	 * 解决excel类型问题，获得数值
	 * 描述：对表格中数值进行格式化
	 * @param cell
	 * @return
	 */
	public  String getValue(Cell cell) {

		String value = "";

		if(null==cell){

			return value;

		}

		switch (cell.getCellType()) {

			//数值型

			case Cell.CELL_TYPE_NUMERIC:

				if (HSSFDateUtil.isCellDateFormatted(cell)) {

					//如果是date类型则 ，获取该cell的date值
					SimpleDateFormat format = null;
					// 验证short值
					if (cell.getCellStyle().getDataFormat() == 14) {
						format = new SimpleDateFormat("yyyy-MM-dd");
					} else if (cell.getCellStyle().getDataFormat() == 21) {
						format = new SimpleDateFormat("HH:mm:ss");
					} else if (cell.getCellStyle().getDataFormat() == 22) {
						format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					} else {
						format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					}

					Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
					value = format.format(date);

				}else {// 纯数字

					BigDecimal big=new BigDecimal(cell.getNumericCellValue());

					value = big.toString();

					//解决1234.0  去掉后面的.0

					if(null!=value&&!"".equals(value.trim())){

						String[] item = value.split("[.]");

						if(1<item.length&&"0".equals(item[1])){

							value=item[0];

						}

					}

				}

				break;

			//字符串类型

			case Cell.CELL_TYPE_STRING:

				value = cell.getStringCellValue().toString();

				break;

			// 公式类型

			case Cell.CELL_TYPE_FORMULA:

				//读公式计算值

				value = String.valueOf(cell.getNumericCellValue());

				if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串

					value = cell.getStringCellValue().toString();

				}

				break;

			// 布尔类型

			case Cell.CELL_TYPE_BOOLEAN:

				value = " "+ cell.getBooleanCellValue();

				break;

			default:

				value = cell.getStringCellValue().toString();

		}

		if("null".endsWith(value.trim())){

			value="";

		}

		return value.trim();

	}



	public static Sheet createSheet(String[] title, Integer headColNum, String sheetName,
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

		Sheet sheet = workbook.createSheet(sheetName);
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
			int colWidth = sheet.getColumnWidth(headColNum) * 2;
			sheet.setColumnWidth(headColNum, colWidth < 3000 ? 3000 : colWidth);
			headColNum++;
		}

		setValueStyle(valueStyle,HSSFCellStyle.BORDER_THIN,IndexedColors.GREY_50_PERCENT.getIndex());
		return sheet;
	}

	public static void export(String[] title, String sheetName, String filename, String[][] values, Workbook workbook,
			Integer headRowNum, CellStyle headStyle, CellStyle valueStyle, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 创建头部
		Cell cell = null;
		Sheet sheet = workbook.createSheet(sheetName);

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

		Row row = sheet.createRow(headRowNum);
		for (int i = 0, len = title.length; i < len; i++) {
			cell = row.createCell(i);
			row.setHeightInPoints(25);
			cell.setCellValue(title[i]);
			cell.setCellStyle(headStyle);
			int colWidth = sheet.getColumnWidth(i) * 2;
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

		// 创建内容
		if (values != null && values.length > 0) {
			for (int i = 0, len = values.length; i < len; i++) {
				row = sheet.createRow(i + 1);
				for (int j = 0; j < values[i].length; j++) {
					Object val = values[i][j];
					if(val == null){
						val = "";
					} else {
						if(val instanceof String) {
							val = HtmlUtils.htmlUnescape(val.toString());
						}
					}
					// 将内容按顺序赋给对应的列对象
					row.createCell(j).setCellValue(val.toString());
					row.getCell(j).setCellStyle(valueStyle);
				}
				// setSizeColumn(sheet,values[i].length);
			}
		}

		// 导出 也就是下载功能， 使用输出流
		String useragent = request.getHeader("User-Agent");
		if (useragent.contains("Firefox")) {
			filename = "=?UTF-8?B?" + new BASE64Encoder().encode(filename.getBytes("utf-8")) + "?=";
		} else {
			filename = URLEncoder.encode(filename, "utf-8");
			filename = filename.replace("+", " ");
		}
		OutputStream out = response.getOutputStream();

		// 导出 输出流 设置下载头信息 Content-Disposition 设置mime类型
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		workbook.write(out);
		out.flush();
		out.close();
	}
	
	/**
	 * 设置值样式
	 * @param valueStyle
	 */
	public static void setValueStyle(CellStyle valueStyle,short border,short borderColor) {
		valueStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 纯色使用前景颜色填充
		valueStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		valueStyle.setBorderRight(border);
		valueStyle.setRightBorderColor(borderColor);
		valueStyle.setBorderLeft(border);
		valueStyle.setLeftBorderColor(borderColor);
		valueStyle.setBorderTop(border);
		valueStyle.setTopBorderColor(borderColor);
		valueStyle.setBorderBottom(border);
		valueStyle.setBottomBorderColor(borderColor);
	}

	// 自适应宽度(中文支持)
	private static void setSizeColumn(Sheet sheet, int size) {
		for (int columnNum = 0; columnNum < size; columnNum++) {
			int columnWidth = sheet.getColumnWidth(columnNum) / 256;
			for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
				Row currentRow;
				// 当前行未被使用过
				if (sheet.getRow(rowNum) == null) {
					currentRow = sheet.createRow(rowNum);
				} else {
					currentRow = sheet.getRow(rowNum);
				}
				if (currentRow.getCell(columnNum) != null) {
					Cell currentCell = currentRow.getCell(columnNum);
					if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
						int length = currentCell.getStringCellValue().getBytes().length;
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}
			}

			sheet.setColumnWidth(columnNum, columnWidth < 255 ? columnWidth * 256 : 256 * 50);
		}
	}

	/**
	 * 判断单元格是否合并
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static MergedRegionResult isMergedRegion(Sheet sheet, int row, int column) {
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
		Row fRow1 = sheet.getRow(row);
		String str = fRow1.getCell(column) == null ? null : fRow1.getCell(column).getStringCellValue();
		return new MergedRegionResult(false, 0, 0, 0, 0, str);
	}
	
	/**
     * 给合并单元格增加边框
     * @param border
     * @param region
     * @param sheet
     * @param wb
     */
    public static void setBorderStyle(int border, CellRangeAddress region, Sheet sheet, Workbook wb){
        RegionUtil.setBorderBottom(border, region, sheet, wb);    //下边框
        RegionUtil.setBorderLeft(border, region, sheet, wb);     //左边框
        RegionUtil.setBorderRight(border, region, sheet, wb);    //右边框
        RegionUtil.setBorderTop(border, region, sheet, wb);      //上边框
    }
    
    /**
     * 下载
     * @param filename
     * @param workbook
     * @param request
     * @param response
     * @throws Exception
     */
    public static void download(String filename,Workbook workbook,HttpServletRequest request, HttpServletResponse response) throws  Exception{
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

}
