import org.junit.Test;

import static org.junit.Assert.*;

public class HuffmanBitStreamTest {

    @Test
    public void testEmptyStream() {
        // Base case: empty stream
        HuffmanBitStream streamEmpty = new HuffmanBitStream();
        assertTrue( streamEmpty.toString().equals( "" ) );
    }

    @Test
    public void testBlockBoundaryConditions() {

        // one below, at, and one above the block size
        HuffmanBitStream streamBlockBoundaries = new HuffmanBitStream();
        for (int i = 0; i < 15; i++) {
            streamBlockBoundaries.addBit(false );
            streamBlockBoundaries.addBit(true);
        }
        streamBlockBoundaries.addBit( true );
        String expectedResult = "0101010101010101010101010101011";
        assertEquals( expectedResult, streamBlockBoundaries.toString());
        streamBlockBoundaries.addBit( false );
        expectedResult += "0";
        assertEquals(expectedResult, streamBlockBoundaries.toString() );

        streamBlockBoundaries.addBit( false );
        expectedResult += "0";
        assertEquals( expectedResult, streamBlockBoundaries.toString() );

        streamBlockBoundaries.addBit( true );
        expectedResult += "1";
        assertEquals( expectedResult, streamBlockBoundaries.toString() );


        HuffmanBitStream stream2 = new HuffmanBitStream();

        for( int i = 0; i < 32; i++ )
            stream2.addBit( false );

        assertEquals( "00000000000000000000000000000000", stream2.toString() );
        stream2.addBit( true );
        assertEquals( "000000000000000000000000000000001", stream2.toString() );


        HuffmanBitStream stream3 = new HuffmanBitStream();

        for( int i = 0; i < 32; i++ )
            stream3.addBit( true );

        assertEquals( "11111111111111111111111111111111", stream3.toString() );
        stream3.addBit( false );
        assertEquals( "111111111111111111111111111111110", stream3.toString() );
    }

    @Test
    public void testBitStreamToString() {

        HuffmanBitStream stream1 = new HuffmanBitStream();

        for (int i = 0; i < 50; i++) {
            stream1.addBit(true);
        }
        stream1.addBit(false );
        String expectedResult = "111111111111111111111111111111111111111111111111110";
        assertTrue(stream1.toString().equals( expectedResult) );
    }
}
