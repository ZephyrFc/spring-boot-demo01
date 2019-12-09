package com.shudieds.log.storage.services;

import com.shudieds.log.storage.bean.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface LogService {

    ResponseData<LoggerContent> loggerContentSearchPage(LoggerContentParams loggerContentParams);

    ResponseData<RunTimeLog> runTimeLogSearchPage(RunTimeLogParams runTimeLogParams);

    ResponseData<BillLog> billLogSearchPage(BillLogParams billLogParams);

    void logBack(HttpServletResponse response, String dc, String sysName);

    List<BillLog> billLogSearch(BillLogParams billLogParams);
}
