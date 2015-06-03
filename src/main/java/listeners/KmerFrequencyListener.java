package listeners;

import java.util.HashMap;

import stat.CategoricalFrequencyAnalyzer;
import stat.OrderedDistribution;

/**
 * Listeners for kmer distribution
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public class KmerFrequencyListener extends CategoricalFrequencyAnalyzer<String>
		implements SequenceListener {

	/**
	 * Kmer length
	 */
	private int kmerLength;

	/**
	 * Creates a Kmer listener with the kmer length specified
	 * 
	 * @param kmerLength
	 *            the length of kmers
	 */
	public KmerFrequencyListener(int kmerLength) {
		super(new OrderedDistribution<Long>(), new HashMap<String, Long>());
		this.kmerLength = kmerLength;
	}

	@Override
	public void sequence(String id, String sequence, String quality) {
		for (int i = 0; i < (sequence.length() - kmerLength); i++) {
			String kmer = sequence.substring(i, (i + kmerLength));
			super.addObject(kmer);
		}

	}

}
