package org.dice_research.launuts.csv;

/**
 * Lau CSV item
 * 
 * Data cleaning is done in {@link LauCsvParser}.
 * 
 * @author Adrian Wilke
 */
public class LauCsvItem {

	public String lauCode;
	public String lauCodeSecond; // only used until 2016
	public String relatedNutsCode;
	public String nameLatin;
	public String nameNational;
	public int population = -1;
	public double area = -1;

	public LauCsvItem(String lauCode, String lauCodeSecond, String relatedNutsCode, String nameLatin,
			String nameNational, int population, double area) {
		this.lauCode = lauCode;
		this.lauCodeSecond = lauCodeSecond;
		this.relatedNutsCode = relatedNutsCode;
		this.nameLatin = nameLatin;
		this.nameNational = nameNational;
		this.population = population;
		this.area = area;
	}

	public String getCountry() {
		if (relatedNutsCode != null && relatedNutsCode.length() >= 2)
			return relatedNutsCode.substring(0, 2);
		else
			return "??";
	}

	@Override
	public String toString() {
		return lauCode + " (" + getCountry() + ", " + nameLatin + ")";
	}

}