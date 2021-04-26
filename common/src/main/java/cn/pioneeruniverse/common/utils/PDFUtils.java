package cn.pioneeruniverse.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.pdf.BaseFont;

import cn.pioneeruniverse.common.factory.Base64ImgReplacedElementFactory;

/**
 * PDF导出类
 * 
 * @author fanwentao
 *
 */
public class PDFUtils {
	
	public static void exportPDF(HttpServletRequest request, HttpServletResponse response) {

	}

	/**
	 * html导出PDF
	 * @param content
	 * @param response
	 * @throws Exception
	 */
	public static void htmlToPDF(String content,String fileName,HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(content != null) {
			content = "<html><body style='font-family: SimSun;'>"+content+"</body></html>";
			content.replaceAll("<table>", "<table border='1' cellspacing='0' cellpadding='0'>");
			String origin = request.getHeader("Host");
			ITextRenderer renderer = new ITextRenderer();
			renderer.getSharedContext().setReplacedElementFactory(new Base64ImgReplacedElementFactory());
			renderer.getSharedContext().getTextRenderer().setSmoothingThreshold(0); 
			renderer.getSharedContext().setBaseURL("http://"+origin+"/projectManage/systemDirectoryDocumentOperate/queryPic/");
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont("simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			renderer.setDocumentFromString(content);
			renderer.layout();
			fileName = URLEncoder.encode(fileName, "utf-8");
			fileName = fileName.replace("+", " ");
			response.setCharacterEncoding("utf-8");
			// 设置pdf格式
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment;filename="+fileName+".pdf");
			OutputStream ostream = response.getOutputStream();
			renderer.createPDF(ostream);
			ostream.close();
		}
	}
}
