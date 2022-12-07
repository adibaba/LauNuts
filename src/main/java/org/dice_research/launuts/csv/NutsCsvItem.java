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

	public int nutsSchema = -1;
	public String nutsCode;
	public String name;

	public NutsCsvItem(int nutsSchema, String nutsCode, String name) {
		this.nutsSchema = nutsSchema;
		this.nutsCode = nutsCode;
		this.name = name;
	}

	@Override
	public String toString() {
		return nutsCode + " (" + name + ", " + nutsSchema + " )";
	}

	public int getLevel() {
		return nutsCode.length() - 2;
	}

	// Check values

	public boolean hasNutsSchema() {
		return nutsSchema != -1;
	}

	public boolean hasNutsCode() {
		return nutsCode != null;
	}

	public boolean hasName() {
		return name != null;
	}

}