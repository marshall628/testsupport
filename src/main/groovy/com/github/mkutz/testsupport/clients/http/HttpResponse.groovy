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
package com.github.mkutz.testsupport.clients.http

import static org.apache.http.HttpHeaders.*
import groovy.util.slurpersupport.GPathResult

import org.ccil.cowan.tagsoup.Parser


/**
 * Simplified response class for HTTP calls. This class wraps a {@link org.apache.http.HttpResponse}.
 *
 * @author Michael Kutz <m.kutz@tarent.de>, 02.10.2012
 */
class HttpResponse {

    /**
     * The wrapped org.apache.http.HttpResponse object.
     */
    @Delegate private final org.apache.http.HttpResponse response

    HttpResponse(org.apache.http.HttpResponse response) {
        this.response = response
    }

    /**
     * @return the {@link #response}'s status code.
     */
    int getStatus() {
        return response.getStatusLine().statusCode
    }

    /**
     * Returns the HTTP {@link #response}'s entity content (see {@link org.apache.http.HttpResponse#getEntity()} and
     * {@link org.apache.http.HttpEntity#getContent()}) and returns it as a {@link GPathResult}. Since {@link Parser}
     * is utilized this is also robust for not well-formed HTML. 
     * 
     * @return the {@link #response}'s body as a {@link GPathResult}.
     */
    GPathResult getBody() {
        XmlSlurper slurper = new XmlSlurper(new Parser())
        return slurper.parse(response.entity.content)
    }

    /**
     * @return the {@link #response}'s location header value.
     */
    String getLocation() {
        return getHeaderValue(LOCATION)
    }

    /**
     * @return the {@link #response}'s content type header value.
     */
    String getContentType() {
        return getHeaderValue(CONTENT_TYPE)
    }

    /**
     * @return the value of the first header named <code>headerName</code> as a String. If the header is not present
     *      <code>null</code> is returned. 
     */
    private String getHeaderValue(String headerName) {
        return response.getFirstHeader(headerName)?.value
    }
}
