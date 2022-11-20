package com.researchspace.dataverse.entities;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DatasetFileDetails {
    private int id;
    private int filesize;
    private String persistentId;
    private String pidURL;
    private String filename;
    private String contentType;
    private String description;
    private String storageIdentifier;
    private String rootDataFileId;
    private String md5;
    private Checksum checksum;
    private LocalDate creationDate;
}
