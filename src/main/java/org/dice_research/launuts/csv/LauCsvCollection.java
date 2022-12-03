package org.dice_research.launuts.csv;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Collection of {@link LauCsvItem}.
 * 
 * Created by {@link LauCsvParser}.
 * 
 * @author Adrian Wilke
 */
public class LauCsvCollection {

	public String sourceId;
	public File file;
	public List<LauCsvItem> lauCsvItems = new LinkedList<>();

	public LauCsvCollection(String sourceId, File file) {
		this.sourceId = sourceId;
		this.file = file;
	}

	public void add(LauCsvItem lauCsvItem) {
		lauCsvItems.add(lauCsvItem);
	}
}
