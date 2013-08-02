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

import spock.lang.Ignore
import spock.lang.Specification

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.PartialRequestBuilder

/**
 * @author Michael Kutz <m.kutz@tarent.de>, 29.06.2012
 */
class RestClientSpec extends Specification {

    Client clientMock = Mock()
    PartialRequestBuilder requestBuilderMock = Mock()
    RestClient restClient = new RestClient("http://restservice.de")

    def setup() {
        restClient.client = clientMock

        clientMock.resource(_ as String) >> requestBuilderMock
    }

    @Ignore("currently not working in this setup")
    def "call"() {
        when:
        RestResponse<Map> response = restClient.post("resource", [a: "map"])

        then:
        response != null
    }
}
