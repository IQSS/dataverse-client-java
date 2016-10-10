package com.researchspace.dataverse.search.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class FileSearchHit extends SearchHit {
	
	private @JsonProperty("file_id") String fileId;
	private @JsonProperty("dataset_citation") String datasetCitation;
	private @JsonProperty("file_content_type") String fileContentType;
	private String description, md5;
	private @JsonProperty("size_in_bytes") int size;
	@JsonProperty("published_at")
	private String publishedAt;
	
	public String getType (){
		return "file";
	}

	@Override
	FileSearchHit asSubtype() {
		return this;
	}

}
