package com.researchspace.dataverse.http;

import org.springframework.core.io.AbstractResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * NativeFileUploader is  helper class that performs upload of files using native API
 */
public class NativeFileUploader {

    public HttpEntity<MultiValueMap<String, Object>> createFileUploadEntity(final FileUploadMetadata metadata, final String apiKey, final AbstractResource resource){

        final MultiValueMap<String,Object> multipartRequest = new LinkedMultiValueMap<>();

        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(AbstractOpsImplV1.API_HEADER, apiKey);
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);//Main request's headers

        final HttpHeaders requestHeadersAttachment = new HttpHeaders();

        final HttpEntity<AbstractResource> attachmentPart = new HttpEntity<>(resource, requestHeadersAttachment);
        multipartRequest.set("file",attachmentPart);

        final HttpHeaders requestHeadersJSON = new HttpHeaders();
        requestHeadersJSON.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<FileUploadMetadata> requestEntityJSON = new HttpEntity<>(metadata, requestHeadersJSON);
        multipartRequest.set("jsonData",requestEntityJSON);

        final HttpEntity<MultiValueMap<String,Object>> requestEntity = new HttpEntity<>(multipartRequest,requestHeaders);//final request
        return requestEntity;

    }
}
