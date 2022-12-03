package org.dice_research.launuts.csv;

/**
 * NUTS CSV item
 * 
 * Data cleaning is done in {@link NutsCsvParser}.
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