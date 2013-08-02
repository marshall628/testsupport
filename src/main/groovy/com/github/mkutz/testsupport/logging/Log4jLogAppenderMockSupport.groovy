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
