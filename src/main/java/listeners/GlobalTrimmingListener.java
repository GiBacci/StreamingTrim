package listeners;

import decoder.QualityDecoder;
import exception.QualityFormatException;

/**
 * Global trimming listener. This class deletes entire sequences if their quality
 * is lower than the cutoff. This algorithm can use an analysis window to check
 * the quality of the nucleotides. If the analysis window length is not set, the
 * algorithm will consider the whole sequence as its analysis window.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class GlobalTrimmingListener extends TrimmingListener {

	/**
	 * The analysis window's length.
	 */
	private int windowsLength = 20;

	/**
	 * If <code>true</code> the trimmer will set the windows length parameter
	 * equal to the length of the analyzed sequence
	 */
	private boolean wholeSequence = false;

	/**
	 * Creates a trimmer with the specified windows length
	 * 
	 * @param windowsLength
	 *            the length of the analysis window
	 */
	public GlobalTrimmingListener(QualityDecoder qdecoder, int windowsLength) {
		super(qdecoder);
		this.windowsLength = windowsLength;
	}

	/**
	 * Creates a trimmer with an analysis windows of the same length as the
	 * length of the sequences
	 */
	public GlobalTrimmingListener(QualityDecoder qdecoder) {
		super(qdecoder);
		this.wholeSequence = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see listeners.TrimmingListener#sequence(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void sequence(String id, String sequence, String quality) {
		boolean ok = true;

		if (qdecoder != null) {
			int qual[] = null;
			try {
				qual = qdecoder.decodeQuality(quality);
			} catch (QualityFormatException e1) {
				String error = String.format(
						"Quality encoding is not correct: %s", quality);
				System.err.println(error);
			}

			if (wholeSequence) {
				windowsLength = qual.length;
			}

			int lastIndex = qual.length - 1;
			int firstIndex = qual.length - windowsLength;

			int cumQuality = 0;
			int cumCutoff = cutoff * windowsLength;

			ok = true;

			while (firstIndex >= 0) {
				for (int i = lastIndex; i >= firstIndex; i--) {
					cumQuality += qual[i];
				}

				if ((cumQuality - cumCutoff) < 0) {
					ok = false;
					break;
				} else {
					firstIndex--;
					lastIndex--;
				}
			}
		}

		if (ok) {
			notifyListeners(id, sequence, quality);
		} else {
			notifyListeners(id, "", "");
		}

	}

}
