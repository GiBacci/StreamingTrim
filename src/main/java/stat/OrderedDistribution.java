package stat;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Numeric distribution.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class OrderedDistribution<T extends Number> implements Distribution<T> {
	/**
	 * A map of ordered values
	 */
	private Map<T, Long> freqTable = new TreeMap<T, Long>();

	/**
	 * Number of values stored
	 */
	private long N = 0;

	@Override
	public void addValue(T value) {
		if (freqTable.get(value) == null) {
			freqTable.put(value, 1L);
		} else {
			freqTable.put(value, (freqTable.get(value) + 1));
		}
		N++;
	}

	@Override
	public Set<T> getValues() {
		return freqTable.keySet();
	}

	@Override
	public long getCount(T value) {
		if (freqTable.get(value) != null)
			return freqTable.get(value);
		return 0;
	}

	/**
	 * @return the mean of the distribution or {@link Double#NaN} if no value
	 *         has been added to this object.
	 */
	@Override
	public double mean() {
		if (N == 0)
			return Double.NaN;
		double sum = 0;
		for (T k : freqTable.keySet())
			sum += k.doubleValue() * freqTable.get(k);
		return sum / N;
	}

	/**
	 * @return the mode of the distribution or <code>null</code> if no value has
	 *         been added to this object.
	 */
	@Override
	public T mode() {
		if (N == 0)
			return null;
		T mode = null;
		long found = 0;

		for (T k : freqTable.keySet()) {
			long time = freqTable.get(k);
			if (time > found) {
				found = time;
				mode = k;
			}
		}
		return mode;
	}

	/**
	 * @return the variance of the distribution or {@link Double#NaN} if no
	 *         value has been added to this object.
	 */
	@Override
	public double variance() {
		if (N == 0)
			return Double.NaN;
		double mean = mean();
		double temp = 0;
		for (T k : freqTable.keySet())
			temp += ((k.doubleValue() - mean) * (k.doubleValue() - mean))
					* freqTable.get(k);
		return temp / (N - 1);
	}

	/**
	 * @return the standard deviation of the distribution or {@link Double#NaN}
	 *         if no value has been added to this object.
	 */
	@Override
	public double sd() {
		if (N == 0)
			return Double.NaN;
		return Math.sqrt(variance());
	}

	/**
	 * @param percentile
	 *            the percentile of the distribution in decimal format (10% =
	 *            0.1; 20% = 0.2 ... and so on)
	 * @return the percentile of the distribution or {@link Double#NaN} if no
	 *         value has been added to this object. The percentile is calculated
	 *         using the "Nearest Rank method".
	 */
	@Override
	public double percentile(double percentile) {
		if (N == 0)
			return Double.NaN;
		// Number of values in the quartile
		double indexing = N * percentile;

		// If indexing has digits the index array contains only the number
		// indexing + 1. On the other hand, if indexing is an even number (it
		// has no decimals), index array contains two numbers: indexing and
		// indexing + 1
		int index[] = (indexing % 1 == 0) ? new int[] { (int) indexing,
				(int) indexing + 1 } : new int[] { (int) indexing + 1 };
		double sum = 0.0;
		for (int i : index)
			sum += getValue(i).doubleValue();
		return sum / index.length;
	}

	/**
	 * @return the biggest number added to this object or <code>null</code> if
	 *         no value has been added to this object.
	 */
	@Override
	public T getMaxValue() {
		if (N == 0)
			return null;
		@SuppressWarnings("unchecked")
		T[] values = (T[]) freqTable.keySet().toArray(
				new Number[freqTable.size()]);
		return values[values.length - 1];
	}

	/**
	 * @return the smallest number added to this object or <code>null</code> if
	 *         no value has been added to this object.
	 */
	@Override
	public T getMinValue() {
		if (N == 0)
			return null;
		@SuppressWarnings("unchecked")
		T[] values = (T[]) freqTable.keySet().toArray(
				new Number[freqTable.size()]);
		return values[0];
	}

	/**
	 * @return the upper fence of the values added to this object as:<br>
	 *         <code>percentile(0.3) + 1.5(|percentile(0.3) - percentile(0.1)|)</code>
	 * <br>
	 *         This method will return the lower value between the one computed
	 *         with the formula above and the highest value added to this
	 *         object.
	 * @see Distribution#percentile(double)
	 */
	@Override
	public double getUpperFence() {
		if (N == 0)
			return Double.NaN;
		double q1 = percentile(0.25);
		double q3 = percentile(0.75);

		double max = getMaxValue().doubleValue();

		double iqr = Math.abs(q1 - q3);
		double c = 1.5;

		double ufence = q3 + (iqr * c);
		double res = (ufence > max) ? max : ufence;
		return res;
	}

	/**
	 * @return the lower fence of the values added to this object as:<br>
	 *         <code>percentile(0.1) - 1.5(|percentile(0.3) - percentile(0.1)|)</code>
	 * <br>
	 *         This method will return the higher value between the one computed
	 *         with the formula above and the lowest value added to this object.
	 * @see Distribution#percentile(double)
	 */
	@Override
	public double getLowerFence() {
		if (N == 0)
			return Double.NaN;
		double q1 = percentile(0.25);
		double q3 = percentile(0.75);

		double min = getMinValue().doubleValue();

		double iqr = Math.abs(q1 - q3);
		double c = 1.5;

		double lfence = q1 - (iqr * c);
		double res = (lfence < min) ? min : lfence;
		return res;
	}

	/**
	 * @param min
	 *            start of binned data
	 * @param max
	 *            end of binned data
	 * @param numBins
	 *            number of bins
	 * @return the distribution binned as specified by the parameters set
	 */
	@Override
	public long[] getBinnedDistribution(long min, long max, int numBins) {
		Set<T> data = freqTable.keySet();
		long[] result = new long[numBins];
		long delta = max - min;

		double binSize = (double) delta / numBins;

		for (T d : data) {
			int bin = (int) ((d.doubleValue() - min) / binSize);
			if (bin < 0) { /* this data is smaller than min */
			} else if (bin > numBins) { /* this data point is bigger than max */
			} else if (bin == numBins) { /*
										 * last element will be included in the
										 * last bin
										 */
				result[--bin] += freqTable.get(d);
			} else {
				result[bin] += freqTable.get(d);
			}
		}
		return result;
	}
	
	@Override
	public long N() {
		return N;
	}

	/**
	 * @param index
	 *            the index of the value
	 * @return the value at the specified index of the frequency map.
	 * @throws IndexOutOfBoundsException
	 *             if index is higher than N
	 */
	private T getValue(int index) {
		if (index > N) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}
		long f = 0;
		for (T i : freqTable.keySet()) {
			f += freqTable.get(i);
			if (f >= index) {
				return i;
			}
		}
		return null;
	}



}
