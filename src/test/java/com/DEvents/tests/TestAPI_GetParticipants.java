package com.DEvents.tests;


import java.io.IOException;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.DEvents.coreFramework.DBConnectivity;
import com.DEvents.coreFramework.TestBase;
import com.DEvents.coreFramework.jsonHandler;
import com.DEvents.utils.XLUtils;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.module.jsv.*;

/**
 * This API fetches all the Participant “User Ids” 
 * @author shmurali
 *
 */
public class TestAPI_GetParticipants extends TestBase{
	
	//List of data input needed for tests
	String url="";
	String statusCode="";
	String cntntType="";
	String cntntEncode="";
	String jsonSchemaFile="";
	String expObjects="";
	String sqlQuery="";
	String sqlParms="";



	@BeforeClass (description="To make the API Request and capture the Response by sourcing data from Excel")
	void getResponse() throws IOException, InterruptedException{
		
		//Read data from Excel
		String sheetName = "GetParticipants";	//Change as per Testing API
		String filepath = System.getProperty("user.dir") + "/" + "src/test/java/com/DEvents/tests/Config/APITestControl.xlsx"; // Common for all APIs
		
		//List data needed for validations
		url=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"URL")).trim();
		statusCode=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Status Code")).trim();
		cntntType=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Content-Type")).trim();
		cntntEncode=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Content Encoding")).trim();
		jsonSchemaFile=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Schema File")).trim();
		expObjects=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Expected Data")).trim();
		sqlQuery=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"SQL Query")).trim();
		sqlParms=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Parameters")).trim();
		
		//Calling API for capturing response
		RestAssured.baseURI = url;
		httpRequest = RestAssured.given();
		httpRequest.header("Ocp-Apim-Subscription-Key", properties.getProperty("OCM_SubscriptionKey"));

		response = httpRequest.request(Method.POST,"");
		Thread.sleep(3000);
	}

	@Test (description="Validate Status Code")
	void checkStatusCode() throws IOException {

		int actualstatusCode = response.getStatusCode();
		System.out.println("Status Code: "+actualstatusCode);
		System.out.println("sourced Status Code: "+statusCode);
		Assert.assertEquals(actualstatusCode, Integer.parseInt(statusCode));
	}

	@Test (description="Validate Content-Type")
	void checkContentType() {

		String contentType = response.header("Content-Type");
		//System.out.println("Content-Type: "+contentType);
		Assert.assertEquals(contentType, "application/json; charset=utf-8");
	}

	@Test (description="Validate Content Encoding")
	void checkContentEncoding() {

		String contentEncoding = response.header("Content-Encoding");
		//System.out.println("Content-Encoding: "+contentEncoding);
		Assert.assertEquals(contentEncoding, "gzip");
	}


	@Test (dependsOnMethods = "checkStatusCode", description="Validate Response time to be less than 5sec")
	void checkResponseTime() {

		Long responseTime = response.getTime();
		System.out.println("Response Time : " + responseTime +" milliseconds");
		Assert.assertTrue(responseTime<5000, "Response takes more than 5 seconds");
	}
	
	@Test (dependsOnMethods = "checkStatusCode", description="Validate JSON Schema of response")
	void validateJSONSchema() {
		
		response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchema/"+jsonSchemaFile));
	}
	
	@Test (dependsOnMethods = {"checkStatusCode","validateJSONSchema"}, description="Validate response body data against DB")
	void checkResponseBody() throws ClassNotFoundException, IOException, SQLException {

		String responseBody = response.getBody().asString();
		System.out.println("Body : " + responseBody);
		
		//Storing JSON data into Excel file
		jsonHandler.json2xlsx(responseBody, "GetParticipants");
		
		//Storing DB result into Excel file
		DBConnectivity.dbResultSet2xlsx(DBConnectivity.getSQLQuery(sqlQuery,sqlParms), "GetParticipants", expObjects);
		
		Assert.assertTrue(jsonHandler.compareOutputSheets("GetParticipants"), "Found mismatch between Json output and Database results");
		
	}
}




