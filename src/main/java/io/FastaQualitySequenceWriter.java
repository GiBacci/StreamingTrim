package io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * {@link FileSequenceWriter} implementation for writing Fasta + Quality files
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class FastaQualitySequenceWriter implements FileSequenceWriter {

	/**
	 * The output sequence file
	 */
	private Writer seqOut = null;

	/**
	 * The output quality file
	 */
	private Writer qualOut = null;

	/**
	 * Cretaes a reader for a Fasta + Quality file
	 * 
	 * @param seqOut
	 *            the sequence file
	 * @param qualOut
	 *            the quality file
	 */
	protected FastaQualitySequenceWriter(OutputStream seqOut,
			OutputStream qualOut) {
		this.seqOut = new OutputStreamWriter(seqOut);
		if (seqOut != null)
			this.qualOut = new OutputStreamWriter(qualOut);
	}

	/**
	 * @see FileSequenceWriter#writeSequence(String, String, String)
	 */
	@Override
	public void writeSequence(String id, String sequence, String quality)
			throws IOException {
		String s = String.format("%1$s%n%2$s%n", id,
				FastaSequenceWriter.splitSeqIntoLines(sequence));
		String q = String.format("%1$s%n%2$s%n", id,
				splitQualIntoLines(quality));
		seqOut.write(s);
		seqOut.flush();

		if (qualOut != null) {
			qualOut.write(q);
			qualOut.flush();
		}

	}

	/**
	 * Private method for splitting a quality sequence into line of the length
	 * specified by the {@link FastaQualitySequenceWriter#CHAR_PER_LINE} field
	 * 
	 * @param line
	 *            the quality
	 * @return a well formatted qual sequence
	 */
	private String splitQualIntoLines(String line) {
		String regexOne = String.format("(?<=\\G.{%d})",
				FastaSequenceWriter.FASTA_NUM_CHAR * 3);
		String regexTwo = String.format("\\s%s", System.lineSeparator());
		String a = line.replaceAll(regexOne, System.lineSeparator());
		String res = a.replaceAll(regexTwo, System.lineSeparator());
		return res.substring(0, res.length() - 1);
	}

	@Override
	public void close() throws IOException {
		this.seqOut.close();
		this.qualOut.close();
	}
}
