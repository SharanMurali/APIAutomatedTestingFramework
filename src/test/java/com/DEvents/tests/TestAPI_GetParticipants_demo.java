package com.DEvents.tests;


import java.io.IOException;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.DEvents.coreFramework.DBConnectivity;
import com.DEvents.coreFramework.TestBase;
import com.DEvents.coreFramework.dataHandler;
import com.DEvents.utils.XLUtils;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.module.jsv.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * This validates the API that fetches all the Participants “User ID” 
 * @author shmurali
 * @since 17 Nov 2019
 */
public class TestAPI_GetParticipants_demo extends TestBase{
	
	public static RequestSpecification httpRequest;
	public static Response response;

	//List of data input needed for tests
	String pathQuery="";
	String statusCode="";
	String cntntType="";
	String cntntEncode="";
	String jsonSchemaFile="";
	String expObjects="";
	String sqlQuery="";
	String sqlParms=null;



	@BeforeTest (description="To make the API Request and capture the Response by sourcing data from Excel")
	void getResponse() throws IOException, InterruptedException{

		//Read data from Excel
		String sheetName = "GetParticipants";	//Change as per API being Tested
		String filepath = System.getProperty("user.dir") + "/" + "src/test/java/com/DEvents/tests/Config/APITestControl.xlsx"; // Common for all APIs

		//List data needed for validations
		pathQuery=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"pathQuery")).trim();
		statusCode=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Status Code")).trim();
		cntntType=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Content-Type")).trim();
		cntntEncode=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Content Encoding")).trim();
		jsonSchemaFile=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Schema File")).trim();
		expObjects=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Expected Data")).trim();
		sqlQuery=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"SQL Query")).trim();
		sqlParms=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Parameters")).trim();

		//Calling API for capturing response
		RestAssured.baseURI = properties.getProperty("BaseURL");
		httpRequest = RestAssured.given();
		httpRequest.header("Ocp-Apim-Subscription-Key", properties.getProperty("OCM_SubscriptionKey"));

		response = httpRequest.request(Method.POST,pathQuery);
		Thread.sleep(3000);
	}

	@Test (description="Validating Status Code")
	void checkStatusCode() throws IOException {

		int actualstatusCode = response.getStatusCode();
		report.log(LogStatus.INFO, "Actual Status Code: "+actualstatusCode);
		report.log(LogStatus.INFO, "Expected Status Code: "+statusCode);
		Assert.assertEquals(actualstatusCode, Integer.parseInt(statusCode));
	}

	@Test (description="Validating Content-Type")
	void checkContentType() {

		String actualContentType = response.header("Content-Type");
		report.log(LogStatus.INFO, "Actual Content-Type: "+actualContentType);
		report.log(LogStatus.INFO, "Expected Content-Type: "+cntntType);
		Assert.assertEquals(actualContentType, cntntType);
	}

	@Test (description="Validating Content Encoding")
	void checkContentEncoding() {

		String actualContentEncoding = response.header("Content-Encoding");
		report.log(LogStatus.INFO, "Actual Content-Encoding: "+actualContentEncoding);
		report.log(LogStatus.INFO, "Expected Content-Encoding: "+cntntEncode);
		Assert.assertEquals(actualContentEncoding, cntntEncode);
	}


	@Test (dependsOnMethods = "checkStatusCode", description="Validating Response time to be less than 5sec")
	void checkResponseTime() {

		Long responseTime = response.getTime();
		System.out.println("Response Time : " + responseTime +" milliseconds");
		report.log(LogStatus.INFO, "Actual Response Time: "+responseTime+" milliseconds");
		Assert.assertTrue(responseTime<5000, "Response takes more than 5 seconds");
	}

	@Test (dependsOnMethods = "checkStatusCode", description="Validating JSON-Schema of Response")
	void validateJSONSchema() {
		report.log(LogStatus.INFO, "JSON Schema file used: "+jsonSchemaFile);
		response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchema/"+jsonSchemaFile));
	}

	@Test (enabled = true,dependsOnMethods = {"checkStatusCode","validateJSONSchema"}, description="Validating Response body data against DB")
	void checkResponseBody() throws ClassNotFoundException, IOException, SQLException {

		String responseBody = response.getBody().asString();
		report.log(LogStatus.INFO, "Response Body : "+responseBody);

		//Storing JSON data into Excel file
		dataHandler.json2xlsx(responseBody, "GetParticipants");

		//Storing DB result into Excel file
		DBConnectivity.dbResultSet2xlsx(DBConnectivity.getSQLQuery(sqlQuery,sqlParms), "GetParticipants", expObjects);

		Assert.assertTrue(dataHandler.compareOutputSheets("GetParticipants"), "Found mismatch between Json output and Database results");

	}
}