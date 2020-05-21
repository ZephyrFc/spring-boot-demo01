package com.shudieds.log.storage.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shudieds.log.storage.bean.*;
import com.shudieds.log.storage.constants.Constants;
import com.shudieds.log.storage.es.BillLogRepository;
import com.shudieds.log.storage.es.LoggerContentRepository;
import com.shudieds.log.storage.es.RunTimeLogRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class LogConsumer {
    private final Logger logger = LoggerFactory.getLogger(LogConsumer.class);
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
        Map<String, List<ConsumerRecord<?, ?>>> groupedLogger =
                records.parallelStream().collect(Collectors.groupingBy(ConsumerRecord::topic));

        List<ConsumerRecord<?, ?>> syslog = groupedLogger.getOrDefault(sys_log_topic, Collections.emptyList());
        List<ConsumerRecord<?, ?>> dc_log_upload = groupedLogger.getOrDefault(dc_log_upload_topic, Collections.emptyList());

        sysLog(syslog);
        dcBuiLog(dc_log_upload);
    }

    private void sysLog(List<ConsumerRecord<?, ?>> syslog) {
        if (syslog.isEmpty()) {
            return;
        }
        List<LoggerContent> loggerContents = syslog.parallelStream().map(r -> {
            try {
                LoggerContent loggerContent = objectMapper.readValue(r.value().toString(),
                        new TypeReference<LoggerContent>() {
                        });
                loggerContent.setId(UUID.randomUUID().toString());
                long timestamp = LocalDateTime.now().toInstant(ZoneOffset.of(Constants.PLUS_EIGHT)).toEpochMilli();
                loggerContent.setTimestamp(String.valueOf(timestamp));
                return loggerContent;
            } catch (IOException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (!loggerContents.isEmpty()) {
            logger.info("save SYS-LOG size: {}", loggerContents.size());
            loggerContentRepository.saveAll(loggerContents);
        }
    }

    private void dcBuiLog(List<ConsumerRecord<?, ?>> records) {
        if (records.isEmpty()) {
            return;
        }
        Map<Class<?>, List<Object>> buiLogs = records.parallelStream().map(
                (record) -> {
                    try {
                        Map<String, Object> data = objectMapper.readValue(record.value().toString(),
                                new TypeReference<Map<String, Object>>() {
                                });
                        if (Constants.RUN_TIME_LOG.equals(data.get(Constants.LOG_TYPE))) {
                            String d = objectMapper.writeValueAsString(data.get(Constants.DATA));
                            RunTimeLog runTimeLog = new RunTimeLog();
                            BeanUtils.copyProperties(generateConvert(Constants.RUN_TIME_LOG, d), runTimeLog);
                            return runTimeLog;
                        } else {
                            String d = objectMapper.writeValueAsString(data.get(Constants.DATA));
                            BillLog billLog = new BillLog();
                            BeanUtils.copyProperties(generateConvert(Constants.BILL_LOG, d), billLog);
                            return billLog;
                        }
                    } catch (IOException e) {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(
                Collectors.groupingBy(Object::getClass));

        List<RunTimeLog> rtlogs = Optional.ofNullable(buiLogs.get(RunTimeLog.class)).orElse(Collections.emptyList())
                .stream().map(o -> (RunTimeLog) o).collect(Collectors.toList());

        List<BillLog> blogs = Optional.ofNullable(buiLogs.get(BillLog.class)).orElse(Collections.emptyList())
                .stream().map(o -> (BillLog) o).collect(Collectors.toList());

        if (!rtlogs.isEmpty()) {
            logger.info("save RUNTIME-LOG size: {}", rtlogs.size());
            runTimeLogRepository.saveAll(rtlogs);
        }

        if (!blogs.isEmpty()) {
            logger.info("save BILL-LOG size: {}", blogs.size());
            billLogRepository.saveAll(blogs);
        }
    }

    private Object generateConvert(String type, String value) throws IOException {
        long timestamp = LocalDateTime.now().toInstant(ZoneOffset.of(Constants.PLUS_EIGHT)).toEpochMilli();
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
            int syncTimestamp = date2TimeStamp(billLogConvert.getSyncTime());
            billLogConvert.setSyncTimestamp(syncTimestamp);
            return billLogConvert;
        }
    }

    private int date2TimeStamp(String date_str) {
        try {
            LocalDateTime begin = LocalDateTime.parse(date_str, DateTimeFormatter.ofPattern(Constants.YYYY_MM_DD_HH_MM_SS));
            long second = begin.toEpochSecond(ZoneOffset.of(Constants.PLUS_EIGHT));
            return (int) second;
        } catch (Exception e) {
            logger.error("date2TimeStamp date convert error:{}", e.getMessage(), e);
        }
        return 0;
    }

}
