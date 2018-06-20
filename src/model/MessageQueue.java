package model;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Queues messages to be served to the message box graphics painter.
 */
public class MessageQueue extends AbstractQueue<String> {

	private ArrayList<String> container = new ArrayList<String>();

	private static MessageQueue instance = new MessageQueue();

	private MessageQueue() {}

	public static MessageQueue getInstance() {
		return instance;
	}

	/**
	 * Increment two messages, because the screen displays two lines of text
	 */
	public void nextMessage() {
		this.poll();
		this.poll();
	}

	/**
	 * Add an array of messages to the queue
	 * 
	 * @param messages
	 *            - messages to add
	 */
	public void addAllMessages(Object[] messages) {
		for (Object message : messages) {
			this.add(message.toString());
		}
	}

	/**
	 * Check to see if there's a next message
	 * 
	 * @return - true if there's more messages
	 */
	public boolean hasNextMessage() {
		return this.peek() != null;
	}

	/**
	 * Add an element to the queue
	 * 
	 * @param arg0
	 *            - the message to add to the queue
	 */
	@Override
	public boolean offer(String arg0) {
		this.container.add(arg0);
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
			retVal = this.container.get(0);
		}
		return retVal;
	}

	/**
	 * Get the head of the queue (first in first out) and remove it from the queue
	 * 
	 * @return popped head of the queue
	 */
	@Override
	public String poll() {
		String retVal = null;
		if (size() != 0) {
			retVal = this.container.remove(0);
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
		return this.container.iterator();
	}

	/**
	 * Get the size of the current queue.
	 * 
	 * @return int size of queue
	 */
	@Override
	public int size() {
		return this.container.size();
	}

	/**
	 * Gets the next two messages in the queue, if there are any.
	 * 
	 * @return two messages up next
	 */
	public String[] getMessages() {
		String x1 = null, x2 = null;
		if (size() > 0) {
			x1 = this.container.get(0);
		}
		if (size() > 1) {
			x2 = this.container.get(1);
		}
		if (x1 == null) {
			return null;
		}
		x2 = (x2 == null) ? "" : x2;
		return new String[] { x1, x2 };
	}
}
