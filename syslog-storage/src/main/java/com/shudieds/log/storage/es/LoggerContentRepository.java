package com.shudieds.log.storage.es;

import com.shudieds.log.storage.bean.LoggerContent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggerContentRepository extends ElasticsearchRepository<LoggerContent, String> {
}
