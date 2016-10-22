package com.researchspace.dataverse.entities.facade;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.ibm.icu.text.SimpleDateFormat;
import com.researchspace.dataverse.entities.Citation;
import com.researchspace.dataverse.entities.CitationField;
import com.researchspace.dataverse.entities.CitationType;
import com.researchspace.dataverse.entities.DataSetMetadataBlock;
import com.researchspace.dataverse.entities.Dataset;
import com.researchspace.dataverse.entities.DatasetVersion;

/**
 * Converts POJO Java facade into underlying JSON object representation
 * 
 * @author rspace
 *
 */
public class DatasetBuilder {

	private static final String KEYWORD_VOCABULARY_URI = "keywordVocabularyURI";
	private static final String KEYWORD_VOCABULARY = "keywordVocabulary";
	private static final String KEYWORD_VALUE = "keywordValue";
	private static final String DATASET_DESC_VALUE = "dsDescriptionValue";
	private static final String DATASET_DESC_DATE = "dsDescriptionDate";
	private static final String DATASET_CONTACT_EMAIL = "datasetContactEmail";
	private static final String DATASET_CONTACT_NAME = "datasetContactName";
	private static final String DATASET_CONTACT_AFFILIATION = "datasetContactAffiliation";
	private static final String AUTHOR_IDENTIFIER_SCHEME = "authorIdentifierScheme";
	private static final String AUTHOR_IDENTIFIER = "authorIdentifier";
	private static final String AUTHOR_AFFILIATION = "authorAffiliation";
	private static final String AUTHOR_NAME = "authorName";

	public Dataset build(DatasetFacade facade) {
		DatasetVersion dv = new DatasetVersion();
		DataSetMetadataBlock blocks = new DataSetMetadataBlock();
		Citation citation = new Citation();

		List<CitationField> fields = createFields(facade, citation);

		dv.setMetadataBlocks(blocks);
		blocks.setCitation(citation);
		citation.setFields(fields);
		Dataset toSubmit = new Dataset();
		toSubmit.setDatasetVersion(dv);
		return toSubmit;
	}

	private List<CitationField> createFields(DatasetFacade facade, Citation citation) {
		List<CitationField> fields = new ArrayList<>();
		//mandatory fields
		addTitle(facade, fields);	
		addAuthors(facade, fields);
		addDescription(facade, fields);
		addKeywords(facade, fields);
		addSubject(facade, fields);
		addContacts(facade, fields);
		
		//optional fields
		addDepositor(facade, fields);
		addSubTitle(facade, fields);
		addAlternativeTitle(facade, fields);
		addAlternativeURL(facade, fields);
		return fields;
	}

	

	private void addAlternativeURL(DatasetFacade facade, List<CitationField> fields) {
		if (facade.getAlternativeURL() != null) {
			CitationField altUrl = createPrimitiveSingleField("alternativeURL", facade.getAlternativeURL().toString());
			fields.add(altUrl);
		}
	}

	private void addAlternativeTitle(DatasetFacade facade, List<CitationField> fields) {
		if (!isEmpty(facade.getAlternativeTitle())) {
			CitationField title = createPrimitiveSingleField("alternativeTitle", facade.getAlternativeTitle());
			fields.add(title);
		}
	}

	private void addSubTitle(DatasetFacade facade, List<CitationField> fields) {
		if (!isEmpty(facade.getSubtitle())) {
			CitationField subtitle = createPrimitiveSingleField("subtitle", facade.getSubtitle());
			fields.add(subtitle);
		}
	}

	private void addSubject(DatasetFacade facade, List<CitationField> fields) {
		CitationField subject = createControlledVocabField("subject", true, Arrays.asList(facade.getSubject()));
		fields.add(subject);
	}

	private void addDescription(DatasetFacade facade, List<CitationField> fields) {
		List<DatasetDescription> descs = facade.getDescriptions();
		List<Map<String, Object>> descList = new ArrayList<>();
		for (DatasetDescription desc: descs) {
			Map<String, Object> map2 = new HashMap<>();
			CitationField descF = createPrimitiveSingleField(DATASET_DESC_VALUE, desc.getDescription());
			map2.put(DATASET_DESC_VALUE, descF);
			if(desc.getDate() != null) {
				CitationField dateF = createPrimitiveSingleField(DATASET_DESC_DATE, isoDate(desc));
				map2.put(DATASET_DESC_DATE, dateF);
			}
			descList.add(map2);
		}
		
		CitationField desc = createCompoundField("dsDescription", true, descList);
		fields.add(desc);
	}
	
	private void addKeywords(DatasetFacade facade, List<CitationField> fields) {
		List<DatasetKeyword> keywords = facade.getKeywords();
		List<Map<String, Object>> keysList = new ArrayList<>();
		for (DatasetKeyword desc: keywords) {
			Map<String, Object> map2 = new HashMap<>();
			CitationField descF = createPrimitiveSingleField(KEYWORD_VALUE, desc.getValue());
			map2.put(KEYWORD_VALUE, descF);
			if(desc.getVocabulary() != null) {
				CitationField vocab = createPrimitiveSingleField(KEYWORD_VOCABULARY, desc.getVocabulary());
				map2.put(KEYWORD_VOCABULARY, vocab);
			}
			if(desc.getVocabularyURI() != null) {
				CitationField vocabURI = createPrimitiveSingleField(KEYWORD_VOCABULARY_URI, desc.getVocabularyURI().toString());
				map2.put(KEYWORD_VOCABULARY_URI, vocabURI);
			}
			keysList.add(map2);
		}
		
		CitationField desc = createCompoundField("keyword", true, keysList);
		fields.add(desc);
		
	}

	private String isoDate(DatasetDescription desc) {
		return String.format("%tF", desc.getDate());
	}

	private void addContacts (DatasetFacade facade, List<CitationField> fields) {
		List<DatasetContact> contacts = facade.getContacts();
		List<Map<String, Object>> contactsList = new ArrayList<>();

		for (DatasetContact contact : contacts) {
			Map<String, Object> map2 = new HashMap<>();
			CitationField email = createPrimitiveSingleField(DATASET_CONTACT_EMAIL, contact.getDatasetContactEmail());
			map2.put(DATASET_CONTACT_EMAIL, email);
			if (!isEmpty(contact.getDatasetContactName())) {
				CitationField name = createPrimitiveSingleField(DATASET_CONTACT_NAME, contact.getDatasetContactName());
				map2.put(DATASET_CONTACT_NAME, name);
			}
			if (!isEmpty(contact.getDatasetContactAffiliation())) {
				CitationField affiliation = createPrimitiveSingleField(DATASET_CONTACT_AFFILIATION,
						contact.getDatasetContactAffiliation());
				map2.put(DATASET_CONTACT_AFFILIATION, affiliation);
			}
			contactsList.add(map2);
		}
		CitationField contact = createCompoundField("datasetContact", true, contactsList);
		fields.add(contact);
	}

	private void addAuthors (DatasetFacade facade, List<CitationField> fields) {
		List<DatasetAuthor> authors = facade.getAuthors();
		List<Map<String, Object>> authorsMap = new ArrayList<>();

		for (DatasetAuthor author : authors) {
			Map<String, Object> map = new HashMap<>();
			CitationField authorName = createPrimitiveSingleField(AUTHOR_NAME, author.getAuthorName());
			map.put(AUTHOR_NAME, authorName);
			if (!isEmpty(author.getAuthorAffiliation())) {
				CitationField affil = createPrimitiveSingleField(AUTHOR_AFFILIATION, author.getAuthorAffiliation());
				map.put(AUTHOR_AFFILIATION, affil);
			}
			if (!isEmpty(author.getAuthorIdentifier())) {
				CitationField id = createPrimitiveSingleField(AUTHOR_IDENTIFIER, author.getAuthorIdentifier());
				map.put(AUTHOR_IDENTIFIER, id);
			}
			if (!isEmpty(author.getAuthorIdentifierScheme())) {
				CitationField scheme = createControlledVocabField(AUTHOR_IDENTIFIER_SCHEME, false,
						asList(new String[] { author.getAuthorIdentifierScheme() }));
				map.put(AUTHOR_IDENTIFIER_SCHEME, scheme);
			}
			authorsMap.add(map);
		}
		CitationField toAdd = createCompoundField("author", true, authorsMap);
		fields.add(toAdd);
	}

	private void addTitle(DatasetFacade facade, List<CitationField> fields) {
		CitationField title = createPrimitiveSingleField("title", facade.getTitle());
		fields.add(title);
	}

	private void addDepositor(DatasetFacade facade, List<CitationField> fields) {
		CitationField deposit = createPrimitiveSingleField("depositor", facade.getDepositor());
		fields.add(deposit);
	}

	public CitationField createPrimitiveSingleField(String name, String value) {
		CitationField cf = new CitationField(name, CitationType.PRIMITIVE.toString(), false, value);
		return cf;
	}

	public CitationField createPrimitiveMultipleField(String name, String... value) {
		CitationField cf = new CitationField(name, CitationType.PRIMITIVE.toString(), true, value);
		return cf;
	}

	public CitationField createCompoundField(String name, boolean isMultiple, List<Map<String, Object>> values) {
		CitationField cf = null;
		checkArgs(isMultiple, values);
		if (isMultiple) {
			cf = new CitationField(name, CitationType.COMPOUND.toString(), isMultiple, values);
		} else {
			cf = new CitationField(name, CitationType.COMPOUND.toString(), isMultiple, values.get(0));
		}
		return cf;
	}

	private void checkArgs(boolean isMultiple, List<?> values) {
		if (!isMultiple && values.size() > 1) {
			throw new IllegalArgumentException(
					String.format("Field is not multiple but %d arguments were supplied", values.size()));
		}
	}

	public CitationField createControlledVocabField(String name, boolean isMultiple, List<String> values) {
		CitationField cf = null;
		checkArgs(isMultiple, values);
		if (isMultiple) {
			cf = new CitationField(name, CitationType.CONTROLLEDVOCABULARY.toString(), isMultiple, values);
		} else {
			cf = new CitationField(name, CitationType.CONTROLLEDVOCABULARY.toString(), isMultiple, values.get(0));
		}
		return cf;
	}

}
