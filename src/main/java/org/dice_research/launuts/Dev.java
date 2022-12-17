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
	public static final boolean DEV = Boolean.FALSE;

	// Mode constant for {@link Main} CLI
	public static final String MODE = "dev";

	// Debugging inside other classes
	public static final boolean NUTS_PRINT_EMPTY = Boolean.FALSE;

	// Execute single methods in this class
	public static final boolean HANDLE_NUTS = Boolean.FALSE;
	public static final boolean HANDLE_LAU = Boolean.FALSE;
	public static final boolean HANDLE_NUTSRDF = Boolean.FALSE;

	// Main (pre-defined) arguments to use
	public static final String[] DEV_ARGS_ALL_LS = new String[] { Main.MODE_LIST };
	public static final String[] DEV_ARGS_ALL_DL = new String[] { Main.MODE_DOWNLOAD };
	public static final String[] DEV_ARGS_ALL_CSV = new String[] { Main.MODE_CSV };
	public static final String[] DEV_ARGS_ALL_CT = new String[] { Main.MODE_COUNTRIES };
	public static final String[] DEV_ARGS_ALL_HELP = new String[] { Main.MODE_HELP };
	public static final String[] DEV_ARGS_ALL_DEV = new String[] { Dev.MODE };
	// Source IDs:
	// nuts-2016-2021, nuts-2013-2016, nuts-2010-2013, nuts-2006-2010,
	// nuts-2003-2006, nuts-1999-2003, nuts-1995-1999,
	// lau2021-nuts2021, lau2020-nuts2016, lau2019-nuts2016, lau2018-nuts2016,
	// lau2017-nuts2016, lau2017-nuts2013, lau2016, lau2015, lau2014, lau2013,
	// lau2012, lau2011-census, lau2011, lau2010, nuts
	public static final String[] DEV_ARGS_CUSTOM = new String[] //
	{ "-" + Main.OPTION_IDS, "nuts-2016-2021 lau2021-nuts2021", "kg" };
	public static final String[] DEV_ARGS_CUSTOM_2 = new String[] //
	{ "-" + Main.OPTION_IDS, "nuts-2016-2021 lau2021-nuts2021", "-" + Main.OPTION_COUNTRIES, "DE", "kg" };
	// Set final development arguments
	public static final String[] DEV_ARGS = DEV_ARGS_CUSTOM;

	/**
	 * Called if {@link Main} mode is "dev".
	 * 
	 * @param ids Source IDs configured in {@link Main}.
	 */
	public static void dev(List<String> ids, List<String> countyCodes) {
		System.err.println("Development execution");
		try {
			for (Source source : new Sources().getSources()) {
				if (ids.contains(source.id)) {
					if (source.sourceType.equals(SourceType.NUTS)) {
						if (HANDLE_NUTS)
							handleNuts(source);
					} else if (source.sourceType.equals(SourceType.LAU)) {
						if (HANDLE_LAU)
							handleLau(source, countyCodes);
					} else if (source.sourceType.equals(SourceType.NUTSRDF)) {
						if (HANDLE_NUTSRDF)
							handleNutsRdf(source);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void handleNuts(Source source) throws Exception {
		File file = new SourceCsvSheets(source).getNutsMainSheetFile();
		NutsCsvCollection nutsCsvCollection = new NutsCsvParser(file, source).parse();

		// Print sizes
		if (Boolean.FALSE)
			System.out.println(nutsCsvCollection);

		// Print values
		if (Boolean.FALSE)
			System.out.println(nutsCsvCollection.getValues(true));

		// Print values as table
		if (Boolean.FALSE)
			System.out.println(nutsCsvCollection.getMarkdownTable());
	}

	private static void handleLau(Source source, List<String> countyCodes) throws Exception {
		for (File file : new SourceCsvSheets(source).getLauSheetFiles()) {
			LauCsvParser parser = new LauCsvParser(file, source);

			// Print headings
			if (Boolean.FALSE)
				System.out.println(source.id + " " + file.getName() + " " + parser.searchHeadingsRow());

			// Print item sizes
			if (Boolean.FALSE) {
				if (countyCodes.isEmpty() || countyCodes.contains(SourceCsvSheets.getLauCountryCode(file)))
					System.out.println(parser.getCountryCode() + " " + parser.parse().lauCsvItems.size());
			}

			// Print values
			if (Boolean.FALSE)
				if (countyCodes.isEmpty() || countyCodes.contains(SourceCsvSheets.getLauCountryCode(file)))
					System.out.println(parser.parse().getValues(true));

			// Print values as table
			if (Boolean.FALSE)
				if (countyCodes.isEmpty() || countyCodes.contains(SourceCsvSheets.getLauCountryCode(file)))
					System.out.println(parser.parse().getMarkdownTable());
		}
	}

	private static void handleNutsRdf(Source source) throws Exception {
		// Print stats of Eurostat KG
		new NutsRdfReader().printStats();
	}
}