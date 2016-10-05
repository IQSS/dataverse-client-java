package com.researchspace.dataverse.entities.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.researchspace.dataverse.entities.Citation;
import com.researchspace.dataverse.entities.CitationField;
import com.researchspace.dataverse.entities.CitationType;
import com.researchspace.dataverse.entities.DataSetMetadataBlock;
import com.researchspace.dataverse.entities.Dataset;
import com.researchspace.dataverse.entities.DatasetVersion;
/**
 * Converts POJO Java facade into underlying JSON object representation
 * @author rspace
 *
 */
public class DatasetBuilder {

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
		addTitle(facade, fields);
		addAuthor(facade, fields);
		addContact(facade, fields);
		addDescription(facade, fields);
		addDepositor(facade, fields);
		addSubject(facade, fields);
		return fields;
	}

	private void addSubject(DatasetFacade facade, List<CitationField> fields) {
		CitationField subject = createControlledVocabField("subject", true, Arrays.asList(facade.getSubject()));
		fields.add(subject);
	}

	private void addDescription(DatasetFacade facade, List<CitationField> fields) {
		CitationField descValue = createPrimitiveSingleField("dsDescriptionValue", facade.getDescription());
		Map<String, Object> map2 = new HashMap<>();
		map2.put("dsDescriptionValue", descValue);
		CitationField desc = createCompoundField("dsDescription", true, Arrays.asList(map2));
		fields.add(desc);

	}

	private void addContact(DatasetFacade facade, List<CitationField> fields) {
		CitationField contactName = createPrimitiveSingleField("datasetContactName", facade.getContactName());
		CitationField contactEmail = createPrimitiveSingleField("datasetContactEmail", facade.getContactEmail());
		Map<String, Object> map2 = new HashMap<>();
		map2.put("datasetContactName", contactName);
		map2.put("datasetContactEmail", contactEmail);
		CitationField contact = createCompoundField("datasetContact", true, Arrays.asList(map2));
		fields.add(contact);
	}

	private void addAuthor(DatasetFacade facade, List<CitationField> fields) {
		CitationField authorName = createPrimitiveSingleField("authorName", facade.getAuthor());
		Map<String, Object> map = new HashMap<>();
		map.put("authorName", authorName);
		CitationField author = createCompoundField("author", true, Arrays.asList(map));
		fields.add(author);
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
