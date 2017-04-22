package com.skp.entity;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class BaseSplittedEntry {
    private Integer dbIndex;
    private Integer tableIndex;

    public Integer getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(Integer dbIndex) {
        this.dbIndex = dbIndex;
    }

    public Integer getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(Integer tableIndex) {
        this.tableIndex = tableIndex;
    }

    public BaseSplittedEntry() {
        super();
    }

    public BaseSplittedEntry(Integer dbIndex, Integer tableIndex) {
        super();
        this.dbIndex = dbIndex;
        this.tableIndex = tableIndex;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
