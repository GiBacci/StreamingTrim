package processes;

import io.FileSequenceReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exception.FileFormatException;
import listeners.SequenceGenerator;
import listeners.SequenceListener;
import listeners.TrimmingListener;

/**
 * Trimmer class this class is the main class of the application
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public class SequenceProcess implements Model {

	/**
	 * List of listeners added to the original file
	 */
	private List<SequenceListener> preTrimListeners;

	/**
	 * List of listeners added to the trimmed file
	 */
	private List<SequenceListener> postTrimListeners;

	/**
	 * List of views
	 */
	private List<View> views;

	/**
	 * Token for trimmed file listeners
	 */
	public final static int POST = 1;

	/**
	 * Token for original file listeners
	 */
	public final static int PRE = 0;

	/**
	 * The original sequences
	 */
	private FileSequenceReader mainReader;

	/**
	 * Creates a new Trimmer
	 * 
	 * @param mainReader
	 *            the main sequence file
	 */
	public SequenceProcess(FileSequenceReader mainReader) {
		this.mainReader = mainReader;
		this.preTrimListeners = new ArrayList<SequenceListener>();
		this.postTrimListeners = new ArrayList<SequenceListener>();
		this.views = new ArrayList<View>();
	}

	/**
	 * Adds a sequence listener to this object
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addListener(SequenceListener listener, int preOrPost) {
		List<SequenceListener> toAdd = null;
		SequenceGenerator gen = null;
		if (preOrPost == POST) {
			toAdd = postTrimListeners;
			gen = (TrimmingListener) getListener(TrimmingListener.class, PRE);
		}
		if (preOrPost == PRE) {
			toAdd = preTrimListeners;
			gen = mainReader;
		}
		if ((toAdd == null) || (gen == null))
			return;
		if (!hasListener(listener.getClass(), preOrPost)) {
			gen.addListener(listener);
			toAdd.add(listener);
		}
	}

	/**
	 * Remove a sequence listener from this object if present
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeListener(Class<? extends SequenceListener> listenerClass,
			int preOrPost) {
		List<SequenceListener> toRemove = null;
		SequenceGenerator gen = null;
		if (preOrPost == POST) {
			toRemove = postTrimListeners;
			gen = (TrimmingListener) getListener(TrimmingListener.class, PRE);
		}
		if (preOrPost == PRE) {
			toRemove = preTrimListeners;
			gen = mainReader;
		}
		if ((toRemove == null) || (gen == null))
			return;
		// Checking if this class has already a listener of the same type
		for (SequenceListener l : toRemove) {
			if (l.getClass().equals(listenerClass)) {
				toRemove.remove(l);
				gen.removeListener(l);
				return;
			}
		}
	}

	/**
	 * Return a listener based on its class. If no listener of the specified
	 * class has been found this method will return <code>null</code>
	 * 
	 * @param listenerClass
	 *            the class name of the listener
	 * @param preOrPost
	 *            one of {@link SequenceProcess#POST} or
	 *            {@link SequenceProcess#PRE}
	 * @return the listener or <code>null</code> if there is not any listener
	 *         with the class name specified
	 */
	public SequenceListener getListener(
			Class<? extends SequenceListener> listenerClass, int preOrPost) {
		List<SequenceListener> toGet = null;
		if (preOrPost == POST)
			toGet = postTrimListeners;
		if (preOrPost == PRE)
			toGet = preTrimListeners;
		if (toGet == null)
			return null;
		for (SequenceListener l : toGet) {
			if (l.getClass().equals(listenerClass))
				return l;
		}
		return null;
	}

	/**
	 * @param listenerClas
	 *            the listener class
	 * @param preOrPost
	 *            listener of pre or post processed file
	 * @return <code>true</code> if this object has a listener of the specified
	 *         class
	 */
	public boolean hasListener(Class<? extends SequenceListener> listenerClas,
			int preOrPost) {
		return getListener(listenerClas, preOrPost) != null;
	}

	/**
	 * @param preOrPost
	 *            set of Class added to pre or post trimming
	 * @return a set of class added to this process
	 */
	public Set<Class<? extends SequenceListener>> getListeners(int preOrPost) {
		List<SequenceListener> toGet = null;
		if (preOrPost == PRE)
			toGet = preTrimListeners;
		if (preOrPost == POST)
			toGet = postTrimListeners;
		Set<Class<? extends SequenceListener>> set = null;
		if (toGet != null) {
			set = new HashSet<Class<? extends SequenceListener>>();
			for (SequenceListener s : toGet)
				set.add(s.getClass());
		}
		return set;
	}

	/**
	 * Starts all processes and notifies each view added to this model
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws FileFormatException
	 *             if the input file has a wrong format
	 */
	public void startProcess() throws IOException, FileFormatException {
		mainReader.readAllSequence();
		updateViews();
	}

	@Override
	public void addView(View view) {
		if (views.indexOf(view) < 0)
			views.add(view);
	}

	@Override
	public void removeView(View view) {
		if (views.indexOf(view) >= 0)
			views.remove(view);
	}

	@Override
	public void updateViews() {
		for (View v : views)
			v.update();
	}

}
