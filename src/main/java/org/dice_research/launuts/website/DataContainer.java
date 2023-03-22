package org.dice_research.launuts.website;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class DataContainer implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<NutsScheme> nutsSchemes;

	public void writeFile(File file) throws IOException {
		Serialization.write(nutsSchemes, file);
	}

	@SuppressWarnings("unchecked")
	public void readFile(File file) throws IOException, ClassNotFoundException {
		nutsSchemes = (List<NutsScheme>) Serialization.read(file);
	}

}