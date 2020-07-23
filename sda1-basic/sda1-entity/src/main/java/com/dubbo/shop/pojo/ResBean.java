package com.dubbo.shop.pojo;

import java.io.Serializable;

// 描述服务器给客户端的返回结果
public class ResBean implements Serializable {

    // 状态码
    private Integer statusCode;

    // 成功数据
    private String data;

    // 失败返回的信息
    private String msg;

    public static ResBean success(String data){
        ResBean resBean = new ResBean();
        resBean.setStatusCode(200);
        resBean.setData(data);
        return resBean;
    }

    public static ResBean error(String msg){
        ResBean resBean = new ResBean();
        resBean.setStatusCode(500);
        resBean.setMsg(msg);
        return resBean;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
