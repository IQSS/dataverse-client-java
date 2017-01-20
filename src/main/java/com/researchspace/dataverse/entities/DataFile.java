package com.researchspace.dataverse.entities;

import lombok.Data;

@Data
public class DataFile {
	 
	private Integer id, rootDataFileId;
	private Long filesize;
	private String filename, contentType, description, storageIdentifier, originalFormatLabel, md5;
	private Checksum checksum;

}
