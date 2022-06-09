package com.core.utils;

import com.core.constants.ConfigConstants;
import org.testng.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configs {
    public static String getConfig(String conf){
        try (InputStream input = Configs.class.getClassLoader().getResourceAsStream(ConfigConstants.CONFIG_PROPERTIES)) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);

            // get the property value and print it out
            return prop.getProperty(conf);

        } catch (IOException ex) {
            Assert.fail("Can not read config" + ex);
        }
        return "";
    }
}
