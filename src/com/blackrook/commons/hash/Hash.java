/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons.hash;

import com.blackrook.commons.AbstractChainedHash;

/**
 * Hashed data structure. It stores several objects
 * in a chained hash table, used primarily for testing for
 * existence of archive-able, searchable objects.
 * @author Matthew Tropiano
 */
public class Hash<T extends Object> extends AbstractChainedHash<T>
{
	/**
	 * Creates a new hash with capacity DEFAULT_CAPACITY, rehash ratio DEFAULT_REHASH.
	 */
	public Hash()
	{
		this(DEFAULT_CAPACITY, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new hash with capacity <i>cap</i> and rehash ratio DEFAULT_REHASH. 
	 * @param capacity the initial table capacity. Must be nonzero and non-negative.
	 */
	public Hash(int capacity)
	{
		this(capacity, DEFAULT_REHASH);
	}
	
	/**
	 * Creates a new hash.
	 * @param capacity the capacity. cannot be negative.
	 * @param rehashRatio the ratio of capacity/tablesize. if this ratio is exceeded, the table's capacity is expanded, and the table is rehashed.
	 * @throws IllegalArgumentException if capacity is negative or ratio is 0 or less.
	 */
	public Hash(int capacity, float rehashRatio)
	{
		super(capacity, rehashRatio);
	}

	/**
	 * Returns a new Hash that is the union of the objects in two hashes,
	 * i.e. a set with all objects from both sets.
	 * @param <T> the object type in the provided hash.
	 * @param <H> the hash table that contains type T. 
	 * @param set1 the first hash.
	 * @param set2 the second hash.
	 * @return a new hash set.
	 * @since 2.20.0
	 */
	@SuppressWarnings("unchecked")
	public static <T, H extends Hash<T>> H union(H set1, H set2)
	{
		Hash<T> out = new Hash<T>();
		for (T val : set1)
			out.put(val);
		for (T val : set2)
			out.put(val);
		return (H)out;
	}

	/**
	 * Returns a new Hash that is the intersection of the objects in two hashes,
	 * i.e. the objects that are present in both sets.
	 * @param <T> the object type in the provided hash.
	 * @param <H> the hash table that contains type T. 
	 * @param set1 the first hash.
	 * @param set2 the second hash.
	 * @return a new hash set.
	 * @since 2.20.0
	 */
	@SuppressWarnings("unchecked")
	public static <T, H extends Hash<T>> H intersection(H set1, H set2)
	{
		Hash<T> out = new Hash<T>();
		
		H bigset = set1.size() > set2.size() ? set1 : set2;
		H smallset = bigset == set1 ? set2 : set1;
		
		for (T val : smallset)
		{
			if (bigset.contains(val))
				out.put(val);
		}
		return (H)out;
	}

	/**
	 * Returns a new Hash that is the difference of the objects in two hashes,
	 * i.e. the objects in the first set minus the objects in the second.
	 * @param <T> the object type in the provided hash.
	 * @param <H> the hash table that contains type T. 
	 * @param set1 the first hash.
	 * @param set2 the second hash.
	 * @return a new hash set.
	 * @since 2.20.0
	 */
	@SuppressWarnings("unchecked")
	public static <T, H extends Hash<T>> H difference(H set1, H set2)
	{
		Hash<T> out = new Hash<T>();
		for (T val : set1)
		{
			if (!set2.contains(val))
				out.put(val);
		}
		return (H)out;
	}

	/**
	 * Returns a new Hash that is the union minus the intersection of the objects in two hashes.
	 * @param <T> the object type in the provided hash.
	 * @param <H> the hash table that contains type T. 
	 * @param set1 the first hash.
	 * @param set2 the second hash.
	 * @return a new hash set.
	 * @since 2.20.0
	 */
	@SuppressWarnings("unchecked")
	public static <T, H extends Hash<T>> H xor(H set1, H set2)
	{
		Hash<T> out = new Hash<T>();
		for (T val : set1)
		{
			if (!set2.contains(val))
				out.put(val);
		}
		for (T val : set2)
		{
			if (!set1.contains(val))
				out.put(val);
		}
		return (H)out;
	}

	@Override
	public void toArray(T[] out) 
	{
		int i = 0;
		for (T value : this)
			out[i++] = value;
	}
	
}
