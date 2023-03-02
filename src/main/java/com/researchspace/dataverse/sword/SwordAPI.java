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
package com.researchspace.dataverse.sword;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.swordapp.client.AuthCredentials;
import org.swordapp.client.CollectionEntries;
import org.swordapp.client.Deposit;
import org.swordapp.client.DepositReceipt;
import org.swordapp.client.ProtocolViolationException;
import org.swordapp.client.SWORDClient;
import org.swordapp.client.SWORDClientException;
import org.swordapp.client.SWORDError;

import com.researchspace.dataverse.entities.Identifier;

import lombok.extern.slf4j.Slf4j;

/**
 * Operations using SWORD client library
 * This is an internal package for calling SWORD-API.
 * @author rspace
 */
@Slf4j
public class SwordAPI {

    private static final String SWORD_FRAGMENT = "/dvn/api/data-deposit/v1.1/swordv2/";

    private static final String APPLICATION_ZIP = "application/zip";

    private static final String ZIP_PACKAGING = "http://purl.org/net/sword/package/SimpleZip";

    /**

     * @param file
     * @param apiKey
     * @param dataverseServer Server root e.g. "https://apitest.dataverse.org"
     * @param doi FRagment e.g.  "10.5072/FK2/MGADL1"
     * @return
     * @throws IOException
     * @throws SWORDClientException
     * @throws SWORDError
     * @throws ProtocolViolationException
     */
    public DepositReceipt deposit(final File file, final String apiKey, final URI dataverseServer, final String doi)
            throws IOException, SWORDClientException, SWORDError, ProtocolViolationException {
        return this.deposit(new FileInputStream(file), file.getName(), apiKey, dataverseServer, doi);
    }

    /**
     * Creates a deposit object to upload a file into a dataverse instance using the SWORD library client.
     *
     * @param is Data coming as a stream.
     * @param filename Name of the file to upload.
     * @param apiKey Key used to authenticate actions into the goal dataverse instance.
     * @param dataverseServer URL of the dataverse instance to attack.
     * @param doi To identify the dataset that is the goal of the file upload.
     * @return Information of the result of the upload via a {@code DepositReceipt} instance.
     * @throws IOException Thrown when a IO error occurs, which is a general error.
     * @throws SWORDClientException Thrown when an exception happens inside the SWORD client.
     * @throws SWORDError Thrown when an exception happens inside the SWORD client.
     * @throws ProtocolViolationException Thrown for unknown reasons.
     */
    public DepositReceipt deposit(final InputStream is, final String filename, final String apiKey, final URI dataverseServer, final String doi)
            throws IOException, SWORDClientException, SWORDError, ProtocolViolationException {
        final SWORDClient cli = new SWORDClient();
        final Deposit dep = new Deposit();
        dep.setFilename(filename);
        dep.setFile(is);
        dep.setMimeType(APPLICATION_ZIP);
        dep.setPackaging(ZIP_PACKAGING);

        final AuthCredentials cred = new AuthCredentials(apiKey, "");

        final String depositURI = dataverseServer.toString() + "/dvn/api/data-deposit/v1.1/swordv2/edit-media/study/doi:"
                + doi;
        final DepositReceipt rct = cli.deposit(depositURI, dep, cred);
        log.info("Deposit received with status {}" ,rct.getStatusCode());
        return rct;
    }

    public CollectionEntries getEntries(final String apiKey, final URI dataverseServer, final String doi)
            throws SWORDClientException, ProtocolViolationException {
        final String depositURI = dataverseServer.toString() + SWORD_FRAGMENT +"statement/study/doi:"
                + doi;
        final SWORDClient cli = new SWORDClient();
        final AuthCredentials cred = new AuthCredentials(apiKey, "");
        return cli.listCollection(depositURI, cred);
    }

    public void deleteFile(final String apiKey, final String fileId)
            throws SWORDClientException, SWORDError, ProtocolViolationException {
        final SWORDClient cli = new SWORDClient();
        final AuthCredentials cred = new AuthCredentials(apiKey, "");
        cli.delete(fileId, cred);
    }

    public void deleteFile(final String apiKey, final URI dataverseServer,
            final Identifier datasetIdentifier, final String fileName)
                    throws SWORDClientException, SWORDError, ProtocolViolationException {
        final String fileId = dataverseServer.toString() + SWORD_FRAGMENT + "edit-media/file/"
                + datasetIdentifier.getId() + "/" + fileName;
        deleteFile(apiKey, fileId);
    }

}
