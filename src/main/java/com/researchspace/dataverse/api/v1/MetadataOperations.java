package com.researchspace.dataverse.api.v1;

import java.util.List;

import com.researchspace.dataverse.entities.MetadataBlock;

public interface MetadataOperations {
	
	/**
	 * Return data about the block whose identifier is passed. 
	 *  identifier can either be the block’s id, or its name:
	 *  <p/>
	 * <strong>Possible bug </strong>Doesn't actually work for numeric ids, only name
	 * @param name The MetadataBlock name
	 * @return
	 */
	MetadataBlock getMetadataById(String name);
	
	/**
	 * Lists brief info about all metadata blocks registered in the system:
	 * @return a {@link List} of {@link MetadataBlock}
	 */
	List<MetadataBlock> getMetadataBlockInfo();

}
