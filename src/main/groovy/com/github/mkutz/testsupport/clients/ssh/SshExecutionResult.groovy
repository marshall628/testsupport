package com.github.mkutz.testsupport.clients.ssh

import groovy.transform.Immutable

/**
 * The class represents a command execution on a remote machine.
 *
 * @author Michael Kutz <m.kutz@tarent.de>, 27.06.2012
 */
@Immutable
class SshExecutionResult {

    /**
     * The exit status code returned by the command execution.
     */
    int exitStatus

    /**
     * The (console) output of the command.
     */
    String output

    /**
     * @return true if command execution with exit status 0.
     */
    boolean isSuccessful() {
        return exitStatus == 0
    }
}
