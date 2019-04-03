/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.index;

import com.blackrook.commons.ObjectPair;
import com.blackrook.commons.ResettableIterable;
import com.blackrook.commons.ResettableIterator;
import com.blackrook.commons.Sizable;
import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.math.Pair;
import com.blackrook.commons.util.ThreadUtils;

/**
 * This is a grid that contains a grid of Object data generally used for maps and lookups.
 * This map is <i>sparse</i>, which means it uses as little memory as possible, which can increase the lookup time in most cases.
 * @author Matthew Tropiano
 */
public class SparseGridIndex<T extends Object> implements ResettableIterable<ObjectPair<Pair, T>>, Sizable
{
	/** List of grid codes. */
	protected HashMap<Pair, T> data;
	
	/**
	 * Creates a new sparse grid of an unspecified width and height.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public SparseGridIndex()
	{
		data = new HashMap<Pair, T>();
	}
	
	/**
	 * Clears everything from the grid.
	 */
	public void clear()
	{
		data.clear();
	}

	/**
	 * Sets an object at a particular part of the grid.
	 * @param x	the grid position x to set info.
	 * @param y	the grid position y to set info.
	 * @param object the object to set. Can be null.
	 */
	public void set(int x, int y, T object)
	{
		Cache c = getCache();
		c.tempPair.set(x, y);
		if (object == null)
			data.removeUsingKey(c.tempPair);
		else
			data.put(new Pair(x, y), object);
	}

	/**
	 * Gets the object at a particular part of the grid.
	 * @param x	the grid position x to get info.
	 * @param y	the grid position y to get info.
	 * @return the object at that set of coordinates or null if not object.
	 */
	public T get(int x, int y)
	{
		Cache c = getCache();
		c.tempPair.set(x, y);
		return data.get(c.tempPair);
	}
	
	@Override
	public String toString()
	{
		return data.toString();
	}

	@Override
	public ResettableIterator<ObjectPair<Pair, T>> iterator()
	{
		return data.iterator();
	}

	@Override
	public int size()
	{
		return data.size();
	}

	@Override
	public boolean isEmpty()
	{
		return size() == 0;
	}
	
	private static final String CACHE_NAME = "$$"+Cache.class.getCanonicalName();

	// Get the cache.
	private Cache getCache()
	{
		Cache out;
		if ((out = (Cache)ThreadUtils.getLocal(CACHE_NAME)) == null)
			ThreadUtils.setLocal(CACHE_NAME, out = new Cache());
		return out;
	}

	/** Cache. */
	private static final class Cache
	{
		/** Temporary pair. */
		private Pair tempPair;
		
		private Cache()
		{
			tempPair = new Pair();
		}
	}
	

}
