package bacci.giovanni.streaming_trim;

import org.junit.Test;

import stat.Distribution;
import stat.OrderedDistribution;
import junit.framework.TestCase;

/**
 * Test case for distribution.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public class TestStats extends TestCase {

	private Distribution<Integer> dist;

	private int first = 2;

	private int second = 3;

	/**
	 * Initializing test
	 */
	public TestStats() {
		this.dist = new OrderedDistribution<Integer>();
		dist.addValue(first);
		dist.addValue(second);
	}

	@Test
	public void testMean() {
		assertEquals(dist.mean(), (double) (first + second) / 2);
	}

	@Test
	public void testVariance() {
		double res = (dist.mean() - first) * (dist.mean() - first);
		res += (dist.mean() - second) * (dist.mean() - second);
		assertEquals(dist.variance(), res);
	}

	@Test
	public void testMax() {
		assertEquals(dist.getMaxValue().intValue(), Math.max(first, second));
	}

	@Test
	public void testMin() {
		assertEquals(dist.getMinValue().intValue(), Math.min(first, second));
	}

	@Test
	public void testCount() {
		assertEquals(dist.getCount(first), 1);
		assertEquals(dist.getCount(second), 1);
	}

	@Test
	public void testFences() {
		assertEquals(dist.getLowerFence(), (double) Math.min(first, second));
		assertEquals(dist.getUpperFence(), (double) Math.max(first, second));
	}

}
