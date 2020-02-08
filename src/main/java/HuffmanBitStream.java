import java.util.ArrayList;

public class HuffmanBitStream {

    private ArrayList<Integer> blockList;
    private static final int blockSize = Integer.SIZE;
    private long totalBitsSet;
    private short bitPosWorkingBlock;

    public HuffmanBitStream() {
        blockList = new ArrayList<Integer>();
        totalBitsSet = 0;
        bitPosWorkingBlock = 0;
    }

    private long capacity() {
        return (blockList.size() * blockSize) - totalBitsSet;
    }

    public void addBit(boolean bitOn) {

        // if no capacity, add another long
        if (capacity() == 0) {
            blockList.add(0);
            bitPosWorkingBlock = blockSize - 1;
        }

        if (bitOn) {
            Integer lastBlock = blockList.get(blockList.size() - 1);

            lastBlock |= 1 << bitPosWorkingBlock;

            blockList.set(blockList.size() - 1, lastBlock);
        }

        bitPosWorkingBlock--;
        totalBitsSet++;
    }

    public String toString() {

        String result = "";
        long bitsLeftToPrint = totalBitsSet;


        for (int i = 0; i < blockList.size(); i++) {
                Integer currBlock = blockList.get(i);
                int bitPos = blockSize - 1;

                while( bitsLeftToPrint > 0 && bitPos >= 0 ) {

                    if ((currBlock & (1 << bitPos)) != 0)
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
