package com.core.utils;

import com.epam.reportportal.annotations.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

@Slf4j
public class AssertUtils {


    @Step("Check if {parameterName} equals {expectedValue}")
    public static void assertEqualsWithOkLog(String parameterName, Object actualValue, Object expectedValue, String errorMessage) {
        Assert.assertEquals(actualValue, expectedValue, errorMessage);
        log.info(String.format("Actual '%s' value matches to expected (%s)", parameterName, expectedValue));
    }

    @Step("Check if {parameterName} is not null")
    public static void assertNotNullWithOkLog(String parameterName, Object actualValue, String errorMessage) {
        Assert.assertNotNull(actualValue, errorMessage);
        log.info(String.format("'%s' value is not null", parameterName));
    }

    @Step("Check if {parameterName} is null")
    public static void assertNullWithOkLog(String parameterName, Object actualValue, String errorMessage) {
        Assert.assertNull(actualValue, errorMessage);
        log.info(String.format("'%s' value is null", parameterName));
    }

    @Step("Check if condition '{conditionName}' is true")
    public static void assertTrueWithOkLog(String conditionName, boolean actualValue, String errorMessage) {
        Assert.assertTrue(actualValue, errorMessage);
        log.info(String.format("'%s' condition is true", conditionName));
    }
}