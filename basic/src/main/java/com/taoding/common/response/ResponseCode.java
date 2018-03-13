package com.taoding.common.response;

/**
 * 响应代码枚举类，响应响和对应的代码
 * @author Vincent
 *
 */
public enum ResponseCode {

	//正常
    OK("OK", 20000),
    
    //后台错误
    ERROR("ERROR", 50000),
    
    //后台验证失败
    INVALID("VALIDATION_ERROR", 50001),
    
    //无效的Token
    TOKEN_INVALID("TOKEN_INVALID", 50008),
    
    //Token已过期
    TOKEN_EXPIRE("TOKEN_EXPIRE", 50014),
	
	//业务逻辑异常
	LOGIC_EXCEPTION("LOGIC_EXCEPTION", 50002);
	
    private String statusDesc;

    private int statusCode;

    private ResponseCode(String statusDesc, int statusCode) {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }

    public int getValue() {
        return this.statusCode;
    }
    
    public String getDescription() {
    	return this.statusDesc;
    }
    
}
