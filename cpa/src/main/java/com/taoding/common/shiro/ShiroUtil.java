package com.taoding.common.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taoding.common.utils.JsonUtil;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/11/13.
 */
public class ShiroUtil {

    /**
     * 是否是Ajax请求
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request){
        return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
    }

    /**
     * 统一返回前端json数据
     *
     * @param response
     * @param data
     */
    public static void writeResponse(HttpServletResponse response, Object data) {
        try {
            response.setContentType("application/json");
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(JsonUtil.objectToJson(data).getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
