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
package com.researchspace.dataverse.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.researchspace.dataverse.api.v1.DataverseConfig;
import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.search.entities.DatasetItem;
import com.researchspace.dataverse.search.entities.DataverseItem;
import com.researchspace.dataverse.search.entities.FileSearchHit;
import com.researchspace.dataverse.search.entities.Item;
import com.researchspace.dataverse.search.entities.SearchConfig;
import com.researchspace.dataverse.search.entities.SearchResults;
import com.researchspace.dataverse.search.entities.SearchType;
import com.researchspace.dataverse.testutils.TestFileUtils;


public class SearchOpsMockServerTest  {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSimpleSearch() throws MalformedURLException {
        final SearchConfig scfg = SearchConfig.builder().q("trees").build();
        final RestTemplate template = new RestTemplate();
        final SearchOperationsImplV1 tss = setupSearchOps(template);
        setUpServerResponse(template, "http://anyDataverse.com/api/v1/search?q=trees", getSimpleQueryResult() );

        final DataverseConfig cfg = new DataverseConfig(new URL("http://anyDataverse.com"), "any", "alias");
        tss.configure(cfg);
        final DataverseResponse<SearchResults<Item>> resp = tss.search(scfg);
        assertNotNull(resp.getData());
        assertEquals("trees", resp.getData().getQ());
    }

    private void setUpServerResponse(final RestTemplate template, final String url, final String response) {
        final MockRestServiceServer server = MockRestServiceServer.bindTo(template).build();
        server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(url))
        .andExpect(method(HttpMethod.GET))
        .andRespond(MockRestResponseCreators.withSuccess(response,
                APPLICATION_JSON));
    }

    @Test
    public void testComplexSearch() throws MalformedURLException {
        final SearchConfig scfg = SearchConfig.builder().q("trees").build();
        final RestTemplate template = new RestTemplate();
        final SearchOperationsImplV1 tss = setupSearchOps(template);
        setUpServerResponse(template, "http://anyDataverse.com/api/v1/search?q=trees", getComplexQueryResult() );
        final DataverseConfig cfg = new DataverseConfig(new URL("http://anyDataverse.com"), "any", "alias");
        tss.configure(cfg);
        final DataverseResponse<SearchResults<Item>> resp = tss.search(scfg);
        assertNotNull(resp.getData());
        assertEquals("trees", resp.getData().getQ());
        final SearchResults<Item> searches = resp.getData();
        assertEquals(3, searches.getTotalCount());
        assertEquals(3, searches.getCountInResponse());
        assertEquals(3, searches.getItems().size());
        assertEquals(1, searches.filterByType(SearchType.dataset).size());
        assertEquals(1, searches.filterByType(SearchType.dataverse).size());
        assertEquals(1, searches.filterByType(SearchType.file).size());
    }

    @Test
    public void testFileSearch() throws MalformedURLException {
        final SearchConfig scfg = SearchConfig.builder().q("trees").type(EnumSet.of(SearchType.file)).build();
        final RestTemplate template = new RestTemplate();
        final SearchOperationsImplV1 tss = setupSearchOps(template);
        setUpServerResponse(template, "http://anyDataverse.com/api/v1/search?q=trees&type=file", getFileQueryResult() );
        final DataverseConfig cfg = new DataverseConfig(new URL("http://anyDataverse.com"), "any", "alias");
        tss.configure(cfg);
        final DataverseResponse<SearchResults<FileSearchHit>> resp = tss.searchFiles(scfg);
        final SearchResults<FileSearchHit> searches = resp.getData();

        assertBasicSearchResultParsing(searches);
        assertEquals("text/plain", searches.getItems().get(0).getFileContentType());
    }

    @Test
    public void testDatasetSearch() throws MalformedURLException {
        final SearchConfig scfg = SearchConfig.builder().q("trees").type(EnumSet.of(SearchType.dataset)).build();
        final SearchOperationsImplV1 tss = setUpSearch(scfg, "http://anyDataverse.com/api/v1/search?q=trees&type=dataset", ()->getDatasetQueryResult()  );

        final DataverseResponse<SearchResults<DatasetItem>> resp = tss.searchDatasets(scfg);
        final SearchResults<DatasetItem> searches = resp.getData();

        assertBasicSearchResultParsing(searches);
        assertEquals("doi:10.5072/FK2/1FUEXN", searches.getItems().get(0).getGlobalId());
    }

    @Test
    public void testDataverseSearch() throws MalformedURLException {
        final SearchConfig scfg = SearchConfig.builder().q("trees").type(EnumSet.of(SearchType.dataverse)).build();
        final SearchOperationsImplV1 tss = setUpSearch(scfg, "http://anyDataverse.com/api/v1/search?q=trees&type=dataverse", ()->getDataverseQueryResult()  );

        final DataverseResponse<SearchResults<DataverseItem>> resp = tss.searchDataverses(scfg);
        final SearchResults<DataverseItem> searches = resp.getData();

        assertBasicSearchResultParsing(searches);
        assertEquals("https://demo.dataverse.org/dataverse/trunctest", searches.getItems().get(0).getUrl());
    }

    SearchOperationsImplV1  setUpSearch  (final SearchConfig srchCfg, final String url, final GetJson expectedJsonGetter) throws MalformedURLException {
        final RestTemplate template = new RestTemplate();
        final SearchOperationsImplV1 tss = setupSearchOps(template);
        setUpServerResponse(template, url, expectedJsonGetter.getJson() );
        final DataverseConfig cfg = new DataverseConfig(new URL("http://anyDataverse.com"), "any", "alias");
        tss.configure(cfg);
        return tss;
    }

    void assertBasicSearchResultParsing (final SearchResults<? extends Item> searches) {
        assertNotNull(searches);
        assertEquals("trees", searches.getQ());
        assertEquals(1, searches.getTotalCount());
        assertEquals(1, searches.getCountInResponse());
        assertEquals(1, searches.getItems().size());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFileSearchThrowsIAEIfTypeNotFile() {
        final SearchConfig scfg = SearchConfig.builder().q("trees").type(EnumSet.of(SearchType.dataset)).build();
        final RestTemplate template = new RestTemplate();
        final SearchOperationsImplV1 tss = setupSearchOps(template);
        tss.searchFiles(scfg);
    }


    private SearchOperationsImplV1 setupSearchOps(final RestTemplate template) {
        final SearchOperationsImplV1 tss = new SearchOperationsImplV1();
        tss.setTemplate(template);
        return tss;
    }

    @FunctionalInterface interface GetJson {

        String getJson ();

    }


    private String getSimpleQueryResult() {
        return TestFileUtils.getJsonFromFile("simpleQuery.json");
    }

    //gets 3 results: file, dataset, dataverse
    private String getComplexQueryResult() {
        return TestFileUtils.getJsonFromFile("multiTypeSearch.json");
    }
    private String getFileQueryResult() {
        return TestFileUtils.getJsonFromFile("fileSearch.json");
    }
    private String getDatasetQueryResult() {
        return TestFileUtils.getJsonFromFile("datasetSearch.json");
    }
    private String getDataverseQueryResult() {
        return TestFileUtils.getJsonFromFile("dataverseSearch.json");
    }

}
