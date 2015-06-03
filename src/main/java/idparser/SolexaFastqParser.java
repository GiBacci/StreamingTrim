package idparser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Id parser for Solexa/Illumina reads.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public class SolexaFastqParser extends FastqIdParser {

	/**
	 * The name of the format
	 */
	private final static String NAME = "Illumina/Solexa";

	/**
	 * Regex for standard Illumina ids
	 */
	protected final static String REGEX = "^@" /* Id starting character */
			+ "(.+?):" /* the unique instrument name */
			+ "(\\d+?):" /* flowcell lane */
			+ "(\\d+?):" /* tile number within the flowcell lane */
			+ "(\\d+?):" /* x-coordinate of the cluster within the tile */
			+ "(\\d+?)" /* y-coordinate of the cluster within the tile */
			+ "(?:#(\\d+?|\\w+?))?" /*
									 * index number (or sequence) for a
									 * multiplexed sample (0 for no indexing)
									 */
			+ "(?:/(1|2))?$"; /*
							 * the member of a pair, /1 or /2 (paired-end or
							 * mate-pair reads only)
							 */

	/**
	 * Map of regex indices
	 */
	@SuppressWarnings("serial")
	private final static Map<IdAttributes, Integer> regexAttributeMap = new HashMap<IdAttributes, Integer>() {
		{
			put(IdAttributes.INSTRUMENT_NAME, 1);
			put(IdAttributes.FLOWCELL_LANE, 2);
			put(IdAttributes.TILE_NUMBER, 3);
			put(IdAttributes.X_COORD, 4);
			put(IdAttributes.Y_COORD, 5);
			put(IdAttributes.INDEX_ID, 6);
			put(IdAttributes.PAIR_MEMBER, 7);
		}
	};

	/**
	 * Build a parser with the correct attributes from an id
	 * 
	 * @param id
	 *            the id
	 */
	protected SolexaFastqParser(String id) {
		Matcher m = Pattern.compile(REGEX).matcher(id);
		// Last guard block
		if (!m.matches())
			return;
		setAttribute(IdAttributes.FASTQ_TYPE, NAME);
		for (IdAttributes a : regexAttributeMap.keySet()) {
			String value = m.group(regexAttributeMap.get(a));
			if (value != null)
				setAttribute(a, m.group(regexAttributeMap.get(a)));
		}
	}

	/**
	 * matching method
	 * 
	 * @param id
	 *            the id to match
	 * @return <code>true</code> if the given id matches the id structure of
	 *         this format
	 */
	protected static boolean matchID(String id) {
		return id.matches(REGEX);
	}

}
