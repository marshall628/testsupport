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

/**
 * <p>
 * Interface log specification support. Inheriting classes will give you support to add a (mocked) which may be used for
 * verification.
 * </p>
 * 
 * <p>
 * Usage example in a Spock {@code Specification}:
 * </p>
 * <code><pre>
 * given:
 * Appender appenderMock = Mock()
 * addMockAppender(loggingClass, appenderMock)
 * 
 * when:
 * loggingClass.logSomething()
 * 
 * then:
 * 1 * appenderMock.doAppend({ it.getMessage() == message && it.getLevel() == Level.DEBUG })
 * </pre></code>
 * 
 * <p>
 * This class is thought as a {@code Mixin} to groovy tests.
 * </p>
 *
 * @author Michael Kutz <m.kutz@tarent.de>, 19.07.2012
 */
interface LogAppenderMockSupport<T> {

    /**
     * <p>
     * Adds the given {@link Appender} <code>appender</code> to the logger of the give {@link Class} <code>clazz</code>.
     * Use a mocked {@link Appender} to verify logging has happened.
     * </p>
     * 
     * @param clazz the {@link Class} whose log the appender should be added to.
     * @param appender the {@link Appender} to be added.
     */
    abstract void addAppenderMock(Class clazz, T appenderMock)
}
