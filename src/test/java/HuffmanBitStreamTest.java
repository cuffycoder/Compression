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
            streamBlockBoundaries.addBit(false);
            streamBlockBoundaries.addBit(true);
        }
        streamBlockBoundaries.addBit(true);
        String expectedResult = "0101010101010101010101010101011";
        assertEquals(expectedResult, streamBlockBoundaries.toString());
        streamBlockBoundaries.addBit(false);
        expectedResult += "0";
        assertEquals(expectedResult, streamBlockBoundaries.toString());

        streamBlockBoundaries.addBit(false);
        expectedResult += "0";
        assertEquals(expectedResult, streamBlockBoundaries.toString());

        streamBlockBoundaries.addBit(true);
        expectedResult += "1";
        assertEquals(expectedResult, streamBlockBoundaries.toString());


        HuffmanBitStream stream2 = new HuffmanBitStream();

        for (int i = 0; i < 32; i++)
            stream2.addBit(false);

        assertEquals("00000000000000000000000000000000", stream2.toString());
        stream2.addBit(true);
        assertEquals("000000000000000000000000000000001", stream2.toString());


        HuffmanBitStream stream3 = new HuffmanBitStream();

        for (int i = 0; i < 32; i++)
            stream3.addBit(true);

        assertEquals("11111111111111111111111111111111", stream3.toString());
        stream3.addBit(false);
        assertEquals("111111111111111111111111111111110", stream3.toString());
    }

    @Test
    public void testBitStreamToString() {

        HuffmanBitStream stream1 = new HuffmanBitStream();

        for (int i = 0; i < 50; i++) {
            stream1.addBit(true);
        }
        stream1.addBit(false);
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
                    origStream.addBit(true);
                } else
                    origStream.addBit(false);
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

            origStream.showBlocks();
            reconstructedStream.showBlocks();

            assertTrue(reconstructedStream.numBitsInStream() == origStream.numBitsInStream());
            assertTrue(reconstructedStream.toString().equals(testCase));
            assertTrue(reconstructedStream.toString().equals(origStream.toString()));
            assertTrue(reconstructedStream.toBase62String().equals(origStream.toBase62String()));
        }

    }

    @Test
    public void scratchTest() {
        HuffmanBitStream s = new HuffmanBitStream();

        long[] tests = {0L,
//                1L,
//                2L,
//                200L,
//                3000L,
//                4000000L,
                1152921504606846976L};

        System.out.println( "Max Value: " + Long.MAX_VALUE );
        System.out.println( "Problem:   " + 1152921504606846976L );

        long increment = 2;
        int num_iters = 0;

//        for (long l = 0L; l < Long.MAX_VALUE; l+=increment ) {
        for( long l : tests ) {
            String encoded = s.intToBase62(l);
            long decoded = s.intFromBase62EncodedString(encoded);

            if (true || decoded != l) {
                System.out.println(l);
                System.out.println(encoded);
                System.out.println(decoded);
                System.out.println("\n");
            }

        }
    }
}
