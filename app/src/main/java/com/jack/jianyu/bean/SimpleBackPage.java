package com.jack.jianyu.bean;

/**
 * author:S.jack
 * data:2016-01-11 17:34
 */


public enum SimpleBackPage {

    ;

    private int values;
    private int title;
    private Class<?> cls;

    private SimpleBackPage(int values, int title, Class<?> cls) {
        this.values = values;
        this.title = title;
        this.cls = cls;
    }

    public int getValues() {
        return values;
    }

    public void setValues(int values) {
        this.values = values;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public static SimpleBackPage getPageValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValues() == val) {
                return p;
            }
        }
        return null;
    }
}
