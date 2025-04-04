package implementations;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class XMLParser {
    private static class TagInfo {
        String name;
        int line;
        boolean isStart;
        boolean isSelfClosing;
        boolean matched = false;
        String fullText;

        TagInfo(String name, int line, boolean isStart, boolean isSelfClosing, String fullText) {
            this.name = name;
            this.line = line;
            this.isStart = isStart;
            this.isSelfClosing = isSelfClosing;
            this.fullText = fullText;
        }
    }

    private static class ErrorMessage {
        int line;
        String message;

        ErrorMessage(int line, String message) {
            this.line = line;
            this.message = message;
        }
    }

    private List<TagInfo> allTags;
    private List<ErrorMessage> errors;

    public XMLParser() {
        allTags = new ArrayList<>();
        errors = new ArrayList<>();
    }

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

        // Detect multiple >> or trailing garbage after tags
        if (line.contains(">>")) {
            errors.add(new ErrorMessage(lineNumber, "Malformed tag sequence on line " + lineNumber + ": contains '>>'"));
        }
    }

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
                // Look back for a matching start tag
                boolean matched = false;
                for (int i = openTags.size() - 1; i >= 0; i--) {
                    TagInfo candidate = openTags.get(i);
                    if (!candidate.matched && candidate.name.equals(tag.name)) {
                        // Match found
                        candidate.matched = true;
                        tag.matched = true;

                        // Any in-between tags are now unclosed
                        for (int j = openTags.size() - 1; j > i; j--) {
                            TagInfo orphan = openTags.get(j);
                            if (!orphan.matched) {
                                errors.add(new ErrorMessage(orphan.line, "Unclosed tag <" + orphan.name + "> from line " + orphan.line));
                                orphan.matched = true;
                            }
                        }

                        // Remove everything down to the match
                        while (openTags.size() > i) {
                            openTags.pop();
                        }

                        matched = true;
                        break;
                    }
                }

                if (!matched) {
                    errors.add(new ErrorMessage(tag.line, "Unmatched closing tag at line " + tag.line + ": " + tag.fullText));

                    // Case mismatch suggestion
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

        // Remaining unmatched start tags
        for (TagInfo tag : openTags) {
            if (!tag.matched) {
                errors.add(new ErrorMessage(tag.line, "Unclosed tag <" + tag.name + "> from line " + tag.line));
            }
        }
    }

    private String extractTagName(String tag) {
        tag = tag.replaceAll("[</>]", "").trim();
        if (tag.isEmpty()) return null;
        return tag.split("\\s+")[0];
    }

    public void printErrors() {
        if (errors.isEmpty()) {
            System.out.println("XML parsed successfully. No errors found.");
        } else {
            // Sort by line number
            errors.sort(Comparator.comparingInt(e -> e.line));
            for (ErrorMessage error : errors) {
                System.out.println(error.message);
            }
        }
    }
}
