package idparser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for Illumina casava format.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public class CasavaFastqParser extends FastqIdParser {

	/**
	 * Name of the format
	 */
	private static final String NAME = "Illumina/Casava";

	/**
	 * Regular expression matching the correct Casava format
	 */
	protected final static String REGEX = "^@" /* Id starting character */
			+ "(.+?):" /* the unique instrument name */
			+ "(\\d+?):" /* the run id */
			+ "(.+?):" /* the flowcell id */
			+ "(\\d+?):" /* flowcell lane */
			+ "(\\d+?):" /* tile number within the flowcell lane */
			+ "(\\d+?):" /* x-coordinate of the cluster within the tile */
			+ "(\\d+?)" /* y-coordinate of the cluster within the tile */
			+ "\\s(?:(1|2):)?" /*
								 * the member of a pair, 1 or 2 (paired-end or
								 * mate-pair reads only)
								 */
			+ "(Y|N):" /* Y if the read is filtered, N otherwise */
			+ "(\\d+?):" /*
						 * 0 when none of the control bits are on, otherwise it
						 * is an even number
						 */
			+ "(\\w+?)$"; /* index sequence */

	/**
	 * Map of regex indices
	 */
	@SuppressWarnings("serial")
	private final static Map<IdAttributes, Integer> regexAttributeMap = new HashMap<IdAttributes, Integer>() {
		{
			put(IdAttributes.INSTRUMENT_NAME, 1);
			put(IdAttributes.RUN_ID, 2);
			put(IdAttributes.FLOWCELL_ID, 3);
			put(IdAttributes.FLOWCELL_LANE, 4);
			put(IdAttributes.TILE_NUMBER, 5);
			put(IdAttributes.X_COORD, 6);
			put(IdAttributes.Y_COORD, 7);
			put(IdAttributes.PAIR_MEMBER, 8);
			put(IdAttributes.FAILS_FILTER, 9);
			put(IdAttributes.CONTROL_BITS, 10);
			put(IdAttributes.INDEX_ID, 11);

		}
	};

	/**
	 * Build a parser with the correct attributes from an id
	 * 
	 * @param id
	 *            the id
	 */
	protected CasavaFastqParser(String id) {
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
