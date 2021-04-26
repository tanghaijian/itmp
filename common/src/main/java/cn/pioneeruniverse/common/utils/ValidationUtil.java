package cn.pioneeruniverse.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
* 数据校验 
* @author:tingting
* @version:2018年11月22日 下午3:36:30 
*/
@ControllerAdvice
public class ValidationUtil extends ResponseEntityExceptionHandler{
	
	 private Logger logger = LoggerFactory.getLogger(getClass());

	    @Override
	    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

	        Map<String, String> messages = new HashMap<>();
	        BindingResult result = ex.getBindingResult();
	        if (result.hasErrors()) {
	            List<ObjectError> errors = result.getAllErrors();
	            for (ObjectError error : errors) {
	                FieldError fieldError = (FieldError) error;
	                messages.put(fieldError.getField(), fieldError.getDefaultMessage());
	            }
	            logger.error(messages.toString());
	        }
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
	    }
	    
	  /*  Hibernate Validator提供的校验注解：  
	    @NotBlank(message =)   验证字符串非null，且trim后长度必须大于0    
	    @Email  被注释的元素必须是电子邮箱地址    
	    @Length(min=,max=)  被注释的字符串的大小必须在指定的范围内    
	    @NotEmpty   被注释的字符串的必须非空    
	    @Range(min=,max=,message=)  被注释的元素必须在合适的范围内
	    @URL  合法的url
	    */
	   

}
