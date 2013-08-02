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
import org.apache.log4j.Layout
import org.apache.log4j.Logger
import org.apache.log4j.SimpleLayout
import org.apache.log4j.WriterAppender

/**
 * {@link LogRecordingSupport} for Log4j logging.
 *
 * @author Michael Kutz <m.kutz@tarent.de>, 19.07.2012
 */
class Log4jLogRecordingSupport implements LogRecordingSupport {

    /** 
     * Holds all current records of logs.
     */
    private Map<String, ByteArrayOutputStream> recordings = [:]

    @Override
    void startLogRecording(Class clazz) {
        startLogRecording(clazz.name)
    }

    @Override
    String stopLogRecording(Class clazz) {
        return stopLogRecording(clazz.name)
    }

    @Override
    public Object startLogRecording(String name) {
        Logger logger = Logger.getLogger(name)
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        Layout layout = new SimpleLayout()
        Appender appender = new WriterAppender(layout, out)
        logger.addAppender(appender)

        recordings.put(name, out)
    }

    @Override
    public String stopLogRecording(String name) {
        ByteArrayOutputStream recording = recordings.remove(name)
        String recordedLog = null

        if (recording != null) {
            recordedLog = recording.toString()
            recording.close()
        }

        return recordedLog
    }
}