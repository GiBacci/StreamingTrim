package idparser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Fastq id parser
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public abstract class FastqIdParser implements IdParser {

	/**
	 * Map of attributes
	 */
	private Map<IdAttributes, String> attributeMap;

	/**
	 * Protected constructor.
	 */
	protected FastqIdParser() {
		this.attributeMap = new HashMap<IdAttributes, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see idparser.IdParser#getAttribute(idparser.IdAttributes)
	 */
	@Override
	public String getAttribute(IdAttributes attribute) {
		return attributeMap.get(attribute);
	}

	/**
	 * @param attribute
	 *            the attribute to set
	 * @param value
	 *            the value of the attribute
	 */
	protected void setAttribute(IdAttributes attribute, String value) {
		attributeMap.put(attribute, value);
	}

	/* (non-Javadoc)
	 * @see idparser.IdParser#getAllAttributes()
	 */
	@Override
	public Set<IdAttributes> getAllAttributes() {
		return attributeMap.keySet();
	}
	
	

}
