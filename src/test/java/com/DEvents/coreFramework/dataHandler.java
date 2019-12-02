package com.DEvents.coreFramework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.DEvents.utils.XLUtils;

/**
 * Collection of methods to handle json
 * @author shmurali
 *
 */
public class dataHandler {

	/**
	 * @author shmurali
	 * @param jsonInput
	 * @param xlOutputfilename
	 * 
	 */
	public static void json2xlsx(String jsonInput, String xlOutputfilename){
		
		String a=Character.toString(jsonInput.charAt(0));
		switch (a) {
		case "{":
			jsonInput="["+jsonInput+"]";
			break;
		default:
			break;
		}
			
		
		String jsonString = "{\"infile\": "+jsonInput+"}";
		JSONObject output;
		try {
			//To convert json data into csv
			output = new JSONObject(jsonString);
			JSONArray docs = output.getJSONArray("infile");
			File file=new File(System.getProperty("user.dir")+"/src/test/java/com/DEvents/tests/jsonResults/"+xlOutputfilename+".csv");
			String csv = CDL.toString(docs);
			FileUtils.writeStringToFile(file, csv, (String)null);

			// To save csv data into Excel - xlsx file
			String xlsxFileAddress = System.getProperty("user.dir")+"/src/test/java/com/DEvents/tests/jsonResults/"+xlOutputfilename+".xlsx"; //xlsx file address
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("JSON_Data");
			String currentLine=null;
			int RowNum=-1;
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((currentLine = br.readLine()) != null) {
				String str[] = currentLine.split(",");
				RowNum++;
				XSSFRow currentRow=sheet.createRow(RowNum);
				for(int i=0;i<str.length;i++){
					currentRow.createCell(i).setCellValue(str[i]);
				}
			}
			// Cleanup
			FileOutputStream fileOutputStream =  new FileOutputStream(xlsxFileAddress);
			workBook.write(fileOutputStream);
			fileOutputStream.close();
			br.close();
			workBook.close();
			file.delete();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
    public static boolean compareOutputSheets(String xlfilename) throws IOException {
    	boolean equalSheets = true;
    	FileInputStream fi = new FileInputStream(System.getProperty("user.dir")+"/src/test/java/com/DEvents/tests/jsonResults/"+xlfilename+".xlsx");
    	XSSFWorkbook wb = new XSSFWorkbook(fi);
		XSSFSheet ws1 = wb.getSheet("DB_Data");
		XSSFSheet ws2 = wb.getSheet("JSON_Data");
		
        if(XLUtils.compareTwoSheets(ws1, ws2)) {
            System.out.println("\n\nThe two excel sheets are Equal");
        } else {
        	equalSheets = false;
            System.out.println("\n\nThe two excel sheets are Not Equal");
        }
        FileOutputStream fo = new FileOutputStream(System.getProperty("user.dir")+"/src/test/java/com/DEvents/tests/jsonResults/"+xlfilename+".xlsx");
        wb.write(fo);
        wb.close();
        fi.close();
        return equalSheets;
    }

}


