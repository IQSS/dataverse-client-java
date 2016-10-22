package com.researchspace.dataverse.entries.facade;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import com.researchspace.dataverse.entities.facade.DatasetAuthor;
import com.researchspace.dataverse.entities.facade.DatasetContact;
import com.researchspace.dataverse.entities.facade.DatasetDescription;
import com.researchspace.dataverse.entities.facade.DatasetFacade;
import com.researchspace.dataverse.entities.facade.DatasetKeyword;
/**
 * Factory methods for returning valid Dataset objects and metadata
 * @author rspace
 *
 */
public class DatasetTestFactory {
	/**
	 * Creates a complex DataSet object
	 * @return
	 * @throws MalformedURLException
	 */
	public static DatasetFacade createFacade() throws MalformedURLException {		
		return DatasetFacade.builder()
		  .author(buildAnAuthor()).author(buildAnotherAuthor())
		  .title("title1")
		  .contact(buildAContact()).contact(buildAnotherContact())
		  .description(buildADesc())
		  .keyword(buildAKeyword("key1")).keyword(buildAKeyword("key2"))
		  .subject("Chemistry")
		  .depositor("A depositor")
		  .subtitle(" A subtitle")
		  .alternativeTitle("altTitle")
		  .alternativeURL(new URL("http://www.myrepo.com"))
		  .build();
	}

	 private static DatasetKeyword buildAKeyword(String key) {
		try {
			return DatasetKeyword.builder().value(key).vocabulary("keywordVocab")
			     .vocabularyURI(new URI("http://vocab.com")).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static DatasetDescription buildADesc() {
		 return DatasetDescription.builder()
				 .date(new Date()).description("some desc")
				 .build();
	}

	static DatasetContact buildAContact() {
		return DatasetContact.builder()
				.datasetContactAffiliation("Some place").datasetContactEmail("contact@email.com").datasetContactName("Sarah Contact")
				.build();
	}
	
	 static DatasetContact buildAnotherContact() {
		return DatasetContact.builder()
				.datasetContactAffiliation("Another place")
				.datasetContactEmail("contact2@email.com")
				.datasetContactName("Brian Contact2")
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
