package appDomain;

import implementations.XMLParser;

/**
 * The {@code Driver} class serves as the entry point for running the XML parsing application.
 * It takes a single command-line argument specifying the path to an XML file, invokes the
 * {@link XMLParser} to parse the file, and then prints any errors found during parsing.
 *
 * <p>Usage:
 * <pre>{@code
 * java -jar parser.jar <xml-file>
 * }</pre>
 *
 * <p>Example:
 * <pre>{@code
 * java -jar parser.jar sample.xml
 * }</pre>
 * 
 * @version 1.0
 * 
 * @author Lochlan Piercey
 * @author Murdoch Piercey
 * @author Terril Moyo
 */
public class Driver {

    /**
     * The main method that starts the XML parser.
     * 
     * @param args the command-line arguments; expects exactly one argument (the XML file path)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar parser.jar <xml-file>");
            return;
        }

        String filename = args[0];
        XMLParser parser = new XMLParser();
        parser.parseFile(filename);
        parser.printErrors();
    }
}
