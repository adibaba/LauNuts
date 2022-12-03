package org.dice_research.launuts.csv;

/**
 * Container for parsed NUTS CSV data.
 * 
 * Data cleaning is done in {@link NutsCsvParser}.
 * 
 * Single items are maintained in {@link NutsCsvCollection}.
 * 
 * @author Adrian Wilke
 */
public class NutsCsvItem {

	public String nutsCode;
	public String name;

	public NutsCsvItem(String nutsCode, String name) {
		this.nutsCode = nutsCode;
		this.name = name;
	}

	@Override
	public String toString() {
		return nutsCode + " (" + name + ")";
	}

}