package org.dice_research.launuts.csv;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dice_research.launuts.Configuration;
import org.dice_research.launuts.io.Io;

/**
 * Index of NUTS CSV data. Configuration and parsing of CSV.
 *
 * @author Adrian Wilke
 */
public class OldNutsCsvIndex {

	private Map<String, OldNutsCsv> csvNutsIndex;

	public OldNutsCsvIndex create() {
		csvNutsIndex = new TreeMap<>();

		// resource-ID to resource-URL
		SortedMap<String, URL> resIndex = getFiles();

		// Read data
		// resource-ID to CsvReader
		SortedMap<String, OldCsvReader> csvReaderIndex = new TreeMap<>();
		for (Entry<String, URL> entry : resIndex.entrySet()) {
			csvReaderIndex.put(entry.getKey(), new OldCsvReader(entry.getValue()).read());
		}

		String id;
		OldNutsCsv nutsCsv;

		// Create one entry <ID, CsvReader> for every file
		// (Using column for old NUTS code)
		for (OldCsvReader csvReader : csvReaderIndex.values()) {
			id = createId(Io.urlToUri(csvReader.getUrl()));
			nutsCsv = new OldNutsCsv(id, csvReader);
			nutsCsv.setRowIndexHeadings(1).setDefaultDataRange();
			csvNutsIndex.put(id, nutsCsv);
		}

		// nuts-2013-2016.csv: Duplicate 2013 code 'FR7'. Checked:
		// 'AUVERGNE-RHÔNE-ALPES' should probably not be 'FR7'.
		// Solution 1: remove row
		// csvNutsIndex.get("2013").addRowToSkip(909);
		// Solution 2: use other file
		id = "2013";
		nutsCsv = new OldNutsCsv(id, csvReaderIndex.get("2010-2013"));
		nutsCsv.setUseCodeOld(false).setRowIndexHeadings(1).setDefaultDataRange();
		nutsCsv.columnIndexValueNotEmptyCheck = 11;
		csvNutsIndex.put(id, nutsCsv);

		// Add 2016
		id = "2016";
		nutsCsv = new OldNutsCsv(id, csvReaderIndex.get("2013-2016"));
		nutsCsv.setUseCodeOld(false).setRowIndexHeadings(1).setDefaultDataRange();
		nutsCsv.columnIndexValueNotEmptyCheck = 11;
		csvNutsIndex.put(id, nutsCsv);

		// NUTS CSV 2021 has another format and is split
		csvNutsIndex.get("2021").setRowIndexHeadings(0).setDefaultDataRange().setRowIndexDataEnd(1844);
		setIndexes2021(csvNutsIndex.get("2021"));

		// NUTS CSV 2021 has additional data
		id = "2021-EXTRA";
		nutsCsv = new OldNutsCsv(id, csvNutsIndex.get("2021").getCsvReader());
		nutsCsv.setRowIndexHeadings(0).setDefaultDataRange().setRowIndexDataBegin(1846);
		setIndexes2021(nutsCsv);
		csvNutsIndex.put(id, nutsCsv);

		return this;
	}

	private String createId(URI uri) {
		String noPath = uri.toString().substring(uri.toString().lastIndexOf('/'));
		int firstMinus = noPath.indexOf('-');
		return noPath.substring(firstMinus + 1, firstMinus + 1 + 4);
	}

	private void setIndexes2021(OldNutsCsv nutsCsv) {
		nutsCsv.columnIndexValueNotEmptyCheck = 5;
		nutsCsv.columnIndexCodeNew = -1;
		nutsCsv.columnIndexCodeOld = 0;
		nutsCsv.columnIndexNameCountry = 1;
		nutsCsv.columnIndexNameNuts1 = 2;
		nutsCsv.columnIndexNameNuts2 = 3;
		nutsCsv.columnIndexNameNuts3 = 4;
		nutsCsv.columnIndexLevel = 5;
		nutsCsv.columnIndexCountryOrder = 6;
		nutsCsv.columnIndexCodeOldOrder = 7;
		nutsCsv.columnIndexCodeNewOrder = -1;
	}

	private SortedMap<String, URL> getFiles() {
		SortedMap<String, URL> map = new TreeMap<>();
		for (URI uri : Io.getFileUris(Configuration.DIRECTORY_CSV_NUTS)) {
			String filename = new File(uri).getName();
			map.put(filename.substring("nuts-".length(), filename.lastIndexOf('.')), Io.uriToUrl(uri));
		}
		return map;
	}

	public OldNutsCsv get(String key) {
		return csvNutsIndex.get(key);
	}

	public Collection<OldNutsCsv> getValues() {
		return csvNutsIndex.values();
	}

	public Set<Entry<String, OldNutsCsv>> getEntries() {
		return csvNutsIndex.entrySet();
	}

	public int getSize() {
		return csvNutsIndex.size();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		if (csvNutsIndex == null) {
			stringBuilder.append("Empty " + getClass().getSimpleName());
		} else {
			for (Entry<String, OldNutsCsv> entry : csvNutsIndex.entrySet()) {
				stringBuilder.append(entry.getKey());
				stringBuilder.append(" = ");
				stringBuilder.append(entry.getValue());
				stringBuilder.append(System.lineSeparator());
			}
		}
		return stringBuilder.toString();
	}
}