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

import org.apache.commons.io.IOUtils

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import com.jcraft.jsch.SftpException


/**
 * SSH client for system test verifications. This client is built around {@link JSch}, see
 * {@link http://www.jcraft.com/jsch/} for more details.
 *
 * @author Michael Kutz <m.kutz@tarent.de>, 25.06.2012
 */
class SshClient {

    /**
     * Max attempts retrying to get a session.
     */
    private static final int MAX_RETRY_ATTEMPTS = 5

    /**
     * The default connection timeout in millis.
     */
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10000

    /**
     * The host name of the remote system.
     */
    private String host

    /**
     * The port to use for connection to the remote system. Default is 22.
     */
    private int port

    /**
     * The user name used for login to the remote system.
     */
    private String username

    /**
     * The password used for login to the remote system.
     */
    private String password

    /**
     * {@link JSch} object to create {@link Session}s.
     */
    private JSch jsch = new JSch()

    /**
     * The current {@link Session}. Use {@link #getSession()} to get a never null, always connected session.
     */
    private Session session

    /**
     * Constructor.
     *
     * @param the host name of the remote system.
     * @param the user name used for login to the remote system.
     * @param the password used for login to the remote system.
     * @param the port to use for connection to the remote system. Default is 22.
     */
    private SshClient(String host, String username, int port = 22) {
        this.host = host
        this.username = username
        this.port = port
        JSch.setConfig("StrictHostKeyChecking", "no")
    }

    public SshClient(String host, String username, String password, int port = 22) {
        this(host, username, port)
        this.password = password
    }

    public SshClient(String host, String username, String privateKey, String passphrase, int port = 22) {
        this(host, username, port)
        if (passphrase) {
            jsch.addIdentity(privateKey, passphrase)
        } else {
            jsch.addIdentity(privateKey)
        }
    }

    /**
     * Executes the given <code>command</code> on the remote system synchronously (will only return after the
     * execution).
     *
     * @param command the command to be executed on the remote system.
     * @return a {@link SshExecutionResult} for the execution.
     */
    public SshExecutionResult executeCommand(String command) {
        ChannelExec channel = getExecChannel(command)

        InputStream is = channel.getInputStream()
        channel.connect()

        StringBuilder output = new StringBuilder()

        byte[] tmp = new byte[1024]
        while (true) {
            while (is.available() > 0) {
                int bytesRead = is.read(tmp)
                if (bytesRead < 0) {
                    break
                }
                output.append(new String(tmp, 0, bytesRead))
            }
            if (channel.isClosed()) {
                break
            }
            Thread.sleep(1000)
        }
        channel.disconnect()

        return new SshExecutionResult(channel.getExitStatus(), output.toString())
    }

    /**
     * Executes the given <code>command</code> on the remote system asynchronously (will return immediately).
     *
     * @param command the command to be executed on the remote system.
     */
    public void executeCommandAsync(String command) {
        ChannelExec channel = getExecChannel(command)
        channel.connect()
        channel.disconnect()
    }

    /**
     * Determine if at the given <code>remoteFilePath</code> there is a file or not (using an <code>ls</code> command).
     *
     * @param remoteFilePath the path to be checked.
     * @return <code>true</code> if the remote file (or directory) exists.
     */
    public boolean remoteFileExists(String remoteFilePath) {
        ChannelSftp channel = getSftpChannel()
        channel.connect()
        boolean remoteFileExists = !channel.ls(remoteFilePath).empty
        channel.disconnect()

        return remoteFileExists
    }

    /**
     * Reads a remote file and returns its content.
     *
     * @param remoteFilePath the path of the file to be read.
     * @return the content of the file as a String.
     */
    public String readRemoteFile(String remoteFilePath) {
        ChannelSftp channel = getSftpChannel()
        channel.connect()
        InputStream inputStream = channel.get(remoteFilePath)
        String content = null
        try {
            content = IOUtils.toString(inputStream)
        } finally {
            IOUtils.closeQuietly(inputStream)
        }
        channel.disconnect()
        return content
    }

    /**
     * Writes the content of a remote file. The former contents of the file will be overwritten -- use
     * {@link #readRemoteFile(String)} to preserve them.
     *
     * @param remoteFilePath the path of the file to be written.
     * @param content the new content to write.
     */
    public void writeRemoteFile(String remoteFilePath, String content) {
        ChannelSftp channel = getSftpChannel()
        channel.connect()
        InputStream inputStream = IOUtils.toInputStream(content)
        try {
            channel.put(inputStream, remoteFilePath)
        } finally {
            IOUtils.closeQuietly(inputStream)
        }
        channel.disconnect()
    }

    /**
     * Deletes a remote file.
     *
     * @param remoteFilePath the path of the file to be written.
     */
    public void deleteRemoteFile(String remoteFilePath) {
        ChannelSftp channel = getSftpChannel()
        channel.connect()
        try {
            channel.rm(remoteFilePath)
        } catch(SftpException e) {
            // if this fails the file does not exist anyway
        }
        channel.disconnect()
    }

    /**
     * @return a {@link Session} to open channels.
     */
    private Session getSession() {
        if (!session || !session.connected) {
            int attempt = 0
            while (attempt < MAX_RETRY_ATTEMPTS){
                session = jsch.getSession(username, host, port)
                if (password) {
                    session.setPassword(password)
                }
                try {
                    session.connect(DEFAULT_CONNECTION_TIMEOUT)
                    attempt = MAX_RETRY_ATTEMPTS
                } catch (JSchException e) {
                    if (attempt == MAX_RETRY_ATTEMPTS) {
                        throw(e)
                    }
                    attempt++
                }
            }
        }

        return session
    }

    /**
     * @return a prepared {@link ChannelExec} to execute a command on the remote system.
     */
    private ChannelExec getExecChannel(String command) {
        ChannelExec channel = getSession().openChannel("exec")
        channel.setInputStream(null)
        channel.setErrStream(System.err)
        channel.setCommand(command)
        return channel
    }

    /**
     * @return a {@link ChannelSftp} to evaluate the remote file system.
     */
    private ChannelSftp getSftpChannel() {
        ChannelSftp channel = getSession().openChannel("sftp")
        return channel
    }

    /**
     * Closes the {@link #session}.
     */
    private void disconnect() {
        session?.disconnect()
    }

    protected void finalize() {
        super.finalize()
        disconnect()
    }
}
