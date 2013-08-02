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

import com.sun.jersey.api.client.ClientResponse



/**
 * Simplified response class for REST calls. This class wraps a {@link HttpResponseDecorator},
 * see {@link http://groovy.codehaus.org/modules/http-builder/doc/rest.html} for more details.
 *
 * @author Michael Kutz <m.kutz@tarent.de>, 26.06.2012
 */
class RestResponse<T> {

    /**
     * The wrapped {@link ClientResponse}.
     */
    @Delegate private final ClientResponse response

    /**
     * The response body.
     */
    private T responseBody = null

    def RestResponse(ClientResponse response) {
        this.response = response
    }

    /**
     * @return the response body. See {@link ClientResponse#getEntity(T)}.
     */
    public T getBody() {
        if (responseBody == null) {
            responseBody = response.getEntity(T)
        }
        return responseBody
    }

    /**
     * @return the value of the location header. This should be set especially for POST call responses.
     */
    public String getLocation() {
        return response.headers.location.first
    }

    /**
     * Asserts that the {@link #response}'s status code equals the <code>expected</code> and returns a boolean to make
     * this method work in Spock's <code>then:</code> blocks.
     * 
     *  @param expectedStatus the expected HTTP status code.
     *  @return true if the {@link #response}'s status code equals the <code>expected</code>.
     */
    public boolean assertStatus(int expected) {
        assert response.status == expected
        return response.status == expected
    }
}
