package com.DEvents.coreFramework;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestBase {
	
	public static RequestSpecification httpRequest;
	public static Response response;
	
	public Logger logger;
	public static Properties properties=null;
	public static String filepath=null;
	public static FileInputStream in;
	
		@BeforeClass
		public void setup() {
		properties = new Properties();
		filepath = "src/test/java/com/DEvents/tests/Config/API_Config.properties";
		try {
			in = new FileInputStream(System.getProperty("user.dir") + "/" + filepath);
			properties.load(in);
			//System.out.println(System.getProperty("user.dir") + "/" + filepath);

		} catch (IOException e) {
			System.out.println("Cause is : "+e.getCause());
			System.out.println("Message is : "+e.getMessage());
			e.printStackTrace();
		}
		}
		
//		@BeforeClass
//		public void setUp() {
//			logger=Logger.getLogger("SampleTest");
//			PropertyConfigurator.configure("Log4j.properties");
//			logger.setLevel(Level.DEBUG);
//		}
		
	

}
