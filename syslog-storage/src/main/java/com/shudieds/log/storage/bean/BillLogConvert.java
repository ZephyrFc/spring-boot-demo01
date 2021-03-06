package com.shudieds.log.storage.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillLogConvert {
    private String id;
    private String sceneCode;
    private String nodeCode;
    private String syncTime;
    private int successCount;
    private int failCount;
    private String dcId;
    private int dataType;
    private String errMsg;
    private String taskId;
    private int linkEnv;
    private String timestamp;
    private int syncTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSceneCode() {
        return sceneCode;
    }
    @JsonProperty(value = "scene_code")
    public void setSceneCode(String sceneCode) {

        this.sceneCode = sceneCode;
    }

    public String getNodeCode() {
        return nodeCode;
    }
    @JsonProperty(value = "node_code")
    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getSyncTime() {
        return syncTime;
    }
    @JsonProperty(value = "sync_time")
    public void setSyncTime(String syncTime) {
        this.syncTime = syncTime;
    }

    public int getSuccessCount() {
        return successCount;
    }
    @JsonProperty(value = "success_count")
    public void setSuccessCount(int successCount) {

        this.successCount = successCount;
    }

    public int getFailCount() {
        return failCount;
    }
    @JsonProperty(value = "fail_count")
    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public String getDcId() {
        return dcId;
    }

    @JsonProperty(value = "dcid")
    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public int getDataType() {
        return dataType;
    }
    @JsonProperty(value = "data_type")
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getErrMsg() {
        return errMsg;
    }
    @JsonProperty(value = "err_msg")
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getLinkEnv() {
        return linkEnv;
    }
    @JsonProperty(value = "link_env")
    public void setLinkEnv(int linkEnv) {
        this.linkEnv = linkEnv;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTaskId() {
        return taskId;
    }
    @JsonProperty(value = "task_id")
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getSyncTimestamp() {
        return syncTimestamp;
    }

    public void setSyncTimestamp(int syncTimestamp) {
        this.syncTimestamp = syncTimestamp;
    }
}
