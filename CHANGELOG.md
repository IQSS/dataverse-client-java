Significant changes since 0.1.0

1.4.0 2026-02-19

- feature: upgraded Jackson dependencies and registered JdkModule with ObjectMapper so that Optional fields can be serialised

1.3.0 2025-12-01

- feature: added 'license' field to DatasetFacade/DatasetVersion #32
- feature: added DataverseOperation to support querying for allowed metadata language(s) #31 

1.2.0 2025-09-15

- feature: added options to set metadataLanguages and different PID protocols #28 #29

1.1.0 In progress

- feature: Support upload of files to a dataset using native API. #16
- feature: After creating a Dataset, the persistent ID is stored in the Identifier object. #22

1.0.0 2022-11-21

Increasing major version due to major updates to dependencies. However, there are no
breaking API changes in this library. 

- dependencies: Major dependency updates to Spring 5.3, Lombok 18.24. 
- build: fix integration tests
- build: enable integration test running through Github actions
- build: test build and test on Java 8, 11, and 17.

0.2.0 2022-11-20

- build:  update gradlew to use gradle 7.5
- build:  Basic Github action to run tests on pull request
- dependency: update Lombok from 1.16 to 1.18.4 to enable building on Java 11
- feature: #12 overloaded method for uploadFile

