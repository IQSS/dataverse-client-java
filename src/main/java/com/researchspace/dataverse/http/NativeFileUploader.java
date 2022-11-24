package com.researchspace.dataverse.http;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class NativeFileUploader {

    public HttpEntity<MultiValueMap<String, Object>> uploadFile(String apiKey, byte [] data, String fName){
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String,Object> multipartRequest = new LinkedMultiValueMap<>();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(AbstractOpsImplV1.apiHeader, apiKey);
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);//Main request's headers

        HttpHeaders requestHeadersAttachment = new HttpHeaders();
        //requestHeadersAttachment.setContentType(MediaType.APPLICATION_OCTET_STREAM);// extract mediatype from file extensionHttpEntity<ByteArrayResource> attachmentPart;
        ByteArrayResource fileAsResource = new ByteArrayResource(data){
            @Override
            public String getFilename(){
                return fName;
            }
        };
        HttpEntity attachmentPart = new HttpEntity<>(fileAsResource,requestHeadersAttachment);

        multipartRequest.set("file",attachmentPart);


        HttpHeaders requestHeadersJSON = new HttpHeaders();

        requestHeadersJSON.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = "{\"description\":\"My description.\",\"directoryLabel\":\"data/subdir1\",\"categories\":[\"Data\"], \"restrict\":\"false\", \"tabIngest\":\"false\"}";
        HttpEntity<String> requestEntityJSON = new HttpEntity<>(requestBody, requestHeadersJSON);

        multipartRequest.set("jsonData",requestEntityJSON);

        HttpEntity<MultiValueMap<String,Object>> requestEntity = new HttpEntity<>(multipartRequest,requestHeaders);//final request

        return requestEntity;

    }
}
