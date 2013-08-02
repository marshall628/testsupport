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
