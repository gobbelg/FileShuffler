/**
 *
 */
package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author VHATVHGOBBEG
 *
 */
public class HeaderReader {

	private final String headerString;
	private final File fileDirectory;
	private final PrintWriter pw;

	public HeaderReader(String[] args)
			throws IllegalArgumentException, FileNotFoundException {
		this.headerString = args[0];
		this.fileDirectory = new File(args[1]);

		if ( !this.fileDirectory.isDirectory() ) {
			throw new IllegalArgumentException("The string supplied, " + args[1]
					+ ", is not a path to a directory");
		}
		this.pw = new PrintWriter(this.fileDirectory);
	}

	public void readHeaders() {
		try (Stream<Path> pathStream = Files.walk(this.fileDirectory.toPath(),
				null);) {
			pathStream.forEach(path -> processPath(path));
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Object processPath(Path path) {

		try (BufferedReader br = new BufferedReader(
				new FileReader(path.toFile()))) {
			String line;
			while ( (line = br.readLine()) != null ) {
				if ( line.trim().startsWith(this.headerString) ) {
					// String
				}
			}

		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
