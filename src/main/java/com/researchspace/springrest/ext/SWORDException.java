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

import org.swordapp.client.SWORDClient;

/**
 * Class for exceptions thrown by {@link SWORDClient} whenever it encounters an error.
 */
public class SWORDException extends RuntimeException {

    /**
     * Serial.
     */
    private static final long serialVersionUID = 6151353497066402179L;

    /**
     * Construct a new instance of {@code RestClientException} with the given message.
     * @param msg the message
     */
    public SWORDException(final String msg) {
        super(msg);
    }

    /**
     * Construct a new instance of {@code RestClientException} with the given message.
     * @param msg the message
     */
    public SWORDException(final String msg, final Exception e) {
        super(msg, e.getCause());
    }

}
