package org.dice_research.launuts.csv;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class NutsCsvParser {

	private File file;
	private int headingsRowNumber = -1;
	private int headingColumnNumberCode = -1;
	private int headingColumnNumberCountry = -1;
	private int headingColumnNumberNuts1 = -1;
	private int headingColumnNumberNuts2 = -1;
	private int headingColumnNumberNuts3 = -1;

	public NutsCsvParser(File file) {
		this.file = file;
	}

	/**
	 * Determines headings row and columns by text comparisons.
	 * 
	 * @param printOverview If true, the heading fields are printed
	 * @throws IOException      on parsing errors
	 * @throws RuntimeException if headings not found
	 */
	public void searchHeadings(boolean printOverview) throws IOException {
		// Set this.headingsRowNumber
		// Get heading values
		List<String> headings = searchHeadingsRow();

		// Set headingColumnNumber*
		searchHeadingColumns(headings);

		if (printOverview) {
			StringBuilder sb = new StringBuilder();
			sb.append(headings);
			sb.append("\n");
			sb.append(headings.get(headingColumnNumberCode));
			sb.append(" | ");
			sb.append(headings.get(headingColumnNumberCountry));
			sb.append(" | ");
			sb.append(headings.get(headingColumnNumberNuts1));
			sb.append(" | ");
			sb.append(headings.get(headingColumnNumberNuts2));
			sb.append(" | ");
			sb.append(headings.get(headingColumnNumberNuts3));
			sb.append("\n");
			System.out.println(sb.toString());
		}
	}

	/**
	 * Determines headings columns by text comparisons.
	 * 
	 * @param headings List of heading values to search in.
	 * 
	 * @throws RuntimeException if headings not found
	 */
	private void searchHeadingColumns(List<String> headings) {
		int columnNumber = -1;
		for (String columnHeading : headings) {
			columnNumber++;
			if (columnHeading.startsWith("Code ")) {
				this.headingColumnNumberCode = columnNumber;
			} else if (columnHeading.equals("Country")) {
				this.headingColumnNumberCountry = columnNumber;
			} else if (columnHeading.equals("NUTS level 1")) {
				this.headingColumnNumberNuts1 = columnNumber;
			} else if (columnHeading.equals("NUTS level 2")) {
				this.headingColumnNumberNuts2 = columnNumber;
			} else if (columnHeading.equals("NUTS level 3")) {
				this.headingColumnNumberNuts3 = columnNumber;
			}
		}

		if (headingColumnNumberCode == -1 || headingColumnNumberCountry == -1 || headingColumnNumberNuts1 == -1
				|| headingColumnNumberNuts2 == -1 || headingColumnNumberNuts3 == -1) {
			throw new RuntimeException("NUTS headings cols not found in " + headings);
		}
	}

	/**
	 * Determines headings row by text comparisons.
	 * 
	 * If found, the line number is stored in this object and the headings values
	 * are returned.
	 * 
	 * If not found, an Exception is thrown.
	 * 
	 * @return headings values
	 * @throws IOException      on parsing errors
	 * @throws RuntimeException if headings not found
	 */
	private List<String> searchHeadingsRow() throws IOException {
		CSVParser csvParser = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
		Iterator<CSVRecord> recordIt = csvParser.iterator();
		int rowNumber = -1;
		List<String> values = new LinkedList<>();
		while (recordIt.hasNext()) {
			// Found in previous iteration
			if (this.headingsRowNumber != -1) {
				return values;
			}

			// Search for heading value(s)
			rowNumber++;
			values.clear();
			Iterator<String> valueIt = recordIt.next().iterator();
			while (valueIt.hasNext()) {
				String value = valueIt.next();
				if (value.equals("NUTS level 3")) {
					this.headingsRowNumber = rowNumber;
				}
				values.add(value);
			}
		}
		throw new RuntimeException("NUTS headings row not found in " + this.file.getAbsolutePath());
	}
}
