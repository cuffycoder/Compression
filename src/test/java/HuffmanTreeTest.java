import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static junit.framework.TestCase.assertTrue;

public class HuffmanTreeTest {

    @Test
    public void encodeStringTree() {
        FrequencyTable<String> stringFrequencyTable = new FrequencyTable<String>();

        String testString = "grant me the serenity to accept the things I cannot change the courage to change the things I can and the wisdom to know the difference";
        testString = "the wheels on the bus go round and round " +
                "round and round 1" +
                "round and round 0" +
                "the wheels on the bus go round and round " +
                "all through the town";
        String[] segments = testString.split(" ");

        for (String segment : segments) {
            stringFrequencyTable.addSingle(segment);
            stringFrequencyTable.addSingle(" ");
        }

        HuffmanTree<String> huffmanTree = new HuffmanTree<String>(stringFrequencyTable);
        String treeHeaderString = huffmanTree.getTreeHeaderString();
        System.out.println( treeHeaderString );

        HuffmanTree<String> huffmanTreeRecreate = new HuffmanTree<String>(treeHeaderString);
        System.out.println( huffmanTreeRecreate.getTreeHeaderString() );

    }

    @Test
    public void encodeAndDecodeCharacterTree() {

        ArrayList<String> testStrings = new ArrayList<String>();
        testStrings.add("go go gophers");
        testStrings.add("the wheels on the bus go round and round round and round round and round the wheels on the bus go round and round all through the town");
        testStrings.add("a");
        testStrings.add("");
        testStrings.add("bb");
        testStrings.add("1a5");
        testStrings.add( "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ;.,~-_");

        for (String testString : testStrings) {
            System.out.println("\n\nTest String: \"" + testString + "\"");

            char[] segments = testString.toCharArray();

            // Build the Frequency table and also get a set of all characters
            FrequencyTable<Character> characterFrequencyTable = new FrequencyTable<Character>();
            HashSet<Character> allCharsInTestString = new HashSet<Character>();

            for (char segment : segments) {
                characterFrequencyTable.addSingle(segment);

                allCharsInTestString.add(segment);
            }

            HuffmanTree<Character> huffmanTree = new HuffmanTree<Character>(characterFrequencyTable);

            HashMap<Character, String> huffCodeMap = huffmanTree.getSegmentHuffCodeMap();

            // all characters in the test String should be in the huffCodeMap
            System.out.println(huffCodeMap.keySet());
            System.out.println(allCharsInTestString);

            assertTrue(huffCodeMap.keySet().equals(allCharsInTestString));

            System.out.println("\n\n");

            String treeHeaderString = huffmanTree.getTreeHeaderString();

            System.out.println(treeHeaderString);

            HuffmanTree<Character> huffmanTreeRecreate = new HuffmanTree<Character>(treeHeaderString);

            assertTrue(huffCodeMap.equals(huffmanTreeRecreate.getSegmentHuffCodeMap()));
        }
    }
}
