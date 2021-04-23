package org.dice_research.launuts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.dice_research.launuts.csv.NutsCsv;
import org.dice_research.launuts.csv.NutsCsvIndex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link NutsCsv} and {@link NutsCsvIndex}.
 *
 * @author Adrian Wilke
 */
class NutsCsvTest {

	// Print if duplicate codes found
	public static boolean verbose = false;

	Map<String, NutsCsv> index;
	Map<String, Integer> sizes;

	@BeforeEach
	void setUp() throws Exception {
		if (verbose == true) {
			System.err.println("Default value not set: setUp " + getClass().getSimpleName());
		}

		// Load data
		index = new NutsCsvIndex().get();

		// Values from xlsx files
		sizes = new TreeMap<>();
		sizes.put("NUTS-1995-1999", 1329);
		sizes.put("NUTS-1999-2003", 1397);
		sizes.put("NUTS-2003-2006", 1752);
		sizes.put("NUTS-2006-2010", 1779);
		sizes.put("NUTS-2010-2013", 1797);
		sizes.put("NUTS-2013-2016", 1828); // Note: FR7 exists twice in source
		sizes.put("NUTS-2021", 1844);
		sizes.put("NUTS-2021-EXTRA", 277);
	}

	@Test
	private void testIndexSize() {
		// Number of files + 1
		Assertions.assertEquals(8, index.size());
	}

	@Test
	void testEmptyValues() {
		for (NutsCsv nutsCsv : index.values()) {
			Iterator<Integer> it = nutsCsv.iterator();
			while (it.hasNext()) {
				Integer row = it.next();
				String message = nutsCsv + row.toString();
				Assertions.assertFalse(nutsCsv.getName(row).isEmpty(), message);
				Assertions.assertFalse(nutsCsv.getLevel(row).isEmpty(), message);
				Assertions.assertFalse(nutsCsv.getCode(row).isEmpty(), message);
			}
		}
	}

	@Test
	void testDuplicates() {

		// true: fail on duplicates (default) ; false: print on duplicates
		boolean assertion = true;
		if (assertion == false) {
			System.err.println("Default value not set: testDuplicates " + getClass().getSimpleName());
		}

		List<String> codes;
		List<String> codesLevels;
		for (Entry<String, NutsCsv> entry : index.entrySet()) {
			codes = new LinkedList<>();
			codesLevels = new LinkedList<>();
			NutsCsv nutsCsv = entry.getValue();
			Iterator<Integer> it = nutsCsv.iterator();
			while (it.hasNext()) {
				Integer row = it.next();
				String code = nutsCsv.getCode(row);
				String level = nutsCsv.getLevel(row);
				String key = code + " " + level;

				// Duplicate: Same code and level
				if (assertion) {
					Assertions.assertFalse(codesLevels.contains(key), nutsCsv.toString() + " " + code);
				} else {
					if (codesLevels.contains(key))
						System.out.println("Duplicate: " + nutsCsv.toString() + " " + code);
				}

				// Print duplicate codes
				if (verbose && code.contains(code)) {
					System.out.println("Note: Code exists multiple times (different levels): " + nutsCsv + " " + code
							+ " " + level + " " + getClass().getSimpleName());
				}

				codes.add(code);
				codesLevels.add(key);
			}
		}
	}

	@Test
	void testSizes() {
		Map<String, Integer> counter = new TreeMap<>();
		for (Entry<String, NutsCsv> entry : index.entrySet()) {
			NutsCsv nutsCsv = entry.getValue();
			Iterator<Integer> it = nutsCsv.iterator();
			int i = 0;
			while (it.hasNext()) {
				it.next();
				i++;
			}
			counter.put(entry.getKey(), i);
		}
		for (Entry<String, Integer> entry : counter.entrySet()) {
			checkSize(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Used by {@link #testSizes()}.
	 */
	private void checkSize(String key, int value) {
		// true: fail test (default) ; false: skip test
		boolean assertion = true;
		if (assertion == false) {
			System.err.println("Default value not set: checkSize " + getClass().getSimpleName());
		}

		// Check if expected value is configured
		if (assertion) {
			Assertions.assertTrue(sizes.containsKey(key), "NUTS CSV contained: " + key);
		} else {
			Assumptions.assumeTrue(sizes.containsKey(key), "NUTS CSV contained: " + key);
		}

		// Check size itself
		if (sizes.containsKey(key)) {
			Assertions.assertEquals(sizes.get(key), value, "NUTS CSV size: " + key);
		}
	}

}