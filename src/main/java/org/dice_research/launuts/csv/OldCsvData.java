package org.dice_research.launuts.csv;

import java.io.Serializable;
import java.util.List;

/**
 * Container for CSV data.
 *
 * @author Adrian Wilke
 */
public abstract class OldCsvData implements Serializable {

	static final long serialVersionUID = 1L;

	List<List<String>> data;

	public List<List<String>> getData() {
		return data;
	}

	public List<String> getRow(int row) {
		return data.get(row);
	}

	public String getValue(int row, int column) {
		return data.get(row).get(column);
	}

	public int getRowSize() {
		return data.size();
	}

	public int getColumnSize() {
		if (data.isEmpty()) {
			return 0;
		} else {
			return data.get(0).size();
		}
	}

	public String getDataAsString(String separator) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int r = 0; r < getRowSize(); r++) {
			for (int c = 0; c < getColumnSize(); c++) {
				if (c != 0) {
					stringBuilder.append(separator);
				}
				stringBuilder.append(getValue(r, c));
			}
			stringBuilder.append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}

	@Override
	public String toString() {
		return getDataAsString("|");
	}

}