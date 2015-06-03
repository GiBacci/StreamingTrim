package idparser;

/**
 * Simple factory class.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public class IdParserFactory {

	/**
	 * Static method for instantiating an id parser
	 * 
	 * @param id
	 *            the sequence id
	 * @return the correct {@link IdParser}
	 */
	public static IdParser createParser(String id) {
		if (SolexaFastqParser.matchID(id))
			return new SolexaFastqParser(id);
		if (CasavaFastqParser.matchID(id))
			return new CasavaFastqParser(id);
		if(NCBIFastqIdParser.matchID(id))
			return new NCBIFastqIdParser(id);
		return null;
	}
}
