package com.researchspace.dataverse.api.v1;

import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.entities.DvMessage;

public interface InfoOperations {
	
	DvMessage getDatasetPublishPopupCustomText () ;
	
	DataverseResponse<Object> setDatasetPublishPopupCustomText (String text) ;

}
