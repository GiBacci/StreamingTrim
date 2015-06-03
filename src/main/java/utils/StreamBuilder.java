package utils;

import inputmonitor.MonitoredInputStream;
import inputmonitor.ProgressDisplay;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Utility class for generating input and output streams from files
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class StreamBuilder {
	
	private static final String GZIP_EXT = ".gz";

	/**
	 * Returns an {@link InputStream}
	 * 
	 * @param inputFile
	 *            the path to the input file
	 * @param gzipped
	 *            <code>true</code> if the input file is gzipped
	 * @param display
	 *            if not <code>null</code> a {@link MonitoredInputStream} will
	 *            be created and this display will be added to it
	 * @return an {@link InputStream}
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@SuppressWarnings("resource")
	public static InputStream buildInputStream(String inputFile,
			boolean gzipped, ProgressDisplay display) throws IOException {

		InputStream in = null;
		FileInputStream fis = new FileInputStream(inputFile);
		InputStream dummyInput = null;
		if (display != null) {
			dummyInput = new MonitoredInputStream(fis);
			((MonitoredInputStream) dummyInput).addProgressDisplay(display);
		} else {
			dummyInput = fis;
		}

		if (gzipped) {
			in = new GZIPInputStream(dummyInput);
		} else {
			in = dummyInput;
		}

		return in;
	}

	/**
	 * Returns an output stream
	 * 
	 * @param outputFile
	 *            the path to the output file
	 * @param gzipped
	 *            if <code>true</code> the output will be gzipped
	 * @return an {@link OutputStream}
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static OutputStream buildOutputStream(String outputFile,
			boolean gzipped) throws IOException {
		OutputStream out = null;
		FileOutputStream fos = new FileOutputStream(outputFile);
		if (gzipped) {
			out = new GZIPOutputStream(fos);
		} else {
			out = fos;
		}
		return out;
	}

	/**
	 * Add a prefix to the given file path
	 * 
	 * @param fileName
	 *            the path to the input file
	 * @param prefix
	 *            the prefix
	 * @return a formatted path with the prefix right before the filename
	 */
	public static String formatOutputName(String fileName, String prefix,
			boolean gzipped) {
		String rgx = "^.*\\.[tar\\.]?gz$";

		boolean hasGzip = Pattern.matches(rgx, fileName);

		fileName = (gzipped && !hasGzip) ? String.format("%s%s", fileName,
				GZIP_EXT) : fileName.replaceAll("\\.tar\\.gz$", GZIP_EXT);

		int splitIndex = fileName.lastIndexOf('/');
		String prefixFile = (splitIndex > 0) ? String.format("%s%s%s",
				fileName.substring(0, splitIndex + 1), prefix,
				fileName.substring(splitIndex + 1)) : String.format("%s%s",
				prefix, fileName);
		return prefixFile;
	}

	/**
	 * This method changes the extension of the given file. If the file has no
	 * extension this will be added at the end of it.
	 * 
	 * @param fileName
	 *            the name of the file
	 * @param extension
	 *            the extension to add
	 * @param gzipped
	 *            If the file is gzipped the ".gz" extension will be included
	 * @return the file name with the given extension at the end of it
	 */
	public static String changeExtension(String fileName, String extension,
			boolean gzipped) {
		extension = (extension.startsWith(".")) ? extension : String.format(
				"%s%s", ".", extension);
		String rgxOne = "^(.+?)(?=((\\.tar)?\\.gz)$)";
		String rgxTwo = "^(.+?)(?=\\.(.*)$)";

		Matcher m = Pattern.compile(rgxOne).matcher(fileName);
		boolean ok = (m.find() && gzipped);

		String name = (ok) ? m.group(1) : fileName;
		String gzip = (ok) ? GZIP_EXT : "";

		m = Pattern.compile(rgxTwo).matcher(name);
		ok = m.find();

		name = (ok) ? m.group(1) : name;

		String finalName = String.format("%s%s%s", name, extension, gzip);
		return finalName;

	}
}
