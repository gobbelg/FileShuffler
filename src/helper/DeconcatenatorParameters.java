package helper;

import java.io.File;

public record DeconcatenatorParameters(File fileForDeconcatenation, String fileSeparator,
    int maxDirectorySize, boolean precedingFileSeparator) {
}
