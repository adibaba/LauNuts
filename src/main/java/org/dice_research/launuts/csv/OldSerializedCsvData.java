package org.dice_research.launuts.csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dice_research.launuts.Configuration;
import org.dice_research.launuts.exceptions.IoRuntimeException;
import org.dice_research.launuts.io.Serialization;

/**
 * Serialization of {@link OldCsvData}.
 *
 * @author Adrian Wilke
 */
public class OldSerializedCsvData extends OldCsvData {

	public static final long serialVersionUID = OldCsvData.serialVersionUID;

	private String filename;

	public OldSerializedCsvData(String filename) {
		this.filename = filename;
	}

	public File getFile() {
		return new File(Configuration.DIRECTORY_CACHE, filename);
	}

	public boolean serializationExists() {
		return getFile().exists();
	}

	public void serialize(OldCsvData csvData) {
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
	public OldSerializedCsvData deserialize() {
		try {
			data = (List<List<String>>) Serialization.read(getFile());
		} catch (ClassNotFoundException | IOException e) {
			throw new IoRuntimeException(e);
		}
		return this;
	}

}