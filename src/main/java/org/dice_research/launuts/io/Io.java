package org.dice_research.launuts.io;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
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
	public static List<URI> listDirectory(String directory) {
		try {
			return Files.list(Paths.get(directory)).map(Path::toUri).collect(Collectors.toList());
		} catch (IOException e) {
			throw new IoRuntimeException(e);
		}
	}

	/**
	 * Gets sorted set of file URIs.
	 */
	public static SortedSet<URI> getFileUris(String directory) {
		SortedSet<URI> set = new TreeSet<>();
		for (URI uri : Io.listDirectory(directory)) {
			set.add(uri);
		}
		return set;
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

	/**
	 * Returns URL of file.
	 */
	public static URL fileToUrl(File file) {
		return uriToUrl(file.toURI());
	}

	/**
	 * Returns URL of URI.
	 */
	public static URL uriToUrl(URI uri) {
		try {
			return uri.toURL();
		} catch (MalformedURLException e) {
			throw new IoRuntimeException(e);
		}
	}

	/**
	 * Returns URI of URL.
	 */
	public static URI urlToUri(URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IoRuntimeException(e);
		}
	}
}