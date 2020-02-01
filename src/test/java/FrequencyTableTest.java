import java.util.Date;
import java.util.HashMap;
import org.junit.*;

public class FrequencyTableTest {

    @Test
    public void checkAddToTableWithStrings() {

        FrequencyTable<String> stringFrequencyTable = new FrequencyTable<String>();

        HashMap<String,Integer> stringTestCases = new HashMap<String, Integer>();
        stringTestCases.put( "Hello", 3 );
        stringTestCases.put( "World", 5 );

        HashMap<String,Integer> leftToPlace = new HashMap<String,Integer>( stringTestCases );


        while( true ) {

            int remaining = leftToPlace.values().stream().reduce( (a,b) -> a+b ).get();

            if( remaining == 0 )
                break;

            for( String segment : leftToPlace.keySet() ) {
                if( leftToPlace.get( segment ) > 0 ) {
                    stringFrequencyTable.addSingle(segment);
                    leftToPlace.put( segment, leftToPlace.get(segment) - 1 );
                }
            }
        }


        assert( stringFrequencyTable.getFrequencies().equals(  stringTestCases ) );

    }

    @Test
    public void checkAddToTableWithDates() {

        FrequencyTable<Date> dateFrequencyTable = new FrequencyTable<Date>();

        HashMap<Date,Integer> dateTestCases = new HashMap<Date, Integer>();
        dateTestCases.put( new Date( 100000L ), 3 );
        dateTestCases.put( new Date( 200000L ), 5 );

        HashMap<Date,Integer> leftToPlace = new HashMap<Date,Integer>( dateTestCases );


        while( true ) {

            int remaining = leftToPlace.values().stream().reduce( (a,b) -> a+b ).get();

            if( remaining == 0 )
                break;

            for( Date segment : leftToPlace.keySet() ) {
                if( leftToPlace.get( segment ) > 0 ) {
                    dateFrequencyTable.addSingle(segment);
                    leftToPlace.put( segment, leftToPlace.get(segment) - 1 );
                }
            }
        }


        assert( dateFrequencyTable.getFrequencies().equals(  dateTestCases ) );

    }
}
