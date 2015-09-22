package model;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Queues messages to be served to the message box graphics painter.
 */
public class MessageQueue extends AbstractQueue<String> {

	private ArrayList<String> container = new ArrayList<String>();

	/**
	 * Add an element to the queue
	 * 
	 * @param arg0
	 *            - the message to add to the queue
	 */
	@Override
	public boolean offer(String arg0) {
		container.add(arg0);
		return true;
	}

	/**
	 * Peek at the first element, but do not remove it.
	 * 
	 * @return String first element in queue
	 */
	@Override
	public String peek() {
		String retVal = null;
		if (size() != 0) {
			retVal = container.get(0);
		}
		return retVal;
	}

	/**
	 * Get the head of the queue (first in first out) and remove it from the
	 * queue
	 * 
	 * @return popped head of the queue
	 */
	@Override
	public String poll() {
		String retVal = null;
		if (size() != 0) {
			retVal = container.remove(0);
		}
		return retVal;
	}

	/**
	 * Provide an iterator over the collection.
	 * 
	 * @return iterator<String>
	 */
	@Override
	public Iterator<String> iterator() {
		return container.iterator();
	}

	/**
	 * Get the size of the current queue.
	 * 
	 * @return int size of queue
	 */
	@Override
	public int size() {
		return container.size();
	}

	/**
	 * Gets the next two messages in the queue, if there are any.
	 * 
	 * @return two messages up next
	 */
	public String[] getMessages() {
		String x1 = null, x2 = null;
		if (size() > 0) {
			x1 = container.get(0);
		}
		if (size() > 1) {
			x2 = container.get(1);
		}
		if (x1 == null) {
			return null;
		} else {
			x2 = (x2 == null) ? "" : x2;
		}
		return new String[] { x1, x2 };
	}
}
