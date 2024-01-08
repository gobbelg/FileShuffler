package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;

import boundaries.ParameterGUI;
import core.FileShuffler;
import helper.DeconcatenatorParameters;

public class Deconcatenator {

	private final File fileForDeconcatenation;
	private final int maxDirectorySize;
	private final String fileSeparator;
	private boolean precedingFileSeparator = true;
	private static final org.apache.logging.log4j.Logger LOGGER = LogManager
			.getLogger(FileShuffler.class);

	public Deconcatenator(String[] args) {
		DeconcatenatorParameters dps = ParameterGUI.getParameters(args);
		this.fileForDeconcatenation = dps.fileForDeconcatenation();
		this.maxDirectorySize = dps.maxDirectorySize();
		this.fileSeparator = dps.fileSeparator();
		this.precedingFileSeparator = dps.precedingFileSeparator();
	}

	public void deconcatenate() {
		deconcatenate(this.precedingFileSeparator);
	}

	public void deconcatenate(boolean precedingFileSeparator) {
		try (Scanner scanner = new Scanner(this.fileForDeconcatenation)) {
			scanner.useDelimiter(this.fileSeparator);

			/*
			 * If there is a file separator before the first file, discard text
			 * before the file separator.
			 */
			if ( precedingFileSeparator && scanner.hasNext() ) {
				scanner.next();
			}
			DirectoryGenerator dirGenerator = new DirectoryGenerator(
					this.fileForDeconcatenation, this.fileSeparator,
					this.maxDirectorySize);
			int fileIndex = 0;
			while ( scanner.hasNext() ) {

				String fileText = scanner.next();
				if ( fileText.isBlank() ) {
					continue;
				}

				String nextFilePathString = dirGenerator
						.getNextFilePathString();
				LOGGER.debug("FilePathString:" + nextFilePathString);

				PrintWriter pw = new PrintWriter(nextFilePathString);
				pw.write(fileText);
				pw.close();
				++fileIndex;
				if ( (fileIndex % 100) == 0 ) {
					LOGGER.info("\n\tWrote file " + fileIndex + ":"
							+ nextFilePathString);
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
