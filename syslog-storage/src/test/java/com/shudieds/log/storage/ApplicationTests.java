package com.shudieds.log.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void contextLoads() throws Exception {
//        Map<String, Object> data = objectMapper.readValue("{\"data\":{\"canvas_node_code\":null,\"dc_id\":\"lgyzy888\",\"err_msg\":null,\"execution_time\":\"2019-08-16 17:36:23\",\"link_env\":1,\"org_code\":\"47b2d999-4de8-4eb9-9da5-327e6bc1918b\",\"return_msg\":\"任务在运行时出现错误-class java.util.concurrent.TimeoutException，下载模型超时\\n 详细内容请查看设备日志文件!\",\"scene_code\":\"OF6562575289283510272\",\"source_bill_code\":null,\"target_bill_code\":null,\"task_begin_time\":\"2019-08-16 17:35:23\"},\"log_type\":\"runtime_log\"}",
//                new TypeReference<Map<String, Object>>() {
//                });
//        System.out.println(data);
//        List<BillLog> logUploads = new ArrayList<>();
//        BillLog dcLogUpload = new BillLog();
//        dcLogUpload.setDataType(2);
//        dcLogUpload.setDcId("1");
//        dcLogUpload.setFailCount(1);
//        dcLogUpload.setLinkEnv(1);
//        dcLogUpload.setNodeCode("1");
//        dcLogUpload.setSceneCode("1");
//        dcLogUpload.setSyncTime("2019-10-11");
//        dcLogUpload.setTaskId("1");
//        dcLogUpload.setSuccessCount(1);
//        logUploads.add(dcLogUpload);
//        logDao.upsert(logUploads);
        //1.解释SQL
//        Properties properties = new Properties();
//        properties.put("url", "jdbc:elasticsearch://192.168.1.241:9300/bill_log_index");
//        properties.put("driverClassName", "com.mysql.jdbc.Driver");
//        DruidDataSource dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
//        Connection connection = dds.getConnection();
//        PreparedStatement ps = connection.prepareStatement("select * from bill_log_index where id='0f690d84-78c1-4a69-aca1-ff0e95a78b44'");
//        ResultSet resultSet = ps.executeQuery();
//        List<String> result = new ArrayList<>();
//        while (resultSet.next()) {
//        }
//        ps.close();
//        connection.close();
//        dds.close();
    }

}
