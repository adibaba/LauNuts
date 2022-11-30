package org.dice_research.launuts.sources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import org.dice_research.launuts.Config;
import org.json.JSONArray;

/**
 * Parses JSON file with source metadata and creates {@link Source} objects.
 * 
 * @author Adrian Wilke
 */
public class Sources {

	public static final String KEY_ID = "id";
	public static final String KEY_FILETYPE = "filetype";
	public static final String KEY_SOURCES = "sources";
	public static final String KEY_TYPE = "type";

	public static final String FILETYPE_XLSX = "xlsx";
	public static final String FILETYPE_XLS = "xls";

	/**
	 * Parses JSON file and returns list of {@link Source} objects.
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<Source> getSources() throws IOException {
		return this.parseJsonFile(new File(Config.get(Config.KEY_SOURCES_FILE)));
	}

	/**
	 * Parses JSON file and returns list of {@link Source} objects.
	 * 
	 * @param file JSON file
	 * @return List of Source objects
	 * @throws IOException
	 */
	private List<Source> parseJsonFile(File file) throws IOException {
		List<Source> sources = new LinkedList<>();
		String json = Files.readString(file.toPath());
		JSONArray jsonArray = new JSONArray(json);
		for (int i = 0; i < jsonArray.length(); i++) {
			sources.add(new Source(jsonArray.getJSONObject(i)));
		}
		return sources;
	}

	/**
	 * Parses JSON file and returns list of {@link Source} IDs.
	 */
	public List<String> getSourceIds() {
		List<String> ids = new LinkedList<>();
		try {
			for (Source source : new Sources().parseJsonFile(new File(Config.get(Config.KEY_SOURCES_FILE)))) {
				ids.add(source.id);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return ids;
	}

}