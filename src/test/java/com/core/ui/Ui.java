package com.core.ui;

import com.core.pages.DeveloperApplyPage;
import com.core.pages.DeveloperPortalPage;
import com.core.pages.HomePage;
import com.core.setup.SeleniumDriverManager;
import com.epam.reportportal.annotations.Step;
import io.cucumber.java.*;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

@Slf4j
public class Ui {
    WebDriver driver;

    @BeforeStep
    public void setup(){
        String BROWSER = System.getProperty("BROWSER");
        driver = SeleniumDriverManager.getWebDriver(BROWSER);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Given("Test kennethreitz")
    public void applyAsDeveloper() {
        HomePage home = new HomePage(driver);
        home.isPageOpened();
        home.checkPresentHeaderelement();
        home.checkPresentBookElement();
    }

    @AfterStep
    public void afterScenario(Scenario scenario) {
        takeScreenshot(scenario);
        driver.quit();
    }

    @Step("Take screenshot")
    private void takeScreenshot(Scenario scenario) {
//		if (scenario.isFailed()) {
            if (driver instanceof TakesScreenshot) {
                scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png", "Screenshot");
            }
//		}
    }

}
