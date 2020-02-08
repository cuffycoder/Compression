import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class HuffmanTree<SegmentType> {

    private HuffNode treeRoot;
    private HashMap<SegmentType, HuffCodeInfo> segmentCodes = new HashMap<SegmentType, HuffCodeInfo>();
    private HuffHeaderInfo headerInfo;

    public HuffmanTree(FrequencyTable<SegmentType> frequencyTable) {

        buildTreeFromFrequencyTable(frequencyTable);
        buildHuffmanCodes();
        buildHeader();
    }

    public HuffmanTree(String treeHeaderString) {

        buildTreeFromHeaderString(treeHeaderString);
        buildHuffmanCodes();
        buildHeader();
    }

    private void buildTreeFromHeaderString(String treeHeaderString) {
        Stack<HuffNode> nodeStack = new Stack<HuffNode>();

        char[] headerChars = treeHeaderString.toCharArray();

        int i = 0;
        int numChars = headerChars.length;

        while (i < numChars) {

            // FixMe( "Figure out how to parse different Segment Types");
            char c = headerChars[i];

            if (c == '1') {

                if (numChars - i < 1)
                    throw new ArrayIndexOutOfBoundsException("Expecting another character after indication of leaf node");

                HuffNode newNode = new HuffNode();
                newNode.setSegmentValue(headerChars[i + 1]);
                newNode.setFrequency(0); // we don't get frequency info in the string header

                nodeStack.push(newNode);
                i += 2;
            } else if (c == '0') {

                int nodeStackSize = nodeStack.size();

                if (nodeStackSize == 0) {
                    // Special case of empty tree
                    this.treeRoot = new HuffNode();
                    this.treeRoot.setFrequency(0);
                    i++;
                }
                // if there's only one node in the stack, then we are done
                else if (nodeStackSize == 1) {
                    this.treeRoot = nodeStack.pop();
                    i++;
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

        // handle case of no segments
        if (workingTable.size() > 0) {
            treeRoot = workingTable.get(0);
        } else {
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
        return this.headerInfo.getTreeHeader();
    }

    public void buildHeader() {

        ArrayList<HuffNode> postOrderTraversedNodes = new ArrayList<HuffNode>();
        postOrderTraverse(this.treeRoot, postOrderTraversedNodes);

        String header = "";
        int numSegments = 0;

        for (HuffNode node : postOrderTraversedNodes) {
            if (node.isLeaf()) {
                header += "1" + node.getSegmentValue().toString();
                numSegments++;
            } else
                header += "0";
        }

        header += "0";

        this.headerInfo = new HuffHeaderInfo();
        this.headerInfo.setNumSegments(numSegments);
        this.headerInfo.setTreeHeader(header);
    }

    public HashMap<SegmentType, String> getSegmentHuffCodeMap() {

        HashMap<SegmentType, String> map = new HashMap<SegmentType, String>();

        segmentCodes.forEach((SegmentType segment, HuffCodeInfo info) -> {
            map.put(segment, info.getCode());
        });

        return map;
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
            if (this.right != null )return true;
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
    }

    class HuffHeaderInfo {
        String treeHeader;
        int numSegments;

        public void setTreeHeader(String treeHeaderString) {
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
