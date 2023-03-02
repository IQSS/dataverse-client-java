/** <pre>
Copyright 2016 ResearchSpace

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
</pre> */
package com.researchspace.dataverse.http;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.researchspace.dataverse.api.v1.DataverseConfig;
import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.springrest.ext.RestClientException;
import com.researchspace.springrest.ext.RestUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Abstract extension class for v1 operation classes.
 */
@Slf4j
public abstract class AbstractOpsImplV1 {

    String apiKey = "";
    String serverURL = "";
    String serverAPIURL = serverURL +"/api";
    String serverAPIv1URL = serverAPIURL +"/v1";
    protected RestTemplate template;

    protected AbstractOpsImplV1() {
        createTemplate();
    }

    public class CustomErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(final ClientHttpResponse response) throws IOException {
            return RestUtil.isError(response.getStatusCode());
        }

        @Override
        public void handleError(final ClientHttpResponse response) throws IOException, RestClientException {
            log.error("Error code returned {} with message {}",
                    response.getStatusCode().value(),
                    response.getStatusText());
            throw new RestClientException(response.getStatusCode().value(), response.getStatusText());
        }
    }

    protected void setTemplate(final RestTemplate template) {
        this.template = template;
    }

    public static final String API_HEADER = "X-Dataverse-key";

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    public void setServerURL(final String serverURL) {
        this.serverURL = serverURL;
        serverAPIURL = serverURL + "/api";
        serverAPIv1URL = serverAPIURL +"/v1";
    }

    public void configure(final DataverseConfig config) {
        setApiKey(config.getApiKey());
        setServerURL(config.getServerURL().toString());
    }

    <T> void handleError(final ResponseEntity<DataverseResponse<T>> resp) throws RestClientException {
        log.debug("{}", resp.getBody());
        if (RestUtil.isError(resp.getStatusCode())) {
            log.error("Error code returned %d with message [%s]", resp.getStatusCodeValue(),
                    resp.getBody().getMessage());
            throw new RestClientException(resp.getStatusCodeValue(), resp.getBody().getMessage());
        }
    }

    void createTemplate() {
        template = new RestTemplate();
        template.setErrorHandler(new CustomErrorHandler());
    }

    String createV1Url(final String ... pathComponents) {
        final String url = serverAPIv1URL + "/" + StringUtils.join(pathComponents, "/") ;
        log.debug("URL is {}", url);
        return url;
    }

    String createAdminUrl(final String ... pathComponents) {
        final String url = serverAPIURL + "/" + StringUtils.join(pathComponents, "/") ;
        log.debug("URL is {}", url);
        return url;
    }

    HttpHeaders addAPIKeyToHeader() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        addApiKey(headers);
        return headers;
    }

    void addApiKey(final HttpHeaders headers) {
        headers.add(API_HEADER, apiKey);
    }

}
