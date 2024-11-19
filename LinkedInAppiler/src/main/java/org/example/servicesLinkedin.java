package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class servicesLinkedin {
    public static String currentHrEmails ;
    public static String currentHrName ;
    public static String currentHrCompany ;


    public static void loginToLinkedIn(WebDriver driver) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();
        wait.until(ExpectedConditions.urlContains("linkedin.com/feed"));
    }

    public static void applyToJobs(WebDriver driver) {
        clickApplyButton(driver);
        submitApplicationViaMailToCurrentHr(driver);
    }

    private static void submitApplicationViaMailToCurrentHr(WebDriver driver) {
    }

    private static void clickApplyButton(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        Set<WebElement> clickedParents = new HashSet<>();
        try {
            List<WebElement> liElements = driver.findElement(By.xpath("//body//ul[not(ancestor::header) and not(ancestor::nav)]")).findElements(By.xpath(".//li"));
            for (WebElement liElement : liElements) {
                WebElement parent = liElement.findElement(By.xpath(".."));
                if (clickedParents.contains(parent)) {
                    continue;
                }
                Utils.highlightElement(driver, liElement);
                liElement.click();
                clickedParents.add(parent);
                if (liElement.getText().contains("Applied")) {
                    continue;
                }
                if (liElement.getText().contains("Easy Apply")) {
                    handlePopupWindow(driver, wait);
                } else {
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='jobs-apply-button--top-card']"))).click();
                    saveTheJobUrl(driver);
                }
//                checkIfChange(driver,liElements);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkIfChange(WebDriver driver, List<WebElement> liElements) {
        List<WebElement> newLiElements = driver.findElement(By.xpath("//body//ul[not(ancestor::header) and not(ancestor::nav)]")).findElements(By.xpath(".//li"));
        for (WebElement newLiElement : newLiElements) {
            if (!liElements.contains(newLiElement)) {
                liElements.add(newLiElement);
            }
        }
    }

    private static void saveTheJobUrl(WebDriver driver) {
        // Get the current window handle
        String originalWindow = driver.getWindowHandle();

        // Wait for the new tab to open
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // Switch to the new tab
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Get the URL of the new tab
        String jobUrl = driver.getCurrentUrl();

        // Save the URL to the CSV file
        CSVWriter.writeToCSV(jobUrl);

        // Close the new tab and switch back to the original tab
        driver.close();
        driver.switchTo().window(originalWindow);
    }

    private static void handlePopupWindow(WebDriver driver, WebDriverWait wait) {
        // Get the current window handle
        String originalWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='jobs-apply-button--top-card']"))).click();

        // Wait for the new window or tab
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // Switch to the new window or tab
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Call the NextButtonsWindow method on the pop-up window
        NextButtonsWindow(wait);

        // Close the pop-up window and switch back to the original window
        driver.close();
        driver.switchTo().window(originalWindow);
    }

    private static void NextButtonsWindow(WebDriverWait wait) {
        try {
            WebElement easyApplyNextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Continue to next step']")));
            easyApplyNextButton.click();
            NextButtonsWindow(wait);
        } catch (Exception e) {
            try {
                WebElement reviewButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Review your application']")));
                reviewButton.click();
                NextButtonsWindow(wait);
            } catch (Exception ex) {
                WebElement reviewButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Submit application']")));
                reviewButton.click();
               return;
            }
        }
        if(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Next']"))).isDisplayed()) {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']"))).click();
        }
    }

    private static void searchForJobs(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, 'jobs/?')]"))).click();
        wait.until(ExpectedConditions.urlContains("linkedin.com/jobs"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Show all']"))).click();
        String currentUrl = driver.getCurrentUrl();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));

    }

    public static void getHrEmails(WebDriver driver) {
        searchForJobs(driver);
        getHrCompany(driver);
        getHrEmailsFromJobs(driver);
    }

    private static void getHrCompany(WebDriver driver) {
    }

    private static void getHrEmailsFromJobs(WebDriver driver) {
    }
}
