package com.shudieds.log.storage.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class RunTimeLogConvert {
    private String id;
    private String dcId;
    private Integer linkEnv;
    private String sceneCode;
    private String canvasNodeCode;
    private String orgCode;
    private String executionTime;
    private String taskBeginTime;
    private String sourceBillCode;
    private String targetBillCode;
    private String returnMsg;
    private String timestamp;

    public Integer getLinkEnv() {
        return linkEnv;
    }
    @JsonProperty(value = "link_env")
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
    @JsonProperty(value = "dc_id")
    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public String getSceneCode() {
        return sceneCode;
    }
    @JsonProperty(value = "scene_code")
    public void setSceneCode(String sceneCode) {
        this.sceneCode = sceneCode;
    }

    public String getCanvasNodeCode() {
        return canvasNodeCode;
    }

    @JsonProperty(value = "canvas_node_code")
    public void setCanvasNodeCode(String canvasNodeCode) {
        this.canvasNodeCode = canvasNodeCode;
    }

    public String getOrgCode() {
        return orgCode;
    }
    @JsonProperty(value = "org_code")
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getExecutionTime() {
        return executionTime;
    }
    @JsonProperty(value = "execution_time")
    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getTaskBeginTime() {
        return taskBeginTime;
    }
    @JsonProperty(value = "task_begin_time")
    public void setTaskBeginTime(String taskBeginTime) {
        this.taskBeginTime = taskBeginTime;
    }

    public String getSourceBillCode() {
        return sourceBillCode;
    }
    @JsonProperty(value = "source_bill_code")
    public void setSourceBillCode(String sourceBillCode) {
        this.sourceBillCode = sourceBillCode;
    }

    public String getTargetBillCode() {
        return targetBillCode;
    }
    @JsonProperty(value = "target_bill_code")
    public void setTargetBillCode(String targetBillCode) {
        this.targetBillCode = targetBillCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }
    @JsonProperty(value = "return_msg")
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
