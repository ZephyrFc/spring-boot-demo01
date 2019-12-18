package com.shudieds.log.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shudieds.log.storage.constants.Constants;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    private Client client;

    @Test
    public void contextLoads() throws Exception {
        List<Map<String,Object>> data = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("logType","ifactory_log_index");
        map1.put("id",1234567890);
        map1.put("name","liuzhicheng");
        map1.put("password","123456");
        map1.put("phone","188888888");
        Map<String,Object> map2 = new HashMap<>();
        map2.put("logType","ifactory_log_index");
        map2.put("id",1234567891);
        map2.put("name","wujiao");
        map2.put("password","654321");
        map2.put("phone","199999999");
        data.add(map1);
        data.add(map2);
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
