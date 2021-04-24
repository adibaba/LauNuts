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
 * Source files as resources
 *
 * Dynamic config for CLI / Webservices
 *
 * @author Adrian Wilke
 */
public class Main {

	Map<String, NutsCsv> nutsCsvIndex;
	NutsRdfReader nutsRdfReader;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Main main = new Main();
		main.nutsCsvIndex = new NutsCsvIndex().get();
		main.nutsRdfReader = new NutsRdfReader().read();

		if (true)
			main.dev();

		if (true)
			main.csvNuts();

		if (false)
			main.rdfNuts();
	}

	private void dev() {
	}

	@SuppressWarnings("unused")
	private void csvNutsPrintIndex() {
		for (Entry<String, NutsCsv> idToNutsCsv : nutsCsvIndex.entrySet()) {
			System.out.println("ID:       " + idToNutsCsv.getKey());
			System.out.println("NutsCsv:  " + idToNutsCsv.getValue());
		}
		System.out.println();
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

	@SuppressWarnings("unused")
	private void rdfNuts() {

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

}