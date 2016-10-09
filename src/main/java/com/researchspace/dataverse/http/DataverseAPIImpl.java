package com.researchspace.dataverse.http;

import com.researchspace.dataverse.api.v1.DatasetOperations;
import com.researchspace.dataverse.api.v1.DataverseAPI;
import com.researchspace.dataverse.api.v1.DataverseConfig;
import com.researchspace.dataverse.api.v1.DataverseOperations;
import com.researchspace.dataverse.api.v1.InfoOperations;
import com.researchspace.dataverse.api.v1.MetadataOperations;
/**  Copyright 2016 ResearchSpace

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

*/
public class DataverseAPIImpl implements DataverseAPI {
	private DataverseOperationsImplV1 dvOperationsImpl = new DataverseOperationsImplV1();

	@Override
	public DatasetOperations getDatasetOperations() {
		return dvOperationsImpl;
	}

	@Override
	public MetadataOperations getMetadataOperations() {
		return dvOperationsImpl;
	}

	@Override
	public DataverseOperations getDataverseOperations() {
		return dvOperationsImpl;
	}

	@Override
	public void configure(DataverseConfig config) {
		dvOperationsImpl.setApiKey(config.getApiKey());
		dvOperationsImpl.setServerURL(config.getServerURL().toString());		
	}

	@Override
	public InfoOperations getInfoOperations() {
		return dvOperationsImpl;
	}
}