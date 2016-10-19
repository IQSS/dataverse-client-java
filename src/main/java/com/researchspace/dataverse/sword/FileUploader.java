package com.researchspace.dataverse.sword;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.swordapp.client.AuthCredentials;
import org.swordapp.client.Deposit;
import org.swordapp.client.DepositReceipt;
import org.swordapp.client.ProtocolViolationException;
import org.swordapp.client.SWORDClient;
import org.swordapp.client.SWORDClientException;
import org.swordapp.client.SWORDError;

import lombok.extern.slf4j.Slf4j;
/**
 * Uploads using SWORD client library
 *  Copyright 2016 ResearchSpace

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

* This is an internal package for calling SWORD-API.
 * @author rspace
 *
 */
@Slf4j
public class FileUploader {

	private static final String APPLICATION_ZIP = "application/zip";
	private static final String ZIP_PACKAGING = "http://purl.org/net/sword/package/SimpleZip";

	/**
	
	 * @param file
	 * @param apiKey
	 * @param dataverseServer Server root e.g. "https://apitest.dataverse.org"
	 * @param doi FRagment e.g.  "10.5072/FK2/MGADL1"
	 * @return
	 * @throws IOException
	 * @throws SWORDClientException
	 * @throws SWORDError
	 * @throws ProtocolViolationException
	 */
	public DepositReceipt deposit(File file, String apiKey, URI dataverseServer, String doi)
			throws IOException, SWORDClientException, SWORDError, ProtocolViolationException {
		SWORDClient cli = new SWORDClient();
		Deposit dep = new Deposit();
		dep.setFilename(file.getName());
		dep.setFile(new FileInputStream(file));
		dep.setMimeType(APPLICATION_ZIP);
		dep.setPackaging(ZIP_PACKAGING);
		
		AuthCredentials cred = new AuthCredentials(apiKey, "");
	
		String depositURI = dataverseServer.toString() + "/dvn/api/data-deposit/v1.1/swordv2/edit-media/study/doi:"
				+ doi;
		DepositReceipt rct = cli.deposit(depositURI, dep, cred);
		log.info("Deposit received with status {}" ,rct.getStatusCode());
		return rct;

	}

}
