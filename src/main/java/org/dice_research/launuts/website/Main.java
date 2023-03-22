package org.dice_research.launuts.website;

import java.io.File;
import java.io.IOException;

/**
 * Generator for LauNuts URI tables.
 * 
 * @author Adrian Wilke
 */
public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		if (args.length == 0) {
			System.err.println("Please provide a LauNuts RDF file");
		} else {
			new Main().getData(args[0]);
		}
	}

	File cachefile = new File("./cache.data");
	DataContainer dataContainer = new DataContainer();

	/**
	 * Parses n-triples file or loads data from cache.
	 */
	private void getData(String modelfilepath) throws IOException, ClassNotFoundException {
		if (!cachefile.exists()) {
			ModelReader modelReader = new ModelReader();
			dataContainer.nutsSchemes = modelReader.readModel(modelfilepath);
			dataContainer.writeFile(cachefile);
		} else {
			dataContainer.readFile(cachefile);
		}
		System.out.println(dataContainer.nutsSchemes.size());
	}

}