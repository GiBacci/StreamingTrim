package inputmonitor;

/**
 * Character progress bar
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class CharacterDisplay implements ProgressDisplay {

	/**
	 * Size of the process.
	 */
	private int size = 0;

	/**
	 * The progress.
	 */
	private int progress = 0;

	/**
	 * The percentage of completion approximated.
	 */
	private int approx_progress = 0;

	/**
	 * A note displayed right to the progress bar.
	 */
	private String note = "";

	/**
	 * The progress bar format. It has to be an even number.
	 */
	private final static String PROGRESS_BAR = "%1s [%2$s%3$3d%% %4$s]";

	/**
	 * Depth of the bar
	 */
	private final static int BAR_DEPTH = 30;
	
	private boolean firstTime = true;

	/**
	 * Creates a display with the given size
	 * 
	 * @param size
	 *            the total size of the process
	 */
	public CharacterDisplay(int size) {
		this.size = size;
	}

	@Override
	public void getProgress(int progress) {
		this.progress += progress;
		int prog = (int) (((double) this.progress / (double) size) * 100);
		if ((approx_progress < prog) || firstTime) {
			approx_progress = prog;
			firstTime = false;
			System.out.print(buildBar() + "\r");
		}
	}

	/**
	 * Builds a bar of character.
	 * 
	 * @return the bar
	 */
	private String buildBar() {
		// Collecting information about the bar length
		int every = 100 / BAR_DEPTH;
		int bars = approx_progress / every;
		int subDepth = BAR_DEPTH / 2;

		// Creating the array of character corresponding to the percentage of
		// completion
		char a[] = new char[BAR_DEPTH];
		if (bars > BAR_DEPTH) {
			bars = BAR_DEPTH;
		}
		for (int i = 0; i < bars; i++) {
			a[i] = '=';
		}
		for (int i = bars; i < a.length; i++) {
			a[i] = ' ';
		}

		// Dividing the bar into two parts
		String all = new String(a);
		String before = all.substring(0, subDepth);
		String after = all.substring(subDepth, BAR_DEPTH);

		// Formatting the bar with the percentage value in the middle
		String res = String.format(PROGRESS_BAR, note, before, approx_progress,
				after);
		return res.trim();
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

}
