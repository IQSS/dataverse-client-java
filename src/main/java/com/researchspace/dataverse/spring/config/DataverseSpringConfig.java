package com.researchspace.dataverse.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.researchspace.dataverse.api.v1.DataverseAPI;
import com.researchspace.dataverse.http.DataverseAPIImpl;

/**
 * Wires up classes and produces Beans for this component.
 * @author rspace
 *
 */
@Configuration
public class DataverseSpringConfig {
	
	@Bean
	@Scope(value="prototype")
	DataverseAPI dataverseAPI(){
		return new DataverseAPIImpl();
	}
	
}
