package com.core.pages;

import com.core.constants.ConfigConstants;
import com.core.utils.Configs;
import com.epam.reportportal.annotations.Step;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import static com.core.constants.SymbolsConstants.COLON_SEPARATOR;
import static com.core.constants.SymbolsConstants.PROTOCOL_SEPARATOR;

@Slf4j
public class HomePage {
    private WebDriver driver;

    private static final String protocol = Configs.getConfig(ConfigConstants.HOME_PAGE_PROTOCOL);
    private static final String host = Configs.getConfig(ConfigConstants.HOME_PAGE_HOST);
    private static final String  port = Configs.getConfig(ConfigConstants.HOME_PAGE_PORT);

    private static String PAGE_URL= ""
            .concat(protocol)
            .concat(PROTOCOL_SEPARATOR)
            .concat(host);


    @FindBy(how = How.XPATH, using = "//*[@id=\"header\"]/div[2]/div[3]/div[2]/div[1]/div[1]/div/a/img")
    private WebElement img;

    @FindBy(how = How.XPATH, using = "//*[@id=\"yui_3_17_2_1_1654804723914_146\"]")
    private WebElement book;

    public HomePage(WebDriver driver){
        this.driver = driver;
        driver.get(PAGE_URL);
        PageFactory.initElements(driver, this);
    }

    @Step("Check page opened")
    public void isPageOpened(){
        String title = driver.getTitle();
        log.info("Title - " + title);
        Assert.assertTrue(title.contains("Kenneth Reitz"));
    }

    @Step("Check present header element")
    public void checkPresentHeaderelement(){
        boolean title = img.isDisplayed();
        log.info("Header element - " + title);
        Assert.assertTrue(title);
    }

    @Step("Check present book element")
    public void checkPresentBookElement(){
        boolean title = img.isDisplayed();
        log.info("Header element - " + title);
        Assert.assertTrue(title);
    }

}
