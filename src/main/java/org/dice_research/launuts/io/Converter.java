package org.dice_research.launuts.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dice_research.launuts.Config;
import org.dice_research.launuts.exceptions.CommandRuntimeException;
import org.dice_research.launuts.sources.Source;
import org.dice_research.launuts.sources.Sources;

/**
 * Converts XLS to XLSX to CSV
 * 
 * 
 * LibreOffice
 * 
 * https://libreoffice.org
 * 
 * Command: "libreoffice --convert-to xlsx file.xls --outdir directory/"
 * 
 * 
 * xlsx2csv
 * 
 * https://github.com/dilshod/xlsx2csv
 * 
 * Installation: "pip install xlsx2csv"
 * 
 * Command: "xlsx2csv -s 0 file.xlsx directory/" ("-s 0" means all sheets)
 * 
 * Note: xlsx2csv execution worked when Eclipse was started from command line.
 * 
 * 
 * @author Adrian Wilke
 */
public class Converter {

	/**
	 * Executes a shell command.
	 */
	public String executeCommand(String... command) {
		StringBuilder stringBuilder = new StringBuilder();
		ProcessBuilder processBuilder = new ProcessBuilder();
		List<String> cmd = new ArrayList<>(command.length + 2);
		cmd.add("sh");
		cmd.add("-c");
		cmd.addAll(Arrays.asList(command));
		processBuilder.command(cmd);
		try {
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			int exitValue = process.waitFor();
			if (exitValue != 0) {
				stringBuilder.append("Error: Process exited with " + exitValue + "\n");
				throw new CommandRuntimeException(stringBuilder.toString(), exitValue);
			}
		} catch (IOException e) {
			throw new CommandRuntimeException(e);
		} catch (InterruptedException e) {
			throw new CommandRuntimeException(e);
		}
		return stringBuilder.toString();
	}

	/**
	 * @return false, if the LibreOffice command does not return 0
	 */
	public boolean isLibreofficeInstalled() {
		try {
			executeCommand("libreoffice --version");
		} catch (CommandRuntimeException e) {
			if (e.exitValue != null)
				return false;
			else
				throw e;
		}
		return true;
	}

	/**
	 * @return false, if the xlsx2csv command does not return 0
	 */
	public boolean isXlsx2csvInstalled() {
		try {
			executeCommand("xlsx2csv --version");
		} catch (CommandRuntimeException e) {
			if (e.exitValue != null) {
				System.out.println(e.exitValue);
				return false;
			} else
				throw e;
		}
		return true;
	}

	/**
	 * @return false, if the in2csv command does not return 0
	 */
	public boolean isIn2csvInstalled() {
		try {
			executeCommand("in2csv --version");
		} catch (CommandRuntimeException e) {
			if (e.exitValue != null) {
				System.out.println(e.exitValue);
				return false;
			} else
				throw e;
		}
		return true;
	}

	/**
	 * Converts XLS files to XLSX files.
	 */
	public void convertXls() throws IOException {
		for (Source source : new Sources().parseJsonFile(new File(Config.get(Config.KEY_SOURCES_FILE)))) {
			if (source.fileType.equals(Sources.FILETYPE_XLS)) {
				String file = source.getDownloadFile().getAbsolutePath();
				if (new File(Config.get(Config.KEY_CONVERTED_DIRECTORY), source.getDownloadFileName() + "x").exists())
					continue;
				File dir = new File(Config.get(Config.KEY_CONVERTED_DIRECTORY));
				if (!dir.exists())
					dir.mkdirs();
				String cmd = "libreoffice --convert-to xlsx " + file + " --outdir " + dir.getAbsolutePath();
				System.out.println(executeCommand(cmd));
			}
		}
	}

	/**
	 * Converts XLSX files to CSV files using xlsx2csv.
	 */
	public void convertCsvXlsx2csv() throws IOException {
		for (Source source : new Sources().parseJsonFile(new File(Config.get(Config.KEY_SOURCES_FILE)))) {
			if (!source.fileType.equals(Sources.FILETYPE_XLSX) && !source.fileType.equals(Sources.FILETYPE_XLS))
				continue;
			String file = source.getXlsxFile().getAbsolutePath();
			File dir = source.getCsvDirectory();
			if (dir.exists())
				continue;
			String cmd = "xlsx2csv -s 0 " + file + " " + dir.getAbsolutePath();
			System.out.println(cmd);
			System.out.println(executeCommand(cmd));
		}
	}

	/**
	 * Converts XLSX files to CSV files using in2csv.
	 */
	public void convertCsvIn2csv() throws IOException {
		for (Source source : new Sources().parseJsonFile(new File(Config.get(Config.KEY_SOURCES_FILE)))) {
			if (!source.fileType.equals(Sources.FILETYPE_XLSX) && !source.fileType.equals(Sources.FILETYPE_XLS))
				continue;

			File targetDirectory = source.getCsvDirectory();
			if (targetDirectory.exists())
				continue;

			// Get sheet names
			File xslxFile = source.getXlsxFile();
			String filepath = xslxFile.getAbsolutePath();
			String cmd = "in2csv --write-sheets - -n " + filepath;
			List<String> sheetnames = Arrays.asList(executeCommand(cmd).split("\n"));

			// Convert to CSV files
			cmd = "in2csv --write-sheets - " + xslxFile;
			executeCommand(cmd);

			// Move generated files
			source.getCsvDirectory().mkdirs();
			for (int i = 0; i < sheetnames.size(); i++) {
				File sourceFile = new File(source.getXlsxFile().getParent(), source.id + "_" + i + ".csv");
				File targetFile = new File(targetDirectory, sheetnames.get(i) + ".csv");
				System.out.println(sourceFile + " -> " + targetFile);
				Files.move(sourceFile.toPath(), targetFile.toPath());
			}
		}
	}
}