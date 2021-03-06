package com.shudieds.collect.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shudieds.log.api.Empty;
import com.shudieds.log.api.LoggerContent;
import com.shudieds.log.api.LoggerServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LogService extends LoggerServiceGrpc.LoggerServiceImplBase {
    private Logger logger = LoggerFactory.getLogger(LogService.class);

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SPAN_ID = "spanId";
    private static final String TRACE_ID = "traceId";
    private static final String SYS_NAME = "sysName";
    private static final String CONTENT = "content";
    private static final String TIMESTAMP = "timestamp";
    @Value("${topics}")
    private String TOPIC_UPLOAD;

    @Override
    public void append(LoggerContent request, StreamObserver<Empty> responseObserver) {
        try {
            UUID uuid = UUID.randomUUID();
            Map<String, Object> map = new HashMap<>(request.getMetaMap());
            map.put(SPAN_ID, request.getSpanId());
            map.put(TRACE_ID, request.getTraceId());
            map.put(CONTENT, request.getContent());
            map.put(TIMESTAMP, request.getTimestamp());
            map.putAll(request.getMetaMap());
            map.put(SYS_NAME, request.getSysName());
            kafkaTemplate.send(new ProducerRecord<>(TOPIC_UPLOAD, uuid.toString(), objectMapper.writeValueAsString(map)));
        } catch (Exception ex) {
            logger.error("append send kafka info error:{}", ex.getMessage(), ex);
        }
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
