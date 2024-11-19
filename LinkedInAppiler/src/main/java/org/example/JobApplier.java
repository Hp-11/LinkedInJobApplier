package org.example;

import org.example.DriverProvider.Chrome;
import org.openqa.selenium.WebDriver;
import java.util.logging.Level;
import java.util.logging.Logger;
 ;

public class JobApplier {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(JobApplier.class.getName());
        try {
            WebDriver driver = Chrome.getDriver(logger);
            if (driver == null) {
                logger.severe("Failed to initialize the WebDriver");
                return;
            }
            driver.get("http://www.linkedin.com/login");
            servicesLinkedin.loginToLinkedIn(driver);
            servicesLinkedin.getHrEmails(driver);
            servicesLinkedin.applyToJobs(driver);
            driver.quit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while initializing the WebDriver", e);
        }
    }


}