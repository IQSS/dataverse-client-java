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
</pre>
 */
package com.researchspace.dataverse.http;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.entities.DVField;
import com.researchspace.dataverse.entities.MetadataBlock;
import com.researchspace.springrest.ext.RestClientException;


/**
 * Metadata operations tests.
 */
public class MetadataOperationsTest  extends AbstractIntegrationTest {

    private static final int MIN_BLOCK_SIZE = 6;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testGetMetdataBlockInfo() {
        // metadat block size is variable
        assertTrue(metadataOPs.getMetadataBlockInfo().size() >= MIN_BLOCK_SIZE);
    }

    @Test(expected = RestClientException.class)
    public void testGetMetdataByIdBlockInfoWithInvalidId() {
        metadataOPs.getMetadataById("abcde");
    }

    @Test
    public void testGetMetdataByIdBlockInfo() throws IOException {
        final MetadataBlock block = metadataOPs.getMetadataById("biomedical");
        for (final DVField fld : block.getFields().values()) {
            assertNotNull(fld);
            //            log.info(fld.toString());
        }
    }
}
