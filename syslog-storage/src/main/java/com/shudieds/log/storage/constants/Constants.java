package com.shudieds.log.storage.constants;

import com.google.common.collect.Lists;

import java.util.List;

public class Constants {
    public static final String RUN_TIME_LOG = "runtime_log";
    public static final String BILL_LOG = "bill_log";
    public static final String LOG_TYPE = "log_type";
    public static final String DATA = "data";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String KAFKA_APPENDER = "kafka-appender";
    public static final String CONTENT_TYPE = "application/octet-stream;charset=UTF-8";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_DISPOSITION_VAL = "attachment;filename=logback-spring.xml";
    public static final String RUN_TIME_LOG_INDEX = "run_time_log_index";
    public static final String BILL_LOG_INDEX = "bill_log_index";
    public static final String LOGGER_CONTENT_INDEX = "logger_content_index";
    public static final String CONTENT_KEYWORD = "content.keyword";
    public static final String CONTENT = "content";
    public static final String TIMESTAMP = "timestamp";
    public static final String ID_KEYWORD = "id.keyword";
    public static final String SPANID_KEYWORD = "spanId.keyword";
    public static final String LOGGERNAME_KEYWORD = "loggerName.keyword";
    public static final String THREADNAME_KEYWORD = "threadName.keyword";
    public static final String LEVEL_KEYWORD = "level.keyword";
    public static final String ORG_KEYWORD = "org.keyword";
    public static final String ORGCODE_KEYWORD = "orgCode.keyword";
    public static final String TASK_KEYWORD = "task.keyword";
    public static final String SYSNAME_KEYWORD = "sysName.keyword";
    public static final String SUCCESSCOUNT = "successCount";
    public static final String FAILCOUNT = "failCount";
    public static final String EXECUTIONTIME_KEYWORD = "executionTime.keyword";
    public static final String EXECUTIONTIME_START = "executionTimeStart";
    public static final String EXECUTIONTIME_END = "executionTimeEnd";
    public static final String TASKBEGINTIME_KEYWORD = "taskBeginTime.keyword";
    public static final String TASKBEGINTIME_START = "taskBeginTimeStart";
    public static final String TASKBEGINTIME_END = "taskBeginTimeEnd";
    public static final String SOURCEBILLCODE_KEYWORD = "sourceBillCode.keyword";
    public static final String TARGETBILLCODE_KEYWORD = "targetBillCode.keyword";
    public static final String RETURNMSG_KEYWORD = "returnMsg.keyword";
    public static final String RETURNMSG = "returnMsg";
    public static final String TRACEID_KEYWORD = "traceId.keyword";
    public static final String DC_KEYWORD = "dc.keyword";
    public static final String SCENE_KEYWORD = "scene.keyword";
    public static final String ERRMSG_KEYWORD = "errMsg.keyword";
    public static final String ERRMSG = "errMsg";
    public static final String TASKID_KEYWORD = "taskId.keyword";
    public static final String TASKID = "taskId";
    public static final String SCENECODE_KEYWORD = "sceneCode.keyword";
    public static final String SCENECODE = "sceneCode";
    public static final String CANVASNODECODE_KEYWORD = "canvasNodeCode.keyword";
    public static final String DCID_KEYWORD = "dcId.keyword";
    public static final String DCID = "dcId";
    public static final String WILDCARD = "*";
    public static final String MYLOG = "mylog.xml";
    public static final String KAFKALOG = "kafka.xml";
    public static final String NAME = "name";
    public static final String APPENDER = "appender";
    public static final String LEVEL = "level";
    public static final String ROOT = "root";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String DC_XML = "dc";
    public static final String SYSNAME = "sysName";
    public static final String PLUS_EIGHT = "+8";
    public static final String NODECODE_KEYWORD = "nodeCode.keyword";
    public static final String NODECODE = "nodeCode";
    public static final String SYNCTIME_KEYWORD = "syncTime.keyword";
    public static final String SYNCTIME = "syncTime";
    public static final String SYNCTIME_START = "syncTimeStart";
    public static final String SYNCTIME_END = "syncTimeEnd";
    public static final String DATATYPE = "dataType";
    public static final String LINKENV = "linkEnv";
    public static final String TIMESTAMP_START = "timestampStart";
    public static final String TIMESTAMP_END = "timestampEnd";
    public static final String PAGESIZE = "pageSize";
    public static final String PAGENUM = "pageNum";
    public static final String KEYWORD = ".keyword";
    public static final String CLASS = "class";
    public static final int TIMESTAMP_TYPE = 1;
    public static final int MINORMAX_TYPE = 2;
    public static final String DESC = "desc";
    public static final String ASC = "asc";
    public static final String SYNCTIMESTAMP = "syncTimestamp";
    public static final List<String> FIELD_LIST = Lists.newArrayList(TIMESTAMP_START, TIMESTAMP_END, SYNCTIME_START,
            SYNCTIME_END, EXECUTIONTIME_START, EXECUTIONTIME_END, TASKBEGINTIME_START, TASKBEGINTIME_END, CLASS, PAGESIZE, PAGENUM);
    public static final List<String> WILDCARD_LIST = Lists.newArrayList(ERRMSG, CONTENT, RETURNMSG);
    public static final List<String> PARAM_LIST = Lists.newArrayList(SCENECODE);
    public static final String ID = "id";
    public static final String SORT = "sort";
    public static final String PAGE = "page";
    public static final String PAGE_SIZE = "pageSize";
    public static final String LOGTYPE = "logType";
    public static final int PAGE_VALUE = 0;
    public static final int PAGE_SIZE_VALUE = 20;
    public static final String TOPIC = "topic";
    public static final String PRODUCER_CONFIG = "producerConfig";
}
