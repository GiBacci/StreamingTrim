package io;

import java.io.Closeable;
import java.io.IOException;

import listeners.SequenceGenerator;
import exception.FileFormatException;

/**
 * File sequence reader interface.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public interface FileSequenceReader extends SequenceGenerator, Closeable {

	/**
	 * Reads all sequences in a file
	 * 
	 * @param listener
	 *            sequence listener
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void readAllSequence() throws IOException, FileFormatException;

}
