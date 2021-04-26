package cn.pioneeruniverse.common.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;


/**
 * 邮件短信工具类(由于短信微信接口在一起，后加;类命名就不改了)
 * @author lid
 * @date 2018年10月29日上午10:39:59
 * @version 1.0
 */
public class WechatUtil {
	private final static Logger log = LoggerFactory.getLogger(WechatUtil.class);
	
	private String wechatESBRestUrl;//微信 url
	private Integer wechatTimeout;//超时

	/**
	 * 创建weixn对象 messageMap放入目标id和消息内容
	 * {"requestBody":{"messageType":"1","channel":"weixin","subChannel":"009","targetType":"1","priority":"1",
	 * "content":"消息消息消息消息消息消息","comcode":1,"target":"12343,55555,22222"},"requestHead":{"consumerID":"itmgr"}}
	 * @param messageMap
	 * @return
	 * @throws IOException
	 * @thorws
	 */
	public WeChatRequestDTO createWechat(String wechatESBRestUrl, String wechatTimeout, String consumerID, String channel, String subChannel, 
			List<String> targetList, String content) throws IOException {
		this.wechatESBRestUrl = wechatESBRestUrl;
		this.wechatTimeout = Integer.parseInt(wechatTimeout);
		
		WeChatRequestDTO weChatRequestDto = new WeChatRequestDTO();
		// 头部
		HashMap<String, String> requestHead = new HashMap<String, String>();
		requestHead.put("consumerID", consumerID);// 服务消费方ID
		weChatRequestDto.setRequestHead(requestHead);
		// body
		HashMap<String, Object> requestBody = new HashMap<String, Object>();
		requestBody.put("channel", channel);// 通道
		requestBody.put("subChannel", subChannel);// 子通道 itmgr:017
		requestBody.put("comcode", 1);// 调用方的机构代码
		requestBody.put("messageType", "text");// 消息类型
		requestBody.put("targetType", "1");// 发送目标类型
		requestBody.put("priority", "1");// 消息优先级
		requestBody.put("target", targetList);// 发送目标ID列表
		requestBody.put("content", content);// 消息内容
		weChatRequestDto.setRequestBody(requestBody);
		return weChatRequestDto;
	}
	
	public boolean sendWeChat(WeChatRequestDTO wechatRequest) {
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		boolean success = true;
		try {
			//String requestParam = URLEncodedUtils.format(requestJsonStr, "UTF-8");
			String requestParam = JSON.toJSONString(wechatRequest);
			//String wechatESBUrlStr = "http://10.1.11.115:8080/daditestgroup/ccicsit/sendsimplemessageservicerest/Restful";
			URL url = new URL(wechatESBRestUrl);
			// 2. 创建HttpURLConnection对象
			HttpURLConnection  connection =  (HttpURLConnection) url.openConnection();
			// 请求方式
			connection.setRequestMethod("POST");
			// 设置连接主机超时（单位：毫秒）
			connection.setConnectTimeout(wechatTimeout);
			// 设置从主机读取数据超时（单位：毫秒）
			connection.setReadTimeout(wechatTimeout);
			// 设置是否输出
			connection.setDoOutput(true);
			// 设置是否读入
			connection.setDoInput(true);
			// 设置是否使用缓存
			connection.setUseCaches(false);
			// 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
			connection.setInstanceFollowRedirects(true);
			// 设置使用标准编码格式编码参数的名-值对
			connection.setRequestProperty("Content-Type", "application/json");
			// 连接
			connection.connect();
			// 写入参数到请求中
			if (null != requestParam) {
				outputStream = connection.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(requestParam.getBytes("UTF-8"));
			}

			StringBuffer buffer = new StringBuffer();
			// 从连接中读取响应信息
			int code = connection.getResponseCode();
			if (code == 200) {
				String str = "";
				inputStream = connection.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
				bufferedReader = new BufferedReader(inputStreamReader);
				while ((str = bufferedReader.readLine()) != null) {
					buffer.append(str);
				}
				// 5. 断开连接
				connection.disconnect();
				log.info("发送微信返回结果=====>{}", JSON.toJSONString(buffer.toString()));
			} else {
				success = false;
			}
			
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
			log.error("WechatUtil.sendWeChat微信发送失败：",e);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}
	
}



