package com.shudieds.client;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.spi.AppenderAttachable;

import java.util.HashMap;
import java.util.Map;

public abstract class NoticeAppenderConfig<E> extends UnsynchronizedAppenderBase<E> implements AppenderAttachable<E> {


    protected Encoder<E> encoder;

    protected Map<String, String> headers = new HashMap<>();

    protected boolean appendTimestamp = true;

    protected String host;

    protected String port;

    protected String dc;

    protected String sysName;

    public void setEncoder(Encoder<E> encoder) {
        this.encoder = encoder;
    }

    public void addHeader(String key) {
        String[] split = key.split("=");
        if (split.length == 2) {
            this.headers.put(split[0], split[1]);
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public boolean isAppendTimestamp() {
        return appendTimestamp;
    }

    public void setAppendTimestamp(boolean appendTimestamp) {
        this.appendTimestamp = appendTimestamp;
    }


    protected boolean checkPrerequisites() {
        boolean errorFree = true;

        if (encoder == null) {
            addError("No encoder set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }

        if (null == host || "".equals(host.trim())) {
            addError("No host set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }

        if (null == port || "".equals(port.trim())) {
            addError("No port set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }

        if (null == dc || "".equals(dc.trim())) {
            addError("No dc set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }

        if (null == sysName || "".equals(sysName.trim())) {
            addError("No sysName set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }
        return errorFree;
    }
}
