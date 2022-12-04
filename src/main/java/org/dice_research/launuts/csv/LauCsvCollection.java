package org.dice_research.launuts.csv;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.dice_research.launuts.io.TableExporter;
import org.dice_research.launuts.sources.SourceCsvSheets;

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

	/**
	 * Extracts country code from file name.
	 */
	public String getCountryCode() {
		return SourceCsvSheets.getLauCountryCode(file);
	}

	public String getValues(boolean breakLines) {
		StringBuilder sb = new StringBuilder();
		sb.append(sourceId);
		sb.append(breakLines ? "\n" : " ");
		sb.append(file);
		sb.append(breakLines ? "\n" : " ");
		for (LauCsvItem lauCsvItem : lauCsvItems) {
			sb.append(lauCsvItem.toString());
			sb.append(breakLines ? "\n" : " ");
		}
		return sb.toString();
	}

	public String getMarkdownTable() {
		TableExporter tab = new TableExporter().setHeadings(
				new String[] { "NUTS", "LAU", "LAU2", "Name Latin", "Name National", "Population", "Area" });
		for (LauCsvItem lauCsvItem : lauCsvItems) {
			tab.addRow(new String[] { lauCsvItem.nutsCodeToString(), lauCsvItem.lauCodeToString(),
					lauCsvItem.lauCodeSecondToString(), lauCsvItem.nameLatinToString(),
					lauCsvItem.nameNationalToString(), lauCsvItem.populationToString(), lauCsvItem.areaToString() });
		}
		return tab.getMarkdown();
	}
}