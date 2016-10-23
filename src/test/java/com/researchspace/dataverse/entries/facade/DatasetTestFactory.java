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
import com.researchspace.dataverse.entities.facade.DatasetPublication;
import com.researchspace.dataverse.entities.facade.DatasetTopicClassification;
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
	 * @throws URISyntaxException 
	 */
	public static DatasetFacade createFacade() throws MalformedURLException, URISyntaxException {		
		return DatasetFacade.builder()
		  .author(buildAnAuthor()).author(buildAnotherAuthor())
		  .title("title1")
		  .contact(buildAContact()).contact(buildAnotherContact())
		  .description(buildADesc())
		  .keyword(buildAKeyword("key1")).keyword(buildAKeyword("key2"))
		  .topicClassification(buildATopicClassification("topic1"))
		  .publication(buildAPublication())
		  .subject("Chemistry")
		  .depositor("A depositor")
		  .subtitle(" A subtitle")
		  .alternativeTitle("altTitle")
		  .alternativeURL(new URL("http://www.myrepo.com"))
		  .note("Some note")
		  .build();
	}

	 private static DatasetPublication buildAPublication() throws MalformedURLException {
		return DatasetPublication.builder()
				.publicationCitation("citation")
				.publicationIdNumber("12435")
				.publicationIDType("arc")
				.publicationURL(new URL("http:///pubmed.com/1234"))
				.build();
	}

	private static DatasetTopicClassification buildATopicClassification(String value) throws URISyntaxException {
		return DatasetTopicClassification.builder().topicClassValue(value)
				.topicClassVocab("a topic vocab").topicClassVocabURI(new URI("http://www.vocab.org)"))
				.build();
	}

	private static DatasetKeyword buildAKeyword(String key) throws URISyntaxException {
			return DatasetKeyword.builder().value(key).vocabulary("keywordVocab")
			     .vocabularyURI(new URI("http://vocab.com")).build();	
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
