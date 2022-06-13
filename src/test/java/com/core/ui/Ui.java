package com.core.ui;

import com.core.constants.ConfigConstants;
import com.core.pages.HomePage;
import com.core.setup.SeleniumDriverManager;
import com.core.utils.Configs;
import com.epam.reportportal.annotations.Step;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.util.concurrent.TimeUnit;

import static com.core.constants.HttpConstants.HTTP_PORT_8181;
import static com.core.constants.HttpConstants.HTTP_PROTOCOL;
import static com.core.constants.SeleniumConstants.MP4;
import static com.core.constants.SeleniumConstants.VIDEO;
import static com.core.constants.SymbolsConstants.*;

@Slf4j
public class Ui {
    WebDriver driver;

    @BeforeStep
    public void setup() {
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
        home.openEtcMenu();
        home.scroll();
    }

    @AfterStep
    public void afterScenario(Scenario scenario) {
        takeScreenshot(scenario);
        takeVideo();
        driver.quit();
    }

    @Step("Take video")
    private void takeVideo() {
        SessionId sessionId = ((RemoteWebDriver) driver).getSessionId();
        String url = HTTP_PROTOCOL +
                PROTOCOL_SEPARATOR +
                Configs.getConfig(ConfigConstants.SELENIUM_SERVER_IP) +
                COLON_SEPARATOR +
                HTTP_PORT_8181 +
                PATH_SEPARATOR +
                VIDEO +
                PATH_SEPARATOR +
                sessionId +
                DOT_SEPARATOR +
                MP4;
        log.info("VIDEO - " + url);
    }

    @Step("Take screenshot")
    private void takeScreenshot(Scenario scenario) {
        if (driver instanceof TakesScreenshot) {
            scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png", "Screenshot");
        }
    }
}
