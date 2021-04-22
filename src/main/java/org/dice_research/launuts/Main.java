package org.dice_research.launuts;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.dice_research.launuts.csv.CsvReader;
import org.dice_research.launuts.csv.NutsCsv;
import org.dice_research.launuts.csv.NutsCsvIndex;

/**
 * TODO Dev.
 *
 * @author Adrian Wilke
 */
public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Main main = new Main();

		// TODO: Dynamic config for CLI / Webservices

		// Current dev
		if (true)
			main.dev();

		// Take a look into data
		if (false)
			main.printParsedCsv(new File("/home/adi/DICE/Data/LauNuts/Sources-2021/data/nuts/nuts-2021.csv"));
	}

	/**
	 * TODO: Check correctness of parsed values; use {@link NutsCsvTest#testSizes};
	 * check {@link NutsCsv#columnIndexValueCheck}
	 */
	@SuppressWarnings("unused")
	private void dev() {

		Map<String, NutsCsv> csvNutsIndex = new NutsCsvIndex().get();

		// Print map above
		for (Entry<String, NutsCsv> entry : csvNutsIndex.entrySet()) {
			System.out.println(entry);
		}

		if (false)
			// Check headings
			for (Entry<String, NutsCsv> entry : csvNutsIndex.entrySet()) {
				System.out.println(entry.getValue().getHeadings());
				// Get example code
				System.out.println(entry.getValue().getCode(3));
			}

		// Print payload
		if (true)
			for (NutsCsv nutsCsv : csvNutsIndex.values()) {
				System.err.println(nutsCsv);
				System.out.println(nutsCsv.getDataString());
			}

//		System.out.println(csvNutsIndex.get("NUTS-2021-EXTRA"));

	}

	private void printParsedCsv(File file) {
		System.out.println(new CsvReader(file).read());
	}

}