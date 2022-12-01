package org.dice_research.launuts;

import org.dice_research.launuts.csv.OldNutsCsvIndex;
import org.dice_research.launuts.rdf.NutsRdfReader;

/**
 * Test data.
 *
 * @author Adrian Wilke
 */
public class TestData {
	public static OldNutsCsvIndex nutsCsvIndex = new OldNutsCsvIndex().create();
	public static NutsRdfReader nutsRdfReader = new NutsRdfReader().read();
}