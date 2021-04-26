package cn.pioneeruniverse.zuul.filter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.StreamUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;

public class XssFilter extends ZuulFilter{
	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}
	// 自定义过滤器执行的顺序，数值越大越靠后执行，越小就越先执行
	@Override
	public int filterOrder() {
		return FilterConstants.PRE_DECORATION_FILTER_ORDER - 2;
	}
	@Override
	public boolean shouldFilter() {
		return true;
	}
	// 执行过滤逻辑
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		try {
			InputStream in = ctx.getRequest().getInputStream();
			String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
			String newBody  = this.cleanXSS(body);

			final byte[] reqBodyBytes = newBody.getBytes();
			ctx.setRequest(new HttpServletRequestWrapper(request){

			@Override
			public ServletInputStream getInputStream() throws IOException {
				return new ServletInputStreamWrapper(reqBodyBytes);
			}

			@Override
			public int getContentLength() {
				return reqBodyBytes.length;
			}
	
			@Override
			public long getContentLengthLong() {
				return reqBodyBytes.length;
			}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	private String cleanXSS(String value) {
		    //value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	        //value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
	       // value = value.replaceAll("'", "&#39;");
//	        value = value.replaceAll("[e|E][v|V][a|A][l|L]\\((.*)\\)", "*eval");
//	        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
//	        value = value.replaceAll("[s|S][c|C][r|R][i|C][p|P][t|T]", "*script");
//	        value = value.replaceAll("[s|S][t|T][y|Y][l|L][E|e]", "*style");
	        value = value.replaceAll(" eval\\(", " EVAL(");
			value = value.replaceAll("([/ ]+?)javascript", "$1JAVASCRIPT");
			value = value.replaceAll("(< *?)script", "$1SCRIPT");
	        
	        return value;
	}
}
