package idparser;

import java.util.Set;

/**
 * Parser for sequence ids.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public interface IdParser {

	/**
	 * @param attribute
	 *            the attribute to retrieve
	 * @return the value associated with the specified attribute or
	 *         <code>null</code> if the attribute has not been found
	 */
	public String getAttribute(IdAttributes attribute);

	/**
	 * @return all id attributes set for this object
	 */
	public Set<IdAttributes> getAllAttributes();
}
