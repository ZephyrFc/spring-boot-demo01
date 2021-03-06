package com.shudieds.log.storage.bean;

import java.util.List;
import java.util.Map;

public class BillLogParams {
    private String id;
    private String sceneCode;
    private List<String> nodeCode;
    private String syncTimeStart;
    private String syncTimeEnd;
    private Integer successCount;
    private Integer failCount;
    private String dcId;
    private Integer dataType;
    private String errMsg;
    private String taskId;
    private Integer linkEnv;
    private String timestampStart;
    private String timestampEnd;
    private Map<String,String> sort;
    private int pageNum;
    private int pageSize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSceneCode() {
        return sceneCode;
    }

    public void setSceneCode(String sceneCode) {
        this.sceneCode = sceneCode;
    }

    public List<String> getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(List<String> nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getSyncTimeStart() {
        return syncTimeStart;
    }

    public void setSyncTimeStart(String syncTimeStart) {
        this.syncTimeStart = syncTimeStart;
    }

    public String getSyncTimeEnd() {
        return syncTimeEnd;
    }

    public void setSyncTimeEnd(String syncTimeEnd) {
        this.syncTimeEnd = syncTimeEnd;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public String getDcId() {
        return dcId;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getLinkEnv() {
        return linkEnv;
    }

    public void setLinkEnv(Integer linkEnv) {
        this.linkEnv = linkEnv;
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

    public Map<String, String> getSort() {
        return sort;
    }

    public void setSort(Map<String, String> sort) {
        this.sort = sort;
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

    @Override
    public String toString() {
        return "BillLogParams{" +
                "id='" + id + '\'' +
                ", sceneCode='" + sceneCode + '\'' +
                ", nodeCode='" + nodeCode + '\'' +
                ", syncTimeStart='" + syncTimeStart + '\'' +
                ", syncTimeEnd='" + syncTimeEnd + '\'' +
                ", successCount=" + successCount +
                ", failCount=" + failCount +
                ", dcId='" + dcId + '\'' +
                ", dataType=" + dataType +
                ", errMsg='" + errMsg + '\'' +
                ", taskId='" + taskId + '\'' +
                ", linkEnv=" + linkEnv +
                ", timestampStart='" + timestampStart + '\'' +
                ", timestampEnd='" + timestampEnd + '\'' +
                ", sort=" + sort +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
