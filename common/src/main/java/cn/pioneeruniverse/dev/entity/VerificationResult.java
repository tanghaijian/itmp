package cn.pioneeruniverse.dev.entity;

public class VerificationResult {


	/**
	 * 状态
	 **/
	private int status;

	/**
	 * sql
	 **/
	private String sql;

	/**
	 * 信息
	 **/
	private String message;

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
