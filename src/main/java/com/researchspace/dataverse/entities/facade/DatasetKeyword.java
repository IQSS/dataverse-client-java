package com.researchspace.dataverse.entities.facade;

import java.net.URI;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class DatasetKeyword {
	
	private @NonNull String value;
	private String vocabulary;
	private URI vocabularyURI;
	

}
