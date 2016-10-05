package com.researchspace.dataverse.entities;

import lombok.Data;
/**
 * Wrapper over response, can include <code>data</code> or an error<code>message</code> but not both
 * @author rspace
 *
 * @param <T> The datatype of the response
 */
@Data
public class DataverseResponse <T> {
	
	private String status;
	private T data;
	private String message;

}
