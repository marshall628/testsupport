package com.github.mkutz.testsupport.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.github.mkutz.testsupport.logging.Log4jLogRecordingSupport;

import spock.lang.Specification

/**
 * @author Michael Kutz <m.kutz@tarent.de>, 20.07.2012
 */
@Mixin(Log4jLogRecordingSupport)
class Log4jLogRecordingSupportSpec extends Specification {

    def "verification of logging contents should be possible"() {
        given:
        LoggingClass loggingClass = new LoggingClass()
        String message = "some message"
        startLogRecording(LoggingClass)
        loggingClass.loggingMethod(message)
        loggingClass.loggingMethod("some more message")

        when:
        String log = stopLogRecording(LoggingClass)

        then:
        log.contains(message)
    }

    def "if no logging happend the record should be empty"() {
        given:
        LoggingClass loggingClass = new LoggingClass()
        startLogRecording(LoggingClass)
        loggingClass.notLoggingMethod()

        when:
        String log = stopLogRecording(LoggingClass)

        then:
        log.empty
    }

    def "starting another recording for one class should reset the record"() {
        given:
        LoggingClass loggingClass = new LoggingClass()
        String message1 = "some erlier message"
        startLogRecording(LoggingClass)
        loggingClass.loggingMethod(message1)
        String message2 = "some message"
        startLogRecording(LoggingClass)
        loggingClass.loggingMethod(message2)

        when:
        String log = stopLogRecording(LoggingClass)

        then:
        log.contains(message2)
        !log.contains(message1)
    }

    def "stopping an unkown recording should simply return null"() {
        expect:
        null == stopLogRecording("not started log recording")
    }

    class LoggingClass {
        private static Logger LOG = LoggerFactory.getLogger(LoggingClass)
        void loggingMethod(String message) {
            LOG.debug(message)
        }
        void notLoggingMethod() {
        }
    }
}
