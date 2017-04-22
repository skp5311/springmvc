package com.skp.entity;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Mobile2Uid {
    private int     id;
    private String  mobileNo;
    private Integer uid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Mobile2Uid() {
        super();
    }

    public Mobile2Uid(String mobileNo, Integer uid) {
        super();
        this.mobileNo = mobileNo;
        this.uid = uid;
    }

    public Mobile2Uid(int id, String mobileNo, Integer uid) {
        super();
        this.id = id;
        this.mobileNo = mobileNo;
        this.uid = uid;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
