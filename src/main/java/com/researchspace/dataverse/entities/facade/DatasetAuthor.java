package com.researchspace.dataverse.entities.facade;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class DatasetAuthor {	
	private @NonNull String authorName;
	private String authorAffiliation, authorIdentifierScheme, authorIdentifier;

}
