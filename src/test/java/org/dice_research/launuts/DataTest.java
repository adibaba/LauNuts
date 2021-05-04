package org.dice_research.launuts;

import java.io.File;

import org.dice_research.launuts.exceptions.IoRuntimeException;
import org.junit.jupiter.api.Test;

/**
 * Checks data used in {@link TestData} and set in {@link Configuration}.
 *
 * @author Adrian Wilke
 */
class DataTest {

	@Test
	void testFileRdfNuts() {
		File fileRdfNuts = new File(Configuration.FILE_RDF_NUTS);
		if (!fileRdfNuts.canRead()) {
			throw new IoRuntimeException("Could not read " + fileRdfNuts.getAbsolutePath());
		}
	}

	@Test
	void testDirCsvNuts() {
		File dirCsvNuts = new File(Configuration.DIRECTORY_CSV_NUTS);
		if (!dirCsvNuts.canRead()) {
			throw new IoRuntimeException("Could not read " + dirCsvNuts.getAbsolutePath());
		}
	}

	@Test
	void testDirCsvLau() {
		File dirCsvLau = new File(Configuration.DIRECTORY_CSV_LAU);
		if (!dirCsvLau.canRead()) {
			throw new IoRuntimeException("Could not read " + dirCsvLau.getAbsolutePath());
		}
	}

}