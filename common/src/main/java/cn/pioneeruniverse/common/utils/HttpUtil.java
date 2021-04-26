package cn.pioneeruniverse.common.utils;

import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 公众平台通用接口工具类
 */
public class HttpUtil {
    private static Logger log = Logger.getLogger(HttpUtil.class);

    /**
     * 忽视证书HostName
     */
    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String s, SSLSession sslsession) {
            return true;
        }
    };

    /**
     * Ignore Certification
     */

    private static TrustManager ignoreCertificationTrustManger = new X509TrustManager() {
        private X509Certificate[] certificates;

        public void checkClientTrusted(X509Certificate certificates[],
                                       String authType) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = certificates;
            }
        }

        public void checkServerTrusted(X509Certificate[] ax509certificate,
                                       String s) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = ax509certificate;
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static String httpRequest(String requestUrl, String requestMethod,
                                     String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader =null;
        BufferedReader bufferedReader =null;
        OutputStream outputStream =null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url
                    .openConnection();
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            // if ("GET".equalsIgnoreCase(requestMethod))
            httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                 outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.flush();
                //outputStream.close();
            }

            // 将返回的输入流转换成字符串
            httpUrlConn.getContentLength();
            httpUrlConn.getContentType();
            // log.info(httpUrlConn.getHeaderFields());
             inputStream = httpUrlConn.getInputStream();
             inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
             bufferedReader = new BufferedReader(
                    inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            /*bufferedReader.close();
            inputStreamReader.close();*/
          
            inputStream = null;
            httpUrlConn.disconnect();

        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }finally {
        	 // 释放资源
            try {
            	 if (null != outputStream) {
                     outputStream.close();
                 }
            	 if(null!=bufferedReader) {
            		 bufferedReader.close();
            	 }
            	if(null!=inputStreamReader) {
            		inputStreamReader.close();
            	}
            	if(null!=inputStream) {
            		inputStream.close();
            	}
			} catch (IOException e) {
				log.error("inputStream close fail");
			}
        }
        return buffer.toString();
    }

    public static JSONObject httpsRequest(String requestUrl,
                                          String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream =null;
        OutputStream outputStream =null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            // Proxy proxy = new Proxy(Proxy.Type.HTTP, new
            // InetSocketAddress("182.1.1.200",80));
            URL url = new URL(requestUrl);
            // HttpsURLConnection httpsUrlConn = (HttpsURLConnection)
            // url.openConnection(proxy);
            HttpsURLConnection httpsUrlConn = (HttpsURLConnection) url
                    .openConnection();
            httpsUrlConn.setSSLSocketFactory(ssf);

            httpsUrlConn.setDoOutput(true);
            httpsUrlConn.setDoInput(true);
            httpsUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpsUrlConn.setRequestMethod(requestMethod);
            httpsUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                 outputStream = httpsUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                //outputStream.close();
            }

            // 将返回的输入流转换成字符串
             inputStream = httpsUrlConn.getInputStream();
             inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
             bufferedReader = new BufferedReader(
                    inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            /*bufferedReader.close();
            inputStreamReader.close();*/
            // 释放资源
            //inputStream.close();
            inputStream = null;
            httpsUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.", ce);
        } catch (Exception e) {
            log.error("https request error:{" + e.getMessage() + "}", e);
        }finally {
        	// 释放资源
            try {
            	  // 当有数据需要提交时
                if (null != outputStream) {
                    outputStream.close();
                }
                if(null !=bufferedReader) {
                	bufferedReader.close();
                }
                if(null!=inputStreamReader) {
                	inputStreamReader.close();
                }
                if(null!=inputStream) {
                	inputStream.close();
                }
				
			} catch (IOException e) {
				log.error("inputStream close fail");
			}
		}
        return jsonObject;
    }

    /**
     * 发送POST请求
     *
     * @param url         请求地址url
     * @param params      需要发送的请求参数字符串
     * @param connTimeout 连接超时时间（毫秒），如果为null则默认为30秒
     * @param readTimeout 读取超时时间（毫秒），如果为null则默认为60秒
     * @return 请求响应内容，如果为null则表示请求异常
     */
    public static String doPost(String url, String params, Integer connTimeout, Integer readTimeout, String contentType) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        OutputStreamWriter oStreamWriter = null;
        InputStreamReader inputStreamReader = null;
        try {
            // 打开和URL之间的连接,根据url
            URLConnection conn = geneUrlConn(url);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", contentType == null ? "application/json" : contentType);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置请求超时时间和读取超时时间
            conn.setConnectTimeout(connTimeout == null ? 300 : connTimeout);
            conn.setReadTimeout(readTimeout == null ? 300 : readTimeout);
            // 获取URLConnection对象对应的输出流
             oStreamWriter =  new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            out = new PrintWriter(oStreamWriter);
            // 发送请求参数
            out.print(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
             inputStreamReader = new InputStreamReader(conn.getInputStream(), "utf-8");
            in = new BufferedReader(inputStreamReader);
            String line;
            while ((line = in.readLine()) != null)
                result += line;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("HTTP server connection timed out.");
            result = null;
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if(oStreamWriter!=null) {
                	oStreamWriter.close();
                }
                if(inputStreamReader!=null) {
                	inputStreamReader.close();
                }
            } catch (IOException ex) {
                log.error("HTTP server connection timed out.");
                ex.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 获取UrlConn
     */
    public static URLConnection geneUrlConn(String urlstr) throws Exception {
        URL url = new URL(urlstr);
        if (urlstr.startsWith("https://")) {
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            HttpsURLConnection httpsConnection = (HttpsURLConnection) url.openConnection();
            // Prepare SSL Context
            TrustManager[] tm = {ignoreCertificationTrustManger};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");

            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            httpsConnection.setSSLSocketFactory(ssf);
            return httpsConnection;
        } else {
            return url.openConnection();
        }

    }


    /**
     * @param
     * @return javax.net.ssl.SSLContext
     * @Description 绕过安全证书验证
     * @MethodName createIgnoreVerifySSL
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/11 16:51
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    /**
     * @param url
     * @param json
     * @param charset
     * @return java.lang.String
     * @Description httpclient请求(application/json)
     * @MethodName doPost
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/11 18:02
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String doPost(String url, String json, String charset){
        String result = null;
        CloseableHttpClient httpClient = null;
        try {
            // 采用绕过验证的方式处理https请求
            SSLContext sslcontext = createIgnoreVerifySSL();
            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);

            // 创建自定义的httpclient对象
            httpClient = HttpClients.custom().setConnectionManager(connManager).build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json;charset=" + charset);
            // 配置超时时间10分钟
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(600 * 1000)
                    .setConnectionRequestTimeout(600 * 1000).setSocketTimeout(600 * 1000).build();
            httpPost.setConfig(requestConfig);
            StringEntity se = new StringEntity(json,charset);
            se.setContentType("application/json");
            httpPost.setEntity(se);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            log.error(String.valueOf(ex.getMessage()), ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error(String.valueOf(e.getMessage()), e);
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return result;
    }
    
    /**
      * 发送DELETE请求
     * @param url         请求地址url
     * @param params      需要发送的请求参数字符串
     * @param connTimeout 连接超时时间（毫秒），如果为null则默认为10秒
     * @param readTimeout 读取超时时间（毫秒），如果为null则默认为10秒
     * @return 请求响应内容，如果为null则表示请求异常
     */
	public static Integer doDelete(String url, String params, Integer connTimeout, Integer readTimeout,
			String contentType, String username, String password) {
		PrintWriter printWriter = null;
		Integer result = null;
		OutputStreamWriter oStreamWriter = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		HttpURLConnection  httpUrlConn = null;
		try {
			// 打开和URL之间的连接,根据url
			httpUrlConn = (HttpURLConnection)geneUrlConn(url);
			// 设置通用的请求属性
//			httpUrlConn.setRequestProperty("accept", "*/*");
//			httpUrlConn.setRequestProperty("connection", "Keep-Alive");
//			httpUrlConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			httpUrlConn.setRequestProperty("Connection", "close");
			httpUrlConn.setRequestProperty("accept", "application/json");//设置接收返回参数格式
			httpUrlConn.setRequestProperty("Content-Length", "1000");//设置内容的长度
			httpUrlConn.setRequestProperty("Content-Type", contentType == null ? "application/json;charset=utf-8" : contentType);//设置编码格式
			httpUrlConn.setUseCaches(false);
			
			String auth = username + ":" + password;
			String res = new String(Base64.encodeBase64(auth.getBytes()));// 对其进行加密
			httpUrlConn.setRequestProperty("Authorization","Basic " + res);// 设置认证属性
			
			httpUrlConn.setRequestMethod("DELETE");
			// 发送POST请求必须设置如下两行
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			
			// 设置请求超时时间和读取超时时间
			httpUrlConn.setConnectTimeout(connTimeout == null ? 30000 : connTimeout);
			httpUrlConn.setReadTimeout(readTimeout == null ? 30000 : readTimeout);
			httpUrlConn.connect();
			// 获取URLConnection对象对应的输出流
			oStreamWriter = new OutputStreamWriter(httpUrlConn.getOutputStream(), "utf-8");
			printWriter = new PrintWriter(oStreamWriter);
			// 发送请求参数
			printWriter.print(params);
			// flush输出流的缓冲
			printWriter.flush();
			
            result = httpUrlConn.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("HTTP server connection timed out.");
			result = null;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (oStreamWriter != null) {
					oStreamWriter.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (httpUrlConn != null) {
					httpUrlConn.disconnect();
				}
			} catch (IOException ex) {
				log.error("HTTP server connection timed out.");
				ex.printStackTrace();
			}
		}
		return result;
	}
    
    /**
     * 通过URL网址提供如同浏览器下载的功能。
     * @param request
     * @param response
     * @param fileName
     * @param downloadUrl
     * @return
     * @throws Exception
     */
    public static String downloadPackage(HttpServletRequest request,HttpServletResponse response, String fileName, String downloadUrl) throws Exception{
		InputStream inStream = null;
		try {
			URL url = new URL(downloadUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置超时间为3秒
			conn.setConnectTimeout(3 * 1000);
			// 防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 得到输入流
			inStream = conn.getInputStream();

			// 获得浏览器代理信息
			final String userAgent = request.getHeader("USER-AGENT");
			// 判断浏览器代理并分别设置响应给浏览器的编码格式
			String finalFileName = null;
			if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident")) {// IE浏览器
				finalFileName = URLEncoder.encode(fileName, "UTF8");
			} else if (StringUtils.contains(userAgent, "Mozilla")) {// google,火狐浏览器
				finalFileName = new String(fileName.getBytes(), "ISO8859-1");
			} else {
				finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
			}
			// 设置HTTP响应头
			response.reset();// 重置 响应头
			response.setContentType("application/x-download");// 告知浏览器下载文件，而不是直接打开，浏览器默认为打开
			response.addHeader("Content-Disposition", "attachment;filename=\"" + finalFileName + "\"");// 下载文件的名称

			// 循环取出流中的数据
			byte[] b = new byte[1024];
			int len;
			while ((len = inStream.read(b)) > 0) {
				response.getOutputStream().write(b, 0, len);
			}
			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

    public static String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
