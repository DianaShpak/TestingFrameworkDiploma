package com.core.pages;
import com.core.constants.ConfigConstants;
import com.core.utils.Configs;
import com.epam.reportportal.annotations.Step;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    @FindBy(how = How.CLASS_NAME, using = "image-overlay")
    private WebElement book;
    @FindBy(how = How.XPATH, using = "//*[@id=\"header\"]/div[2]/div[3]/div[2]/div[1]/div[2]/div/nav/div[3]")
    private WebElement etcMenu;
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
        boolean isPresent = img.isDisplayed();
        log.info("Header element - " + isPresent);
        Assert.assertTrue(isPresent);
    }
    @Step("Check present book element")
    public void checkPresentBookElement(){
        boolean isPresent = book.isDisplayed();
        log.info("Present book element - " + isPresent);
        Assert.assertTrue(isPresent);
    }
    @Step("Open etc menu")
    public void openEtcMenu() {
        Actions actions = new Actions(driver);
        actions.moveToElement(etcMenu).perform();
        sleep(10000);
    }
    @Step("Scroll")
    public void scroll() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement Element = driver.findElement(By.xpath("//*[@id=\"block-yui_3_17_2_1_1630548711199_5495\"]"));
        js.executeScript("arguments[0].scrollIntoView();", Element);
        sleep(15000);
    }
    private void sleep(Integer ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}