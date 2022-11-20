/*
 * 
 */
package com.researchspace.dataverse.entities.facade;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
/**
 * /** <pre>
Copyright 2016 ResearchSpace

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
</pre>
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
		  .producer(buildAProducer())
		  .productionDate(new Date())
		  .productionPlace("Edinburgh, UK")
		  .contributor(buildAContributor("Fred")).contributor(buildAContributor("Tim"))
		  .subject("Chemistry")
		  .depositor("A depositor")
		  .subtitle(" A subtitle")
		  .alternativeTitle("altTitle")
		  .alternativeURL(new URL("https://www.myrepo.com"))
		  .note("Some note")
		  .languages(Arrays.asList(new String []{"English", "French"}))
		  .build();
	}

	 private static DatasetContributor buildAContributor(String name) {
		return DatasetContributor.builder()
				.name(name)
				.type(ContributorType.ProjectLeader)
				.build();
	}

	private static DatasetProducer buildAProducer() throws MalformedURLException {
		return DatasetProducer.builder()
				.name("a producer")
				.abbreviation("abbr")
				.affiliation("UoE")
				.logoURL(new URL("https://pubmed.logo.com/1234"))
				.url(new URL("https://pubmed.com/1234"))
				.build();
	}

	private static DatasetPublication buildAPublication() throws MalformedURLException {
		return DatasetPublication.builder()
				.publicationCitation("citation")
				.publicationIdNumber("12435")
				.publicationIDType(PublicationIDType.ean13)
				.publicationURL(new URL("https://pubmed.com/1234"))
				.build();
	}

	private static DatasetTopicClassification buildATopicClassification(String value) throws URISyntaxException {
		return DatasetTopicClassification.builder().topicClassValue(value)
				.topicClassVocab("a topic vocab").topicClassVocabURI(new URI("https://www.vocab.org"))
				.build();
	}

	private static DatasetKeyword buildAKeyword(String key) throws URISyntaxException {
			return DatasetKeyword.builder().value(key).vocabulary("keywordVocab")
			     .vocabularyURI(new URI("https://vocab.com")).build();
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
