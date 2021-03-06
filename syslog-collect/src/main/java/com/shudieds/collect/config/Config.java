package com.shudieds.collect.config;

import com.shudieds.collect.services.LogService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Configuration
public class Config {

    @Value("${grpc.listen.port}")
    private int grpcPort;

    @Bean
    public LogService logService() {
        return new LogService();
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public Server server(@Autowired LogService logService) {
        Server build = ServerBuilder.forPort(this.grpcPort)
                .addService(logService).build();
        CompletableFuture.runAsync(()-> {
            try {
                build.awaitTermination();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
        });
        return build;
    }


    @Resource
    private KafkaProperties kafkaProperties;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
