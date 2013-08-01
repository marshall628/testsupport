/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
