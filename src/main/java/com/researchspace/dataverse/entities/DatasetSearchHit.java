package com.researchspace.dataverse.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DatasetSearchHit extends SearchHit {

	private @JsonProperty("global_id") String globalId;
	private @JsonProperty("published_at") String publishedAt;
	private String description, citation, citationHtml;
	
	public String getType (){
		return "file";
	}
}
