package stat;

import java.util.Map;

public abstract class CategoricalFrequencyAnalyzer<K> extends FrequencyAnalyzer<Long> {

	private Map<K, Long> freqMap;
	
	public CategoricalFrequencyAnalyzer(Distribution<Long> dist, Map<K, Long> freqMap) {
		super(dist);
		this.freqMap = freqMap;
	}
	
	public void addObject(K value) {
		if (freqMap.get(value) == null) {
			freqMap.put(value, 1L);
		} else {
			freqMap.put(value, freqMap.get(value) + 1);
		}
	}

	/* (non-Javadoc)
	 * @see stat.FrequencyAnalyzer#getDistribution()
	 */
	@Override
	public Distribution<Long> getDistribution() {
		for(K k : freqMap.keySet())
			super.addValue(freqMap.get(k));
		return super.getDistribution();
	}
	
	

}
