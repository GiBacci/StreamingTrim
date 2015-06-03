package stat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Frequency distribution for multiple series of objects
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 * @param <K>
 *            the type of the indices
 * @param <T>
 *            the type of distributions
 * @param <I>
 *            the type of objects stored in the distributions
 */
public abstract class MultipleFrequencyAnalyzer<K, T extends Distribution<I>, I extends Number> {
	/**
	 * The list of distributions
	 */
	private Map<K, T> dist;

	/**
	 * Constructor
	 */
	public MultipleFrequencyAnalyzer() {
		this.dist = new HashMap<K, T>();
	}

	/**
	 * @param index
	 *            the index of the distribution
	 * @return the distribution associated with the given index
	 */
	public T getDistribution(K index) {
		return dist.get(index);
	}

	/**
	 * Adds a distribution to this object
	 * 
	 * @param index
	 *            the index of the distribution
	 * @param dist
	 *            the distribution
	 */
	public void addDistribution(K index, T dist) {
		this.dist.put(index, dist);
	}

	/**
	 * @return the number of distributions stored
	 */
	public int size() {
		return dist.size();
	}

	/**
	 * @return the set of indices used
	 */
	public Set<K> getIndices() {
		return dist.keySet();
	}
}
