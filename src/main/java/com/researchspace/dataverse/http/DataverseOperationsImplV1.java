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

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.noNullElements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.abdera.model.Entry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.swordapp.client.CollectionEntries;
import org.swordapp.client.ProtocolViolationException;
import org.swordapp.client.SWORDClientException;
import org.swordapp.client.SWORDError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.researchspace.dataverse.api.v1.DatasetOperations;
import com.researchspace.dataverse.api.v1.DataverseOperations;
import com.researchspace.dataverse.api.v1.InfoOperations;
import com.researchspace.dataverse.api.v1.MetadataOperations;
import com.researchspace.dataverse.api.v1.UsersOperations;
import com.researchspace.dataverse.entities.Dataset;
import com.researchspace.dataverse.entities.DatasetFileList;
import com.researchspace.dataverse.entities.DatasetVersion;
import com.researchspace.dataverse.entities.DataverseGet;
import com.researchspace.dataverse.entities.DataverseObject;
import com.researchspace.dataverse.entities.DataversePost;
import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.entities.DvMessage;
import com.researchspace.dataverse.entities.Identifier;
import com.researchspace.dataverse.entities.MetadataBlock;
import com.researchspace.dataverse.entities.PublishedDataset;
import com.researchspace.dataverse.entities.Version;
import com.researchspace.dataverse.entities.facade.DatasetBuilder;
import com.researchspace.dataverse.entities.facade.DatasetFacade;
import com.researchspace.dataverse.sword.SwordAPI;
import com.researchspace.springrest.ext.RestClientException;
import com.researchspace.springrest.ext.SWORDException;

import lombok.extern.slf4j.Slf4j;

/**
 * Operation classes implementation.
 */
@Slf4j
public class DataverseOperationsImplV1 extends AbstractOpsImplV1
implements DatasetOperations, MetadataOperations, InfoOperations,
DataverseOperations, UsersOperations {

    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#getDataverseById(java.lang.String)
     */
    @Override
    public DataverseGet getDataverseById(final String dataverseAlias) {
        final String url = createV1Url("dataverses" , dataverseAlias);
        log.debug(url);
        final HttpEntity<String> entity = createHttpEntity("");
        final ParameterizedTypeReference<DataverseResponse<DataverseGet>> type =
                new ParameterizedTypeReference<DataverseResponse<DataverseGet>>() {
        };
        final ResponseEntity<DataverseResponse<DataverseGet>> resp = template.exchange(url, HttpMethod.GET, entity, type);
        log.debug(resp.getBody().toString());
        return resp.getBody().getData();
    }

    @Override
    public DataverseResponse<DvMessage> deleteDataverse(final String dataverseAlias) {
        final String url = createV1Url("dataverses", dataverseAlias);
        log.debug(url);

        final HttpEntity<String> entity = createHttpEntity("");
        final ParameterizedTypeReference<DataverseResponse<DvMessage>> type =
                new ParameterizedTypeReference<DataverseResponse<DvMessage>>() {};
                final ResponseEntity<DataverseResponse<DvMessage>> resp = template.exchange(url, HttpMethod.DELETE, entity, type);
                log.debug(resp.getBody().toString());
                return resp.getBody();

    }

    @Override
    public DataverseResponse<DataversePost> createNewDataverse(final String parentDv, final DataversePost toCreate)
            throws RestClientException {
        isTrue(!isEmpty(toCreate.getAlias()), "Alias must be specified");
        isTrue(!isEmpty(toCreate.getName()), "Name must be specified");
        noNullElements(toCreate.getDataverseContacts(), "At least 1 email contact must be provided");
        isTrue(!isEmpty(toCreate.getAlias()), "Alias must be specified");
        final String url = createV1Url("dataverses", parentDv);
        final String json = marshalDataset(toCreate);
        final HttpEntity<String> entity = createHttpEntity(json);
        final ParameterizedTypeReference<DataverseResponse<DataversePost>> type =
                new ParameterizedTypeReference<DataverseResponse<DataversePost>>() {
        };
        final ResponseEntity<DataverseResponse<DataversePost>> resp =
                template.exchange(url, HttpMethod.POST, entity, type);
        log.debug(resp.getBody().toString());
        handleError(resp);
        return resp.getBody();

    }

    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#createDataset(com.researchspace.dataverse.entities.facade.DatasetFacade, java.lang.String)
     */
    @Override
    public Identifier createDataset(final DatasetFacade facade, final String dataverseAlias) throws RestClientException {
        final String url = createV1Url("dataverses", dataverseAlias, "datasets");
        final String json = getJsonFromFacade(facade);
        log.debug(json);
        final HttpEntity<String> entity = createHttpEntity(json);
        final ParameterizedTypeReference<DataverseResponse<Identifier>> type =
                new ParameterizedTypeReference<DataverseResponse<Identifier>>() {
        };
        final ResponseEntity<DataverseResponse<Identifier>> resp = template.exchange(url, HttpMethod.POST, entity, type);
        handleError(resp);
        return resp.getBody().getData();
    }

    @Override
    public Identifier createDataset(final String dataSetJson, final String dataverseAlias) throws RestClientException {
        final String url = createV1Url("dataverses", dataverseAlias, "datasets");
        final HttpEntity<String> entity = createHttpEntity(dataSetJson);
        final ParameterizedTypeReference<DataverseResponse<Identifier>> type =
                new ParameterizedTypeReference<DataverseResponse<Identifier>>() {
        };
        final ResponseEntity<DataverseResponse<Identifier>> resp = template.exchange(url, HttpMethod.POST, entity, type);
        handleError(resp);
        return resp.getBody().getData();
    }


    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#updateDataset(com.researchspace.dataverse.entities.facade.DatasetFacade, com.researchspace.dataverse.entities.Identifier)
     */
    @Override
    public DatasetVersion updateDataset(final DatasetFacade facade, final Identifier id) throws RestClientException {
        final String url = createV1Url("datasets",  id.getId() + "", "versions", ":draft");
        final Dataset ds = new DatasetBuilder().build(facade);
        final String json = marshalDataset(ds.getDatasetVersion());
        final HttpEntity<String> entity = createHttpEntity(json);
        final ParameterizedTypeReference<DataverseResponse<DatasetVersion>> type =
                new ParameterizedTypeReference<DataverseResponse<DatasetVersion>>() {
        };
        final ResponseEntity<DataverseResponse<DatasetVersion>> resp = template.exchange(url, HttpMethod.PUT, entity,type);
        handleError(resp);
        return resp.getBody().getData();

    }

    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#updateDataset(com.researchspace.dataverse.entities.facade.DatasetFacade, com.researchspace.dataverse.entities.Identifier)
     */
    @Override
    public DatasetVersion updateDataset(final Dataset dataset, final Identifier id) throws RestClientException {
        final String url = createV1Url("datasets",  id.getId() + "", "versions", ":draft");
        final String json = marshalDataset(dataset.getDatasetVersion());
        final HttpEntity<String> entity = createHttpEntity(json);
        final ParameterizedTypeReference<DataverseResponse<DatasetVersion>> type =
                new ParameterizedTypeReference<DataverseResponse<DatasetVersion>>() {
        };
        final ResponseEntity<DataverseResponse<DatasetVersion>> resp = template.exchange(url, HttpMethod.PUT, entity,type);
        handleError(resp);
        return resp.getBody().getData();

    }

    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#getDataset(com.researchspace.dataverse.entities.Identifier)
     */
    @Override
    public Dataset getDataset(final Identifier dsIdentifier) throws RestClientException {
        final String url = createV1Url("datasets",  dsIdentifier.getId() + "");
        final HttpEntity<String> entity = createHttpEntity("");
        final ParameterizedTypeReference<DataverseResponse<Dataset>> type =
                new ParameterizedTypeReference<DataverseResponse<Dataset>>() {
        };
        final ResponseEntity<DataverseResponse<Dataset>> resp = template.exchange(url, HttpMethod.GET, entity, type);
        handleError(resp);
        return resp.getBody().getData();
    }

    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#getDatasetVersions(com.researchspace.dataverse.entities.Identifier)
     */
    @Override
    public List<DatasetVersion> getDatasetVersions (final Identifier dsIdentifier) throws RestClientException {
        final String url = createV1Url("datasets",  dsIdentifier.getId() + "", "versions");
        final HttpEntity<String> entity = createHttpEntity("");
        final ParameterizedTypeReference<DataverseResponse<List<DatasetVersion>>> type =
                new ParameterizedTypeReference<DataverseResponse<List<DatasetVersion>>>() {};
                final ResponseEntity<DataverseResponse<List<DatasetVersion>>> resp = template.exchange(url, HttpMethod.GET, entity, type);
                log.debug("{}", resp.getBody());
                handleError(resp);
                return resp.getBody().getData();
    }

    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#uploadFile(java.lang.String, java.io.File)
     */
    @Override
    public void uploadFile (final String doi, final File file) throws SWORDException{
        final SwordAPI uploader = new SwordAPI();
        try {
            uploader.deposit(file, apiKey, new URI(serverURL), doi);
        } catch (IOException | SWORDClientException  | ProtocolViolationException | URISyntaxException e) {
            final String msg = String.format("Couldn't upload file %s with doi %s : %s",
                    file.getName(), doi, e.getMessage());
            log.error(msg);
            throw new SWORDException(msg);
        } catch (final SWORDError error) {
            if (!isEmpty(error.getErrorBody())) {
                final String msg = String.format("SwordError: %s", error.getErrorBody());
                log.error(msg);
                throw new SWORDException(msg);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#uploadFile(java.lang.String, java.io.File)
     */
    @Override
    public List<String> listFilesUrls (final String doi) throws SWORDException {
        final SwordAPI swordApi = new SwordAPI();
        final List<String> fileNames = new ArrayList<>();
        try {
            final CollectionEntries entries = swordApi.getEntries(apiKey, new URI(serverURL), doi);
            for (final Entry entry : entries.getEntries()) {
                fileNames.add(entry.getIdElement().getText());
            }
            return fileNames;
        } catch (SWORDClientException  | ProtocolViolationException | URISyntaxException e) {
            final String msg = String.format("Couldn't download file names with doi %s : %s", doi, e.getMessage());
            log.error(msg);
            throw new SWORDException(msg);
        }
    }

    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#deleteDataset(com.researchspace.dataverse.entities.Identifier)
     */
    @Override
    public DvMessage deleteDataset(final Identifier dsIdentifier) throws RestClientException {
        final String url = createV1Url("datasets",  dsIdentifier.getId() +"" );

        final HttpEntity<String> entity = createHttpEntity("");
        final ParameterizedTypeReference<DataverseResponse<DvMessage>> type =
                new ParameterizedTypeReference<DataverseResponse<DvMessage>>() {};
                final ResponseEntity<DataverseResponse<DvMessage>> resp = template.exchange(url, HttpMethod.DELETE, entity, type);
                handleError(resp);
                return resp.getBody().getData();
    }

    private HttpEntity<String> createHttpEntity(final String body) {
        final HttpHeaders headers = addAPIKeyToHeader();
        return new HttpEntity<>(body, headers);
    }

    private String getJsonFromFacade(final DatasetFacade facade) {
        final Dataset dataset = new DatasetBuilder().build(facade);
        return marshalDataset(dataset);
    }

    private String marshalDataset(final Object object) {
        final ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#getDataverseContents(java.lang.String)
     */
    @Override
    public List<DataverseObject> getDataverseContents(final String dataverseAlias) throws RestClientException {
        final String url = createV1Url("dataverses", dataverseAlias, "contents" );
        final HttpEntity<String> entity = createHttpEntity("");
        final ParameterizedTypeReference<DataverseResponse<List<DataverseObject>>> type =
                new ParameterizedTypeReference<DataverseResponse<List<DataverseObject>>>() {
        };
        final ResponseEntity<DataverseResponse<List<DataverseObject>>> resp =
                template.exchange(url, HttpMethod.GET, entity, type);
        handleError(resp);
        final DataverseResponse<List<DataverseObject>> dv = resp.getBody();
        log.debug("Response is status {} with number elements {}", resp.getBody().getStatus(),
                resp.getBody().getData().size());
        if (!dv.getData().isEmpty()) {
            log.debug("First entry is {}", dv.getData().get(0));
        }
        return dv.getData();
    }

    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#getMetadataBLockInfo()
     */
    @Override
    public List<MetadataBlock> getMetadataBlockInfo() throws RestClientException {
        final String url = createV1Url("metadatablocks");
        final HttpHeaders headers = addAPIKeyToHeader();
        final HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        final ParameterizedTypeReference<DataverseResponse<List<MetadataBlock>>> type =
                new ParameterizedTypeReference<DataverseResponse<List<MetadataBlock>>>() {
        };
        final ResponseEntity<DataverseResponse<List<MetadataBlock>>> resp = template.exchange(url, HttpMethod.GET, entity,
                type);
        handleError(resp);
        return resp.getBody().getData();
    }

    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#getMetadataById(java.lang.String)
     */
    @Override
    public MetadataBlock getMetadataById(final String name) throws RestClientException {
        final String url = createV1Url("metadatablocks", name );

        final HttpHeaders headers = addAPIKeyToHeader();
        final HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        final ParameterizedTypeReference<DataverseResponse<MetadataBlock>> type = new ParameterizedTypeReference<DataverseResponse<MetadataBlock>>() {
        };
        final ResponseEntity<DataverseResponse<MetadataBlock>> resp = template.exchange(url, HttpMethod.GET, entity, type);
        handleError(resp);

        return resp.getBody().getData();
    }



    /* (non-Javadoc)
     * @see com.researchspace.dataverse.http.DataverseAPI#publishDataset(com.researchspace.dataverse.entities.Identifier, com.researchspace.dataverse.entities.Version)
     */
    @Override
    public DataverseResponse<PublishedDataset> publishDataset(final Identifier dsIdentifier, final Version version) {
        final String url = createV1Url("datasets", dsIdentifier.getId() + "", "actions", ":publish") + "?type="
                + version.name().toLowerCase();

        final HttpEntity<String> entity = createHttpEntity("");
        final ParameterizedTypeReference<DataverseResponse<PublishedDataset>> type = new ParameterizedTypeReference<DataverseResponse<PublishedDataset>>() {
        };
        try {
            final ResponseEntity<DataverseResponse<PublishedDataset>> resp = template.exchange(url, HttpMethod.POST, entity, type);
            log.debug(resp.getBody().toString());
            return resp.getBody();
        } catch (final RestClientException e) {
            throw new RestClientException(e.getCode(), e.getMessage());
        }

    }

    @Override
    public DataverseResponse<DataversePost> publishDataverse(final String dvName) throws RestClientException {
        final String url = createV1Url("dataverses", dvName, "actions", ":publish");
        final HttpEntity<String> entity = createHttpEntity("");
        final ParameterizedTypeReference<DataverseResponse<DataversePost>> type = new ParameterizedTypeReference<DataverseResponse<DataversePost>>() {
        };
        final ResponseEntity<DataverseResponse<DataversePost>> resp = template.exchange(url, HttpMethod.POST, entity, type);
        log.debug(resp.getBody().toString());
        handleError(resp);
        return resp.getBody();
    }

    @Override
    public DvMessage getDatasetPublishPopupCustomText() {
        final String url = createAdminUrl("info", "settings", ":DatasetPublishPopupCustomText");
        final HttpEntity<String> entity = createHttpEntity("");
        final ParameterizedTypeReference<DataverseResponse<DvMessage>> type = new ParameterizedTypeReference<DataverseResponse<DvMessage>>() {
        };
        final ResponseEntity<DataverseResponse<DvMessage>> resp = template.exchange(url, HttpMethod.GET, entity, type);
        log.debug(resp.getBody().toString());
        return resp.getBody().getData();

    }

    @Override
    public DataverseResponse<Object> setDatasetPublishPopupCustomText(final String text) {
        final String url = createAdminUrl("admin", "settings", ":DatasetPublishPopupCustomText");
        final HttpEntity<String> entity = createHttpEntity(text);
        final ParameterizedTypeReference<DataverseResponse<Object>> type = new ParameterizedTypeReference<DataverseResponse<Object>>() {
        };
        final ResponseEntity<DataverseResponse<Object>> resp = template.exchange(url, HttpMethod.PUT, entity, type);
        log.debug(resp.getBody().toString());
        return resp.getBody();
    }



    @Override
    public void deleteFile(final String fileId) throws SWORDException {
        final SwordAPI swordApi = new SwordAPI();
        try {
            swordApi.deleteFile(apiKey, fileId);
        } catch (SWORDClientException | ProtocolViolationException e) {
            final String msg = String.format("Couldn't delete file named %s : %s", fileId, e.getMessage());
            log.error(msg);
            throw new SWORDException(msg);
        } catch (final SWORDError error) {
            if (!isEmpty(error.getErrorBody())) {
                final String msg = String.format("SwordError: %s", error.getErrorBody());
                log.error(msg);
                throw new SWORDException(msg);
            }
        }
    }

    @Override
    public void deleteFile(final String fileName, final Identifier dsIdentifier)
            throws URISyntaxException, SWORDException {
        final SwordAPI swordApi = new SwordAPI();
        try {
            swordApi.deleteFile(apiKey, new URI(serverURL), dsIdentifier, fileName);
        } catch (SWORDClientException | ProtocolViolationException e) {
            log.error("Couldn't delete file named {} : {}", fileName, e.getMessage());
            log.error("cause : {}", e.getCause());
            throw new SWORDException(e.getMessage());
        } catch (final SWORDError error) {
            if (!isEmpty(error.getErrorBody())) {
                log.error("SwordError: {}", error.getErrorBody());
                throw new SWORDException(error.getErrorBody());
            }
        } catch (final URISyntaxException error) {
            log.error("URI Malformed: {}", serverURL);
            throw error;
        }
    }

    @Override
    public LocalDateTime getTokenExpirationDate() throws ParseException, RestClientException {
        final String url = createAdminUrl("users", "token");
        final HttpEntity<String> entity = createHttpEntity("");
        final ParameterizedTypeReference<DataverseResponse<DvMessage>> type =
                new ParameterizedTypeReference<DataverseResponse<DvMessage>>() {
        };
        final ResponseEntity<DataverseResponse<DvMessage>> resp = template.exchange(url, HttpMethod.GET, entity, type);
        // Split the String given as <Token -token- expires on -date->
        final String date = resp.getBody().getData().getMessage().split(" expires on ")[1];
        LocalDateTime time;
        try {
            time = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        } catch (final DateTimeParseException e) {
            time = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"));
        }
        return time;
    }

    @Override
    public String getTokenExpiration() throws ParseException, RestClientException {
        final String url = createAdminUrl("users", "token");
        final HttpEntity<String> entity = createHttpEntity("");
        final ParameterizedTypeReference<DataverseResponse<DvMessage>> type =
                new ParameterizedTypeReference<DataverseResponse<DvMessage>>() {
        };
        final ResponseEntity<DataverseResponse<DvMessage>> resp = template.exchange(url, HttpMethod.GET, entity, type);
        return resp.getBody().getData().getMessage();
    }

    @Override
    public DatasetFileList uploadNativeFile( final byte[] data, final FileUploadMetadata metadata, final Identifier dsIdentifier,  final String fileName){
        final ByteArrayResource resource = new ByteArrayResource(data){
            @Override
            public String getFilename(){
                return fileName;
            }
        };
        return getDatasetFileList(metadata, dsIdentifier, resource);
    }
    @Override
    public DatasetFileList uploadNativeFile(final InputStream data, final long contentLength, final FileUploadMetadata metadata, final Identifier dsIdentifier,  final String fileName) {
        final InputStreamResource resource = new InputStreamResource(data) {
            @Override
            public String getFilename(){
                return fileName;
            }

            @Override
            public long contentLength() throws IOException {
                return contentLength;
            }
        };
        return getDatasetFileList(metadata, dsIdentifier,  resource);

    }

    private DatasetFileList getDatasetFileList(final FileUploadMetadata metadata, final Identifier dsIdentifier, final AbstractResource resource) {
        final String url = createV1Url("datasets", ":persistentId", "add") + "?persistentId=" + dsIdentifier.getPersistentId();
        final ParameterizedTypeReference<DataverseResponse<DatasetFileList>> type =
                new ParameterizedTypeReference<DataverseResponse<DatasetFileList>>() {};
                final HttpEntity<MultiValueMap<String, Object>> entity = new NativeFileUploader().createFileUploadEntity(metadata, apiKey, resource);
                final ResponseEntity<DataverseResponse<DatasetFileList>> resp = template.exchange(url, HttpMethod.POST, entity, type);
                log.debug("{}", resp.getBody());
                handleError(resp);
                return resp.getBody().getData();
    }

    @Override
    public void uploadFile(final String doi, final InputStream file, final String filename) {
        final SwordAPI uploader = new SwordAPI();
        try {
            uploader.deposit(file, filename, apiKey, new URI(serverURL), doi);
        } catch (IOException | SWORDClientException  | ProtocolViolationException | URISyntaxException e) {
            log.error("Couldn't upload file {} with doi {} : {}", filename, doi, e.getMessage());
            throw new SWORDException("Couldn't upload file.", e);
        } catch (final SWORDError e) {
            if (!isEmpty(e.getErrorBody())) {
                log.error("SwordError: {}", e.getErrorBody());
                throw new SWORDException("SwordError", e);
            }
        }

    }
}
