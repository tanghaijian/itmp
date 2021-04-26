package cn.pioneeruniverse.common.factory;

import java.io.IOException;

import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import com.itextpdf.text.BadElementException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.codec.Base64;

/**
 * 
* @ClassName: Base64ImgReplacedElementFactory
* @Description: 图片转换器，生成pdf时使用
* @author author
* @date 2020年8月21日 下午4:59:43
*
 */
public class Base64ImgReplacedElementFactory implements ReplacedElementFactory {

	@Override
	public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth,
			int cssHeight) {
		Element e = box.getElement();
		if (e == null) {
			return null;
		}
		String nodeName = e.getNodeName();
		// 找到img标签
		if (nodeName.equals("img")) {
			String attribute = e.getAttribute("src");
			FSImage fsImage;
			try {
				// 生成itext图像
				fsImage = buildImage(attribute, uac);
			} catch (Exception e1) {
				fsImage = null;
			} 
			if (fsImage != null) {
				// 对图像进行缩放
				if (cssWidth != -1 || cssHeight != -1) {
					fsImage.scale(cssWidth, cssHeight);
				}
				return new ITextImageElement(fsImage);
			}
		}

		return null;
	}

	/**
	 *      * 编解码base64并生成itext图像          
	 * 
	 * @throws com.lowagie.text.BadElementException
	 */
	protected FSImage buildImage(String srcAttr, UserAgentCallback uac)
			throws IOException, BadElementException, com.lowagie.text.BadElementException {
		FSImage fiImg = null;
		if (srcAttr.toLowerCase().startsWith("data:image/")) {
			String base64Code = srcAttr.substring(srcAttr.indexOf("base64,") + "base64,".length(), srcAttr.length());
			// 解码
			byte[] decodedBytes = Base64.decode(base64Code);

			fiImg = new ITextFSImage(Image.getInstance(decodedBytes));
		} else {
			fiImg = uac.getImageResource(srcAttr).getImage();
		}
		return fiImg;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFormSubmissionListener(FormSubmissionListener listener) {
		// TODO Auto-generated method stub

	}

}
