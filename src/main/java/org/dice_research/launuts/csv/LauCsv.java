package org.dice_research.launuts.csv;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.dice_research.launuts.exceptions.IoRuntimeException;

/**
 * LAU CSV uses a {@link CsvReader} or a serialized version to read data.
 * Provides methods for data access.
 *
 * @author Adrian Wilke
 */
public class LauCsv {

	// Underlying data
	private CsvData csvData;

	private String id;
	private URI fileUri;

	public LauCsv(String id, URI fileUri) {
		this.id = id;
		this.fileUri = fileUri;
	}

	public LauCsv readCsv(boolean useCache) {

		SerializedCsvData serializedCsvData = new SerializedCsvData(Integer.toString(fileUri.hashCode()));

		if (serializedCsvData.serializationExists()) {
			csvData = serializedCsvData.deserialize();

		} else {
			try {
				csvData = new CsvReader(fileUri.toURL()).read();
				serializedCsvData.serialize(csvData);
			} catch (MalformedURLException e) {
				throw new IoRuntimeException(e);
			}
		}

		return this;
	}

	// TODO
	public List<String> tmpReadAndReturnFirstRow() {
		readCsv(true);
		if (csvData.getRowSize() > 0) {
			return csvData.getRow(0);
		} else {
			return new ArrayList<>(0);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "|" + id;
	}
}