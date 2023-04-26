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
</pre>
 */
package com.researchspace.dataverse.entities.facade;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.researchspace.dataverse.entities.CitationType;
import com.researchspace.dataverse.entities.Dataset;
import com.researchspace.dataverse.entities.DatasetMetadataBlock;
import com.researchspace.dataverse.entities.DatasetVersion;
import com.researchspace.dataverse.entities.Field;

/**
 * Converts POJO Java facade into underlying JSON object representation
 * @author rspace
 */
public class DatasetBuilder {

    private static final String CONTRIBUTOR = "contributor";
    private static final String CONTRIBUTOR_TYPE = "contributorType";
    private static final String CONTRIBUTOR_NAME = "contributorName";
    private static final String PRODUCER_LOGO_URL = "producerLogoURL";
    private static final String PRODUCER_URL = "producerURL";
    private static final String PRODUCER_AFFILIATION = "producerAffiliation";
    private static final String PRODUCER_ABBREVIATION = "producerAbbreviation";
    private static final String PRODUCER_NAME = "producerName";
    private static final String PUBLICATION_URL = "publicationURL";
    private static final String PUBLICATION_ID = "publicationIDNumber";
    private static final String PUBLICATION_ID_TYPE = "publicationIDType";
    private static final String PUBLICATION_CITATION= "publicationCitation";
    private static final String KEYWORD_VOCABULARY_URI = "keywordVocabularyURI";
    private static final String KEYWORD_VOCABULARY = "keywordVocabulary";
    private static final String KEYWORD_VALUE = "keywordValue";
    private static final String TOPIC_VOCABULARY_URI = "topicClassVocabURI";
    private static final String TOPIC_VOCABULARY = "topicClassVocab";
    private static final String TOPIC_VALUE = "topicClassValue";
    private static final String DATASET_DESC_VALUE = "dsDescriptionValue";
    private static final String DATASET_DESC_DATE = "dsDescriptionDate";
    private static final String DATASET_CONTACT_EMAIL = "datasetContactEmail";
    private static final String DATASET_CONTACT_NAME = "datasetContactName";
    private static final String DATASET_CONTACT_AFFILIATION = "datasetContactAffiliation";
    private static final String AUTHOR_IDENTIFIER_SCHEME = "authorIdentifierScheme";
    private static final String AUTHOR_IDENTIFIER = "authorIdentifier";
    private static final String AUTHOR_AFFILIATION = "authorAffiliation";
    private static final String AUTHOR_NAME = "authorName";
    private static final String KIND_OF_DATA = "kindOfData";
    private static final String TIME_PERIOD_COVERED = "timePeriodCovered";
    private static final String TIME_PERIOD_COVERED_START = "timePeriodCoveredStart";
    private static final String TIME_PERIOD_COVERED_END = "timePeriodCoveredEnd";

    // Note : North and south latitude are named "Longitude" because dataverse expect this in the .json.
    // It's obviously an error, in the meantime, they'll be named like this waiting for a patch.
    private static final String WEST_LONGITUDE = "westLongitude";
    private static final String EAST_LONGITUDE = "eastLongitude";
    private static final String NORTH_LONGITUDE = "northLongitude";
    private static final String SOUTH_LONGITUDE = "southLongitude";
    private static final String GEOGRAPHIC_BOUNDING_BOX = "geographicBoundingBox";

    public Dataset build(final DatasetFacade facade) {
        final Map<String, DatasetMetadataBlock> metadataBlocks = new HashMap<>();
        metadataBlocks.put("citation", new DatasetMetadataBlock("Citation Metadata", createCitationFields(facade)));
        metadataBlocks.put("geospatial", new DatasetMetadataBlock("Geospatial Metadata", createFields(facade)));
        if (facade.getTermsOfUse() != null) {
            return build(metadataBlocks, facade.getTermsOfUse());
        }
        return build(metadataBlocks);
    }

    public Dataset build(final Map<String, DatasetMetadataBlock> metadataBlocks) {
        final DatasetVersion dv = new DatasetVersion();
        final Dataset toSubmit = new Dataset();
        toSubmit.setDatasetVersion(dv);
        dv.setMetadataBlocks(metadataBlocks);
        return toSubmit;
    }

    public Dataset build(final Map<String, DatasetMetadataBlock> metadataBlocks, final String termsOfUse) {
        final Dataset dataset = build(metadataBlocks);
        dataset.getDatasetVersion().setTermsOfUse(termsOfUse);
        return dataset;
    }

    private List<Field> createFields(final DatasetFacade facade) {
        final List<Field> fields = new ArrayList<>();
        addGeographicBoundingBox(facade, fields);
        return fields;
    }

    private List<Field> createCitationFields(final DatasetFacade facade) {
        final List<Field> fields = new ArrayList<>();
        //mandatory fields
        addTitle(facade, fields);
        addAuthors(facade, fields);
        addDescription(facade, fields);
        addKeywords(facade, fields);
        addTopicClassifications(facade, fields);
        addPublications(facade, fields);
        addLanguages(facade, fields);
        addNotes(facade, fields);
        addProducers(facade, fields);
        addProductionDate(facade, fields);
        addProductionPlace(facade, fields);
        addContributor(facade, fields);
        addSubject(facade, fields);
        addContacts(facade, fields);

        //optional fields
        addDepositor(facade, fields);
        addSubTitle(facade, fields);
        addAlternativeTitle(facade, fields);
        addAlternativeURL(facade, fields);
        addKindOfData(facade, fields);
        addTimePeriodCovered(facade, fields);
        return fields;
    }

    private void addProductionDate(final DatasetFacade facade, final List<Field> fields) {
        if( facade.getProductionDate() != null) {
            final Field prodDate = createPrimitiveSingleField("productionDate", isoDate(facade.getProductionDate()));
            fields.add(prodDate);
        }
    }

    private void addProductionPlace(final DatasetFacade facade, final List<Field> fields) {
        if(!facade.getProductionPlaces().isEmpty()){
            final Field prodPlace = createPrimitiveMultipleField("productionPlace",
                    facade.getProductionPlaces().toArray(new String[0]));
            fields.add(prodPlace);
        }
    }


    private void addLanguages(final DatasetFacade facade, final List<Field> fields) {
        if (!facade.getLanguages().isEmpty()) {
            final Field field = createControlledVocabField("language", true, facade.getLanguages());
            fields.add(field);
        }
    }

    private void addNotes(final DatasetFacade facade, final List<Field> fields) {
        if(!isEmpty(facade.getNote())){
            final Field altUrl = createPrimitiveSingleField("notesText", facade.getNote());
            fields.add(altUrl);
        }
    }

    private void addAlternativeURL(final DatasetFacade facade, final List<Field> fields) {
        if (facade.getAlternativeURL() != null) {
            final Field altUrl = createPrimitiveSingleField("alternativeURL", facade.getAlternativeURL().toString());
            fields.add(altUrl);
        }
    }

    private void addKindOfData(final DatasetFacade facade, final List<Field> fields) {
        if (facade.getKindsOfData() != null) {
            final Field kindOfData = createPrimitiveMultipleField(KIND_OF_DATA,
                    facade.getKindsOfData().toArray(new String[0]));
            fields.add(kindOfData);

        }
    }

    private void addAlternativeTitle(final DatasetFacade facade, final List<Field> fields) {
        if (!isEmpty(facade.getAlternativeTitle())) {
            final Field title = createPrimitiveSingleField("alternativeTitle", facade.getAlternativeTitle());
            fields.add(title);
        }
    }

    private void addSubTitle(final DatasetFacade facade, final List<Field> fields) {
        if (!isEmpty(facade.getSubtitle())) {
            final Field subtitle = createPrimitiveSingleField("subtitle", facade.getSubtitle());
            fields.add(subtitle);
        }
    }

    private void addSubject(final DatasetFacade facade, final List<Field> fields) {
        final Field subject = createControlledVocabField("subject", true, Arrays.asList(facade.getSubject()));
        fields.add(subject);
    }

    private void addDescription(final DatasetFacade facade, final List<Field> fields) {
        final List<DatasetDescription> descs = facade.getDescriptions();
        final List<Map<String, Object>> descList = new ArrayList<>();
        for (final DatasetDescription desc: descs) {
            final Map<String, Object> map2 = new HashMap<>();
            final Field descF = createPrimitiveSingleField(DATASET_DESC_VALUE, desc.getDescription());
            map2.put(DATASET_DESC_VALUE, descF);
            if(desc.getDate() != null) {
                final Field dateF = createPrimitiveSingleField(DATASET_DESC_DATE, isoDate(desc.getDate()));
                map2.put(DATASET_DESC_DATE, dateF);
            }
            descList.add(map2);
        }

        final Field desc = createCompoundField("dsDescription", true, descList);
        fields.add(desc);
    }

    private void addTopicClassifications(final DatasetFacade facade, final List<Field> fields) {
        final List<DatasetTopicClassification> topics = facade.getTopicClassifications();
        final List<Map<String, Object>> topicsList = new ArrayList<>();
        for (final DatasetTopicClassification topic: topics) {
            final Map<String, Object> map = new HashMap<>();
            addOptionalPrimitiveField(topic.getTopicClassValue(), map, TOPIC_VALUE);
            addOptionalPrimitiveField(topic.getTopicClassVocab(), map, TOPIC_VOCABULARY);
            addOptionalPrimitiveField(topic.getTopicClassVocabURI().toString(), map, TOPIC_VOCABULARY_URI);
            topicsList.add(map);
        }
        final Field topicClassifn = createCompoundField("topicClassification", true, topicsList);
        fields.add(topicClassifn);
    }

    private void addProducers(final DatasetFacade facade, final List<Field> fields) {
        final List<DatasetProducer> topics = facade.getProducers();
        final List<Map<String, Object>> topicsList = new ArrayList<>();
        for (final DatasetProducer topic: topics) {
            final Map<String, Object> map = new HashMap<>();
            addOptionalPrimitiveField(topic.getName(), map, PRODUCER_NAME);
            addOptionalPrimitiveField(topic.getAbbreviation(), map, PRODUCER_ABBREVIATION);
            addOptionalPrimitiveField(topic.getAffiliation(), map, PRODUCER_AFFILIATION);
            addOptionalPrimitiveField(topic.getUrl().toString(), map, PRODUCER_URL);
            addOptionalPrimitiveField(topic.getLogoURL().toString(), map, PRODUCER_LOGO_URL);
            topicsList.add(map);
        }
        final Field topicClassifn = createCompoundField("producer", true, topicsList);
        fields.add(topicClassifn);
    }

    private void addContributor(final DatasetFacade facade, final List<Field> fields) {
        final List<DatasetContributor> contribs = facade.getContributors();
        final List<Map<String, Object>> contribList = new ArrayList<>();
        for (final DatasetContributor contrib: contribs) {
            final Map<String, Object> map = new HashMap<>();
            addOptionalPrimitiveField(contrib.getName(), map, CONTRIBUTOR_NAME);
            if(contrib.getType()!=null) {
                final Field cf =createControlledVocabField(CONTRIBUTOR_TYPE, false,
                        asList(contrib.getType().getDisplayName()));
                map.put(CONTRIBUTOR_TYPE, cf);
            }
            contribList.add(map);
        }
        final Field topicClassifn = createCompoundField(CONTRIBUTOR, true, contribList);
        fields.add(topicClassifn);
    }

    private void addPublications(final DatasetFacade facade, final List<Field> fields) {
        final List<DatasetPublication> publications = facade.getPublications();
        final List<Map<String, Object>> list = new ArrayList<>();
        for (final DatasetPublication publication: publications) {
            final Map<String, Object> map = new HashMap<>();
            addOptionalPrimitiveField(publication.getPublicationCitation(), map, PUBLICATION_CITATION);
            addOptionalPrimitiveField(publication.getPublicationIdNumber(), map, PUBLICATION_ID);
            addOptionalPrimitiveField(publication.getPublicationURL().toString(), map, PUBLICATION_URL);
            if (publication.getPublicationIDType()!= null) {
                final Field scheme = createControlledVocabField(PUBLICATION_ID_TYPE, false,
                        asList(publication.getPublicationIDType().name()));
                map.put(PUBLICATION_ID_TYPE, scheme);
            }
            list.add(map);
        }
        final Field publication = createCompoundField("publication", true, list);
        fields.add(publication);
    }

    private void addKeywords(final DatasetFacade facade, final List<Field> fields) {
        final List<DatasetKeyword> keywords = facade.getKeywords();
        final List<Map<String, Object>> keysList = new ArrayList<>();
        for (final DatasetKeyword keyword: keywords) {
            final Map<String, Object> map2 = new HashMap<>();
            final Field descF = createPrimitiveSingleField(KEYWORD_VALUE, keyword.getValue());
            map2.put(KEYWORD_VALUE, descF);
            addOptionalPrimitiveField(keyword.getVocabulary(), map2, KEYWORD_VOCABULARY);
            addOptionalPrimitiveField(keyword.getVocabularyURI().toString(), map2, KEYWORD_VOCABULARY_URI);
            keysList.add(map2);
        }

        final Field desc = createCompoundField("keyword", true, keysList);
        fields.add(desc);

    }

    private String isoDate(final Date date) {
        return String.format("%tF", date);
    }

    private void addContacts (final DatasetFacade facade, final List<Field> fields) {
        final List<DatasetContact> contacts = facade.getContacts();
        final List<Map<String, Object>> contactsList = new ArrayList<>();
        for (final DatasetContact contact : contacts) {
            final Map<String, Object> map2 = new HashMap<>();
            final Field email = createPrimitiveSingleField(DATASET_CONTACT_EMAIL, contact.getDatasetContactEmail());
            map2.put(DATASET_CONTACT_EMAIL, email);
            addOptionalPrimitiveField(contact.getDatasetContactName(), map2, DATASET_CONTACT_NAME);
            addOptionalPrimitiveField(contact.getDatasetContactAffiliation(), map2, DATASET_CONTACT_AFFILIATION);
            contactsList.add(map2);
        }
        final Field contact = createCompoundField("datasetContact", true, contactsList);
        fields.add(contact);
    }

    private void addAuthors (final DatasetFacade facade, final List<Field> fields) {
        final List<DatasetAuthor> authors = facade.getAuthors();
        final List<Map<String, Object>> authorsMap = new ArrayList<>();
        for (final DatasetAuthor author : authors) {
            final Map<String, Object> map = new HashMap<>();
            final Field authorName = createPrimitiveSingleField(AUTHOR_NAME, author.getAuthorName());
            map.put(AUTHOR_NAME, authorName);
            addOptionalPrimitiveField(author.getAuthorAffiliation(), map, AUTHOR_AFFILIATION);
            addOptionalPrimitiveField(author.getAuthorIdentifier(), map, AUTHOR_IDENTIFIER);

            if (!isEmpty(author.getAuthorIdentifierScheme())) {
                final Field scheme = createControlledVocabField(AUTHOR_IDENTIFIER_SCHEME, false,
                        asList(author.getAuthorIdentifierScheme()));
                map.put(AUTHOR_IDENTIFIER_SCHEME, scheme);
            }
            authorsMap.add(map);
        }
        final Field toAdd = createCompoundField("author", true, authorsMap);
        fields.add(toAdd);
    }

    private void addTimePeriodCovered (final DatasetFacade facade, final List<Field> fields) {
        final List<DatasetTimePeriodCovered> timePeriodCovered = facade.getTimePeriodsCovered();
        final List<Map<String, Object>> timePeriodMap = new ArrayList<>();
        for (final DatasetTimePeriodCovered period : timePeriodCovered) {
            final Map<String, Object> map = new HashMap<>();
            final Field start = createPrimitiveSingleField(TIME_PERIOD_COVERED_START, isoDate(period.getTimePeriodCoveredStart()));
            map.put(TIME_PERIOD_COVERED_START, start);
            final Field end = createPrimitiveSingleField(TIME_PERIOD_COVERED_END, isoDate(period.getTimePeriodCoveredEnd()));
            map.put(TIME_PERIOD_COVERED_END, end);
            timePeriodMap.add(map);
        }
        final Field toAdd = createCompoundField(TIME_PERIOD_COVERED, true, timePeriodMap);
        fields.add(toAdd);
    }

    private void addGeographicBoundingBox (final DatasetFacade facade, final List<Field> fields) {
        final List<DatasetGeographicBoundingBox> geographicBoundingBoxes = facade.getGeographicBoundingBoxes();
        if (geographicBoundingBoxes != null && !geographicBoundingBoxes.isEmpty()) {
            final List<Map<String, Object>> geographicBoundariesMap = new ArrayList<>();
            for (final DatasetGeographicBoundingBox geographicBoundingBox : geographicBoundingBoxes) {
                final Map<String, Object> map = new HashMap<>();
                final Field west = createPrimitiveSingleField(WEST_LONGITUDE, geographicBoundingBox.getWestLongitude().toString());
                map.put(WEST_LONGITUDE, west);
                final Field east = createPrimitiveSingleField(EAST_LONGITUDE, geographicBoundingBox.getEastLongitude().toString());
                map.put(EAST_LONGITUDE, east);
                final Field north = createPrimitiveSingleField(NORTH_LONGITUDE, geographicBoundingBox.getNorthLongitude().toString());
                map.put(NORTH_LONGITUDE, north);
                final Field south = createPrimitiveSingleField(SOUTH_LONGITUDE, geographicBoundingBox.getSouthLongitude().toString());
                map.put(SOUTH_LONGITUDE, south);
                geographicBoundariesMap.add(map);
            }
            final Field toAdd = createCompoundField(GEOGRAPHIC_BOUNDING_BOX, true, geographicBoundariesMap);
            fields.add(toAdd);
        }
    }

    private void addOptionalPrimitiveField(final String value,  final Map<String, Object> map, final String field) {
        if (!isEmpty(value)) {
            final Field affil = createPrimitiveSingleField(field, value);
            map.put(field, affil);
        }
    }

    private void addTitle(final DatasetFacade facade, final List<Field> fields) {
        final Field title = createPrimitiveSingleField("title", facade.getTitle());
        fields.add(title);
    }

    private void addDepositor(final DatasetFacade facade, final List<Field> fields) {
        final Field deposit = createPrimitiveSingleField("depositor", facade.getDepositor());
        fields.add(deposit);
    }

    public Field createPrimitiveSingleField(final String name, final String value) {
        return new Field(name, CitationType.PRIMITIVE.toString(), false, value);
    }

    public Field createPrimitiveMultipleField(final String name, final String... value) {
        return new Field(name, CitationType.PRIMITIVE.toString(), true, value);
    }

    public Field createCompoundField(final String name, final boolean isMultiple, final List<Map<String, Object>> values) {
        Field cf = null;
        checkArgs(isMultiple, values);
        if (isMultiple) {
            cf = new Field(name, CitationType.COMPOUND.toString(), isMultiple, values);
        } else {
            cf = new Field(name, CitationType.COMPOUND.toString(), isMultiple, values.get(0));
        }
        return cf;
    }

    private void checkArgs(final boolean isMultiple, final List<?> values) {
        if (!isMultiple && values.size() > 1) {
            throw new IllegalArgumentException(
                    String.format("Field is not multiple but %d arguments were supplied", values.size()));
        }
    }

    public Field createControlledVocabField(final String name, final boolean isMultiple, final List<String> values) {
        Field cf = null;
        checkArgs(isMultiple, values);
        if (isMultiple) {
            cf = new Field(name, CitationType.CONTROLLEDVOCABULARY.toString(), isMultiple, values);
        } else {
            cf = new Field(name, CitationType.CONTROLLEDVOCABULARY.toString(), isMultiple, values.get(0));
        }
        return cf;
    }

}
