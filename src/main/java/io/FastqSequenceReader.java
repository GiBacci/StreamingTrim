package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import listeners.SequenceListener;
import exception.FileFormatException;

/**
 * {@link FileSequenceReader} implementation for reading fasq files.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class FastqSequenceReader implements FileSequenceReader {

	/**
	 * The reader
	 */
	private BufferedReader reader = null;

	/**
	 * Number of read lines
	 */
	private long lineRead = 0;

	/**
	 * Last sequence starts here
	 */
	private long seqLine = 0;

	/**
	 * The list of listeners
	 */
	private ArrayList<SequenceListener> listeners = null;

	/**
	 * Regular expression for FASTQ ids
	 */
	private static final String ID_RGX = "^@.+$";

	/**
	 * Regular expression for FASTQ quality ids
	 */
	private static final String Q_ID = "^\\+(?:.+)?$";

	/**
	 * Regular expression for FASTQ sequences
	 */
	private static final String SEQ_RGX = "^[a-zA-Z]+$";

	/**
	 * Creates the sequence reader
	 * 
	 * @param in
	 *            input sequence file
	 * 
	 */
	public FastqSequenceReader(InputStream in) {
		this.reader = new BufferedReader(new InputStreamReader(in));
		this.listeners = new ArrayList<SequenceListener>();
	}

	/**
	 * @see FileSequenceReader#readAllSequence(SequenceListener)
	 * @throws IOException
	 *             if an I/O error occurs reading from the file
	 * @throws FileFormatException
	 *             if the fastq file is not formatted correctly
	 */
	@Override
	public void readAllSequence() throws IOException, FileFormatException {
		String id = null;
		String qual = null;
		StringBuffer seq = new StringBuffer();

		String line = null;
		while ((line = reader.readLine()) != null) {
			lineRead++;
			if (line.isEmpty())
				continue;
			if (Pattern.matches(ID_RGX, line) && (id == null)) {
				id = line;
				seqLine = lineRead;
				continue;
			}
			if (Pattern.matches(SEQ_RGX, line) && (id != null)) {
				seq.append(line);
				continue;
			}
			if (Pattern.matches(Q_ID, line)) {
				qual = consumeQuality(seq.length());
				if (qual == null)
					throw new FileFormatException(String.format(
							"Wrong formatted sequence at line: %d", seqLine));
				if (qual.length() != seq.length())
					throw new FileFormatException(String.format(
							"Sequence and quality lengths differ at line: %d",
							seqLine));
				notifyListeners(id, seq.toString(), qual);
				id = null;
				seq = new StringBuffer();
				qual = null;
				continue;
			}
			throw new FileFormatException(String.format(
					"Wrong formatted sequence at line: %d", lineRead));
		}
	}

	/**
	 * Consume quality lines until the specified length have been reached
	 * 
	 * @param length
	 *            the length of the quality sequence
	 * @return a fastq qualty string
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private String consumeQuality(int length) throws IOException {
		String line = null;
		StringBuffer qualityBuffer = new StringBuffer(length);
		while ((line = reader.readLine()) != null) {
			lineRead++;
			qualityBuffer.append(line);
			if (qualityBuffer.length() >= length)
				break;
		}
		return qualityBuffer.toString().trim();
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
		this.reader.close();
	}

}
