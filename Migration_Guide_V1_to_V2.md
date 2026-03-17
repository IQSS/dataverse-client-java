Version 2.0.0 removes the SWORD protocol as an upload mechanism.

* SWORD library (`org.swordapp:sword2-client`) is no longer a dependency of the project.

* `com.researchspace.dataverse.api.v1.DatasetOperations` interface and `DataverseOperationsImplV1`
  implementation no longer provide `uploadFile()` methods. Use `uploadNativeFile()` methods instead.

* `com.researchspace.dataverse.sword.FileUploader` was removed.

For example usage of `uploadNativeFile()` method you can check implementation
of [DataverseRSpaceRepository.java](https://github.com/rspace-os/rspace-dataverse-adapter/blob/main/src/main/java/com/researchspace/dataverse/rspaceadapter/DataverseRSpaceRepository.java)
within [rspace-dataverse-adapter](https://github.com/rspace-os/rspace-dataverse-adapter) project.

If you need to use old SWORD upload you can still use version 1.4.2, which is available on JitPack
or buildable via git history.

