package com.researchspace.dataverse.entities;

import lombok.Data;
/**
 * Top-levle object returned after uploading a file through native API
 * @author rspace
 *
 */
@Data
public class DvFile {
 private String description, label;
 private Integer version, datasetVersionId;
 private DataFile dataFile;
}
