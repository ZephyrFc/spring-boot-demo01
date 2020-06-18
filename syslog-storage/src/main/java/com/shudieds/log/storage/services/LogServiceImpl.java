package com.shudieds.log.storage.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.shudieds.log.storage.bean.*;
import com.shudieds.log.storage.constants.Constants;
import com.shudieds.log.storage.es.BillLogRepository;
import com.shudieds.log.storage.es.LoggerContentRepository;
import com.shudieds.log.storage.es.RunTimeLogRepository;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.shudieds.log.storage.constants.Constants.*;

@Service
public class LogServiceImpl implements LogService {
    private final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
    @Resource
    private LoggerContentRepository loggerContentRepository;
    @Resource
    private RunTimeLogRepository runTimeLogRepository;
    @Resource
    private BillLogRepository billLogRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    private Client client;
    @Value("${grpc.listen.host}")
    private String host;
    @Value("${grpc.listen.port}")
    private String port;
    @Value("#{'${topics}'.split(',')[2]}")
    private String topic;
    @Value("${producerConfig}")
    private String producerConfig;

    @Override
    public ResponseData<LoggerContent> loggerContentSearchPage(LoggerContentParams loggerContentParams) {
        logger.info("loggerContentSearchPage params:{}", loggerContentParams);
        ResponseData<LoggerContent> responseData = new ResponseData<>();
        try {
            BoolQueryBuilder boolQueryBuilder = generateLoggerContentBuilder(loggerContentParams);
            SearchQuery searchQuery = generateSearchQuery(boolQueryBuilder, loggerContentParams.getPageNum(),
                    loggerContentParams.getPageSize(), Constants.LOGGER_CONTENT_INDEX, loggerContentParams.getSort());
            Page<LoggerContent> page = loggerContentRepository.search(searchQuery);
            setResponseData(page, responseData, loggerContentParams.getPageNum(), loggerContentParams.getPageSize());
        } catch (Exception ex) {
            logger.error("loggerContentSearchPage search error:{}", ex.getMessage(), ex);
        }
        return responseData;
    }

    @Override
    public ResponseData<RunTimeLog> runTimeLogSearchPage(RunTimeLogParams runTimeLogParams) {
        logger.info("runTimeLogSearchPage params:{}", runTimeLogParams);
        ResponseData<RunTimeLog> responseData = new ResponseData<>();
        try {
            BoolQueryBuilder boolQueryBuilder = generateRunTimeLogBuilder(runTimeLogParams);
            SearchQuery searchQuery = generateSearchQuery(boolQueryBuilder, runTimeLogParams.getPageNum(),
                    runTimeLogParams.getPageSize(), Constants.RUN_TIME_LOG_INDEX, runTimeLogParams.getSort());
            Page<RunTimeLog> page = runTimeLogRepository.search(searchQuery);
            setResponseData(page, responseData, runTimeLogParams.getPageNum(), runTimeLogParams.getPageSize());
        } catch (Exception ex) {
            logger.error("runTimeLogSearchPage search error:{}", ex.getMessage(), ex);
        }
        return responseData;
    }

    @Override
    public ResponseData<BillLog> billLogSearchPage(BillLogParams billLogParams) {
        logger.info("billLogSearchPage params:{}", billLogParams);
        ResponseData<BillLog> responseData = new ResponseData<>();
        try {
            BoolQueryBuilder boolQueryBuilder = generateBillLogBuilder(billLogParams);
            SearchQuery searchQuery = generateSearchQuery(boolQueryBuilder, billLogParams.getPageNum(),
                    billLogParams.getPageSize(), Constants.BILL_LOG_INDEX, billLogParams.getSort());
            Page<BillLog> page = billLogRepository.search(searchQuery);
            setResponseData(page, responseData, billLogParams.getPageNum(), billLogParams.getPageSize());
        } catch (Exception ex) {
            logger.error("billLogSearchPage search error:{}", ex.getMessage(), ex);
        }
        return responseData;
    }

    @Override
    public void logBack(HttpServletResponse response, String dc, String sysName) {
        logger.info("logBack host:{},port:{},dc:{},sysName:{}", host, port, dc, sysName);
        Map<String, String> data = new HashMap<>();
        data.put(Constants.HOST, host);
        data.put(Constants.PORT, port);
        data.put(Constants.DC_XML, dc);
        data.put(Constants.SYSNAME, sysName);
        generateXml(response, Constants.MYLOG, data);
    }

    @Override
    public void logBack(HttpServletResponse response, String level) {
        Map<String, String> data = new HashMap<>();
        data.put(Constants.TOPIC, topic);
        data.put(Constants.PRODUCER_CONFIG, producerConfig);
        data.put(LEVEL, level);
        generateXml(response, Constants.KAFKALOG, data);
    }

    private void generateXml(HttpServletResponse response, String file, Map<String, String> data) {
        try (InputStream inputStream = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(file));
             OutputStream outputStream = response.getOutputStream()) {
            byte[] bytes = writeXml(inputStream, data);
            response.setContentType(Constants.CONTENT_TYPE);
            response.addHeader(Constants.CONTENT_DISPOSITION, Constants.CONTENT_DISPOSITION_VAL);
            outputStream.write(Objects.requireNonNull(bytes));
            outputStream.flush();
        } catch (Exception ex) {
            logger.error("logBack get xml configuration file error:{}", ex.getMessage(), ex);
        }
    }

    @Override
    public List<BillLog> billLogSearch(BillLogParams billLogParams) {
        logger.info("billLogSearch params:{}", billLogParams);
        List<BillLog> billLogs = new ArrayList<>();
        SearchResponse searchResponse = null;
        try {
            BoolQueryBuilder boolQueryBuilder = generateBillLogBuilder(billLogParams);
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch(Constants.BILL_LOG_INDEX).setQuery(boolQueryBuilder);
            setSort(searchRequestBuilder, billLogParams.getSort());
            searchRequestBuilder.setScroll("2m").setSize(1000);
            searchResponse = searchRequestBuilder.execute().actionGet();
            SearchHits hits = searchResponse.getHits();
            setHits(hits, billLogs);
            do {
                SearchScrollRequestBuilder searchScrollRequestBuilder = client.prepareSearchScroll(searchResponse.getScrollId());
                searchScrollRequestBuilder.setScroll("2m");
                searchResponse = searchScrollRequestBuilder.execute().actionGet();
                SearchHits shits = searchResponse.getHits();
                setHits(shits, billLogs);
            } while (searchResponse.getHits().getHits().length > 0);

        } catch (Exception ex) {
            logger.error("billLogSearch search error:{}", ex.getMessage(), ex);
        }finally {
            if (null != searchResponse){
                clearScroll(client, searchResponse.getScrollId());
            }
        }
        return billLogs;
    }

    @Override
    public List<Map<String, Object>> ifactorySearch(Map<String, Object> params) throws Exception {
        Objects.requireNonNull(params);
        Objects.requireNonNull(params.get(LOGTYPE));
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> builderParams = params.entrySet().stream().filter(x -> !LOGTYPE.equals(x.getKey())
                && !PAGE.equals(x.getKey())
                && !PAGE_SIZE.equals(x.getKey())
                && !SORT.equals(x.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        BoolQueryBuilder boolQueryBuilder = generateIfactoryBuilder(builderParams);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(String.valueOf(params.get(LOGTYPE))).setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(null == params.get(PAGE) ? PAGE_VALUE : (int) params.get(PAGE) - 1)
                .setSize((null == params.get(PAGE) ? PAGE_SIZE_VALUE : (int) params.get(PAGE_SIZE)));
        setSort(searchRequestBuilder, (Map) params.get(SORT));
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        setHits(hits, result);
        return result;
    }

    private BoolQueryBuilder generateIfactoryBuilder(Map<String, Object> params) {
        List<QueryBuilder> queryBuilders = new ArrayList<>();
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach((k, v) -> {
                QueryBuilder queryBuilder = null;
                if (v instanceof String) {
                    queryBuilder = QueryBuilders.wildcardQuery(k + Constants.KEYWORD,
                            Constants.WILDCARD + v + Constants.WILDCARD);
                }
                if (v instanceof Number) {
                    queryBuilder = QueryBuilders.termQuery(k, v);
                }
                if (v instanceof List) {
                    queryBuilder = QueryBuilders.termsQuery(k + Constants.KEYWORD, v);
                }
                Optional.ofNullable(queryBuilder).ifPresent(queryBuilders::add);
            });
        }
        return getBoolQueryBuilder(queryBuilders);
    }

    private boolean clearScroll(Client client, String scrollId) {
        ClearScrollRequestBuilder clearScrollRequestBuilder = client.prepareClearScroll();
        clearScrollRequestBuilder.addScrollId(scrollId);
        ClearScrollResponse response = clearScrollRequestBuilder.get();
        return response.isSucceeded();
    }

    private <T> void setHits(SearchHits shits, List<T> list) throws Exception {
        for (SearchHit hit : shits) {
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            String json = objectMapper.writeValueAsString(map);
            T data = objectMapper.readValue(json, new TypeReference<T>() {
            });
            Optional.ofNullable(data).ifPresent(list::add);
        }
    }

    private int date2TimeStamp(String date_str, String format, boolean flag) {
        try {
            LocalDateTime begin = LocalDateTime.of(LocalDate.parse(date_str,
                    DateTimeFormatter.ofPattern(format)), flag ? LocalTime.MIN : LocalTime.MAX);
            Long second = begin.toEpochSecond(ZoneOffset.of(Constants.PLUS_EIGHT));
            return second.intValue();
        } catch (Exception e) {
            logger.error("date2TimeStamp date convert error:{}", e.getMessage(), e);
        }
        return 0;
    }

    private String getMinOrMaxDate(String date_str, String format, boolean flag) {
        try {
            LocalDateTime begin = LocalDateTime.of(LocalDate.parse(date_str,
                    DateTimeFormatter.ofPattern(format)), flag ? LocalTime.MIN : LocalTime.MAX);
            return begin.format(DateTimeFormatter.ofPattern(Constants.YYYY_MM_DD_HH_MM_SS));
        } catch (Exception e) {
            logger.error("getMinOrMaxDate date convert error:{}", e.getMessage(), e);
        }
        return null;
    }

    private BoolQueryBuilder generateLoggerContentBuilder(LoggerContentParams loggerContentParams) {
        List<QueryBuilder> queryBuilders = generateQueryBuilder(loggerContentParams);
        List<QueryBuilder> rangeQueryBuilders = generateRangeQueryBuilder(loggerContentParams);
        queryBuilders.addAll(rangeQueryBuilders);
        return getBoolQueryBuilder(queryBuilders);
    }

    private BoolQueryBuilder generateRunTimeLogBuilder(RunTimeLogParams runTimeLogParams) {
        List<QueryBuilder> queryBuilders = generateQueryBuilder(runTimeLogParams);
        List<QueryBuilder> rangeQueryBuilders = generateRangeQueryBuilder(runTimeLogParams);
        queryBuilders.addAll(rangeQueryBuilders);
        return getBoolQueryBuilder(queryBuilders);
    }

    private BoolQueryBuilder generateBillLogBuilder(BillLogParams billLogParams) {
        List<QueryBuilder> queryBuilders = generateQueryBuilder(billLogParams);
        List<QueryBuilder> rangeQueryBuilders = generateRangeQueryBuilder(billLogParams);
        queryBuilders.addAll(rangeQueryBuilders);
        return getBoolQueryBuilder(queryBuilders);
    }

    private List<QueryBuilder> generateRangeQueryBuilder(Object object) {
        Objects.requireNonNull(object);
        List<QueryBuilder> queryBuilders = new ArrayList<>();
        String start = null, end = null;
        if (object instanceof LoggerContentParams) {
            LoggerContentParams loggerContentParams = (LoggerContentParams) object;
            start = loggerContentParams.getTimestampStart();
            end = loggerContentParams.getTimestampEnd();
        } else if (object instanceof RunTimeLogParams) {
            RunTimeLogParams runTimeLogParams = (RunTimeLogParams) object;
            start = runTimeLogParams.getTimestampStart();
            end = runTimeLogParams.getTimestampEnd();
            QueryBuilder executionTimeBuilder = getRangeQueryBuilder(runTimeLogParams.getExecutionTimeStart(),
                    runTimeLogParams.getExecutionTimeEnd(), Constants.EXECUTIONTIME_KEYWORD);
            QueryBuilder taskBeginTimeBuilder = getRangeQueryBuilder(runTimeLogParams.getTaskBeginTimeStart(),
                    runTimeLogParams.getTaskBeginTimeEnd(), Constants.TASKBEGINTIME_KEYWORD);
            Optional.ofNullable(executionTimeBuilder).ifPresent(queryBuilders::add);
            Optional.ofNullable(taskBeginTimeBuilder).ifPresent(queryBuilders::add);
        } else if (object instanceof BillLogParams) {
            BillLogParams billLogParams = (BillLogParams) object;
            start = billLogParams.getTimestampStart();
            end = billLogParams.getTimestampEnd();
            QueryBuilder syncTimeBuilder = getRangeQueryBuilder(billLogParams.getSyncTimeStart(),
                    billLogParams.getSyncTimeEnd(), Constants.SYNCTIME_KEYWORD);
            Optional.ofNullable(syncTimeBuilder).ifPresent(queryBuilders::add);
        }
        QueryBuilder timestampBuilder = getRangeQueryBuilder(start, end, Constants.TIMESTAMP);
        Optional.ofNullable(timestampBuilder).ifPresent(queryBuilders::add);
        return queryBuilders;
    }

    private List<QueryBuilder> generateQueryBuilder(Object object) {
        Objects.requireNonNull(object);
        List<QueryBuilder> queryBuilders = new ArrayList<>();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(object.getClass());
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String name = propertyDescriptor.getName();
            Method readMethod = propertyDescriptor.getReadMethod();
            try {
                Object value = readMethod.invoke(object);
                if (!StringUtils.isEmpty(value) && excludeField(name)) {
                    String key = (value instanceof String || value instanceof List) ? name + Constants.KEYWORD : name;
                    QueryBuilder queryBuilder = null;
                    if (WILDCARD_LIST.contains(name)) {
                        queryBuilder = QueryBuilders.wildcardQuery(key, Constants.WILDCARD + value + Constants.WILDCARD);
                    } else {
                        if (value instanceof List) {
                            if (!CollectionUtils.isEmpty((List) value)) {
                                queryBuilder = QueryBuilders.termsQuery(key, (List) value);
                            }
                        } else if (!(value instanceof Map)) {
                            queryBuilder = QueryBuilders.termQuery(key, value);
                        }
                    }
                    Optional.ofNullable(queryBuilder).ifPresent(queryBuilders::add);
                }
            } catch (Exception e) {
                logger.error("generateQueryBuilder reflect error:{}", e.getMessage(), e);
            }
        }
        return queryBuilders;
    }

    private boolean excludeField(String name) {
        return !FIELD_LIST.contains(name);
    }

    private QueryBuilder getRangeQueryBuilder(Object val1, Object val2, String key) {
        if (StringUtils.isEmpty(key) || (StringUtils.isEmpty(val1) && StringUtils.isEmpty(val2))) {
            return null;
        }
        RangeQueryBuilder queryBuilder;
        if (Constants.TIMESTAMP.equals(key)) {
            queryBuilder = QueryBuilders.rangeQuery(key);
            Optional.ofNullable(val1).ifPresent(v1 -> queryBuilder.from(v1, false));
            Optional.ofNullable(val2).ifPresent(v2 -> queryBuilder.from(v2, false));
        } else {
            queryBuilder = QueryBuilders.rangeQuery(key);
            Optional.ofNullable(val1).ifPresent(v1 -> queryBuilder.from(getMinOrMaxDate(String.valueOf(v1),
                    Constants.YYYY_MM_DD, true), true));
            Optional.ofNullable(val2).ifPresent(v2 -> queryBuilder.to(getMinOrMaxDate(String.valueOf(v2),
                    Constants.YYYY_MM_DD, false), true));
        }
        return queryBuilder;
    }

    private BoolQueryBuilder getBoolQueryBuilder(List<QueryBuilder> queryBuilders) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(queryBuilders)) {
            queryBuilders.stream().filter(Objects::nonNull).forEach(boolQueryBuilder::must);
        }
        return boolQueryBuilder;
    }

    private SearchQuery generateSearchQuery(BoolQueryBuilder queryBuilder, int pageNum, int pageSize, String index, Map<String, String> sort) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        nativeSearchQueryBuilder.withQuery(queryBuilder).withIndices(index).withPageable(pageable);
        setSort(nativeSearchQueryBuilder, sort);
        return nativeSearchQueryBuilder.build();
    }

    private void setSort(Object obj, Map<String, String> sort) {
        Optional.ofNullable(sort).ifPresent(s -> s.forEach((k, v) -> {
            SortBuilder sortBuilder = SortBuilders.fieldSort(k);
            if (DESC.equalsIgnoreCase(v)) {
                sortBuilder.order(SortOrder.DESC);
            } else {
                sortBuilder.order(SortOrder.ASC);
            }
            if (obj instanceof NativeSearchQueryBuilder) {
                ((NativeSearchQueryBuilder) obj).withSort(sortBuilder);
            }
            if (obj instanceof SearchRequestBuilder) {
                ((SearchRequestBuilder) obj).addSort(sortBuilder);
            }
        }));
    }

    private <T> void setResponseData(Page<T> page, ResponseData<T> responseData, int pageNum, int pageSize) {
        if (Optional.ofNullable(page).isPresent()) {
            responseData.setData(Lists.newArrayList(page));
            responseData.setTotalPage(page.getTotalPages());
            responseData.setPageNum(pageNum);
            responseData.setPageSize(pageSize);
            responseData.setTotal(page.getTotalElements());
        }
    }

    private byte[] writeXml(InputStream inputStream, Map<String, String> data) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        XMLWriter xmlWriter = null;
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            Element rootElt = document.getRootElement();
            Iterator iter = rootElt.elementIterator(Constants.APPENDER);
            Element iterRoot = rootElt.element(Constants.ROOT);
            Optional.ofNullable(iterRoot).ifPresent(ir -> Optional.ofNullable(ir.attribute(LEVEL)).ifPresent(i -> i.setValue(data.get(LEVEL))));
            // 遍历head节点
            while (iter.hasNext()) {
                Element recordEle = (Element) iter.next();
                String value = recordEle.attributeValue(Constants.NAME);
                if (Constants.KAFKA_APPENDER.equals(value)) {
                    Optional.ofNullable(data).ifPresent(d ->
                            d.entrySet().stream().filter(d1 -> !LEVEL.equals(d1.getKey())).forEach(k -> {
                                recordEle.addElement(k.getKey()).addText(k.getValue());
                            }));
                }
            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            byteArrayOutputStream = new ByteArrayOutputStream();
            xmlWriter = new XMLWriter(byteArrayOutputStream, format);
            xmlWriter.write(document);
            xmlWriter.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            logger.error("writeXml write xml new nodes error:{}", e.getMessage(), e);
        } finally {
            try {
                Objects.requireNonNull(byteArrayOutputStream).close();
                Objects.requireNonNull(xmlWriter).close();
            } catch (IOException e) {
                logger.error("writeXml write xml stream close error:{}", e.getMessage(), e);
            }
        }
        return null;
    }
}
