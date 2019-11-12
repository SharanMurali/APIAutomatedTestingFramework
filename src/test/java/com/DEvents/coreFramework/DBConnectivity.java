package com.DEvents.coreFramework;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DBConnectivity {
	public static Properties properties=null;
	public static String filepath=null;
	public static FileInputStream in;
	
	public static FileInputStream fi;
	public static FileOutputStream fo;
	public static XSSFWorkbook wb;
	public static XSSFSheet ws;
	public static XSSFRow row;
	public static XSSFCell cell;


	/**
	 * Method to hit a query in MySQL DB and retrieve ResultSet
	 * @author SharanMurali
	 * @since Nov 08 2019
	 * @param Query String
	 * @return ResultSet
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static ResultSet dbResultSet2xlsx(String sQuery, String xlOutputfilename, String columnHeaders) throws ClassNotFoundException, IOException{

		Statement stmt = null;
		ResultSet rset = null;

		readDBPropertyfile();

		String url = "jdbc:"+properties.getProperty("dbSystem")+"://"+properties.getProperty("dbServerName")+":"+properties.getProperty("dbPort")+"/"+properties.getProperty("dbSchema")+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=IST";

		System.out.println("Connection String URL- "+url);

		/* Query statement selecting all columns of the table. Using this query,
		/*  result set column metadata can be retrieved.  */
		try
		{
			System.out.println("\n MySQL Connectivity in progress: \n");

			/* Loading the MySQL JDBC driver  */
			Class.forName("com.mysql.cj.jdbc.Driver");

			/* Attempting to connect to MySQL */
			System.out.println(" Attempting to connect to MySQL via" +
					" the JDBC driver...");

			/* Creating a connection object */
			Connection con = DriverManager.getConnection(url,properties.getProperty("dbUserName"),properties.getProperty("dbPassword") );
			System.out.println(" Connection to MySQL established. \n");

			try
			{
				String schemaName=properties.getProperty("dbSchema");
				if (schemaName != null && schemaName.length() != 0) {

					try {
						/* Creating a statement object from an active connection schema*/
						stmt = con.createStatement();
						stmt.executeUpdate("USE " + schemaName);
						System.out.println("The schema is set successfully.");
					} catch (SQLException exception) {
						System.out.println(exception.getMessage());
						exception.printStackTrace();
					}    

				}
				System.out.println(" Statement object created. \n");

				try
				{
					rset = stmt.executeQuery(sQuery);
					
					fi = new FileInputStream(System.getProperty("user.dir")+"/src/test/java/com/DEvents/tests/jsonResults/"+xlOutputfilename+".xlsx");
					wb = new XSSFWorkbook(fi);
					ws = wb.createSheet("DB_Data");

					//Writes column header in the first row of the spreadsheet
					String colHeader[] =columnHeaders.split(";");
					row = ws.createRow(0);
					int i=0;

					for(String col:colHeader) {
						cell = row.createCell(i);
						cell.setCellValue(col);
						i++;
					}

					int j=1;
					while(rset.next()) {
						row = ws.createRow(j);
						i=0;
						for(String col:colHeader) {
							cell = row.createCell(i);
							cell.setCellValue(rset.getString(col));
							i++;
						}
						j++;
					}
					
					fo = new FileOutputStream(System.getProperty("user.dir")+"/src/test/java/com/DEvents/tests/jsonResults/"+xlOutputfilename+".xlsx");
					wb.write(fo);
					wb.close();
					fi.close();
					fo.close();
					System.out.println("database results written successfully into excel");
					
				}
				finally
				{
					/* Close the statement */
					stmt.close();
					System.out.println("\n Statement object closed. \n");
				}
			}
			finally
			{
				/* Close the connection */
				System.out.println(" Closing connection to MySQL...");
				con.close();
				System.out.println(" Connection to MySQL closed. \n");
			}

			System.out.println(" MySQL DB Connectivity finished. \n");
		}
		catch (SQLException ex)
		{
			/* If a SQLException was generated.  Catch it and display
             the error information. Note that there could be multiple
             error objects chained together. */
			System.out.println("*** SQLException caught ***");

			while (ex != null)
			{
				System.out.println(" Error code: " + ex.getErrorCode());
				System.out.println(" SQL State: " + ex.getSQLState());
				System.out.println(" Message: " + ex.getMessage());
				ex.printStackTrace();
				ex = ex.getNextException();
			}

			throw new IllegalStateException ("DB hit failed.") ;
		}


		return rset;
	}
	
	
	/**
	 * Method to hit a query in MySQL DB and retrieve data in String 2D Array
	 * @author SharanMurali
	 * @since Oct 11 2019
	 * @param sQuery
	 * @return String[][]
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String[][] getResultFromDB(String sQuery) throws ClassNotFoundException, IOException{

		Statement stmt = null;
		ResultSet rset = null;
		String[][] tablematrix = null;

		readDBPropertyfile();


		/* Creation of URL to be passed to the JDBC driver */
		//String url = "jdbc:"+properties.getProperty("dbSystem")+"://"+properties.getProperty("dbServerName")+":"+properties.getProperty("dbPort")+"/"+properties.getProperty("dbSchema");
		//				+"?verifyServerCertificate=false"+
		//				"&useSSL=false"+
		//				"&requireSSL=false"; 
		String url = "jdbc:"+properties.getProperty("dbSystem")+"://"+properties.getProperty("dbServerName")+":"+properties.getProperty("dbPort")+"/"+properties.getProperty("dbSchema")+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=IST";

		System.out.println("Connection String URL- "+url);

		/* Query statement selecting all columns of the table. Using this query,  */
		/* result set column metadata can be retrieved.  */
		try
		{
			System.out.println("\n MySQL Connectivity in progress: \n");

			/* Loading the MySQL JDBC driver  */
			Class.forName("com.mysql.cj.jdbc.Driver");
			//System.out.println(" JDBC driver loaded. \n");

			/* Attempting to connect to MySQL */
			System.out.println(" Attempting to connect to MySQL via" +
					" the JDBC driver...");

			/* Creating a connection object */
			//System.out.println(" User-" + properties.getProperty("dbUserName") + " connected.");
			Connection con = DriverManager.getConnection(url,properties.getProperty("dbUserName"),properties.getProperty("dbPassword") );
			System.out.println(" Connection to MySQL established. \n");

			try
			{
				String schemaName=properties.getProperty("dbSchema");
				if (schemaName != null && schemaName.length() != 0) {

					try {
						/* Creating a statement object from an active connection schema*/
						stmt = con.createStatement();
						stmt.executeUpdate("USE " + schemaName);
						System.out.println("The schema is set successfully.");
					} catch (SQLException exception) {
						System.out.println(exception.getMessage());
						exception.printStackTrace();
					}    

				}
				System.out.println(" Statement object created. \n");

				try
				{
					rset = stmt.executeQuery(sQuery);
					ResultSetMetaData rsmd = rset.getMetaData(); /* Retrieve the properties of the ResultSet object. A ResultSetMetaData object can be used to determine the types and properties of the columns in a ResultSet. */

					/* Retrieve the number of columns and rows returned */
					int rowCount = 0;
					rowCount = getRecordCountFromDB(sQuery);

					int colCount = rsmd.getColumnCount();
					tablematrix = new String[rowCount][colCount];

					//System.out.println(" This result has " + colCount + " columns.\n");

					/* For every column, display it's information. */
					System.out.println(" Displaying column information: ");

					int i = 0;     /* Initialize loop counter */
					while(rset.next())
					{

						for(int j=1;j<=colCount;j++){
							if(rset.getString(j)!=null){
								tablematrix[i][j-1]=rset.getString(j).replaceAll("\n", "").trim();
								System.out.println(rset.getString(j));
								System.out.println("Table Matrix value["+i+"]["+(j-1)+"]: "+tablematrix[i][j-1].toString()+"\t");
							}else{
								tablematrix[i][j-1]="NULL";
							}                    	
							System.out.println("Table Matrix length: "+tablematrix.length);
						}
						/* Increment column counter */
						i++;
					}
				}
				finally
				{
					/* Close the statement */
					stmt.close();
					System.out.println("\n Statement object closed. \n");
				}
			}
			finally
			{
				/* Close the connection */
				System.out.println(" Closing connection to MySQL...");
				con.close();
				System.out.println(" Connection to MySQL closed. \n");
			}

			System.out.println(" MySQL DB Connectivity finished. \n");
		}
		catch (SQLException ex)
		{
			/* If a SQLException was generated.  Catch it and display
             the error information. Note that there could be multiple
             error objects chained together. */
			System.out.println("*** SQLException caught ***");

			while (ex != null)
			{
				System.out.println(" Error code: " + ex.getErrorCode());
				System.out.println(" SQL State: " + ex.getSQLState());
				System.out.println(" Message: " + ex.getMessage());
				ex.printStackTrace();
				ex = ex.getNextException();
			}

			throw new IllegalStateException ("DB hit failed.") ;
		}


		return tablematrix;
	}
	
	/**
	 * Method to hit a query in MySQL DB and retrieve record counts only
	 * @author SharanMurali
	 * @since Oct 11 2019
	 * @param sQuery
	 * @return int
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public int getRecordCountFromDB(String sQuery) throws ClassNotFoundException, IOException{

		int rowCount = 0;
		Statement stmt = null;
		ResultSet rset = null;

		readDBPropertyfile();


		/* Creation of URL to be passed to the JDBC driver */
		String url = "jdbc:"+properties.getProperty("dbSystem")+"://"+properties.getProperty("dbServerName")+":"+properties.getProperty("dbPort")+"/"+properties.getProperty("dbSchema")+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=IST";

		try
		{
			/* Loading the MySQL JDBC driver  */
			Class.forName("com.mysql.cj.jdbc.Driver");

			/* Creating a connection object */
			Connection con = DriverManager.getConnection(url,properties.getProperty("dbUserName"),properties.getProperty("dbPassword") );
			//        System.out.println(" User " + properties.getProperty("dbUserName") + " connected.");
			//			System.out.println(" Connection to MySQL established. \n");

			try
			{
				String schemaName=properties.getProperty("dbSchema");
				if (schemaName != null && schemaName.length() != 0) {

					try {
						/* Creating a statement object from an active connection schema*/
						stmt = con.createStatement();
						stmt.executeUpdate("USE " + schemaName);
					} catch (SQLException exception) {
						System.out.println(exception.getMessage());
						exception.printStackTrace();
					}    

				}
				/* System.out.println(" Statement object created. \n"); */

				/* Retrieve the number of rows returned */
				try
				{
					rset = stmt.executeQuery(sQuery);         

					/* Initialize loop counter */
					while(rset.next())
					{
						++rowCount;
					}
				}
				finally
				{
					/* Close the statement */
					stmt.close();
				}
			}
			finally
			{
				/* Close the connection */
				con.close();
			}

		}
		catch (SQLException ex)
		{
			System.out.println("*** SQLException caught ***");

			while (ex != null)
			{
				System.out.println(" Error code: " + ex.getErrorCode());
				System.out.println(" SQL State: " + ex.getSQLState());
				System.out.println(" Message: " + ex.getMessage());
				ex.printStackTrace();
				ex = ex.getNextException();
			}

			throw new IllegalStateException ("DB hit failed.") ;
		}


		return rowCount;
	}
	
	/**
	 * Method to replace parameters in a SQL query String
	 * @param sqlQuery
	 * @param sqlParameters
	 * @return
	 * @throws IOException
	 */
	public static String getSQLQuery(String sqlQuery, String sqlParameters) throws IOException {
		
		List<String> qParms = new ArrayList<String>(Arrays.asList(sqlParameters.split(";")));
		System.out.println("Query before Modification: "+sqlQuery);
		int count = StringUtils.countMatches(sqlQuery, "@PARM");
		if (count!=qParms.size()) {
			System.out.println("Mismatch in number of Variables between SQL query and passed paramaters");
		}
		else {
			Iterator<String> itr = qParms.iterator();
			while (itr.hasNext()) {
				String var = itr.next();
				//if (key.toString().startsWith(queryInitials)) {
					//System.out.println("Key: " + key + "    Value: " + map.get(key));
				sqlQuery = sqlQuery.replaceFirst("@PARM", var);
			}	
		}
		System.out.println("Query after Modification: "+sqlQuery);
		return sqlQuery;
	}


	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		DBConnectivity td = new DBConnectivity();
		String[][] str = td.getResultFromDB("select distinct USER_EMAIL_ID from de_event_users");
		System.out.println(str[0][0]); 
	}
	
	/**
	 * To read Properties file for DB Configuration details
	 **/
	public static void readDBPropertyfile() {
		properties = new Properties();
		filepath = "src/test/java/com/DEvents/tests/Config/DBConfig.properties";
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
	

}
