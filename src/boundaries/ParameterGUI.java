package boundaries;

import java.io.File;
import javax.swing.JOptionPane;
import helper.DeconcatenatorParameters;
import helper.GeneralHelper;

public class ParameterGUI {

  private static final String DEFAULT_FILE_SEPARATOR = "-------------------------";;
  private static final int DEFAULT_MAX_DIRECTORY_SIZE = 100;
  private static final boolean DEFAULT_PRECEDING_FILE_SEPARATOR = false;

  private ParameterGUI() {}

  public static File getFileForDeconcatenation() {
    return getFileForDeconcatenation(new String[0]);
  }

  public static File getFileForDeconcatenation(String[] args) {
    File fileForDeconcatenation = null;
    if (args != null && args.length > 0) {
      fileForDeconcatenation = new File(args[0]);
    }
    while (fileForDeconcatenation == null || !fileForDeconcatenation.isFile()
        || !fileForDeconcatenation.exists()) {
      fileForDeconcatenation = GeneralHelper.getFile("Select concatenated file");
    }
    return fileForDeconcatenation;
  }

  public static String getFileSeparator() {
    return getFileSeparator(new String[0]);
  }

  public static String getFileSeparator(String[] args) {
    String fileSeparator = null;
    if (args != null && args.length > 1) {
      fileSeparator = args[1];
    }
    while (fileSeparator == null || fileSeparator.isBlank()) {
      fileSeparator = DEFAULT_FILE_SEPARATOR;
      fileSeparator = JOptionPane.showInputDialog(null, fileSeparator);
    }
    return fileSeparator;
  }

  public static int getMaxDirectorySize() {
    return getMaxDirectorySize(new String[0]);
  }

  public static int getMaxDirectorySize(String[] args) {
    int maxDirectorySize = 0;
    if (args != null && args.length > 2) {
      maxDirectorySize = Integer.parseInt(args[2]);
    }
    while (maxDirectorySize < 1) {
      String maxSizeString = JOptionPane.showInputDialog(null, DEFAULT_MAX_DIRECTORY_SIZE);
      maxDirectorySize = Integer.parseInt(maxSizeString);
    }
    return maxDirectorySize;
  }

  public static DeconcatenatorParameters getParameters(String[] args) {
    File file = getFileForDeconcatenation(args);
    String separator = getFileSeparator(args);
    int maxDirectorySize = getMaxDirectorySize(args);
    boolean precedingFileSeparator = precedingSeparator(args);

    return new DeconcatenatorParameters(file, separator, maxDirectorySize, precedingFileSeparator);
  }

private static boolean precedingSeparator(String[] args) {
	boolean precedingSeparator = DEFAULT_PRECEDING_FILE_SEPARATOR;
    if (args != null && args.length > 3) {
    	precedingSeparator = Boolean.parseBoolean(args[3].toLowerCase());
    }
	return precedingSeparator;
}
}
