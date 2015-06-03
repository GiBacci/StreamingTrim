package listeners;

import java.util.HashMap;

import stat.CategoricalFrequencyAnalyzer;
import stat.OrderedDistribution;

public class SequenceFrequencyListener extends
		CategoricalFrequencyAnalyzer<String> implements SequenceListener {

	public SequenceFrequencyListener() {
		super(new OrderedDistribution<Long>(), new HashMap<String, Long>());
	}

	@Override
	public void sequence(String id, String sequence, String quality) {		
		super.addObject(sequence);
	}

}
