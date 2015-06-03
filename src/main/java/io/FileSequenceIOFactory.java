package io;

import java.io.InputStream;
import java.io.OutputStream;

import decoder.QualityDecoder;

/**
 * Factory for creating IO classes able to deal with fastq or fasta + qual files
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public abstract class FileSequenceIOFactory {

	/**
	 * Protected constructor
	 */
	protected FileSequenceIOFactory() {
	};

	/**
	 * Factory method
	 * 
	 * @param in
	 *            the input file stream
	 * @return a {@link FileSequenceIOFastqFactory}
	 */
	public static FileSequenceIOFactory getFactory(InputStream in) {
		if (in == null)
			throw new NullPointerException("Input stream can't be null");
		return new FileSequenceIOFastqFactory(in);
	}

	/**
	 * Factory method
	 * 
	 * @param in
	 *            the input file stream
	 * @param inq
	 *            the quality input file stream
	 * @return a {@link FileSequenceIOFastaQualityFactory}
	 */
	public static FileSequenceIOFactory getFactory(InputStream in,
			InputStream inq) {
		if (in == null || inq == null)
			throw new NullPointerException("Input streams can't be null");
		return new FileSequenceIOFastaQualityFactory(in, inq);
	}

	/**
	 * Creates an instance of {@link FileSequenceReader}
	 * 
	 * @return a file sequence reader
	 */
	public abstract FileSequenceReader createReader();

	/**
	 * Creates an instance of {@link FileSequenceWriter}
	 * 
	 * @return a file sequence writer
	 */
	public abstract FileSequenceWriter createWriter(OutputStream out,
			OutputStream... out2);

	/**
	 * Created an instance of {@link QualityDecoder}
	 * 
	 * @return a quality decoder specified for the file type
	 */
	public abstract QualityDecoder createQualityDecoder();

}
