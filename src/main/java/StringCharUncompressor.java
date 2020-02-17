import java.util.ArrayList;

public class StringCharUncompressor {

    private HuffmanTree<Character> huffmanTree;
    private String compressedString;

    public StringCharUncompressor( String huffCompressedStringWithHeader ) {
        huffmanTree = new HuffmanTree<Character>(huffCompressedStringWithHeader);
        compressedString = huffCompressedStringWithHeader;
    }

    public String getUncompressedString() {
        ArrayList<Character> charsInArrayList = huffmanTree.uncompress( compressedString );

        String result = "";

        for( Character c : charsInArrayList )
            result += c;

        return result;
    }

    public void showCodes() {
        huffmanTree.printHuffmanCodes();
    }
}
