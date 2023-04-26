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

import java.io.IOException;
import java.util.List;

import com.researchspace.dataverse.entities.DataverseGet;
import com.researchspace.dataverse.entities.DataverseObject;
import com.researchspace.dataverse.entities.DataversePost;
import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.entities.DvMessage;
import com.researchspace.dataverse.entities.Identifier;
import com.researchspace.dataverse.entities.facade.DatasetFacade;
import com.researchspace.springrest.ext.RestClientException;
import com.researchspace.springrest.ext.SWORDException;
/**
 * Operations on the Dataverse.
 */
public interface DataverseOperations {


    /**
     * Create a new dataverse. The supplied {@link DataversePost} must contain as a minimum:
     *  <ul>
     *   <li> alias
     *   <li> Name
     *    <li> At least one contact email
     *  </ul>
     *  @param parentDataverseAlias The owning Dataverse
     *  @throws IllegalArgumentException if any mandatory fields are <code>null</code>
     *  @throws RestClientException
     */
    DataverseResponse<DataversePost> createNewDataverse (String parentDataverseAlias, DataversePost toCreate)
            throws RestClientException;

    /**
     * Creates a new Dataset within the specified dataverse.
     * @param facade
     * @param dataverseAlias
     * @return The id of the created dataset
     * @throws IOException
     * @throws RestClientException
     */
    Identifier createDataset(DatasetFacade facade, String dataverseAlias)
            throws RestClientException;


    /**
     * Create a dataset from JSON within the specified dataverse.
     * @param dataSetJson representation of the dataset as expected by dataverse for a dataset creation
     * @param dataverseAlias alias
     * @return The id of the created dataset
     * @throws RestClientException
     */
    Identifier createDataset(String dataSetJson, String dataverseAlias) throws RestClientException;

    /**
     * Gets an overview of the contents of the specified Dataverse
     * @param dataverseAlias
     * @return
     */
    List<DataverseObject> getDataverseContents(String dataverseAlias) throws RestClientException;

    /**
     * Returns complete information on the dataverse
     * @param dataverseAlias
     * @return data representation of the dataverse
     */
    DataverseGet getDataverseById(String dataverseAlias) throws RestClientException;

    /**
     * Deletes a dataverse
     * @param dataverseAlias numeric or unique identifier of the dataverse
     * @return A DataverseResponse<DvMessage><DvMessage>. If deleted successfully, getData will contain a message.
     *      If status is error ( e.g. couldn't be deleted) the DataverseResponse will contain a message
     */
    DataverseResponse<DvMessage> deleteDataverse(String dataverseAlias);

    /**
     * Publishes the specified Dataverse.
     * @param dataverseAlias numeric or unique identifier of the dataverse
     * @return The updated {@link DataversePost} object
     */
    DataverseResponse<DataversePost> publishDataverse(String dataverseAlias);

    /**
     * Delete a file in a dataset.
     * @param fileId
     */
    void deleteFile(String fileId) throws SWORDException;

}
