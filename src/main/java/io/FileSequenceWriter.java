package io;

import java.io.Closeable;
import java.io.IOException;

/**
 * File sequence writer interface.
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public interface FileSequenceWriter extends Closeable {
	/**
	 * Write a sequence in a file following the file format specifications.
	 * @param id the id of the sequence
	 * @param sequence the sequence
	 * @param quality the quality
	 * @throws IOException if an I/O error occurs
	 */
	public void writeSequence(String id, String sequence, String quality) throws IOException;
}
