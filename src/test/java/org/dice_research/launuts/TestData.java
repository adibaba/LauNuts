package org.dice_research.launuts;

import org.dice_research.launuts.csv.NutsCsvIndex;
import org.dice_research.launuts.rdf.NutsRdfReader;

/**
 * Test data.
 *
 * @author Adrian Wilke
 */
public class TestData {
	public static NutsCsvIndex nutsCsvIndex = new NutsCsvIndex().create();
	public static NutsRdfReader nutsRdfReader = new NutsRdfReader().read();
}