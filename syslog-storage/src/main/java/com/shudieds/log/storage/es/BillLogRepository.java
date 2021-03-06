package com.shudieds.log.storage.es;

import com.shudieds.log.storage.bean.BillLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillLogRepository extends ElasticsearchRepository<BillLog, String> {
}
