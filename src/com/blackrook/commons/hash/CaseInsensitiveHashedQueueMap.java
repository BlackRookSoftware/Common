/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.hash;

import com.blackrook.commons.linkedlist.Queue;

/**
 * A special hashed queue map that maps keys to lists of objects instead of just
 * a single object. The {@link #enqueue(String, Object)} method in this case
 * will append an object to the end of list instead of replacing the contents.
 * The method {@link #size()}, however will only give the amount of lists in the table,
 * not added objects. The method {@link #get(Object)} returns the queue associated with
 * the key, resolved case-insensitively.
 * @author Matthew Tropiano
 */
public class CaseInsensitiveHashedQueueMap<V extends Object> extends CaseInsensitiveHashMap<Queue<V>>
{
	/**
	 * Creates a new hashed queue map with capacity DEFAULT_CAPACITY, rehash ratio DEFAULT_REHASH.
	 */
	public CaseInsensitiveHashedQueueMap()
	{
		this(DEFAULT_CAPACITY, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new hashed queue map with capacity <i>cap</i> and rehash ratio DEFAULT_REHASH. 
	 * @param capacity the initial table capacity. Must be nonzero and non-negative.
	 * @throws IllegalArgumentException if capacity is negative.
	 */
	public CaseInsensitiveHashedQueueMap(int capacity)
	{
		this(capacity, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new hashed queue map.
	 * @param capacity the capacity. cannot be negative.
	 * @param rehashRatio the ratio of capacity/tablesize. if this ratio is exceeded, 
	 * the table's capacity is expanded, and the table is rehashed.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public CaseInsensitiveHashedQueueMap(int capacity, float rehashRatio)
	{
		super(capacity, rehashRatio);
	}

	/**
	 * Enqueues a value in the queue designated by a key.
	 * Adds a new queue if it doesn't exist already.
	 * @param key the key,
	 * @param value the value to add.
	 */
	public void enqueue(String key, V value)
	{
		Queue<V> queue = get(key);
		if (queue == null)
		{
			queue = new Queue<V>();
			put(key, queue);
		}
		queue.enqueue(value);
	}
	
	/**
	 * Dequeues a value in the queue designated by a key.
	 * If the last object corresponding to the key was dequeued, the key is removed. 
	 * If no list is associated with the key, this returns null.
	 * @param key the key.
	 * @return the value dequeued from the corresponding queue, or null if no values.
	 */
	public V dequeue(String key)
	{
		Queue<V> queue = get(key);
		V out = null;
		if (queue != null)
		{
			out = queue.dequeue();
			if (queue.size() == 0)
				removeUsingKey(key);
		}
		return out;
	}
	
	/**
	 * Removes a value from a queue designated by a key.
	 * If the last object corresponding to the key was removed, the key is removed. 
	 * @param key the key.
	 * @param value the value.
	 * @return true if it was removed, false otherwise.
	 */
	public boolean removeValue(String key, V value)
	{
		Queue<V> queue = get(key);
		boolean out = false;
		if (queue != null)
		{
			out = queue.remove(value);
			if (queue.size() == 0)
				removeUsingKey(key);
		}
		return out;
	}
	
}
