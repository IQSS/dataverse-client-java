package com.researchspace.dataverse.entities.facade;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatasetContributor {
	
	private String name;
	private ContributorType type;

}
