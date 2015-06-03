package exception;

/**
 * File format exception.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class FileFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7429732565462918245L;

	/**
	 * The line at which the exception has been generated. If -1 the line has
	 * not been specified
	 */
	private long line = -1;

	/**
	 * The id of the sequence that has generated the exception
	 */
	private String seqId = null;

	/**
	 * Creates the exception with a message
	 * 
	 * @param message
	 *            the message
	 */
	public FileFormatException(String message) {
		super(message);
	}

	/**
	 * Creates the exception with a message and the line number where it has
	 * been thrown
	 * 
	 * @param message
	 *            the message
	 * @param line
	 *            the line number
	 */
	public FileFormatException(String message, long line) {
		super(message);
		this.line = line;
	}

	/**
	 * Creates the exception with the message, the id and the line number where
	 * it has been thrown
	 * 
	 * @param message
	 *            the message
	 * @param line
	 *            the line number
	 * @param id
	 *            the sequence id
	 */
	public FileFormatException(String message, long line, String id) {
		super(message);
		this.line = line;
		this.seqId = id;
	}

	/**
	 * @return the seqId
	 */
	public String getSeqId() {
		return seqId;
	}

	/**
	 * @param line
	 *            the line to set
	 */
	public long getLine() {
		return line;
	}

}
