package org.dice_research.launuts.excel;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Reads Excel file values.
 *
 * @author Adrian Wilke
 */
public class ExcelFile {

	/**
	 * Iterates a workbook and returns excel sheets.
	 * 
	 * Once you have finished working with it, call {@link #close()}.
	 */
	public class ExcelSheetIterator implements Iterator<ExcelSheet>, Closeable {

		private Workbook workbook;
		private Iterator<Sheet> sheetIterator;

		public ExcelSheetIterator() throws InvalidFormatException, IOException {
			try {
				if (file.getName().endsWith(".xlsx"))
					this.workbook = new XSSFWorkbook(file);
				else
					this.workbook = new HSSFWorkbook(new FileInputStream(file));
			} catch (Exception e) {
				// Print exception information to be aware of file
				System.err.println("Exception: " + ExcelSheetIterator.class.getName() + " " + file.getAbsolutePath());
				throw e;
			}
			sheetIterator = this.workbook.sheetIterator();
		}

		@Override
		public boolean hasNext() {
			return sheetIterator.hasNext();
		}

		@Override
		public ExcelSheet next() {
			return new ExcelSheet(sheetIterator.next());
		}

		public void close() throws IOException {
			workbook.close();
		}
	}

	private File file;

	public ExcelFile(File file) {
		this.file = file;
	}

	/**
	 * Iterates a workbook and returns excel sheets.
	 * 
	 * Once you have finished working with it, call {@link #close()}.
	 */
	public ExcelSheetIterator getExcelSheetIterator() throws InvalidFormatException, IOException {
		return new ExcelSheetIterator();
	}
}
