package org.dice_research.launuts;

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

		// Parse without exceptions
		List<Source> sources = new Sources().getSources();
		Assertions.assertTrue(sources.size() >= 7 + 14 + 1);

		// For humans
		if (Boolean.FALSE) {
			for (Source source : sources) {
				System.out.println(source);
			}
		}
		if (Boolean.FALSE) {
			for (Source source : sources) {
				System.out.println(source.id);
				System.out.println(source.fileType);
				System.out.println(source.sourceType);
				System.out.println(source.sources);
				System.out.println();
			}
		}
	}
}