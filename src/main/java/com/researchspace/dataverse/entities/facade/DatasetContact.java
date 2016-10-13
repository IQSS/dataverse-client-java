package com.researchspace.dataverse.entities.facade;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class DatasetContact {	
	private @NonNull String datasetContactEmail;
	private String datasetContactAffiliation, datasetContactName;

}
