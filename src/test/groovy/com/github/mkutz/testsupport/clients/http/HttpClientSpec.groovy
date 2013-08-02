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

import org.apache.http.HttpStatus
import spock.lang.Ignore
import spock.lang.Specification

/**
 * @author Michael Kutz <m.kutz@tarent.de>, 02.10.2012
 */
@Ignore("These only work on machines connected to the internet and not running a web server")
class HttpClientSpec extends Specification {

    HttpClient httpClient = new HttpClient()

    def "simple HTTP GET call to google.com should work"() {
        when:
        HttpResponse response = httpClient.get("http://www.google.de")

        then:
        response.status == HttpStatus.SC_OK
        response.contentType.contains("text/html")
    }

    def "simple HTTP POST call to tarent.de should work"() {
        given:
        String url = "http://www.tarent.de/web/tarent/home?p_p_id=77&amp;p_p_lifecycle=0&amp;p_p_url_type=0&amp;p_p_state=maximized&amp;p_p_mode=view&amp;_77_struts_action=%2Fjournal_content_search%2Fsearch"

        when:
        HttpResponse response = httpClient.post(url,  ["_77_keywords": "test"])

        then:
        response.status == HttpStatus.SC_OK
        response.body.head.title.text().contains("tarent")
    }

    def "redirecting should be detectable"() {
        when:
        HttpResponse response = httpClient.get("http://www.google.com")

        then:
        response.status == HttpStatus.SC_MOVED_TEMPORARILY
        response.location == "http://www.google.de/"
    }

    def "another redirect"() {
        when:
        HttpResponse response = httpClient.get("http://gmail.com")

        then:
        response.status == HttpStatus.SC_MOVED_PERMANENTLY
        response.location == "http://mail.google.com/mail/"
    }
}
