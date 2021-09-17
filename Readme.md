# Dataverse API bindings project

This project is a Java wrapper around the [Dataverse  API](http://guides.dataverse.org/en/4.2/api/).  
It was initially contributed by [ResearchSpace](www.researchspace.com) in October 2016.

## Building 

### Dependencies 

This project requires Java 8 to compile and run.  
It also uses Spring-web (to provide low-level HTTP request/response parsing.)    
The Sword client library is included in this project as a jar file as it is not available
 in a public maven repository.

### Building 

This project is built using Gradle. You can build straight away without needing to install anything:

    ./gradlew clean build -x integrationTest
   
which will compile, run unit tests (but not integration tests) and build a jar file.

### Running integration tests

Integration tests require a connection to  a Dataverse instance.
In order to connect to a Dataverse for running tests, the following configuration is set up in `test.properties`.

    dataverseServerURL=https://demo.dataverse.org
    dataverseAlias=rspace-demo
    
As a minimum, you'll need to specify an API key on the command line to run the tests:

    ./gradlew clean integrationTest -DdataverseApiKey=xxx-xxxx-xxxx

You can also override the Dataverse server URL and Id with your own settings by setting them on the command line:

    ./gradlew clean integrationTest  -DdataverseServerURL=https://my.dataverse.org -DdataverseApiKey=xxx-xxx-xxx -DdataverseAlias=MY-DEMO-DATAVERSE
    
### Installing into a Maven repository

This project can be added as a Gradle or Maven dependency in your project using [JitPack](https://jitpack.io).

Or, you can run:

    ./gradlew clean install
    
to install into a local repository and generate a pom.xml file for calculating dependencies.
    
## Usage

The best way to explore the bindings currently is by examining integration tests, especially those extending from `AbstractIntegrationTest`.

Very briefly....

```java
    DataverseAPI api = new DataverseAPIImpl();
    //must set in serverURL and apiKey first.
    DataverseConfig config = new DataverseConfig(serverURL, apiKey, dataverseAlias);
    api.configure(config);
    // now you can call
    api.getDataverseOperations().getDataverseById(dataverseAlias);
```

Searching uses a builder pattern to build a search query:

```java
    SearchOperations searchOps = dataverseAPI.getSearchOperations();
    SearchConfig cfg = searchOps.builder().q(FILE_SEARCH_TERM)
				.sortBy(SortBy.date)
				.sortOrder(SortOrder.asc)
				.showFacets(true)
				.showRelevance(true)
				.start(1)
				.perPage(3)
				.build();
    DataverseResponse<SearchResults<Item>> results = searchOps.search(cfg);
```

### Synchronisation and thread-safety

There is no explicit synchronisation performed in this library. The Dataverse configuration is stored in the 
internal state of  implementation classes, so new instances of `DataverseAPIImpl` should be used for each request if running in a multi-threaded environment connecting to different Dataverses.
    
## Developing

This project makes use of [Project Lombok](https://projectlombok.org) which greatly speeds up the development of POJO classes to wrap JSON data structures. There are [instructions](https://projectlombok.org/features/index.html) on how to add it to your IDE.

### Coding standards

Please make sure tests pass before committing, and to add new tests for new additions.

## Progress

API | Endpoint | URL | Implemented ?| Notes 
------|----------|-----|--------------|-------
Native|Dataverses | POST `api/dataverses/$id` | Y| - 
| -   | -         | GET `api/dataverses/$id` | Y | -
| -   | -         | GET `api/dataverses/$id/contents` | Y | -
| -   | -         | DELETE `api/dataverses/$id` | Y | -
| -   | -         | POST `api/dataverses/$id/datasets` | Y | -
| -   | -         | POST `api/dataverses/$identifier/actions/:publish` | Y | -
Native|Datasets | POST `api/dataverses/$id` | Y| -
| -   | -         | GET `api/datasets/$id` | Y | -
| -   | -         | DELETE `api/datasets/$id` | Y | -
| -   | -         | GET `api/datasets/$id/versions` | Y | -
| -   | -         | GET `PUT api/datasets/$id/versions/:draft?` | Y | -
| -   | -         | POST `PUT api/datasets/$id/actions/:publish?type=$type` | Y | -
Native|MetadataBlocks | GET ` api/metadatablocks` | Y| -
| -   | -         | GET ` api/metadatablocks/$identifier` | Y| -
Search | - | GET `api/search` | In progress | All query params supported, optional data not returned yet.
Sword | Upload file | 'Add files to a dataset with a zip file' | Y | -
