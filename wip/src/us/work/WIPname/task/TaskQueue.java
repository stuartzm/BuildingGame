package us.rescyou.meme.task;

import java.util.LinkedList;
import java.util.Queue;

import org.newdawn.slick.SlickException;

public class TaskQueue {

	/* Private Variables */

	private Queue<Task> queue;

	/* Initialization */

	/**
	 * Creates a new TaskQueue.
	 */
	public TaskQueue() {
		this.queue = new LinkedList<Task>();
	}

	/* Updating */

	/**
	 * Ticks the current Task. Moves to the next Task if the current one is
	 * completed.
	 * 
	 * @param delta
	 *            the amount of time, in seconds, since the last tick.
	 * @throws SlickException
	 */
	public void tick(float delta) throws SlickException {
		Task current = getCurrentTask();

		if (current != null && current.isCompleted()) {
			getQueue().remove();
			current = getCurrentTask();
		}

		if (current != null) {
			current.tick(delta);
		}
	}

	/* Direct Queue Modification */

	/**
	 * Retrieves and removes the head of the TaskQueue.
	 * 
	 * @return the head of the TaskQueue.
	 */
	private Task remove() {
		Queue<Task> queue = getQueue();
		return queue.remove();
	}

	/**
	 * Retrieves, but does not remove, the head of TaskQueue.
	 * 
	 * @return the head of the TaskQueue.
	 */
	private Task peek() {
		Queue<Task> queue = getQueue();
		return queue.peek();
	}

	/**
	 * Adds a Task to the TaskQueue, if space is available.
	 * 
	 * @param task
	 *            the Task to be offered to the TaskQueue.
	 */
	private void offer(Task task) {
		Queue<Task> queue = getQueue();
		queue.offer(task);
	}

	/* Abstracted Queue Modification */

	/**
	 * Adds a Task to the TaskQueue.
	 * 
	 * @param task
	 *            the Task to be added to the TaskQueue.
	 */
	public void addTask(Task task) {
		offer(task);
	}

	/**
	 * Appends a Task to the head of the TaskQueue.
	 * 
	 * @param task
	 *            the Task to be appended to the TaskQueue.
	 */
	public void appendTask(Task task) {
		Queue<Task> queue = getQueue();
		Queue<Task> newQueue = new LinkedList<Task>();
		newQueue.add(task);

		for (int i = 0; i < queue.size(); i++) {
			newQueue.add(queue.remove());
		}

		setQueue(newQueue);
	}

	/**
	 * Retrieves and removes the current Task.
	 */
	public Task removeCurrentTask() {
		return remove();
	}

	/* Getters */

	/**
	 * @return the number of Tasks in the TaskQueue.
	 */
	public int getSize() {
		return getQueue().size();
	}

	/**
	 * @return the head of the TaskQueue.
	 */
	public Task getCurrentTask() {
		return peek();
	}

	/**
	 * @return a Set containing every Task in the TaskQueue.
	 */
	private Queue<Task> getQueue() {
		return queue;
	}

	/* Setters */

	/**
	 * Sets all of the Tasks in the TaskQueue.
	 * 
	 * @param queue
	 *            a Set containing all of the Tasks in the TaskQueue.
	 */
	private void setQueue(Queue<Task> queue) {
		this.queue = queue;
	}

}
