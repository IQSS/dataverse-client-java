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

/**
 * Listing of Kind Of Data for Dataset 'KindOfData' field.
 * @author ltromel
 */
public enum DatasetKindOfData {
    AUDIOVISUAL("Audiovisual"),
    COLLECTION("Collection"),
    DATASET("Dataset"),
    EVENT("Event"),
    IMAGE("Image"),
    INTERACTIVE_RESOURCE("Interactive Resource"),
    OODEL("Model"),
    PHYSICAL_OBJECT("Physical Object"),
    SERVICE("Service"),
    SOFTWARE("Software"),
    SOUND("Sound"),
    TEXT("Text"),
    WORKFLOW("Workflow"),
    OTHER("Other");

    private String displayName;
    DatasetKindOfData (final String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

}
