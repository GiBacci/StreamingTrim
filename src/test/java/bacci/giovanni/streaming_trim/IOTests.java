package bacci.giovanni.streaming_trim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import io.FileSequenceIOFactory;
import io.FileSequenceIOFastaQualityFactory;
import io.FileSequenceIOFastqFactory;
import io.FileSequenceReader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import exception.FileFormatException;
import junit.framework.TestCase;
import listeners.SequenceListener;

// TODO debug FastaQualitySequenceReader
@RunWith(BlockJUnit4ClassRunner.class)
public class IOTests extends TestCase {

	private final static String name = "sample";
	private final static String wrong_name = "wrong-sample";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	public File testFile(String fileName) {
		File res = null;

		try {
			res = new File(this.getClass().getResource("/" + fileName).toURI());
			assertNotNull("Missing test file", res);
		} catch (URISyntaxException e1) {
			fail();
		}
		return res;
	}

	@Test
	public void testIOFastqFactory() {
		String input = name + ".fastq";
		FileSequenceIOFactory fact = null;

		File res = testFile(input);

		try {
			fact = FileSequenceIOFactory.getFactory(new FileInputStream(res));
		} catch (FileNotFoundException e) {
			fail(String.format("%s not found", res.toString()));
		}

		assertEquals(fact.getClass(), FileSequenceIOFastqFactory.class);

		FileSequenceReader reader = fact.createReader();

		SequenceCounter count = new SequenceCounter();
		reader.addListener(count);
		try {
			reader.readAllSequence();
		} catch (IOException e) {
			fail("I/O error occurs during testing");
		} catch (FileFormatException e) {
			fail("FileFormatException in test file");
		}

		assertEquals(count.numSeq, 10);
	}

	@Test
	public void testIOFastaQualityFactory() {
		String input1 = name + ".fasta";
		String input2 = name + ".qual";
		FileSequenceIOFactory fact = null;

		File res1 = testFile(input1);
		File res2 = testFile(input2);

		try {
			fact = FileSequenceIOFactory.getFactory(new FileInputStream(res1),
					new FileInputStream(res2));
		} catch (FileNotFoundException e) {
			fail(String.format("%s or %s not found", res1.toString(),
					res2.toString()));
		}

		assertEquals(fact.getClass(), FileSequenceIOFastaQualityFactory.class);

		FileSequenceReader reader = fact.createReader();

		SequenceCounter count = new SequenceCounter();
		reader.addListener(count);
		try {
			reader.readAllSequence();
		} catch (IOException e) {
			fail("I/O error occurs during testing");
		} catch (FileFormatException e) {
			String error = String.format(
					"FileFormatException in fasta + qual test file: %s",
					e.getMessage());
			System.out.println(error);
			fail(error);
		}

		assertEquals(count.numSeq, 10);
	}

	@Test
	public void testWrongFastqReader() throws FileFormatException {
		String input = wrong_name + ".fastq";
		FileSequenceIOFactory fact = null;

		File res = testFile(input);

		try {
			fact = FileSequenceIOFactory.getFactory(new FileInputStream(res));
		} catch (FileNotFoundException e) {
			fail(String.format("%s not found", res.toString()));			
		}

		FileSequenceReader reader = fact.createReader();
		try {
			thrown.expect(FileFormatException.class);
			reader.readAllSequence();
		} catch (IOException e) {
			fail("I/O error occurs during testing");
		}
	}

	@Test
	public void testWrongFastaQualityReader() throws FileFormatException {
		String input1 = wrong_name + ".fasta";
		String input2 = name + ".qual";
		FileSequenceIOFactory fact = null;

		File res1 = testFile(input1);
		File res2 = testFile(input2);

		try {
			fact = FileSequenceIOFactory.getFactory(new FileInputStream(res1),
					new FileInputStream(res2));
		} catch (FileNotFoundException e) {
			fail(String.format("%s or %s not found", res1.toString(),
					res2.toString()));
		}

		FileSequenceReader reader = fact.createReader();
		try {
			thrown.expect(FileFormatException.class);
			reader.readAllSequence();
		} catch (IOException e) {
			fail("I/O error occurs during testing");
		}
	}

	private class SequenceCounter implements SequenceListener {

		public int numSeq = 0;

		@Override
		public void sequence(String id, String sequence, String quality) {
			numSeq++;
		}

	}

}
