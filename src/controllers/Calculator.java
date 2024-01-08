package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import helper.GeneralHelper;

public class Calculator {

	public final static Logger LOGGER = LogManager.getLogger(Calculator.class);

	public static int getDirectoryDepth(File fileForDeconcatenation,
			String fileSeparator, int maxDirectorySize) {
		int numberOfFiles = countFiles(fileForDeconcatenation, fileSeparator);
		int maxDepth = getMaxDepth(numberOfFiles, maxDirectorySize);
		return maxDepth;
	}

	private static int countFiles(File fileForDeconcatenation,
			String fileSeparator) {
		int fileCount = 0;
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(fileForDeconcatenation));
			String line = null;
			int lineIndex = 0;
			while ( (line = br.readLine()) != null ) {
				LOGGER.debug("Line " + ++lineIndex + ": " + line);
				if ( line.contains(fileSeparator) ) {
					fileCount++;
				}
			}
			br.close();
		}
		catch (IOException e) {
			GeneralHelper.errorWriter("Unable to read file:\n\t"
					+ fileForDeconcatenation.getAbsolutePath());
			e.printStackTrace();
		}
		System.out.println(fileCount + " files counted in file: "
				+ fileForDeconcatenation.getName());
		return fileCount;
	}

	private static int getMaxDepth(int numberOfFiles, int maxDirectorySize) {
		return (int) Math
				.ceil(Math.log(numberOfFiles) / Math.log(maxDirectorySize));
	}
}
