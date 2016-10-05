package com.researchspace.dataverse.entities.facade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Simple POJO to set info for Dataset. 
 * @author rspace
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatasetFacade   {
	
	private String title, author, contactName, contactEmail, description, subject, depositor;

}
