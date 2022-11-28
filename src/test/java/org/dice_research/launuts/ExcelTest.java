package org.dice_research.launuts;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.dice_research.launuts.excel.ExcelFile;
import org.dice_research.launuts.excel.ExcelFile.ExcelSheetIterator;
import org.dice_research.launuts.excel.ExcelSheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Checks data read by {@link ExcelFile}.
 *
 * @author Adrian Wilke
 */
class ExcelTest {

	@Test
	void test() throws InvalidFormatException, IOException {
		List<List<String>> values = null;
		List<String> headings = null;
		List<String> sheets = new LinkedList<>();

		File file = new File("src/test/resources", "table.xlsx");
		ExcelFile excelFile = new ExcelFile(file);
		ExcelSheetIterator excelSheetIterator = excelFile.getExcelSheetIterator();
		boolean first = true;
		while (excelSheetIterator.hasNext()) {
			ExcelSheet excelSheet = excelSheetIterator.next();
			if (first) {
				first = false;
				values = excelSheet.getValues();
				headings = excelSheet.getHeadings();
			}
			sheets.add(excelSheet.getName());
		}

		Assertions.assertEquals(4, values.size());
		Assertions.assertEquals(3, headings.size());
		Assertions.assertEquals(2, sheets.size());

		// For humans
		if (Boolean.FALSE) {

			// Print first table
			for (List<String> rows : values) {
				for (String string : rows) {
					System.out.print(string);
					System.out.print(" ");
				}
				System.out.println();
			}

			// Print headings of first table
			for (String string : headings) {
				System.out.print(string);
				System.out.print(" ");
			}
			System.out.println();

			// Print sheet names
			System.out.println(sheets);
		}
	}

}