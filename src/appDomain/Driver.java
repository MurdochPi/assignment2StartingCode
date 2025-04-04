package appDomain;

import implementations.XMLParser;

public class Driver {
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
