package com.taoding.common.utils;

import java.io.Serializable;

/**
 * Created by yaochenglong on 2017/12/1.
 * 返回前端的数据格式载体
 */
public class AjaxJson implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5402580199710609179L;
	
	public boolean success = true;
    public String msg;
    public Object data;


    public AjaxJson() {
    }

    public AjaxJson(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
