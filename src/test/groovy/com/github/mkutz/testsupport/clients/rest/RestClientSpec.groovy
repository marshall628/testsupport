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
