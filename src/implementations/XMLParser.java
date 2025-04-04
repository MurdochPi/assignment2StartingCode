package implementations;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * A basic XML parser that validates XML tag matching and detects malformed or mismatched tags.
 * 
 * <p>This parser performs a two-pass scan:
 * <ul>
 *   <li>Pass 1: Extracts and classifies tags from each line of input.</li>
 *   <li>Pass 2: Matches opening and closing tags, identifies unclosed or mismatched elements.</li>
 * </ul>
 * 
 * <p>It does not perform schema or DTD validation, but catches:
 * <ul>
 *   <li>Unclosed tags</li>
 *   <li>Unmatched closing tags</li>
 *   <li>Malformed syntax (e.g., extra characters, case mismatches)</li>
 *   <li>Self-closing tags</li>
 * </ul>
 * 
 * Errors are stored and can be printed using {@link #printErrors()}.
 * 
 * @author Lochlan Piercey
 * @author Murdoch Piercey
 * @author Terril Moyo
 * @version 1.0
 * @since 2025
 */
public class XMLParser {

    /**
     * Stores information about a parsed tag.
     */
    private static class TagInfo {
        String name;
        int line;
        boolean isStart;
        boolean isSelfClosing;
        boolean matched = false;
        String fullText;

        /**
         * Constructs metadata for a tag.
         *
         * @param name the tag name
         * @param line the line number it appears on
         * @param isStart true if it is a start tag
         * @param isSelfClosing true if it is a self-closing tag
         * @param fullText the full text of the tag, including angle brackets
         */
        TagInfo(String name, int line, boolean isStart, boolean isSelfClosing, String fullText) {
            this.name = name;
            this.line = line;
            this.isStart = isStart;
            this.isSelfClosing = isSelfClosing;
            this.fullText = fullText;
        }
    }

    /**
     * Represents a validation error message associated with a specific line.
     */
    private static class ErrorMessage {
        int line;
        String message;

        /**
         * Constructs an error message.
         *
         * @param line the line number where the error occurred
         * @param message the error description
         */
        ErrorMessage(int line, String message) {
            this.line = line;
            this.message = message;
        }
    }

    private List<TagInfo> allTags;
    private List<ErrorMessage> errors;

    /**
     * Constructs an empty XML parser.
     */
    public XMLParser() {
        allTags = new ArrayList<>();
        errors = new ArrayList<>();
    }

    /**
     * Parses the provided XML file and extracts tag information,
     * performing structural validation.
     *
     * @param filename the name or path of the XML file to parse
     */
    public void parseFile(String filename) {
        File file = new File(filename);
        if (!file.isAbsolute()) {
            file = new File(System.getProperty("user.dir"), filename);
        }

        if (!file.exists() || !file.isFile()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                extractTags(line.trim(), lineNumber);
                lineNumber++;
            }

            matchTags();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Extracts tags from a line of XML text and identifies basic malformations.
     *
     * @param line the line content
     * @param lineNumber the line number in the source file
     */
    private void extractTags(String line, int lineNumber) {
        Pattern tagPattern = Pattern.compile("<[^<>]+>");
        Matcher matcher = tagPattern.matcher(line);

        while (matcher.find()) {
            String tag = matcher.group();

            // Skip declarations and comments
            if (tag.startsWith("<?") && tag.endsWith("?>")) return;
            if (tag.startsWith("<!--") && tag.endsWith("-->")) return;

            if (!tag.matches("<[^<>]+>")) {
                errors.add(new ErrorMessage(lineNumber, "Malformed tag at line " + lineNumber + ": " + tag));
                continue;
            }

            boolean isSelfClosing = tag.matches("<[^>]+/\\s*>");
            boolean isEnd = tag.matches("</[^>]+>");
            boolean isStart = !isEnd && !isSelfClosing;

            String tagName = extractTagName(tag);

            if (tagName != null) {
                allTags.add(new TagInfo(tagName, lineNumber, isStart, isSelfClosing, tag));
            } else {
                errors.add(new ErrorMessage(lineNumber, "Unable to extract tag name at line " + lineNumber + ": " + tag));
            }
        }

        if (line.contains(">>")) {
            errors.add(new ErrorMessage(lineNumber, "Malformed tag sequence on line " + lineNumber + ": contains '>>'"));
        }
    }

    /**
     * Matches start and end tags, checking for proper nesting, unclosed tags,
     * and mismatched tag cases.
     */
    private void matchTags() {
        Stack<TagInfo> openTags = new Stack<>();

        for (TagInfo tag : allTags) {
            if (tag.isSelfClosing) {
                tag.matched = true;
                continue;
            }

            if (tag.isStart) {
                openTags.push(tag);
            } else {
                boolean matched = false;
                for (int i = openTags.size() - 1; i >= 0; i--) {
                    TagInfo candidate = openTags.get(i);
                    if (!candidate.matched && candidate.name.equals(tag.name)) {
                        candidate.matched = true;
                        tag.matched = true;

                        for (int j = openTags.size() - 1; j > i; j--) {
                            TagInfo orphan = openTags.get(j);
                            if (!orphan.matched) {
                                errors.add(new ErrorMessage(orphan.line, "Unclosed tag <" + orphan.name + "> from line " + orphan.line));
                                orphan.matched = true;
                            }
                        }

                        while (openTags.size() > i) {
                            openTags.pop();
                        }

                        matched = true;
                        break;
                    }
                }

                if (!matched) {
                    errors.add(new ErrorMessage(tag.line, "Unmatched closing tag at line " + tag.line + ": " + tag.fullText));

                    for (TagInfo open : allTags) {
                        if (open.isStart && !open.matched &&
                            open.name.equalsIgnoreCase(tag.name) &&
                            !open.name.equals(tag.name)) {

                            errors.add(new ErrorMessage(tag.line,
                                "Possible malformed tag at line " + tag.line + ": " + tag.fullText +
                                " — mismatched case with <" + open.name + "> from line " + open.line));
                            break;
                        }
                    }
                }
            }
        }

        for (TagInfo tag : openTags) {
            if (!tag.matched) {
                errors.add(new ErrorMessage(tag.line, "Unclosed tag <" + tag.name + "> from line " + tag.line));
            }
        }
    }

    /**
     * Extracts a tag name from a full tag string.
     *
     * @param tag the full tag text
     * @return the tag name, or {@code null} if extraction fails
     */
    private String extractTagName(String tag) {
        tag = tag.replaceAll("[</>]", "").trim();
        if (tag.isEmpty()) return null;
        return tag.split("\\s+")[0];
    }

    /**
     * Prints all collected errors to the standard output.
     * If no errors are found, a success message is printed.
     */
    public void printErrors() {
        if (errors.isEmpty()) {
            System.out.println("XML parsed successfully. No errors found.");
        } else {
            errors.sort(Comparator.comparingInt(e -> e.line));
            for (ErrorMessage error : errors) {
                System.out.println(error.message);
            }
        }
    }
}
