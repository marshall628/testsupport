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
package com.github.mkutz.testsupport.clients.rest


import org.apache.http.HttpStatus

import spock.lang.Specification

import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.core.util.MultivaluedMapImpl

/**
 * @author Michael Kutz <m.kutz@tarent.de>, 18.07.2012
 */
class RestResponseSpec extends Specification {

    def "body, location and status should be taken from the wrapped ClientResponse"() {
        given:
        String expectedBody = "some body"
        String expectedLocation = "http://somewhere/resource/location"
        Integer expectedStatus = HttpStatus.SC_OK

        ClientResponse clientResponseMock = Mock()
        clientResponseMock.getEntity(_) >> expectedBody
        clientResponseMock.headers >> new MultivaluedMapImpl([location: [first: expectedLocation]])
        clientResponseMock.status >> expectedStatus

        when:
        RestResponse<String> restResponse = new RestResponse<String>(clientResponseMock)

        then:
        restResponse.body == expectedBody
        restResponse.location == expectedLocation
        restResponse.assertStatus(expectedStatus)
    }
}
