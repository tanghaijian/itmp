package cn.pioneeruniverse.common.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.dev.entity.ExtendedField;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 导出Excel文件（导出“XLSX”格式，支持大数据量导出   @see org.apache.poi.ss.SpreadsheetVersion）
 * add by ztt
 *
 */
public class ExportExcel {
	
	private static Logger log = LoggerFactory.getLogger(ExportExcel.class);
			
	/**
	 * 工作薄对象
	 */
	private SXSSFWorkbook wb ;
	
	/**
	 * 工作表对象
	 */
	private Sheet sheet;
	
	/**
	 * 样式列表
	 */
	private Map<String, CellStyle> styles;
	
	/**
	 * 当前行号
	 */
	private int rownum;

	private Long changeRowNum;

	private Method changeRowNumMethod;

	private int changeRowNumIndex;

	/**
	 * 注解列表（Object[]{ ExcelField, Field/Method }）
	 */
	List<Object[]> annotationList = Lists.newArrayList();

	public ExportExcel(){}
	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 */
	public ExportExcel(String title, Class<?> cls){
		this(title, cls, 1);
	}
	public ExportExcel(String title, Class<?> cls,List<ExtendedField> extendedFields){
		this.ExportExcelNew(title, cls, 1,extendedFields);
	}


	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 * @param type 导出类型（1:导出数据；2：导出模板）
	 * @param groups 导入分组
	 */
	public ExportExcel(String title, Class<?> cls, int type, int... groups){
		// Get annotation field 
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs){
			ExcelField ef = f.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type()==0 || ef.type()==type)){
				if (groups!=null && groups.length>0){
					boolean inGroup = false;
					for (int g : groups){
						if (inGroup){
							break;
						}
						for (int efg : ef.groups()){
							if (g == efg){
								inGroup = true;
								annotationList.add(new Object[]{ef, f});
								break;
							}
						}
					}
				}else{
					annotationList.add(new Object[]{ef, f});
				}
			}
		}
		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms){
			ExcelField ef = m.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type()==0 || ef.type()==type)){
				if (groups!=null && groups.length>0){
					boolean inGroup = false;
					for (int g : groups){
						if (inGroup){
							break;
						}
						for (int efg : ef.groups()){
							if (g == efg){
								inGroup = true;
								annotationList.add(new Object[]{ef, m});
								break;
							}
						}
					}
				}else{
					annotationList.add(new Object[]{ef, m});
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		// Initialize
		List<String> headerList = Lists.newArrayList();
		for (Object[] os : annotationList){
			String t = ((ExcelField)os[0]).title();
			// 如果是导出，则去掉注释
			if (type==1){
				String[] ss = StringUtils.split(t, "**", 2);
				if (ss.length==2){
					t = ss[0];
				}
			}
			headerList.add(t);
		}
		initialize(title, headerList);
	}








	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 * @param type 导出类型（1:导出数据；2：导出模板）
	 * @param groups 导入分组
	 */
	public void ExportExcelNew(String title, Class<?> cls, int type, List<ExtendedField> extendedFields, int... groups){
		// Get annotation field
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs){
			ExcelField ef = f.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type()==0 || ef.type()==type)){
				if (groups!=null && groups.length>0){
					boolean inGroup = false;
					for (int g : groups){
						if (inGroup){
							break;
						}
						for (int efg : ef.groups()){
							if (g == efg){
								inGroup = true;
								annotationList.add(new Object[]{ef, f});
								break;
							}
						}
					}
				}else{
					annotationList.add(new Object[]{ef, f});
				}
			}
		}
		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms){
			ExcelField ef = m.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type()==0 || ef.type()==type)){
				if (groups!=null && groups.length>0){
					boolean inGroup = false;
					for (int g : groups){
						if (inGroup){
							break;
						}
						for (int efg : ef.groups()){
							if (g == efg){
								inGroup = true;
								annotationList.add(new Object[]{ef, m});
								break;
							}
						}
					}
				}else{
					annotationList.add(new Object[]{ef, m});
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		// Initialize
		List<String> headerList = Lists.newArrayList();
		for (Object[] os : annotationList){
			String t = ((ExcelField)os[0]).title();
			// 如果是导出，则去掉注释
			if (type==1){
				String[] ss = StringUtils.split(t, "**", 2);
				if (ss.length==2){
					t = ss[0];
				}
			}
			headerList.add(t);
		}
		if(extendedFields!=null && extendedFields.size()>0){
			for(ExtendedField extendedField:extendedFields){
				headerList.add(extendedField.getLabel()+"(自定义字段:"+extendedField.getFieldName()+")");

			}
		}
		initialize(title, headerList);

	}


	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headers 表头数组
	 */
	public ExportExcel(String title, String[] headers) {
		initialize(title, Lists.newArrayList(headers));
	}
	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headerList 表头列表
	 */
	public ExportExcel(String title, List<String> headerList) {
		initialize(title, headerList);
	}
	
	/**
	 * 初始化函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headerList 表头列表
	 */
	private void initialize(String title, List<String> headerList) {
		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet("Export");
		this.styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)){
			Row titleRow = sheet.createRow(rownum++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
					titleRow.getRowNum(), titleRow.getRowNum(), headerList.size()-1));
		}
		// Create header
		if (headerList == null){
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(rownum++);
		headerRow.setHeightInPoints(16);
		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			String[] ss = StringUtils.split(headerList.get(i), "**", 2);
			if (ss.length==2){
				cell.setCellValue(ss[0]);
				Comment comment = this.sheet.createDrawingPatriarch().createCellComment(
						new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
				comment.setString(new XSSFRichTextString(ss[1]));
				cell.setCellComment(comment);
			}else{
				cell.setCellValue(headerList.get(i));
			}
			sheet.autoSizeColumn(i);
		}
		for (int i = 0; i < headerList.size(); i++) {  
			int colWidth = sheet.getColumnWidth(i)*2;
	        sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);  
		}
		log.debug("Initialize success.");
	}
	
	/**
	 * 创建表格样式
	 * @param wb 工作薄对象
	 * @return 样式列表
	 */
	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font titleFont = wb.createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(titleFont);
		styles.put("title", style);

		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);
		style.setFont(dataFont);
		styles.put("data", style);
		
		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		styles.put("data1", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("data2", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		styles.put("data3", style);
		
		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
//		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.BLACK.getIndex());
		style.setFont(headerFont);
		styles.put("header", style);
		
		return styles;
	}

	/**
	 * 添加一行
	 * @return 行对象
	 */
	public Row addRow(){
		return sheet.createRow(rownum++);
	}
	

	/**
	 * 添加一个单元格
	 * @param row 添加的行
	 * @param column 添加列号
	 * @param val 添加值
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val){
		return this.addCell(row, column, val, 0, Class.class);
	}
	
	/**
	 * 添加一个单元格
	 * @param row 添加的行
	 * @param column 添加列号
	 * @param val 添加值
	 * @param align 对齐方式（1：靠左；2：居中；3：靠右）
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType){
		Cell cell = row.createCell(column);
		String cellFormatString = "@";
		try {
			if(val == null){
				cell.setCellValue("");
			}else if(fieldType != Class.class){
				cell.setCellValue((String)fieldType.getMethod("setValue", Object.class).invoke(null, val));
			}else{
				if(val instanceof String) {
					cell.setCellValue((String) val);
				}else if(val instanceof Integer) {
					cell.setCellValue((Integer) val);
					cellFormatString = "0";
				}else if(val instanceof Long) {
					cell.setCellValue((Long) val);
					cellFormatString = "0";
				}else if(val instanceof Double) {
					cell.setCellValue((Double) val);
					cellFormatString = "0.0";
				}else if(val instanceof Float) {
					cell.setCellValue((Float) val);
					cellFormatString = "0.00";
				}else if(val instanceof Date) {
					cell.setCellValue((Date) val);
					cellFormatString = "yyyy-MM-dd";
				}else {
					cell.setCellValue((String)Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(), 
						"fieldtype."+val.getClass().getSimpleName()+"Type")).getMethod("setValue", Object.class).invoke(null, val));
				}
				
			}
			if(val != null){
				CellStyle style = styles.get("data_column_"+column);
				if (style == null){
					style = wb.createCellStyle();
					style.cloneStyleFrom(styles.get("data"+(align>=1&&align<=3?align:"")));
			        style.setDataFormat(wb.createDataFormat().getFormat(cellFormatString));
					styles.put("data_column_" + column, style);
				}
				cell.setCellStyle(style);
			} else {
				CellStyle style = styles.get("data_column_"+column);
				if (style == null){
					style = wb.createCellStyle();
					style.cloneStyleFrom(styles.get("data"+(align>=1&&align<=3?align:"")));
				}
				cell.setCellStyle(style);
			}
		} catch (Exception ex) {
			log.info("Set cell value ["+row.getRowNum()+","+column+"] error: " + ex.toString());
			cell.setCellValue(val.toString());
		}
		return cell;
	}

	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @return list 数据列表
	 */
	public <E> ExportExcel setDataList(List<E> list){
		for (E e : list){
			int colunm = 0;
			Row row = this.addRow();
			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList){
				ExcelField ef = (ExcelField)os[0];
				Object val = null;
				// Get entity value
				try{
					if (StringUtils.isNotBlank(ef.value())){
						val = Reflections.invokeGetter(e, ef.value());
					}else{
						if (os[1] instanceof Field){
							val = Reflections.invokeGetter(e, ((Field)os[1]).getName());
						}else if (os[1] instanceof Method){
							val = Reflections.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
						}
					}
					
				}catch(Exception ex) {
					// Failure to ignore
					log.info(ex.toString());
					val = "";
				}
				this.addCell(row, colunm++, val, ef.align(), ef.fieldType());
				sb.append(val + ", ");
			}
			log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
		}
		return this;
	}


	public <E> ExportExcel setDataObjectList(List<E> list){
		for (E e : list){
			int colunm = 0;
			Row row = this.addRow();
			StringBuilder sb = new StringBuilder();
			this.addCell(row, colunm++, e, 0, e.getClass());
			log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
		}
		return this;
	}


	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @return list 数据列表
	 */
	public <E> ExportExcel setDataListNew(List<E> list,List<String> filedNames){
		for (E e : list){
			int colunm = 0;
			Row row = this.addRow();
			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList){
				ExcelField ef = (ExcelField)os[0];
				Object val = null;
				// Get entity value
				try{
					if (StringUtils.isNotBlank(ef.value())){
						val = Reflections.invokeGetter(e, ef.value());
					}else{
						if (os[1] instanceof Field){
							val = Reflections.invokeGetter(e, ((Field)os[1]).getName());
						}else if (os[1] instanceof Method){
							val = Reflections.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
						}
					}

				}catch(Exception ex) {
					// Failure to ignore
					log.info(ex.toString());
					val = "";
				}
				this.addCell(row, colunm++, val, ef.align(), ef.fieldType());
				sb.append(val + ", ");
			}



			try {
				if(filedNames!=null && filedNames.size()>0) {

					Method method = e.getClass().getMethod("getExtendedFields", new Class[]{});
					List<ExtendedField> extendedFields = (List<ExtendedField>) method.invoke(e, new Object[]{});
					if (extendedFields != null && extendedFields.size() > 0) {

						for(String fileName:filedNames){
							boolean flag=false;
							for(ExtendedField extendedField : extendedFields){


								if(extendedField.getFieldName().equals(fileName)){
									if(extendedField.getValueName()==null){
										extendedField.setValueName("");
									}
									this.addCell(row, colunm++, extendedField.getValueName(), 1,  Class.class);
									 flag=true;

								}
							}
							if(flag==false){
								this.addCell(row, colunm++, "", 1,  Class.class);
							}
						}

//						for (ExtendedField extendedField : extendedFields) {
//                            if(filedNames.contains(extendedField.getFieldName())) {
//                            	if(extendedField.getValueName()==null){
//                            		extendedField.setValueName("");
//								}
//								this.addCell(row, colunm++, extendedField.getValueName(), 1,  Class.class);
//							}
//						}
					}else{
						for(String fileName:filedNames){
							this.addCell(row, colunm++,"", 1,  Class.class);
						}
					}


				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

//		      List<ExtendedField>	extendedField=Reflections.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
//
//			ExtendedField extendedField=new ExtendedField();



			log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
		}


		return this;
	}



	/**
	 * 输出数据流
	 * @param os 输出数据流
	 */
	public ExportExcel write(OutputStream os) throws IOException{
		wb.write(os);
		return this;
	}
	
	/**
	 * 输出到客户端
	 * @param fileName 输出文件名
	 */
	public ExportExcel write(HttpServletResponse response, String fileName) throws IOException{
		response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("utf-8"),"ISO8859-1"));
		write(response.getOutputStream());
		return this;
	}
	
	/**
	 * 输出到文件
	 * @param name 输出文件名
	 */
	public ExportExcel writeFile(String name) throws FileNotFoundException{
		FileOutputStream os = new FileOutputStream(name);
		try {
			this.write(os);
		} catch (Exception e) {
		}finally {
			try {
				if (os!=null) {
					os.close();
				}
			} catch (Exception e2) {
				
			}
		}
		return this;
	}
	
	/**
	 * 清理临时文件
	 */
	public ExportExcel dispose(){
		wb.dispose();
		return this;
	}


	/**
	 * 设置第一行和第二行有合并情况的表头
	 * @param cls1 主表头 实体类
	 * @param cls2 需要分行的表头 实体类
	 * @param sheetName
	 * @param firstCol 第一行合并 第二行有子集的开始列数
	 * @param lastCol 第一行合并 第二行有子集的结束列数
	 * @param firstColName  第一行合并 第二行有子集 填入的数据
	 * @return
	 */
	public ExportExcel setWorkHead(Class<?> cls1, Class<?> cls2, String sheetName, int cellRangeRow, int firstCol, int lastCol, String firstColName){
		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet(sheetName);
		this.styles = createStyles(wb);

		Row rowFirst = this.sheet.createRow(rownum++);
		Method[] methods1 = cls1.getDeclaredMethods();

		List<CellRangeAddress> rangeAddressList = new ArrayList<>();
		for (int i = 0,len = methods1.length; i < len; i++) {
			ExcelField excelField1 = methods1[i].getAnnotation(ExcelField.class);
			if(excelField1 != null ){
				if(excelField1.sort() >= 0){
					if(excelField1.isList() == false ){
						CellRangeAddress callFirst= new CellRangeAddress(0,cellRangeRow,excelField1.sort(),excelField1.sort());
						sheet.addMergedRegion(callFirst);
						rangeAddressList.add(callFirst);

						Cell cell1 = rowFirst.createCell(excelField1.sort());
						cell1.setCellValue(excelField1.title());
						cell1.setCellStyle(styles.get("header"));

						sheet.autoSizeColumn(excelField1.sort());
						int colWidth = sheet.getColumnWidth(excelField1.sort())*2;
						sheet.setColumnWidth(excelField1.sort(), colWidth < 3000 ? 3000 : colWidth);
					}
					annotationList.add(new Object[]{excelField1, methods1[i]});
				}
			}
		}
		CellRangeAddress callSecond = new CellRangeAddress(0,0,firstCol,lastCol);
		sheet.addMergedRegion(callSecond);
		setBorderStyle(CellStyle.BORDER_THIN,callSecond);

		Cell cell = rowFirst.createCell(firstCol);
		cell.setCellValue(firstColName);
		cell.setCellStyle(styles.get("header"));


		Row rowSecond = sheet.createRow(rownum++);
		for(CellRangeAddress  rangeAddress:rangeAddressList){
			setBorderStyle(CellStyle.BORDER_THIN,rangeAddress);
		}

		Method[] methods2 = cls2.getDeclaredMethods();
		for (int i = 0,len = methods2.length; i < len; i++) {
			ExcelField excelField2 = methods2[i].getAnnotation(ExcelField.class);
			if(excelField2 != null ){
				CellRangeAddress cellRangeAddress = new CellRangeAddress(cellRangeRow,cellRangeRow,excelField2.sort(),excelField2.sort());
				sheet.addMergedRegion(cellRangeAddress);
				setBorderStyle(CellStyle.BORDER_THIN,cellRangeAddress);

				Cell cell2 = rowSecond.createCell(excelField2.sort());
				cell2.setCellValue(excelField2.title());
				cell2.setCellStyle(styles.get("header"));

				annotationList.add(new Object[]{excelField2, methods2[i]});
				sheet.autoSizeColumn(excelField2.sort());
				int colWidth = sheet.getColumnWidth(excelField2.sort())*2;
				sheet.setColumnWidth(excelField2.sort(), colWidth < 3000 ? 3000 : colWidth);
			}
		}

		return this;
	}

	/**
	 *  实体类有list情况，表头不需要合并
	 * @param cls1
	 * @param cls2
	 * @param sheetName
	 * @return
	 */
	public ExportExcel setWorkHead(Class<?> cls1, Class<?> cls2, String sheetName){
		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet(sheetName);
		this.styles = createStyles(wb);

		Row rowFirst = this.sheet.createRow(rownum++);
		Method[] methods1 = cls1.getDeclaredMethods();

		for (int i = 0,len = methods1.length; i < len; i++) {
			ExcelField excelField1 = methods1[i].getAnnotation(ExcelField.class);
			if(excelField1 != null ){
				if(excelField1.sort() >= 0){
					// 判断是list 或者 不是
					if(excelField1.isList() == false ){
						Cell cell1 = rowFirst.createCell(excelField1.sort());
						cell1.setCellValue(excelField1.title());
						cell1.setCellStyle(styles.get("header"));

						sheet.autoSizeColumn(excelField1.sort());
						int colWidth = sheet.getColumnWidth(excelField1.sort())*2;
						sheet.setColumnWidth(excelField1.sort(), colWidth < 3000 ? 3000 : colWidth);
					} else {
						Method[] methods2 = cls2.getDeclaredMethods();
						for (int ii = 0,leni = methods2.length; ii < leni; ii++) {
							ExcelField excelField2 = methods2[ii].getAnnotation(ExcelField.class);
							if(excelField2 != null ){
								Cell cell2 = rowFirst.createCell(excelField2.sort());
								cell2.setCellValue(excelField2.title());
								cell2.setCellStyle(styles.get("header"));

								annotationList.add(new Object[]{excelField2, methods2[ii]});
								sheet.autoSizeColumn(excelField2.sort());
								int colWidth = sheet.getColumnWidth(excelField2.sort())*2;
								sheet.setColumnWidth(excelField2.sort(), colWidth < 3000 ? 3000 : colWidth);
							}
						}
					}
					annotationList.add(new Object[]{excelField1, methods1[i]});
				}
			}
		}
		return this;
	}


	/**
	 * 表头动态生成
	 * @param cls 需要导出的实体类
	 * @param methodName 实体类动态生成表头的方法名称
	 * @param changeRowNum 动态表头生成的个数
	 * @param list 动态表头生成的数据
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public ExportExcel setWorkHead(Class<?> cls,String methodName,Long changeRowNum,List<Object> list,String sheetName) throws Exception{
		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet(sheetName);
		this.styles = createStyles(wb);
		this.changeRowNum = changeRowNum;

		Row rowFirst = this.sheet.createRow(rownum++);
		Method[] methods = cls.getDeclaredMethods();
		this.changeRowNumMethod = cls.getMethod(methodName);
		String title = this.changeRowNumMethod.getAnnotation(ExcelField.class).title();
		Cell cell = null;
		for(int i = 0; i < this.changeRowNum.longValue();i++){
			cell = rowFirst.createCell(i);
			cell.setCellValue(list.get(i).toString());
			cell.setCellStyle(styles.get("header"));
		}

		for (int i = 0,len = methods.length; i < len; i++) {
			ExcelField excelField = methods[i].getAnnotation(ExcelField.class);
			if(excelField != null && excelField.isList() == false){
				annotationList.add(new Object[]{excelField, methods[i]});
				if(excelField.title().equals(title)){
					this.changeRowNumIndex = annotationList.size()-1;
					continue;
				} else {
					cell = rowFirst.createCell(this.changeRowNum.intValue()+excelField.sort());
					cell.setCellValue(excelField.title());
					cell.setCellStyle(styles.get("header"));
				}
			}
		}
		return this;
	}

	/**
	 * 需要导出的数据
	 * @param list
	 * @param <E>
	 * @return
	 */
	public <E> ExportExcel setDataListWithList(List<E> list){
		Integer valListSize = 0;
		for (E e : list){
			Class eClass =  e.getClass();
			Class methodClass =  null;

			Row row = this.addRow();
			StringBuilder sb = new StringBuilder();

			for (Object[] os : annotationList){
				if (os[1] instanceof Method){
					if(((Method)os[1]).getReturnType() == java.util.List.class){
						Object val = Reflections.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
						List<E> valList = (List<E>)val;
						valListSize = valList.size();
						setDataWithList((List<E>)val,row);
						break;
					}
				}
			}
			for (Object[] os : annotationList){
				ExcelField ef = (ExcelField)os[0];
				Object val = null;
				// Get entity value
				try{
					if (StringUtils.isNotBlank(ef.value())){
						val = Reflections.invokeGetter(e, ef.value());
					}else{
						if (os[1] instanceof Method){
							val = Reflections.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
							methodClass = ((Method)os[1]).getDeclaringClass();
							if(((Method)os[1]).getReturnType() == java.util.List.class){
								continue;
							}
						}
					}

				}catch(Exception ex) {
					// Failure to ignore
					log.info(ex.toString());
					continue;
				}
				if(eClass == methodClass){
					int firstRow = row.getRowNum();
					int lastRow = valListSize.intValue() == 0?firstRow:firstRow+valListSize-1;
					CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow,lastRow,ef.sort(),ef.sort());
					sheet.addMergedRegion(cellRangeAddress);
					setBorderStyle(CellStyle.BORDER_THIN,cellRangeAddress);

					this.addCell(row, ef.sort(), val, ef.align(), ef.fieldType());
					sb.append(val + ", ");
				}
			}
			log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
		}
		return this;
	}

	/**
	 * 需要导出数据 中含有的子集
	 * @param list
	 * @param row
	 * @param <E>
	 */
	public <E> void  setDataWithList(List<E> list,Row row){
		for (int i = 0,len = list.size(); i < len; i++) {

			if( i != 0){
				row = this.addRow();
			}

			Class listClass = list.get(i).getClass();
			Class methodClass =  null;

			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList){

				ExcelField ef = (ExcelField)os[0];
				Object val = null;
				// Get entity value
				try{
					if (StringUtils.isNotBlank(ef.value())){
						val = Reflections.invokeGetter(list.get(i), ef.value());
					}else{
						if (os[1] instanceof Method){
							val = Reflections.invokeMethod(list.get(i), ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
							methodClass = ((Method)os[1]).getDeclaringClass();
							if(((Method)os[1]).getReturnType() == java.util.List.class){
								setDataWithList((List<E>)val,row);
								methodClass = java.util.List.class;
							}
						}
					}
				}catch(Exception ex) {
					// Failure to ignore
					log.info(ex.toString());
					continue;
				}
				if(listClass == methodClass){
					this.addCell(row, ef.sort(), val, ef.align(), ef.fieldType());
					sb.append(val + ", ");
				}
			}
			log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
		}
	}

	/**
	 * 合并单元格 设置边框格式
	 * @param border
	 * @param region
	 */
	public void setBorderStyle(int border, CellRangeAddress region){
		RegionUtil.setBorderBottom(border, region, this.sheet, this.wb); // 下边框
		RegionUtil.setBottomBorderColor(HSSFColor.BLACK.index,region, this.sheet, this.wb);
		RegionUtil.setBorderLeft(border, region, this.sheet, this.wb);     //左边框
		RegionUtil.setLeftBorderColor(HSSFColor.BLACK.index,region, this.sheet, this.wb);
		RegionUtil.setBorderRight(border, region, this.sheet, this.wb);    //右边框
		RegionUtil.setRightBorderColor(HSSFColor.BLACK.index,region, this.sheet, this.wb);
		RegionUtil.setBorderTop(border, region, this.sheet, this.wb);      //上边框
		RegionUtil.setTopBorderColor(HSSFColor.BLACK.index,region, this.sheet, this.wb);
	}

	/**
	 * 导出的数据列数为动态的
	 * @param list 需要导出的数据
	 * @param
	 * @param <E>
	 * @return
	 */
	public <E> ExportExcel setChangeRowNumList(List<E> list){
		for (E e : list){

			Row row = this.addRow();
			StringBuilder sb = new StringBuilder();

			Object changeVal = null;
			Object[] changeOs = annotationList.get(this.changeRowNumIndex);
			ExcelField changeEf = (ExcelField)changeOs[0];
			if (StringUtils.isNotBlank(changeEf.value())){
				changeVal = Reflections.invokeGetter(e, changeEf.value());
			} else {
				if (changeOs[1] instanceof Method){
					changeVal = Reflections.invokeMethod(e, ((Method)changeOs[1]).getName(), new Class[] {}, new Object[] {});
				}
			}
			if(((Method)changeOs[1]).getReturnType() == java.lang.String.class){
				if(changeVal != null && !changeVal.toString().equals("")){
					String[] str = changeVal.toString().split(changeEf.splitRegex());
					for (int i = 0,len = str.length; i < len; i++) {
						this.addCell(row, i, str[i], changeEf.align(), changeEf.fieldType());
					}
				}
			} else if(((Method)changeOs[1]).getReturnType() == java.util.List.class){
				List<E> valList = (List<E>)changeVal;
				for (int ii = 0,len = valList.size(); ii < len; ii++) {
					this.addCell(row, ii, valList.get(ii), changeEf.align(), changeEf.fieldType());
				}
			}

			for (Object[] os : annotationList){
				ExcelField ef = (ExcelField)os[0];
				if(ef.title().equals(changeEf.title())){
					continue;
				}
				Object val = null;
				try{
					if (StringUtils.isNotBlank(ef.value())){
						val = Reflections.invokeGetter(e, ef.value());
					}else{
						if (os[1] instanceof Method){
							val = Reflections.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
						}
					}
				}catch(Exception ex) {
					log.info(ex.toString());
					continue;
				}
				this.addCell(row, this.changeRowNum.intValue()+ef.sort(), val, ef.align(), ef.fieldType());
				sb.append(val + ", ");
			}
		}
		return this;
	}


	/**
	 * 导出测试
	 */
	/*public static void main(String[] args) throws Throwable {
		
		List<String> headerList = Lists.newArrayList();
		for (int i = 1; i <= 10; i++) {
			headerList.add("表头"+i);
		}
		
		List<String> dataRowList = Lists.newArrayList();
		for (int i = 1; i <= headerList.size(); i++) {
			dataRowList.add("数据"+i);
		}
		
		List<List<String>> dataList = Lists.newArrayList();
		for (int i = 1; i <=10; i++) {
			dataList.add(dataRowList);
		}

		ExportExcel ee = new ExportExcel("表格标题", headerList);
		
		for (int i = 0; i < dataList.size(); i++) {
			Row row = ee.addRow();
			for (int j = 0; j < dataList.get(i).size(); j++) {
				ee.addCell(row, j, dataList.get(i).get(j));
			}
		}
		
		ee.writeFile("target/export.xlsx");

		ee.dispose();
		
		log.debug("Export success.");
		
	}*/

}
