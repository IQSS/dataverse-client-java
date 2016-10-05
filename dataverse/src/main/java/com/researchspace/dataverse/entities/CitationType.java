package com.researchspace.dataverse.entities;

public enum CitationType {

	PRIMITIVE("primitive"), COMPOUND("compound"), CONTROLLEDVOCABULARY("controlledVocabulary");
	CitationType(String displayName) {
		this.displayName = displayName;
	}
	
	public String toString (){
		return displayName;
	}

	private String displayName;
}
