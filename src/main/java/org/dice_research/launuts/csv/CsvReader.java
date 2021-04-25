package org.dice_research.launuts.csv;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.dice_research.launuts.exceptions.CsvRuntimeException;
import org.dice_research.launuts.exceptions.IoRuntimeException;
import org.dice_research.launuts.io.Io;

/**
 * CSV Reader.
 * 
 * Usage: Use {@link #CsvReader(URL)} or {@link #setUrl(URL)} to set the CSV URL
 * to parse. Use setter methods to specify the parsing configuration. Call
 * {@link #read()} to parse data and getter methods to access data.
 * 
 * @see https://commons.apache.org/proper/commons-csv/
 *
 * @author Adrian Wilke
 */
public class CsvReader {

	private URL url;
	private Charset charset = StandardCharsets.UTF_8;
	private CSVFormat csvFormat = CSVFormat.DEFAULT;
	private boolean trimValues = true;
	private boolean trimMatrix = true;

	private List<List<String>> data;
	private int maxRowIndex;
	private int maxColumnIndex;

	public CsvReader() {
	}

	public CsvReader(URL url) {
		setUrl(url);
	}

	public CsvReader setUrl(URL url) {
		if (url.getProtocol().equals("file")) {
			try {
				Io.checkReadable(new File(url.toURI()));
			} catch (URISyntaxException e) {
				throw new IoRuntimeException(e);
			}
		}
		this.url = url;
		return this;
	}

	public CsvReader setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	public CsvReader setCsvFormat(CSVFormat csvFormat) {
		this.csvFormat = csvFormat;
		return this;
	}

	public CsvReader setTrimValues(boolean trimValues) {
		this.trimValues = trimValues;
		return this;
	}

	public CsvReader setTrimMatrix(boolean trimMatrix) {
		this.trimMatrix = trimMatrix;
		return this;
	}

	public CsvReader read() {
		if (url == null) {
			throw new CsvRuntimeException("No CSV url set.");
		}

		this.data = new LinkedList<List<String>>();
		this.maxRowIndex = -1;
		this.maxColumnIndex = -1;

		Iterator<String> iterator;
		List<String> rowValues;
		int r, c, columnWithContent;
		String value;

		// Parse
		CSVParser csvParser;
		try {
			csvParser = CSVParser.parse(url, charset, csvFormat);
		} catch (IOException e) {
			throw new CsvRuntimeException(e);
		}

		try {

			// Rows
			r = -1;
			for (CSVRecord record : csvParser.getRecords()) {
				r++;
				iterator = record.iterator();
				rowValues = new ArrayList<String>(record.size());

				// Cells
				c = columnWithContent = -1;
				while (iterator.hasNext()) {
					c++;
					value = iterator.next();

					if (trimValues) {
						value = value.trim();
					}

					if (!value.isEmpty()) {
						columnWithContent = c;
					}

					rowValues.add(value);
				}

				this.data.add(rowValues);

				if (columnWithContent > this.maxColumnIndex) {
					this.maxColumnIndex = columnWithContent;
				}
				if (columnWithContent > -1) {
					this.maxRowIndex = r;
				}
			}

		} catch (IOException e) {
			throw new CsvRuntimeException(e);
		}

		if (trimMatrix) {
			clean();
		}

		return this;
	}

	private void clean() {
		for (int r = 0; r <= maxRowIndex; r++) {
			data.set(r, data.get(r).subList(0, maxColumnIndex + 1));
		}
		for (int r = data.size() - 1; r > maxRowIndex; r--) {
			data.remove(r);
		}
	}

	public URL getUrl() {
		return url;
	}

	public List<List<String>> getData() {
		return data;
	}

	public List<String> getRow(int row) {
		return data.get(row);
	}

	public String getValue(int row, int column) {
		return data.get(row).get(column);
	}

	public int getRowSize() {
		return data.size();
	}

	public int getColumnSize() {
		if (data.isEmpty()) {
			return 0;
		} else {
			return data.get(0).size();
		}
	}

	public String getDataAsString(String separator) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int r = 0; r < getRowSize(); r++) {
			for (int c = 0; c < getColumnSize(); c++) {
				if (c != 0) {
					stringBuilder.append(separator);
				}
				stringBuilder.append(getValue(r, c));
			}
			stringBuilder.append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}

	@Override
	public String toString() {
		return getDataAsString("|");
	}
}