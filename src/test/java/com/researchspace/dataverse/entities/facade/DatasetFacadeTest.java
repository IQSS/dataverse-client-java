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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DatasetFacadeTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = NullPointerException.class)
    public void testAuthorRequired() {
        DatasetFacade.builder().contact(DatasetTestFactory.buildAContact()).build();
    }

    @Test(expected = NullPointerException.class)
    public void testContactRequired() {
        DatasetFacade.builder().author(DatasetTestFactory.buildAnAuthor()).build();
    }

    @Test
    public void testProductionDate() {
        final DatasetFacade facade = new DatasetFacade() ;
        assertNull(facade.getProductionDate());
        final Calendar cal = Calendar.getInstance();
        final Date now = cal.getTime();
        final long nowMillis = now.getTime();
        facade.setProductionDate(now);
        now.setTime(10000L);

        assertEquals(nowMillis, facade.getProductionDate().getTime());

    }

}
