package org.dice_research.launuts;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.dice_research.launuts.sources.Source;
import org.dice_research.launuts.sources.Sources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link Sources}.
 *
 * @author Adrian Wilke
 */
class SourcesTest {

	@Test
	void test() throws IOException {
		List<Source> sources = new Sources().parseJsonFile(new File(Sources.SOURCES_FILE));
		Assertions.assertTrue(sources.size() >= 6);

		// For humans
		if (Boolean.FALSE) {
			for (Source source : sources) {
				System.out.println(source);
			}
		}
	}
}