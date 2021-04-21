package org.dice_research.launuts.csv;

import java.util.Iterator;
import java.util.List;

public class NutsCsv implements Iterable<List<String>> {

	private final CsvReader csvReader;
	private int rowHeadings;
	private int columnCode;

	// TODO: plan is for 2021-data to use the same csvReader, but split the data.
	// See line 1847, it splits the data
	private int rowDataBegin;
	private int rowDataEnd;

	public NutsCsv(CsvReader csvReader) {
		this.csvReader = csvReader;
	}

	public NutsCsv setRowHeadings(int rowHeadings) {
		this.rowHeadings = rowHeadings;
		return this;
	}

	public NutsCsv setDefaultDataRange() {
		rowDataBegin = rowHeadings + 1;
		rowDataEnd = csvReader.getRowSize();
		return this;

	}

	public NutsCsv setRowCode(int rowCode) {
		this.columnCode = rowCode;
		return this;
	}

	public List<String> getHeadings() {
		return csvReader.getRow(rowHeadings);
	}

	public String getCode(int row) {
		return csvReader.getRow(row).get(columnCode);
	}

	@Override
	public Iterator<List<String>> iterator() {
		return new Iterator<List<String>>() {

			private int index = rowDataBegin;

			@Override
			public boolean hasNext() {
				return index < rowDataEnd;
			}

			@Override
			public List<String> next() {
				return csvReader.getRow(index++);
			}
		};
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + csvReader.getFile().getName() + "]";
	}

}