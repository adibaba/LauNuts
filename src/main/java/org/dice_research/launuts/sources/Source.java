package org.dice_research.launuts.sources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedList;
import java.util.List;

import org.dice_research.launuts.Config;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Data source. Created by {@link Sources} and JSON file.
 * 
 * @author Adrian Wilke
 */
public class Source {

	public String id;
	public String fileType;
	public SourceType sourceType;
	public List<String> sources = new LinkedList<>();

	/**
	 * Constructor builds object variables based on JSON object.
	 * 
	 * @param jsonObject
	 */
	public Source(JSONObject jsonObject) {
		this.id = jsonObject.getString(Sources.KEY_ID);
		this.fileType = jsonObject.getString(Sources.KEY_FILETYPE);

		JSONArray jsonArray = jsonObject.getJSONArray(Sources.KEY_SOURCES);
		for (int i = 0; i < jsonArray.length(); i++) {
			this.sources.add(jsonArray.get(i).toString());
		}

		String type = jsonObject.getString(Sources.KEY_TYPE);
		for (SourceType sourceType : SourceType.values()) {
			if (sourceType.toString().toLowerCase().equals(type)) {
				this.sourceType = sourceType;
			}
		}
		if (this.sourceType == null) {
			throw new RuntimeException("Unknown source type: " + type);
		}
	}

	/**
	 * @return File name based on ID and file extension
	 */
	public String getDownloadFileName() {
		return id + "." + fileType;
	}

	/**
	 * @return File in local download directory
	 */
	public File getDownloadFile() {
		return new File(Config.get(Config.KEY_DOWNLOAD_DIRECTORY), getDownloadFileName());
	}

	/**
	 * @return XSLX file. For downloaded XSL files, the converted version path is
	 *         used.
	 */
	public File getXlsxFile() {
		if (fileType.equals(Sources.FILETYPE_XLSX))
			return getDownloadFile();
		else if (fileType.equals(Sources.FILETYPE_XLS))
			return new File(Config.get(Config.KEY_CONVERTED_DIRECTORY), id + ".xlsx");
		else
			return null;
	}

	public File getCsvDirectory() {
		return new File(Config.get(Config.KEY_CSV_DIRECTORY), id);
	}

	/**
	 * Downloads file if not already existing in local download directoy
	 * 
	 * @throws IOException
	 */
	public void download() throws IOException {
		download(false);
	}

	/**
	 * Downloads file.
	 * 
	 * @param force True if existing files should be overwritten
	 * @throws IOException
	 */
	public void download(boolean force) throws IOException {
		// Source: https://www.baeldung.com/java-download-file
		File file = getDownloadFile();
		if (!file.exists() || force) {
			URL url = new URL(sources.get(0));
			System.out.println("Downloading file " + file.getAbsolutePath() + " from " + url);
			file.getParentFile().mkdirs();
			ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			fileOutputStream.close();
		} else if (file.exists()) {
			System.out.println("Skipping download of existing file: " + file.getAbsolutePath());
		}
	}

	@Override
	public String toString() {
		return id;
	}
}