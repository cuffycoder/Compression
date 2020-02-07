import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanTree<SegmentType> {

    private HuffNode treeRoot;
    private FrequencyTable frequencyTable;
    private HashMap<SegmentType, HuffCodeInfo> segmentCodes;

    public HuffmanTree(FrequencyTable<SegmentType> in_frequencyTable) {
        this.frequencyTable = in_frequencyTable;
        buildTreeFromFrequencyTable();

        segmentCodes = new HashMap<SegmentType, HuffCodeInfo>();
        buildHuffmanCodes();
    }

    private void buildTreeFromFrequencyTable() {

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

        treeRoot = workingTable.get(0);
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

            traverseTreeAndBuildCodeTable(startingNode.left, newPrefix);

            // do right
            newPrefix = prefix + "1";
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


    private void postOrderTraverse( HuffNode currNode, ArrayList<HuffNode> result ) {

        // base case: leaf node - add to the result
        if( currNode.isLeaf() ) {
            result.add( currNode );
        }
        else
        {
            // non-leaf node: add the left, then the right, then self
            postOrderTraverse( currNode.left, result );
            postOrderTraverse( currNode.right, result );
            result.add( currNode );
        }
    }

    public String getHeader() {

        ArrayList<HuffNode> postOrderTraversedNodes = new ArrayList<HuffNode>();
        postOrderTraverse( this.treeRoot, postOrderTraversedNodes );

        String header = "";
        int numSegments = 0;

        for( HuffNode node : postOrderTraversedNodes ) {
            if( node.isLeaf() ) {
                header += "1" + node.getSegmentValue().toString();
                numSegments++;
            }
            else
                header += "0";
        }

        // add an extra pair of zeros at the end
        header += "00";

        // include the number of segments
        header += ";" + Integer.toString( numSegments ) + ";";

        return header;
    }


    class HuffNode<SegmentType> {
        private int frequency;
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
            if (segmentValue != null) return true;
            return false;
        }

        public void setLeft(HuffNode node) {
            this.left = node;
        }

        public void setRight(HuffNode node) {
            this.right = node;
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

}
