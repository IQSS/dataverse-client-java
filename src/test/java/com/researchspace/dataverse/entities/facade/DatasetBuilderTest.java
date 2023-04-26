/** <pre>
Copyright 2016 ResearchSpace

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
</pre> */
package com.researchspace.dataverse.entities.facade;

import static com.researchspace.dataverse.entities.facade.DatasetTestFactory.createFacade;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.researchspace.dataverse.entities.Dataset;

public class DatasetBuilderTest {

    DatasetBuilder builder;
    @Before
    public void setUp() throws Exception {
        builder = new DatasetBuilder();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws URISyntaxException, IOException {
        final DatasetFacade facade = createFacade();
        final ObjectWriter mapper = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final Dataset dversion = builder.build(facade);
        final String json = mapper.writeValueAsString(dversion);
        assertEquals(StringUtils.difference("There are differences between output and expected : " +
                FileUtils.readFileToString(new File("src/integration-test/resources/dataset-builder-test.json")),
                json),
                FileUtils.readFileToString(new File("src/integration-test/resources/dataset-builder-test.json")),
                json);
    }
}
