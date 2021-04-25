package org.dice_research.launuts.io;

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * Resources access.
 *
 * @author Adrian Wilke
 */
public abstract class Resources {

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
}