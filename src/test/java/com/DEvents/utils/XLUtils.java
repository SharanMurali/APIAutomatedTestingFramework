package com.DEvents.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class XLUtils 
{
	public static FileInputStream fi;
	public static FileOutputStream fo;
	public static XSSFWorkbook wb;
	public static XSSFSheet ws;
	public static XSSFRow row;
	public static XSSFCell cell;
	public static XSSFCellStyle style;

	
	public static int getRowCount(String xlfile,String xlsheet) throws IOException 
	{
		int rowcount;
		fi=new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		ws = wb.getSheet(xlsheet);
		rowcount = ws.getPhysicalNumberOfRows() - 1;
		wb.close();
		fi.close();
		return rowcount;	
	}
	
	public static int getCellCount(String xlfile,String xlsheet,int rownum) throws IOException
	{
		int cellcount;
		fi= new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		ws = wb.getSheet(xlsheet);
		row =  ws.getRow(rownum);
		cellcount = row.getLastCellNum();
		wb.close();
		fi.close();
		return cellcount;

	}
	public static String getCellData(String xlfile,String xlsheet,int rownum,int colnum) throws IOException 
	{
		String data;
		fi = new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		ws = wb.getSheet(xlsheet);
		row = ws.getRow(rownum);
		cell = row.getCell(colnum);
		try 
		{
			data = cell.getStringCellValue();
			return data;

		} catch (Exception e) {
			data="";
			return data;
		}
	}

	public static void setCellData(String xlfile,String xlsheet,int rownum,int colnum,String data) throws IOException
	{

		fi = new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		ws = wb.getSheet(xlsheet);
		row = ws.getRow(rownum);
		cell = row.createCell(colnum);
		cell.setCellValue(data);

		fo = new FileOutputStream(xlfile);
		wb.write(fo);
		fi.close();
		fo.close();

	}
	public static void fillGreenColour(String xlfile,String xlsheet,int rownum,int colnum) throws IOException
	{
		fi = new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		ws = wb.getSheet(xlsheet);
		row = ws.getRow(rownum);
		cell = row.getCell(colnum);
		style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell.setCellStyle(style);

		fo=new FileOutputStream(xlfile);
		wb.write(fo);
		wb.close();
		fi.close();
		fo.close();
	}
	public static void fillRedColour(String xlfile,String xlsheet,int rownum,int colnum) throws IOException 
	{
		fi = new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		ws = wb.getSheet(xlsheet);
		row = ws.getRow(rownum);
		cell = row.getCell(colnum);
		style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.RED.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell.setCellStyle(style);

		fo = new FileOutputStream(xlfile);
		wb.write(fo);
		wb.close();
		fi.close();
		fo.close();

	}

	public static int getColumnIndexbyHeader(String xlfile,String xlsheet,String header)throws IOException
	{
		int colIndex=-1;
		fi=new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		ws = wb.getSheet(xlsheet);

		row =  ws.getRow(0);
		for (int i=0;i<row.getLastCellNum();i++) {
			if(getCellData(xlfile,xlsheet,0,i).equalsIgnoreCase(header)) {
				colIndex=i;
				break;
			}
		}

		wb.close();
		fi.close();
		return colIndex;

	}

	public static void resultSet2xlsx(ResultSet resultSet, String xlOutputfilename, String columnHeaders) throws IOException, SQLException {

		fi = new FileInputStream(System.getProperty("user.dir")+"/src/test/java/com/DEvents/tests/jsonResults/"+xlOutputfilename+".xlsx");
		wb = new XSSFWorkbook(fi);
		ws = wb.createSheet("DB_Data");

		//Writes column header in the first row of the spreadsheet
		String colHeader[] =columnHeaders.split(";");
		row = ws.createRow(1);
		int i=1;

		for(String col:colHeader) {
			cell = row.createCell(i);
			cell.setCellValue(col);
			i++;
		}

		int j=2;
		while(resultSet.next()) {
			row = ws.createRow(j);
			i=1;
			for(String col:colHeader) {
				cell = row.createCell(i);
				cell.setCellValue(resultSet.getInt(col));
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


	// Compare Two Sheets
	public static boolean compareTwoSheets(XSSFSheet sheet1, XSSFSheet sheet2) {
		int firstRow1 = sheet1.getFirstRowNum();
		int lastRow1 = sheet1.getLastRowNum();
		System.out.println("\n\nLast Row :"+lastRow1);
		boolean equalSheets = true;
		if(lastRow1==0) {
			equalSheets =false;
		}
		else {
			for(int i=firstRow1; i <= lastRow1; i++) {

				System.out.println("\n\nComparing Row "+i);

				XSSFRow row1 = sheet1.getRow(i);
				XSSFRow row2 = sheet2.getRow(i);
				if(!compareTwoRows(row1, row2)) {
					equalSheets = false;
					System.out.println("Row "+i+" - Not Equal");
					//break;
				} else {
					System.out.println("Row "+i+" - Equal");
				}
			}
		}
		return equalSheets;
	}

	// Compare Two Rows
	public static boolean compareTwoRows(XSSFRow row1, XSSFRow row2) {
		if((row1 == null) && (row2 == null)) {
			return true;
		} else if((row1 == null) || (row2 == null)) {
			return false;
		}

		int firstCell1 = row1.getFirstCellNum();
		int lastCell1 = row1.getLastCellNum();
		boolean equalRows = true;

		// Compare all cells in a row
		for(int i=firstCell1; i <= lastCell1; i++) {
			XSSFCell cell1 = row1.getCell(i);
			XSSFCell cell2 = row2.getCell(i);
			if(!compareTwoCells(cell1, cell2)) {
				equalRows = false;
				System.err.println("       Cell "+i+" - NOt Equal");
				break;
			} else {
				System.out.println("       Cell "+i+" - Equal");
			}
		}
		return equalRows;
	}

	// Compare Two Cells
	public static boolean compareTwoCells(XSSFCell cell1, XSSFCell cell2) {
		if((cell1 == null) && (cell2 == null)) {
			return true;
		} else if((cell1 == null) || (cell2 == null)) {
			return false;
		}
		style = (XSSFCellStyle) cell2.getSheet().getWorkbook().createCellStyle();
		style.setFillForegroundColor(IndexedColors.RED.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		boolean equalCells = false;
		CellType type1 = cell1.getCellType();
		CellType type2 = cell2.getCellType();
		if (type1 == type2) {
			if (cell1.getCellStyle().equals(cell2.getCellStyle())) {
				// Compare cells based on its type
				switch (cell1.getCellType()) {
				case FORMULA:
					if (cell1.getCellFormula().equals(cell2.getCellFormula())) {
						equalCells = true;
					}
					else {
						cell2.setCellStyle(style);
					}
					break;
				case NUMERIC:
					if (cell1.getNumericCellValue() == cell2
					.getNumericCellValue()) {
						equalCells = true;
					}
					else {
						cell2.setCellStyle(style);
					}
					break;
				case STRING:
					if (cell1.getStringCellValue().equals(cell2
							.getStringCellValue())) {
						equalCells = true;
					}
					else {
						cell2.setCellStyle(style);
					}
					break;
				case BLANK:
					if (cell2.getCellType() == CellType.BLANK) {
						equalCells = true;
					}
					else {
						cell2.setCellStyle(style);
					}
					break;
				case BOOLEAN:
					if (cell1.getBooleanCellValue() == cell2
					.getBooleanCellValue()) {
						equalCells = true;
					}
					else {
						cell2.setCellStyle(style);
					}
					break;
				case ERROR:
					if (cell1.getErrorCellValue() == cell2.getErrorCellValue()) {
						equalCells = true;
					}
					break;
				default:
					if (cell1.getStringCellValue().equals(
							cell2.getStringCellValue())) {
						equalCells = true;
					}
					else {
						cell2.setCellStyle(style);
					}
					break;
				}
			} else {
				return false;
			}
		} 
		else if((type1==CellType.BLANK && type2==CellType.STRING) || (type2==CellType.BLANK && type1==CellType.STRING)) {
			if(cell1.getStringCellValue()=="" || cell2.getStringCellValue()=="") {
				equalCells = true;
			}	
		}
		else {
			return false;
		}
		return equalCells;
	}

}
