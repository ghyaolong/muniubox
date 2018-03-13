package com.taoding.common.shiro;

/**
 * Created by Administrator on 2017/11/13.
 */
/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年11月14日 下午2:52:21
 * <p>
 * Company: 淘丁集团
 * <p>
 * @author yaochenglong
 * @version 1.0.0
 */
public class Result {

    private Integer code;

    private String msg;

    public Result(AuthorizationStatus status)
    {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

   
}
