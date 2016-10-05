package com.researchspace.dataverse.entities;

import java.util.Date;

import lombok.Data;

@Data
public class DatasetVersion {
	private Long id;
	private String versionState;
	private String productionDate;
	private Date lastUpdateTime;
	private Date createTime;
	private DataSetMetadataBlock metadataBlocks;
	private int versionNumber, versionMinorNumber;

}
