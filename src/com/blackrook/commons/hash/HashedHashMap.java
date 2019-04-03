/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.hash;

/**
 * A special hashed queue map that maps keys to hashed sets of objects instead of just
 * a single object. The {@link #add(Object, Object)} method in this case
 * will add an object to the hash instead of replacing the contents.
 * The method {@link #size()}, however will only give the amount of hashes in the table,
 * not added objects. The method {@link #get(Object)} returns the hash associated with
 * the key.
 * @author Matthew Tropiano
 * @since 2.9.0
 */
public class HashedHashMap<K extends Object, V extends Object> extends HashMap<K, Hash<V>>
{
	/** Number of elements in the table. */
	protected int hashCapacity;
	/** Rehashing ratio for rehashing. */
	protected float hashRehashRatio;

	/**
	 * Creates a new hashed queue map with capacity DEFAULT_CAPACITY, rehash ratio DEFAULT_REHASH.
	 * The created hashes have capacity DEFAULT_CAPACITY, rehash ratio DEFAULT_REHASH.
	 */
	public HashedHashMap()
	{
		this(DEFAULT_CAPACITY, DEFAULT_REHASH, DEFAULT_CAPACITY, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new hashed hash map with capacity <i>cap</i> and rehash ratio DEFAULT_REHASH. 
	 * The created hashes have capacity DEFAULT_CAPACITY, rehash ratio DEFAULT_REHASH.
	 * @param capacity the initial table capacity. Must be nonzero and non-negative.
	 * @throws IllegalArgumentException if capacity is negative.
	 */
	public HashedHashMap(int capacity)
	{
		this(capacity, DEFAULT_REHASH, DEFAULT_CAPACITY, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new hashed hash map.
	 * The created hashes have capacity DEFAULT_CAPACITY, rehash ratio DEFAULT_REHASH.
	 * @param capacity the capacity. cannot be negative.
	 * @param rehashRatio the ratio of capacity/tablesize. if this ratio is exceeded, 
	 * the table's capacity is expanded, and the table is rehashed.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public HashedHashMap(int capacity, float rehashRatio)
	{
		this(capacity, rehashRatio, DEFAULT_CAPACITY, DEFAULT_REHASH);
	}

	/**
	 * Creates a new hashed hash map.
	 * The created hashes have rehash ratio DEFAULT_REHASH.
	 * @param capacity the capacity. cannot be negative.
	 * @param rehashRatio the ratio of capacity/tablesize. if this ratio is exceeded, 
	 * the table's capacity is expanded, and the table is rehashed.
	 * @param hashCapacity initial capacity of the new hashes in this map.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public HashedHashMap(int capacity, float rehashRatio, int hashCapacity)
	{
		this(capacity, rehashRatio, hashCapacity, DEFAULT_REHASH);
	}

	/**
	 * Creates a new hashed hash map, defining characteristics for the created hashes.
	 * @param capacity the capacity. cannot be negative.
	 * @param rehashRatio the ratio of capacity/tablesize. if this ratio is exceeded, 
	 * the table's capacity is expanded, and the table is rehashed.
	 * @param hashCapacity initial capacity of the new hashes in this map.
	 * @param hashRehashRatio the ratio of capacity/tablesize of the new hashes in this map. if this ratio is exceeded, 
	 * the table's capacity is expanded, and the table is rehashed.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public HashedHashMap(int capacity, float rehashRatio, int hashCapacity, float hashRehashRatio)
	{
		super(capacity, rehashRatio);
		this.hashCapacity = hashCapacity;
		this.hashRehashRatio = hashRehashRatio;
	}

	/**
	 * Adds a value in the hash designated by a key.
	 * Adds a new hash if it doesn't exist already.
	 * @param key the key,
	 * @param value the value to add.
	 */
	public void add(K key, V value)
	{
		Hash<V> hash = get(key);
		if (hash == null)
		{
			hash = new Hash<V>(hashCapacity, hashRehashRatio);
			put(key, hash);
		}
		hash.put(value);
	}
	
	/**
	 * Removes a value from a queue designated by a key.
	 * If the last object corresponding to the key was removed, the key is removed. 
	 * @param key the key.
	 * @param value the value.
	 * @return true if it was removed, false otherwise.
	 */
	public boolean removeValue(K key, V value)
	{
		Hash<V> hash = get(key);
		boolean out = false;
		if (hash != null)
		{
			out = hash.remove(value);
			if (hash.size() == 0)
				removeUsingKey(key);
		}
		return out;
	}

	/**
	 * Checks if a value exists for a corresponding key.
	 * @param key the key.
	 * @param value the value.
	 * @return true if it exists, false otherwise.
	 * @since 2.21.0
	 */
	public boolean containsValue(K key, V value)
	{
		Hash<V> hash = get(key);
		boolean out = false;
		if (hash != null)
			return hash.contains(value);
		return out;
	}
	
}
