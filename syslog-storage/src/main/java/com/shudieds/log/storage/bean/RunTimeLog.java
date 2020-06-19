package com.shudieds.log.storage.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "run_time_log_index",shards = 3,replicas = 1)
public class RunTimeLog {
    @Id
    //唯一ID
    private String id;
    //DCId
    private String dcId;
    //环境 1 测试 2 正式
    private Integer linkEnv;
    //场景编码
    private String sceneCode;
    //节点编码
    private String canvasNodeCode;
    //组织编码
    private String orgCode;
    //执行时间
    private String executionTime;
    //任务开始时间
    private String taskBeginTime;
    //原单号
    private String sourceBillCode;
    //目标单号
    private String targetBillCode;
    //返回的错误信息
    private String returnMsg;
    //创建时间戳
    private String timestamp;

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

    public String getSceneCode() {
        return sceneCode;
    }

    public void setSceneCode(String sceneCode) {
        this.sceneCode = sceneCode;
    }

    public String getCanvasNodeCode() {
        return canvasNodeCode;
    }

    public void setCanvasNodeCode(String canvasNodeCode) {
        this.canvasNodeCode = canvasNodeCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getTaskBeginTime() {
        return taskBeginTime;
    }

    public void setTaskBeginTime(String taskBeginTime) {
        this.taskBeginTime = taskBeginTime;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
