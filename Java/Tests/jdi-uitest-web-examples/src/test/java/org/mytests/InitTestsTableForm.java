package org.mytests;

import com.epam.jdi.uitests.web.selenium.elements.composite.WebSite;
import com.epam.jdi.uitests.web.testng.testRunner.TestNGBase;
import org.mytests.epam.site.site.EpamSite;
import org.testng.annotations.BeforeSuite;


/**
 * Created by Roman_Iovlev on 7/13/2015.
 */
public class InitTestsTableForm extends TestNGBase {

    @BeforeSuite(alwaysRun = true)
    public static void setUp() throws Exception {
        WebSite.init(EpamSite.class);
    }

}
