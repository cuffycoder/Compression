import java.util.ArrayList;

public class StringCharCompressor {

    private FrequencyTable<Character> frequencyTable;
    private HuffmanTree<Character> huffmanTree;
    ArrayList<Character> uncompressedAsArrayList;

    public StringCharCompressor( String uncompressedText ) {

        uncompressedAsArrayList = new ArrayList<Character>();

        for( char c : uncompressedText.toCharArray() )
            uncompressedAsArrayList.add( Character.valueOf(c));

        buildHuffmanTree();
    }

    public String getUncompressed() {
        String uncompressed = "";

        for (int idx = 0; idx < uncompressedAsArrayList.size(); idx++) {
            uncompressed += uncompressedAsArrayList.get(idx);
        }
        return uncompressed;
    }

    private void buildHuffmanTree() {

        // build the frequency table
        frequencyTable = new FrequencyTable<Character>();

        for( Character segment : uncompressedAsArrayList )
            frequencyTable.addSingle( segment );

        this.huffmanTree = new HuffmanTree<Character>(frequencyTable);
    }

    public String getCompressedText() {

        String compressed = "";

        compressed = huffmanTree.compressSegments( uncompressedAsArrayList );

        return compressed;
    }

    public void showCodes() {
        huffmanTree.printHuffmanCodes();
    }
}
