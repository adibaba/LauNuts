package org.dice_research.launuts.sources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;

/**
 * Parses JSON file with source metadata and creates {@link Source} objects.
 * 
 * @author Adrian Wilke
 */
public class Sources {

	public static final String SOURCES_FILE = "sources.json";

	public static final String KEY_ID = "id";
	public static final String KEY_FILETYPE = "filetype";
	public static final String KEY_SOURCES = "sources";
	public static final String KEY_TYPE = "type";

	/**
	 * Parses JSON file and returns list of {@link Source} objects.
	 * 
	 * @param file JSON file
	 * @return List of Source objects
	 * @throws IOException
	 */
	public List<Source> parseJsonFile(File file) throws IOException {
		List<Source> sources = new LinkedList<>();
		String json = Files.readString(file.toPath());
		JSONArray jsonArray = new JSONArray(json);
		for (int i = 0; i < jsonArray.length(); i++) {
			sources.add(new Source(jsonArray.getJSONObject(i)));
		}
		return sources;
	}

}