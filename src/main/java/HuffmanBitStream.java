import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanBitStream {

    private ArrayList<Long> blockList;
    private static final int blockSize = Long.SIZE - 1; // don't use the sign bit
    private long totalBitsSet;
    private short bitPosWorkingBlock;
    private static long[] masks;

    private static char[] BASE_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static HashMap<Character,Integer> baseAlphabetMap;
    private static String delimitter = " ";

    public HuffmanBitStream() {
        blockList = new ArrayList<Long>();
        totalBitsSet = 0;
        bitPosWorkingBlock = 0;

        // initialize this to make decoding faster
        baseAlphabetMap = new HashMap<Character,Integer>();

        int idx = 0;
        for( char c : BASE_ALPHABET ) {
            baseAlphabetMap.put( c, idx );
            idx++;
        }


        // initialize the masks
        masks = new long[blockSize];
        masks[0] = 1L;

        for( int i = 1; i < blockSize; i++ ) {
            masks[i] = masks[i-1] * 2;
        }
    }

    public HuffmanBitStream( String base62EncodedString ) {

        // first call the normal constructor
        this();

        String[] parts = base62EncodedString.split( delimitter );

        if( parts.length > 0 ) {

            // first part is the number of bits
            long numBits = intFromBase62EncodedString( parts[0] );

            this.totalBitsSet = numBits;

            for( int idx = 1; idx < parts.length; idx++ ) {
                long block = intFromBase62EncodedString( parts[idx] );

                blockList.add( block );
            }
        }

    }


    private long capacity() {
        return (blockList.size() * blockSize) - totalBitsSet;
    }

    public void addBit(boolean bitOn) {

        // if no capacity, add another long
        if (capacity() == 0) {
            blockList.add(0L);
            bitPosWorkingBlock = blockSize - 1; // don't use the sign bit
        }


        if (bitOn) {
            Long lastBlock = blockList.get(blockList.size() - 1);

            lastBlock += masks[bitPosWorkingBlock];
            blockList.set(blockList.size() - 1, lastBlock);
        }

        bitPosWorkingBlock--;

        totalBitsSet++;
    }

    public long numBitsInStream() { return totalBitsSet; }

    public String toBase62String() {

        String result = "";

        // first 32 bits are the number of bits in the stream
        result += intToBase62( numBitsInStream() );

        for( Long block : this.blockList ) {
            result += delimitter + intToBase62( block );
        }

        return result;
    }

    public void showBlocks() {

        for( long block : blockList )
            System.out.println( block + "\t" + Long.toBinaryString( block ) );
    }

    public String intToBase62( long n ) {
        StringBuilder result = new StringBuilder();

        if( 0 == n ) {
            result.append( BASE_ALPHABET[0] );
        }

        long base_len = BASE_ALPHABET.length;
        long c = n;

        while( c != 0 ) {
            long rem = Long.remainderUnsigned(c, base_len);
            c = Long.divideUnsigned( c, base_len );
            result.append( BASE_ALPHABET[ (int) rem] );
        }

        return result.reverse().toString();
    }

    public long intFromBase62EncodedString( String base62EncodedString ) {
        char[] chars = base62EncodedString.toCharArray();

        int base_len = BASE_ALPHABET.length;
        int num_chars = chars.length;
        long result = 0;
        for( int idx = 0; idx < num_chars; idx++ ) {
            int power = num_chars - ( idx + 1 );

            long digit_value = baseAlphabetMap.get( chars[idx] );
            long pos_value = (long) Math.pow( base_len, power );

            result +=  pos_value * digit_value;
        }

        return result;
    }

    public String toString() {

        String result = "";
        long bitsLeftToPrint = totalBitsSet;


        for (int i = 0; i < blockList.size(); i++) {
                Long currBlock = blockList.get(i);
                int bitPos = blockSize - 1;

                while( bitsLeftToPrint > 0 && bitPos >= 0 ) {

                    if ((currBlock & (masks[bitPos])) != 0)
                        result += "1";
                    else
                        result += "0";

                    bitsLeftToPrint--;
                    bitPos--;
                }
            }


        return result;
    }

}
