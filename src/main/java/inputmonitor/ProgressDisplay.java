package inputmonitor;

/**
 * Display for pregress of a long process
 * 
 * @see ProgressMonitor
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public interface ProgressDisplay {

	/**
	 * Pushes a new progress value
	 * 
	 * @param progress
	 *            the new progress value
	 */
	public void getProgress(int progress);
}
