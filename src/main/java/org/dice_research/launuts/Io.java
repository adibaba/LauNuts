package org.dice_research.launuts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.dice_research.launuts.exceptions.IoRuntimeException;

/**
 * General input / output.
 *
 * @author Adrian Wilke
 */
public abstract class Io {

	/**
	 * Gets unsorted list of directory contents (names of directories and files).
	 */
	public static List<String> listDirectory(String directory) {
		try {
			return Files.list(Paths.get(directory)).map(Path::getFileName).map(Path::toString)
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new IoRuntimeException(e);
		}
	}

	/**
	 * Gets sorted map: filename to file.
	 */
	public static Map<String, File> getFilesIndex(String directory) {
		Map<String, File> map = new TreeMap<>();
		for (String filename : Io.listDirectory(directory)) {
			map.put(filename, new File(directory, filename));
		}
		return map;
	}

	/**
	 * Throws runtime exception, if file is not readable.
	 */
	public static void checkReadable(File file) {
		if (file == null) {
			throw new IoRuntimeException("File not set.");
		} else if (!file.canRead()) {
			throw new IoRuntimeException("Can not read " + file.getAbsolutePath());
		}
	}
}