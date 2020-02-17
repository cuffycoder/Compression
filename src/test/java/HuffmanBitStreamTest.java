import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class HuffmanBitStreamTest {

    @Test
    public void testEmptyStream() {
        // Base case: empty stream
        HuffmanBitStream streamEmpty = new HuffmanBitStream();
        assertTrue(streamEmpty.toString().equals(""));
    }

    @Test
    public void testBlockBoundaryConditions() {

        // one below, at, and one above the block size
        HuffmanBitStream streamBlockBoundaries = new HuffmanBitStream();
        for (int i = 0; i < 15; i++) {
            streamBlockBoundaries.pushBit(false);
            streamBlockBoundaries.pushBit(true);
        }
        streamBlockBoundaries.pushBit(true);
        String expectedResult = "0101010101010101010101010101011";
        assertEquals(expectedResult, streamBlockBoundaries.toString());
        streamBlockBoundaries.pushBit(false);
        expectedResult += "0";
        assertEquals(expectedResult, streamBlockBoundaries.toString());

        streamBlockBoundaries.pushBit(false);
        expectedResult += "0";
        assertEquals(expectedResult, streamBlockBoundaries.toString());

        streamBlockBoundaries.pushBit(true);
        expectedResult += "1";
        assertEquals(expectedResult, streamBlockBoundaries.toString());


        HuffmanBitStream stream2 = new HuffmanBitStream();

        for (int i = 0; i < 32; i++)
            stream2.pushBit(false);

        assertEquals("00000000000000000000000000000000", stream2.toString());
        stream2.pushBit(true);
        assertEquals("000000000000000000000000000000001", stream2.toString());


        HuffmanBitStream stream3 = new HuffmanBitStream();

        for (int i = 0; i < 32; i++)
            stream3.pushBit(true);

        assertEquals("11111111111111111111111111111111", stream3.toString());
        stream3.pushBit(false);
        assertEquals("111111111111111111111111111111110", stream3.toString());
    }

    @Test
    public void testBitStreamToString() {

        HuffmanBitStream stream1 = new HuffmanBitStream();

        for (int i = 0; i < 50; i++) {
            stream1.pushBit(true);
        }
        stream1.pushBit(false);
        String expectedResult = "111111111111111111111111111111111111111111111111110";
        assertTrue(stream1.toString().equals(expectedResult));

        System.out.println(stream1.toBase62String());
    }

    @Test
    public void testBase62EncodeDecode() {

        ArrayList<String> testCases = new ArrayList<String>();

        testCases.add( "" );
        testCases.add( "0" );
        testCases.add( "1" );
        testCases.add( "00" );
        testCases.add("001");
        testCases.add("100");
        testCases.add("101");
        testCases.add("11111111111111111111111111111111111111111111111110");
        testCases.add("111111111111111111111111111111111111111111111111110");
        testCases.add("1111111111111111111111111111111111111111111111111111111111111111");
        testCases.add("11111111111111111111111111111111111111111111111111111111111111111");
        testCases.add("00000000000000000000000000000000");
        testCases.add("0000000000000000000000000000000000000000000000000000000000000000");
        testCases.add("00000000000000000000000000000000000000000000000000000000000000000");
        testCases.add("00000000000000000000000000000000000000000000000000000000000000001");
        testCases.add("11111111111111111111111111111111111111111111111111111111111111111");
        testCases.add("111111111111111111111111111111111111111111111111110111111111111111101111111111111111011111111111111110");


        for (String testCase : testCases) {
            HuffmanBitStream origStream = new HuffmanBitStream();

            for (char c : testCase.toCharArray()) {
                if ('1' == c) {
                    origStream.pushBit(true);
                } else
                    origStream.pushBit(false);
            }

            System.out.println("\n\n");
            System.out.println("Test String     : " + testCase);
            System.out.println("Stream As String: " + origStream.toString());
            System.out.println("Stream As Base62: " + origStream.toBase62String());

            assertTrue(origStream.toString().equals(testCase));
            assertTrue(origStream.numBitsInStream() == testCase.length());

            HuffmanBitStream reconstructedStream = new HuffmanBitStream(origStream.toBase62String());
            System.out.println("Reconstructed As String: " + reconstructedStream.toString());
            System.out.println("Reconstructed As Base62: " + reconstructedStream.toBase62String());

            assertTrue(reconstructedStream.numBitsInStream() == origStream.numBitsInStream());
            assertTrue(reconstructedStream.toString().equals(testCase));
            assertTrue(reconstructedStream.toString().equals(origStream.toString()));
            assertTrue(reconstructedStream.toBase62String().equals(origStream.toBase62String()));
        }

    }

}
