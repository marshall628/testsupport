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
 * Abstract super class for log recording support. Inheriting classes will give you support for recording logs of a
 * certain name to perform assertions about logged data.
 * </p>
 * 
 * <p>
 * Usage example in a Spock Specification:
 * </p>
 * <code><pre>
 * given:
 * startLogRecording(LoggingClass)
 * 
 * when:
 * loggingClass.loggingMethod(message)
 * 
 * then:
 * String log = stopLogRecording(LoggingClass)
 * log.contains(message)
 * !log.contains(password)
 * </pre></code>
 * 
 * <p>
 * This class is thought as a {@link Mixin} to groovy tests.
 * </p>
 * 
 * @author Michael Kutz <m.kutz@tarent.de>, 19.07.2012
 */
interface LogRecordingSupport {

    /**
     * Starts recording the log of a certain class.
     * 
     * @param clazz the {@link Class} whose log should be recorded.
     */
    void startLogRecording(Class clazz)

    /**
     * Stops recording the log of the given class and returns what was logged since {@link #startRecording(Class)} was
     * called.
     * 
     * @param clazz the {@link Class} whose log should be returned and no longer be recorded.
     * @return the recorded log.
     */
    String stopLogRecording(Class clazz)

    /**
     * Starts recording the log of a logger with the given <code>name</code>.
     * 
     * @param name the name of the logger to be recorded.
     */
    abstract startLogRecording(String name)

    /**
     * Starts recording the log of a logger with the given <code>name</code> and returns what was logged since
     * {@link #startRecording(String)} was called.
     * 
     * @param name the name of the logger whose log should be returned and no longer be recorded.
     * @return the recorded log.
     */
    abstract String stopLogRecording(String name)
}
