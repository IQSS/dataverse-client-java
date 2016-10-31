package com.researchspace.dataverse.testutils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TestFileUtils {
	
	public static String getJsonFromFile (String filename) {
		try {
			return FileUtils.readFileToString(new File("src/test/resources/data/json", filename));
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read file " + filename);
		}
	}

}
