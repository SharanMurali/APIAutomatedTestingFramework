package com.DEvents.coreFramework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.testng.ITestResult;
import org.testng.annotations.*;

import com.DEvents.utils.XLUtils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class TestBase {

	public static Properties properties=null;
	public static String configFile=null;
	public static String filepath=null;
	public static FileInputStream in;

	private static ExtentReports extent;
	private static ExtentTest parentClass;
	protected static ExtentTest report;
	private static String reportFileName = "API_AutomatedTest_Report"+".html";
	private static String fileSeperator = System.getProperty("file.separator");
	private static String reportFilepath = System.getProperty("user.dir") +fileSeperator+ "TestReport";
	private static String reportFileLocation =  reportFilepath +fileSeperator+ reportFileName;

	public static String DB_eventID="43";
	public static String DB_kioskID="23";

	@BeforeSuite
	public void setup() throws ClassNotFoundException, IOException {
		properties = new Properties();
		configFile = "src/test/java/com/DEvents/tests/Config/API_Config.properties";
		filepath = System.getProperty("user.dir") + "/" + "src/test/java/com/DEvents/tests/Config/APIDataMockupCollection.xlsx"; // Common for all APIs
		try {
			in = new FileInputStream(System.getProperty("user.dir") + "/" + configFile);
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
		
		/*Code for Data Mock-up in Database */
		if(properties.getProperty("Test_Data_Injection").equalsIgnoreCase("true")) {
			if(DBConnectivity.executeBatchQuery(dataHandler.querySetReader("D;I"))) {
				System.out.println("Data Mockup completed successfully");

				String[][] str = DBConnectivity.getResultFromDB(XLUtils.getCellData(filepath,"DataSourcing",1,XLUtils.getColumnIndexbyHeader(filepath,"DataSourcing","Query")).trim());
				DB_eventID=str[0][0];
				str = DBConnectivity.getResultFromDB(XLUtils.getCellData(filepath,"DataSourcing",2,XLUtils.getColumnIndexbyHeader(filepath,"DataSourcing","Query")).trim());
				DB_kioskID=str[0][0];


			}
			else {
				System.out.println("Data Mockup Failed");
			}
		}
		else if(properties.getProperty("Test_Data_Injection").equalsIgnoreCase("false")) {
			System.out.println("Data Mockup Ignored");
		}

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
	public void fullteardown() throws ClassNotFoundException, IOException {
		extent.flush();

		/*Code for Data cleanup in Database */
		if(DBConnectivity.executeBatchQuery(dataHandler.querySetReader("D"))) {
			System.out.println("Data Cleanup completed successfully");
		}else {
			System.out.println("Data Cleanup Failed");
		}
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
			File testReport = new File(reportFileLocation);
			if(testReport.exists()) {
				testReport.delete();
			}
		}
		return reportFileLocation;
	}

}
