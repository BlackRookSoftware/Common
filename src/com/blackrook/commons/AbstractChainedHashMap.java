/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import com.blackrook.commons.list.List;

/**
 * This is an abstract hash map data structure. It stores several hashable objects
 * in a chained hash table that are paired with values.
 * @author Matthew Tropiano
 */
public abstract class AbstractChainedHashMap<K extends Object, V extends Object> 
	extends AbstractChainedHash<ObjectPair<K,V>> implements AbstractMap<K, V>
{
	/**
	 * Creates a new chained hash map with capacity DEFAULT_CAPACITY, rehash ratio DEFAULT_REHASH.
	 */
	public AbstractChainedHashMap()
	{
		this(DEFAULT_CAPACITY, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new chained hash map with capacity <i>cap</i> and rehash ratio DEFAULT_REHASH. 
	 * @param capacity the initial table capacity. Must be nonzero and non-negative.
	 */
	public AbstractChainedHashMap(int capacity)
	{
		this(capacity, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new chained hash map.
	 * @param capacity the capacity. cannot be negative.
	 * @param rehashRatio the ratio of capacity/tablesize. if this ratio is exceeded, 
	 * the table's capacity is expanded, and the table is rehashed.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public AbstractChainedHashMap(int capacity, float rehashRatio)
	{
		super(capacity, rehashRatio);
	}

	@Override
	public boolean equalityMethod(ObjectPair<K,V> object1, ObjectPair<K,V> object2)
	{
		return equalityMethodForKey(object1.getKey(), object2.getKey());
	}

	@Override
	protected int getHashcodeFor(ObjectPair<K,V> object)
	{
		return getHashcodeForKey(object.getKey());
	}

	/**
	 * Returns the hashcode for a map key.
	 */
	protected int getHashcodeForKey(K key)
	{
		return key.hashCode();
	}

	/**
	 * Checks if two keys are equal.
	 * @param key1 the first key.
	 * @param key2 the second key.
	 * @return true if the keys are considered equal, false otherwise.
	 */
	public boolean equalityMethodForKey(K key1, K key2)
	{
		if (key1 == null && key2 != null)
			return false;
		else if (key1 != null && key2 == null)
			return false;
		else if (key1 == null && key2 == null)
			return true;
		return key1.equals(key2);
	}
	
	/**
	 * Gets the object pair attached that uses the provided key.
	 * @param key the desired key.
	 * @return the value associated with the key or 
	 * null if no value associated with the provided key.
	 */
	protected ObjectPair<K,V> getPairUsingKey(K key)
	{
		List<ObjectPair<K,V>> vect = getByIndex(getTableIndexForKey(key));
		for (int i = 0; i < vect.size(); i++)
		{
			ObjectPair<K,V> entry = vect.getByIndex(i);
			if (equalityMethodForKey(entry.getKey(), key))
				return entry;
		}
		return null;
	}

	/**
	 * Returns the index at which the pair will be added
	 * to the hash table.
	 */
	protected int getTableIndexForKey(K key)
	{
		return Math.abs(getHashcodeForKey(key)) % storageArray.length;
	}

	/**
	 * Adds/replaces a [key, value] pair to this map.
	 * @param key the key associated with a value.
	 * @param value the value associated with the provided key.
	 */
	public void put(K key, V value)
	{
		ObjectPair<K,V> pair = getPairUsingKey(key);
		if (pair != null)
			pair.setValue(value);
		else
			super.put(new ObjectPair<K,V>(key, value));
	}

	@Override
	public V get(K key)
	{
		ObjectPair<K,V> pair = getPairUsingKey(key);
		return pair != null ? pair.getValue() : null;
	}
	
	/**
	 * Removes a value from this hash using a key.
	 * @param key the key to use.
	 * @return the value removed or null if no value is associated with that key.
	 */
	public V removeUsingKey(K key)
	{
		List<ObjectPair<K,V>> vect = getByIndex(getTableIndexForKey(key));
		for (int i = 0; i < vect.size(); i++)
		{
			ObjectPair<K,V> entry = vect.getByIndex(i);
			if (equalityMethodForKey(entry.getKey(), key))
			{
				vect.removeIndex(i);
				size--;
				return entry.getValue();
			}
		}
		
		return null;
	}

	/**
	 * Checks if a key (by equality) is present in the hash.
	 * @param key the key to use for checking presence.
	 * @return true if it is in the hash, false otherwise.
	 */
	public boolean containsKey(K key)
	{
		return getPairUsingKey(key) != null;
	}

	/**
	 * Returns an iterator that iterates through each key
	 * in the hash.
	 */
	public ResettableIterator<K> keyIterator()
	{
		return new KeyIterator();
	}
	
	/**
	 * Returns an iterator that iterates through each key
	 * in the hash.
	 */
	public ResettableIterator<V> valueIterator()
	{
		return new ValueIterator();
	}
	
	/**
	 * Iterator for the Key objects in the hash.
	 */
	protected class KeyIterator implements ResettableIterator<K>
	{
		private ResettableIterator<ObjectPair<K, V>> innerIterator;

		public KeyIterator()
		{
			innerIterator = iterator();
		}
		
		@Override
		public boolean hasNext()
		{
			return innerIterator.hasNext();
		}

		@Override
		public K next()
		{
			return innerIterator.next().getKey();
		}

		@Override
		public void remove()
		{
			innerIterator.remove();
		}

		@Override
		public void reset()
		{
			innerIterator.reset();
		}
	}
	
	/**
	 * Iterator for the Key objects in the hash.
	 */
	protected class ValueIterator implements ResettableIterator<V>
	{
		private ResettableIterator<ObjectPair<K, V>> innerIterator;

		public ValueIterator()
		{
			innerIterator = iterator();
		}
		
		@Override
		public boolean hasNext()
		{
			return innerIterator.hasNext();
		}

		@Override
		public V next()
		{
			return innerIterator.next().getValue();
		}

		@Override
		public void remove()
		{
			innerIterator.remove();
		}

		@Override
		public void reset()
		{
			innerIterator.reset();
		}
	}
	
}

