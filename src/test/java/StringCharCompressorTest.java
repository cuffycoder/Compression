import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class StringCharCompressorTest {

    @Test
    public void testCompressUncompress() {
        ArrayList<String> testStrings = new ArrayList<String>();

        testStrings.add( "" );
        testStrings.add( "a" );
        testStrings.add( "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" );
        testStrings.add( "the quick brown fox jumped over the lazy dog");
        testStrings.add( "It was the best of times, it was the worst of times, it was the age of wisdom, it was the age of foolishness, it was the epoch of belief, it was the epoch of incredulity, it was the season of Light, it was the season of Darkness, it was the spring of hope, it was the winter of despair, we had everything before us, we had nothing before us, we were all going direct to Heaven, we were all going direct the other way - in short, the period was so far like the present period, that some of its noisiest authorities insisted on its being received, for good or for evil, in the superlative degree of comparison only.");
        testStrings.add( "go go gophers");
        testStrings.add( "The wheels on the bus go round and round\n" +
                "Round and round\n" +
                "Round and round\n" +
                "The wheels on the bus go round and round\n" +
                "All through the town\n" +
                "The wipers on the bus go Swish, swish, swish\n" +
                "Swish, swish, swish\n" +
                "Swish, swish, swish\n" +
                "The wipers on the bus go Swish, swish, swish\n" +
                "All through the town\n" +
                "The horn on the bus goes Beep, beep, beep\n" +
                "Beep, beep, beep\n" +
                "Beep, beep, beep\n" +
                "The horn on the bus goes Beep, beep, beep\n" +
                "All through the town\n" +
                "The doors on the bus go open and shut\n" +
                "Open and shut\n" +
                "Open and shut\n" +
                "The doors on the bus go open and shut\n" +
                "All through the town\n" +
                "The Driver on the bus says \"Move on back\n" +
                "Move on back, move on back\"\n" +
                "The Driver on the bus says \"Move on back\"\n" +
                "All through the town…");

        for( String testString : testStrings ) {
            StringCharCompressor compressor = new StringCharCompressor( testString );

            String compressedText = compressor.getCompressedText();

            System.out.println( "Original        : " + testString );
            System.out.println( "Size(Original)  : " + testString.length());
            System.out.println( "Compressed      : " + compressedText );
            System.out.println( "Size(Compressed): " + compressedText.length() );

            StringCharUncompressor uncompressor = new StringCharUncompressor( compressedText );
            String uncompressed = uncompressor.getUncompressedString();
            System.out.println( "Uncompressed    : " +  uncompressed );

            assertTrue( uncompressed.equals( testString ));
            System.out.println( "\n\n");
            compressor.showCodes();

        }

    }
}
