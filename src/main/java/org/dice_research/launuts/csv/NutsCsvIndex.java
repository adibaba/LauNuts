package org.dice_research.launuts.csv;

import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.dice_research.launuts.io.Io;
import org.dice_research.launuts.io.Resources;

/**
 * Index of NUTS CSV data. Configuration and parsing of CSV.
 *
 * @author Adrian Wilke
 */
public class NutsCsvIndex {

	private Map<String, NutsCsv> csvNutsIndex;

	public NutsCsvIndex create() {
		csvNutsIndex = new TreeMap<>();

		// resource-ID to resource-URL
		Map<String, URL> resIndex = Resources.getNutsCsvResources();

		// resource-ID to CsvReader
		Map<String, CsvReader> csvReaderIndex = new TreeMap<>();
		for (Entry<String, URL> entry : resIndex.entrySet()) {
			csvReaderIndex.put(entry.getKey(), new CsvReader(entry.getValue()).read());
		}

		String id;
		NutsCsv nutsCsv;

		// Create one entry <ID, CsvReader> for every file
		// (Using column for old NUTS code)
		for (CsvReader csvReader : csvReaderIndex.values()) {
			id = createId(Io.urlToUri(csvReader.getUrl()));
			nutsCsv = new NutsCsv(id, csvReader);
			nutsCsv.setRowIndexHeadings(1).setDefaultDataRange();
			csvNutsIndex.put(id, nutsCsv);
		}

		// nuts-2013-2016.csv: Duplicate 2013 code 'FR7'. Checked:
		// 'AUVERGNE-RHÔNE-ALPES' should probably not be 'FR7'.
		// Solution 1: remove row
		// csvNutsIndex.get("2013").addRowToSkip(909);
		// Solution 2: use other file
		id = "2013";
		nutsCsv = new NutsCsv(id, csvReaderIndex.get("2010-2013"));
		nutsCsv.setUseCodeOld(false).setRowIndexHeadings(1).setDefaultDataRange();
		nutsCsv.columnIndexValueNotEmptyCheck = 11;
		csvNutsIndex.put(id, nutsCsv);

		// Add 2016
		id = "2016";
		nutsCsv = new NutsCsv(id, csvReaderIndex.get("2013-2016"));
		nutsCsv.setUseCodeOld(false).setRowIndexHeadings(1).setDefaultDataRange();
		nutsCsv.columnIndexValueNotEmptyCheck = 11;
		csvNutsIndex.put(id, nutsCsv);

		// NUTS CSV 2021 has another format and is split
		csvNutsIndex.get("2021").setRowIndexHeadings(0).setDefaultDataRange().setRowIndexDataEnd(1844);
		setIndexes2021(csvNutsIndex.get("2021"));

		// NUTS CSV 2021 has additional data
		id = "2021-EXTRA";
		nutsCsv = new NutsCsv(id, csvNutsIndex.get("2021").getCsvReader());
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

	private void setIndexes2021(NutsCsv nutsCsv) {
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

	public NutsCsv get(String key) {
		return csvNutsIndex.get(key);
	}

	public Collection<NutsCsv> getValues() {
		return csvNutsIndex.values();
	}

	public Set<Entry<String, NutsCsv>> getEntries() {
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
			for (Entry<String, NutsCsv> entry : csvNutsIndex.entrySet()) {
				stringBuilder.append(entry.getKey());
				stringBuilder.append(" = ");
				stringBuilder.append(entry.getValue());
				stringBuilder.append(System.lineSeparator());
			}
		}
		return stringBuilder.toString();
	}
}