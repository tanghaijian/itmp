package cn.pioneeruniverse.job.exception;

/**
 * 
* @ClassName: BaseException
* @Description: 异常封装-基础异常
* @author author
* @date 2020年8月24日 下午2:51:07
*
 */
public class BaseException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4814572689574826814L;
	/**
     * 异常对应的错误类型
     */
    private final ErrorType errorType;

    /**
     * 默认是系统异常
     */
    public BaseException() {
        this.errorType = SystemErrorType.SYSTEM_ERROR;
    }

    public BaseException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

	public ErrorType getErrorType() {
		return errorType;
	}
}
