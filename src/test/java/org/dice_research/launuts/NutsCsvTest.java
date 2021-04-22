package org.dice_research.launuts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.dice_research.launuts.csv.NutsCsv;
import org.dice_research.launuts.csv.NutsCsvIndex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NutsCsvTest {

	Map<String, NutsCsv> index;

	@BeforeEach
	void setUp() throws Exception {
		index = new NutsCsvIndex().get();
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
	void testSizes() {
		// TODO: Get list of exprected sizes
		Assumptions.assumeTrue(false);

		Map<String, Integer> counter = new HashMap<>();
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
			System.out.println(entry);
		}
	}
}