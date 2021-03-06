package com.shudieds.log.storage.es;

import com.shudieds.log.storage.bean.RunTimeLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunTimeLogRepository extends ElasticsearchRepository<RunTimeLog, String> {
}
