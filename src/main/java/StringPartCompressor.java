import java.util.ArrayList;
import java.util.HashMap;

public class StringPartCompressor {

    private String inputString;
    private FrequencyTable<String> frequencyTable;
    private HuffmanTree<String> huffmanTree;
    private int largestSegmentLength = 4;
    private ArrayList<String> uncompressedAsArrayList;

    public StringPartCompressor(String input, int largestSegmentLength ) {
        inputString = input;

        this.largestSegmentLength = largestSegmentLength;
        uncompressedAsArrayList = getSegments();

        // build the frequency table
        frequencyTable = new FrequencyTable<String>();

        for (String segment : getSegments())
            frequencyTable.addSingle(segment);

        buildHuffmanTree();
    }

    private HashMap<String, Integer> getFrequencyTableStringsOfLength(int segmentLength) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();

        char[] chars = inputString.toCharArray();

        for (int idx = 0; idx <= (chars.length - segmentLength); idx++) {
            String segment = "";

            for (int i = 0; i < segmentLength; i++)
                segment += chars[idx + i];

            Integer newCount = 1;
            if (result.containsKey(segment))
                newCount = result.get(segment) + 1;

            result.put(segment, newCount);
        }

        return result;
    }

    private ArrayList<String> getSegments() {
        int keywordSegmentLength = largestSegmentLength;
        HashMap<String, Integer> keywords = filterKeywordsByThreshold(keywordSegmentLength, 2 );
        ArrayList<String> segments = new ArrayList<String>();

        String currWord = "";

        for (int i = 0; i < inputString.length(); i++) {
            String potentialKeyword = inputString.substring(i, Integer.min(inputString.length() - 1, i + keywordSegmentLength));

            if (keywords.containsKey(potentialKeyword)) {
                segments.add(potentialKeyword);
                i += keywordSegmentLength - 1;
            } else
                segments.add(inputString.substring(i, i + 1));
        }

        return segments;
    }

    private HashMap<String, Integer> filterKeywordsByThreshold(int segmentLength, float percentThreshold) {
        // include those where: (chars in segment * frequency) >= 5% of the total length of the string
        int keywordThreshold = (int) (this.inputString.length() * (percentThreshold / 100) * segmentLength);

        // get the segments for each length
        HashMap<String, Integer> potentialKeyWords = getFrequencyTableStringsOfLength(segmentLength);

        HashMap<String, Integer> keywords = new HashMap<String, Integer>();

        for (String segment : potentialKeyWords.keySet()) {
            if (potentialKeyWords.get(segment) >= keywordThreshold)
                keywords.put(segment, potentialKeyWords.get(segment));
        }

        return keywords;
    }

    private void buildHuffmanTree() {
        huffmanTree = new HuffmanTree<String>(frequencyTable);
    }

    public String getCompressedText() {

        String compressed = "";

        compressed = huffmanTree.compressSegments(uncompressedAsArrayList);

        return compressed;
    }

}
