package cn.pioneeruniverse.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

/**
 * @Author: ztt
 * @Description: 根据模板导出word 包含表格 图表
 *
 */
public class WorderToNewWordUtils {
	/**
	 * 根据模板生成word文档
	 * @param inputUrl 模板路径  改为 InputStream 输入流， 因为 打jar包发布到服务器 会找不到路径
	 * @param textMap 需要替换的文本内容
	 * @param mapList 需要动态生成的内容
	 * @return
	 */
    public static XWPFDocument changWord(InputStream inputUrl, Map<String, Object> textMap, Map<String, Object> mapList) {
    	XWPFDocument document = null;
        try {
            //获取docx解析对象
        	//document = new CustomXWPFDocument(POIXMLDocument.openPackage(inputUrl));
            document = new XWPFDocument(inputUrl);
            
            changeText(document,textMap);
            //解析替换表格对象
            WorderToNewWordUtils.changeTable(document, textMap, mapList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }
 
    /**
     * 替换段落文本
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合
     */
    public static void changeText(XWPFDocument document, Map<String, Object> textMap){
        //获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();
 
        for (XWPFParagraph paragraph : paragraphs) {
            //判断此段落时候需要进行替换
            String text = paragraph.getText();
            System.out.println(text);
            if(checkText(text)){
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    //替换模板原来位置
                    Object ob = changeValue(run.toString(), textMap);
                    if (ob instanceof String){
                        setText(run,(String)ob);
                    }else if(ob instanceof Map) {
                    	run.setText("", 0);
                    	Map pic = (Map)ob;
                        int width = Integer.parseInt(pic.get("width").toString());
                        int height = Integer.parseInt(pic.get("height").toString());
                        int picType = getPictureType(pic.get("type").toString());
                        byte[] byteArray = (byte[]) pic.get("content");
                        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
                        try {
                            document.addPictureData(byteInputStream, picType);
                            createPicture(document, document.getAllPictures().size()-1, width, height, paragraph);
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
 
    /**
     * 替换表格对象方法
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合
     * @param mapList 需要动态生成的内容
     */
    public static void changeTable(XWPFDocument document, Map<String, Object> textMap,Map<String, Object> mapList){
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        
        //循环所有需要进行替换的文本，进行替换
        for (int i = 0; i < tables.size(); i++) {
            XWPFTable table = tables.get(i);
            if(checkText(table.getText())){
                List<XWPFTableRow> rows = table.getRows();
                //遍历表格,并替换模板
                eachTable(document,rows, textMap);
            }
        }
        
        //操作word中的表格
        for(int i = 0;i < tables.size();i++) {
        	XWPFTable table = tables.get(i);
        	for(Map.Entry<String, Object> entry : mapList.entrySet()) {
        		if(table.getText().indexOf("##{"+entry.getKey()+"}##") != -1) {
        			insertTable(table, (List<String[]>)entry.getValue());
        			break;
        		}  
        	}
        }
    }
 
    /**
     * 遍历表格
     * @param rows 表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(XWPFDocument document,List<XWPFTableRow> rows ,Map<String, Object> textMap){
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if(checkText(cell.getText())){
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            Object ob = changeValue(run.toString(), textMap);
                            setText(run,(String)ob);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 为表格插入数据，行数不够添加新行
     * @param table 需要插入数据的表格
     * @param daList 表格的插入数据
     */
    public static void insertTable(XWPFTable table,List<String[]> daList){
    		table.removeRow(1);
    		 
            //创建行和创建需要的列
            for(int i = 0; i < daList.size(); i++){
                XWPFTableRow row = table.insertNewTableRow(1);//添加一个新行
                for(int j = 0;j < daList.get(i).length;j++) {
                	row.createCell();
                }
            }
            
            //加内边框
	         CTTblBorders borders=table.getCTTbl().getTblPr().addNewTblBorders();  
	   		 CTBorder hBorder=borders.addNewInsideH();  
	   	     hBorder.setVal(STBorder.Enum.forString("single"));  
	   	     hBorder.setSz(new BigInteger("1"));  
	   	     hBorder.setColor("CCCCCC");  
	   	     CTBorder vBorder=borders.addNewInsideV();  
	   	     vBorder.setVal(STBorder.Enum.forString("single"));  
	   	     vBorder.setSz(new BigInteger("1"));  
	   	     vBorder.setColor("CCCCCC");  
	   	     
            //获取单元格插入数据
            for(int i = 0; i < daList.size(); i++){
                List<XWPFTableCell> cells = table.getRow(i+1).getTableCells();
                for(int j = 0; j < cells.size(); j++){
                    XWPFTableCell cell02 = cells.get(j);
                    cell02.setText(daList.get(i)[j]);
                }
            }
           
    }
 
    /**
     * 判断文本中时候包含$
     * @param text 文本
     * @return 包含返回true,不包含返回false
     */
    public static boolean checkText(String text){
        boolean check  =  false;
        if(text.indexOf("$")!= -1){
            check = true;
        }
        return check;
    }
 
    /**
     * 匹配传入信息集合与模板
     * @param value 模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static Object changeValue(String value, Map<String, Object> textMap){
        Set<Entry<String, Object>> textSets = textMap.entrySet();
        Object valu = null;
        for (Entry<String, Object> textSet : textSets) {
            //匹配模板与替换值 格式${key}$
            String key = textSet.getKey();
            if(key.indexOf(value)!= -1){
                valu = textSet.getValue();
            }
        }
        return valu;
    }

    /**
     * 根据图片类型，取得对应的图片类型代码
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType){
        int res = XWPFDocument.PICTURE_TYPE_PICT;
        if(picType != null){
            if(picType.equalsIgnoreCase("png")){
                res = XWPFDocument.PICTURE_TYPE_PNG;
            }else if(picType.equalsIgnoreCase("dib")){
                res = XWPFDocument.PICTURE_TYPE_DIB;
            }else if(picType.equalsIgnoreCase("emf")){
                res = XWPFDocument.PICTURE_TYPE_EMF;
            }else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){
                res = XWPFDocument.PICTURE_TYPE_JPEG;
            }else if(picType.equalsIgnoreCase("wmf")){
                res = XWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }
    
    public static void createPicture(XWPFDocument document,int id, int width, int height,XWPFParagraph paragraph) {
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        String blipId = document.getAllPictures().get(id).getPackageRelationship().getId();
        CTInline inline = paragraph.createRun().getCTR().addNewDrawing().addNewInline();
        String picXml = ""
        +"<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
        +"   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
        +"      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
        +"         <pic:nvPicPr>" + "            <pic:cNvPr id=\""
        + id
        +"\" name=\"Generated\"/>"
        +"            <pic:cNvPicPr/>"
        +"         </pic:nvPicPr>"
        +"         <pic:blipFill>"
        +"            <a:blip r:embed=\""
        + blipId
        +"\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
        +"            <a:stretch>"
        +"               <a:fillRect/>"
        +"            </a:stretch>"
        +"         </pic:blipFill>"
        +"         <pic:spPr>"
        +"            <a:xfrm>"
        +"               <a:off x=\"0\" y=\"0\"/>"
        +"               <a:ext cx=\""
        + width
        +"\" cy=\""
        + height
        +"\"/>"
        +"            </a:xfrm>"
        +"            <a:prstGeom prst=\"rect\">"
        +"               <a:avLst/>"
        +"            </a:prstGeom>"
        +"         </pic:spPr>"
        +"      </pic:pic>"
        +"   </a:graphicData>" + "</a:graphic>";

        inline.addNewGraphic().addNewGraphicData();
        XmlToken xmlToken = null;
        try{
        xmlToken = XmlToken.Factory.parse(picXml);
        }catch(XmlException xe) {
        xe.printStackTrace();
        }
        inline.set(xmlToken);

        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);

        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);

        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("图片"+ id);
        docPr.setDescr("测试");
        }
    
    /**
     * 设置文本(换行处理)
     * @param run
     * @param text
     */
    private static void setText(XWPFRun run,String text) {
    	if(text != null) {
    		String[] textArr = text.split("\r");
    		int index = 0;
        	for (String str : textArr) {
        		if(!str.equals("null")) {
        			run.setText(str,index);
        			if(textArr.length != 1) {
        				run.addCarriageReturn();
        			}
        			index++;
        		}
    		}
    	}else {
    		run.setText("",0);
    	}
    }
}
