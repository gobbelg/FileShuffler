/**
 *
 */
package core;

import java.io.FileNotFoundException;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;

import controllers.Deconcatenator;

/**
 * Main class for concatenating and deconcatenating files
 *
 * @author gtony
 * @date 02 Nov 2022
 *
 */
public class FileShuffler {

	private enum RunType {
		BASE, CHECK_SETUP, DECONCATENATE, READ_LINES
	}

	private static final org.apache.logging.log4j.Logger LOGGER = LogManager
			.getLogger(FileShuffler.class);

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		final RunType runType = args.length > 0 ? RunType.valueOf(args[0])
				: RunType.CHECK_SETUP;

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				switch (runType) {
				case BASE: {
				}
					break;
				case CHECK_SETUP: {
					for (int i = 0; i < 100; i++) {
						LOGGER.trace("i is: " + i);
					}
				}
					break;
				case DECONCATENATE: {
					String[] updatedArgs = args;
					if ( args.length > 1 ) {
						updatedArgs = Arrays.copyOfRange(args, 1, args.length);
					}
					deconcatenate(updatedArgs);
				}
					break;
				/*
				 * Reads the headers in a directory of files from lines that
				 * begin with a string given within 'args' variable, which also
				 * contains the full path to the directory with the files. It
				 * automatically saves the names of the files and the file
				 * headers to a separate file in the parent of the directory
				 * with the files.
				 */
				case READ_LINES: {
					String[] updatedArgs = args;
					if ( args.length > 1 ) {
						updatedArgs = Arrays.copyOfRange(args, 1, args.length);
					}
					readLines(updatedArgs);
				}
					break;
				default:
					break;

				}

				System.out.println("Finished!");
				System.exit(0);

			}

			/**
			 * Deconcatenates a text file containing multiple text files
			 * separated by a defined delimiter. The files are placed in a
			 * hierarchical set of files at the greatest depth of the hierarchy,
			 * and the directories are named according to depth and when they
			 * are created in the hierarchy using a depth-first build of the
			 * hierarchy.
			 *
			 * The args parameter should contain the path as a string as
			 * args[0], the file delimiter as args[1], and the maximum size of
			 * any directory in the hierarchy in terms of the number of files or
			 * directories it will contain in args[2].
			 *
			 * @param args
			 */
			private void deconcatenate(String[] args) {
				Deconcatenator dc = new Deconcatenator(args);
				dc.deconcatenate();
			}

			private void readLines(String[] updatedArgs) {
				HeaderReader hr;
				try {
					hr = new HeaderReader(args);
					hr.readHeaders();
				}
				catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

}
