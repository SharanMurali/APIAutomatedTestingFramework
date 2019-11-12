package com.DEvents.tests;


import java.io.IOException;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.DEvents.coreFramework.TestBase;
import com.DEvents.utils.XLUtils;

import io.restassured.RestAssured;
import io.restassured.http.Method;

/**
 * This API fetches all the Participant “User Ids” 
 * @author shmurali
 *
 */
public class TestAPI_GetParticipants4 extends TestBase{

//	@BeforeClass(dataProvider="")
//	void getWeather() throws InterruptedException {
//
//		RestAssured.baseURI = "https://devents.azure-api.net/Event/GetParticipants";
//		httpRequest = RestAssured.given();
//
//		httpRequest.header("Ocp-Apim-Subscription-Key", properties.getProperty("OCM_SubscriptionKey"));
//
//		response = httpRequest.request(Method.POST,"");
//		Thread.sleep(3000);
//	}
	
	@Test(dataProvider="API_DataProvider")
	void getResponse(String URL, String Statuscode, String CType, String CEncode) throws InterruptedException {

		RestAssured.baseURI = URL;
		httpRequest = RestAssured.given();

		httpRequest.header("Ocp-Apim-Subscription-Key", properties.getProperty("OCM_SubscriptionKey"));

		response = httpRequest.request(Method.POST,"");
		Thread.sleep(3000);
	}

	@Test (dependsOnMethods = "getResponse",dataProvider="API_DataProvider")
	void checkStatusCode(String URL, String Statuscode, String CType, String CEncode) throws IOException {

		int statusCode = response.getStatusCode();
		System.out.println("Status Code: "+statusCode);
		System.out.println("sourced Status Code: "+Statuscode);
		Assert.assertEquals(statusCode, Integer.parseInt(Statuscode));
	}

	@Test (dependsOnMethods = "getResponse")
	void checkContentType() {

		String contentType = response.header("Content-Type");
		//System.out.println("Content-Type: "+contentType);
		Assert.assertEquals(contentType, "application/json; charset=utf-8");
	}

	@Test (dependsOnMethods = "getResponse")
	void checkContentEncoding() {

		String contentEncoding = response.header("Content-Encoding");
		//System.out.println("Content-Encoding: "+contentEncoding);
		Assert.assertEquals(contentEncoding, "gzip");
	}


	@Test (dependsOnMethods = "checkStatusCode")
	void checkResponseTime() {

		Long responseTime = response.getTime();
		System.out.println("Response Time : " + responseTime +" milliseconds");
		Assert.assertTrue(responseTime<5000, "Response takes more than 3 seconds");
	}

	@Test (dependsOnMethods = "checkStatusCode")
	void checkResponseBody() {

		String responseBody = response.getBody().asString();
		System.out.println("Body : " + responseBody);
		//Assert.assertEquals(statusCode, 200);
	}



	@DataProvider(name="API_DataProvider")
	String [][] getAPIData () throws IOException {

		//Read data from Excel
		String filepath = System.getProperty("user.dir") + "/" + "src/test/java/com/DEvents/tests/Config/APITestControl.xlsx";
		String sheetName = "GetParticipants";

		int rowCount = XLUtils.getRowCount(filepath,sheetName);
		String columnList[]= {"URL","Status Code","Content-Type","Content Encoding"};

		String inputData[][] = new String[rowCount][columnList.length]; 

		for (int i=0;i<rowCount;i++) {
			for (int j=0;j<columnList.length;j++) {
				inputData[i][j]=XLUtils.getCellData(filepath,sheetName,i+1,XLUtils.getColumnIndexbyHeader(filepath,sheetName,columnList[j])).trim();
			}
		}

		System.out.println("Input Dataset: "+Arrays.toString(inputData));

		return inputData;
	}

}
