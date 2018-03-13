package com.taoding.ocr.module;

/**
 * Created by Administrator on 2017/11/23.
 */
public class OCREntity {
    private int errno;
    private String msg;
    private Words data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Words getData() {
        return data;
    }

    public void setData(Words data) {
        this.data = data;
    }
}
