import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class StringPartCompressorTest {

    @Test
    public void testCompressUncompress() {
        ArrayList<String> testStrings = new ArrayList<String>();
/*
        testStrings.add( "" );
        testStrings.add( "a" );
        testStrings.add( "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" );
        testStrings.add( "the quick brown fox jumped over the lazy dog");
        testStrings.add( "The wheels on the bus go round and round\n" +
                "Round and round\n" +
                "Round and round\n" +
                "The wheels on the bus go round and round\n" +
                "All through the town\n" +
                "The wipers on the bus go Swish, swish, swish\n" +
                "Swish, swish, swish\n" +
                "Swish, swish, swish\n" +
                "The wipers on the bus go Swish, swish, swish\n" +
                "All through the town\n" +
                "The horn on the bus goes Beep, beep, beep\n" +
                "Beep, beep, beep\n" +
                "Beep, beep, beep\n" +
                "The horn on the bus goes Beep, beep, beep\n" +
                "All through the town\n" +
                "The doors on the bus go open and shut\n" +
                "Open and shut\n" +
                "Open and shut\n" +
                "The doors on the bus go open and shut\n" +
                "All through the town\n" +
                "The Driver on the bus says \"Move on back\n" +
                "Move on back, move on back\"\n" +
                "The Driver on the bus says \"Move on back\"\n" +
                "All through the town…");

        testStrings.add( "It was the best of times,\n" +
                "it was the worst of times,\n" +
                "it was the age of wisdom,\n" +
                "it was the age of foolishness,\n" +
                "it was the epoch of belief,\n" +
                "it was the epoch of incredulity,\n" +
                "it was the season of Light,\n" +
                "it was the season of Darkness,\n" +
                "it was the spring of hope,\n" +
                "it was the winter of despair,\n" +
                "\n" +
                "we had everything before us, we had nothing before us, we were all going direct to Heaven, we were all going direct the other way— in short, the period was so far like the present period, that some of its noisiest authorities insisted on its being received, for good or for evil, in the superlative degree of comparison only.\n" +
                "\n" +
                "There were a king with a large jaw and a queen with a plain face, on the throne of England; there were a king with a large jaw and a queen with a fair face, on the throne of France. In both countries it was clearer than crystal to the lords of the State preserves of loaves and fishes, that things in general were settled for ever.\n" +
                "\n" +
                "It was the year of Our Lord one thousand seven hundred and seventy-five. Spiritual revelations were conceded to England at that favoured period, as at this. Mrs. Southcott had recently attained her five-and-twentieth blessed birthday, of whom a prophetic private in the Life Guards had heralded the sublime appearance by announcing that arrangements were made for the swallowing up of London and Westminster. Even the Cock-lane ghost had been laid only a round dozen of years, after rapping out its messages, as the spirits of this very year last past (supernaturally deficient in originality) rapped out theirs. Mere messages in the earthly order of events had lately come to the English Crown and People, from a congress of British subjects in America: which, strange to relate, have proved more important to the human race than any communications yet received through any of the chickens of the Cock-lane brood.\n" +
                "\n" +
                "France, less favoured on the whole as to matters spiritual than her sister of the shield and trident, rolled with exceeding smoothness down hill, making paper money and spending it. Under the guidance of her Christian pastors, she entertained herself, besides, with such humane achievements as sentencing a youth to have his hands cut off, his tongue torn out with pincers, and his body burned alive, because he had not kneeled down in the rain to do honour to a dirty procession of monks which passed within his view, at a distance of some fifty or sixty yards. It is likely enough that, rooted in the woods of France and Norway, there were growing trees, when that sufferer was put to death, already marked by the Woodman, Fate, to come down and be sawn into boards, to make a certain movable framework with a sack and a knife in it, terrible in history. It is likely enough that in the rough outhouses of some tillers of the heavy lands adjacent to Paris, there were sheltered from the weather that very day, rude carts, bespattered with rustic mire, snuffed about by pigs, and roosted in by poultry, which the Farmer, Death, had already set apart to be his tumbrils of the Revolution. But that Woodman and that Farmer, though they work unceasingly, work silently, and no one heard them as they went about with muffled tread: the rather, forasmuch as to entertain any suspicion that they were awake, was to be atheistical and traitorous.\n" +
                "\n" +
                "In England, there was scarcely an amount of order and protection to justify much national boasting. Daring burglaries by armed men, and highway robberies, took place in the capital itself every night; families were publicly cautioned not to go out of town without removing their furniture to upholsterers' warehouses for security; the highwayman in the dark was a City tradesman in the light, and, being recognised and challenged by his fellow-tradesman whom he stopped in his character of “the Captain,” gallantly shot him through the head and rode away; the mail was waylaid by seven robbers, and the guard shot three dead, and then got shot dead himself by the other four, “in consequence of the failure of his ammunition:” after which the mail was robbed in peace; that magnificent potentate, the Lord Mayor of London, was made to stand and deliver on Turnham Green, by one highwayman, who despoiled the illustrious creature in sight of all his retinue; prisoners in London gaols fought battles with their turnkeys, and the majesty of the law fired blunderbusses in among them, loaded with rounds of shot and ball; thieves snipped off diamond crosses from the necks of noble lords at Court drawing-rooms; musketeers went into St. Giles's, to search for contraband goods, and the mob fired on the musketeers, and the musketeers fired on the mob, and nobody thought any of these occurrences much out of the common way. In the midst of them, the hangman, ever busy and ever worse than useless, was in constant requisition; now, stringing up long rows of miscellaneous criminals; now, hanging a housebreaker on Saturday who had been taken on Tuesday; now, burning people in the hand at Newgate by the dozen, and now burning pamphlets at the door of Westminster Hall; to-day, taking the life of an atrocious murderer, and to-morrow of a wretched pilferer who had robbed a farmer's boy of sixpence.\n" +
                "\n" +
                "All these things, and a thousand like them, came to pass in and close upon the dear old year one thousand seven hundred and seventy-five. Environed by them, while the Woodman and the Farmer worked unheeded, those two of the large jaws, and those other two of the plain and the fair faces, trod with stir enough, and carried their divine rights with a high hand. Thus did the year one thousand seven hundred and seventy-five conduct their Greatnesses, and myriads of small creatures—the creatures of this chronicle among the rest—along the roads that lay before them.\n" +
                "\n" +
                "\n");
*/
        testStrings.add( "Eternal Father bless our land\n" +
                "Guard us with Thy mighty hand\n" +
                "Keep us free from evil powers\n" +
                "Be our light through countless hours\n" +
                "To our leaders, Great Defender,\n" +
                "Grant true wisdom from above\n" +
                "Justice, truth be ours forever\n" +
                "Jamaica, land we love\n" +
                "Jamaica, Jamaica, Jamaica, land we love.\n" +
                "\n" +
                "Teach us true respect for all\n" +
                "Stir response to duty's call\n" +
                "Strengthen us the weak to cherish\n" +
                "Give us vision lest we perish\n" +
                "Knowledge send us, Heavenly Father,\n" +
                "Grant true wisdom from above\n" +
                "Justice, truth be ours forever\n" +
                "Jamaica, land we love\n" +
                "Jamaica, Jamaica, Jamaica, land we love.");

        for( String testString : testStrings ) {
            StringPartCompressor compressor = new StringPartCompressor( testString, 1 );

            String compressedText = compressor.getCompressedText();

            System.out.println( "Original        : " + testString );
            System.out.println( "Size(Original)  : " + testString.length());
            System.out.println( "Compressed      : " + compressedText );
            System.out.println( "Size(Compressed): " + compressedText.length() );

            StringPartUncompressor uncompressor = new StringPartUncompressor( compressedText );
            String uncompressed = uncompressor.getUncompressedString();
            System.out.println( "Uncompressed    : " +  uncompressed );

            assertTrue( uncompressed.equals( testString ));
            System.out.println( "\n\n");

        }

    }
}
