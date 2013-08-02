/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.mkutz.testsupport.logging

import org.apache.log4j.Appender
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.github.mkutz.testsupport.logging.Log4jLogAppenderMockSupport;

import spock.lang.Specification

/**
 * @author Michael Kutz <m.kutz@tarent.de>, 20.07.2012
 */
@Mixin(Log4jLogAppenderMockSupport)
class Log4jLogAppenderMockSupportSpec extends Specification {

    def "verification of logging events should be possible"() {
        given:
        LoggingClass loggingClass = new LoggingClass()
        Appender appenderMock = Mock()
        String message = "some log message"

        when:
        addAppenderMock(LoggingClass, appenderMock)

        and:
        loggingClass.loggingMethod(message)

        then:
        1 * appenderMock.doAppend({ it.getMessage() == message })
    }

    class LoggingClass {
        private static Logger LOG = LoggerFactory.getLogger(LoggingClass)
        void loggingMethod(String message) {
            LOG.debug(message)
        }
    }
}
