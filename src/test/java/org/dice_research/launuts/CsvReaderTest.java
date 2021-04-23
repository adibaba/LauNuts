package org.dice_research.launuts;

import java.io.File;

import org.dice_research.launuts.csv.CsvReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link CsvReader}.
 *
 * @author Adrian Wilke
 */
class CsvReaderTest {

	@Test
	void test() {
		File file = new File("src/test/resources", "test.csv");
		CsvReader csvReader = new CsvReader(file).read();
		Assertions.assertEquals(3, csvReader.getRowSize());
		Assertions.assertEquals(3, csvReader.getColumnSize());
		Assertions.assertEquals("C 3, space", csvReader.getValue(2, 2));
	}

	@Test
	void testNoClean() {
		File file = new File("src/test/resources", "test.csv");
		CsvReader csvReader = new CsvReader(file).setTrimValues(false).setTrimMatrix(false).read();
		Assertions.assertEquals(4, csvReader.getRowSize());
		Assertions.assertEquals(4, csvReader.getColumnSize());
		Assertions.assertEquals(" C 3, space ", csvReader.getValue(2, 2));
	}

	@Test
	void testOneValue() {
		File file = new File("src/test/resources", "test-a1.csv");
		CsvReader csvReader = new CsvReader(file).read();
		Assertions.assertEquals(1, csvReader.getRowSize());
		Assertions.assertEquals(1, csvReader.getColumnSize());
		Assertions.assertEquals("A1", csvReader.getValue(0, 0));
	}

	@Test
	void testEmpty() {
		File file = new File("src/test/resources", "test-empty.csv");
		CsvReader csvReader = new CsvReader(file).read();
		Assertions.assertEquals(0, csvReader.getRowSize());
		Assertions.assertEquals(0, csvReader.getColumnSize());
	}

}
