package com.taoding.common.response;


import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.JsonUtil;

/**
 * 请求和响应处理器，请求代码时进行Bean验证，
 * 控制器响应后将返回的业务对象转换为前台可以识别的结果
 * @author vincent
 *
 */
@Aspect
@Component
public class ResponseInterceptor {

    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private Validator validator = validatorFactory.getValidator();
    
    private static final Logger logger = LoggerFactory.getLogger(ResponseInterceptor.class);

    @Around("execution(* com.taoding.controller..*Controller.*(..))")
    public Object responseHandle(ProceedingJoinPoint pjp) {
        Response response = new Response();
        
        String validationResult = validateBean(pjp.getArgs());
        try {
        	if (null != validationResult) {
                response.setMessage(validationResult);
                response.setResult(null);
                response.setStatus(ResponseCode.INVALID.getValue());
            } else {
            	Object obj = pjp.proceed();
                response.setMessage(ResponseCode.OK.getDescription());
                response.setResult(obj);
                response.setStatus(ResponseCode.OK.getValue());
            }
        } catch (Throwable throwable) {
        	handleException(throwable, response);
        }
        
        //添加暴露前台相应 add lixc 2017年11月14日17:25:28
		HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getResponse();
		if(null!= res){
			res.addHeader("Access-Control-Expose-Headers", "Authorization");
		}
        return JsonUtil.convertResponseToJson(response);
    }

    /**
     * 查看参数列表中的参数是否需要bean校验，如果需要，则进行校验，并返回错误信息
     * @param objects 参数列表
     * @return null表示较验正常，否则返回第一个校验的错误
     */
    @SuppressWarnings("rawtypes")
    private String validateBean(Object[] objects) {
        if (null == objects || objects.length == 0){
            return null;
        }

        String message = null;

        for (Object obj : objects) {
        	if(null == obj){
        		continue;	
        	}
        	
            //遍历参数列表，查看参数是否被com.taoding.common.annotation.ValidationBean声明，
            //如果是，说明该参数需要bean校验
            int length = obj.getClass().getAnnotationsByType(ValidationBean.class).length;
            if (length > 0) {
                Set<ConstraintViolation<Object>> validateResults = validator.validate(obj);
                if (!validateResults.isEmpty()) {
                    for(ConstraintViolation result: validateResults) {
                        message = result.getMessage();
                        break;
                    }
                }
            }
        }
        return message;
    }
    
    /**
     * 处理异常
     * @param throwable 异常
     * @param response 结果
     */
    public static void handleException(Throwable throwable, Response response) {
    	throwable.printStackTrace();
    	logger.error(throwable.getMessage());
        if (throwable.getClass().equals(LogicException.class)) {
        	response.setMessage(((LogicException)throwable).getMessageInfo());
        	response.setResult("");
            response.setStatus(ResponseCode.LOGIC_EXCEPTION.getValue());
        } else {
        	response.setMessage(ResponseCode.ERROR.getDescription());
        	response.setResult("");
            response.setStatus(ResponseCode.ERROR.getValue());
        }
    }
}
