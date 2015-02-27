/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
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
