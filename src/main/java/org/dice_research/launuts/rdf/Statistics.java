package org.dice_research.launuts.rdf;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dice_research.launuts.csv.LauCsvItem;
import org.dice_research.launuts.csv.NutsCsvItem;

/**
 * Counts concepts added to RDF.
 * 
 * @author Adrian Wilke
 */
public class Statistics {

	public int lau = 0;
	public int area = 0;
	public int population = 0;

	public Map<Integer, Integer> nuts = new HashMap<>();

	public int linked = 0;

	public Statistics() {
		nuts.put(0, 0);
		nuts.put(1, 0);
		nuts.put(2, 0);
		nuts.put(3, 0);
	}

	public void countLauCsvItem(LauCsvItem lauCsvItem) {
		lau++;
		if (lauCsvItem.hasArea())
			area++;
		if (lauCsvItem.hasPopulation())
			population++;
	}

	public void countNutsCsvItem(NutsCsvItem nutsCsvItem) {
		nuts.replace(nutsCsvItem.getLevel(), nuts.get(nutsCsvItem.getLevel()) + 1);

		if (Boolean.FALSE && nutsCsvItem.getLevel() == 0)
			System.out.println(nutsCsvItem.nutsCode);
	}

	public void countLinked() {
		linked++;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("LAU:        " + lau + "\n");
		sb.append("Area:       " + area + "\n");
		sb.append("Population: " + population + "\n");
		for (Entry<Integer, Integer> entry : nuts.entrySet()) {
			sb.append("NUTS " + entry.getKey() + ": " + entry.getValue() + "\n");
		}
		sb.append("Linked:     " + linked + "\n");
		return sb.toString();
	}
}