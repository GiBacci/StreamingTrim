package listeners;

import java.util.ArrayList;
import java.util.List;

import io.FileSequenceWriter;
import decoder.QualityDecoder;
import exception.QualityFormatException;

/**
 * Simple trimmer. The trimmer implements the {@link SequenceListener} interface
 * and the {@link SequenceGenerator} interface in order to be able to write out
 * and analyze trimmed sequences
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class TrimmingListener implements SequenceListener, SequenceGenerator {

	/**
	 * The cutoff
	 */
	protected int cutoff = 18;

	/**
	 * If the length of a trimmed sequence is equal or higher than this value
	 * the sequence is written out otherwise it will be deleted, If this values
	 * is equal to -1 the length check step is skipped.
	 */
	private int minLength = -1;

	/**
	 * The quality decoder
	 */
	protected QualityDecoder qdecoder = null;

	/**
	 * The writer for trimmed sequences
	 */
	protected FileSequenceWriter writer = null;

	/**
	 * List of sequence listener objects
	 */
	private List<SequenceListener> listeners = new ArrayList<SequenceListener>();
	

	public TrimmingListener(QualityDecoder qdecoder) {
		this.qdecoder = qdecoder;
	}

	/**
	 * @param cutoff
	 *            the cutoff to set
	 */
	public void setCutoff(int cutoff) {
		this.cutoff = cutoff;
	}

	/**
	 * @param minLength
	 *            the minLength to set
	 */
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	@Override
	public void sequence(String id, String sequence, String quality) {
		int cutIndex = sequence.length();
		if (qdecoder != null) {
			int qual[] = null;

			try {
				qual = qdecoder.decodeQuality(quality);
			} catch (QualityFormatException e1) {
				String error = String.format(
						"Quality encoding is not correct: %s", quality);
				System.err.println(error);
			}

			cutIndex = qual.length;
			int cumQuality = 0;

			for (int i = (qual.length - 1); i >= 0; i--) {
				int cumCutoff = cutoff * (cutIndex - i);
				cumQuality += qual[i];

				if ((cumQuality - cumCutoff) < 0) {
					cutIndex = i;
					cumQuality = 0;
				}
			}

		}

		if ((minLength > -1) && (cutIndex < minLength)) {
			notifyListeners(id, "", "");
		} else {
			notifyListeners(id, sequence.substring(0, cutIndex),
					quality.substring(0, cutIndex));

		}

	}

	/**
	 * @param qdecoder
	 *            the qdecoder to set
	 */
	public void setQdecoder(QualityDecoder qdecoder) {
		this.qdecoder = qdecoder;
	}

	/**
	 * @param writer
	 *            the writer to set
	 */
	public void setWriter(FileSequenceWriter writer) {
		this.writer = writer;
	}

	/**
	 * @return the qdecoder
	 */
	public QualityDecoder getQdecoder() {
		return qdecoder;
	}

	@Override
	public void addListener(SequenceListener listener) {
		int index = listeners.indexOf(listener);
		if (index < 0) {
			listeners.add(listener);
		}

	}

	@Override
	public void removeListener(SequenceListener listener) {
		int index = listeners.indexOf(listener);
		if (index >= 0) {
			listeners.remove(listener);
		}

	}

	@Override
	public void notifyListeners(String id, String sequence, String quality) {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).sequence(id, sequence, quality);
		}

	}

}
