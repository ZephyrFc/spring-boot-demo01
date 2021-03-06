package com.shudieds.client;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import com.shudieds.log.api.LoggerContent;
import com.shudieds.log.api.LoggerServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NoticeAppender<E> extends NoticeAppenderConfig<E> {
    private Logger logger = LoggerFactory.getLogger(NoticeAppender.class);
    private final AppenderAttachableImpl<E> aai = new AppenderAttachableImpl<>();
    private final ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<>();
    private LoggerServiceGrpc.LoggerServiceBlockingStub stub;
    private static final String DC = "dc";
    private static final String LEVEL = "level";
    private static final String THREAD_NAME = "threadName";
    private static final String LOGGER_NAME = "loggerName";

    @Override
    protected void append(E e) {
        try {
            LoggingEvent loggingEvent = (LoggingEvent) e;
            Map<String, String> map = loggingEvent.getMDCPropertyMap();
            Map<String, String> meta = new HashMap<>();
            meta.put(DC, super.dc);
            meta.putAll(map);
            meta.put(LEVEL, loggingEvent.getLevel().toString());
            meta.put(THREAD_NAME, loggingEvent.getThreadName());
            meta.put(LOGGER_NAME, loggingEvent.getLoggerName());
            LoggerContent loggerContent = LoggerContent.newBuilder().
                    putAllMeta(meta).
                    setContent(loggingEvent.getFormattedMessage()).
                    setSysName(super.sysName).
                    setTimestamp((int) Math.round(loggingEvent.getTimeStamp() / 1000d)).build();
            this.stub.append(loggerContent);
        } catch (Exception ex) {
            logger.error("append grpc error:{}", ex.getMessage(), ex);
        }
    }

    private void deferAppend(E event) {
        queue.add(event);
    }

    // drains queue events to super
    private void ensureDeferredAppends() {
        E event;
        while ((event = queue.poll()) != null) {
            super.doAppend(event);
        }
    }

    @Override
    public void doAppend(E e) {
        ensureDeferredAppends();
        if (e instanceof ILoggingEvent && ((ILoggingEvent) e).getLoggerName().startsWith("")) {
            deferAppend(e);
        } else {
            super.doAppend(e);
        }
    }

    @Override
    public void start() {
        if (!super.checkPrerequisites()) {
            return;
        }
        ManagedChannel channel = ManagedChannelBuilder.forAddress(super.host, Integer.parseInt(super.port))
                .usePlaintext()
                .build();
        this.stub = LoggerServiceGrpc.newBlockingStub(channel);
        super.start();
    }

    public void addAppender(Appender<E> newAppender) {
        aai.addAppender(newAppender);
    }

    public Iterator<Appender<E>> iteratorForAppenders() {
        return aai.iteratorForAppenders();
    }

    public Appender<E> getAppender(String name) {
        return aai.getAppender(name);
    }

    public boolean isAttached(Appender<E> appender) {
        return aai.isAttached(appender);
    }

    public void detachAndStopAllAppenders() {
        aai.detachAndStopAllAppenders();
    }

    public boolean detachAppender(Appender<E> appender) {
        return aai.detachAppender(appender);
    }

    public boolean detachAppender(String name) {
        return aai.detachAppender(name);
    }
}
