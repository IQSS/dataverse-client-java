/**
<pre>
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
 </pre>
 */
package com.researchspace.dataverse.api.v1;

import java.text.ParseException;
import java.time.LocalDateTime;

import com.researchspace.springrest.ext.RestClientException;

/**
 * Operations on Users (tokens).
 */
public interface UsersOperations {

    /**
     * Get token expiration date.
     * @return java.time.LocalDateTime token expiration.
     * @throws ParseException
     * @throws RestClientException
     */
    LocalDateTime getTokenExpirationDate() throws ParseException;

    /**
     * Get token expiration text returned by dataverse.
     * @return String as the form of <Token XXXXX expires on yyyy-MM-dd HH:mm:ss.SSS>.
     * @throws ParseException
     * @throws RestClientException
     */
    String getTokenExpiration() throws ParseException;

}
