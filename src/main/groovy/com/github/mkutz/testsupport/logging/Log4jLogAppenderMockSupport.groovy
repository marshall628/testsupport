package com.github.mkutz.testsupport.logging

import org.apache.log4j.Appender
import org.apache.log4j.Logger

/**
 * {@link LogAppenderMockSupport} for Log4j logging.
 *
 * @author Michael Kutz <m.kutz@tarent.de>, 19.07.2012
 */
class Log4jLogAppenderMockSupport implements LogAppenderMockSupport<Appender> {

    @Override
    public void addAppenderMock(Class clazz, Appender appenderMock) {
        Logger logger = Logger.getLogger(clazz)
        logger.addAppender(appenderMock)
    }
}
