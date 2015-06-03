package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import listeners.SequenceListener;
import exception.FileFormatException;

/**
 * {@link FileSequenceReader} implementation for reading fasta + quality files.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class FastaQualitySequenceReader implements FileSequenceReader {

	/**
	 * The list of listeners
	 */
	private ArrayList<SequenceListener> listeners;

	/**
	 * FASTA sequence reader
	 */
	private FastaLikeReader seqReader;

	/**
	 * Quality sequence reader
	 */
	private FastaLikeReader qualReader;

	/**
	 * Regular expression matching FASTA IDs
	 */
	private static final String FASTA_ID = "^>.+$";

	/**
	 * Position of the id in the sequence and quality array
	 */
	private static final int ID = 0;

	/**
	 * Position of the sequence in the sequence and quality array
	 */
	private static final int SEQ = 1;

	/**
	 * Constructor
	 * 
	 * @param seq
	 *            the sequence file (FASTA format)
	 * @param qual
	 *            the quality file (QUAL format)
	 */
	protected FastaQualitySequenceReader(InputStream seq, InputStream qual) {
		this.seqReader = new FastaSequenceReader(seq);
		this.qualReader = new FastaQualityReader(qual);
		this.listeners = new ArrayList<SequenceListener>();
	}

	@Override
	public void readAllSequence() throws IOException, FileFormatException {
		String[] s;
		String[] q;
		while ((s = seqReader.read()) != null) {
			q = qualReader.read();
			if (q == null)
				throw new FileFormatException("Quality file is too short");
			if (!s[ID].equals(q[ID])) {
				String error = String.format(
						"Sequence id and quality id are not matched: %s - %s",
						s[ID], q[ID]);
				throw new FileFormatException(error);
			}
			if (s[SEQ].length() != getQualityLength(q[SEQ])) {
				String error = String
						.format("Sequence length and quality length are different: %d - %d",
								s[SEQ].length(), getQualityLength(q[SEQ]));
				throw new FileFormatException(error);
			}
			notifyListeners(s[ID], s[SEQ], q[SEQ]);
		}
		if (qualReader.read() != null)
			throw new FileFormatException("Sequence file is too short");
	}

	/**
	 * Returns the number of quality values in a quality string
	 * 
	 * @param quality
	 *            the quality string
	 * @return the number of quality values
	 */
	private int getQualityLength(String quality) {
		Pattern pattern = Pattern.compile("\\b(\\d+)\\b");
		Matcher m = pattern.matcher(quality);
		int count = 0;
		while (m.find())
			count++;
		return count;
	}

	@Override
	public void addListener(SequenceListener listener) {
		int index = listeners.indexOf(listener);
		if (index < 0) {
			listeners.add(listener);
		}

	}

	@Override
	public void removeListener(SequenceListener listener) {
		int index = listeners.indexOf(listener);
		if (index >= 0) {
			listeners.remove(index);
		}
	}

	@Override
	public void notifyListeners(String id, String sequence, String quality) {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).sequence(id, sequence, quality);
		}
	}

	@Override
	public void close() throws IOException {
		this.seqReader.reader.close();
		this.qualReader.reader.close();
	}

	/**
	 * Implementation of a reader for FASTA like files
	 * 
	 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
	 *         Bacci</a>
	 *
	 */
	private abstract class FastaLikeReader {

		/**
		 * The main reader
		 */
		private BufferedReader reader;
		
		/**
		 * if <code>true</code> this class will read from the line buffer
		 */
		private boolean buffer = false;

		/**
		 * A line buffer
		 */
		private String bufferLine = null;

		/**
		 * Number of lines read from the buffer
		 */
		private long numLine = 0;

		/**
		 * Constructor
		 * 
		 * @param reader
		 *            an input Stream
		 */
		private FastaLikeReader(InputStream reader) {
			this.reader = new BufferedReader(new InputStreamReader(reader));
		}

		/**
		 * Method for reading FASTA like sequences. This method returns an array
		 * of string with length 2. At index 0 is reported the id of the
		 * sequence whereas at index 1 is reported the sequence itself
		 * 
		 * @return an array of string
		 * @throws IOException
		 *             if an I/O error occurs
		 * @throws FileFormatException
		 *             if the file is not in the correct format
		 */
		private String[] read() throws IOException, FileFormatException {
			String line;
			String id = null;
			StringBuffer s = new StringBuffer();
			while ((line = readLine(buffer)) != null) {
				if (line.isEmpty())
					// if line is empty continue
					continue;
				if (Pattern.matches(FASTA_ID, line) && id != null) {
					// if line is an id but the id is not null break and read
					// from buffer
					buffer = true;
					break;
				}
				if (Pattern.matches(FASTA_ID, line) && id == null) {
					// if line is an id and the id is null store the id and
					// continue without reading from buffer
					buffer = false;
					id = line;
					continue;
				}
				if (Pattern.matches(getRegex(), line)) {
					// if line is a sequence append it to the sequence buffer
					s.append(format(line));
				} else {
					// if the line doesn't match any specified regular
					// expression a FileFormatException will be thrown
					throw new FileFormatException(formatError());
				}
			}
			// If everithing is null return null
			if (id == null && s.length() == 0)
				return null;
			return new String[] { id, s.toString() };
		}
		
		private String readLine(boolean buffer) throws IOException {
			if (buffer)
				return bufferLine;
			bufferLine = reader.readLine();
			numLine++;
			return bufferLine;
		}

		/**
		 * Sequence regex. It is different for each type of sequence (quality
		 * and DNA sequence in this case)
		 * 
		 * @return the regular expression for this sequence format
		 */
		protected abstract String getRegex();

		/**
		 * Format a sequence string based on the FASTA file format
		 * 
		 * @param input
		 *            the input sequence
		 * @return a string formatted following the FASTA standard
		 */
		protected abstract String format(String input);

		/**
		 * @return an error message for {@link FileFormatException}
		 */
		protected abstract String formatError();

	}


	/**
	 * {@link FastaLikeReader} implementation for reading in fasta sequence
	 * files
	 * 
	 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
	 *         Bacci</a>
	 *
	 */
	private class FastaSequenceReader extends FastaLikeReader {

		/**
		 * Regular expression for sequence data
		 */
		private static final String SEQ_RGX = "^[a-zA-Z]+$";

		/**
		 * Constructor
		 * 
		 * @param reader
		 *            the main reader
		 */
		private FastaSequenceReader(InputStream reader) {
			super(reader);
		}

		@Override
		protected String getRegex() {
			return SEQ_RGX;
		}

		@Override
		protected String format(String input) {
			return input.trim();
		}

		@Override
		protected String formatError() {
			return String.format("Wrong sequence format at line: %d",
					super.numLine);
		}

	}

	/**
	 * {@link FastaLikeReader} implementation for reading in qual sequence files
	 * 
	 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
	 *         Bacci</a>
	 *
	 */
	private class FastaQualityReader extends FastaLikeReader {

		/**
		 * Regular expression for quality data
		 */
		private static final String SEQ_RGX = "^[0-9\\s]+$";

		/**
		 * Constructor
		 * 
		 * @param reader
		 *            the main reader
		 */
		private FastaQualityReader(InputStream reader) {
			super(reader);
		}

		@Override
		protected String getRegex() {
			return SEQ_RGX;
		}

		@Override
		protected String format(String input) {
			return String.format("%s ", input.trim());
		}

		@Override
		protected String formatError() {
			return String.format("Wrong quality format at line: %d",
					super.numLine);
		}

	}

}
