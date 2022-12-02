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

/**
 * NUTS CSV parser
 * 
 * @author Adrian Wilke
 */
public class NutsCsvParser {

	private File file;
	private String sourceId;
	private int headingsRowIndex = -1;
	private int headingColumnNumberCode = -1;
	private int headingColumnNumberCountry = -1;
	private int headingColumnNumberNuts1 = -1;
	private int headingColumnNumberNuts2 = -1;
	private int headingColumnNumberNuts3 = -1;

	public NutsCsvParser(File file, String sourceId) {
		this.file = file;
		this.sourceId = sourceId;
	}

	/**
	 * Searches columns of headings and parses values.
	 * 
	 * @throws IOException on parsing errors
	 */
	public NutsCsvCollection parse() throws IOException {

		// Get column numbers
		searchHeadings(false);

		// Parse rows
		NutsCsvCollection nutsCsvCollection = new NutsCsvCollection(sourceId);
		CSVParser csvParser = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
		int rowIndex = -1;
		Iterator<CSVRecord> recordIt = csvParser.iterator();
		String nutsCode, value;
		while (recordIt.hasNext()) {
			rowIndex++;
			CSVRecord csvRecord = recordIt.next();
			if (rowIndex <= headingsRowIndex) {
				continue;
			}

			// Parse row values
			nutsCode = null;
			value = null;
			nutsCode = csvRecord.get(headingColumnNumberCode).trim();
			if (nutsCode.isEmpty() || nutsCode.length() > 5) {
				continue;
			}
			if (nutsCode.length() == 2) {
				value = csvRecord.get(headingColumnNumberCountry).replace(" ", " ").trim();
			} else if (nutsCode.length() == 3) {
				value = csvRecord.get(headingColumnNumberNuts1).trim();
			} else if (nutsCode.length() == 4) {
				value = csvRecord.get(headingColumnNumberNuts2).trim();
			} else if (nutsCode.length() == 5) {
				value = csvRecord.get(headingColumnNumberNuts3).trim();
			}
			if (nutsCode == null || nutsCode.isEmpty() || value == null || value.isEmpty()) {
				continue;
			}
			nutsCsvCollection.add(new NutsCsvItem(nutsCode, value));
		}
		return nutsCsvCollection;
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
		int rowIndex = -1;
		List<String> values = new LinkedList<>();
		while (recordIt.hasNext()) {
			// Found in previous iteration
			if (this.headingsRowIndex != -1) {
				return values;
			}

			// Search for heading value(s)
			rowIndex++;
			values.clear();
			Iterator<String> valueIt = recordIt.next().iterator();
			while (valueIt.hasNext()) {
				String value = valueIt.next();
				if (value.equals("NUTS level 3")) {
					this.headingsRowIndex = rowIndex;
				}
				values.add(value);
			}
		}
		throw new RuntimeException("NUTS headings row not found in " + this.file.getAbsolutePath());
	}
}
