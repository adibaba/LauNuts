package org.dice_research.launuts.sources;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.dice_research.launuts.Config;
import org.dice_research.launuts.exceptions.DownloadRuntimeException;
import org.dice_research.launuts.io.Io;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Data source. Created by {@link Sources} and JSON file.
 * 
 * @author Adrian Wilke
 */
public class Source {

	/**
	 * Identifier of source
	 */
	public String id;

	/**
	 * Filetype. Required as not every download URL provides file extension (e.g.
	 * LAU 2016).
	 */
	public String fileType;

	/**
	 * Source type. Required to distinguish types of sources for further parsing.
	 */
	public SourceType sourceType;

	/**
	 * URL to download sources. Is list to provide mirrors (copies of files).
	 */
	public List<String> sources = new LinkedList<>();

	/**
	 * Used NUTS scheme.
	 */
	public int nutsScheme;

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

		if (jsonObject.has(Sources.KEY_NUTS_SCHEME))
			this.nutsScheme = jsonObject.getInt(Sources.KEY_NUTS_SCHEME);
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

	/**
	 * @return Directory containing CSV files
	 */
	public File getCsvDirectory() {
		return new File(Config.get(Config.KEY_CSV_DIRECTORY), id);
	}

	/**
	 * @return {@link SourceCsvSheets} object
	 */
	public SourceCsvSheets getSheets() {
		return new SourceCsvSheets(this);
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
	 * 
	 * @throws DownloadRuntimeException On any exceptions during download.
	 */
	public void download(boolean force) throws DownloadRuntimeException {
		Io.download(sources.get(0), getDownloadFile(), force);
	}

	@Override
	public String toString() {
		return id;
	}
}