package listeners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stat.FrequencyAnalyzer;
import stat.OrderedDistribution;

/**
 * Listener for GC count
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public class GCAnalyzerListener extends FrequencyAnalyzer<Double> implements SequenceListener {

	/**
	 * GC recognition pattern (case insensitive)
	 */
	private static final Pattern GC = Pattern.compile("(G|C)",
			Pattern.CASE_INSENSITIVE);

	/**
	 * Number of bases found
	 */
	private long baseCount = 0;

	/**
	 * Number of GC found
	 */
	private long gcCount = 0;
	

	public GCAnalyzerListener() {
		super(new OrderedDistribution<Double>());
	}

	@Override
	public void sequence(String id, String sequence, String quality) {
		double gc = 0.0;
		baseCount += sequence.length();
		Matcher m = GC.matcher(sequence);
		while (m.find()) {
			gc++;
			gcCount++;
		}
		super.addValue(gc / sequence.length());
	}

	/**
	 * @return the GC content as the proportion between all bases and GC
	 */
	public double getGCcontent() {
		return (double)gcCount / baseCount;
	}
		
}
