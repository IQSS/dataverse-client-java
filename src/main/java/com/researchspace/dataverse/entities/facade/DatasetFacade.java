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

import java.net.URL;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

/**
 * Simple POJO to set info for Dataset.
 * @author rspace
 */
@Data
@Builder
@AllArgsConstructor
public class DatasetFacade   {

    // License
    private String termsOfUse;

    // Citation metadata
    private @NonNull String title;
    private @NonNull @Singular List<DatasetAuthor> authors;
    private @NonNull @Singular List<DatasetContact> contacts;
    private @NonNull String subject;
    private @NonNull @Singular List<DatasetDescription> descriptions;
    private String  depositor, subtitle, alternativeTitle;
    private URL alternativeURL;
    private @Singular List<DatasetKeyword> keywords;
    private @Singular List<DatasetTopicClassification> topicClassifications;
    private @Singular List<DatasetPublication> publications;
    private @Singular List<DatasetProducer> producers;
    private String note;
    private List<String> languages;
    private Date productionDate;
    private @Singular("productionPlace") List<String> productionPlaces;
    private @Singular List<DatasetContributor> contributors;
    private @Singular("kindOfData") List<String> kindsOfData;
    private @Singular("timePeriodCovered") List<DatasetTimePeriodCovered> timePeriodsCovered;

    // Geospatial metadata
    private @Singular List<DatasetGeographicBoundingBox> geographicBoundingBoxes;

    /**
     * Returns a copy if the internally stored Date
     * @return
     */
    public Date getProductionDate () {
        if(productionDate != null){
            return new Date(productionDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * Sets this object's date as a copy of the parameter Date.
     * @param date
     */
    public void setProductionDate(final Date date) {
        productionDate = new Date(date.getTime());
    }

    /**
     * For testing
     */
    DatasetFacade() {
        // TODO Auto-generated constructor stub
    }

}
