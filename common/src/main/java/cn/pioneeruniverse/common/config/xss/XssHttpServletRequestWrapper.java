package cn.pioneeruniverse.common.config.xss;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map.Entry;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.utils.JsoupUtils;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 18:10 2019/5/15
 * @Modified By:
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    HttpServletRequest orgRequest = null;

    //判断是否是上传 上传忽略
    boolean isUpData = false;

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
        String contentType = request.getContentType();
        if (null != contentType) {
            isUpData = contentType.startsWith("multipart");
        }
    }

    /**
     * @param name
     * @return java.lang.String
     * @Description 覆盖getParameter方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取
     * @MethodName getParameter
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/15 18:15
     */
    @Override
    public String getParameter(String name) {
        name = JsoupUtils.cleanForXssHttpServletRequest(name);
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            //value = StringEscapeUtils.escapeHtml4(value);
            value = JsoupUtils.cleanForXssHttpServletRequest(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] arr = super.getParameterValues(name);
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                //arr[i] = StringEscapeUtils.unescapeHtml4(arr[i]);
                arr[i] = JsoupUtils.cleanForXssHttpServletRequest(arr[i]);
            }
        }
        return arr;
    }

    @Override
    public String getHeader(String name) {
        name = JsoupUtils.cleanForXssHttpServletRequest(name);
        String value = super.getHeader(name);
        if (StringUtils.isNotBlank(value)) {
            //value = StringEscapeUtils.escapeHtml4(value);
            value = JsoupUtils.cleanForXssHttpServletRequest(value);
        }
        return value;
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (isUpData) {
            return super.getInputStream();
        } else {
            //处理原request的流中的数据
            byte[] bytes = inputHandlers(super.getInputStream()).getBytes();
            final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return bais.read();
                }
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }
            };
        }

    }

    public String inputHandlers(ServletInputStream servletInputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(servletInputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (servletInputStream != null) {
                try {
                    servletInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String finl = JsoupUtils.cleanForXssJson(sb.toString());
        return finl;
    }

    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) req).getOrgRequest();
        }
        return req;
    }




}
