package org.dice_research.launuts;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

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
	NutsRdfReader nutsRdfReader;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Main main = new Main();
		main.nutsCsvIndex = new NutsCsvIndex().get();
		main.nutsRdfReader = new NutsRdfReader().read();

		if (false)
			main.dev();

		if (true)
			main.rdfNuts();

		if (false)
			main.csvNuts();
	}

	private void dev() {
		csvNutsPrintIndex();

//		int rdfId = 2010;
//		String csvId = "NUTS-2010-2013";
		int rdfId = 2013;
		String csvId = "NUTS-2013-2016";

		Set<String> nutsCodesRdf = new TreeSet<>();
		for (String uri : nutsRdfReader.getResourceUrisInScheme(rdfId)) {
			nutsCodesRdf.add(uri.substring(uri.lastIndexOf('/') + 1));
		}

		Set<String> nutsCodesCsv = new TreeSet<>();
		NutsCsv nutsCsv = nutsCsvIndex.get(csvId);
		Iterator<Integer> it = nutsCsv.iterator();
		while (it.hasNext()) {
			nutsCodesCsv.add(nutsCsv.getCode(it.next()));
		}

		Set<String> nutsCodesRdfCheck = new TreeSet<>(nutsCodesRdf);
		nutsCodesRdfCheck.removeAll(nutsCodesCsv);
		Set<String> nutsCodesCsvCheck = new TreeSet<>(nutsCodesCsv);
		nutsCodesCsvCheck.removeAll(nutsCodesRdf);

		System.out.println(nutsCodesRdfCheck);
		System.out.println(nutsCodesCsvCheck);
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
		if (false) {
			String id = "NUTS-2021-EXTRA";
			System.out.println(nutsCsvIndex.get(id).getDataString());
		}

		// Print data
		if (true) {
			String id = "NUTS-2021-EXTRA";
			System.out.println(nutsCsvIndex.get(id).getDataSourceString(" | "));
		}
	}

}