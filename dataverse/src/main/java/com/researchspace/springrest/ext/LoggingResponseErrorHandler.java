package com.researchspace.springrest.ext;


import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import lombok.extern.slf4j.Slf4j;
/**
 * Logs a 400 response instead of throwing exception, so we can capture the error message from Json
 * 
 * @author rspace
 * @see http://springinpractice.com/2013/10/07/handling-json-error-object-responses-with-springs-resttemplate/
 *
 */
@Slf4j
public class LoggingResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("Response error: {} {}", response.getStatusCode(), response.getStatusText());
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return RestUtil.isError(response.getStatusCode());
    }
}
