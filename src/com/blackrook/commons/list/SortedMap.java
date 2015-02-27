/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.list;

import java.util.Iterator;

import com.blackrook.commons.ObjectPair;
import com.blackrook.commons.AbstractVector;

/**
 * Maintains an expandable list of paired objects, but is always sorted.
 * @author Matthew Tropiano
 * @param <T> any Object.
 */
public class SortedMap<T extends Comparable<T>, U extends Object> extends AbstractVector<ObjectPair<T, U>>
{
	/**
	 * Constructs a new SortedMap with capacity DEFAULT_CAPACITY.
	 */
	public SortedMap()
	{
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructs a new SortedMap with defined capacity.
	 * Capacity incrementor = capacity.
	 * @param capacity	the initial capacity of the vector.
	 */
	public SortedMap(int capacity)
	{
		this(capacity,capacity);
	}
	
	/**
	 * Constructs a new SortedMap with defined capacity and capacity increment.
	 * @param capacity	the initial capacity of the vector.
	 * @param inc		the capacity incrementor.
	 */
	public SortedMap(int capacity, int inc)
	{
		super(capacity, inc);
	}
	
	/**
	 * Adds an object to the list and sorts it insertion-style.
	 */
	public void add(T key, U value)
	{
		if (key == null)
			throw new IllegalArgumentException("Key cannot be null.");
		super.add(new ObjectPair<T,U>(key,value));
		resort(size-1);
	}
	
	/**
	 * Replaces the value of a node in this structure.
	 * If it doesn't exist, it will be added.
	 */
	public void replace(T key, U value)
	{
		int i = getIndexOf(key);
		if (i >= 0)
			(getByIndex(i)).setValue(value);
		else
			add(key,value);
	}
	
	/**
	 * Checks if an object exists in this map via comparison binary-search style.
	 */
	public boolean contains(T key)
	{
		return getIndexOf(key) != -1;
	}
	
	/**
	 * Checks if an object exists in this map via comparison binary-search style.
	 * @return the index of the desired key or -1 if it does not exist.
	 */
	public int getIndexOf(T key)
	{
		int u = size, l = 0;
		int i = (u+l)/2;
		int prev = u;
		
		while (i != prev)
		{
			if ((getByIndex(i)).getKey().equals(key))
				return i;
			int c = (getByIndex(i)).getKey().compareTo(key); 
			
			if (c < 0)
				l = i;
			else if (c == 0)
				return i;
			else
				u = i;
			
			prev = i;
			i = (u+l)/2;
		}
		
		return -1;
	}

	/**
	 * Checks if an object exists in this map via comparison linear-search style.
	 * @return the index of the desired value or -1 if it does not exist.
	 */
	public int getIndexOfValue(U value)
	{
		for (int i = 0; i < storageArray.length; i++)
			if ((getByIndex(i)).getValue().equals(value))
				return i;
		return -1;
	}

	/**
	 * Gets the value at a particular index and returns the value.
	 */
	public U getValueAtIndex(int index)
	{
		ObjectPair<T,U> node = super.getByIndex(index);
		return node != null ? node.getValue() : null;
	}

	/**
	 * Returns the object using this key.
	 */
	public U get(T key)
	{
		int i = getIndexOf(key);
		if (i == -1)
			return null;
		return (getByIndex(i)).getValue();
	}

	/**
	 * Removes an object from this Map.
	 * @return the removed object or null it wasn't in the Map.
	 */
	public U remove(T key)
	{
		int i = getIndexOf(key);
		if (i == -1)
			return null;
		return removeValueAtIndex(i);
	}
	
	/**
	 * Removes a value from the map.
	 * @return the removed object or null it wasn't in the Map.
	 */
	public boolean removeByValue(U value)
	{
		int i = this.getIndexOfValue(value);
		return i >= 0 ? removeIndex(i) != null : false;
	}
	
	/**
	 * Removes the first object from this Map.
	 * @return the removed object or null the Map is empty.
	 */
	public U removeFirst()
	{
		return removeValueAtIndex(0);
	}
	
	/**
	 * Removes the last object from this Map.
	 * @return the removed object or null the Map is empty.
	 */
	public U removeLast()
	{
		return removeValueAtIndex(size-1);
	}
	

	/**
	 * Removes the key, value pair at a particular index and returns
	 * the value.
	 */
	public U removeValueAtIndex(int index)
	{
		if (index < 0 || index >= size)
			return null;

		return super.removeIndex(index).getValue();
	}

	/**
	 * Returns an iterator of this map's values.
	 */
	public Iterator<U> getValueIterator()
	{
		return new ValueIterator(this);
	}
	
	/**
	 * Resorts this vector (insertion sort) from an index.
	 * @param index	the index.
	 */
	public void resort(int index)
	{
		while (index > 0 && getByIndex(index).getKey().compareTo(getByIndex(index-1).getKey()) < 0)
			swap(index--);
	}

	/** 
	 * Swaps the object at this index with the one before it (used in sort).
	 */
	protected final void swap(int index)
	{
		ObjectPair<T,U> tmp = getByIndex(index);
		setByIndex(index, getByIndex(index-1));
		setByIndex(index-1, tmp);
	}

	public class ValueIterator implements Iterator<U> 
	{
		SortedMap<T,U> mapRef;
		int currIndex;
		
		ValueIterator(SortedMap<T,U> sm)
		{
			mapRef = sm;
			currIndex = -1;
		}
		
		@Override
		public boolean hasNext()
		{
			return currIndex < size-1;
		}

		@Override
		public U next()
		{
			return getByIndex(++currIndex).getValue();
		}

		@Override
		public void remove()
		{
			mapRef.removeIndex(currIndex);
		}

	}
	
	public class NodeIterator implements Iterator<ObjectPair<T,U>>
	{
		int currIndex;
		
		NodeIterator(SortedMap<T,U> sm)
		{
			currIndex = -1;
		}
		
		@Override
		public boolean hasNext()
		{
			return currIndex < size-1;
		}

		@Override
		public ObjectPair<T,U> next()
		{
			return getByIndex(++currIndex);
		}

		@Override
		public void remove()
		{
			removeIndex(currIndex--);
		}

	}
	
}
