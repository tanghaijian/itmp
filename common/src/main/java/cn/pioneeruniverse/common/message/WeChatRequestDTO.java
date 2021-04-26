package cn.pioneeruniverse.common.message;

import java.util.Map;

public class WeChatRequestDTO {

	/**
	 * 请求Head
	 **/
	private Map<String, String> requestHead;//请求Head

	/**
	 * 请求body
	 **/
	private Map<String, Object> requestBody;//请求body

	public Map<String, String> getRequestHead() {
		return requestHead;
	}

	public void setRequestHead(Map<String, String> requestHead) {
		this.requestHead = requestHead;
	}

	public Map<String, Object> getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(Map<String, Object> requestBody) {
		this.requestBody = requestBody;
	}

}
