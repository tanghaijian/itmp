package cn.pioneeruniverse.common.utils;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 
 * @author   Dom4j 生成XML文档与解析XML文档
 */
public class XMLUtil {
	private static Logger log = Logger.getLogger(XMLUtil.class);
	

	  /**
	    *describe   :功能说明：将xml文本的节点提取成k,v对应的map
 	    *@param xml 输入报文
 	    *@return    : Map<String,String>    key=节点;value=节点内容
	    *@author    :lee
	    *@createDate:2014-2-10  下午01:56:38
	    *@history
	    */ 
	public static Map<String,String> parserXmlToMap(String xml ) {
 		SAXReader saxReader = new SAXReader();
 		Map<String,String> map=new HashMap<String, String>();
		try {
			Document document = saxReader.read( new ByteArrayInputStream(xml.getBytes("utf-8")));
			Element nodes = document.getRootElement();
			Element node =null;
			for (Iterator<?> i = nodes.elementIterator(); i.hasNext();) {
  				node = (Element) i.next();
  				map.put(node.getName(), node.getText());
  				log.info(node.getName() + ":" + node.getText());
 			}
		} catch ( Exception e) {
			log.error("xml 解析失败！ xml="+xml,e);
		}
		return map;
		 
	}
	  /**
	    *describe   :功能说明：将map 的key值作为xml的节点，value作为节点值，输出组装结果
 	    *@param map  
	    *@param rootName  xml 的根节点
 	    *@return    : String  
	    *@author    :lee
	    *@createDate:2014-2-10  下午02:30:33
	    *@history
	    */ 
	public static String mapToXmlStr(Map<String,String> map,String rootName){
  		 String key="";
  		 StringBuffer xmlBf=new StringBuffer("<"+rootName+">");
		 for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			 key=iterator.next();
			 log.info("key="+key+";value="+map.get(key));
			 xmlBf.append("<"+key+">");
			 xmlBf.append("<![CDATA["+map.get(key)+"]]>");
			 xmlBf.append("</"+key+">");
		 }
		 xmlBf.append("</"+rootName+">");
 	 	 return xmlBf.toString();
	}
	
	 /**
	    *describe   :功能说明：将map 的key值作为xml的节点，value作为节点值，输出组装结果
	    *@param map  
	    *@param rootName  xml 的根节点
	    *@return    : String  
	    *@author    :lee
	    *@createDate:2014-2-10  下午02:30:33
	    *@history
	    */ 
	public static String listMapToXmlStr(List<Map<String,String>> list,String rootName){
		Element root = DocumentHelper.createElement(rootName);
		Document document = DocumentHelper.createDocument(root);
		 if(list !=null && list.size()>0){
			 for(int i = 0 ;i<list.size();i++){
				 Map map = list.get(i);
				 Element node = root.addElement("ITEM");
				 Iterator<String> iterator = map.keySet().iterator();
				 while(iterator.hasNext()) {
					String key=iterator.next();
					Element snode = node.addElement(key);
					snode.addText(map.get(key)==null?"":map.get(key).toString());
				 }
			 }
		 }
	 	 return document.asXML();
	}
}
