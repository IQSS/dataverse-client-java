package com.researchspace.dataverse.entities.facade;

import java.net.URL;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class DatasetProducer {
	
	private String name, affiliation, abbreviation;
	private URL url, logoURL;

}
