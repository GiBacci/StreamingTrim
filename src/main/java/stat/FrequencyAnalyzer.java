package stat;

/**
 * Frequency distribution analyzer
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 * @param <T>
 *            the type of object to analyze
 */
public abstract class FrequencyAnalyzer<T extends Number> {

	/**
	 * The distribution
	 */
	private Distribution<T> dist;

	/**
	 * @param dist
	 *            the distribution
	 */
	public FrequencyAnalyzer(Distribution<T> dist) {
		this.dist = dist;
	}

	/**
	 * @return the frequency distribution
	 */
	public Distribution<T> getDistribution() {
		return dist;
	}

	/**
	 * Adds a new value to this frequency distribution
	 * 
	 * @param value
	 *            the value to add
	 */
	public void addValue(T value) {
		dist.addValue(value);
	}

}
