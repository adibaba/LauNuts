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
 * LauNuts - Command Line Interface.
 *
 * @author Adrian Wilke
 */
public class Main {

	public static final String MODE_LIST = "ls";
	public static final String MODE_DOWNLOAD = "dl";
	public static final String MODE_CSV = "csv";
	public static final String MODE_HELP = "help";

	public static StringBuilder helpTextBuilder;
	public static Map<String, String> modes;

	private NutsCsvIndex nutsCsvIndex;

	public static void main(String[] args) throws IOException {
		if (Dev.DEV)
			args = Dev.DEV_ARGS;

		// File check
		File configurationFile = new File(Config.CONFIGURATION_FILE);
		if (!configurationFile.canRead()) {
			System.err.println("Error: Can not read file " + configurationFile.getAbsolutePath());
			return;
		}
		File sourcesFile = new File(Config.get(Config.KEY_SOURCES_FILE));
		if (!sourcesFile.canRead()) {
			System.err.println("Error: Can not read file " + sourcesFile.getAbsolutePath());
			return;
		}

		// Defaults
		String mode = MODE_HELP;
		List<String> ids = new Sources().getSourceIds();

		// List of all available modes
		modes = new LinkedHashMap<>();
		modes.put(MODE_LIST, "  Lists available dataset IDs");
		modes.put(MODE_DOWNLOAD, "  Downloads dataset sources");
		modes.put(MODE_CSV, " Converts Excel datasets to CSV");
		modes.put(MODE_HELP, "Print this help");

		// Build help text
		helpTextBuilder = new StringBuilder().append("\n" + "Modes:" + "\n");
		for (Entry<String, String> entry : modes.entrySet()) {
			helpTextBuilder.append("  " + entry.getKey() + ": " + entry.getValue() + "\n");
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

		// Print configuration
		if (Dev.DEV) {
			StringBuilder sb = new StringBuilder();
			sb.append("DEV").append("\n");
			sb.append("Mode: " + mode).append("\n");
			sb.append("IDs:  " + ids).append("\n");
			System.err.print(sb.toString());
		}

		// Run: Print help
		if (mode.equals(MODE_HELP)) {
			HelpFormatter helpFormatter = new HelpFormatter();
			StringBuilder sb = new StringBuilder();
			sb.append("Usage:   COMMAND [OPTIONS] MODE\n");
			sb.append("Example: java -jar launuts.jar -ids \"nuts-2016-2021 lau2021-nuts2021\" dl");
			helpFormatter.setSyntaxPrefix(sb.toString());
			helpFormatter.printHelp(helpTextBuilder.toString(), options);
		}

		// Run: Print IDs
		else if (mode.equals(MODE_LIST)) {
			System.out.println("Available dataset IDs:");
			for (String sourceId : new Sources().getSourceIds()) {
				System.out.println(sourceId);
			}
		}

		// Run: Download datasets
		else if (mode.equals(MODE_DOWNLOAD)) {
			for (Source source : new Sources().getSources()) {
				if (ids.contains(source.id))
					source.download();
			}
		}

		// Run: Convert to CSV
		else if (mode.equals(MODE_CSV)) {
			Converter converter = new Converter();
			boolean libreoffice = converter.isLibreofficeInstalled();
			boolean xlsx2csv = converter.isIn2csvInstalled();
			for (Source source : new Sources().getSources()) {
				if (ids.contains(source.id)) {
					// Convert XLS -> XLSX
					if (source.fileType.equals(Sources.FILETYPE_XLS)) {
						if (!libreoffice) {
							System.out.println("Skipping XLS convertion as LibreOffice not found: " + source.id);
						} else {
							converter.convertXls(source);
						}
					}
					// Convert XLSX -> CSV
					if (!xlsx2csv) {
						System.out.println("Skipping XLS convertion as csvIn2csv not found: " + source.id);
					} else {
						converter.convertCsvIn2csv(source, Dev.DEV_SKIP_SHEET_CREATION);
					}
				}
			}
		}
	}

	// --------------------- old code to refactor/integrate -------------

	private void dev() throws IOException {
		// TODO
		// new LauCsvIndex().create().tmpReadData();
	}

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