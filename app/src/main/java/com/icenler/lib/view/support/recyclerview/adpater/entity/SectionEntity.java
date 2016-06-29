package com.icenler.lib.view.support.recyclerview.adpater.entity;

public abstract class SectionEntity<T> {

    public boolean isHeader;
    public String header;
    public T t;

    public SectionEntity(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.t = null;
    }

    public SectionEntity(T t) {
        this.isHeader = false;
        this.header = null;
        this.t = t;
    }
}
