package org.dice_research.launuts;

import java.io.File;
import java.util.List;

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

	// Debugging
	public static final boolean NUTS_PRINT_EMPTY = Boolean.FALSE;

	// Main (pre-defined) arguments to use
	public static final String[] DEV_ARGS_ALL_DL = new String[] { "dl" };
	public static final String[] DEV_ARGS_ALL_CSV = new String[] { "csv" };
	public static final String[] DEV_ARGS_ALL_STAT = new String[] { "stat" };
	public static final String[] DEV_ARGS_ALL_HELP = new String[] { "help" };
	public static final String[] DEV_ARGS_ALL_DEV = new String[] { "dev" };
	public static final String[] DEV_ARGS_CUSTOM = new String[] { "-ids", "nuts-2016-2021 lau2021-nuts2021", "dev" };
	public static final String[] DEV_ARGS = DEV_ARGS_CUSTOM;

	/**
	 * Called if {@link Main} mode is "dev".
	 * 
	 * @param ids Source IDs configured in {@link Main}.
	 */
	public static void dev(List<String> ids) {
		System.err.println("Development execution");
		try {
			for (Source source : new Sources().getSources()) {
				if (ids.contains(source.id)) {
					if (source.sourceType.equals(SourceType.NUTS)) {
						handleNuts(source);
					} else if (source.sourceType.equals(SourceType.LAU)) {
						handleLau(source);
					} else if (source.sourceType.equals(SourceType.NUTSRDF)) {
						handleNutsRdf(source);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void handleNuts(Source source) throws Exception {
		if (Boolean.TRUE)
			return;

		File file = new SourceCsvSheets(source).getNutsMainSheetFile();
		NutsCsvCollection nutsCsvCollection = new NutsCsvParser(file, source.id).parse();

		// Print sizes
		if (Boolean.TRUE)
			System.out.println(nutsCsvCollection);

		// Print values
		if (Boolean.FALSE)
			System.out.println(nutsCsvCollection.getValues(true));

		// Print values as table
		if (Boolean.FALSE)
			System.out.println(nutsCsvCollection.getMarkdownTable());
	}

	private static void handleLau(Source source) throws Exception {
		if (Boolean.FALSE)
			return;

		for (File file : new SourceCsvSheets(source).getLauSheetFiles()) {
			LauCsvParser parser = new LauCsvParser(file, source.id);

			// Print headings
			if (Boolean.FALSE)
				System.out.println(source.id + " " + parser.searchHeadingsRow());

			// Print item sizes
			if (Boolean.TRUE) {
				System.out.println(parser.getCountryCode() + " " + parser.parse().lauCsvItems.size());
			}
		}
	}

	private static void handleNutsRdf(Source source) throws Exception {
		if (Boolean.TRUE)
			return;

		// Print stats of Eurostat KG
		new NutsRdfReader().printStats();
	}

	// Notes

	// There was a LAU2 Code

	// area sometimes given in m2 and sometime km2? e.g. lau2020

	// nuts-2013-2016.csv: Duplicate 2013 code 'FR7'. Checked:
	// 'AUVERGNE-RHÔNE-ALPES' should probably not be 'FR7'.
}