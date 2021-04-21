package org.dice_research.launuts;

import java.io.File;
import java.util.Iterator;
import java.util.List;
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
			nutsCsv.setRowHeadings(1).setDefaultDataRange().setRowCode(2);
			csvNutsIndex.put(entry.getKey().substring(0, entry.getKey().lastIndexOf('.')).toUpperCase(), nutsCsv);
		}
		csvNutsIndex.get("NUTS-2021").setRowHeadings(0).setDefaultDataRange().setRowCode(0);

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
			Iterator<List<String>> it = nutsCsv.iterator();
			while (it.hasNext()) {
				System.out.println(it.next());
			}
		}
		// Works. Next step: set correct row-indexes. Implement methods to access
		// NUTS-level and titles.
	}

	private void printParsedCsv(File file) {
		System.out.println(new CsvReader(file).read());
	}

}