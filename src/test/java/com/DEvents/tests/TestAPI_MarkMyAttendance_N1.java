package com.DEvents.tests;


import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.DEvents.coreFramework.DBConnectivity;
import com.DEvents.coreFramework.TestBase;
import com.DEvents.coreFramework.dataHandler;
import com.DEvents.tests.Config.TestDataProviderClass;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.module.jsv.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * This validates the API that checks if Invalid QR Code error is displayed for incorrect Event ID - Negative Scenario
 * @author shmurali
 * @since 1 Dec 2019
 */
public class TestAPI_MarkMyAttendance_N1 extends TestBase{
	
	public static RequestSpecification httpRequest;
	public static Response response;
	
	String sheetName = "MarkMyAttendance_N1";	//Change as per API being Tested

	//List of data input needed for tests
	String pathQuery="";
	String statusCode="";
	String cntntType="";
	String cntntEncode="";
	int respTime=5;
	String jsonSchemaFile="";
	String expObjects="";
	String sqlQuery="";
	String sqlParms=null;

	
	/**Test Description :: To make the API Request and capture the Response by calling data from Data Provider class **/
	@Test (priority=0,dataProvider="MarkMyAttendanceN1_Input", dataProviderClass=TestDataProviderClass.class,description="API Requesting & Capturing Response")
	public void getResponse(String Query, String stsCode, String contentType, String contentEncode, 
			String responseTime, String jsonSchema, String expctCol, String sqlQ, String parm) throws IOException, InterruptedException{

		pathQuery = Query;
		statusCode=stsCode;
		cntntType=contentType;
		cntntEncode=contentEncode;
		respTime = Integer.parseInt(responseTime);
		jsonSchemaFile=jsonSchema;
		expObjects=expctCol;
		sqlQuery=sqlQ;
		sqlParms=parm;

		//Calling API for capturing response
		RestAssured.baseURI = properties.getProperty("BaseURL");
		httpRequest = RestAssured.given();
		httpRequest.header("Ocp-Apim-Subscription-Key", properties.getProperty("OCM_SubscriptionKey"));
		
		//To dynamically pass the eventID created during Data Injection
		pathQuery=pathQuery.replaceAll("#", String.valueOf(Integer.parseInt(DB_eventID) - 2));
		
		response = httpRequest.request(Method.PUT,pathQuery);
		TimeUnit.SECONDS.sleep(3);
	}

	@Test (priority=1,description="Validating Status Code")
	void checkStatusCode() throws IOException {

		int actualstatusCode = response.getStatusCode();
		report.log(LogStatus.INFO, "Actual Status Code: "+actualstatusCode);
		report.log(LogStatus.INFO, "Expected Status Code: "+statusCode);
		Assert.assertEquals(actualstatusCode, Integer.parseInt(statusCode));
	}

	@Test (priority=2,description="Validating Content-Type")
	void checkContentType() {

		String actualContentType = response.header("Content-Type");
		report.log(LogStatus.INFO, "Actual Content-Type: "+actualContentType);
		report.log(LogStatus.INFO, "Expected Content-Type: "+cntntType);
		Assert.assertEquals(actualContentType, cntntType);
	}

	@Test (priority=3,description="Validating Content Encoding")
	void checkContentEncoding() {

		String actualContentEncoding = response.header("Content-Encoding");
		report.log(LogStatus.INFO, "Actual Content-Encoding: "+actualContentEncoding);
		report.log(LogStatus.INFO, "Expected Content-Encoding: "+cntntEncode);
		Assert.assertEquals(actualContentEncoding, cntntEncode);
	}


	@Test (priority=4,dependsOnMethods = "checkStatusCode", description="Validating Response time to be less than 5sec")
	void checkResponseTime() {

		Long responseTime = response.getTime();
		report.log(LogStatus.INFO, "Actual Response Time: "+responseTime+" milliseconds");
		Assert.assertTrue(responseTime< (respTime * 1000), "Response takes more than "+respTime+" seconds");
	}

	@Test (priority=5,dependsOnMethods = "checkStatusCode", description="Validating JSON-Schema of Response")
	void validateJSONSchema() {
		report.log(LogStatus.INFO, "JSON Schema file used: "+jsonSchemaFile);
		response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(jsonSchemaFile));
	}
	
	//(enabled = true,dependsOnMethods = {"checkStatusCode","validateJSONSchema"}, description="Validating Response body data against DB")
	@Test (priority=6,enabled = true, description="Validating Response body data against DB")
	void checkResponseBody() throws ClassNotFoundException, IOException, SQLException {

		String responseBody = response.getBody().asString();
		report.log(LogStatus.INFO, "Response Body : "+responseBody);

		//Storing JSON data into Excel file
		dataHandler.json2xlsx(responseBody, sheetName);

		//Storing DB result into Excel file
		DBConnectivity.dbResultSet2xlsx(DBConnectivity.getSQLQuery(sqlQuery,String.valueOf(Integer.parseInt(DB_eventID) - 2)), sheetName, expObjects);

		//Comparing the JSON data vs. DB datad 
		Assert.assertTrue(dataHandler.compareOutputSheets(sheetName), "Found mismatch between Json output and Database results");

	}
}