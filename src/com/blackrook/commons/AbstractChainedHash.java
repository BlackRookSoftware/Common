/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import com.blackrook.commons.list.List;

/**
 * This is an abstract hash data structure. It stores several hashable objects
 * in a chained hash table.
 * @author Matthew Tropiano
 */
public abstract class AbstractChainedHash<P extends Object> extends AbstractArrayStorage<List<P>> implements AbstractSet<P>
{
	/** Default rehash ratio. */
	public static final float DEFAULT_REHASH = 0.75f;

	/** Number of elements in the table. */
	protected int size;
	/** Rehashing ratio for rehashing. */
	protected float rehashRatio;

	/**
	 * Creates a new chained hash with capacity DEFAULT_CAPACITY, rehash ratio DEFAULT_REHASH.
	 */
	public AbstractChainedHash()
	{
		this(DEFAULT_CAPACITY, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new chained hash with capacity <i>cap</i> and rehash ratio DEFAULT_REHASH. 
	 * @param capacity the initial table capacity. Must be nonzero and non-negative.
	 */
	public AbstractChainedHash(int capacity)
	{
		this(capacity, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new chained hash.
	 * @param capacity the capacity. cannot be negative.
	 * @param rehashRatio the ratio of capacity/tablesize. if this ratio is exceeded, the table's capacity is expanded, and the table is rehashed.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public AbstractChainedHash(int capacity, float rehashRatio)
	{
		super(capacity);
		
		if (capacity <= 0)
			throw new IllegalArgumentException("Capacity can't 0 or less.");
		if (rehashRatio <= 0.0f)
			throw new IllegalArgumentException("Ratio can't be 0 or less.");
		
		for (int i = 0; i < storageArray.length; i++)
			storageArray[i] = new List<P>(4);
		this.rehashRatio = rehashRatio;
		size = 0;
	}

    /**
     * Clears the contents of this list.
     */
	public void clear()
	{
		if (isEmpty()) return;
		for (int i = 0; i < storageArray.length; i++)
			getByIndex(i).clear();
		size = 0;
	}
	
	/**
	 * Doubles this table's capacity, and rehashes this table.
	 */
	protected void rehash()
	{
		List<P> itemQueue = new List<P>(size());
		for (int i = 0; i < storageArray.length; i++)
		{
			List<P> vect = getByIndex(i);
			while (!vect.isEmpty())
				itemQueue.add(vect.removeIndex(vect.size()-1)); // remove from end = quick
		}
		
		Object[] newArray = new Object[storageArray.length * 2];
		System.arraycopy(storageArray, 0, newArray, 0, storageArray.length);
		for (int i = storageArray.length; i < newArray.length; i++)
			newArray[i] = new List<P>(4);
		storageArray = newArray;

		size = 0;

		while (!itemQueue.isEmpty())
			put(itemQueue.removeIndex(itemQueue.size()-1));
	}

	/**
	 * @return true if the table needs to be rehashed, false otherwise.
	 */
	protected boolean rehashCheck()
	{
		return ((float)size)/storageArray.length > rehashRatio;
	}
	
	/**
	 * Gets the hashcode for the input object.
	 * @param object the object to create a hash code for.
	 * @return the code returned.
	 */
	protected int getHashcodeFor(P object)
	{
		return object.hashCode();
	}

	/**
	 * Determines if the objects are equal. This can be implemented differently
	 * in case a data structure has a different concept of what is considered equal.
	 * @param object1 the first object.
	 * @param object2 the second object.
	 * @return true if the keys are considered equal, false otherwise.
	 */
	protected boolean equalityMethod(P object1, P object2)
	{
		if (object1 == null && object2 != null)
			return false;
		else if (object1 != null && object2 == null)
			return false;
		else if (object1 == null && object2 == null)
			return true;
		return ((P)object1).equals(object2);
	}
	
	/**
	 * Finds the appropriate slot index for an object.
	 * @param object the object to use. 
	 * @return the index at which an object will be added/searched for in the hash.
	 */
	protected int getTableIndexFor(P object)
	{
		return Math.abs(getHashcodeFor(object)) % storageArray.length;
	}
	
	@Override
	public void put(P object)
	{
		if (contains(object))
			return;
		if (rehashCheck()) rehash();
		getByIndex(getTableIndexFor(object)).add(object);
		size++;
	}
	
	@Override
	public boolean contains(P object)
	{
		List<P> vect = getByIndex(getTableIndexFor(object));
		for (int i = 0; i < vect.size(); i++)
		{
			P entry = vect.getByIndex(i);
			if (equalityMethod(entry, object))
				return true;
		}
		
		return false;
	}
	
	@Override
	public boolean remove(P object)
	{
		List<P> vect = getByIndex(getTableIndexFor(object));
		for (int i = 0; i < vect.size(); i++)
		{
			P entry = vect.getByIndex(i);
			if (equalityMethod(entry, object))
			{
				vect.removeIndex(i);
				size--;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the amount of objects in the hash.
	 */
	public int size()
	{
		return size;
	}

	/**
	 * Returns true if there is nothing in this vector, false otherwise.
	 * Equivalent to <code>size() == 0</code>.
	 */
	public boolean isEmpty()
	{
		return size() == 0;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		boolean once = false;
		sb.append("[");
		for (Object obj : this)
		{
			if (once)
				sb.append(", ");
			sb.append(obj);
			once = true;
		}
		sb.append("]");
		
		return sb.toString();
	}

	@Override
	public ResettableIterator<P> iterator()
	{
		return new ChainedHashIterator();
	}
	
	/**
	 * Iterator object for chained hashes.
	 */
	protected class ChainedHashIterator implements ResettableIterator<P>
	{
		/** Current index. */
		protected int currentHashIndex;
		/** Current chain. */
		protected List<P> currentChain;
		/** Current chain index. */
		protected int currentChainIndex;
		/** Removed flag. */
		protected boolean removeFlag;
		
		public ChainedHashIterator()
		{
			reset();
		}
		
		@Override
		public boolean hasNext()
		{
			boolean b = currentChainIndex >= 0;
			b = b && (currentChainIndex < currentChain.size() && currentChain.getByIndex(currentChainIndex) != null);
			if (!b)
			{
				int ci = getNextIndex(currentHashIndex + 1);
				if (ci >= 0)
				{
					currentHashIndex = ci;
					currentChain = getByIndex(ci);
					currentChainIndex = 0;
					b = true;
				}
			}
			return b;
		}

		@Override
		public P next()
		{
			P out = currentChain.getByIndex(currentChainIndex);
			do {currentChainIndex++;} 
			while (currentChainIndex < currentChain.size() && currentChain.getByIndex(currentChainIndex) == null);
			removeFlag = false;
			return out;
		}

		@Override
		public void remove()
		{
			if (removeFlag)
				throw new IllegalStateException("remove() called before next()");

			currentChain.removeIndex(currentChainIndex-1);
			size--;
			removeFlag = true;
		}

		/**
		 * Recursively checks as much as it needs to to figure out
		 * if hasNext() should return true. Makes use of Java's
		 * boolean expression evaluation behavior.
		 * @param index starting list index to check.
		 * @return an index if there is a "next," -1 otherwise.
		 */
		protected int getNextIndex(int index)
		{
			while (index < storageArray.length && getByIndex(index).isEmpty())
				index++;
			return index >= storageArray.length ? -1 : index; 
		}

		@Override
		public void reset()
		{
			removeFlag = true;
			currentHashIndex = getNextIndex(0);
			if (currentHashIndex != -1)
			{
				currentChain = getByIndex(currentHashIndex);
				currentChainIndex = 0;
			}
			else
			{
				currentChain = null;
				currentChainIndex = -1;
			}
		}
		
}
	
}

