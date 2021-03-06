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

import org.apache.http.HttpRequest
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.params.HttpClientParams
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair

/**
 * <p>
 * This class wraps a {@link HttpClient} to simply provide highly abstracted methods for component or system test of
 * HTTP interfaces (for instance to verify SAML usage). It provides protected methods for generic post and get calls to
 * its sub classes.
 * </p>
 * 
 * <p>
 * It is thought as a super class for specialized clients that provide one method per link from the service document.
 * </p>
 *
 * @author Michael Kutz <m.kutz@tarent.de>, 02.10.2012
 */
class HttpClient {

    protected DefaultHttpClient httpClient = new DefaultHttpClient()

    HttpClient() {
        HttpClientParams.setRedirecting(httpClient.getParams(), false)
    }

    /**
     * Executes an HTTP GET method to the given <code>url</code>.
     * 
     * @param url the URL to call.
     * @return the resulting {@link HttpResponse}.
     */
    HttpResponse get(String url) {
        return execute(new HttpGet(url))
    }

    /**
     * Executes an HTTP POST method to the given <code>url</code> with the given parameters.
     * 
     * @param url the URL to call.
     * @param parameters the parameters to send.
     * @return the resulting {@link HttpResponse}.
     */
    HttpResponse post(String url, Map<String, String> parameters) {
        HttpPost request = new HttpPost(url)

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(parameters.size())
        parameters.each {
            nameValuePairs.add(new BasicNameValuePair(it.key, it.value))
        }
        request.entity = new UrlEncodedFormEntity(nameValuePairs)

        return execute(request)
    }

    /**
     * Executes the given <code>request</code>.
     * 
     * @param request the request to be executed.
     * @return the resulting {@link HttpResponse}.
     */
    private HttpResponse execute(HttpRequest request) {
        return new HttpResponse(httpClient.execute(request))
    }
}
