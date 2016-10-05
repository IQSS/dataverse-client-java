package com.researchspace.dataverse.entities;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetadataBlock {
 private String id, displayName, name;
 private Map<String, DVField> fields;
}
