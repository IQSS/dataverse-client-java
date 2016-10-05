package com.researchspace.dataverse.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitationField {
	private String typeName, typeClass;
	private boolean multiple;
	private Object value;
}
