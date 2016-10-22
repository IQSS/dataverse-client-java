package com.researchspace.dataverse.entities.facade;

import java.net.URL;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
/**
 * Simple POJO to set info for Dataset. 
 * @author rspace
 *
 */
@Data
@Builder
public class DatasetFacade   {
	
	private @NonNull String title; 
	private @NonNull @Singular List<DatasetAuthor> authors;
	private @NonNull @Singular List<DatasetContact> contacts;
	private @NonNull String subject;
	private @NonNull @Singular List<DatasetDescription> descriptions;
	private String  depositor, subtitle, alternativeTitle;
	private URL alternativeURL;
	private @Singular List<DatasetKeyword> keywords;

}
