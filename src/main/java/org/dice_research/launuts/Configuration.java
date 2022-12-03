package org.dice_research.launuts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * Configuration values parsed from JSON file.
 * 
 * Usage: Config.get(Config.KEY_***).
 * 
 * Implemented as Singleton, see https://www.baeldung.com/java-singleton
 *
 * @author Adrian Wilke
 */
public enum Configuration {

	INSTANCE();

	public static String get(String key) {
		return Configuration.INSTANCE.getInstance().configuration_values.get(key);
	}

	public static final String CONFIGURATION_FILE = "configuration.json";

	public static final String KEY_DOWNLOAD_DIRECTORY = "download-directory";
	public static final String KEY_CONVERTED_DIRECTORY = "converted-directory";
	public static final String KEY_CSV_DIRECTORY = "csv-directory";
	public static final String KEY_SOURCES_FILE = "sources-file";

	private Map<String, String> configuration_values;

	public Configuration getInstance() {
		return getInstance(CONFIGURATION_FILE);
	}

	/**
	 * Reads configuration from JSON file and returns instance.
	 */
	public Configuration getInstance(String file) {
		if (configuration_values == null) {
			configuration_values = new HashMap<>();
			String json = "{}";
			try {
				json = Files.readString(new File(file).toPath());
			} catch (IOException e) {
				System.err.println("Error while reading configuration file " + new File(file).getAbsolutePath());
				System.exit(1);
			}
			JSONObject jsonObject = new JSONObject(json);
			for (String key : jsonObject.keySet()) {
				configuration_values.put(key, jsonObject.getString(key));
			}
		}
		return INSTANCE;
	}

}