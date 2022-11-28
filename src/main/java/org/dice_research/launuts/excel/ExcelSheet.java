package org.dice_research.launuts.excel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Extracts cell values from Excel sheet.
 * 
 * @author Adrian Wilke
 */
public class ExcelSheet {

	private Sheet sheet;

	public ExcelSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	/**
	 * @return Name of the current sheet.
	 */
	public String getName() {
		return sheet.getSheetName();
	}

	/**
	 * Gets lists of rows. Each list contains a list of cell values.
	 * 
	 * @return Embedded lists of string values
	 */
	public List<List<String>> getValues() {
		List<List<String>> values = new LinkedList<List<String>>();
		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			List<String> rowValues = new LinkedList<String>();
			Iterator<Cell> cellIterator = row.iterator();
			while (cellIterator.hasNext()) {
				rowValues.add(getCellValue(cellIterator.next()));
			}
			values.add(rowValues);
		}
		return values;
	}

	/**
	 * Gets lists of headings.
	 * 
	 * @return Lists of string values
	 */
	public List<String> getHeadings() {
		List<String> headings = new LinkedList<String>();
		Iterator<Row> rowIterator = sheet.rowIterator();
		if (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.iterator();
			while (cellIterator.hasNext()) {
				headings.add(getCellValue(cellIterator.next()));
			}
		}
		return headings;
	}

	/**
	 * Gets string value of a cell.
	 * 
	 * The implementation is based on the value parsing of
	 * {@link org.apache.poi.ss.usermodel.Cell#getStringCellValue()}.
	 * 
	 * @return String value of a cell.
	 */
	private String getCellValue(Cell cell) {
		CellType cellType = cell.getCellType();
		if (cellType.equals(CellType.STRING) || cellType.equals(CellType.BLANK)) {
			return cell.getStringCellValue();
		} else if (cellType.equals(CellType.NUMERIC)) {
			return Double.toString(cell.getNumericCellValue());
		} else if (cellType.equals(CellType.FORMULA)) {
			return cell.getCellFormula();
		} else {
			throw new IllegalStateException(cellType.toString());
		}
	}
}