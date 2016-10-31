package com.researchspace.dataverse.search.entities;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Encapsulates search results.
 * <p/>
 * Items' subclass can be identified from the 'type' value.
 * 
 * @author rspace
 *
 */
@Data
public class SearchResults {

	String q;
	@JsonProperty(value = "total_count")
	private int totalCount;
	@JsonProperty(value = "count_in_response")
	private int countInResponse;
	int start;

	List<Object> spellingAlternatives;
	List<Item> items;
	
	/**
	 * Filters a list of SearchHits by their type.
	 * @param type
	 * @return
	 */
	public List<Item> filterByType (SearchType type) {
		return items.stream().filter((i)->
		 i.getType().equalsIgnoreCase(type.name()))
		.collect(Collectors.toList());
	}

}
