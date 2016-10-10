package com.researchspace.dataverse.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SearchResults  {
	
	String q;
	@JsonProperty(value="total_count")
	private int totalCount;
	@JsonProperty(value="count_in_response")
	private int countInResponse;
	int start;
	
	List<Object> spellingAlternatives;
	List<SearchHit> items;

}
