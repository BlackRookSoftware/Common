/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.grid;

import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.linkedlist.Queue;

/**
 * A sparse grid map that contains lists of objects.
 * Be advised that the {@link #get(int, int)} method may return null if no objects
 * are queued at that particular spot.
 * @since 2.2.0
 * @author Matthew Tropiano
 */
public class SparseQueueGridMap<T extends Object> extends SparseGridMap<Queue<T>>
{
	/** Holds the true size of this grid map. */
	private int trueSize;
	
	/**
	 * Creates a new sparse queue grid of an unspecified width and height.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public SparseQueueGridMap()
	{
		this(SIZE_UNSPECIFIED, SIZE_UNSPECIFIED);
		trueSize = 0;
	}
	
	/**
	 * Creates a new sparse queue grid of a specified width and height.
	 * @param width the width of the grid in data values.
	 * @param height the height of the grid in data values.
	 */
	public SparseQueueGridMap(int width, int height)
	{
		super(width, height);
	}
	
	/**
	 * Enqueues an object at a particular grid coordinate.
	 * @param x the x-coordinate.
	 * @param y the y-coordinate.
	 * @param object the object to add.
	 */
	public void enqueue(int x, int y, T object)
	{
		if (object != null)
		{
			getQueue(x, y).enqueue(object);
			trueSize++;
		}
	}
	
	/**
	 * Dequeues an object at a particular grid coordinate.
	 * @param x the x-coordinate.
	 * @param y the y-coordinate.
	 * @return the first object added at the set of coordinates, null if no objects enqueued.
	 */
	public T dequeue(int x, int y)
	{
		T out =  getQueue(x, y).dequeue();
		if (out != null)
			trueSize--;
		return out;
	}
	
	/**
	 * Returns an iterator for a queue at a particular grid coordinate.
	 * @param x the x-coordinate.
	 * @param y the y-coordinate.
	 * @return a resettable iterator for the queue, or null if no queue exists.
	 */
	public ResettableIterator<T> iterator(int x, int y)
	{
		return getQueue(x, y).iterator();
	}
	
	/**
	 * Returns a queue for a set of coordinates. If no queue exists, it is created.
	 * @param x the x-coordinate.
	 * @param y the y-coordinate.
	 */
	protected Queue<T> getQueue(int x, int y)
	{
		Queue<T> out = get(x, y);
		if (out == null)
		{
			out = new Queue<T>();
			set(x, y, out);
		}
		return out;
	}
	
	@Override
	public void set(int x, int y, Queue<T> queue)
	{
		Queue<T> oldQueue = get(x, y);
		if (oldQueue != null)
			trueSize -= oldQueue.size();
		super.set(x, y, queue);
		if (queue != null)
			trueSize += queue.size();
	}
	
	@Override
	public void clear()
	{
		super.clear();
		trueSize = 0;
	}
	
	@Override
	public int size()
	{
		return trueSize;
	}
	
}
