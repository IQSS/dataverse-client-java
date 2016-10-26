package com.researchspace.dataverse.entities.facade;

public enum ContributorType {
    DataCollector("Data Collector"),
    DataCurator("Data Curator"),
    DataManager("Data Manager"),
    Editor("Editor"),
    Funder("Funder"),
    HostingInstitution("Hosting Institution"),
    ProjectLeader("Project Leader"),
    ProjectManager("Project Manager"),
    ProjectMember("Project Member"),
    RelatedPerson("Related Person"),
    Researcher("Researcher"),
    ResearchGroup("Research Group"),
    RightsHolder("Rights Holder"),
    Sponsor("Sponsor"),
    Supervisor("Supervisor"),
    WorkPackageLeader("Work Package Leader"),
    Other("Other");
	
	private String displayName;
	private ContributorType (String displayName) {
		this.displayName = displayName;
	}
	public String getDisplayName() {
		return displayName;
	}

}
