package cn.pioneeruniverse.common.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WordUtil {

	/**
	 * html导出word
	 * 
	 * @param content
	 * @param response
	 * @throws Exception
	 */
	public static void htmlToWord(String content,String fileName, HttpServletResponse response) throws Exception {
		if (content != null) {
			Document doc = Jsoup.parse(content.toString());
			Elements tableElements = doc.select("table");
			tableElements.attr("border", "1");
			tableElements.attr("cellspacing", "0");
			tableElements.attr("cellpadding", "0");
			Elements imgElements = doc.select("img");
			for (Element element : imgElements) {
				String url = element.attr("src");
				element.attr("src", url.split("#")[0]);
			}
			content = doc.outerHtml();
			byte b[] = content.getBytes();
			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			POIFSFileSystem poifs = new POIFSFileSystem();
			DirectoryEntry directory = poifs.getRoot();
			DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
			fileName = URLEncoder.encode(fileName, "utf-8");
			fileName = fileName.replace("+", " ");
			// 设置word格式
			OutputStream ostream = response.getOutputStream();
			response.setContentType("application/msword;charset=GB2312");
			response.setHeader("Content-disposition", "attachment;filename="+fileName+".doc");
			poifs.writeFilesystem(ostream);
			bais.close();
			ostream.close();
		}
	}
}
