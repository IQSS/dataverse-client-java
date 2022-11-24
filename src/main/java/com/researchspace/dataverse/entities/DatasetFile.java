package com.researchspace.dataverse.entities;

import lombok.Data;

import java.util.List;

@Data
public class DatasetFile {
    private  String description;
    private  String label;
    private  String directoryLabel;
    private  String datasetVersionId;
    private List<String> categories;
    private  boolean restricted;
    private  int version;
    private DatasetFileDetails dataFile;

}
