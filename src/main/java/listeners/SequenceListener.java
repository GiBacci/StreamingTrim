package listeners;

import io.FileSequenceReader;

/**
 * Sequence listener interface.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public interface SequenceListener {
	/**
	 * Method called by a {@link FileSequenceReader} object while reading from a
	 * sequence file
	 * 
	 * @param id
	 *            the id of the sequence
	 * @param sequence
	 *            the sequence
	 * @param quality
	 *            the quality
	 * @see FileSequenceReader#readAllSequence(SequenceListener)
	 */
	public void sequence(String id, String sequence, String quality);
}
