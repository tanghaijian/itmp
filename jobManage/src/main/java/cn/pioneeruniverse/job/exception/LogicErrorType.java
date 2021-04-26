package cn.pioneeruniverse.job.exception;

/**
 * 
* @ClassName: LogicErrorType
* @Description: 业务逻辑方面的错误代码，封装于此
* @author author
* @date 2020年8月24日 下午2:51:37
*
 */
public enum LogicErrorType implements ErrorType{
	
	 UNAUTHORIZED("040000","请求缺少token"),
	 AUTHORIZED_EXPIRE("040001","token不存在或已经过期"),
	    
	 PERMISSION_UNAUTHORIZED("050000","未授权"),
	    
	 USER_NOT_EXISTS("060000","用户不存在"),
	 USER_PASS_NOT_MATCH("060001","用户或密码错误");

    private String code;
    private String mesg;

    LogicErrorType(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMesg() {
		return mesg;
	}

	public void setMesg(String mesg) {
		this.mesg = mesg;
	}
}
