package org.dice_research.launuts;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.dice_research.launuts.csv.LauCsvCollection;
import org.dice_research.launuts.csv.LauCsvItem;
import org.dice_research.launuts.csv.LauCsvParser;
import org.dice_research.launuts.csv.NutsCsvCollection;
import org.dice_research.launuts.csv.NutsCsvParser;
import org.dice_research.launuts.rdf.NutsRdfReader;
import org.dice_research.launuts.sources.Source;
import org.dice_research.launuts.sources.SourceCsvSheets;
import org.dice_research.launuts.sources.SourceType;
import org.dice_research.launuts.sources.Sources;

/**
 * LauNuts - Development options.
 * 
 * {@link Dev#DEV} enables development mode.
 * 
 * {@link Dev#DEV_ARGS} are used as arguments in {@link Main}.
 * 
 * {@link Dev#dev(List)} is called, if mode is set to "dev" in arguments.
 *
 * @author Adrian Wilke
 */
public abstract class Dev {

	// Default: false. Enables Development mode.
	public static final boolean DEV = false;

	// Mode constant for {@link Main} CLI
	public static final String MODE = "dev";

	// Main (pre-defined) arguments to use
	public static final String[] DEV_ARGS_ALL_DL = new String[] { "dl" };
	public static final String[] DEV_ARGS_ALL_CSV = new String[] { "csv" };
	public static final String[] DEV_ARGS_ALL_STAT = new String[] { "stat" };
	public static final String[] DEV_ARGS_ALL_HELP = new String[] { "help" };
	public static final String[] DEV_ARGS_ALL_DEV = new String[] { "dev" };
	public static final String[] DEV_ARGS_SINGLE = new String[] { "-ids", "lau2018-nuts2016", "csv" };
	public static final String[] DEV_ARGS = DEV_ARGS_ALL_DEV;

	/**
	 * Called if {@link Main} mode is "dev".
	 * 
	 * @param ids Source IDs configured in {@link Main}.
	 */
	public static void dev(List<String> ids) {
		System.err.println("Development execution");
		try {
			// Print stats of Eurostat KG
			if (Boolean.TRUE) {
				new NutsRdfReader().printStats();
			}

			for (Source source : new Sources().getSources()) {
				if (ids.contains(source.id)) {

					if (source.sourceType.equals(SourceType.NUTS)) {
						File file = new SourceCsvSheets(source).getNutsMainSheetFile();

						NutsCsvCollection nutsCsvCollection = new NutsCsvParser(file, source.id).parse();

						if (Boolean.TRUE)
							System.out.println(nutsCsvCollection);

						if (Boolean.FALSE)
							System.out.println(nutsCsvCollection.getValues(true));

						if (Boolean.FALSE)
							System.out.println(nutsCsvCollection.getMarkdownTable());
					}

					if (Boolean.FALSE)
						if (source.sourceType.equals(SourceType.LAU)) {
							for (File file : new SourceCsvSheets(source).getLauSheetFiles()) {
								LauCsvParser parser = new LauCsvParser(file, source.id);
//							List<String> h = p.searchHeadingsRow();

//							System.out.println(source.id + " " + h);

//							p.searchHeadingColumns(h);

								// TODO: Check parsed values
								LauCsvCollection lauCsvCollection = parser.parse();
								Set<String> keys = lauCsvCollection.getKeys();
								System.out.println(keys);
								List<LauCsvItem> lauCsvItems = lauCsvCollection
										.getLauCsvItemList(keys.iterator().next());
								for (LauCsvItem lauCsvItem : lauCsvItems) {
									System.out.println(lauCsvItem);
								}
							}
						}

					// There was a LAU2 Code

					// area sometimes given in m2 and sometime km2? e.g. lau2020

					// nuts-2013-2016.csv: Duplicate 2013 code 'FR7'. Checked:
					// 'AUVERGNE-RHÔNE-ALPES' should probably not be 'FR7'.
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}