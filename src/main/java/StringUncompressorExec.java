
import org.apache.commons.cli.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class StringUncompressorExec {

    private static String readFile(String fileWithFullPath) throws IOException {

        String fileContents = new String(Files.readAllBytes(Paths.get(fileWithFullPath)));

        return fileContents;
    }

    private void writeStringToFile(String text, String fileName) throws IOException {

        FileWriter myFileWriter = new FileWriter(fileName);

        myFileWriter.write(text);
        myFileWriter.close();
    }

    public static void main(String[] args) {

        Options options = new Options();

        Option inputFileName = new Option("f", "filename", true, "input file name");
        inputFileName.setRequired(true);
        options.addOption(inputFileName);

        try {
            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();

            String inputText = "";

            CommandLine cmd;
            cmd = parser.parse(options, args);
            inputText = StringUncompressorExec.readFile(cmd.getOptionValue("filename"));

            // strip any trailing newlines
            if( inputText.charAt( inputText.length() - 1 ) == '\n' ) {
                inputText = inputText.substring(0, inputText.length() - 1 );
            }

            // uncompress

            StringPartUncompressor uncompressor = new StringPartUncompressor(inputText);
            System.out.println(uncompressor.getUncompressedString());

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("compressor", options);
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(2);
        }


    }

}
