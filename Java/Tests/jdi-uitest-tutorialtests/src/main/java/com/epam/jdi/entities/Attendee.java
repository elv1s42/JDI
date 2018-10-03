package com.epam.jdi.entities;


import com.epam.commons.DataClass;

/**
 * Created by Roman_Iovlev on 10/22/2015.
 */
/*@AllArgsConstructor
@NoArgsConstructor*/
public class Attendee extends DataClass<Attendee> {
    public JobSearchFilter filter = new JobSearchFilter();

    public String name = "Roman";
    public String lastName = "Iovlev";
    public String email = "roman_iovlev@epam.com";
    public String country = "Russian Federation";
    public String city = "Saint-Petersburg";
    //public String cv = new File("").getAbsolutePath() +  "\\src\\test\\resources\\cv.txt";
    public String comment = "I WANT TO WORK IN EPAM!!!";

}
