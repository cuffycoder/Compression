
import org.apache.commons.cli.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class StringCompressorExec {

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

        Option largestSegmentValue = new Option( "s", "segmentLength", true, "the size of frequently occurring strings to match" );
        largestSegmentValue.setRequired( false );
        options.addOption(largestSegmentValue);

        try {
            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();

            String inputText = "";

            CommandLine cmd;
            cmd = parser.parse(options, args);
            inputText = StringCompressorExec.readFile(cmd.getOptionValue("filename"));


            // compress

            if( cmd.hasOption( "segmentLength")) {
                StringPartCompressor compressor = new StringPartCompressor( inputText, Integer.valueOf( cmd.getOptionValue( "segmentLength" ) ) );
                System.out.println( compressor.getCompressedText() );

            }
            else
            {
                StringPartCompressor compressor = new StringPartCompressor( inputText, 3 );
                System.out.println( compressor.getCompressedText() );

            }


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
