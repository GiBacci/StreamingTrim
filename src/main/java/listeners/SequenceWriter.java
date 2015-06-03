package listeners;

import io.FileSequenceWriter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sequence listener that writes output sequences in a file
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class SequenceWriter implements SequenceListener {

	/**
	 * The sequence writer
	 */
	private FileSequenceWriter writer = null;

	/**
	 * Empty sequence (or <code>null</code> sequences) behavior
	 */
	private EmptySequenceBehaviour behavior = null;

	/**
	 * Logger for this class
	 */
	private Logger logger = null;

	/**
	 * Creates a sequence writer with the specified behavior for empty sequences
	 * 
	 * @param writer
	 *            the file sequence writer
	 * @param behavior
	 *            the behavior for empty sequences
	 */
	public SequenceWriter(FileSequenceWriter writer,
			EmptySequenceBehaviour behavior) {
		this.writer = writer;
		this.behavior = behavior;
	}

	/**
	 * Creates a sequence writer with the specified behavior for empty sequences
	 * and the specified logger for collecting instances of {@link IOException}
	 * 
	 * @param writer
	 *            the file sequence writer
	 * @param behavior
	 *            the behavior for empty sequences
	 * @param logger
	 *            the logger
	 */
	public SequenceWriter(FileSequenceWriter writer,
			EmptySequenceBehaviour behavior, Logger logger) {
		this.writer = writer;
		this.behavior = behavior;
		this.logger = logger;
	}

	@Override
	public void sequence(String id, String sequence, String quality) {

		if ((sequence == null) || (sequence.length() == 0)) {
			if (behavior == EmptySequenceBehaviour.WRITE) {
				sequence = "";
				quality = "";
			} else if (behavior == EmptySequenceBehaviour.SKIP) {
				return;
			}
		}

		try {
			writer.writeSequence(id, sequence, quality);
		} catch (IOException e) {
			if (logger != null) {
				logger.log(Level.SEVERE, e.getMessage());
			} else {
				System.err.println("I/O error occurs writing output file.");
			}
		}
	}

	/**
	 * Empty sequence behavior
	 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
	 *
	 */
	public enum EmptySequenceBehaviour {
		/**
		 * Write an empty sequence
		 */
		WRITE, 
		
		/**
		 * Skip the sequence
		 */
		SKIP;
	}

}
