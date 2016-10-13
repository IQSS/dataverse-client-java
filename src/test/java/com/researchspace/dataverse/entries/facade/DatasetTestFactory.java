package com.researchspace.dataverse.entries.facade;

import com.researchspace.dataverse.entities.facade.DatasetAuthor;
import com.researchspace.dataverse.entities.facade.DatasetContact;
import com.researchspace.dataverse.entities.facade.DatasetFacade;
/**
 * Factory methods for returning valid Dataset objects and metadata
 * @author rspace
 *
 */
public class DatasetTestFactory {
	
	public static DatasetFacade createFacade() {		
		return DatasetFacade.builder()
		  .author(buildAnAuthor()).author(buildAnotherAuthor())
		  .title("title1")
		  .contact(buildAContact()).contact(buildAnotherContact())
		  .description("some desc")
		  .subject("Chemistry")
		  .depositor("A depositor")
		  .subtitle(" A subtitle")
		  .alternativeTitle("altTitle")
		  .build();
	}

	 static DatasetContact buildAContact() {
		return DatasetContact.builder()
				.datasetContactAffiliation("Some place").datasetContactEmail("contact@email.com").datasetContactName("Sarah Contact")
				.build();
	}
	
	 static DatasetContact buildAnotherContact() {
		return DatasetContact.builder()
				.datasetContactAffiliation("Another place").datasetContactEmail("contact2@email.com").datasetContactName("Brian Contact2")
				.build();
	}

	 static DatasetAuthor buildAnotherAuthor() {
		return DatasetAuthor.builder().authorName("John  Smith")
				  .authorAffiliation("Dataverse")
				  .authorIdentifierScheme("ISNI")
				  .authorIdentifier("1234-5678").build();
	}

	 static DatasetAuthor buildAnAuthor() {
		return DatasetAuthor.builder().authorName("Fred Blogs")
				  .authorAffiliation("RSpace")
				  .authorIdentifierScheme("ORCID")
				  .authorIdentifier("1234-5678").build();
	}

}
