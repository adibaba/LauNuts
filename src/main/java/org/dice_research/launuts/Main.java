package org.dice_research.launuts;

import java.util.Map.Entry;

import org.dice_research.launuts.csv.LauCsvIndex;
import org.dice_research.launuts.csv.NutsCsv;
import org.dice_research.launuts.csv.NutsCsvIndex;
import org.dice_research.launuts.rdf.NutsRdfReader;

/**
 * Development.
 * 
 * TODO following:
 * 
 * Check LAU CSV data
 * 
 * Dynamic config for CLI / Webservices
 *
 * @author Adrian Wilke
 */
public class Main {

	// TODO
	public static final boolean DEV = false;

	private NutsCsvIndex nutsCsvIndex;

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		Main main = new Main();
		main.nutsCsvIndex = new NutsCsvIndex().create();

		if (DEV) {
			System.err.println("Development mode");

			if (true)
				main.dev();

			if (false)
				main.csvNuts();

			if (false)
				main.rdfNuts();

		} else {
			main.defaultMain();
		}
	}

	private void dev() {
		// TODO
		new LauCsvIndex().create().tmpReadData();
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