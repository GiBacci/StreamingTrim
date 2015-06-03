package inputmonitor;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A monitored input stream. Each time a read method is invoked within this
 * class the method {@link ProgressMonitor#notifyProgress(int)} will be called
 * passing as parameter the number of byte read or skipped.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class MonitoredInputStream extends FilterInputStream implements
		ProgressMonitor {

	/**
	 * List of displays associated to this progress monitor
	 */
	private List<ProgressDisplay> displays = new ArrayList<ProgressDisplay>();

	/**
	 * Creates a monitored input stream
	 * 
	 * @param in
	 *            the input stream
	 * @param size
	 *            the size of the stream
	 */
	public MonitoredInputStream(InputStream in) {
		super(in);
	}

	@Override
	public void addProgressDisplay(ProgressDisplay display) {
		int index = displays.indexOf(display);
		if (index < 0) {
			displays.add(display);
		}
	}

	@Override
	public void removeProgressDisplay(ProgressDisplay display) {
		int index = displays.indexOf(display);
		if (index > 0) {
			displays.remove(display);
		}
	}

	@Override
	public void notifyProgress(int progress) {
		for (int i = 0; i < displays.size(); i++) {
			displays.get(i).getProgress(progress);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#read()
	 */
	@Override
	public int read() throws IOException {
		int read = super.read();
		notifyProgress(read);
		return read;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int read = super.read(b, off, len);
		notifyProgress(read);
		return read;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException {
		int read = super.read(b);
		notifyProgress(read);
		return read;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException {
		long skipped = super.skip(n);
		if (skipped > Integer.MAX_VALUE) {
			skipped = Integer.MAX_VALUE;
		}
		notifyProgress((int) skipped);
		return skipped;
	}

}
