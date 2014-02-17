/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.hash;

/**
 * A special type of hash that tallies/counts use of objects. 
 * @author Matthew Tropiano
 * @param <T> an Object type.
 * @since 2.7.0
 */
public class CountMap<T extends Object> extends HashMap<T, Integer>
{
	/**
	 * Creates a new count map with capacity DEFAULT_CAPACITY, rehash ratio DEFAULT_REHASH.
	 */
	public CountMap()
	{
		this(DEFAULT_CAPACITY, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new count map with capacity <i>cap</i> and rehash ratio DEFAULT_REHASH. 
	 * @param capacity the initial table capacity. Must be nonzero and non-negative.
	 * @throws IllegalArgumentException if capacity is negative.
	 */
	public CountMap(int capacity)
	{
		this(capacity, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new count map.
	 * @param capacity the capacity. cannot be negative.
	 * @param rehashRatio the ratio of capacity/tablesize. if this ratio is exceeded, 
	 * the table's capacity is expanded, and the table is rehashed.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public CountMap(int capacity, float rehashRatio)
	{
		super(capacity, rehashRatio);
	}

	/**
	 * Returns the count of an object.
	 * If this does not contain the object, then this returns 0.
	 * @return the current count of the object, or 0 if the object was not added.
	 */
	public int getCount(T object)
	{
		if (!containsKey(object))
			return 0;
		return get(object);
	}
	
	/**
	 * Adds one to the count of an object.
	 * If the object has not been counted, it is placed in this map, and is given a count of 1.
	 * @param object the object to add.
	 */
	public void give(T object)
	{
		give(object, 1);
	}
	
	/**
	 * Adds an amount to the count of an object.
	 * @param object the object to add.
	 * @param amount the amount to give/add.
	 */
	public void give(T object, int amount)
	{
		if (amount < 0)
			take(object, -amount);
		else
		{
			int a = getCount(object) + amount;
			if (a > 0)
				put(object, a);
		}
	}
	
	/**
	 * Subtracts one from the count of an object.
	 * If the object has not been counted, this returns 0.
	 * If the count is 0 after this runs, the object instance is removed.
	 * @param object the object to remove.
	 * @return a reference to the object, or null if no object was counted before.
	 */
	public int take(T object)
	{
		return take(object, 1);
	}
	
	/**
	 * Subtracts an amount from the count of an object.
	 * If the count is 0 after this runs, the object instance is removed.
	 * @param object the object to remove.
	 * @return the actual amount taken, doesn't always equal amount.
	 */
	public int take(T object, int amount)
	{
		if (amount < 0)
		{
			give(object, -amount);
			return 0;
		}
		int c = getCount(object);
		int n = Math.max(0, c - amount);
		int out = c - n;
		if (n == 0)
			removeUsingKey(object);
		else
			put(object, n);
		return out;
	}
	
	/**
	 * Removes the whole amount from the count of an object.
	 * The object instance is removed.
	 * @param object the object to remove.
	 * @return the actual amount taken.
	 */
	public int takeAll(T object)
	{
		return take(object, getCount(object));
	}
	
}
