package com.researchspace.dataverse.entities;

import java.net.URL;
import java.util.Date;

import lombok.Data;

@Data
public class PublishedDataset {
	
	private Long id;
	private String identifier, protocol, authority, publisher;
	private URL persistentUrl;
	private Date publicationDate;

}
