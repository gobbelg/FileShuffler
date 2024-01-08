package helper;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * ********************************************************** Helper - Static helper methods for the
 * Bayes algorithms
 *
 * @author Glenn Gobbel, Jul 23, 2010
 *         <p>
 *         *********************************************************
 */
public class GeneralHelper<T> {
  private static long ticTime;


  /**
   * ************************************************************** return strings identical to the
   * input strings except that all characters are lower case
   *
   * @param inputStrings
   * @return **************************************************************
   */
  public static String[] allToLowerCase(final String[] inputStrings) {
    String[] outputStrings = new String[inputStrings.length];
    for (int i = 0; i < inputStrings.length; i++) {
      outputStrings[i] = inputStrings[i].toLowerCase(Locale.ENGLISH);
    }
    return outputStrings;
  }


  /**
   * ************************************************************** return strings identical to the
   * input strings except that all characters are upper case
   * **************************************************************
   */
  public static String[] allToUpperCase(final String[] inputStrings) {
    String[] outputStrings = new String[inputStrings.length];
    for (int i = 0; i < inputStrings.length; i++) {
      outputStrings[i] = inputStrings[i].toUpperCase(Locale.ENGLISH);
    }
    return outputStrings;
  }


  public static <T> boolean arrayContains(final T[] theArray, final T obj) {
    for (T curObj : theArray) {
      if (curObj.equals(obj)) {
        return true;
      }
    }
    return false;
  }


  /**
   * ************************************************************** Create a single string from the
   * array of attributes and return it with the delimiter between the strings
   *
   * <p>
   * **************************************************************
   */
  public static String arrayToDelimString(final String[] inArray, final String delimiter) {
    StringBuilder resultSB = new StringBuilder(inArray[0]);
    for (int i = 1; i < inArray.length; i++) {
      resultSB.append(delimiter + inArray[i]);
    }

    String resultString = new String(resultSB);
    return resultString;
  }


  /**
   * ************************************************************** Create a single string from the
   * array of attributes and return it with spaces in between the attributes
   *
   * <p>
   * **************************************************************
   */
  public static String arrayToString(final String[] inArray) {
    StringBuilder resultSB = new StringBuilder(inArray[0]);
    for (int i = 1; i < inArray.length; i++) {
      resultSB.append(" " + inArray[i]);
    }

    String resultString = new String(resultSB);
    return resultString;
  }

  public static double calcF_Measure(final int truePositive, final int falsePositive,
      final int falseNegative) {
    if (truePositive < 1 && falsePositive < 1 && falseNegative < 1) {
      return Double.NaN;
    } else {
      return 2.0 * truePositive / (2.0 * truePositive + falsePositive + falseNegative);
    }
  }


  public static double calcPrecision(final int truePositive, final int falsePositive) {
    if (truePositive < 1 & falsePositive < 1) {
      return Double.NaN;
    } else {
      return (double) truePositive / (truePositive + falsePositive);
    }
  }


  public static double calcRecall(final double truePositives, final double falseNegatives) {
    if (truePositives < 1 & falseNegatives < 1) {
      return Double.NaN;
    } else {
      return truePositives / (truePositives + falseNegatives);
    }
  }


  /**
   * *********************************************************** Join two arrays, array1 and array2,
   * so that element n in array1 is replace by the concatenation of array1[n] + "joiner" + array2[n]
   *
   * @param array1
   * @param array2
   * @param joiner
   * @author Glenn Gobbel - Mar 11, 2012 ***********************************************************
   */
  public static void concatenateArrays(final String[] array1, final String[] array2,
      final String joiner) {
    for (int i = 0; i < array1.length && i < array2.length; i++) {
      array1[i] += joiner + array2[i];
    }
  }


  /**
   * *********************************************************** Join two arrays, array1 and array2,
   * so that element n in array1 is replace by the concatenation of array1[n] + "joiner" +
   * array2[n]. However, only concatenate elements in array2 at the indices specified by the
   * keptStringsInArray1 array
   *
   * @param array1
   * @param array2
   * @param keptStringsInArray2
   * @param joiner
   * @author Glenn Gobbel - Mar 11, 2012 ***********************************************************
   */
  public static void concatenateArraysByIndex(final String[] array1, final String[] array2,
      final int[] keptStringsInArray2, final String joiner) {
    for (int i = 0; i < keptStringsInArray2.length; i++) {
      array1[i] += joiner + array2[keptStringsInArray2[i]];
    }
  }


  /**
   * Includes timestamp with the user given file name. Removes any "_UPDATE_" string from the user
   * given file name to avoid conflict during the update process file naming.
   *
   * @param candidateFile The file given by the user
   * @return File with timestamp included in it's name
   * @author Sanjib Saha, July 22, 2014
   */
  public static File correctFileName(final File candidateFile) {
    // Removing the file extension to include the timestamp in the file name
    String fileName = candidateFile.getName().replaceAll("\\.soln", "");

    // Removing any "_UPDATE_" string from file name to avoid confusion
    // in the updateSolution
    fileName = fileName.replaceAll("_UPDATE_", "");

    int dotIndex = fileName.indexOf(".");

    if (dotIndex > -1) {
      fileName = fileName.substring(0, dotIndex);
    }

    if (fileName.length() < 1) {
      return null;
    }

    String directoryName = candidateFile.getParentFile().getAbsolutePath();
    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    return new File(directoryName + File.separator + fileName + "_" + timeStamp + ".soln");
  }


  /**
   * ************************************************************** Count the number of lines in a
   * file. This is a slower but more consistent way to count lines across different operating
   * systems
   *
   * @param theFile
   * @return Number of Lines as a long integer
   * @throws IOException **************************************************************
   */
  public static long countFileLines(final File theFile) throws IOException {
    Scanner is = new Scanner(theFile);
    int count = 0;

    try {
      while (is.hasNextLine()) {
        is.nextLine();
        count++;
      }
    } catch (Exception e) {
      System.out.println("Error counting lines in file");
      return 0;
    } finally {
      if (is != null) {
        is.close();
      }
    }

    return count;
  }


  /**
   * ************************************************************** Count the number of lines in a
   * file. This is just happens to be a more efficient way of doing it instead of reading line by
   * line. However, unlike countEmptyFileLines(), this method only counts lines that have some info
   * between newline or linefeed characters. This 'should' work on all modern systems in spite of
   * the way they handle/interpret newlines. This still needs to be debugged more carefully to
   * handle multiple return characters in a row.
   *
   * @return Number of Lines as a long integer
   *         **************************************************************
   */
  public static long countNonEmptyFileLines(final File theFile) throws IOException {
    InputStream is = new BufferedInputStream(new FileInputStream(theFile));
    try {
      byte[] c = new byte[1024];
      long count = 0;
      int readChars = 0;
      boolean hasChars = false;
      boolean newlineAtEnd = true;
      long lastFound = -1;
      long totalRead = 0;
      while ((readChars = is.read(c)) != -1) {
        newlineAtEnd = false;
        hasChars = readChars != 0;
        for (int i = 0; i < readChars; ++i) {
          if (c[i] == '\r' || c[i] == '\n') {
            // Don't count two return characters in a row
            if (i + totalRead - lastFound > 1) {
              ++count;
              // System.out.println("Line number: " + count);
              lastFound = i + totalRead;
            }
            // Mark that the last character in the read was a
            // newline character
            if (i == readChars - 1) {
              newlineAtEnd = true;
            }
          }
        }
        totalRead += readChars;
      }

      // Account for case where last line has characters but no newline
      // character
      if (!newlineAtEnd && hasChars) {
        count++;
      }
      return count;
    } finally {
      is.close();
    }
  }


  /**
   * @param inputList
   * @param totalSamples
   * @param <T>
   * @return
   */
  public static <T> List<List<T>> createRandomSamples(final List<T> inputList,
      final int totalSamples) {
    Collections.shuffle(inputList, new Random(System.currentTimeMillis()));
    int sampleSize = Math.round((float) inputList.size() / totalSamples);
    java.util.Iterator<T> iterator = inputList.iterator();
    int curIndex = 0;
    List<List<T>> randomLists = new ArrayList<>(totalSamples);
    List<T> innerList = null;
    while (curIndex + sampleSize < inputList.size()) {
      innerList = new ArrayList<>(2 * sampleSize);
      for (int i = 0; i < sampleSize; i++, curIndex++) {
        innerList.add(iterator.next());
      }
      randomLists.add(innerList);
    }
    while (iterator.hasNext()) {
      innerList.add(iterator.next());
    }

    return randomLists;
  }


  public static String[] dblToStringArray(final double[] dblArray) {
    String[] resultArray = new String[dblArray.length];

    for (int i = 0; i < dblArray.length; i++) {
      resultArray[i] = Double.toString(dblArray[i]);
    }

    return resultArray;
  }


  public static String doubleToPString(final double value) {
    Double.toString(value);
    return String.format("%5.2f", value).replace(".", "p");
  }


  // Provide standard way to report errors
  public static void errorWriter(final String theError) {
    JOptionPane.showMessageDialog(null, theError);
  }


  public static List<String> fileToPathList(final List<File> fileList) {
    List<String> returnList = new ArrayList<>(fileList.size());
    for (File curFile : fileList) {
      returnList.add(curFile.getAbsolutePath());
    }
    return returnList;
  }


  public static int findMinimumDifference(final Set<Integer> firstSet,
      final Set<Integer> secondSet) {
    int minimumDistance = Integer.MAX_VALUE;
    for (int firstInt : firstSet) {
      for (int secondInt : secondSet) {
        int distance = Math.abs(firstInt - secondInt);
        if (distance < minimumDistance) {
          minimumDistance = distance;
        }
      }
    }
    return minimumDistance;
  }


  public static String getAbsoluteFilePathFromURL(final URL url) {
    String pathToUrl = null;

    pathToUrl = url.getAuthority() + url.getPath();

    return new File(pathToUrl).getAbsolutePath();
  }


  /**
   * ************************************************************** Generate a dialog to select a
   * directory and return it as a file
   *
   * @return The chosen file or null if cancel is pressed
   *         **************************************************************
   */
  public static File getDirectory(final String thePrompt) {
    JFileChooser fc = new JFileChooser();
    fc.setDialogTitle(thePrompt);
    fc.setFileHidingEnabled(true);
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int fileChosen = fc.showOpenDialog(null);
    return getFile(fc, fileChosen);
  }


  /**
   * ************************************************************** Generate a dialog to select a
   * directory and return it as a file
   *
   * @return The chosen file or null if cancel is pressed
   *         **************************************************************
   */
  public static File getDirectory(final String thePrompt, final String pathToStartDirectory) {
    JFileChooser fc = new JFileChooser(pathToStartDirectory);
    fc.setDialogTitle(thePrompt);
    fc.setFileHidingEnabled(true);
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int fileChosen = fc.showOpenDialog(null);
    return getFile(fc, fileChosen);
  }



  /**
   * ************************************************************** Returns the path to a file
   * without the file name itself. If the full path to the file is "C://A/B/theFile.txt", this
   * returns the string "C://A/B/".
   *
   * @return A string containing just the path to the directory containing a file
   *         **************************************************************
   */
  public static String getDirectoryOfFile(final String fullFilePath) {
    String fileDirectory = "";
    String sysFileSeparator = File.separator;
    int nameStart = fullFilePath.lastIndexOf(sysFileSeparator) + sysFileSeparator.length();
    fileDirectory = fullFilePath.substring(0, nameStart);
    return fileDirectory;
  }


  /**
   * *********************************************************** Given a list of arrays, this
   * returns a list in which each element of the returned lists contains only one element at
   * theElement index in the original list. So, given a list of string arrays with theElement set to
   * 3, if would return a list of strings where each string is the 3rd element in each array in the
   * original list.
   *
   * @param theList
   * @param theElement
   * @author Glenn Gobbel - Jun 20, 2012 ***********************************************************
   */
  public static <T> List<T> getElementsFromListOfArrays(final List<T[]> theList,
      final int theElement) {
    List<T> theResults = new ArrayList<>(theList.size());

    for (T[] curArray : theList) {
      theResults.add(curArray[theElement]);
    }

    return theResults;
  }


  /**
   * ************************************************************** Generate a dialog to retrieve a
   * file
   *
   * @return The chosen file or null if cancel is pressed
   *         **************************************************************
   */
  public static File getFile(final String thePrompt) {
    return getFile(thePrompt, "");
  }


  public static File getFile(final String thePrompt, final String baseFileDirectory) {
    File returnFile = null;

    JFileChooser fc = new JFileChooser(baseFileDirectory);
    fc.setDialogTitle(thePrompt);
    fc.setFileHidingEnabled(true);
    int fileChosen = fc.showOpenDialog(null);
    return getFile(fc, fileChosen);
  }


  /**
   * *********************************************************** Create a file filter supplying the
   * accepted extensions as an array of strings.
   *
   * @param theExtensions
   * @return
   * @author Glenn Gobbel - Jul 6, 2012 ***********************************************************
   */
  public static java.io.FileFilter getFileFilter(final String... theExtensions) {
    return new java.io.FileFilter() {
      @Override
      public boolean accept(final File pathname) {
        String theName = pathname.getName();
        for (String curExtension : theExtensions) {
          if (theName.endsWith(curExtension)) {
            return true;
          }
        }
        return false;
      }
    };
  }


  /**
   * *********************************************************** Create a file filter supplying the
   * accepted extensions as an array of strings.
   *
   * @param theExtensions
   * @return
   * @author Glenn Gobbel - Jul 6, 2012 ***********************************************************
   */
  public static java.io.FilenameFilter getFilenameFilter(final String... theExtensions) {
    return new java.io.FilenameFilter() {
      @Override
      public boolean accept(final File dir, final String name) {
        for (String curExtension : theExtensions) {
          if (name.endsWith(curExtension)) {
            return true;
          }
        }
        return false;
      }
    };
  }


  /**
   * ************************************************************** Generate a dialog to retrieve
   * the path to a file
   *
   * @return The chosen file or null if cancel is pressed
   *         **************************************************************
   */
  public static String getFilePath(final String thePrompt, final FileFilter theFilter) {
    JFileChooser fc = new JFileChooser();
    if (theFilter != null) {
      fc.addChoosableFileFilter(theFilter);
    }
    fc.setDialogTitle(thePrompt);
    fc.setFileHidingEnabled(true);
    int fileChosen = fc.showOpenDialog(null);
    File chosenFile = getFile(fc, fileChosen);
    return chosenFile == null ? null : chosenFile.getAbsolutePath();
  }


  public static File[] getFilesInDirectory(final String thePrompt, final String[] fileExtensions) {
    File theDirectory = getDirectory(thePrompt);

    if (theDirectory != null && theDirectory.isDirectory()) {
      return theDirectory.listFiles(getFileFilter(fileExtensions));
    }
    {
      errorWriter("No Directory Chosen");
      return null;
    }
  }


  /**
   * ************************************************************** Generate a dialog to get a file
   * for storing data
   *
   * @return The chosen canonical path to the file or null if cancel is pressed
   *         **************************************************************
   */
  public static String getNewFileName(final String thePrompt, final String prefix,
      final String suffix) {
    String resultName = null;
    try {
      resultName = GeneralHelper.getDirectory(thePrompt).getCanonicalPath();
      resultName += File.separator + prefix + System.currentTimeMillis() + suffix;
    } catch (IOException e) {
      System.out.println(e);
      e.printStackTrace();
    }
    return resultName;
  }


  /**
   * *********************************************************** Return the maximum overlap of one
   * string over another. For the two strings under consideration, s1 and s2, s2 must start at
   * position 0 or later of s1, and all characters beyond the matching position must match s1.
   *
   * @param s1
   * @param s2
   * @return
   * @author Glenn Gobbel - May 4, 2012 ***********************************************************
   */
  public static String getOverlap(final String s1, final String s2) {
    int beginIndex = 0;
    int s1Length = s1.length();

    // Just return s2 if it is completely contained within s1
    if (s1.indexOf(s2) != -1) {
      return s2;
    }

    // Otherwise, take substrings of s1 starting at its beginning and
    // find the first one that matches that start of s2
    while (beginIndex < s1Length && s2.indexOf(s1.substring(beginIndex)) != 0) {
      beginIndex++;
    }

    if (!(beginIndex < s1Length)) {
      return null;
    }

    return s1.substring(beginIndex);
  }


  public static String getSelectedButtonName(final ButtonGroup theButtonGroup) {
    Enumeration<AbstractButton> buttonElements = theButtonGroup.getElements();
    while (buttonElements.hasMoreElements()) {
      AbstractButton theButton = buttonElements.nextElement();
      if (theButton.isSelected()) {
        return theButton.getText().toLowerCase();
      }
    }
    return null;
  }


  /**
   * ************************************************************** Picks a subarray from a String
   * array from the index start to the end of the array. Precondition: The input 'start' parameters
   * must be less than the length of the input array. Returns null if not.
   *
   * @param inArray - String array to pick out subelements from
   * @param start - Starting index to select element
   * @return String [] - Returned String subarray.
   *         **************************************************************
   */
  public static char[] getSubArray(final char[] inArray, final int start) {
    char[] resultArray = null;
    if (inArray != null) {
      int resultArrayLength = inArray.length - start;
      if (resultArrayLength > 0) {
        resultArray = new char[resultArrayLength];
        System.arraycopy(inArray, start, resultArray, 0, resultArrayLength);
      }
    }
    return resultArray;
  }


  /**
   * ************************************************************** Picks a subarray from a String
   * array from the index start to the end of the array. Precondition: The input 'start' parameters
   * must be less than the length of the input array. Returns null if not.
   *
   * @param inArray - String array to pick out subelements from
   * @param start - Starting index to select element
   * @return String [] - Returned String subarray.
   *         **************************************************************
   */
  public static String[] getSubArray(final String[] inArray, final int start) {
    String[] resultArray = null;
    if (inArray != null) {
      int resultArrayLength = inArray.length - start;
      if (resultArrayLength > 0) {
        resultArray = new String[resultArrayLength];
        System.arraycopy(inArray, start, resultArray, 0, resultArrayLength);
      }
    }
    return resultArray;
  }


  /**
   * ************************************************************** Picks a subarray from a String
   * array and returns "length" elements starting with element start.
   *
   * <p>
   * Precondition: Parameter 'start' must be within 'inArray,' so start must be less than length of
   * inArray. Also, 'start' plus 'resultLength' must not be greater than the length of inArray (to
   * avoid trying to copy outside the bounds of the array). Finally, resultLength must be > 0.
   * Returns null if any of these conditions are not met.
   *
   * @param inArray - String array to pick out subelements from
   * @param start - Starting index to select element
   * @param resultLength - Number of elements to select
   * @return String [] - Returned String subarray
   *         **************************************************************
   */
  public static String[] getSubArray(final String[] inArray, final int start,
      final int resultLength) {
    String[] resultArray = null;
    if (resultLength > 0 && inArray != null && start < inArray.length
        && start + resultLength <= inArray.length) {
      resultArray = new String[resultLength];
      System.arraycopy(inArray, start, resultArray, 0, resultLength);
      return resultArray;
    }
    return resultArray;
  }


  public static String getTimeStamp() {
    DateFormat dateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
    Date date = new Date();
    return dateFormat.format(date);
  }


  /*
   * Get a timeStamp
   */
  public static String getTimeStampLong() {
    Calendar calendar = Calendar.getInstance();
    String timeStamp = calendar.getTime().toString();
    return timeStamp;
  }


  public static String intArrayToString(final int[] input) {
    StringBuffer result = new StringBuffer("");
    for (int i = 0; i < input.length; result
        .append(input[i++] + System.getProperty("line.separator"))) {

    }
    return result.toString();
  }


  public static String[] intToStringArray(final int[] intArray) {
    String[] resultArray = new String[intArray.length];

    for (int i = 0; i < intArray.length; i++) {
      resultArray[i] = Integer.toString(intArray[i]);
    }

    return resultArray;
  }


  public static void main(final String[] args) {}


  public static int maxAbsoluteValue(final int[] intArray) {
    assert intArray.length > 0;
    int curMax = 0;
    for (int curValue : intArray) {
      int absCurValue = Math.abs(curValue);
      if (absCurValue > curMax) {
        curMax = absCurValue;
      }
    }
    return curMax;
  }


  public static void printException(final Exception exception) {
    System.err.println("Error:" + exception.getMessage());
    exception.printStackTrace();

  }


  /**
   * Removes duplicates from a list, returning another list with the same order as the original
   * except that additional duplicates after the first instance are removed.
   *
   * @param elements
   * @return
   */
  public static <T> List<T> removeDuplicates(final List<T> elements) throws Exception {
    if (elements instanceof ArrayList) {
      return new ArrayList<>(new LinkedHashSet<>(elements));
    }
    if (elements instanceof LinkedList) {
      return new LinkedList<>(new LinkedHashSet<>(elements));
    }
    if (elements instanceof Vector) {
      return new Vector<>(new LinkedHashSet<>(elements));
    } else {
      throw new Exception("removeDuplicates() method does not support this list type");
    }
  }


  /**
   * ************************************************************** Takes a file name as a string
   * and removes all characters after a given character and the character itself. It looks for the
   * character starting at the back of the string
   *
   * @return A string containing the file without any characters after the extension mark character
   *         or the extension itself **************************************************************
   */
  public static String removeFileExtension(final String fileName, final char extensionSeparator) {
    if (fileName.indexOf(extensionSeparator) == -1) {
      return fileName;
    }
    return fileName.substring(0, fileName.lastIndexOf(extensionSeparator));
  }


  /**
   * ************************************************************** Replace nulls in input string
   * with values in replacement string
   * **************************************************************
   */
  public static void replaceNulls(final String[] inputStrings, final String[] replacementStrings) {
    for (int i = 0; i < inputStrings.length; i++) {
      if (inputStrings[i] == null) {
        inputStrings[i] = replacementStrings[i];
      }
    }
  }


  /**
   * *********************************************************** Determine the indices of the
   * strings in startString [] that are also found in the tester string. Precondition: All strings
   * in tester should be in startString and they should be in the order they would be found in
   * startString
   *
   * @param startString
   * @param tester
   * @return int []
   * @author Glenn Gobbel - Mar 11, 2012 ***********************************************************
   */
  public static int[] sameStrings(final String[] startString, final String[] tester) {
    // if (tester.length < 1)
    // {
    // return null;
    // }
    //
    int[] result = new int[tester.length];
    int startStringIndex = 0, testerIndex = 0, resultIndex = 0;

    while (startStringIndex < startString.length && testerIndex < tester.length) {
      if (startString[startStringIndex].equalsIgnoreCase(tester[testerIndex])) {
        result[resultIndex++] = startStringIndex;
        startStringIndex++;
        testerIndex++;
      } else {
        startStringIndex++;
      }
    }

    return result;
  }



  /**
   * Feb 20, 2018
   *
   * @param annotationAcceptedConcepts
   * @param trainingAcceptedConcepts
   * @return
   */
  public static <T> boolean setIsSubset(final Set<T> subsetCandidate,
      final Set<T> supersetCandidate) {
    for (T element : subsetCandidate) {
      if (!supersetCandidate.contains(element)) {
        return false;
      }
    }

    return true;
  }


  public static <T> boolean setsOverlap(final Set<T> left, final Set<T> right) {
    Set<T> shortSet = left.size() < right.size() ? left : right;

    for (T item : shortSet) {
      if (right.contains(item)) {
        return true;
      }
    }
    return false;
  }


  /*
   * Method copied from https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-
   * java to sort a map by its values
   */
  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
    List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
      @Override
      public int compare(final Map.Entry<K, V> o1, final Map.Entry<K, V> o2) {
        return o1.getValue().compareTo(o2.getValue());
      }
    });

    Map<K, V> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : list) {
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }


  /*
   * Method copied from https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-
   * java to sort a map by its values
   */
  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map,
      final boolean flipSort) {
    if (!flipSort) {
      return sortByValue(map);
    }
    List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
      @Override
      public int compare(final Map.Entry<K, V> o1, final Map.Entry<K, V> o2) {
        return o2.getValue().compareTo(o1.getValue());
      }
    });

    Map<K, V> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : list) {
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }


  public static String stringListToString(final List<String> inputString) {
    Iterator<String> stringIterator = inputString.iterator();
    StringBuilder sb = new StringBuilder(stringIterator.hasNext() ? stringIterator.next() : "");

    while (stringIterator.hasNext()) {
      sb.append(" ").append(stringIterator.next());
    }

    return sb.toString();
  }


  public static String subArrayToDelimString(final String[] inArray, final int start,
      final int length, final String delimiter) throws Exception {
    if (length < 1) {
      return "";
    }

    if (start + length > inArray.length) {
      throw new Exception("Unable to convert array to string - length of subarray too long");
    }

    StringBuilder resultSB = new StringBuilder(inArray[start]);
    for (int i = start + 1; i < inArray.length && i < start + length; i++) {
      resultSB.append(delimiter + inArray[i]);
    }

    String resultString = new String(resultSB);
    return resultString;
  }


  /**
   * This could probably be rewritten in a recursive manner to deal with any dimensional array of a
   * generic. The base case would be when the dimension of the input array was 1 or 0.
   *
   * @param inArray
   * @return
   */
  public static int[][][] threeDimIntArrayCopy(final int[][][] inArray) {
    int[][][] outArray = new int[inArray.length][][];
    for (int i = 0; i < inArray.length; i++) {
      int iArrayLength = inArray[i].length;
      outArray[i] = new int[iArrayLength][];
      for (int j = 0; j < iArrayLength; j++) {
        int ijArrayLength = inArray[i][j].length;
        outArray[i][j] = new int[ijArrayLength];
        for (int k = 0; k < ijArrayLength; k++) {
          outArray[i][j][k] = inArray[i][j][k];
        }
      }
    }

    return outArray;
  }


  /**
   * *********************************************** tic Starts a timer. Duration is printed in
   * response to toc().
   *
   * @param event - String representing the event being timed
   * @author Glenn T. Gobbel - Dec 14, 2011 ***********************************************
   */
  public static void tic(final String event) {
    GeneralHelper.ticTime = System.currentTimeMillis();
    if (event.length() > 0) {
      System.out.println("\nTimer started:  " + event);
    }
  }


  public static void toc() {
    long duration = System.currentTimeMillis() - GeneralHelper.ticTime;

    System.out.println("Duration: " + duration + " milliseconds");
  }


  public static void toc(final String textString) {
    System.out.println("Timer stopped:" + textString);
    toc();
  }


  /**
   * *********************************************** writeStringArrayList
   *
   * @param theList
   * @author Glenn T. Gobbel - Dec 14, 2011 ***********************************************
   */
  public static void writeList(final List<String[]> theList) {
    String curString;
    ListIterator<String[]> theIterator = theList.listIterator();
    try {
      FileWriter fWriter = new FileWriter("ListTest_" + System.currentTimeMillis(), false);
      PrintWriter pw = new PrintWriter(new BufferedWriter(fWriter));
      while (theIterator.hasNext()) {
        curString =
            arrayToDelimString(theIterator.next(), ",") + System.getProperty("line.separator");
        pw.print(curString);
      }
      pw.flush();
      pw.close();
      fWriter.close();
    } catch (IOException e) {
      System.out.println("Unable to create file in method WriteList()");
      e.printStackTrace();
    }
  }


  /**
   * *********************************************** writeVectorAtInterval
   *
   * @param <T>
   * @param fileName
   * @param inVector
   * @param interval
   * @author Glenn T. Gobbel - Dec 14, 2011 ***********************************************
   */
  public static <T> void writeVectorAtInterval(final String fileName, final Vector<T> inVector,
      final int interval) {
    File writeFile = new File(fileName);
    long curTime = 0;
    PrintWriter pw = null;

    try {
      pw = new PrintWriter(new BufferedWriter(new FileWriter(writeFile, false)), true);

      for (Enumeration<T> e = inVector.elements(); e.hasMoreElements();) {
        pw.println(curTime + "," + e.nextElement().toString());
        curTime += interval;
      }
    } catch (IOException e) {
      System.out.println("Unable to write vector to file");
      e.printStackTrace();
      System.exit(0);
    } finally {
      if (pw != null) {
        pw.close();
      }
    }
  }


  private static File getFile(JFileChooser fc, int fileChosen) {
    if (!(fileChosen == JFileChooser.APPROVE_OPTION)) {
      return null;
    }
    File file = fc.getSelectedFile();
    if (!Files.isSymbolicLink(file.toPath())) {
      return file;
    }
    try {
      Path filePath = Files.readSymbolicLink(file.toPath());
      return filePath.toFile();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
