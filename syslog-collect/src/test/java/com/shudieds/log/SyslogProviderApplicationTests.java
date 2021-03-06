package com.shudieds.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shudieds.collect.services.LogService;
import com.shudieds.log.api.LoggerContent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class SyslogProviderApplicationTests {
//    private KafkaTemplate<String, String> kafkaTemplate;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TOPIC_UPLOAD = "dc-log-upload";
    //
//    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
    @Resource
    private KafkaTemplate<String, String>  kafkaTemplate;

    @Test
    public void contextLoads() throws Exception {
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "192.168.1.242:9092");
//        props.put("key.serializer", StringSerializer.class);
//        props.put("value.serializer", StringSerializer.class);
//
//        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ListenableFuture<SendResult<String, String>> ss = kafkaTemplate.send(new ProducerRecord<>(TOPIC_UPLOAD, String.valueOf(1564384949), "33333333333333"));
        System.out.println(ss);
    }
}
