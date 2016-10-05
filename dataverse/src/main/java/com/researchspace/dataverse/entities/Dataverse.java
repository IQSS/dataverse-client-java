package com.researchspace.dataverse.entities;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Data
@JsonFormat
public class Dataverse {
	private String id;
	private String alias;
	private String name;
	private String affiliation;
	private String permissionRoot;
	private String description;
	private String ownerId;
	private Date creationDate;
	
	private DataverseUser creator;
	
	private List<DataverseContacts> dataverseContacts;

}
