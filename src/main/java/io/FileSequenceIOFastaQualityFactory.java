package io;

import java.io.InputStream;
import java.io.OutputStream;
import decoder.FastaQualityDecoder;
import decoder.QualityDecoder;

/**
 * {@link FileSequenceIOFactory} implementation for dealing with fasta + qual
 * files
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class FileSequenceIOFastaQualityFactory extends FileSequenceIOFactory {

	/**
	 * The stream for sequence file
	 */
	private InputStream in;

	/**
	 * The stream for quality file
	 */
	private InputStream inq;

	/**
	 * Protected constructor
	 * 
	 * @param in
	 *            the stream for sequence file
	 * @param inq
	 *            the stream for quality file
	 */
	protected FileSequenceIOFastaQualityFactory(InputStream in, InputStream inq) {
		if (in == null || inq == null)
			throw new NullPointerException("Input streams must not be null");
		this.in = in;
		this.inq = inq;
	}

	@Override
	public FileSequenceReader createReader() {
		return new FastaQualitySequenceReader(in, inq);
	}

	@Override
	public QualityDecoder createQualityDecoder() {
		return new FastaQualityDecoder();
	}

	/**
	 * Facotry method for creating a {@link FastaQualitySequenceWriter}.
	 */
	@Override
	public FileSequenceWriter createWriter(OutputStream out,
			OutputStream... out2) {
		if (out == null || out2 == null || out2.length == 0)
			throw new IllegalArgumentException(
					"You must declare two valid input streams");
		return new FastaQualitySequenceWriter(out, out2[0]);
	}

}
