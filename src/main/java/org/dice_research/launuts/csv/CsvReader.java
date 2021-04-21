package org.dice_research.launuts.csv;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.dice_research.launuts.Io;
import org.dice_research.launuts.exceptions.CsvRuntimeException;

/**
 * CSV Reader.
 * 
 * Usage: Use {@link #CsvReader(File)} or {@link #setFile(File)} to set the CSV
 * file to parse. Use setter methods to specify the parsing configuration. Call
 * {@link #read()} to parse data and getter methods to access data.
 * 
 * @see https://commons.apache.org/proper/commons-csv/
 *
 * @author Adrian Wilke
 */
public class CsvReader {

	private File file;
	private Charset charset = StandardCharsets.UTF_8;
	private CSVFormat csvFormat = CSVFormat.DEFAULT;
	private boolean trimValues = true;
	private boolean removeEmptyRows = true;
	private CSVParser csvParser;

	private List<List<String>> data;
	private int columnSize;

	public CsvReader() {
	}

	public CsvReader(File file) {
		setFile(file);
	}

	public CsvReader setFile(File file) {
		Io.checkReadable(file);
		this.file = file;
		return this;
	}

	public CsvReader setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	public CsvReader setCsvFormat(CSVFormat csvFormat) {
		this.csvFormat = csvFormat;
		return this;
	}

	public CsvReader setTrimValues(boolean trimValues) {
		this.trimValues = trimValues;
		return this;
	}

	public CsvReader setRemoveEmptyRows(boolean removeEmptyRows) {
		this.removeEmptyRows = removeEmptyRows;
		return this;
	}

	public CsvReader read() {
		this.data = new LinkedList<List<String>>();
		this.columnSize = 0;

		Iterator<String> iterator;
		List<String> rowValues;
		boolean isEmptyRow;
		int column;
		String value;

		try {
			// Rows
			for (CSVRecord record : parse().csvParser.getRecords()) {
				iterator = record.iterator();
				rowValues = new ArrayList<String>(record.size());
				isEmptyRow = true;
				column = 0;

				// Cells
				while (iterator.hasNext()) {

					value = iterator.next();
					if (trimValues) {
						value = value.trim();
					}

					if (!value.isEmpty()) {
						isEmptyRow = false;
						column++;
					}

					rowValues.add(value);
				}

				if (!removeEmptyRows || !isEmptyRow) {
					this.data.add(rowValues);
				}

				if (column > this.columnSize) {
					this.columnSize = column;
				}
			}

		} catch (IOException e) {
			throw new CsvRuntimeException(e);
		}
		return this;
	}

	private CsvReader parse() {
		if (file == null) {
			throw new CsvRuntimeException("No CSV file set.");
		}
		try {
			this.csvParser = CSVParser.parse(file, charset, csvFormat);
		} catch (IOException e) {
			throw new CsvRuntimeException(e);
		}
		return this;
	}

	public File getFile() {
		return file;
	}

	public List<List<String>> getData() {
		return data;
	}

	public List<String> getRow(int row) {
		return data.get(row);
	}

	public String getValue(int row, int column) {
		return data.get(row).get(column);
	}

	public int getRowSize() {
		return data.size();
	}

	public int getColumnSize() {
		return columnSize;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int r = 0; r < getRowSize(); r++) {
			for (int c = 0; c < getColumnSize(); c++) {
				if (c != 0) {
					stringBuilder.append(", ");
				}
				stringBuilder.append(getValue(r, c));
			}
			stringBuilder.append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}
}