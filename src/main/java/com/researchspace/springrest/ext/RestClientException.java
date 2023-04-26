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
package com.researchspace.springrest.ext;

import org.springframework.web.client.RestTemplate;

/**
 * Local class for exceptions thrown by {@link RestTemplate} whenever it encounters
 * client-side HTTP errors.
 */
public class RestClientException extends RuntimeException {

    /**
     * Serial.
     */
    private static final long serialVersionUID = -4790742590602186386L;

    /**
     * Error code.
     */
    private final Integer code;

    /**
     * Construct a new instance of {@code RestClientException} with the given message.
     * @param errorCode error code
     * @param msg the message
     */
    public RestClientException(final Integer errorCode, final String msg) {
        super(msg);
        code = errorCode;
    }

    public Integer getCode() {
        return code;
    }

}
