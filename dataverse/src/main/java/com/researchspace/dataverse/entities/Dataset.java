package com.researchspace.dataverse.entities;

import java.net.URL;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor()
@NoArgsConstructor()
public class Dataset {
	private DatasetVersion datasetVersion, latestVersion;
	private Long id;
	private String identifier, protocol, authority;
	private URL persistentUrl;
	
	/**
	 * Getter for the DOI String used to identify a dataset for SWORD upload
	 * @return an {@link Optional}. Will be <code>null</code> if <code>persistentURL</code> is not set.
	 */
	public Optional<String> getDoiId (){
		if(persistentUrl == null) {
			return Optional.empty();
		} else {
			return Optional.of(getPersistentUrl().getPath().substring(1));
		}
	}
}
