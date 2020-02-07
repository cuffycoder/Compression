import org.junit.Test;

public class HuffmanTreeTest {

    @Test
    public void encodeStringTree() {
        FrequencyTable<String> stringFrequencyTable = new FrequencyTable<String>();

        String testString = "grant me the serenity to accept the things I cannot change the courage to change the things I can and the wisdom to know the difference";
        testString = "the wheels on the bus go round and round " +
                "round and round " +
                "round and round " +
                "the wheels on the bus go round and round " +
                "all through the town";
        String[] segments = testString.split( " " );

        for( String segment: segments ) {
            stringFrequencyTable.addSingle(segment);
            stringFrequencyTable.addSingle( " ");
        }

        HuffmanTree<String> huffmanTree = new HuffmanTree<String>( stringFrequencyTable );

        huffmanTree.printHuffmanCodes();
    }

    @Test
    public void encodeCharacterTree() {
        FrequencyTable<Character> characterFrequencyTable = new FrequencyTable<Character>();

        String testString = "grant me the serenity to accept the things I cannot change the courage to change the things I can and the wisdom to know the difference";
        testString = "the wheels on the bus go round and round " +
                "round and round " +
                "round and round " +
                "the wheels on the bus go round and round " +
                "all through the town";

        testString = "go go gophers";
        char[] segments = testString.toCharArray();

        for( char segment: segments ) {
            characterFrequencyTable.addSingle(segment);
        }

        HuffmanTree<Character> huffmanTree = new HuffmanTree<Character>( characterFrequencyTable );

        huffmanTree.printHuffmanCodes();

        System.out.println( "\n\n");

        System.out.println( huffmanTree.getHeader() );
    }
}
