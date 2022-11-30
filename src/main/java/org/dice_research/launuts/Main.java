package org.dice_research.launuts;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.dice_research.launuts.csv.NutsCsv;
import org.dice_research.launuts.csv.NutsCsvIndex;
import org.dice_research.launuts.io.Converter;
import org.dice_research.launuts.rdf.NutsRdfReader;
import org.dice_research.launuts.sources.Source;
import org.dice_research.launuts.sources.Sources;

/**
 * Command Line Interface.
 * 
 * TODO: Test with compiled version
 *
 * @author Adrian Wilke
 */
public class Main {

	// Development mode to set arguments inside code
	private static final boolean DEV = false;
	private static final String[] DEV_ARGS = new String[] { "-ids", "nuts-2016-2021 nuts-2013-2016", "list" };

	public static final String MODE_LIST = "list";
	public static final String MODE_HELP = "help";

	public static StringBuilder helpTextBuilder;
	public static Map<String, String> modes;

	private NutsCsvIndex nutsCsvIndex;

	public static void main(String[] args) throws IOException {
		if (DEV)
			args = DEV_ARGS;

		// Defaults
		String mode = MODE_HELP;
		List<String> ids = new Sources().getSourceIds();

		// List of all available modes
		modes = new LinkedHashMap<>();
		modes.put(MODE_LIST, "Lists available dataset IDs");
		modes.put(MODE_HELP, "Print this help");

		// Build help text
		helpTextBuilder = new StringBuilder().append("\n" + "Modes:" + "\n");
		for (Entry<String, String> entry : modes.entrySet()) {
			helpTextBuilder.append(" -" + entry.getKey() + ": " + entry.getValue() + "\n");
		}
		helpTextBuilder.append("Options:\n");

		// Parser with options
		Options options = new Options()
				.addOption(Option.builder("ids").hasArg(true).argName("\"ID-1 ID-2 ...\"").build());
		DefaultParser parser = new DefaultParser();

		// Parse arguments and options
		try {
			CommandLine commandLine = parser.parse(options, args);

			// Set mode
			List<String> arguments = Arrays.asList(commandLine.getArgs());
			if (arguments.size() > 1)
				throw new ParseException("Error: Multiple modes given:" + arguments);
			else if (arguments.size() == 1) {
				mode = arguments.get(0);
				if (!modes.containsKey(mode))
					throw new ParseException("Error: Unknown mode: " + mode);
			}

			// Set IDs
			if (commandLine.hasOption("ids")) {
				List<String> newIds = Arrays.asList(commandLine.getOptionValue("ids").split(" "));
				for (String newId : newIds) {
					if (!ids.contains(newId)) {
						throw new ParseException("Error: Unknown ID: " + newId);
					}
				}
				ids = newIds;
			}
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			mode = MODE_HELP;
		}

		if (DEV) {
			StringBuilder sb = new StringBuilder();
			sb.append("DEV").append("\n");
			sb.append("Mode: " + mode).append("\n");
			sb.append("IDs:  " + ids).append("\n");
			System.err.println(sb.toString());
		}

		// Run: Print help
		if (mode.equals(MODE_HELP)) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.setSyntaxPrefix("Usage: COMMAND [OPTIONS] MODE");
			helpFormatter.printHelp(helpTextBuilder.toString(), options);
		}

		// Run: Set IDs
		else if (mode.equals(MODE_LIST)) {
			System.out.println("Available dataset IDs:");
			for (String sourceId : new Sources().getSourceIds()) {
				System.out.println(sourceId);
			}
		}

//		Main main = new Main();
//		main.nutsCsvIndex = new NutsCsvIndex().create();
//
//		if (DEV) {
//			System.err.println("Development mode");
//
//			if (true)
//				main.dev();
//
//			if (false)
//				main.csvNuts();
//
//			if (false)
//				main.rdfNuts();
//
//		} else {
//			main.defaultMain();
//		}
	}

	private void dev() throws IOException {
		// TODO
		// new LauCsvIndex().create().tmpReadData();
		// download();
		convert();
	}

	/**
	 * TODO new :)
	 * 
	 * Downloads all files listed in sources.json, if not already existing.
	 */
	private void download() {
		try {
			for (Source source : new Sources().parseJsonFile(new File(Config.get(Config.KEY_SOURCES_FILE)))) {
				source.download();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * TODO new :)
	 */
	private void convert() throws IOException {
		Converter converter = new Converter();
		if (!converter.isLibreofficeInstalled()) {
			System.out.println("LibreOffice not installed or found. Skipping XLS TO XLSX converion.");
		} else {
			converter.convertXls();
		}

		if (!converter.isIn2csvInstalled()) {
			System.out.println("xlsx2csv not installed or found. Skipping XLSX to CSV converion.");
		} else {
			converter.convertCsvIn2csv();
		}
	}

	// ---------------------------- old code to refactor/integrate
	// ------------------------------

	@SuppressWarnings("unused")
	private void csvNuts() {

		// Print important data
		if (true) {
			String id = "2021";
			System.out.println(nutsCsvIndex.get(id).getDataString());
		}

		// Print data
		if (false) {
			String id = "2021";
			System.out.println(nutsCsvIndex.get(id).getDataSourceString(" | "));
		}
	}

	private void rdfNuts() {
		NutsRdfReader nutsRdfReader = new NutsRdfReader().read();

		int maxIndex = 3;
		int i;

		if (true) {
			i = 0;
			for (String uri : nutsRdfReader.getAllResourceUris()) {
				if (i++ > maxIndex)
					break;
				System.out.println(uri);
			}
			System.out.println();
		}

		if (true) {
			i = 0;
			for (String uri : nutsRdfReader.getAllPredicateUris()) {
				if (i++ > maxIndex)
					break;
				System.out.println(uri);
			}
			System.out.println();
		}

		if (true) {
			i = 0;
			for (String uri : nutsRdfReader.getResourceUrisInSchemeAndLevel(2016, 2)) {
				if (i++ > maxIndex)
					break;
				System.out.println(uri);
			}
			System.out.println();
		}
	}

	private void defaultMain() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Entry<String, NutsCsv> entry : nutsCsvIndex.getEntries()) {
			stringBuilder.append(entry.getKey());
			stringBuilder.append(System.lineSeparator());
			stringBuilder.append(entry.getValue().getDataString());
			stringBuilder.append(System.lineSeparator());
		}
		System.out.println(stringBuilder.toString());
	}

}