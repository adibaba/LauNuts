package org.dice_research.launuts;

import java.util.Map;

import org.dice_research.launuts.csv.NutsCsv;
import org.dice_research.launuts.csv.NutsCsvIndex;
import org.dice_research.launuts.rdf.NutsRdfReader;

/**
 * Test data.
 *
 * @author Adrian Wilke
 */
public class TestData {

	public static Map<String, NutsCsv> nutsCsvIndex = new NutsCsvIndex().get();

	public static NutsRdfReader nutsRdfReader = new NutsRdfReader().read();
}