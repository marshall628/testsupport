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

import spock.lang.Specification

/**
 * @author Michael Kutz <m.kutz@tarent.de>, 18.07.2012
 */
class SshExecutionResultSpec extends Specification {

    def "exit status 0 should be considered successful"() {
        expect:
        new SshExecutionResult(0, "some output").successful
    }

    def "any exit status not 0 should not be considered successful"() {
        expect:
        !new SshExecutionResult(exitStatus, "some output").successful

        where:
        exitStatus << [-99, -1, 1, 100]
    }
}
