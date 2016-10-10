package com.researchspace.dataverse.http;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.noNullElements;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.swordapp.client.ProtocolViolationException;
import org.swordapp.client.SWORDClientException;
import org.swordapp.client.SWORDError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.researchspace.dataverse.api.v1.DatasetOperations;
import com.researchspace.dataverse.api.v1.DataverseOperations;
import com.researchspace.dataverse.api.v1.InfoOperations;
import com.researchspace.dataverse.api.v1.MetadataOperations;
import com.researchspace.dataverse.entities.Dataset;
import com.researchspace.dataverse.entities.DatasetVersion;
import com.researchspace.dataverse.entities.Dataverse;
import com.researchspace.dataverse.entities.DataverseObject;
import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.entities.DvMessage;
import com.researchspace.dataverse.entities.Identifier;
import com.researchspace.dataverse.entities.MetadataBlock;
import com.researchspace.dataverse.entities.Version;
import com.researchspace.dataverse.entities.facade.DatasetBuilder;
import com.researchspace.dataverse.entities.facade.DatasetFacade;
import com.researchspace.dataverse.sword.FileUploader;

import lombok.extern.slf4j.Slf4j;
/**  Copyright 2016 ResearchSpace

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

*/
@Slf4j
public class DataverseOperationsImplV1 extends AbstractOpsImplV1 implements DatasetOperations, MetadataOperations, InfoOperations, DataverseOperations {

	


	/* (non-Javadoc)
	 * @see com.researchspace.dataverse.http.DataverseAPI#getDataverseById(java.lang.String)
	 */
	@Override
	public Dataverse getDataverseById(String dataverseAlias) {
		String url = createV1Url("dataverses" , dataverseAlias);
		log.debug(url);
		RestTemplate template = createTemplate();
		HttpEntity<String> entity = createHttpEntity("");
		ParameterizedTypeReference<DataverseResponse<Dataverse>> type = new ParameterizedTypeReference<DataverseResponse<Dataverse>>() {
		};
		ResponseEntity<DataverseResponse<Dataverse>> resp = template.exchange(url, HttpMethod.GET, entity, type);
		log.debug(resp.getBody().toString());
		return resp.getBody().getData();
	}

	
	
	@Override
	public DataverseResponse<DvMessage> deleteDataverse(String dataverseAlias) {
		String url = createV1Url("dataverses", dataverseAlias);
		log.debug(url);
		RestTemplate template = createTemplate();
		HttpEntity<String> entity = createHttpEntity("");
		ParameterizedTypeReference<DataverseResponse<DvMessage>> type =
				new ParameterizedTypeReference<DataverseResponse<DvMessage>>() {};
		ResponseEntity<DataverseResponse<DvMessage>> resp = template.exchange(url, HttpMethod.DELETE, entity, type);
		log.debug(resp.getBody().toString());
		return resp.getBody();
		
	}
	
	@Override
	public DataverseResponse<Dataverse> createNewDataverse(String parentDv, Dataverse toCreate) {
		isTrue(!isEmpty(toCreate.getAlias()), "Alias must be specified");
		isTrue(!isEmpty(toCreate.getName()), "Name must be specified");
		noNullElements(toCreate.getDataverseContacts(), "At least 1 email contact must be provided"); 
		isTrue(!isEmpty(toCreate.getAlias()), "Alias must be specified");
		String url = createV1Url("dataverses", parentDv);

		RestTemplate template = createTemplate();
		String json = marshalDataset(toCreate);
		HttpEntity<String> entity = createHttpEntity(json);
		ParameterizedTypeReference<DataverseResponse<Dataverse>> type = new ParameterizedTypeReference<DataverseResponse<Dataverse>>() {
		};
		ResponseEntity<DataverseResponse<Dataverse>> resp = template.exchange(url, HttpMethod.POST, entity, type);
		log.debug(resp.getBody().toString());
		handleError(resp);
		return resp.getBody();
		
	}

	/* (non-Javadoc)
	 * @see com.researchspace.dataverse.http.DataverseAPI#createDataset(com.researchspace.dataverse.entities.facade.DatasetFacade, java.lang.String)
	 */
	@Override
	public Identifier createDataset(DatasetFacade facade, String dataverseAlias)  {
		String url = createV1Url("dataverses", dataverseAlias,"datasets");
		RestTemplate template = createTemplate();

		String json = getJsonFromFacade(facade);
		HttpEntity<String> entity = createHttpEntity(json);
		ParameterizedTypeReference<DataverseResponse<Identifier>> type = new ParameterizedTypeReference<DataverseResponse<Identifier>>() {
		};

		ResponseEntity<DataverseResponse<Identifier>> resp = template.exchange(url, HttpMethod.POST, entity, type);
		handleError(resp);
		return resp.getBody().getData();
	}
	
	
	/* (non-Javadoc)
	 * @see com.researchspace.dataverse.http.DataverseAPI#updateDataset(com.researchspace.dataverse.entities.facade.DatasetFacade, com.researchspace.dataverse.entities.Identifier)
	 */
	@Override
	public DatasetVersion updateDataset(DatasetFacade facade, Identifier id) {
		String url = createV1Url("datasets",  id.getId() +"", "versions",":draft");
		
		Dataset ds = new DatasetBuilder().build(facade);
		String json = marshalDataset(ds.getDatasetVersion());
		RestTemplate template = createTemplate();
		
		HttpEntity<String> entity = createHttpEntity(json);
		ParameterizedTypeReference<DataverseResponse<DatasetVersion>> type = new ParameterizedTypeReference<DataverseResponse<DatasetVersion>>() {
		};
		ResponseEntity<DataverseResponse<DatasetVersion>> resp = template.exchange(url, HttpMethod.PUT, entity,type);
		
		handleError(resp);
		return resp.getBody().getData();
		
	}
	
	/* (non-Javadoc)
	 * @see com.researchspace.dataverse.http.DataverseAPI#getDataset(com.researchspace.dataverse.entities.Identifier)
	 */
	@Override
	public Dataset getDataset(Identifier dsIdentifier) {
		String url = createV1Url("datasets",  dsIdentifier.getId() +"");
		RestTemplate template = createTemplate();
		HttpEntity<String> entity = createHttpEntity("");
		ParameterizedTypeReference<DataverseResponse<Dataset>> type = new ParameterizedTypeReference<DataverseResponse<Dataset>>() {
		};
		ResponseEntity<DataverseResponse<Dataset>> resp = template.exchange(url, HttpMethod.GET, entity, type);
		handleError(resp);
		return resp.getBody().getData();
	}
	
	/* (non-Javadoc)
	 * @see com.researchspace.dataverse.http.DataverseAPI#getDatasetVersions(com.researchspace.dataverse.entities.Identifier)
	 */
	@Override
	public List<DatasetVersion> getDatasetVersions (Identifier dsIdentifier) {
		String url = createV1Url("datasets",  dsIdentifier.getId() +"", "versions");
		RestTemplate template = createTemplate();
		HttpEntity<String> entity = createHttpEntity("");
		ParameterizedTypeReference<DataverseResponse<List<DatasetVersion>>> type = new ParameterizedTypeReference<DataverseResponse<List<DatasetVersion>>>() {
		};
		ResponseEntity<DataverseResponse<List<DatasetVersion>>> resp = template.exchange(url, HttpMethod.GET, entity, type);
		log.debug("{}", resp.getBody());
		handleError(resp);
		return resp.getBody().getData();
	}
	
	/* (non-Javadoc)
	 * @see com.researchspace.dataverse.http.DataverseAPI#uploadFile(java.lang.String, java.io.File)
	 */
	@Override
	public void uploadFile (String doi, File file) {
		FileUploader uploader = new FileUploader();
		try {
			uploader.deposit(file, apiKey, new URI(serverURL), doi);
		} catch (IOException | SWORDClientException | SWORDError | ProtocolViolationException | URISyntaxException e) {		
			log.error("Couldn't upload file {} with doi {} : {}", file.getName(), doi.toString(), e.getMessage());
			throw new RestClientException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.researchspace.dataverse.http.DataverseAPI#deleteDataset(com.researchspace.dataverse.entities.Identifier)
	 */
	@Override
	public DvMessage deleteDataset(Identifier dsIdentifier) {
		String url = createV1Url("datasets",  dsIdentifier.getId() +"" );
		RestTemplate template = createTemplate();
		HttpEntity<String> entity = createHttpEntity("");
		ParameterizedTypeReference<DataverseResponse<DvMessage>> type = new ParameterizedTypeReference<DataverseResponse<DvMessage>>() {
		};
		ResponseEntity<DataverseResponse<DvMessage>> resp = template.exchange(url, HttpMethod.DELETE, entity, type);
		handleError(resp);
		return resp.getBody().getData();
	}

	private HttpEntity<String> createHttpEntity(String body) {
		HttpHeaders headers = addAPIKeyToHeader();
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		return entity;
	}

	private String getJsonFromFacade(DatasetFacade facade) {
		Dataset dataset = new DatasetBuilder().build(facade);
		return marshalDataset(dataset);
	}

	private String marshalDataset(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	/* (non-Javadoc)
	 * @see com.researchspace.dataverse.http.DataverseAPI#getDataverseContents(java.lang.String)
	 */
	@Override
	public List<DataverseObject> getDataverseContents(String dataverseAlias) {
		String url = createV1Url("dataverses", dataverseAlias, "contents" );
		RestTemplate template = createTemplate();
		HttpEntity<String> entity = createHttpEntity("");
		ParameterizedTypeReference<DataverseResponse<List<DataverseObject>>> type = new ParameterizedTypeReference<DataverseResponse<List<DataverseObject>>>() {
		};
		ResponseEntity<DataverseResponse<List<DataverseObject>>> resp = template.exchange(url, HttpMethod.GET, entity,
				type);
		handleError(resp);
		DataverseResponse<List<DataverseObject>> dv = resp.getBody();
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
	public List<MetadataBlock> getMetadataBlockInfo() {
		String url = createV1Url("metadatablocks" );
		RestTemplate template = createTemplate();
		HttpHeaders headers = addAPIKeyToHeader();
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ParameterizedTypeReference<DataverseResponse<List<MetadataBlock>>> type = new ParameterizedTypeReference<DataverseResponse<List<MetadataBlock>>>() {
		};
		ResponseEntity<DataverseResponse<List<MetadataBlock>>> resp = template.exchange(url, HttpMethod.GET, entity,
				type);
		handleError(resp);
		return resp.getBody().getData();
	}

	/* (non-Javadoc)
	 * @see com.researchspace.dataverse.http.DataverseAPI#getMetadataById(java.lang.String)
	 */
	@Override
	public MetadataBlock getMetadataById(String name) {
		String url = createV1Url("metadatablocks", name );
		RestTemplate template = createTemplate();
		HttpHeaders headers = addAPIKeyToHeader();
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ParameterizedTypeReference<DataverseResponse<MetadataBlock>> type = new ParameterizedTypeReference<DataverseResponse<MetadataBlock>>() {
		};
		ResponseEntity<DataverseResponse<MetadataBlock>> resp = template.exchange(url, HttpMethod.GET, entity, type);
		handleError(resp);

		return resp.getBody().getData();
	}

	

	/* (non-Javadoc)
	 * @see com.researchspace.dataverse.http.DataverseAPI#publishDataset(com.researchspace.dataverse.entities.Identifier, com.researchspace.dataverse.entities.Version)
	 */
	@Override
	public void publishDataset(Identifier dsIdentifier, Version version) {
		String url = createV1Url("datasets", dsIdentifier.getId() + "", "actions", ":publish") + "?type="
				+ version.name().toLowerCase();
		RestTemplate template = createTemplate();
		HttpEntity<String> entity = createHttpEntity("");
//		ParameterizedTypeReference<DataverseResponse<DvMessage>> type = new ParameterizedTypeReference<DataverseResponse<DvMessage>>() {
//		};
		ResponseEntity<String> resp = template.exchange(url, HttpMethod.GET, entity, String.class);
		log.debug(resp.getBody());
     	//handleError(resp);
//		return resp.getBody().getData();
		
	}

	@Override
	public DataverseResponse<Dataverse> publishDataverse(String dvName) {
		String url = createV1Url("dataverses", dvName, "actions", ":publish");
		RestTemplate template = createTemplate();
		HttpEntity<String> entity = createHttpEntity("");
		ParameterizedTypeReference<DataverseResponse<Dataverse>> type = new ParameterizedTypeReference<DataverseResponse<Dataverse>>() {
		};
		ResponseEntity<DataverseResponse<Dataverse>> resp = template.exchange(url, HttpMethod.POST, entity, type);
		log.debug(resp.getBody().toString());
		return resp.getBody();
		
	}

	@Override
	public DvMessage getDatasetPublishPopupCustomText() {
		String url = createAdminUrl("admin", "settings", ":DatasetPublishPopupCustomText");
		RestTemplate template = createTemplate();
		HttpEntity<String> entity = createHttpEntity("");
		ParameterizedTypeReference<DataverseResponse<DvMessage>> type = new ParameterizedTypeReference<DataverseResponse<DvMessage>>() {
		};
		ResponseEntity<DataverseResponse<DvMessage>> resp = template.exchange(url, HttpMethod.GET, entity, type);
		log.debug(resp.getBody().toString());
		return resp.getBody().getData();
		
	}

	@Override
	public DataverseResponse<Object> setDatasetPublishPopupCustomText(String text) {
		String url = createAdminUrl("admin", "settings", ":DatasetPublishPopupCustomText");
		RestTemplate template = createTemplate();
		HttpEntity<String> entity = createHttpEntity(text);
		ParameterizedTypeReference<DataverseResponse<Object>> type = new ParameterizedTypeReference<DataverseResponse<Object>>() {
		};
		ResponseEntity<DataverseResponse<Object>> resp = template.exchange(url, HttpMethod.PUT, entity, type);
		log.debug(resp.getBody().toString());
		return resp.getBody();
	}	
}
