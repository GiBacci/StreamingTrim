package decoder;


public class FastaQualityDecoder implements QualityDecoder {
	
	private static final String QUAL_SEPARATOR = "(\\s{1,2})";
	

	@Override
	public int[] decodeQuality(String quality) {
		String[] splitted = quality.split(QUAL_SEPARATOR);
		int[] qualityValues = new int[splitted.length];
		for (int i = 0; i < splitted.length; i++) {
			int q = Integer.parseInt(splitted[i].trim());
			qualityValues[i] = q;
		}
		return qualityValues;
	}
}
