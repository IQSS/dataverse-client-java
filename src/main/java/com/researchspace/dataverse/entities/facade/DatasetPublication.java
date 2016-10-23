package com.researchspace.dataverse.entities.facade;

import java.net.URL;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatasetPublication {
	
	private String publicationCitation, publicationIdNumber;
	private PublicationIDType publicationIDType;
	private URL publicationURL;

}
