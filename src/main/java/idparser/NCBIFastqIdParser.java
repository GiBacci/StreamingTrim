package idparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Id parser for FASTQ sequence obtained through NCBI SRA
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public class NCBIFastqIdParser extends FastqIdParser {

	/**
	 * Format name
	 */
	private static final String NAME = "NCBI/SRA";

	/**
	 * Regular expression matching the NCBI SRA format.
	 */
	private final static String REGEX = "^@" /* Id starting character */
			+ "(.+?)" /* NCBI-assigned identifier */
			+ "\\s(.+?)?" /* Original id */
			+ "(?:\\s(.+?))?$"; /* Other description */

	/**
	 * Processing the id storing all available informations
	 * 
	 * @param id
	 *            the id
	 */
	protected NCBIFastqIdParser(String id) {
		Matcher m = Pattern.compile(REGEX).matcher(id);
		if (!m.matches())
			return;
		if (m.group(2) != null) {
			IdParser subParser = IdParserFactory.createParser("@" + m.group(2));
			if (subParser != null) {
				for (IdAttributes a : subParser.getAllAttributes())
					setAttribute(a, subParser.getAttribute(a));
			}
		}
		setAttribute(IdAttributes.FASTQ_TYPE, NAME);
		setAttribute(IdAttributes.NCBI_INDEX, m.group(1));
		if (m.group(3) != null)
			setAttribute(IdAttributes.NCBI_DESCRIPTION, m.group(3));
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
