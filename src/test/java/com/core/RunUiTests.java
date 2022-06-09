package com.core;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = {"pretty", "com.epam.reportportal.cucumber.ScenarioReporter"},
		features = "src/test/resources/features/selenium",
		tags = "not @ignore",
		glue = "com.core.ui"
)
public class RunUiTests {
}
