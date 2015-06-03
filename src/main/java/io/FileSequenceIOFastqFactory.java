package io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import listeners.SequenceListener;
import decoder.FastqQualityDecoder;
import decoder.FastqQualityDecoder.QualityEncoding;
import decoder.QualityDecoder;
import exception.FileFormatException;

/**
 * {@link FileSequenceIOFactory} implementation for dealing with fastq files
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class FileSequenceIOFastqFactory extends FileSequenceIOFactory {

	private static final int BYTE_TO_READ = 10000000;
	
	private BufferedInputStream in;

	/**
	 * @see FileSequenceIOFactory#FileSequenceIOFactory(InputStream, Writer)
	 */
	protected FileSequenceIOFastqFactory(InputStream in) {
		this.in = new BufferedInputStream(in);
	}

	@Override
	public FileSequenceReader createReader() {
		return new FastqSequenceReader(in);
	}

	@Override
	public FileSequenceWriter createWriter(OutputStream out, OutputStream... out2) {
		if(out == null)
			throw new NullPointerException("Output stream can't be null");
		return new FastqSequenceWriter(out);
	}

	/**
	 * Creates a quality decoder for fastq file. The type of encoding is
	 * auto-guessed reading the first 10'000'000 bytes from the input stream. If
	 * the auto-guess is not needed it is recommended to initialize a
	 * {@link FastqQualityDecoder} with the specified encoding without launching
	 * this method
	 * 
	 * @see FastqQualityDecoder#FastqQualityDecoder(QualityEncoding)
	 * @return a {@link FastqQualityDecoder} or <code>null</code> if an I/O
	 *         error occurs or if it cannot correctly guess the encoding
	 */
	@Override
	public QualityDecoder createQualityDecoder() {
		if (in == null)
			return null;
		
		// Mark Buffered input stream
		in.mark(BYTE_TO_READ);

		// Initialize new byte buffer
		byte head[] = new byte[BYTE_TO_READ];

		try {
			// Reading bytes into the new buffer
			in.read(head);
			// Reset the buffer
			in.reset();
		} catch (IOException e) {
			System.err.println("I/O error/s occurs reading sequence file.");
			System.exit(-1);
		}

		// Generate an input stream from the buffer and pass it to a sequence
		// reader
		ByteArrayInputStream byteIn = new ByteArrayInputStream(head);
		@SuppressWarnings("resource")
		FastqSequenceReader reader = new FastqSequenceReader(byteIn);

		// Guessing quality
		QualityEncodingGuess guess = new QualityEncodingGuess();
		reader.addListener(guess);

		try {
			reader.readAllSequence();
		} catch (IOException e) {
			System.err.println("I/O error/s occurs reading sequence file.");
			System.exit(-1);
		} catch (FileFormatException e) {
			String error = String.format(
					"Sequence at line %d is not well formatted", e.getLine());
			System.err.println(error);
			System.exit(-1);
		} finally {
			try {
				byteIn.close();
			} catch (IOException e) {
				// Nothing we can do
			}
		}

		// If the guessing class has recognized the encoding it will return a
		// quality decoder otherwise it will return null
		if (guess.encoding != null) {
			return new FastqQualityDecoder(guess.encoding);
		}
		return null;
	}

	/**
	 * Guessing quality class. This guess can be not totally accurate.
	 * 
	 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
	 *         Bacci</a>
	 * 
	 */
	private class QualityEncodingGuess implements SequenceListener {

		private QualityEncoding encoding = null;
		private int offset = -1;
		private int lowerBound = 41;
		private int upperBound = -5;

		@Override
		public void sequence(String id, String sequence, String quality) {

			// Testing B tail typical of Illumina 1.5 encoding
			Pattern btail = Pattern.compile("B+$");
			Matcher m = btail.matcher(quality);

			boolean qCtrl = m.find();

			for (int i = 0; i < quality.length(); i++) {
				int q = (int) quality.charAt(i);

				// Searching for correct offset
				if ((offset != 33) && (offset != 64)) {
					if ((q - 64) < -5) {
						offset = 33;
					} else if ((q - 33) > 41) {
						offset = 64;
					}
				}

				// Searching for lower and upper bounds
				if ((offset == 33) || (offset == 64)) {
					lowerBound = Math.min(q - offset, lowerBound);
					upperBound = Math.max(q - offset, upperBound);
				}
			}

			// Testing Solexa encoding
			if ((lowerBound < 0) && (offset == 64)) {
				this.encoding = QualityEncoding.SOLEXA;
				return;

				// Testing Sanger encoding
			} else if ((lowerBound == 0) && (upperBound == 40)
					&& (offset == 33)) {
				this.encoding = QualityEncoding.SANGER;
				return;

				// Testing Illumina 1.3 encoding
			} else if ((lowerBound == 0) && (upperBound == 40)
					&& (offset == 64)) {
				this.encoding = QualityEncoding.ILLUMINA13;
				return;

				// Testing Illumina 1.5 encoding
			} else if ((lowerBound == 2) && qCtrl && (offset == 64)) {
				this.encoding = QualityEncoding.ILLUMINA15;
				return;

				// Testing Illumina 1.8 encoding
			} else if ((upperBound > 40) && (offset == 33)) {
				this.encoding = QualityEncoding.ILLUMINA18;
				return;

				// Testing Undefined encoding Phred+33
			} else if (offset == 33) {
				this.encoding = QualityEncoding.PHRED33;

				// Testing Undefined encoding Phred+64
			} else if (offset == 64) {
				this.encoding = QualityEncoding.PHRED64;
			}

		}

	}
}
