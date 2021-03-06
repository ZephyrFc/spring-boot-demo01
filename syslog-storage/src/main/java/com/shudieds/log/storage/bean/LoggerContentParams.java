package com.shudieds.log.storage.bean;

import java.util.Map;

public class LoggerContentParams {
    private String id;
    private String dc;
    private String scene;
    private String timestampStart;
    private String timestampEnd;
    private String spanId;
    private String traceId;
    private String content;
    private String loggerName;
    private String threadName;
    private String level;
    private String sysName;
    private String org;
    private String task;
    private Map<String,String> sort;
    private int pageNum;
    private int pageSize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getTimestampStart() {
        return timestampStart;
    }

    public void setTimestampStart(String timestampStart) {
        this.timestampStart = timestampStart;
    }

    public String getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(String timestampEnd) {
        this.timestampEnd = timestampEnd;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Map<String, String> getSort() {
        return sort;
    }

    public void setSort(Map<String, String> sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "LoggerContentParams{" +
                "id='" + id + '\'' +
                ", dc='" + dc + '\'' +
                ", scene='" + scene + '\'' +
                ", timestampStart='" + timestampStart + '\'' +
                ", timestampEnd='" + timestampEnd + '\'' +
                ", spanId='" + spanId + '\'' +
                ", traceId='" + traceId + '\'' +
                ", content='" + content + '\'' +
                ", loggerName='" + loggerName + '\'' +
                ", threadName='" + threadName + '\'' +
                ", level='" + level + '\'' +
                ", sysName='" + sysName + '\'' +
                ", org='" + org + '\'' +
                ", task='" + task + '\'' +
                ", sort=" + sort +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
