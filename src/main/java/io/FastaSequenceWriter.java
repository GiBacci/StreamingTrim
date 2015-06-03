package io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * This class writes sequences in fasta format discarding the quality string.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public class FastaSequenceWriter implements FileSequenceWriter {

	protected static final int FASTA_NUM_CHAR = 70;

	/**
	 * Writer for fasta sequences
	 */
	private Writer out;

	public FastaSequenceWriter(OutputStream out) {
		this.out = new OutputStreamWriter(out);
	}

	@Override
	public void writeSequence(String id, String sequence, String quality)
			throws IOException {
		id = (id.startsWith("@")) ? String.format("%s%s", ">", id.substring(1))
				: id;
		id = (id.startsWith(">")) ? String.format("%s%n", id) : String.format(
				"%s%s%n", ">", id);
		out.write(id);
		sequence = String.format("%s%n", splitSeqIntoLines(sequence));
		out.write(sequence);
		out.flush();
	}

	/**
	 * Protected method for splitting a sequence into line of the length
	 * specified by the num_char parameter. This method is used by the
	 * {@link FastaQualitySequenceWriter} class too.
	 * 
	 * @param line
	 *            the sequence
	 * @param num_char
	 *            number of characters per line
	 * @return a well formatted fasta sequence
	 */
	protected static String splitSeqIntoLines(String line) {
		String regex = String.format("(?<=\\G.{%d})", FASTA_NUM_CHAR);
		String res = line.replaceAll(regex, System.lineSeparator());
		return res.trim();
	}

	@Override
	public void close() throws IOException {
		this.out.close();
	}

}
