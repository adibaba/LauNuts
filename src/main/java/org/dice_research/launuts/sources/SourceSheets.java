package org.dice_research.launuts.sources;

import java.io.File;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.dice_research.launuts.io.Io;

/**
 * Methods to access single sheets already available as CSV data.
 * 
 * @author Adrian Wilke
 */
public class SourceSheets {

	private Source source;

	public SourceSheets(Source source) {
		this.source = source;
	}

	/**
	 * Searches in CSV directory for main NUTS file and returns it.
	 * 
	 * For 1995 to 2001 data, main NUTS file names start with "NUTS".
	 */
	public File getNutsMainSheetFile() {
		if (source.sourceType.equals(SourceType.NUTS)) {
			// Collect all CSV files starting with NUTS
			List<File> nutsFiles = new LinkedList<>();
			for (URI fileUri : Io.listDirectory(source.getCsvDirectory().getAbsolutePath())) {
				File file = new File(fileUri);
				if (file.getName().startsWith("NUTS")) {
					nutsFiles.add(file);
				}
			}
			// Return unique file or throw Exception
			if (nutsFiles.size() == 1) {
				return nutsFiles.get(0);
			} else {
				throw new RuntimeException("Did not find an unique NUTS CSV file: " + nutsFiles);
			}
		} else
			throw new RuntimeException("Not a NUTS source: " + source.id);
	}

	/**
	 * Searches in CSV directory for LAU files and returns them.
	 * 
	 * A typical filename is "DE.csv". Some files without main content consist of
	 * 434 Byte (e.g. in lau2020-nuts2016) or less.
	 */
	public List<File> getLauSheetFiles() {
		if (source.sourceType.equals(SourceType.LAU)) {
			List<File> lauFiles = new LinkedList<>();
			for (URI fileUri : Io.listDirectory(source.getCsvDirectory().getAbsolutePath())) {
				File file = new File(fileUri);
				if (file.getName().length() == 6 && file.length() > 450) {
					lauFiles.add(file);
				}
			}
			return lauFiles;
		} else
			throw new RuntimeException("Not a LAU source: " + source.id);
	}

}