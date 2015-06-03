package decoder;

import exception.QualityFormatException;

/**
 * Quality decoder for fastq files.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class FastqQualityDecoder implements QualityDecoder {

	/**
	 * The offset of the quality
	 */
	private QualityEncoding encoding = QualityEncoding.SANGER;

	public FastqQualityDecoder(QualityEncoding offset) {
		this.encoding = offset;
	}

	@Override
	public int[] decodeQuality(String quality) throws QualityFormatException {
		int[] qual = new int[quality.length()];
		for (int i = 0; i < qual.length; i++) {
			int c = quality.charAt(i);
			int q = c - encoding.getOffset();
			if ((q < encoding.lowerBound) || (q > encoding.upperBoud)) {
				throw new QualityFormatException(quality);
			}
			qual[i] = c - encoding.getOffset();
		}
		return qual;
	}

	/**
	 * @return the encoding
	 */
	public QualityEncoding getEncoding() {
		return encoding;
	}

	/**
	 * Possible offsets for fastq files
	 * 
	 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
	 *         Bacci</a>
	 * 
	 */
	public enum QualityEncoding {

		/**
		 * Sanger encoding
		 */
		SANGER(33, 0, 40, "Sanger"),

		/**
		 * Solexa encoding
		 */
		SOLEXA(64, -5, 40, "Solexa"),

		/**
		 * Illumina 1.3 - 1.5 encoding
		 */
		ILLUMINA13(64, 0, 40, "Illumina 1.3"),

		/**
		 * Illumina 1.5 - 1.8 encoding
		 */
		ILLUMINA15(64, 3, 40, "Illumina 1.5"),

		/**
		 * Illumina 1.8+ encoding
		 */
		ILLUMINA18(33, 0, 41, "Illumina 1.8"),

		/**
		 * General Phred+33 quality encoding (bounds here are the lowest and the
		 * highest possible for all encodings)
		 */
		PHRED33(33, -5, 50, "Undefined Phred+33"),

		/**
		 * General Phred+64 quality encoding (bounds here are the lowest and the
		 * highest possible for all encodings)
		 */
		PHRED64(64, -5, 50, "Undefined Phred+64");

		/**
		 * Quality offset
		 */
		int offset;

		/**
		 * Upper bound for quality values
		 */
		int upperBoud;

		/**
		 * Lower bound for quality values
		 */
		int lowerBound;

		/**
		 * Encoding name
		 */
		String encoding = null;

		/**
		 * Creates a specific encoding
		 * 
		 * @param offset
		 *            the quality offset
		 * @param lowerBound
		 *            the lower bound for quality
		 * @param upperBound
		 *            the upper bound for quality
		 * @param encoding
		 *            the encoding name
		 */
		private QualityEncoding(int offset, int lowerBound, int upperBound,
				String encoding) {
			this.offset = offset;
			this.upperBoud = upperBound;
			this.lowerBound = lowerBound;
			this.encoding = encoding;
		}

		/**
		 * Returns the offset
		 * 
		 * @return the quality offset
		 */
		private int getOffset() {
			return offset;
		}

		/**
		 * Returns the encoding name
		 * 
		 * @return the encoding name
		 */
		public String getEncodingName() {
			return encoding;
		}

		/**
		 * @return the upperBoud
		 */
		public int getUpperBoud() {
			return upperBoud;
		}

		/**
		 * @return the lowerBound
		 */
		public int getLowerBound() {
			return lowerBound;
		}
	}

}
