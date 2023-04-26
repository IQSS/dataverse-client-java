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

import java.net.URL;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
/**
 * Configures the server, apikey and root dataverse alias
 * @author rspace
 */
@EqualsAndHashCode(of={"serverURL"})
@AllArgsConstructor
@ToString()
public class DataverseConfig {

    private @Getter
    final URL serverURL;

    /**
     * The API Key provided from Dataverse account
     * @return the apiKey
     */
    private @Getter
    final String apiKey;
    private @Getter
    final String repositoryName;

}
