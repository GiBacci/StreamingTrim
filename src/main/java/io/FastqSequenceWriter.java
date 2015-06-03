package io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * {@link FileSequenceWriter} implementation for writing sequence in fastq
 * format
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class FastqSequenceWriter implements FileSequenceWriter {
	
	/**
	 * The writer
	 */
	private Writer out = null;

	/**
	 * Creates a fastq writer 
	 * @param out the output file
	 */
	protected FastqSequenceWriter(OutputStream out) {
		this.out =  new OutputStreamWriter(out);
	}

	/**
	 * @see FileSequenceWriter#writeSequence(String, String, String)
	 */
	@Override
	public void writeSequence(String id, String sequence, String quality)
			throws IOException {
		String fmt = String.format("%1$s%n%2$s%n+%n%3$s%n", id, sequence,
				quality);
		out.write(fmt);
		out.flush();
	}

	@Override
	public void close() throws IOException {
		this.out.close();		
	}

}
