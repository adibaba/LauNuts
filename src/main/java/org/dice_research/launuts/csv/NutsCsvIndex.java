package org.dice_research.launuts.csv;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.dice_research.launuts.Configuration;
import org.dice_research.launuts.Io;

/**
 * Index of NUTS CSV data.
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

		// ID to NutsCsv instance for every file
		csvNutsIndex = new TreeMap<>();
		Map<String, File> filenameToFile = Io.getFilesIndex(Configuration.DIRECTORY_CSV_NUTS);
		for (Entry<String, File> entry : filenameToFile.entrySet()) {
			String id = createId(entry.getKey());
			NutsCsv nutsCsv = new NutsCsv(id, new CsvReader(entry.getValue()).read());
			nutsCsv.setRowIndexHeadings(1).setDefaultDataRange();
			csvNutsIndex.put(id, nutsCsv);
		}

		// NUTS 2013-2016: Duplicate code 'FR7', 'CENTRE-EST' is also in RDF,
		// 'AUVERGNE-RHÔNE-ALPES' is not
		csvNutsIndex.get("NUTS-2013-2016").addRowToSkip(909);

		// NUTS CSV 2021 has another format and is splitted
		csvNutsIndex.get("NUTS-2021").setRowIndexHeadings(0).setDefaultDataRange().setRowIndexDataEnd(1844);
		setIndexes2021(csvNutsIndex.get("NUTS-2021"));

		// NUTS CSV 2021 has additional data
		String id = "NUTS-2021-EXTRA";
		NutsCsv nutsCsv = new NutsCsv(id, new CsvReader(filenameToFile.get("nuts-2021.csv")).read());
		nutsCsv.setRowIndexHeadings(0).setDefaultDataRange().setRowIndexDataBegin(1846);
		setIndexes2021(nutsCsv);
		csvNutsIndex.put(id, nutsCsv);
	}

	private String createId(String filename) {
		return filename.substring(0, filename.lastIndexOf('.')).toUpperCase();
	}

	private void setIndexes2021(NutsCsv nutsCsv) {
		nutsCsv.columnIndexValueCheck = 5;
		nutsCsv.columnIndexCodeNew = -1;
		nutsCsv.columnIndexCodeOld = 0;
		nutsCsv.columnIndexNameCountry = 1;
		nutsCsv.columnIndexNameNuts1 = 2;
		nutsCsv.columnIndexNameNuts2 = 3;
		nutsCsv.columnIndexNameNuts3 = 4;
		nutsCsv.columnIndexLevel = 5;
		nutsCsv.columnIndexCountrySort = 6;
		nutsCsv.columnIndexCodeOldSort = 7;
		nutsCsv.columnIndexCodeNewSort = -1;
	}
}