package com.dubbo.shop.pojo;

import java.util.List;

public class wangEditorBean {

    private int errno;

    private List<String> data;


    public static wangEditorBean success(List<String> images){
        wangEditorBean item = new wangEditorBean();
        item.setErrno(0);
        item.setData(images);

        return item;
    }

    public static wangEditorBean error(List<String> images){
        wangEditorBean item = new wangEditorBean();
        item.setErrno(1);

        return item;
    }


    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
