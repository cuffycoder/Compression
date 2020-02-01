import java.util.ArrayList;
import java.util.HashMap;

public class FrequencyTable<SegmentType> {

    private HashMap<SegmentType,Integer> frequencies;

    public FrequencyTable() {
        frequencies = new HashMap<SegmentType, Integer>();
    }

    public void addSingle( SegmentType instance ) {

        Integer newcount = 1;

        if( frequencies.containsKey( instance ) ) {
            newcount = frequencies.get( instance ) + 1;
        }

        frequencies.put( instance, newcount );
    }

    public HashMap<SegmentType,Integer> getFrequencies() {
        return frequencies;
    }

    public int segmentGetFrequency( SegmentType segmentValue ) {
        if( frequencies.containsKey( segmentValue ))
            return frequencies.get( segmentValue );

        return 0;
    }

    public int numberOfSegments() { return frequencies.size(); }

    public ArrayList<SegmentType> segments() { return new ArrayList<SegmentType>( frequencies.keySet() );}
}
