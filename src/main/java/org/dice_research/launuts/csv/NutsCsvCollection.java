package org.dice_research.launuts.csv;

import java.util.LinkedList;
import java.util.List;

import org.dice_research.launuts.io.TableExporter;

/**
 * Collection of {@link NutsCsvItem}.
 * 
 * Created by {@link NutsCsvParser}.
 * 
 * @author Adrian Wilke
 */
public class NutsCsvCollection {

	public String sourceId;
	public List<NutsCsvItem> country = new LinkedList<>();
	public List<NutsCsvItem> nuts1 = new LinkedList<>();
	public List<NutsCsvItem> nuts2 = new LinkedList<>();
	public List<NutsCsvItem> nuts3 = new LinkedList<>();

	public NutsCsvCollection(String sourceId) {
		this.sourceId = sourceId;
	}

	public void add(NutsCsvItem nutsCsvItem) {
		switch (nutsCsvItem.nutsCode.length()) {
		case 2:
			country.add(nutsCsvItem);
			break;
		case 3:
			nuts1.add(nutsCsvItem);
			break;
		case 4:
			nuts2.add(nutsCsvItem);
			break;
		case 5:
			nuts3.add(nutsCsvItem);
			break;
		default:
			break;
		}
	}

	public String getValues(boolean breakLines) {
		StringBuilder sb = new StringBuilder();
		sb.append(sourceId);
		sb.append(breakLines ? "\n" : " ");
		for (NutsCsvItem nutsCsvItem : country) {
			sb.append(nutsCsvItem.toString());
			sb.append(breakLines ? "\n" : " ");
		}
		for (NutsCsvItem nutsCsvItem : nuts1) {
			sb.append(nutsCsvItem.toString());
			sb.append(breakLines ? "\n" : " ");
		}
		for (NutsCsvItem nutsCsvItem : nuts2) {
			sb.append(nutsCsvItem.toString());
			sb.append(breakLines ? "\n" : " ");
		}
		for (NutsCsvItem nutsCsvItem : nuts3) {
			sb.append(nutsCsvItem.toString());
			sb.append(breakLines ? "\n" : " ");
		}
		return sb.toString();
	}

	public String getMarkdownTable() {
		TableExporter tab = new TableExporter().setHeadings(new String[] { "Code", "Name" });
		for (NutsCsvItem nutsCsvItem : country) {
			tab.addRow(new String[] { nutsCsvItem.nutsCode, nutsCsvItem.name });
		}
		for (NutsCsvItem nutsCsvItem : nuts1) {
			tab.addRow(new String[] { nutsCsvItem.nutsCode, nutsCsvItem.name });
		}
		for (NutsCsvItem nutsCsvItem : nuts2) {
			tab.addRow(new String[] { nutsCsvItem.nutsCode, nutsCsvItem.name });
		}
		for (NutsCsvItem nutsCsvItem : nuts3) {
			tab.addRow(new String[] { nutsCsvItem.nutsCode, nutsCsvItem.name });
		}
		return tab.getMarkdown();
	}

	@Override
	public String toString() {
		return (country.size() + nuts1.size() + nuts2.size() + nuts3.size()) + " = " + country.size() + " + "
				+ nuts1.size() + " + " + nuts2.size() + " + " + nuts3.size() + ", " + sourceId;
	}

}