package listeners;

import java.util.logging.Level;
import java.util.logging.Logger;

import stat.MultipleFrequencyAnalyzer;
import stat.OrderedDistribution;
import decoder.QualityDecoder;
import exception.QualityFormatException;

/**
 * Quality analyzer. This class stores a frequency distribution for each
 * base in the sequences, from base number 1 to the last base of the longest sequence
 * found
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class QualityAnalyzerListener
		extends
		MultipleFrequencyAnalyzer<Integer, OrderedDistribution<Integer>, Integer>
		implements SequenceListener {

	/**
	 * Quality decoder
	 */
	private QualityDecoder decoder = null;

	/**
	 * The logger. It can be <code>null</code>. In this case errors will be
	 * printed using {@link System#err}
	 */
	private Logger logger = null;

	/**
	 * Creates an analyzer listener with the specified quality decoder
	 * 
	 * @param decoder
	 *            the quality decoder
	 */
	public QualityAnalyzerListener(QualityDecoder decoder) {
		this.decoder = decoder;
	}

	/**
	 * Creates an analyzer listener with the specified quality decoder and the
	 * specified logger
	 * 
	 * @param decoder
	 *            the quality decoder
	 * @param logger
	 *            the logger
	 */
	public QualityAnalyzerListener(QualityDecoder decoder, Logger logger) {
		this(decoder);
		this.logger = logger;
	}

	@Override
	public void sequence(String id, String sequence, String quality) {
		// Testing the strings (if some are null the method will stop here)
		if ((sequence == null) || (quality == null) || sequence.isEmpty()
				|| quality.isEmpty()) {
			return;
		}

		int[] q = null;

		try {
			q = decoder.decodeQuality(quality);
		} catch (QualityFormatException e) {
			if (logger != null) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}

		// Adding values
		for (int i = 0; i < q.length; i++) {
			if (super.getDistribution(i + 1) != null) {
				super.getDistribution(i + 1).addValue(q[i]);
			} else {
				OrderedDistribution<Integer> d = new OrderedDistribution<Integer>();
				d.addValue(q[i]);
				super.addDistribution(i + 1, d);
			}

		}
	}
}
