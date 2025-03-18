import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class demonstrates file handling, including recursive traversal of directories and
 * generating markdown files for Java source code.
 */
public class FileHandling {
    public static Integer fileCountMain = 0;
    public static Integer fileCountTest = 0;
    // Path to the scheduler directory
    public static final String BASE_FOLDER_PATH = "C:\\Users\\gengi\\Documents\\llm-test-projects\\property-management\\";
    public static final String MAIN_MD_FILE_NAME = "main.md";
    public static final String TEST_MD_FILE_NAME = "test.md";

    public static void main(String[] args) {
        processBaseFolder(BASE_FOLDER_PATH);
        System.out.println("Total files processed: " + fileCountMain);
        System.out.println("Total files processed: " + fileCountTest);
        fileCountMain = 0;
        fileCountTest = 0;
    }

    /**
     * Processes the base folder and handles all top-level files and subdirectories.
     *
     * @param baseFolderPath the base folder to process
     */
    private static void processBaseFolder(String baseFolderPath) {
        try (Stream<Path> paths = Files.list(Paths.get(baseFolderPath))) {
            List<Path> sortedPaths = paths.sorted().toList();
            sortedPaths.forEach(path -> {
                if (Files.isDirectory(path)) {
                    handleDirectory(path);
                } else {
                    handleFile(path);
                }
            });
        } catch (IOException e) {
            logError("Error listing files in directory: " + baseFolderPath, e);
        }
    }

    /**
     * Recursively processes all subdirectories and files.
     *
     * @param folderPath the path to the folder to be processed
     */
    private static void handleDirectory(Path folderPath) {
        System.out.println("Directory: " + folderPath);
        processBaseFolder(folderPath.toString());
    }

    /**
     * Processes a single file by creating a markdown file if it matches the criteria.
     *
     * @param filePath the file path to process
     */
    private static void handleFile(Path filePath) {
        System.out.println("File: " + filePath);
        createMarkdownFile(filePath.toString());
    }

    /**
     * Creates a markdown file from a Java file. Test files and main files are written to separate markdown files.
     *
     * @param inputFilePath the path to the Java file
     */
    private static void createMarkdownFile(String inputFilePath) {
        String outputFilePath = getOutputFilePath(inputFilePath);

        try {
            if (inputFilePath.endsWith(".java")) {
                List<String> lines = Files.readAllLines(Paths.get(inputFilePath));
                String markdownContent = generateMarkdownContent(inputFilePath, lines);
                writeToFile(outputFilePath, markdownContent);
            }
        } catch (IOException e) {
            logError("Error creating markdown file for: " + inputFilePath, e);
        }
    }

    /**
     * Generates the output file path based on whether the file is a test or main file.
     *
     * @param inputFilePath the path to the input file
     * @return the output file path
     */
    private static String getOutputFilePath(String inputFilePath) {
        if (inputFilePath.endsWith(".java")) {
            if (inputFilePath.contains("Test")) {
                fileCountTest++;
                return BASE_FOLDER_PATH + File.separator + TEST_MD_FILE_NAME;
            } else {
                fileCountMain++;
                return BASE_FOLDER_PATH + File.separator + MAIN_MD_FILE_NAME;
            }
        }
        return null;
    }

    /**
     * Generates markdown content for the Java file.
     *
     * @param inputFilePath the path to the input file
     * @param lines         the lines of the file
     * @return the formatted markdown content
     */
    private static String generateMarkdownContent(String inputFilePath, List<String> lines) {
        StringBuilder markdownContent = new StringBuilder();
        String fileName = String.valueOf(Paths.get(inputFilePath).getFileName());
        Integer fileCount = fileName.contains("Test") ? fileCountTest : fileCountMain;
        markdownContent.append("**")
                .append(fileCount)
                .append(".")
                .append("**")
                .append("  ");
        markdownContent.append("**``")
                .append(fileName)
                .append("``**");
        markdownContent.append(": ");
        markdownContent.append("**")
                .append(inputFilePath.replace(BASE_FOLDER_PATH, ""))
                .append("**\n\n");
        markdownContent.append("```java\n");
        lines.forEach(line -> markdownContent.append(line).append("\n"));
        markdownContent.append("```\n\n");
        return markdownContent.toString();
    }

    /**
     * Writes the given content to the specified file.
     *
     * @param filePath the path to the file
     * @param content  the content to write
     */
    private static void writeToFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        System.out.println(filePath + " Markdown file created successfully!");
    }

    /**
     * Logs an error message and stack trace.
     *
     * @param message the error message
     * @param e       the exception
     */
    private static void logError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
