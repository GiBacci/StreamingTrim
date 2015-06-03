package exception;

/**
 * A quality format exception.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class QualityFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2096978298080602865L;

	/**
	 * The string representing the quality that has generated the exception
	 */
	private String quality = null;

	private final static String MESSAGE = "Sequence quality is not in the correct format";

	public QualityFormatException(String quality) {
		this.quality = quality;
	}

	/**
	 * @return the quality
	 */
	public String getQuality() {
		return quality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return MESSAGE;
	}

}
