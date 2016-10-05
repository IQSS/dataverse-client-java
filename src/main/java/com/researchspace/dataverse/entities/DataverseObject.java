package com.researchspace.dataverse.entities;

import java.net.URL;

import lombok.Data;

@Data
public class DataverseObject {
	
	private String id, identifier, protocol, authority,type;
	private URL persistentUrl;

}
