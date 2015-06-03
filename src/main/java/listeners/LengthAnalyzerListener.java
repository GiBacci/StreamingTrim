package listeners;

import stat.FrequencyAnalyzer;
import stat.OrderedDistribution;

/**
 * Length values analyzer. This class stores the length of each sequence passed
 * in a {@link StatUtils} object generating some useful statistics if needed
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public class LengthAnalyzerListener extends FrequencyAnalyzer<Integer>
		implements SequenceListener {

	public LengthAnalyzerListener() {
		super(new OrderedDistribution<Integer>());
	}

	@Override
	public void sequence(String id, String sequence, String quality) {
		// Testing the strings (if some are null the method will stop here)
		if ((sequence == null) || (quality == null) || sequence.isEmpty()
				|| quality.isEmpty()) {
			return;
		}
		super.addValue(sequence.length());
	}
}
