package com.researchspace.dataverse.search.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class DataverseSearchHit extends SearchHit {
	String identifier;
	@JsonProperty("published_at")
	String publishedAt;
	
	public String getType (){
		return "dataverse";
	}

	@Override
	DataverseSearchHit asSubtype() {
		return this;
	}
}
