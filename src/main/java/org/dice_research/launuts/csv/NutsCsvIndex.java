package org.dice_research.launuts.csv;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.dice_research.launuts.Configuration;
import org.dice_research.launuts.Io;

/**
 * Index of NUTS CSV data. Configuration and parsing of CSV.
 *
 * @author Adrian Wilke
 */
public class NutsCsvIndex {

	Map<String, NutsCsv> csvNutsIndex;

	/**
	 * Gets map: IDs to NutsCsv instances.
	 */
	public Map<String, NutsCsv> get() {
		if (csvNutsIndex == null) {
			create();
		}
		return csvNutsIndex;
	}

	private void create() {
		csvNutsIndex = new TreeMap<>();
		String id;
		NutsCsv nutsCsv;

		// Get files
		Map<String, File> filenameToFile = Io.getFilesIndex(Configuration.DIRECTORY_CSV_NUTS);

		// Create CsvReader for each file, read files
		Map<String, CsvReader> filenameToCsvReader = new TreeMap<>();
		for (Entry<String, File> entry : filenameToFile.entrySet()) {
			filenameToCsvReader.put(entry.getKey(), new CsvReader(entry.getValue()).read());
		}

		// Create one entry <ID, CsvReader> for every file (Using column for old NUTS
		// code)
		for (Entry<String, CsvReader> entry : filenameToCsvReader.entrySet()) {
			id = createId(entry.getKey());
			nutsCsv = new NutsCsv(id, entry.getValue());
			nutsCsv.setRowIndexHeadings(1).setDefaultDataRange();
			csvNutsIndex.put(id, nutsCsv);
		}

		// nuts-2013-2016.csv: Duplicate 2013 code 'FR7'. Checked:
		// 'AUVERGNE-RHÔNE-ALPES' should probably not be 'FR7'.
		// Solution 1: remove row
		// csvNutsIndex.get("2013").addRowToSkip(909);
		// Solution 2: use other file
		id = "2013";
		nutsCsv = new NutsCsv(id, filenameToCsvReader.get("nuts-2010-2013.csv"));
		nutsCsv.setUseCodeOld(false).setRowIndexHeadings(1).setDefaultDataRange();
		nutsCsv.columnIndexValueNotEmptyCheck = 11;
		csvNutsIndex.put(id, nutsCsv);

		// Add 2016
		id = "2016";
		nutsCsv = new NutsCsv(id, filenameToCsvReader.get("nuts-2013-2016.csv"));
		nutsCsv.setUseCodeOld(false).setRowIndexHeadings(1).setDefaultDataRange();
		nutsCsv.columnIndexValueNotEmptyCheck = 11;
		csvNutsIndex.put(id, nutsCsv);

		// NUTS CSV 2021 has another format and is split
		csvNutsIndex.get("2021").setRowIndexHeadings(0).setDefaultDataRange().setRowIndexDataEnd(1844);
		setIndexes2021(csvNutsIndex.get("2021"));

		// NUTS CSV 2021 has additional data
		id = "2021-EXTRA";
		nutsCsv = new NutsCsv(id, csvNutsIndex.get("2021").getCsvReader());
		nutsCsv.setRowIndexHeadings(0).setDefaultDataRange().setRowIndexDataBegin(1846);
		setIndexes2021(nutsCsv);
		csvNutsIndex.put(id, nutsCsv);
	}

	private String createId(String filename) {
		int firstMinus = filename.indexOf('-');
		return filename.substring(firstMinus + 1, firstMinus + 1 + 4);
	}

	private void setIndexes2021(NutsCsv nutsCsv) {
		nutsCsv.columnIndexValueNotEmptyCheck = 5;
		nutsCsv.columnIndexCodeNew = -1;
		nutsCsv.columnIndexCodeOld = 0;
		nutsCsv.columnIndexNameCountry = 1;
		nutsCsv.columnIndexNameNuts1 = 2;
		nutsCsv.columnIndexNameNuts2 = 3;
		nutsCsv.columnIndexNameNuts3 = 4;
		nutsCsv.columnIndexLevel = 5;
		nutsCsv.columnIndexCountryOrder = 6;
		nutsCsv.columnIndexCodeOldOrder = 7;
		nutsCsv.columnIndexCodeNewOrder = -1;
	}
}