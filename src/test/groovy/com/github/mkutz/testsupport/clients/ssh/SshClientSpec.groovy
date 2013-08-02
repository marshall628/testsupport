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
package com.github.mkutz.testsupport.clients.ssh

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import spock.lang.Specification

/**
 * @author Michael Kutz <m.kutz@tarent.de>, 29.06.2012
 */
class SshClientSpec extends Specification {

    static final String HOST = "localhost"
    static final String USERNAME = "username"
    static final String PASSWORD = "password"
    static final int PORT = 23
    static final String CONSOLE_OUTPUT = "Just some mocked console output for verification..."

    JSch jschMock = Mock()
    Session sessionMock = Mock()
    ChannelExec channelExecMock = Mock()
    ChannelSftp channelSftpMock = Mock()

    SshClient sshClient = new SshClient(HOST, USERNAME, PASSWORD, PORT)

    def setup() {
        sshClient.jsch = jschMock

        jschMock.getSession(USERNAME, HOST, PORT) >> sessionMock

        sessionMock.isConnected() >>> [false, true]
        sessionMock.openChannel("exec") >> channelExecMock
        sessionMock.openChannel("sftp") >> channelSftpMock
    }

    def "execute command should return the expected exit status and output"() {
        given:
        String command = "command"
        int exitStatus = 0

        and:
        1 * channelExecMock.getInputStream() >> new ByteArrayInputStream(CONSOLE_OUTPUT.bytes)
        2 * channelExecMock.isClosed() >>> [false, true]
        1 * channelExecMock.getExitStatus() >> exitStatus

        when:
        SshExecutionResult result = sshClient.executeCommand(command)

        then: "the session should be connected and use the given password"
        1 * sessionMock.setPassword(PASSWORD)
        1 * sessionMock.connect(_ as Integer)

        and: "the exec channel should use the given command, be connected and disconnected"
        1 * channelExecMock.setCommand(command)
        1 * channelExecMock.connect()
        1 * channelExecMock.disconnect()

        and: "the expected exit status and console output should be returned"
        result.successful
        result.output == CONSOLE_OUTPUT
    }

    def "cheching existance of a remote file should work"() {
        given:
        String remoteFilePath = "/tmp/exists.txt"
        1 * channelSftpMock.ls(remoteFilePath) >> [remoteFilePath]

        when:
        boolean result = sshClient.remoteFileExists(remoteFilePath)

        then: "the session should be connected and use the given password"
        1 * sessionMock.setPassword(PASSWORD)
        1 * sessionMock.connect(_ as Integer)

        and: "the sftp channel be connected and disconnected"
        1 * channelSftpMock.connect()
        1 * channelSftpMock.disconnect()

        and: "the expected result should be returned"
        result == true
    }

    def "a disconnected session should be reconnected on demand"() {
        given:
        sessionMock.isConnected >> false

        and:
        String remoteFilePath = "/tmp/exists.txt"
        1 * channelSftpMock.ls(remoteFilePath) >> [remoteFilePath]

        when:
        sshClient.remoteFileExists(remoteFilePath)

        then:
        1 * sessionMock.connect(_ as Integer)
    }
}
