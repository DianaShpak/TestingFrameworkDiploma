/*
 * Copyright 2022 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.core.method;

import com.core.constants.ConfigConstants;
import com.core.servicesApiImplementation.methods.MethodsClient;
import com.core.servicesApiImplementation.methods.response.*;
import com.core.setup.ManagerClients;
import com.core.utils.AssertUtils;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class HttpMethodsTest {
	private MethodsClient methodsClient;

	@Before
	public void beforeScenario() {
		ManagerClients managerClients = new ManagerClients();
		methodsClient = managerClients.getMethodsClient();
	}

	@Given("Test get")
	public void get() {
		GetResponse getResponse = methodsClient.get();
		Assert.assertEquals(getResponse.getOrigin(), "46.188.179.8", "Check origin");
	}

	@Then("Test patch")
	public void patch() {
		PatchResponse getResponse = methodsClient.patch();
		Assert.assertEquals(getResponse.getOrigin(), "46.188.179.8", "Check origin");
	}

	@Then("Test delete")
	public void delete() {
		DeleteResponse getResponse = methodsClient.delete();
		Assert.assertEquals(getResponse.getOrigin(), "46.188.179.8", "Check origin");
	}

	@Then("Test post")
	public void post() {
		PostResponse getResponse = methodsClient.post();
		Assert.assertEquals(getResponse.getOrigin(), "46.188.179.8", "Check origin");
	}

	@Then("Test put")
	public void put() {
		PutResponse getResponse = methodsClient.put();
		Assert.assertEquals(getResponse.getOrigin(), "46.188.179.8", "Check origin");
	}

	@Then("Test post negative")
	public void postNegative() {
		PostResponse getResponse = methodsClient.post();
		Assert.assertEquals(getResponse.getOrigin(), "46.188.179.8X", "Check origin");
	}

	@Then("Test put negative")
	public void putNegative() {
		PutResponse getResponse = methodsClient.put();
		Assert.assertEquals(getResponse.getOrigin(), "46.188.179.8X", "Check origin");
	}

}
