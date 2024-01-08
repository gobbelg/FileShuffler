package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayDeque;

import helper.GeneralHelper;

public class DirectoryGenerator {

	private class StoragePath {

		private Path pathToDirectory;
		private int filesInDirectory;

		/*
		 * The depth field refers to the depth that a directory is found
		 * relative to a root or reference directory. The root directory is
		 * defined to have a depth o '0'.
		 */
		private int depth;

		/*
		 * The directoryIndex field refers to the
		 */
		private int directoryIndex;

		/*
		 * Store the path to directories and the number of files or directories
		 * within them
		 */
		private StoragePath(Path pathToStorage, int filesInDirectory, int depth,
				int index) {
			this.pathToDirectory = pathToStorage;
			this.filesInDirectory = filesInDirectory;
			this.depth = depth;
			this.directoryIndex = index;
		}
	}

	private ArrayDeque<StoragePath> storagePathStack;
	private int maxDirectorySize;
	private int maxDirectoryDepth;
	private final static DecimalFormat DECIMAL_FORMATTER = new DecimalFormat(
			"000");

	private final static String BASE_FILE_NAME = "PEARLS_TIU_230812";

	public DirectoryGenerator(File fileForDeconcatenation, String fileDelimiter,
			int maxDirectorySize) {
		this.maxDirectorySize = maxDirectorySize;
		this.maxDirectoryDepth = Calculator.getDirectoryDepth(
				fileForDeconcatenation, fileDelimiter, maxDirectorySize);
		System.out.println("Directory depth set to: " + this.maxDirectoryDepth);
		initializeStoragePathStack(fileForDeconcatenation,
				this.maxDirectorySize, this.maxDirectoryDepth);
	}

	public String getNextFilePathString() throws IOException {

		if ( !(this.storagePathStack
				.peek().filesInDirectory < this.maxDirectorySize) ) {
			repopulateStoragePathStack();
		}

		StoragePath activeStoragePath = this.storagePathStack.peek();
		String fileName = getFileName(activeStoragePath);
		File file = new File(activeStoragePath.pathToDirectory.toString(),
				fileName);
		activeStoragePath.filesInDirectory++;
		return file.getAbsolutePath();

	}

	private String getFileName(StoragePath activeStoragePath) {
		StringBuilder sb = new StringBuilder(BASE_FILE_NAME);
		sb.append("_");
		sb.append(DECIMAL_FORMATTER.format(activeStoragePath.depth));
		sb.append("_");
		sb.append(DECIMAL_FORMATTER.format(activeStoragePath.directoryIndex));
		sb.append("_");
		sb.append(DECIMAL_FORMATTER.format(activeStoragePath.filesInDirectory));
		sb.append(".txt");
		return sb.toString();
	}

	private void initializeStoragePathStack(File fileForDeconcatenation,
			int maxDirectorySize, int maxDirectoryDepth) {

		this.storagePathStack = new ArrayDeque<>(maxDirectoryDepth);
		File rootDirectory = new File(fileForDeconcatenation.getParentFile(),
				"DeconcatRoot_" + GeneralHelper.getTimeStamp());
		int depth = 0;
		int directoryIndex = 0;
		populateStoragePathStack(rootDirectory, depth, directoryIndex);
	}

	private void populateStoragePathStack(File directoryToBuild, int depth,
			int directoryIndex) {
		try {
			Path activePath = Files
					.createDirectories(directoryToBuild.toPath());
			this.storagePathStack.push(
					new StoragePath(activePath, 0, depth, directoryIndex));
			while ( ++depth < this.maxDirectoryDepth ) {
				Path childPath = (new File(activePath.toFile(),
						"deconcat_" + DECIMAL_FORMATTER.format(depth) + "_"
								+ DECIMAL_FORMATTER.format(0))).toPath();
				activePath = Files.createDirectories(childPath);
				this.storagePathStack.peek().filesInDirectory++;
				this.storagePathStack
						.push(new StoragePath(activePath, 0, depth, 0));
			}
		}
		catch (Exception e) {
			GeneralHelper.errorWriter(
					"Unable to build stack for storing paths to directories");
		}
	}

	private void repopulateStoragePathStack() throws IOException {

		int previousStoreagePathIndex = updateStack();
		int depth = this.storagePathStack.size();
		int directoryIndex = previousStoreagePathIndex + 1;

		StoragePath topPath = this.storagePathStack.peek();
		topPath.filesInDirectory++;

		File directoryFile = new File(topPath.pathToDirectory.toFile(),
				"deconcat_" + DECIMAL_FORMATTER.format(depth) + "_"
						+ DECIMAL_FORMATTER.format(directoryIndex));
		populateStoragePathStack(directoryFile, depth, directoryIndex);

	}

	/**
	 * Goes through the StoragePath stack to find a storage path containing less
	 * than the maximum number of files specified by maxDirectorySize
	 *
	 * @throws IOException
	 */
	private int updateStack() throws IOException {

		StoragePath storagePath = null;
		while ( !this.storagePathStack.isEmpty() && !(this.storagePathStack
				.peek().filesInDirectory < this.maxDirectorySize) ) {
			storagePath = this.storagePathStack.pop();

		}
		if ( this.storagePathStack.isEmpty() ) {
			GeneralHelper
					.errorWriter("Insufficient space allocated for storage");
			throw new IOException("Insufficient space allocated for storage");
		}

		return storagePath.directoryIndex;
	}

}
