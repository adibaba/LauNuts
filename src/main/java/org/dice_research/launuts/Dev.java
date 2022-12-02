package org.dice_research.launuts;

import java.io.File;
import java.util.List;

import org.dice_research.launuts.csv.NutsCsvCollection;
import org.dice_research.launuts.csv.NutsCsvParser;
import org.dice_research.launuts.sources.Source;
import org.dice_research.launuts.sources.SourceSheets;
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
			for (Source source : new Sources().getSources()) {
				if (ids.contains(source.id)) {

					// TODO: Extract values
					if (source.sourceType.equals(SourceType.NUTS)) {
						File file = new SourceSheets(source).getNutsMainSheetFile();

						NutsCsvCollection nutsCsvCollection = new NutsCsvParser(file, source.id).parse();
						System.out.println(nutsCsvCollection);
						System.out.println(nutsCsvCollection.getValues(true));
					}

				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}