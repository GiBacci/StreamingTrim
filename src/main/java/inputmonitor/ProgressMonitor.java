package inputmonitor;

/**
 * A lang process that has to be monitored
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public interface ProgressMonitor {
	/**
	 * Adds a display to this process
	 * 
	 * @param display
	 *            the display to add
	 */
	public void addProgressDisplay(ProgressDisplay display);

	/**
	 * Remove a display to this process
	 * 
	 * @param display
	 *            the display to remove
	 */
	public void removeProgressDisplay(ProgressDisplay display);

	/**
	 * Notifies all displays associated with this process
	 */
	public void notifyProgress(int progress);
}
