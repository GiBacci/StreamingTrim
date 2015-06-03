package stat;

import java.util.Set;

/**
 * Distribution of numbers.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 * @param <T>
 */
public interface Distribution<T extends Number> {

	/**
	 * Add an object to the distribution
	 * 
	 * @param value
	 *            the number to add
	 */
	public void addValue(T value);

	/**
	 * @return the values stored in this distribution
	 */
	public Set<T> getValues();

	/**
	 * The number of times that the given object has been found in the
	 * distribution
	 * 
	 * @param value
	 *            the object
	 * @return the number of times that the given object has been found in the
	 *         distribution or 0 if the object has not been found.
	 */
	public long getCount(T value);

	/**
	 * @return the average value of the distribution
	 * 
	 */
	public double mean();

	/**
	 * @return the mode of the distribution
	 */
	public T mode();

	/**
	 * @return the variance of the distribution
	 */
	public double variance();

	/**
	 * @return the standard deviation of the distribution
	 */
	public double sd();

	/**
	 * The specified percentile of the distribution ranging from 0.0 to 1.0.
	 * 
	 * @param percentile
	 *            the percentile, from 0.0 to 1.0
	 * @return the value of the specified percentile
	 */
	public double percentile(double percentile);

	/**
	 * @return the biggest value of the distribution
	 */
	public T getMaxValue();

	/**
	 * @return the highest value of the distribution
	 */
	public T getMinValue();

	/**
	 * @return the upper fence of the distribution
	 */
	public double getUpperFence();

	/**
	 * @return the lower fence of the distribution
	 */
	public double getLowerFence();

	/**
	 * The distribution binned with the specified parameters
	 * 
	 * @param min
	 *            min value
	 * @param max
	 *            max value
	 * @param numBins
	 *            numer of equal sized bins from min to max
	 * @return the distribution binned as specified
	 */
	public long[] getBinnedDistribution(long min, long max, int numBins);

	/**
	 * @return the number of elements added to the distribution
	 */
	public long N();

}
