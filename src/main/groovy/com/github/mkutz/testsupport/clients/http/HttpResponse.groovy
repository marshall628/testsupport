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
