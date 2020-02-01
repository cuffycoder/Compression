import java.util.ArrayList;

public class UncompressedText {

    private String text;

    public UncompressedText( String text ) {
        this.text = text;
    }

    private char[] getSegments() {
        return text.toCharArray();
    }

    public FrequencyTable<Character> getFrequencyTable() {

        FrequencyTable<Character> frequencyTable = new FrequencyTable<Character>();

        char[] segments = this.getSegments();

        for( char seg : segments ) {
            frequencyTable.addSingle( seg );
        }

        return frequencyTable;
    }
}
