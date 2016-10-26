package com.researchspace.dataverse.entities.facade;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
	private @Singular List<DatasetTopicClassification> topicClassifications;
	private @Singular List<DatasetPublication> publications;
	private @Singular List<DatasetProducer> producers;
	private String note;
	private List<String> languages = new ArrayList<>();
	private Date productionDate;
	private String productionPlace;
	private @Singular List<DatasetContributor> contributors;
	
	/**
	 * Returns a copy if the internally stored Date
	 * @return
	 */
	public Date getProductionDate (){
		return new Date(productionDate.getTime());
	}
	/**
	 * Sets this obkect's date as a opy of the parameter Date.
	 * @param date
	 */
	public void setProductionDate(Date date) {
		this.productionDate = new Date (productionDate.getTime());
	}


}
