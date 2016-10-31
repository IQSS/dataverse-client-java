package com.researchspace.dataverse.search.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DatasetItem extends Item {

	private @JsonProperty("global_id") String globalId;
	private @JsonProperty("published_at") String publishedAt;
	private String description, citation, citationHtml;

	public String getType() {
		return "dataset";
	}

}
