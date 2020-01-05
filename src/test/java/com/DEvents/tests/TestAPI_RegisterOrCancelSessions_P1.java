package com.DEvents.tests;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.DEvents.coreFramework.DBConnectivity;
import com.DEvents.coreFramework.TestBase;
import com.DEvents.tests.Config.TestDataProviderClass;
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
	int respTime=5;
	String sqlQuery="";
	String sqlParms=null;
	

	/**Test Description :: To make the API Request and capture the Response by calling data from Data Provider class **/
	@Test (priority=0,dataProvider="RegisterOrCancelSessionsP1_Input", dataProviderClass=TestDataProviderClass.class,description="API Requesting & Capturing Response")
	public void getResponse(String Query, String stsCode, String responseTime, String sqlQ, String parm) throws IOException, InterruptedException{

		pathQuery = Query;
		statusCode=stsCode;
		respTime = Integer.parseInt(responseTime);
		sqlQuery=sqlQ;
		sqlParms=parm;

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

	@Test (priority=1,description="Validating Status Code")
	void checkStatusCode() throws IOException {

		int actualstatusCode = response.getStatusCode();
		report.log(LogStatus.INFO, "Actual Status Code: "+actualstatusCode);
		report.log(LogStatus.INFO, "Expected Status Code: "+statusCode);
		Assert.assertEquals(actualstatusCode, Integer.parseInt(statusCode));
	}

	@Test (priority=2,dependsOnMethods = "checkStatusCode", description="Validating Response time to be less than 5sec")
	void checkResponseTime() {

		Long responseTime = response.getTime();
		report.log(LogStatus.INFO, "Actual Response Time: "+responseTime+" milliseconds");
		Assert.assertTrue(responseTime< (respTime * 1000), "Response takes more than "+respTime+" seconds");
	}
	
	@Test (priority=3,enabled = true,dependsOnMethods = "checkStatusCode", description="Validating Registration success against DB")
	void checkRegistrationInDB() throws ClassNotFoundException, IOException {
		
		String[][] str=DBConnectivity.getResultFromDB(DBConnectivity.getSQLQuery(sqlQuery,sqlParms));
		
		if(!str[0][0].isEmpty() && str[0][0].equals(sqlParms)) {
			report.log(LogStatus.PASS, "Registration successful - Found record of Attendee: "+str[0][0]+" in DB table");
		}
		else {
			report.log(LogStatus.FAIL, "Registration FAILED");
		}
		
	}
}