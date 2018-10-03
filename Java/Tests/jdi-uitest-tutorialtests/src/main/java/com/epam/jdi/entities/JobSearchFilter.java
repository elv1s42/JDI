package com.epam.jdi.entities;

import com.epam.commons.DataClass;
import com.epam.jdi.enums.JobCategories;

import static com.epam.jdi.enums.JobCategories.QA;
import static com.epam.jdi.enums.Locations.SAINT_PETERSBURG;

/**
 * Created by Roman_Iovlev on 10/22/2015.
 */

/*@AllArgsConstructor
@NoArgsConstructor*/
public class JobSearchFilter extends DataClass {
    public CharSequence keywords = "Test Automation Engineer (back-end)";
    public JobCategories category = QA;
    public String location = SAINT_PETERSBURG.value;
    public JobSearchFilter(){ }

    public JobSearchFilter(CharSequence keywords, JobCategories category,String location ){
        this.keywords=keywords;
        this.category=category;
        this.location=location;
    }

}
