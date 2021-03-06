package com.shudieds.log.storage.bean;

import java.util.List;
import java.util.Map;

public class RunTimeLogParams {
    private String id;
    private String dcId;
    private Integer linkEnv;
    private List<String> sceneCode;
    private String canvasNodeCode;
    private List<String> orgCode;
    private String executionTimeStart;
    private String executionTimeEnd;
    private String taskBeginTimeStart;
    private String taskBeginTimeEnd;
    private String sourceBillCode;
    private String targetBillCode;
    private String returnMsg;
    private String timestampStart;
    private String timestampEnd;
    private Map<String, String> sort;
    private int pageNum;
    private int pageSize;

    public Integer getLinkEnv() {
        return linkEnv;
    }

    public void setLinkEnv(Integer linkEnv) {
        this.linkEnv = linkEnv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDcId() {
        return dcId;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public List<String> getSceneCode() {
        return sceneCode;
    }

    public void setSceneCode(List<String> sceneCode) {
        this.sceneCode = sceneCode;
    }

    public String getCanvasNodeCode() {
        return canvasNodeCode;
    }

    public void setCanvasNodeCode(String canvasNodeCode) {
        this.canvasNodeCode = canvasNodeCode;
    }

    public List<String> getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(List<String> orgCode) {
        this.orgCode = orgCode;
    }

    public String getExecutionTimeStart() {
        return executionTimeStart;
    }

    public void setExecutionTimeStart(String executionTimeStart) {
        this.executionTimeStart = executionTimeStart;
    }

    public String getExecutionTimeEnd() {
        return executionTimeEnd;
    }

    public void setExecutionTimeEnd(String executionTimeEnd) {
        this.executionTimeEnd = executionTimeEnd;
    }

    public String getTaskBeginTimeStart() {
        return taskBeginTimeStart;
    }

    public void setTaskBeginTimeStart(String taskBeginTimeStart) {
        this.taskBeginTimeStart = taskBeginTimeStart;
    }

    public String getTaskBeginTimeEnd() {
        return taskBeginTimeEnd;
    }

    public void setTaskBeginTimeEnd(String taskBeginTimeEnd) {
        this.taskBeginTimeEnd = taskBeginTimeEnd;
    }

    public String getSourceBillCode() {
        return sourceBillCode;
    }

    public void setSourceBillCode(String sourceBillCode) {
        this.sourceBillCode = sourceBillCode;
    }

    public String getTargetBillCode() {
        return targetBillCode;
    }

    public void setTargetBillCode(String targetBillCode) {
        this.targetBillCode = targetBillCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
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

    public Map<String, String> getSort() {
        return sort;
    }

    public void setSort(Map<String, String> sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "RunTimeLogParams{" +
                "id='" + id + '\'' +
                ", dcId='" + dcId + '\'' +
                ", sceneCode=" + sceneCode +
                ", canvasNodeCode='" + canvasNodeCode + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", executionTimeStart='" + executionTimeStart + '\'' +
                ", executionTimeEnd='" + executionTimeEnd + '\'' +
                ", taskBeginTimeStart='" + taskBeginTimeStart + '\'' +
                ", taskBeginTimeEnd='" + taskBeginTimeEnd + '\'' +
                ", sourceBillCode='" + sourceBillCode + '\'' +
                ", targetBillCode='" + targetBillCode + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", timestampStart='" + timestampStart + '\'' +
                ", timestampEnd='" + timestampEnd + '\'' +
                ", sort=" + sort +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
