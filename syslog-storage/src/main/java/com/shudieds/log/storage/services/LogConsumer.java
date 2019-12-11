package com.shudieds.log.storage.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shudieds.log.storage.bean.*;
import com.shudieds.log.storage.constants.Constants;
import com.shudieds.log.storage.es.BillLogRepository;
import com.shudieds.log.storage.es.LoggerContentRepository;
import com.shudieds.log.storage.es.RunTimeLogRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;

@Component
public class LogConsumer {
    private Logger logger = LoggerFactory.getLogger(LogConsumer.class);
    @Autowired
    private LoggerContentRepository loggerContentRepository;
    @Autowired
    private RunTimeLogRepository runTimeLogRepository;
    @Autowired
    private BillLogRepository billLogRepository;
    @Resource
    private Client client;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Value("#{'${topics}'.split(',')[0]}")
    private String sys_log_topic;
    @Value("#{'${topics}'.split(',')[1]}")
    private String dc_log_upload_topic;


    @KafkaListener(topics = "#{'${topics}'.split(',')}")
    public void consumer(List<ConsumerRecord<?, ?>> records) throws Exception {
        logger.info("consumer data:{}", records);
        List<LoggerContent> loggerContents = new ArrayList<>();
        List<RunTimeLog> runTimeLogs = new ArrayList<>();
        List<BillLog> billLogs = new ArrayList<>();
        List<Map<String, Object>> ifactory = new ArrayList<>();
        if (Optional.ofNullable(records).isPresent()) {
            records.forEach(record -> {
                try {
                    logger.info("consumer kafka value:{}", record.value().toString());
                    if (sys_log_topic.equals(record.topic())) {
                        LoggerContent loggerContent = objectMapper.readValue(record.value().toString(),
                                new TypeReference<LoggerContent>() {
                                });
                        loggerContent.setId(UUID.randomUUID().toString());
                        long timestamp = LocalDateTime.now().toInstant(ZoneOffset.of(Constants.PLUS_EIGHT)).toEpochMilli();
                        loggerContent.setTimestamp(String.valueOf(timestamp));
                        loggerContents.add(loggerContent);
                    } else if (dc_log_upload_topic.equals(record.topic())) {
                        Map<String, Object> data = objectMapper.readValue(record.value().toString(),
                                new TypeReference<Map<String, Object>>() {
                                });
                        if (CollectionUtils.isEmpty(data)) return;
                        if (Constants.RUN_TIME_LOG.equals(data.get(Constants.LOG_TYPE))) {
                            dcLogUploadHandler(data, Constants.RUN_TIME_LOG, RunTimeLog.class,
                                    runTimeLogs, (v1, v2) -> v2.add(v1));
                        } else {
                            dcLogUploadHandler(data, Constants.BILL_LOG, BillLog.class,
                                    billLogs, (v1, v2) -> v2.add(v1));
                        }
                    } else {
                        Map<String, Object> data = objectMapper.readValue(record.value().toString(),
                                new TypeReference<Map<String, Object>>() {
                                });
                        ifactory.add(data);
                    }
                } catch (Exception e) {
                    logger.error("consumer read kafka data error:{}", e.getMessage(), e);
                }
            });
            if (!CollectionUtils.isEmpty(loggerContents)) {
                loggerContentRepository.saveAll(loggerContents);
            }
            if (!CollectionUtils.isEmpty(runTimeLogs)) {
                runTimeLogRepository.saveAll(runTimeLogs);
            }
            if (!CollectionUtils.isEmpty(billLogs)) {
                billLogRepository.saveAll(billLogs);
            }
            if (!CollectionUtils.isEmpty(ifactory)) {
                ifactoryHandler(ifactory);
            }
        }
    }

    private <T> void dcLogUploadHandler(Map<String, Object> data, String flag, Class<T> cls, List<T> list, BiConsumer<T, List<T>> biConsumer) {
        try {
            String d = objectMapper.writeValueAsString(data.get(Constants.DATA));
            T obj = cls.newInstance();
            BeanUtils.copyProperties(generateConvert(flag, d), obj);
            biConsumer.accept(obj, list);
        } catch (Exception e) {
            logger.error("dcLogUploadHandler map to object error:{}", e.getMessage(), e);
        }
    }

    private Object generateConvert(String type, String value) throws Exception {
        long timestamp = LocalDateTime.now().toInstant(ZoneOffset.of(Constants.PLUS_EIGHT)).toEpochMilli();
//                LocalDateTime.now().toEpochSecond(ZoneOffset.of(Constants.PLUS_EIGHT));
        if (Constants.RUN_TIME_LOG.equals(type)) {
            RunTimeLogConvert runTimeLogConvert = objectMapper.readValue(value,
                    new TypeReference<RunTimeLogConvert>() {
                    });
            runTimeLogConvert.setId(UUID.randomUUID().toString());
            runTimeLogConvert.setTimestamp(String.valueOf(timestamp));
            return runTimeLogConvert;
        } else {
            BillLogConvert billLogConvert = objectMapper.readValue(value,
                    new TypeReference<BillLogConvert>() {
                    });
            billLogConvert.setId(UUID.randomUUID().toString());
            billLogConvert.setTimestamp(String.valueOf(timestamp));
            int syncTimestamp = date2TimeStamp(billLogConvert.getSyncTime(), Constants.YYYY_MM_DD_HH_MM_SS);
            billLogConvert.setSyncTimestamp(syncTimestamp);
            return billLogConvert;
        }
    }

    private int date2TimeStamp(String date_str, String format) {
        try {
            LocalDateTime begin = LocalDateTime.parse(date_str, DateTimeFormatter.ofPattern(format));
            Long second = begin.toEpochSecond(ZoneOffset.of(Constants.PLUS_EIGHT));
            return second.intValue();
        } catch (Exception e) {
            logger.error("date2TimeStamp date convert error:{}", e.getMessage(), e);
        }
        return 0;
    }

    public void ifactoryHandler(List<Map<String, Object>> data) throws Exception {
        if (!CollectionUtils.isEmpty(data)) {
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            data.forEach(map -> {
                map.put(Constants.TIMESTAMP, LocalDateTime.now().toInstant(ZoneOffset.of(Constants.PLUS_EIGHT)).toEpochMilli());
                IndexRequest request = client.prepareIndex(String.valueOf(map.get(Constants.LOGTYPE)),
                        map.get(Constants.LOGTYPE) + "_type",
                        String.valueOf(null == map.get(Constants.ID) ? UUID.randomUUID().toString()
                                : map.get(Constants.ID))).setSource(map).request();
                bulkRequest.add(request);
            });
            bulkRequest.execute().actionGet();
        }
    }

}
