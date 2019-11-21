package com.DEvents.coreFramework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.testng.ITestResult;
import org.testng.annotations.*;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestBase {

	public static RequestSpecification httpRequest;
	public static Response response;

	public static Properties properties=null;
	public static String filepath=null;
	public static FileInputStream in;

	private static ExtentReports extent;
	private static ExtentTest parentClass;
	protected static ExtentTest report;
	private static String reportFileName = "API_AutomatedTest_Report"+".html";
	private static String fileSeperator = System.getProperty("file.separator");
	private static String reportFilepath = System.getProperty("user.dir") +fileSeperator+ "TestReport";
	private static String reportFileLocation =  reportFilepath +fileSeperator+ reportFileName;
	
	@BeforeSuite
	public void setup() {
		properties = new Properties();
		filepath = "src/test/java/com/DEvents/tests/Config/API_Config.properties";
		try {
			in = new FileInputStream(System.getProperty("user.dir") + "/" + filepath);
			properties.load(in);

		} catch (IOException e) {
			System.out.println("Cause is : "+e.getCause());
			System.out.println("Message is : "+e.getMessage());
			e.printStackTrace();
		}

		/*Code for generating extent report during runtime*/
		extent = new ExtentReports (getReportPath(reportFilepath));
		//Set environment details
		extent
			.addSystemInfo("Project Name", "D-Events Mobile App")
			.addSystemInfo("Environment", "API-RestAssured")
			.addSystemInfo("Author", "Sharan Murali");
		//loading the external xml file (i.e., extent-config.xml) which was placed under the base directory
		//You could find the xml file below. Create xml file in your project and copy past the code mentioned below
		extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));

	}
	
	@BeforeClass
	public void testSetup() {
		parentClass=extent.startTest(this.getClass().getSimpleName());
	}
	
	@BeforeMethod
	public void methodSetup(Method method) {
		report=extent.startTest(method.getAnnotation(Test.class).description());
		parentClass.appendChild(report);
	}
	
	@AfterMethod
	public void checkResults(ITestResult result) {
		
		if(result.getStatus()==ITestResult.FAILURE) {
			report.log(LogStatus.FAIL, "Test Method Failed: "+result.getMethod().getMethodName()+"<br>" +
					"FAILED DUE TO EXCEPTION: "+result.getThrowable());
		}
		else if (result.getStatus()==ITestResult.SKIP) {
			report.log(LogStatus.SKIP, "Test Skipped"+"<br>"+
					"Skipped due to: "+ result.getSkipCausedBy());
		}
		else if (result.getStatus()==ITestResult.SUCCESS) {
			report.log(LogStatus.PASS, "Test Passed");
			
		}
		
	}
	
	@AfterClass
	public void teardown() {
		parentClass.getRunStatus();
		extent.endTest(parentClass);
	}
	
	@AfterSuite
	public void fullteardown() {
		extent.flush();
	}

	//Create the report path
	private static String getReportPath (String path) {
		File testDirectory = new File(path);
		if (!testDirectory.exists()) {
			if (testDirectory.mkdir()) {
				System.out.println("Directory: " + path + " is created!" );
				return reportFileLocation;
			} else {
				System.out.println("Failed to create directory: " + path);
				return System.getProperty("user.dir");
			}
		} else {
			System.out.println("Directory already exists: " + path);
		}
		return reportFileLocation;
	}

}
