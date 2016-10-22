package com.researchspace.dataverse.entities.facade;

import java.util.Date;



import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class DatasetDescription {
	
	private @NonNull String  description;
	private Date date;

}
