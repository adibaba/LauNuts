package org.dice_research.launuts.csv;

import java.util.Iterator;
import java.util.List;

import org.dice_research.launuts.exceptions.CsvRuntimeException;

/**
 * NUTS CSV uses a {@link CsvReader} to read data and provides methods for data
 * access. The data to read depends on indexes of rows and columns defined in
 * this class. Row indexes can be set by setter methods, column indexes can be
 * set directly.
 * 
 * As the underlying CSV files sometimes include data of two years, the data to
 * use can be specified with {@link #setUseCodeOld(boolean)}.
 * 
 * This class provides an Iterator to loop through the underlying data payload.
 * The iterator depends on {@link #rowIndexDataBegin} and
 * {@link #rowIndexDataEnd}. It omits rows with missing code and rows with
 * missing {@link #columnIndexValueCheck}.
 * 
 * Configuration: Use the constructor {@link #NutsCsv(CsvReader)} and read the
 * underlying data using {@link CsvReader#read()}. After optionally setting the
 * index of headings with {@link #setRowIndexHeadings(int)}, use
 * {@link #setDefaultDataRange()} or the methods for begin and end indexes. If
 * required, configure the column indexes and set
 * {@link #setUseCodeOld(boolean)}. Use the iterator and getter methods for data
 * access.
 *
 * @author Adrian Wilke
 */
public class NutsCsv implements Iterable<Integer> {

	private String id;

	// Default table row/columns based on 2013-2016
	public int columnIndexValueCheck = 0;
	public int columnIndexCodeOld = 1;
	public int columnIndexCodeNew = 2;
	public int columnIndexNameCountry = 3;
	public int columnIndexNameNuts1 = 4;
	public int columnIndexNameNuts2 = 5;
	public int columnIndexNameNuts3 = 6;
	public int columnIndexLevel = 8;
	public int columnIndexCountrySort = 9;
	public int columnIndexCodeOldSort = 10;
	public int columnIndexCodeNewSort = 11;

	// Row structure
	private int rowIndexHeadings = 0;
	private int rowIndexDataBegin;
	private int rowIndexDataEnd;

	// Use columnIndexCodeOld (or columnIndexCodeNew)
	private boolean useCodeOld = true;

	// Underlying data
	private final CsvReader csvReader;

	public NutsCsv(String id, CsvReader csvReader) {
		this.id = id;
		this.csvReader = csvReader;
	}

	public NutsCsv setRowIndexHeadings(int rowIndexHeadings) {
		this.rowIndexHeadings = rowIndexHeadings;
		return this;
	}

	public NutsCsv setRowIndexDataBegin(int rowIndexDataBegin) {
		this.rowIndexDataBegin = rowIndexDataBegin;
		return this;
	}

	public NutsCsv setRowIndexDataEnd(int rowIndexDataEnd) {
		this.rowIndexDataEnd = rowIndexDataEnd;
		return this;
	}

	public NutsCsv setDefaultDataRange() {
		setRowIndexDataBegin(rowIndexHeadings + 1);
		setRowIndexDataEnd(csvReader.getRowSize() - 1);
		for (int i = rowIndexDataBegin; i < rowIndexDataEnd; i++) {
			if (csvReader.getValue(i, columnIndexValueCheck).isEmpty()) {
				setRowIndexDataEnd(i - 1);
			}
		}
		return this;
	}

	public NutsCsv setUseCodeOld(boolean useCodeOld) {
		this.useCodeOld = useCodeOld;
		return this;
	}

	public List<String> getHeadings() {
		return csvReader.getRow(rowIndexHeadings);
	}

	private int getColumnIndexCode() {
		return useCodeOld ? columnIndexCodeOld : columnIndexCodeNew;
	}

	public boolean hasCode(int row) {
		return !csvReader.getValue(row, getColumnIndexCode()).isEmpty();
	}

	/**
	 * Iterates through data. Use columns 'code' and 'value-check' to omit rows
	 * without code.
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			private int index = rowIndexDataBegin;

			@Override
			public boolean hasNext() {
				return (getNext(index + 1) != -1);
			}

			@Override
			public Integer next() {
				index = getNext(index + 1);
				return index;
			}

			private int getNext(int rowToCheck) {
				if (rowToCheck > rowIndexDataEnd) {
					return -1;
				} else if (hasCode(rowToCheck) && !csvReader.getValue(rowToCheck, columnIndexValueCheck).isEmpty()) {
					return rowToCheck;
				} else {
					return getNext(rowToCheck + 1);
				}
			}
		};
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "<" + id + "|" + csvReader.getFile().getName() + ">";
	}

	public String getDataString() {
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<Integer> it = iterator();
		int row;
		while (it.hasNext()) {
			row = it.next();
			stringBuilder.append(getLevel(row));
			stringBuilder.append(", ");
			stringBuilder.append(getCode(row));
			stringBuilder.append(", ");
			stringBuilder.append(getName(row));
			stringBuilder.append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}

	public String getCode(int row) {
		if (getColumnIndexCode() == -1) {
			throw new CsvRuntimeException(
					"Column index for code invalid " + (useCodeOld ? "(old code)" : "(new code)"));
		}
		return csvReader.getRow(row).get(getColumnIndexCode());
	}

	public String getName(int row) {
		if (getLevel(row).equals("0")) {
			return csvReader.getRow(row).get(columnIndexNameCountry);
		} else if (getLevel(row).equals("1")) {
			return csvReader.getRow(row).get(columnIndexNameNuts1);
		} else if (getLevel(row).equals("2")) {
			return csvReader.getRow(row).get(columnIndexNameNuts2);
		} else if (getLevel(row).equals("3")) {
			return csvReader.getRow(row).get(columnIndexNameNuts3);
		} else {
			throw new CsvRuntimeException("Unknown level '" + csvReader.getRow(row).get(columnIndexLevel) + "' (index "
					+ columnIndexLevel + ") in row " + row + " " + this);
		}
	}

	public String getLevel(int row) {
		return csvReader.getRow(row).get(columnIndexLevel);
	}

}