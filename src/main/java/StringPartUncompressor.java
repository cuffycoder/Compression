import java.util.ArrayList;

public class StringPartUncompressor {

    private HuffmanTree<String> huffmanTree;
    private String compressedString;

    public StringPartUncompressor(String huffCompressedStringWithHeader ) {
        huffmanTree = new HuffmanTree<String>(huffCompressedStringWithHeader);
        compressedString = huffCompressedStringWithHeader;
    }

    public String getUncompressedString() {
        ArrayList<String> stringInArrayList = huffmanTree.uncompress( compressedString );

        String result = "";

        for( String segment : stringInArrayList )
            result += segment;

        return result;
    }

    public void showCodes() {
        huffmanTree.printHuffmanCodes();
    }
}
