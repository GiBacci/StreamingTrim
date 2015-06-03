package listeners;

/**
 * Sequence generator interface
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public interface SequenceGenerator {
	/**
	 * Adds a listener to this sequence generator
	 * 
	 * @param listener
	 *            the listened
	 */
	public void addListener(SequenceListener listener);

	/**
	 * Removes a listener from this sequence generator
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeListener(SequenceListener listener);

	/**
	 * Notifies all listeners
	 * 
	 * @param id
	 *            the sequence id
	 * @param sequence
	 *            the sequence
	 * @param quality
	 *            the quality
	 */
	public void notifyListeners(String id, String sequence, String quality);
}
