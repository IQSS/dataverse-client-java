package com.researchspace.dataverse.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;


@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "type"
		)
@JsonSubTypes({
  @Type(value = DataverseSearchHit.class, name = "dataverse"),
  @Type(value = DatasetSearchHit.class, name = "dataset"),
  @Type(value = FileSearchHit.class, name = "file"),
  })
@Data
public abstract class SearchHit {
	private String name, type, url;
	
	@JsonProperty(value="image_url")
	private String   imageUrl;

}
