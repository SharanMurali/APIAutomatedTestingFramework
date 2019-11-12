package com.DEvents.utils;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Listeners extends TestListenerAdapter{

	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest test;

	public void onStart(ITestContext testContext) {
		
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")+System.getProperty("file.separator")+"TestReport"+System.getProperty("file.separator")+"RunReport.html");
		htmlReporter.config().setDocumentTitle("Automation_Report");
		htmlReporter.config().setReportName("API_TestAutomation_Report");
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().enableTimeline(true);


		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		//Set environment details
		extent.setSystemInfo("Project Name", "D-Events Mobile App");
		extent.setSystemInfo("Environment", "API-RestAssured");
		extent.setSystemInfo("Author", "Sharan Murali");
	}
	
	public void onTestSuccess(ITestResult result) {
		test=extent.createTest(result.getName());
		test.log(Status.PASS, "Test Case-" + result.getName()+" PASSED");
		test.log(Status.PASS, "Test Case-" + result.getMethod().getDescription()+" PASSED");
	}
	
	public void onTestFailure(ITestResult result) {
		test=extent.createTest(result.getName());
		test.log(Status.FAIL, "Test Case-" + result.getName()+" FAILED");
		test.log(Status.FAIL, "Failure due to: " + result.getThrowable());
	}
	
	public void onTestSkipped(ITestResult result) {
		test=extent.createTest(result.getName());
		test.log(Status.SKIP, "Test Case-" + result.getName()+" SKIPPED");
		test.log(Status.SKIP, "Skipped due to: " + result.getSkipCausedBy());
	}
	
	public void onFinish(ITestContext testContext) {
		extent.flush();	
	}

}
