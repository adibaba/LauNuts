package org.dice_research.launuts;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dice_research.launuts.csv.NutsCsv;
import org.dice_research.launuts.rdf.NutsRdfReader;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Compares values of {@link NutsCsv} and {@link NutsRdfReader}.
 *
 * @author Adrian Wilke
 */
class NutsCsvRdfTest {

	@Test
	void test() {
		Assert.assertTrue(getNotContained(2010, "2010", true).isEmpty());
		Assert.assertTrue(getNotContained(2010, "2010", false).isEmpty());

		Assert.assertTrue(getNotContained(2016, "2016", true).isEmpty());
		Assert.assertTrue(getNotContained(2016, "2016", false).isEmpty());

		Assert.assertTrue(getNotContained(2013, "2013", true).isEmpty());

		// GBP is in 2013 schema, has skos:broader UK, is deprecated and has no label.
		SortedSet<String> nutsCodes = getNotContained(2013, "2013", false);
		Assert.assertEquals(1, nutsCodes.size());
		Assert.assertEquals("GBP", nutsCodes.iterator().next());
	}

	/**
	 * Gets NUTS codes which are contained in one set, but not in the other.
	 * 
	 * @param rdfId 2010 | 2013 | 2016
	 * @param csvId IDs like in index
	 */
	private SortedSet<String> getNotContained(int rdfId, String csvId, boolean removeFromSecond) {

		Set<String> nutsCodesRdf = new HashSet<>();
		for (String uri : TestData.nutsRdfReader.getResourceUrisInScheme(rdfId)) {
			nutsCodesRdf.add(uri.substring(uri.lastIndexOf('/') + 1));
		}

		Set<String> nutsCodesCsv = new HashSet<>();
		NutsCsv nutsCsv = TestData.nutsCsvIndex.get(csvId);
		Iterator<Integer> it = nutsCsv.iterator();
		while (it.hasNext()) {
			nutsCodesCsv.add(nutsCsv.getCode(it.next()));
		}

		SortedSet<String> nuts;
		if (removeFromSecond) {
			nuts = new TreeSet<>(nutsCodesCsv);
			nuts.removeAll(nutsCodesRdf);
		} else {
			nuts = new TreeSet<>(nutsCodesRdf);
			nuts.removeAll(nutsCodesCsv);
		}
		return nuts;
	}

}