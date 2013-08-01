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
