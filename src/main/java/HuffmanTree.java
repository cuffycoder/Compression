import java.util.*;

public class HuffmanTree<SegmentType> {

    private HuffNode treeRoot;
    private HashMap<SegmentType, HuffCodeInfo> segmentCodes = new HashMap<SegmentType, HuffCodeInfo>();
    private HuffHeaderInfo headerInfo;

    private static final HashSet<Character> specialCharacters;
    static {
        specialCharacters = new HashSet<Character>();
        specialCharacters.add('0');
        specialCharacters.add('1');
        specialCharacters.add('\\');
    }

    private enum CompressedTextParts {
        TREE_DEFINITION,
        COMPRESSED_TEXT
    }

    public HuffmanTree(FrequencyTable<SegmentType> frequencyTable) {
        buildTreeFromFrequencyTable(frequencyTable);
        buildHuffmanCodes();
    }

    public HuffmanTree(String compressedText) {
        HashMap<CompressedTextParts, String> textParts = this.parseCompressedString(compressedText);
         buildTreeFromHeaderString(textParts.get(CompressedTextParts.TREE_DEFINITION));
        buildHuffmanCodes();
    }

    private HashMap<CompressedTextParts, String> parseCompressedString(String compressedString) {
        char[] chars = compressedString.toCharArray();

        // Format: <NumberOfCharsInTreeDefinition>~<TreeDefinition>~<HuffmanBitStream>
        String treeDefSizeAsString = "";

        int idx = 0;

        while (idx < chars.length && chars[idx] != '~')
            treeDefSizeAsString += chars[idx++];

        int treeDefSize = Integer.valueOf(treeDefSizeAsString);

        // now read the next treeDefSize characters
        idx++;
        String treeDef = "";
        int treeDefLeftToRead = treeDefSize;
        while (treeDefLeftToRead > 0) {
            treeDef += chars[idx++];
            treeDefLeftToRead--;
        }

        if (chars[idx++] != '~')
            throw new RuntimeException("Expected delimiter");

        // rest of the string is the actual compressed base62 string
        String base62String = "";
        while (idx < chars.length) {
            base62String += chars[idx++];
        }

        HashMap<CompressedTextParts, String> result = new HashMap<CompressedTextParts, String>();
        result.put(CompressedTextParts.TREE_DEFINITION, treeDef);
        result.put(CompressedTextParts.COMPRESSED_TEXT, base62String);

        return result;
    }

    private void buildTreeFromHeaderString(String treeHeaderString) {
        Stack<HuffNode> nodeStack = new Stack<HuffNode>();

        char[] headerChars = treeHeaderString.toCharArray();

        int i = 0;
        int numChars = headerChars.length;

        while (i < numChars) {

            char c = headerChars[i];

            if (c == '1') {

                if (numChars - i < 1)
                    throw new ArrayIndexOutOfBoundsException("Expecting another character after indication of leaf node");

                HuffNode newNode = new HuffNode();
                // TODO: keep reading until get to an un-escaped 1 or 0; this may mean we have to just assume SegmentType is String
                String segment = "";
                boolean escaped = false;
                i++;
                while( i < numChars ) {

                    if( !escaped &&'\\' == headerChars[i]) {
                        escaped = true;
                        i++;
                        continue;
                    }

                    // if we're not escaped and we hit a 1 or 0, stop
                    if( !escaped && ( '1' == headerChars[i] || '0' == headerChars[i] )) {
                        newNode.setSegmentValue( segment );
                        newNode.setFrequency(0); // we don't get frequency info in the string header
                        nodeStack.push(newNode);
                        // i += 2; // we are already at the delimiter, don't advance
                        break;
                    }
                    else
                    {
                        segment += headerChars[i];
                        i++;
                        escaped = false;
                    }
                }

            } else if (c == '0') {

                int nodeStackSize = nodeStack.size();

                if (nodeStackSize == 0) {
                    // Special case of empty tree
                    this.treeRoot = new HuffNode();
                    this.treeRoot.setFrequency(0);
                    break;
                }
                // if there's only one node in the stack, then we are done
                else if (nodeStackSize == 1) {
                    this.treeRoot = nodeStack.pop();
                    break;
                } else {
                    // we pop the two items from the stack
                    HuffNode newNode = new HuffNode();
                    newNode.setRight(nodeStack.pop());
                    newNode.setLeft(nodeStack.pop());
                    newNode.setFrequency(0);

                    nodeStack.push(newNode);
                    i++;
                }
            }
        }
    }


    private void buildTreeFromFrequencyTable(FrequencyTable<SegmentType> frequencyTable) {

        // 1. Create leaf nodes for each element
        ArrayList<HuffNode> workingTable = new ArrayList<HuffNode>();

        ArrayList<SegmentType> segmentValues = frequencyTable.segments();

        for (SegmentType segmentValue : segmentValues) {

            HuffNode leafNode = new HuffNode<SegmentType>();
            leafNode.setSegmentValue(segmentValue);
            leafNode.setFrequency(frequencyTable.segmentGetFrequency(segmentValue));
            workingTable.add(leafNode);
        }

        // Sort by frequency
        workingTable.sort((a, b) -> {

            return a.getFrequency() - b.getFrequency();

        });

        while (workingTable.size() > 1) {

            // combine two lowest frequency elements (leaf or otherwise) under a new node
            HuffNode newNode = new HuffNode();
            newNode.setLeft(workingTable.get(0));
            newNode.setRight(workingTable.get(1));
            newNode.setFrequency(workingTable.get(0).getFrequency() + workingTable.get(1).getFrequency());

            // place the new node to the left of the lowest frequency node that's greater than it (i.e., keep working table sorted)
            int newindex = 0;

            for (int i = 2; i < workingTable.size(); i++) {
                if (workingTable.get(i).getFrequency() > newNode.getFrequency()) {
                    newindex = i;
                    break;
                }
            }

            if (newindex == 0) {
                // add to the end
                workingTable.add(newNode);
            } else {
                workingTable.add(newindex, newNode);
            }

            // remove the two nodes that were consolidated
            workingTable.remove(1);
            workingTable.remove(0);
        }

        if (workingTable.size() > 0) {
            treeRoot = workingTable.get(0);
        } else {
            // handle case of no segments
            treeRoot = new HuffNode();
            treeRoot.setFrequency(0);
            treeRoot.left = null;
            treeRoot.right = null;
        }

    }

    private void traverseTreeAndBuildCodeTable(HuffNode startingNode, String prefix) {

        // base case: we've hit a leaf node
        if (startingNode.isLeaf()) {
            HuffCodeInfo newCode = new HuffCodeInfo();

            // special case: single segment
            if (prefix == "")
                prefix = "0";

            newCode.setCode(prefix);
            newCode.setFrequency(startingNode.getFrequency());

            this.segmentCodes.put((SegmentType) startingNode.getSegmentValue(), newCode);
        } else {
            // do left
            String newPrefix = prefix + "0";

            if (startingNode.hasLeft())
                traverseTreeAndBuildCodeTable(startingNode.left, newPrefix);

            // do right
            newPrefix = prefix + "1";
            if (startingNode.hasRight())
                traverseTreeAndBuildCodeTable(startingNode.right, newPrefix);
        }
    }

    private void buildHuffmanCodes() {

        traverseTreeAndBuildCodeTable(this.treeRoot, "");
    }


    public void printHuffmanCodes() {
        segmentCodes.forEach((SegmentType segment, HuffCodeInfo info) -> {


            System.out.println(segment + ": " + info.getCode() + " " + info.getFrequency());
        });
    }


    private void postOrderTraverse(HuffNode currNode, ArrayList<HuffNode> result) {

        // base case: leaf node - add to the result
        if (currNode.isLeaf()) {
            result.add(currNode);
        } else {
            // non-leaf node: add the left, then the right, then self
            if (currNode.hasLeft())
                postOrderTraverse(currNode.left, result);

            if (currNode.hasRight())
                postOrderTraverse(currNode.right, result);

            result.add(currNode);
        }
    }

    public String getTreeHeaderString() {

        ArrayList<HuffNode> postOrderTraversedNodes = new ArrayList<HuffNode>();
        postOrderTraverse(this.treeRoot, postOrderTraversedNodes);

        String treeDetails = "";
        int numSegments = 0;

        for (HuffNode node : postOrderTraversedNodes) {
            if (node.isLeaf()) {
                treeDetails += "1" + escapeSpecialCharacters(node.getSegmentValue().toString());
                numSegments++;
            } else
                treeDetails += "0";
        }

        treeDetails += "0";

        // we prefix the header with the number of characters in the tree details
        String header = treeDetails.length() + "~" + treeDetails + "~";

        return header;
    }

    private String escapeSpecialCharacters(String input) {
        String result = "";

        for (char c : input.toCharArray()) {
            if (specialCharacters.contains(c))
                result += "\\" + c;
            else
                result += c;
        }

        return result;
    }

    public HashMap<SegmentType, String> getSegmentHuffCodeMap() {

        HashMap<SegmentType, String> map = new HashMap<SegmentType, String>();

        segmentCodes.forEach((SegmentType segment, HuffCodeInfo info) -> {
            map.put(segment, info.getCode());
        });

        return map;
    }

    private String getCompressedBase62(ArrayList<SegmentType> uncompressed) {
        HuffmanBitStream huffmanBitStream = new HuffmanBitStream();

        for (SegmentType segment : uncompressed) {

            if (segmentCodes.containsKey(segment)) {
                HuffCodeInfo codeInfo = segmentCodes.get(segment);

                boolean[] bits = codeInfo.getBinaryPath();

                for (boolean b : bits)
                    huffmanBitStream.pushBit(b);
            } else
                throw new RuntimeException("No code present for [" + segment + "]. Rebuild tree");
        }

        return huffmanBitStream.toBase62String();
    }

    public String compressSegments(ArrayList<SegmentType> segments) {
        String compressed = "";

        compressed += this.getTreeHeaderString();
        compressed += this.getCompressedBase62(segments);

        return compressed;
    }

    public ArrayList<SegmentType> uncompress(String compressedStringWithHeader) {

        HashMap<CompressedTextParts, String> parsed = this.parseCompressedString(compressedStringWithHeader);

        HuffmanBitStream bitStream = new HuffmanBitStream(parsed.get(CompressedTextParts.COMPRESSED_TEXT));

        ArrayList<SegmentType> result = uncompressHuffmanBitStream(bitStream);

        return result;
    }

    private ArrayList<SegmentType> uncompressHuffmanBitStream(HuffmanBitStream huffmanBitStream) {
        // TODO:
        Stack<Boolean> bitStack = huffmanBitStream.getBitStack();
        ArrayList<SegmentType> result = new ArrayList<SegmentType>();

        String uncompressedString = "";

        // build a reverse hash that maps from code to SegmentType value
        HashMap<String, SegmentType> codeToSegmentValue = new HashMap<String, SegmentType>();
        for (SegmentType segmentVal : segmentCodes.keySet()) {
            codeToSegmentValue.put(segmentCodes.get(segmentVal).getCode(), segmentVal);
        }

        HuffNode currNode = this.treeRoot;

        String currCode = "";
        while (!bitStack.isEmpty()) {

            boolean nextBit = bitStack.pop();

            if (nextBit == true)
                currCode += "1";
            else
                currCode += "0";

            if (codeToSegmentValue.containsKey(currCode)) {
                result.add(codeToSegmentValue.get(currCode));
                currCode = "";
            }
        }

        return result;
    }

    class HuffNode<SegmentType> {
        private int frequency = 0;
        private SegmentType segmentValue = null;
        private HuffNode left = null;
        private HuffNode right = null;

        public void setSegmentValue(SegmentType segval) {
            segmentValue = segval;
        }

        public SegmentType getSegmentValue() {
            return segmentValue;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public int getFrequency() {
            return frequency;
        }

        public boolean isLeaf() {
            if (null != this.getSegmentValue()) return true;
            return false;
        }

        public void setLeft(HuffNode node) {
            this.left = node;
        }

        public void setRight(HuffNode node) {
            this.right = node;
        }

        public boolean hasLeft() {
            if (this.left != null) return true;
            return false;
        }

        public boolean hasRight() {
            if (this.right != null) return true;
            return false;
        }

    }

    class HuffCodeInfo {
        String code;
        int frequency;

        public void setCode(String code) {
            this.code = code;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public String getCode() {
            return this.code;
        }

        public int getFrequency() {
            return this.frequency;
        }

        public boolean[] getBinaryPath() {
            boolean[] result = new boolean[this.code.length()];

            int idx = 0;
            for (char c : this.code.toCharArray()) {
                if ('1' == c)
                    result[idx] = true;
                else
                    result[idx] = false;

                idx++;
            }

            return result;
        }
    }

    class HuffHeaderInfo {
        String treeHeader;
        int numSegments;

        public void setTreeHeaderString(String treeHeaderString) {
            treeHeader = treeHeaderString;
        }

        public void setNumSegments(int numSegments) {
            this.numSegments = numSegments;
        }

        public String getTreeHeader() {
            return treeHeader;
        }

        public int getNumSegments() {
            return numSegments;
        }
    }

}
