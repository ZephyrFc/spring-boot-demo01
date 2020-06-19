package com.shudieds.log.storage.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "bill_log_index",shards = 3,replicas = 1)
public class BillLog {
    @Id
    //唯一ID，用的uuid
    private String id;
    //场景编码
    private String sceneCode;
    //节点编码
    private String nodeCode;
    //同步时间
    private String syncTime;
    //成功记录数
    private int successCount;
    //失败记录数
    private int failCount;
    //DCId
    private String dcId;
    //数据类型0:读 1:写
    private int dataType;
    //错误信息
    private String errMsg;
    //任务id
    private String taskId;
    //环境信息 1 测试环境 2 正式环境
    private int linkEnv;
    //创建时间
    private String timestamp;
    //同步时间戳
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
    public void setSceneCode(String sceneCode) {

        this.sceneCode = sceneCode;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(String syncTime) {
        this.syncTime = syncTime;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {

        this.successCount = successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public String getDcId() {
        return dcId;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getLinkEnv() {
        return linkEnv;
    }

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
