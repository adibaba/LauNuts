package org.dice_research.launuts.csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dice_research.launuts.Configuration;
import org.dice_research.launuts.Serialization;
import org.dice_research.launuts.exceptions.IoRuntimeException;

/**
 * Serialization of {@link CsvData}.
 *
 * @author Adrian Wilke
 */
public class SerializedCsvData extends CsvData {

	public static final long serialVersionUID = CsvData.serialVersionUID;

	private String filename;

	public SerializedCsvData(String filename) {
		this.filename = filename;
	}

	public File getFile() {
		return new File(Configuration.DIRECTORY_CACHE, filename);
	}

	public boolean serializationExists() {
		return getFile().exists();
	}

	public void serialize(CsvData csvData) {
		try {
			// For serialization, a list which implements java.io.Serializable
			ArrayList<ArrayList<String>> arrayList = new ArrayList<>(csvData.getData().size());
			for (List<String> list : csvData.getData()) {
				arrayList.add(new ArrayList<>(list));
			}
			Serialization.write(arrayList, getFile());
		} catch (IOException e) {
			throw new IoRuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public SerializedCsvData deserialize() {
		try {
			data = (List<List<String>>) Serialization.read(getFile());
		} catch (ClassNotFoundException | IOException e) {
			throw new IoRuntimeException(e);
		}
		return this;
	}

}