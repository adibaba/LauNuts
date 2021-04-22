package org.dice_research.launuts;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.dice_research.launuts.csv.CsvReader;
import org.dice_research.launuts.csv.NutsCsv;

/**
 * TODO Dev.
 *
 * @author Adrian Wilke
 */
public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Main main = new Main();

		// Current dev
		if (true)
			main.dev();

		// Take a look into data
		if (false)
			main.printParsedCsv(new File("/home/adi/DICE/Data/LauNuts/Sources-2021/data/nuts/nuts-2021.csv"));
	}

	/**
	 * TODO Current dev
	 */
	@SuppressWarnings("unused")
	private void dev() {

		// ID to NutsCsv instance for every file
		Map<String, NutsCsv> csvNutsIndex = new TreeMap<>();
		for (Entry<String, File> entry : Io.getFilesIndex(Configuration.DIRECTORY_CSV_NUTS).entrySet()) {
			NutsCsv nutsCsv = new NutsCsv(new CsvReader(entry.getValue()).read());
			nutsCsv.setRowIndexHeadings(1).setDefaultDataRange();
			csvNutsIndex.put(entry.getKey().substring(0, entry.getKey().lastIndexOf('.')).toUpperCase(), nutsCsv);
		}

		// Empty lines are removed. This is end of official / start of partners
		// TODO: add partners in another instance
		csvNutsIndex.get("NUTS-2021").setDefaultDataRange().setRowIndexDataEnd(1844);
		csvNutsIndex.get("NUTS-2021").columnIndexValueCheck = -1;
		csvNutsIndex.get("NUTS-2021").columnIndexCodeNew = -1;
		csvNutsIndex.get("NUTS-2021").columnIndexCodeOld = 0;
		csvNutsIndex.get("NUTS-2021").columnIndexNameCountry = 1;
		csvNutsIndex.get("NUTS-2021").columnIndexNameNuts1 = 2;
		csvNutsIndex.get("NUTS-2021").columnIndexNameNuts2 = 3;
		csvNutsIndex.get("NUTS-2021").columnIndexNameNuts3 = 4;
		csvNutsIndex.get("NUTS-2021").columnIndexLevel = 5;
		csvNutsIndex.get("NUTS-2021").columnIndexCountrySort = 6;
		csvNutsIndex.get("NUTS-2021").columnIndexCodeOldSort = 7;
		csvNutsIndex.get("NUTS-2021").columnIndexCodeNewSort = -1;

		// TODO: Check correctness of parsed values

		// Print map above
		for (Entry<String, NutsCsv> entry : csvNutsIndex.entrySet()) {
			System.out.println(entry);
		}

		if (true)
			// Check headings
			for (Entry<String, NutsCsv> entry : csvNutsIndex.entrySet()) {
				System.out.println(entry.getValue().getHeadings());
				// Get example code
				System.out.println(entry.getValue().getCode(3));
			}

		// Print payload
		for (NutsCsv nutsCsv : csvNutsIndex.values()) {

			System.err.println(nutsCsv);
//			Iterator<Integer> it = nutsCsv.iterator();
//			while (it.hasNext()) {
//				int row = it.next();
//				System.out.println(row);
//			}
			System.out.println(nutsCsv.getDataString());
		}
		// Works. Next step: set correct row-indexes. Implement methods to access
		// NUTS-level and titles.
	}

	private void printParsedCsv(File file) {
		System.out.println(new CsvReader(file).read());
	}

}