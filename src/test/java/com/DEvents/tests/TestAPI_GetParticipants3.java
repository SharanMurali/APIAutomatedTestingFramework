package com.DEvents.tests;


import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.DEvents.coreFramework.TestBase;

import io.restassured.RestAssured;
import io.restassured.http.Method;

/**
 * This API fetches all the Participant “User Ids” 
 * @author shmurali
 *
 */
public class TestAPI_GetParticipants3 extends TestBase{
	
	@BeforeClass
	void getWeather() throws InterruptedException {
		
		RestAssured.baseURI = "https://devents.azure-api.net/Event";
		httpRequest = RestAssured.given();
		
		httpRequest.header("Ocp-Apim-Subscription-Key", properties.getProperty("OCM_SubscriptionKey"));
		
		response = httpRequest.request(Method.POST,"/GetParticipants");
		Thread.sleep(3000);
	}
	
	@Test
	void checkStatusCode() {
		
		int statusCode = response.getStatusCode();
		System.out.println("Status Code: "+statusCode);
		Assert.assertEquals(statusCode, 200);
	}
	
	@Test
	void checkContentType() {
		
		String contentType = response.header("Content-Type");
		//System.out.println("Content-Type: "+contentType);
		Assert.assertEquals(contentType, "application/json; charset=utf-8");
	}
	
	@Test
	void checkContentEncoding() {
		
		String contentEncoding = response.header("Content-Encoding");
		//System.out.println("Content-Encoding: "+contentEncoding);
		Assert.assertEquals(contentEncoding, "gzip");
	}
	
	
	@Test (dependsOnMethods = "checkStatusCode")
	void checkResponseTime() {
		
		Long responseTime = response.getTime();
		//System.out.println("Response Time : " + responseTime +" milliseconds");
		Assert.assertTrue(responseTime<3000, "Response takes more than 3 seconds");
	}
	
	@Test (dependsOnMethods = "checkStatusCode")
	void checkResponseBody() {
		
		String responseBody = response.getBody().asString();
		System.out.println("Body : " + responseBody);
		//Assert.assertEquals(statusCode, 200);
	}

}
