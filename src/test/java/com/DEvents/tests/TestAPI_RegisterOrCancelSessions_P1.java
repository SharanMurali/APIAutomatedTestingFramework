package com.DEvents.tests;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.DEvents.coreFramework.DBConnectivity;
import com.DEvents.coreFramework.TestBase;
import com.DEvents.utils.XLUtils;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * This validates the API that registers an attendee for a session
 * @author shmurali
 * @since 4 Dec 2019
 */
public class TestAPI_RegisterOrCancelSessions_P1 extends TestBase{
	
	public static RequestSpecification httpRequest;
	public static Response response;
	
	String sheetName = "RegisterOrCancelSessions_P1";	//Change as per API being Tested

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



	@BeforeTest (description="To prepare the API Request and capture the Response by sourcing data from Excel")
	void getResponse() throws IOException, InterruptedException{

		//List data needed for validations
		pathQuery=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"pathQuery")).trim();
		statusCode=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Status Code")).trim();
		//cntntType=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Content-Type")).trim();
		//cntntEncode=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Content Encoding")).trim();
		respTime=Integer.parseInt(XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Expected Max. Response Time (sec)")));
		//jsonSchemaFile=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Schema File")).trim();
		//expObjects=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Expected Data")).trim();
		sqlQuery=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"SQL Query")).trim();
		sqlParms=XLUtils.getCellData(filepath,sheetName,1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,"Parameters")).trim();

		//Calling API for capturing response
		RestAssured.baseURI = properties.getProperty("BaseURL");
		httpRequest = RestAssured.given();
		httpRequest.header("Ocp-Apim-Subscription-Key", properties.getProperty("OCM_SubscriptionKey"));
		
		//To dynamically pass the eventID created during Data Injection
		pathQuery=pathQuery.replaceAll("e#", DB_eventID).replaceAll("k#", DB_kioskID);
		System.out.println(pathQuery);
		response = httpRequest.request(Method.PUT,pathQuery);
		TimeUnit.SECONDS.sleep(3);
	}

	@Test (description="Validating Status Code")
	void checkStatusCode() throws IOException {

		int actualstatusCode = response.getStatusCode();
		report.log(LogStatus.INFO, "Actual Status Code: "+actualstatusCode);
		report.log(LogStatus.INFO, "Expected Status Code: "+statusCode);
		Assert.assertEquals(actualstatusCode, Integer.parseInt(statusCode));
	}

	@Test (dependsOnMethods = "checkStatusCode", description="Validating Response time to be less than 5sec")
	void checkResponseTime() {

		Long responseTime = response.getTime();
		report.log(LogStatus.INFO, "Actual Response Time: "+responseTime+" milliseconds");
		Assert.assertTrue(responseTime< (respTime * 1000), "Response takes more than "+respTime+" seconds");
	}
	
	//(enabled = true,dependsOnMethods = {"checkStatusCode","validateJSONSchema"}, description="Validating Response body data against DB")
	@Test (enabled = true,dependsOnMethods = "checkStatusCode", description="Validating Registration success against DB")
	void checkRegistrationInDB() throws ClassNotFoundException, IOException {
		
		String[][] str=DBConnectivity.getResultFromDB(DBConnectivity.getSQLQuery(sqlQuery,sqlParms));
		
		if(!str[0][0].isEmpty() && str[0][0].equals("APITest@deloitte.com")) {
			report.log(LogStatus.PASS, "Registration successful - Found record of Attendee: "+str[0][0]+" in DB table");
		}
		else {
			report.log(LogStatus.FAIL, "Registration FAILED");
		}
		
	}
}