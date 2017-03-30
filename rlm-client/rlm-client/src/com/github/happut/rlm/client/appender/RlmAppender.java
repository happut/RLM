package com.github.happut.rlm.client.appender;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * Title: Logback ArteryAppender<br>
 * Description:<br>
 * Copyright: Copyright (c) 2015<br>
 * Company: 北京华宇软件股份有限公司<br>
 * 
 * @author Artery_zhangchw
 * @version 1.0
 * @date 2015-7-29
 */
public class RlmAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void append(final ILoggingEvent event) {
        Map map = new HashMap();
        map.put("logger", event.getLoggerName());
        map.put("level", String.valueOf(event.getLevel())); // in case
                                                            // getLevel
                                                            // returns
                                                            // null
        map.put("start", new Date(event.getTimeStamp()));
        map.put("thread", event.getThreadName());
        map.put("message", event.getFormattedMessage());
        logException(event.getThrowableProxy(), map);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void logException(IThrowableProxy tp, Map log) {
        if (tp == null)
            return;
        String tpAsString = ThrowableProxyUtil.asString(tp); // the stack trace
                                                             // basically
        List<String> stackTrace = Arrays.asList(tpAsString.replace("\t", "").split(CoreConstants.LINE_SEPARATOR));
        if (stackTrace.size() > 0) {
            log.put("exception", stackTrace.get(0));
        }
        if (stackTrace.size() > 1) {
            log.put("stacktrace", stackTrace.subList(1, stackTrace.size()));
        }
    }
}