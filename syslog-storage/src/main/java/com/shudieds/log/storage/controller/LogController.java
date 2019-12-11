package com.shudieds.log.storage.controller;

import com.shudieds.log.storage.bean.*;
import com.shudieds.log.storage.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 普通日志内容分页查询
     *
     * @param loggerContentParams
     * @return
     */
    @RequestMapping(value = "/loggerContentSearchPage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseData<LoggerContent> loggerContentSearchPage(@RequestBody LoggerContentParams loggerContentParams) {
        return logService.loggerContentSearchPage(loggerContentParams);
    }

    /**
     * DC日志分页查询
     *
     * @param runTimeLogParams
     * @return
     */
    @RequestMapping(value = "/runTimeLogSearchPage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseData<RunTimeLog> runTimeLogSearchPage(@RequestBody RunTimeLogParams runTimeLogParams) {
        return logService.runTimeLogSearchPage(runTimeLogParams);
    }

    /**
     * 单据日志分页查询
     *
     * @param billLogParams
     * @return
     */
    @RequestMapping(value = "/billLogSearchPage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseData<BillLog> billLogSearchPage(@RequestBody BillLogParams billLogParams) {
        return logService.billLogSearchPage(billLogParams);
    }

    /**
     * 获取日志配置文件
     *
     * @param response
     * @param sysName
     * @param dc
     */
    @RequestMapping(value = "{sysName}/{dc}/logBack.xml", method = RequestMethod.GET, produces = "application/xml;charset=UTF-8")
    public void logBack(HttpServletResponse response, @PathVariable String sysName, @PathVariable String dc) {
        logService.logBack(response, dc, sysName);
    }

    /**
     * 查询单据日志接口
     *
     * @param billLogParams
     * @return
     */
    @RequestMapping(value = "/billLogSearch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<BillLog> billLogSearch(@RequestBody BillLogParams billLogParams) {
        return logService.billLogSearch(billLogParams);
    }

    @RequestMapping(value = "/ifactorySearch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<Map<String, Object>> ifactorySearch(@RequestBody Map<String, Object> params) throws Exception{
        return logService.ifactorySearch(params);
    }
}
