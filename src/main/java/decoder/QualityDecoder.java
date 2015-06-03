package decoder;

import exception.QualityFormatException;

/**
 * Quality decoder interface.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public interface QualityDecoder {

	/**
	 * Decode the quality of a sequence from a string to an array of quality
	 * values
	 * 
	 * @param quality
	 *            the quality
	 * @return an array of quality values
	 * @throws QualityFormatException
	 *             if the quality has an incorrect format according to the
	 *             encoding
	 */
	public int[] decodeQuality(String quality) throws QualityFormatException;
}
