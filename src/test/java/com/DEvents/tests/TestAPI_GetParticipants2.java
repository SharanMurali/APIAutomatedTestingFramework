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
public class TestAPI_GetParticipants2 extends TestBase{
	
	@BeforeClass
	void getWeather() throws InterruptedException {
		
		RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
		httpRequest = RestAssured.given();
		response = httpRequest.request(Method.GET,"/Hyderabad");
		
		Thread.sleep(3000);
	}
	
	@Test
	void checkStatusCode() {
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
	}
	
	@Test
	void checkContentType() {
		
		String contentType = response.header("Content-Type");
		System.out.println("Content-Type: "+contentType);
		//Assert.assertEquals(statusCode, 200);
	}
	
	@Test
	void checkContentEncoding() {
		
		String contentEncoding = response.header("Content-Encoding");
		System.out.println("Content-Encoding: "+contentEncoding);
		//Assert.assertEquals(statusCode, 200);
	}
	
	@Test (dependsOnMethods = "checkStatusCode")
	void checkResponseBody() {
		
		String responseBody = response.getBody().asString();
		System.out.println("Body : " + responseBody);
		//Assert.assertEquals(statusCode, 200);
	}
	
	@Test (dependsOnMethods = "checkStatusCode")
	void checkResponseTime() {
		
		Long responseTime = response.getTime();
		System.out.println("Response Time : " + responseTime +" milliseconds");
		//Assert.assertEquals(statusCode, 200);
	}

}
