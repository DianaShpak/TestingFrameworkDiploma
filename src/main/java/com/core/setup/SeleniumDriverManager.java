package com.core.setup;
import com.core.constants.ConfigConstants;
import com.core.constants.HttpConstants;
import com.core.constants.SymbolsConstants;
import com.core.utils.Configs;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.core.constants.Browsers.*;
import static com.core.constants.SeleniumConstants.*;
@Slf4j
public class SeleniumDriverManager {
    public static WebDriver getWebDriver(String browser) {
        RemoteWebDriver driver = null;
        try {
            DesiredCapabilities capabilities = getDesiredCapabilities(browser);
            assert capabilities != null;
            driver = new RemoteWebDriver(new URL(
                    HttpConstants.HTTP_PROTOCOL +
                            SymbolsConstants.PROTOCOL_SEPARATOR +
                            Configs.getConfig(ConfigConstants.SELENIUM_SERVER_IP) +
                            REMOTE_WEB_DRIVER_PORT_AND_URN), capabilities);
            driver.manage().window().fullscreen();
            log.info(String.format("WebDriver %s has been successfully initiated", driver.getSessionId()));
        } catch (MalformedURLException e) {
            Assert.fail(e.getMessage());
        }
        return driver;
    }

    private static DesiredCapabilities getDesiredCapabilities(String browser) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        capabilities.setCapability(SESSION_TIME_OUT, "10m");
        capabilities.setCapability(ENABLE_LOG, true);
        capabilities.setCapability(SCREEN_RESOLUTION, Configs.getConfig(ConfigConstants.SCREEN_RESOLUTION));
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true,
                "args", new String[]{"--headless", "--disable-gpu", "--test-type", "--ignore-certificate-errors"}
        ));
        List<String> arg = new ArrayList<>();
        arg.add(IGNORE_CERTIFICATE_ERRORS);
        arg.add(START_MAXIMIZED);
        switch (browser) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(arg);
                capabilities.setCapability(BROWSER_VERSION, Configs.getConfig(ConfigConstants.BROWSER_VERSION_CHROME));
                capabilities.setCapability(BROWSER_NAME, CHROME);
                capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                return capabilities;
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments(arg);
                capabilities.setCapability(BROWSER_VERSION,  Configs.getConfig(ConfigConstants.BROWSER_VERSION_FIREFOX));
                capabilities.setCapability(BROWSER_NAME, FIREFOX);
                capabilities.setCapability(ChromeOptions.CAPABILITY, firefoxOptions);
                return capabilities;
            case OPERA:
                OperaOptions operaOptions = new OperaOptions();
                operaOptions.addArguments(arg);
                capabilities.setCapability(BROWSER_VERSION, Configs.getConfig(ConfigConstants.BROWSER_VERSION_OPERA));
                capabilities.setCapability(BROWSER_NAME, OPERA);
                return capabilities;
            default:
                Assert.fail("Choose another browser, There are no settings for this browser");
                return null;
        }
    }
}
