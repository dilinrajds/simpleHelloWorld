package generateChartFromExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonToExcelConverterWithoutUpdate {

	private ObjectMapper mapper = new ObjectMapper();

	public String jsonFileToExcelFile(String jsonFilePath, String excelFilePath, String excelFileExtension) {
		String response = "Converted Succesfully";
		File jsonFile = new File(jsonFilePath);

		try {
			File file = null;

			if (new File(excelFilePath).createNewFile()) {
				file = new File(excelFilePath);
				Workbook wb = new XSSFWorkbook();
				if (excelFileExtension.equals(".xls")) {
					wb = new HSSFWorkbook();
				}
				FileOutputStream fileOut = new FileOutputStream(excelFilePath);
				wb.write(fileOut);
				fileOut.close();
				wb.close();
			} else {
				file = new File(excelFilePath);
				file.delete();
				Workbook wb = new XSSFWorkbook();
				if (excelFileExtension.equals(".xls")) {
					wb = new HSSFWorkbook();
				}
				FileOutputStream fileOut = new FileOutputStream(excelFilePath);
				wb.write(fileOut);
				fileOut.close();
				wb.close();
			}

			FileInputStream inputStream = new FileInputStream(file);
			Workbook workbook = null;

			if (!jsonFile.getName().endsWith(".json")) {
				response = "The source file should be .json file only";
			} else {

				// Creating workbook object based on target file format
				if (excelFileExtension.equals(".xls") || excelFileExtension.equals(".xlsx")) {
					workbook = WorkbookFactory.create(inputStream);
				} else {
					response = "The target file extension should be .xls or .xlsx only";
				}

				// Reading the json file
				ObjectNode jsonData = (ObjectNode) mapper.readTree(jsonFile);

				// Iterating over the each sheets
				Iterator<String> sheetItr = jsonData.fieldNames();
				while (sheetItr.hasNext()) {

					// create the workbook sheet
					String sheetName = sheetItr.next();
					Sheet sheet = null;
					try {
						sheet = workbook.createSheet(sheetName);
					} catch (Exception e) {
						sheet = workbook.getSheet(sheetName);
					}

					ArrayNode sheetData = (ArrayNode) jsonData.get(sheetName);
					ArrayList<String> headers = new ArrayList<String>();

					// Creating cell style for header to make it bold
					CellStyle headerStyle = workbook.createCellStyle();
					Font font = workbook.createFont();
					font.setBold(true);
					headerStyle.setFont(font);

					// creating the header into the sheet
					Row header = sheet.createRow(0);
					Iterator<String> it = sheetData.get(0).fieldNames();
					int headerIdx = 0;
					while (it.hasNext()) {
						String headerName = it.next();
						headers.add(headerName);
						Cell cell = header.createCell(headerIdx++);
						cell.setCellValue(headerName);
						// apply the bold style to headers
						cell.setCellStyle(headerStyle);
					}

					// Iterating over the each row data and writing into the sheet
					int excelRow = sheet.getLastRowNum() + 1;
					for (int i = 0; i < sheetData.size(); i++) {
						ObjectNode rowData = (ObjectNode) sheetData.get(i);
						Row row = sheet.createRow(i + excelRow);
						for (int j = 0; j < headers.size(); j++) {
							String value = rowData.get(headers.get(j)).asText();
							row.createCell(j).setCellValue(value);
						}
					}

					for (int i = 0; i < headers.size(); i++) {
						sheet.autoSizeColumn(i);
					}

				}

				// write the workbook into target file
				inputStream.close();

				FileOutputStream fos = new FileOutputStream(file);
				workbook.write(fos);
				

				// close the workbook and fos
				workbook.close();
				fos.close();
				return response;
			}
		} catch (Exception e) {
			response = "Failed to Convert --->" + e.toString();
			;
		}
		return response;
	}

	public static void main(String[] args) {

	}

}