package org.dice_research.launuts.io;

import java.net.URL;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Resources access.
 *
 * @author Adrian Wilke
 */
public abstract class Resources {

	/**
	 * Resource files have been removed, external files are used.
	 */
	@Deprecated
	public static Map<String, URL> getNutsCsvResources() {
		Map<String, URL> map = new TreeMap<>();
		ClassLoader classLoader = Resources.class.getClassLoader();
		map.put("1995-1999", classLoader.getResource("nuts-csv/nuts-1995-1999.csv"));
		map.put("1999-2003", classLoader.getResource("nuts-csv/nuts-1999-2003.csv"));
		map.put("2003-2006", classLoader.getResource("nuts-csv/nuts-2003-2006.csv"));
		map.put("2006-2010", classLoader.getResource("nuts-csv/nuts-2006-2010.csv"));
		map.put("2010-2013", classLoader.getResource("nuts-csv/nuts-2010-2013.csv"));
		map.put("2013-2016", classLoader.getResource("nuts-csv/nuts-2013-2016.csv"));
		map.put("2021", classLoader.getResource("nuts-csv/nuts-2021.csv"));
		return map;
	}

	public static SortedMap<String, String> getLauCsvDirectories() {
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