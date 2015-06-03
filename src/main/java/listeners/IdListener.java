package listeners;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import idparser.IdAttributes;
import idparser.IdParser;
import idparser.IdParserFactory;

public class IdListener implements SequenceListener, Closeable {

	private static final String SEP = ";";

	private BufferedWriter writer;

	private Logger logger = null;

	public IdListener(BufferedWriter writer) {
		this.writer = writer;
	}

	public IdListener(BufferedWriter writer, Logger logger) {
		this(writer);
		this.logger = logger;
	}

	@Override
	public void sequence(String id, String sequence, String quality) {
		IdParser parser = IdParserFactory.createParser(id);
		if (parser != null) {
			String xcoord = parser.getAttribute(IdAttributes.X_COORD);
			String ycoord = parser.getAttribute(IdAttributes.Y_COORD);
			String indexId = parser.getAttribute(IdAttributes.INDEX_ID);
			String pair = parser.getAttribute(IdAttributes.PAIR_MEMBER);
			if (xcoord != null && ycoord != null && indexId != null && pair != null) {
				try {
					writer.write(String.format("%s%s%s%s%s%s%s%n", xcoord, SEP,
							ycoord, SEP, indexId, SEP, pair));
				} catch (IOException e) {
					if (logger != null) {
						logger.log(Level.SEVERE, e.getMessage(), e);
					} else {
						System.err.println("Cannot write x and y coordinates");
					}
				}
			}
		}

	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

}
