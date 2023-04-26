/** <pre>
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
</pre> */
package com.researchspace.dataverse.entities.facade;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

/**
 * Factory methods for returning valid Dataset objects and metadata
 * @author rspace
 */
public class DatasetTestFactory {

    private static final Date SOME_DATE = Date.from(Instant.ofEpochMilli(1));

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
                .productionDate(SOME_DATE)
                .productionPlace("Edinburgh, UK")
                .contributor(buildAContributor("Fred")).contributor(buildAContributor("Tim"))
                .subject("Chemistry")
                .depositor("A depositor")
                .subtitle(" A subtitle")
                .alternativeTitle("altTitle")
                .alternativeURL(new URL("http://www.myrepo.com"))
                .note("Some note")
                .kindsOfData(Arrays.asList("Dataset", "Collection"))
                .languages(Arrays.asList("English"))
                .build();
    }

    private static DatasetContributor buildAContributor(final String name) {
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
                .logoURL(new URL("http://pubmed.logo.com/1234"))
                .url(new URL("http://pubmed.com/1234"))
                .build();
    }

    private static DatasetPublication buildAPublication() throws MalformedURLException {
        return DatasetPublication.builder()
                .publicationCitation("citation")
                .publicationIdNumber("12435")
                .publicationIDType(PublicationIDType.ean13)
                .publicationURL(new URL("http://pubmed.com/1234"))
                .build();
    }

    private static DatasetTopicClassification buildATopicClassification(final String value) throws URISyntaxException {
        return DatasetTopicClassification.builder().topicClassValue(value)
                .topicClassVocab("a topic vocab").topicClassVocabURI(new URI("http://www.vocab.org"))
                .build();
    }

    private static DatasetKeyword buildAKeyword(final String key) throws URISyntaxException {
        return DatasetKeyword.builder().value(key).vocabulary("keywordVocab")
                .vocabularyURI(new URI("http://vocab.com")).build();
    }

    private static DatasetDescription buildADesc() {
        return DatasetDescription.builder()
                .date(SOME_DATE).description("some desc")
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
