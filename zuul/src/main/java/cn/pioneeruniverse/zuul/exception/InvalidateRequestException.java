package cn.pioneeruniverse.zuul.exception;

import com.alibaba.fastjson.JSONObject;

public class InvalidateRequestException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2723285328184679950L;

	private String code;
	
	private String message;
	
	public InvalidateRequestException(String code,String message){
		super(message);
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString(){
		
		JSONObject obj = new JSONObject();
		obj.put("code", this.code);
		obj.put("message", this.message);
		return obj.toJSONString();
	}
}
