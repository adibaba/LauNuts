package org.dice_research.launuts.csv;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Collection of {@link LauCsvItem}.
 * 
 * Created by {@link LauCsvParser}.
 * 
 * @author Adrian Wilke
 */
public class LauCsvCollection {

	public String sourceId;
	private SortedMap<String, List<LauCsvItem>> lauCsvItems = new TreeMap<>();

	public LauCsvCollection(String sourceId) {
		this.sourceId = sourceId;
	}

	public void add(LauCsvItem lauCsvItem) {
		String country = lauCsvItem.getCountryCode();
		List<LauCsvItem> lauCsvList;
		if (!lauCsvItems.containsKey(country)) {
			lauCsvList = lauCsvItems.put(country, new LinkedList<>());
		}
		lauCsvList = lauCsvItems.get(country);
		lauCsvList.add(lauCsvItem);
	}

	public Set<String> getKeys() {
		return lauCsvItems.keySet();
	}

	public List<LauCsvItem> getLauCsvItemList(String key) {
		return lauCsvItems.get(key);
	}
}
