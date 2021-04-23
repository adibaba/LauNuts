package org.dice_research.launuts;

import java.util.Map;
import java.util.Map.Entry;

import org.dice_research.launuts.csv.NutsCsv;
import org.dice_research.launuts.csv.NutsCsvIndex;
import org.dice_research.launuts.rdf.NutsRdfReader;

/**
 * Development.
 * 
 * TODO following:
 *
 * Check in xlsx ranges, which code (old/new) has to be used. FR7 in 2013-16.
 * Validation: By RDF.
 *
 * There are duplicate coes in nuts csv, see NutsCsvTest verbose
 *
 * Dynamic config for CLI / Webservices
 * 
 * Source files as resources
 *
 * @author Adrian Wilke
 */
public class Main {

	Map<String, NutsCsv> nutsCsvIndex;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Main main = new Main();

		if (true)
			main.dev();

		if (false)
			main.csvRdf();
	}

	@SuppressWarnings("unused")
	private void dev() {

		NutsRdfReader nutsRdfReader = new NutsRdfReader().read();

		if (false) {
			for (String uri : nutsRdfReader.getAllResourceUris()) {
				System.out.println(uri);
			}
			System.out.println();
		}

		if (false) {
			for (String uri : nutsRdfReader.getAllPredicateUris()) {
				System.out.println(uri);
			}
			System.out.println();
		}

		if (true) {
			for (String uri : nutsRdfReader.getResourceUrisInSchemeAndLevel(2016, 2)) {
				System.out.println(uri);
			}
			System.out.println();
		}
	}

	@SuppressWarnings("unused")
	private void csvRdf() {

		// Load
		nutsCsvIndex = new NutsCsvIndex().get();

		// Print index
		if (true) {
			for (Entry<String, NutsCsv> idToNutsCsv : nutsCsvIndex.entrySet()) {
				System.out.println("ID:       " + idToNutsCsv.getKey());
				System.out.println("NutsCsv:  " + idToNutsCsv.getValue());
			}
			System.out.println();
		}

		// Print important data
		if (true) {
			String id = "NUTS-2021";
			System.out.println(nutsCsvIndex.get(id).getDataString());
		}

		// Print data
		if (false) {
			String id = "NUTS-2021";
			System.out.println(nutsCsvIndex.get(id).getDataSourceString(" | "));
		}
	}

}