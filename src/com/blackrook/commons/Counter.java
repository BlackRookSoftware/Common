package com.blackrook.commons;

/**
 * A thread-safe incremental counter.
 * Mutually exclusive on writes.
 * @since 2.17.0
 * @author Matthew Tropiano
 */
public class Counter
{
	/** Current value. */
	private int value;

	/**
	 * Creates a new Counter, starting at 0.
	 */
	public Counter()
	{
		this.value = 0;
	}

	/**
	 * Creates a new Counter.
	 * @param value the starting value.
	 */
	public Counter(int value)
	{
		this.value = value;
	}
	
	/**
	 * Increments the value, then returns it.
	 */
	public int incr()
	{
		synchronized (this)
		{
			return ++this.value;
		}
	}
	
	/**
	 * Returns the current value of this counter.
	 */
	public int get()
	{
		return value;
	}
	
}