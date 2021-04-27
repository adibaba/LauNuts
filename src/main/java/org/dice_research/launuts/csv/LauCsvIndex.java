package org.dice_research.launuts.csv;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dice_research.launuts.Configuration;
import org.dice_research.launuts.io.Io;

/**
 * Index of LAU CSV data. Configuration and parsing of CSV.
 *
 * @author Adrian Wilke
 */
public class LauCsvIndex {

	private SortedMap<String, SortedMap<String, LauCsv>> csvLauIndex;

	public LauCsvIndex create() {
		csvLauIndex = new TreeMap<>();

		SortedMap<String, SortedMap<String, URI>> files = getFiles();
		for (Entry<String, SortedMap<String, URI>> dirEntry : files.entrySet()) {
			SortedMap<String, LauCsv> map = new TreeMap<>();
			for (Entry<String, URI> fileEntry : dirEntry.getValue().entrySet()) {
				map.put(fileEntry.getKey(), new LauCsv(fileEntry.getKey(), fileEntry.getValue()));
			}
			csvLauIndex.put(dirEntry.getKey(), map);
		}

		return this;
	}

	/**
	 * TODO
	 */
	public void tmpReadData() {
		System.out.println(csvLauIndex.keySet());

		// System.out.println(csvLauIndex.get("27-2010").keySet());
		// System.out.println(csvLauIndex.get("27-2010").get("DE").tmp());

		long time = System.currentTimeMillis();
		for (Entry<String, SortedMap<String, LauCsv>> mapEntry : csvLauIndex.entrySet()) {
			for (Entry<String, LauCsv> lauCsvEntry : mapEntry.getValue().entrySet()) {
				List<String> row = lauCsvEntry.getValue().tmpReadAndReturnFirstRow();
				if (row.isEmpty()) {
					System.err.println(mapEntry.getKey() + " " + lauCsvEntry.getKey());
				}
				System.out.println(lauCsvEntry.getValue().tmpReadAndReturnFirstRow());
			}
		}
		System.out.println(1.0 * (System.currentTimeMillis() - time) / 1000);
		// No cache: 215.084
		// Cache: 104.152
	}

	private SortedMap<String, SortedMap<String, URI>> getFiles() {
		SortedMap<String, SortedMap<String, URI>> map = new TreeMap<>();
		SortedMap<String, String> ids = getIds();
		for (URI uri : Io.getFileUris(Configuration.DIRECTORY_CSV_LAU)) {
			File dir = new File(uri);
			if (dir.isDirectory()) {
				SortedMap<String, URI> fileMap = new TreeMap<>();
				for (URI fileUri : Io.getFileUris(dir.getPath())) {
					File file = new File(fileUri);
					if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")
							&& file.getName().length() == 2 + 4) {
						fileMap.put(file.getName().substring(0, 2).toUpperCase(), file.toURI());
					}
				}
				if (!fileMap.isEmpty()) {
					map.put(ids.get(dir.getName()), fileMap);
				}
			}
		}
		return map;
	}

	private SortedMap<String, String> getIds() {
		SortedMap<String, String> map = new TreeMap<>();
		map.put("EU-27_2010.xlsx", "27-2010");
		map.put("EU-27_2011.xlsx", "27-2011");
		map.put("EU-27-LAU-2020-NUTS-2021-NUTS-2016.xlsx", "27-2020-2021-2016");
		map.put("EU-28_2011_Census.xlsx", "28-2011");
		map.put("EU-28_2012.xlsx", "28-2012");
		map.put("EU-28_2013.xlsx", "28-2013");
		map.put("EU-28_2014.xlsx", "28-2014");
		map.put("EU-28_2015.xlsx", "28-2015");
		map.put("EU-28_LAU_2017_NUTS_2013.xlsx", "28-2017-2013");
		map.put("EU-28_LAU_2017_NUTS_2016.xlsx", "28-2017-2016");
		map.put("EU-28-LAU-2018-NUTS-2016.xlsx", "28-2018-2016");
		map.put("EU-28-LAU-2019-NUTS-2016.xlsx", "28-2019-2016");
		return map;
	}
}