package com.researchspace.dataverse.entities;

import java.util.List;

import lombok.Data;

@Data
public class Citation {
	private String displayName = "Citation Metadata";
	private List<CitationField> fields;

	

}
