package openDart;

import java.nio.file.Path;
import java.nio.file.Paths;

public class UnzipFile {
    public static void main(String[] args) {
        // get current directory path
        Path currentRelativePath = Paths.get("");
        String currentAbsoultePath = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + currentAbsoultePath);

        String zipFilePath = "download.zip";    // "download.zip" file in current directory
        String destDirectory = currentAbsoultePath;
        UnzipUtility unzipper = new UnzipUtility();
        try {
            // Extracts a zip file specified by the zipFilePath to a directory specified by destDirectory (will be created if does not exists)
            unzipper.unzip(zipFilePath, destDirectory);
        } catch (Exception ex) {
            // some errors occurred
            ex.printStackTrace();
        }
    }
}
