package com.researchspace.dataverse.search.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

/**
 * Base class of SearchHits returned in the <code>items</code> field of a search
 * result.
 * 
 * @author rspace
 *
 */
// used to deserialise Json into correct subclasses using value of 'type'
// property
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = DataverseSearchHit.class, name = "dataverse"),
		@Type(value = DatasetSearchHit.class, name = "dataset"), 
		@Type(value = FileSearchHit.class, name = "file"), })
@Data
public abstract class SearchHit {
	private String name, type, url;

	@JsonProperty(value = "image_url")
	private String imageUrl;

	abstract SearchHit asSubtype();

}
