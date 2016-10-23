package com.researchspace.dataverse.entities.facade;

import java.net.URI;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatasetTopicClassification {
	
	private String topicClassValue, topicClassVocab;
	private URI topicClassVocabURI;

}
