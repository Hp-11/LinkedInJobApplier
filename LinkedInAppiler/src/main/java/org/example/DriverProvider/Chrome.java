package org.example.DriverProvider;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.util.logging.Logger;

public class Chrome {
    public static WebDriver getDriver(Logger logger) {
        String chromeDriverPath = "C:/Dev/seleniumTest/AutomationReports/src/main/resources/chromedriver-win64/chromedriver-win64/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        // Verify the chromedriver file exists
        File chromeDriverFile = new File(chromeDriverPath);
        if (!chromeDriverFile.exists()) {
            logger.severe("Chromedriver executable not found at: " + chromeDriverPath);
            return null;
        }

        return new ChromeDriver();
    }
}